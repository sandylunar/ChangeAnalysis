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
 * ������ ��ȡ�ݻ����󣬼���Ԥ�����ӵ�ֵ ����source��sheet2
 * 
 * @author Administrator
 * 
 */
public class CalculatePredictFactors {

    /**
     * @param args
     */

    static final int BEGIN = 2; // V1����
    static final int END = 13; // Vmax����
    static final int OFFSET = 0; // ��V1+offset+1��ʼ������model������һ�������Vmax-V1-offset��model

    // static final int[] numberOfUC =
    // {186,189,195,221,276,310,303,339,360,394,394,426,452,461};//V0-Vmax��������
    static final int UCNAME = 1;
    static final int MODULE = 0;
    static final int DATASET = 13; // dataset���к�
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
    HashMap<Double, Double> life = new HashMap<Double, Double>(); // UC���ڵİ汾���
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

	// ��ȡÿһ�У����㵱ǰ�е�ģ��ӷ���
	for (int row = 1; row < srcSheet.getRows(); row++) {

	    for (int moduleIndex = BEGIN; moduleIndex <= END; moduleIndex++) {
		isDel = false;
		isAdd = false;

		// ȥ��ɾ���ĺ�������

		// �ж���һ���Ƿ�Ϊɾ��������
		for (int t = BEGIN; t <= moduleIndex; t++) {
		    cont = srcSheet.getCell(t, row).getContents();
		    if (cont.equals("-1")) {
			isDel = true;
			break;
		    }
		}

		// �ж���һ���Ƿ��Ǻ�������������
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

		    // ����module��index
		    int moduleLocation = getModuleIndex(cont);

		    // ������+1

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

	// ������ĵ�
	InputStream is = null;
	is = new FileInputStream(source);
	Workbook wb = null;
	wb = Workbook.getWorkbook(is);

	// ������ĵ�
	WritableWorkbook wwb = null;
	wwb = Workbook.createWorkbook(new File(target)); // ����Ҫʹ��Workbook��Ĺ�����������һ����д��Ĺ�����(Workbook)����
	
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

	    // ��ȡÿһ�У����㵱ǰ�е�ģ��ӷ���
	    readVolatility(srcSheet);

	    // ���ж�ȡ (3-8)
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
		life = new HashMap<Double, Double>(); // UC���ڵİ汾���
		lastDistance = new HashMap<Double, Double>();
		fv = new HashMap<Double, Double>();
		seq = new HashMap<Double, Double>();
		topicSimilarity = new HashMap<Double, Double>();
		topicContain = new HashMap<Double, Double>();
		
		LinkedList<Double> usecases = new LinkedList<Double>();

		// ����ǰEvolution���������usecase�б�,�������ǵ�ǰ�汾�з����޸ĵ��������к�
		// ������ǰEvolution�������б�,row
		for (int row = 1; row < nRow; row++) {

		    isDel = false;
		    isAdd = false;
		    boolean isFirst = true;

		    int lastChange = BEGIN;

		    // �ж���һ���Ƿ�Ϊɾ��������
		    for (int t = BEGIN; t <= currColumn; t++) {
			grid = srcSheet.getCell(t, row);
			if (grid.getContents().equals("-1"))
			    isDel = true;

		    }

		    // �ж���һ���Ƿ��Ǻ�������������
		    for (int t = currColumn + 1; t <= END; t++) {
			grid = srcSheet.getCell(t, row);
			if (grid.getContents().equals("2"))
			    isAdd = true;
		    }

		    if (!isDel && !isAdd) {
			usecases.add(new Double(row));

			Cell c = srcSheet.getCell(2, row);// ��������

			// ���㵱ǰ�汾ÿ������������ life
			for (int t = BEGIN; t <= currColumn; t++) { // t =
								    // [3,evolution]
			    grid = srcSheet.getCell(t, row);
			    if (grid.getContents().equals("2")) {
				life.put(row + 0.0, currColumn - t + 1.0);
				isFirst = false;
			    }
			}
			if (isFirst)
			    life.put(row + 0.0, currColumn - BEGIN + 2.0); // ��һ���汾��������

			// ���㵱ǰ�汾����������Ĵ���
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

		// ��ȡǰ�����汾��ֵ
		for (int prev = BEGIN; prev <= currColumn; prev++) {

		    // ���ж�ȡÿ���汾������״̬
		    for (Double row : usecases) {

			grid = srcSheet.getCell(prev, row.intValue());

			// ����freq, occur
			// Cell�е�ֵΪ1����2ʱ��Freq++�� Occur++
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
			// Cell �е�ֵΪ0������
		    }
		}

		// ���ݹ�һ��

		for (Double row : usecases) {
		    if (freq.get(row) != null) {
			Double b = freq.get(row);
			freq.put(row, b / (currColumn - 1));
		    }
		}

		// ����occur
		for (Double row : usecases) {

		    if (freq.get(row) != null) {
			occurence.put(row, distance.get(row)
				/ (freq.get(row) * life.get(row)));
		    }

		}

		// ����FV

		for (Double row : usecases) {
		    if (currColumn < END) {
			grid = srcSheet.getCell(currColumn + 1, row.intValue());
			// ���°汾��Ϊ�޸ĵ�״̬ʱ����Ϊ1
			if (grid.getContents().equals("1"))
			    fv.put(row, 1.0);
			else
			    fv.put(row, 0.0);
		    } else {
			fv.put(row, 0.0);
		    }
		}
		
		//���� topic
		for (Double row : usecases) {
		    String ucName = srcSheet.getCell(UCNAME,row.intValue()).getContents();
		    String modelName = srcSheet.getCell(MODULE,row.intValue()).getContents();
		    Double topicS = getTopicSimilarity(ucName,modelName,currColumn);
		    Double topicC = getTopicContain(ucName,modelName,currColumn);
		    topicSimilarity.put(row, topicS);
		    topicContain.put(row, topicC);
		}
		

		// ���
		System.out.println("\nWriting to Excel Evolution "
			+ Integer.toString(currColumn - 1));
		WritableSheet ws = wwb.createSheet("Model "
			+ Integer.toString(currColumn - 1), 0);

		// д��ͷ
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

	// д�� Excel ����
	try {
	    wwb.write();
	    // �رտ�д��� Excel ����
	    wwb.close();
	    // �ر�ֻ���� Excel ����
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

	System.out.println("��ô�Ҳ���ģ���أ�" + module);
	return -1;
    }
}
