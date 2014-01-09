package train;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import text.TextSimilarity;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * 第三步 读取演化矩阵，计算预测因子的值 读入source的sheet2
 * 
 * @author Administrator
 * 
 */
public class CalculatePredictFactors {

    /**
     * @param args
     */

    static final int BEGIN = 2; // V1的列
    static final int END = 13; // Vmax的列
    static final int OFFSET = 0; // 从V1+offset+1开始，计算model，所以一共会产生Vmax-V1-offset个model

    // static final int[] numberOfUC =
    // {186,189,195,221,276,310,303,339,360,394,394,426,452,461};//V0-Vmax的用例数
    static final int UCNAME = 1;
    static final int MODULE = 0;
    static final int DATASET = 13; // dataset的列号
    static final int FISRTMODEL = 2;
    static final int LASTMODEL = 10;

    static String[] modules = new String[] { "Change management",
	    "Measure management", "Process management", "Product management",
	    "Project management", "Quality management", "Risk management",
	    "System management", "Task management", "Test management" };
    int[][][] volatility = new int[END - BEGIN + 1][modules.length][2];

    HashMap<Double, Double> freq = new HashMap<Double, Double>();
    HashMap<Double, Double> distance = new HashMap<Double, Double>();
    HashMap<Double, Double> occurence = new HashMap<Double, Double>();
    HashMap<Double, Double> life = new HashMap<Double, Double>(); // UC存在的版本编号
    HashMap<Double, Double> lastDistance = new HashMap<Double, Double>();
    HashMap<Double, Double> fv = new HashMap<Double, Double>();
    HashMap<Double, Double> seq = new HashMap<Double, Double>();
    HashMap<Double, Double> topicSimilarity = new HashMap<Double,Double>();
    HashMap<Double, Double> topicContain = new HashMap<Double, Double>();
    
    HashMap<Double, String> topicMaps = new HashMap<Double,String>();

    void readVolatility(Sheet srcSheet) {
	Cell grid;

	boolean isDel = false;
	boolean isAdd = false;
	String cont;

	// 读取每一行，计算当前列的模块挥发度
	for (int row = 1; row < srcSheet.getRows(); row++) {

	    for (int moduleIndex = BEGIN; moduleIndex <= END; moduleIndex++) {
		isDel = false;
		isAdd = false;

		// 去掉删除的和新增的

		// 判断这一行是否为删除的需求
		for (int t = BEGIN; t <= moduleIndex; t++) {
		    cont = srcSheet.getCell(t, row).getContents();
		    if (cont.equals("-1")) {
			isDel = true;
			break;
		    }
		}

		// 判断这一行是否是后期新增的需求
		for (int t = moduleIndex + 1; t <= END; t++) {
		    cont = srcSheet.getCell(t, row).getContents();
		    if (cont.equals("2")) {
			isAdd = true;
			break;
		    }
		}

		if ((!isDel) && (!isAdd)) {

		    grid = srcSheet.getCell(0, row);
		    cont = grid.getContents();

		    // 查找module的index
		    int moduleLocation = getModuleIndex(cont);

		    // 总数上+1

		    cont = srcSheet.getCell(moduleIndex, row).getContents();
		    int tmp = volatility[moduleIndex - BEGIN][moduleLocation][0];
		    volatility[moduleIndex - BEGIN][moduleLocation][0] = tmp + 1;

		    if (cont.equals("1") || cont.equals("2")) {
			tmp = volatility[moduleIndex - BEGIN][moduleLocation][1];
			volatility[moduleIndex - BEGIN][moduleLocation][1] = tmp + 1;
		    }
		}
	    }
	}

	for (int i = 0; i < END - BEGIN; i++) {
	    System.out.print("Version = " + i);

	    for (int k = 0; k < modules.length; k++) {
		System.out.print(" ModuleIndex = " + k + " Total = "
			+ volatility[i][k][0] + " Changes = "
			+ volatility[i][k][1] + "\n");
	    }

	}
   }
    
    protected void readAndCalculate(String source, String target) throws BiffException, IOException, RowsExceededException, WriteException {

	// 输入的文档
	InputStream is = null;
	is = new FileInputStream(source);
	Workbook wb = null;
	wb = Workbook.getWorkbook(is);

	// 输出的文档
	WritableWorkbook wwb = null;
	wwb = Workbook.createWorkbook(new File(target)); // 首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象
	
	if (wwb == null || wb == null) 
	    return;

	Sheet srcSheet = wb.getSheet("Sheet1");
	int nRow = srcSheet.getRows();
	
	Sheet topicMaps = wb.getSheet("maps");
	
	initTopicMaps(topicMaps);


	if (srcSheet != null) {
	    Cell grid;
	    boolean isDel = false;
	    boolean isAdd = false;

	    String cont;

	    // 读取每一行，计算当前列的模块挥发度
	    readVolatility(srcSheet);

	    // 逐列读取 (3-8)
	    for (int currColumn = BEGIN + OFFSET; currColumn <= END; currColumn++) { // evolution
										     // =
										     // [3,8],
										     // E2-E7,
										     // evolution
										     // -
										     // 1
										     // =
										     // version
		
		freq = new HashMap<Double, Double>();
		distance = new HashMap<Double, Double>();
		occurence = new HashMap<Double, Double>();
		life = new HashMap<Double, Double>(); // UC存在的版本编号
		lastDistance = new HashMap<Double, Double>();
		fv = new HashMap<Double, Double>();
		seq = new HashMap<Double, Double>();
		topicSimilarity = new HashMap<Double, Double>();
		topicContain = new HashMap<Double, Double>();
		
		LinkedList<Double> usecases = new LinkedList<Double>();

		// 将当前Evolution的需求读入usecase列表,里面存的是当前版本中发生修改的用例的行号
		// 遍历当前Evolution的需求列表,row
		for (int row = 1; row < nRow; row++) {

		    isDel = false;
		    isAdd = false;
		    boolean isFirst = true;

		    int lastChange = BEGIN;

		    // 判断这一行是否为删除的需求
		    for (int t = BEGIN; t <= currColumn; t++) {
			grid = srcSheet.getCell(t, row);
			if (grid.getContents().equals("-1"))
			    isDel = true;

		    }

		    // 判断这一行是否是后期新增的需求
		    for (int t = currColumn + 1; t <= END; t++) {
			grid = srcSheet.getCell(t, row);
			if (grid.getContents().equals("2"))
			    isAdd = true;
		    }

		    if (!isDel && !isAdd) {
			usecases.add(new Double(row));

			Cell c = srcSheet.getCell(2, row);// 用例名字

			// 计算当前版本每个用例的寿命 life
			for (int t = BEGIN; t <= currColumn; t++) { // t =
								    // [3,evolution]
			    grid = srcSheet.getCell(t, row);
			    if (grid.getContents().equals("2")) {
				life.put(row + 0.0, currColumn - t + 1.0);
				isFirst = false;
			    }
			}
			if (isFirst)
			    life.put(row + 0.0, currColumn - BEGIN + 2.0); // 第一个版本中新增的

			// 计算当前版本的连续变更的次数
			int sequence = 0;
			int last = 0;

			for (int t = BEGIN; t <= currColumn; t++) { // t =
								    // [3,evolution]
			    grid = srcSheet.getCell(t, row);
			    cont = grid.getContents();

			    if (cont.equals("2") || cont.equals("1")) {
				if (last == t - 1)
				    sequence++;
				else
				    sequence = 1;

				last = t;

				lastChange = t;
			    }
			}
			if (currColumn == 2)
			    seq.put(row + 0.0, 0.0);
			else
			    seq.put(row + 0.0, sequence / (currColumn - 2.0));
			lastDistance.put(row + 0.0, currColumn - lastChange
				+ 0.0);
		    }
		}

		// 读取前几个版本的值
		for (int prev = BEGIN; prev <= currColumn; prev++) {

		    // 逐行读取每个版本的用例状态
		    for (Double row : usecases) {

			grid = srcSheet.getCell(prev, row.intValue());

			// 计算freq, occur
			// Cell中的值为1或者2时，Freq++， Occur++
			if (grid.getContents().equals("1")
				|| grid.getContents().equals("2")) {// ||grid.getContents().equals("2")
			    Double d = freq.get(row);

			    if (d == null)
				d = 0.0;
			    d++;
			    freq.put(row, d);

			    Double tmp = distance.get(row);
			    if (tmp == null)
				tmp = 0.0;

			    distance.put(row, tmp + currColumn - prev);

			}
			// Cell 中的值为0，不管
		    }
		}

		// 数据归一化

		for (Double row : usecases) {
		    if (freq.get(row) != null) {
			Double b = freq.get(row);
			freq.put(row, b / (currColumn - 1));
		    }
		}

		// 整理occur
		for (Double row : usecases) {

		    if (freq.get(row) != null) {
			occurence.put(row, distance.get(row)
				/ (freq.get(row) * life.get(row)));
		    }

		}

		// 计算FV

		for (Double row : usecases) {
		    if (currColumn < END) {
			grid = srcSheet.getCell(currColumn + 1, row.intValue());
			// 当新版本中为修改的状态时，才为1
			if (grid.getContents().equals("1"))
			    fv.put(row, 1.0);
			else
			    fv.put(row, 0.0);
		    } else {
			fv.put(row, 0.0);
		    }
		}
		
		//计算 topic
		for (Double row : usecases) {
		    String ucName = srcSheet.getCell(UCNAME,row.intValue()).getContents();
		    String modelName = srcSheet.getCell(MODULE,row.intValue()).getContents();
		    Double topicS = getTopicSimilarity(ucName,modelName,currColumn);
		    Double topicC = getTopicContain(ucName,modelName,currColumn);
		    topicSimilarity.put(row, topicS);
		    topicContain.put(row, topicC);
		}
		

		// 输出
		System.out.println("\nWriting to Excel Evolution "
			+ Integer.toString(currColumn - 1));
		WritableSheet ws = wwb.createSheet("Model "
			+ Integer.toString(currColumn - 1), 0);

		// 写表头
		ws.addCell(new Label(0, 0, "UC_Name"));
		ws.addCell(new Label(1, 0, "Module"));
		ws.addCell(new Label(2, 0, "ModuleVolality"));
		ws.addCell(new Label(3, 0, "ModuleVolalityAVG"));
		ws.addCell(new Label(4, 0, "Frequency"));
		ws.addCell(new Label(5, 0, "Distance"));
		ws.addCell(new Label(6, 0, "Lifecycle"));
		ws.addCell(new Label(7, 0, "Occurence"));
		ws.addCell(new Label(8, 0, "Sequence"));
		ws.addCell(new Label(9, 0, "LastChange"));
		ws.addCell(new Label(10, 0, "TopicSimilariy"));
		ws.addCell(new Label(11, 0, "TopicContain"));
		ws.addCell(new Label(12, 0, "FV_Actual"));
		ws.addCell(new Label(13, 0, "Dataset"));


		int currRow = 1;
		Double value;
		String moduleWrite;
		try {

		    for (Double j : usecases) {
			ws.addCell(new jxl.write.Label(0, currRow, srcSheet
				.getCell(UCNAME, j.intValue()).getContents()));

			moduleWrite = srcSheet.getCell(MODULE, j.intValue())
				.getContents();
			ws
				.addCell(new jxl.write.Label(1, currRow,
					moduleWrite));
			ws.addCell(new jxl.write.Number(DATASET, currRow,
				currColumn - 1));

			value = getModuleVolatility(moduleWrite, currColumn);
			ws.addCell(new jxl.write.Number(2, currRow, value));

			value = getModuleVolatilityAVG(moduleWrite, currColumn);
			ws.addCell(new jxl.write.Number(3, currRow, value));

			value = freq.get(j) == null ? 0 : freq.get(j);
			Double value2;
			ws.addCell(new jxl.write.Number(4, currRow, value));

			value = distance.get(j) == null ? 0 : distance.get(j);
			ws.addCell(new jxl.write.Number(5, currRow, value));

			value2 = life.get(j) == null ? 0 : life.get(j);
			ws.addCell(new jxl.write.Number(6, currRow, value2));

			value2 = occurence.get(j) == null ? 0 : occurence
				.get(j);
			ws.addCell(new jxl.write.Number(7, currRow, value2));

			value = seq.get(j) == null ? 0 : seq.get(j);
			ws.addCell(new jxl.write.Number(8, currRow, value));

			value = lastDistance.get(j) == null ? 0 : lastDistance
				.get(j);
			ws.addCell(new jxl.write.Number(9, currRow, value));

			value = fv.get(j) == null ? 0 : fv.get(j);
			ws.addCell(new jxl.write.Number(12, currRow, value));
			
			value = topicSimilarity.get(j) == null ? 0:topicSimilarity.get(j);
			ws.addCell(new jxl.write.Number(10, currRow, value));
			
			value = topicContain.get(j) == null ? 0:topicContain.get(j);
			ws.addCell(new jxl.write.Number(11, currRow, value));

			currRow++;
		    }

		    // for(;currRow < numberOfUC[currColumn - 1 ]+1;currRow++ ){
		    // ws.addCell(new jxl.write.Number(3, currRow, 0));
		    // ws.addCell(new jxl.write.Number(4, currRow, 0));
		    // ws.addCell(new jxl.write.Number(5, currRow, 1));
		    // ws.addCell(new jxl.write.Number(6, currRow, 0));
		    // ws.addCell(new jxl.write.Number(7, currRow, 0));
		    // ws.addCell(new jxl.write.Number(8, currRow, 0));
		    // }

		    freq.clear();
		    distance.clear();
		    fv.clear();
		    life.clear();

		} catch (RowsExceededException e) {
		    e.printStackTrace();
		} catch (WriteException e) {
		    e.printStackTrace();
		}
	    }
	}

	wb.close();

	// 写入 Excel 对象
	try {
	    wwb.write();
	    // 关闭可写入的 Excel 对象
	    wwb.close();
	    // 关闭只读的 Excel 对象
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (WriteException e) {
	    e.printStackTrace();
	}
    }

    private void initTopicMaps(Sheet maps) {
	int rows = maps.getRows();
	for(int i = 1; i < rows; i++){
	    String topics = maps.getCell(0, i).getContents();
	    String current = maps.getCell(1, i).getContents();
	    if(!topics.isEmpty() && !current.isEmpty())
		topicMaps.put(Double.valueOf(current), topics);
	}
    }

    private Double getTopicContain(String ucName, String modelName,
	    int currColumn) {
	String changeModel = null;
	boolean nameContain = false;
	boolean modelContain = false;
	int end = 0;
	
	String topics = topicMaps.get(new Double(currColumn));
	if(topics==null)
	    return 0.0;
	
	String[] topicsplit = topics.split(",");
	
	for(String topic : topicsplit){
	    String[] terms = topic.trim().split(" ");
	    end = terms.length-1;
	    
	    Pattern pattern = Pattern.compile("[a-zA-Z]+");
	    Matcher match = pattern.matcher(terms[terms.length-1]);
	    
	    if(match.matches()){
		changeModel = terms[terms.length-1];
		end = terms.length-2;
		if(modelName.contains(changeModel))
		    modelContain = true;
	    }
	    else
		 modelContain = true;
	    
	    for(int i = 0; i<= end; i++){
		if(ucName.contains(terms[i]))
		    nameContain = true;
	    }
	    
	    if(nameContain && modelContain){
		System.out.println("Find a match: <"+ucName+ modelName+"> to <"+topic+">");
		return 1.0;
		}

	}
	return 0.0;
    }

    private Double getTopicSimilarity(String ucName, String modelName,
	    int currColumn) {
	String changeModel = null;
	int end = 0;
	float similarName =0;
	int similarModel = 0;
	
	String topics = topicMaps.get(new Double(currColumn));
	if(topics==null)
	    return 0.0;
	
	String[] topicsplit = topics.split(",");
	
	for(String topic : topicsplit){
	    String[] terms = topic.trim().split(" ");
	    end = terms.length-1;
	    
	    Pattern pattern = Pattern.compile("[a-zA-Z]+");
	    Matcher match = pattern.matcher(terms[terms.length-1]);
	    
	    if(match.matches()){
		changeModel = terms[terms.length-1];
		end = terms.length-2;
	    }
	    
	    for(int i = 0; i<= end; i++){
		similarName += TextSimilarity.getSimilarity(ucName, terms[i])/(end+1);
	    }
	    
	    if(changeModel !=null && modelName.contains(changeModel))
		similarModel = 1;

	}
	return similarName+similarModel+0.0;
    }

    private Double getModuleVolatility(String moduleWrite, int currColumn) {
	int total = volatility[currColumn - BEGIN][getModuleIndex(moduleWrite)][0];
	double changes = volatility[currColumn - BEGIN][getModuleIndex(moduleWrite)][1];
	if (total == 0)
	    return new Double(0);
	return new Double(changes / total);
    }

    private Double getModuleVolatilityAVG(String moduleWrite, int currColumn) {
	double sum = 0;

	for (int i = BEGIN; i <= currColumn; i++) {
	    sum += getModuleVolatility(moduleWrite, i).doubleValue();
	}

	return new Double(sum / (currColumn - BEGIN + 1));
    }

    private static int getModuleIndex(String module) {
	for (int i = 0; i < modules.length; i++) {
	    if (module.equalsIgnoreCase(modules[i]))
		return i;
	}

	System.out.println("怎么找不到模块呢？" + module);
	return -1;
    }
}
