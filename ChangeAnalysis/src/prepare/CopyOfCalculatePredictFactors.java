package prepare;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;

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
 * 读入source的sheet2
 * @author Administrator
 *
 */
public class CopyOfCalculatePredictFactors {

    /**
     * @param args
     */
    static final int BEGIN = 1; //V1的列
    static final int END = 13; //Vmax的列
    static final int OFFSET = 1; //从V0+offset+1开始，计算model，所以一共会产生Vmax-V0-offset个model
    static final int[] numberOfUC = {186,189,195,221,276,310,303,339,360,394,394,426,452,461};//V0-Vmax的用例数
    private static final int UCNAME = 0;
    public static void main(String[] args) {

	String source = "I:\\Data\\ChangeAnalysis\\ChangeMetrix_14v.xls";
	String target = "I:\\Data\\ChangeAnalysis\\FV Train Set 11.xls";

	readAndCalculate(source, target);

    }

    private static void readAndCalculate(String source, String target) {
	HashMap<Double,Double>  freq = new HashMap<Double,Double>  ();
	HashMap<Double,Double>  distance = new HashMap<Double,Double> ();
	HashMap<Double,Double>  life = new HashMap<Double,Double> (); // UC存在的版本编号
	HashMap<Double,Double>  fv = new HashMap<Double,Double> ();
	HashMap<Double,Double> seq = new HashMap<Double,Double> ();
	

	// 输入的文档
	InputStream is = null;
	try {
	    is = new FileInputStream(source);
	} catch (FileNotFoundException e1) {
	    e1.printStackTrace();
	}

	Workbook wb = null;
	try {
	    // 构造Workbook（工作薄）对象
	    wb = Workbook.getWorkbook(is);
	} catch (BiffException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	// 输出的文档
	WritableWorkbook wwb = null;
	try {
	    // 首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象
	    wwb = Workbook.createWorkbook(new File(target));
	} catch (IOException e) {
	    e.printStackTrace();
	}
	if (wwb == null)
	    return;

	if (wb == null)
	    return;

	Sheet srcSheet = wb.getSheet("Sheet1");
	int nRow = srcSheet.getRows();

	if (srcSheet != null) {

	    Cell grid;

	    // 逐列读取 (3-8)
	    for (int currColumn = BEGIN+OFFSET; currColumn <= END; currColumn++) { //evolution = [3,8], E2-E7, evolution - 1 = version
		
		LinkedList<Double> usecases = new LinkedList<Double>();
		
		//TODO 
		double minOccur = END-BEGIN+1; 
		double maxOccur = 0;
		
		//将当前Evolution的需求读入usecase列表,里面存的是当前版本中发生修改的用例的行号
		//遍历当前Evolution的需求列表,row
		for (int row = 1; row < nRow; row++){
		    
		    boolean isDel = false;
		    boolean isAdd = false;
		    boolean isFirst = true;
		    
		    
		    //判断这一行是否为删除的需求
		    for (int t = BEGIN; t <= currColumn; t++){
			grid = srcSheet.getCell(t,row);
			if (grid.getContents().equals("-1"))
			    isDel = true;
			
		    }
		    
		    //判断这一行是否是后期新增的需求
		    for (int t = currColumn +1; t <= END; t++){ 
			grid = srcSheet.getCell(t,row);
			if (grid.getContents().equals("2"))
			    isAdd = true;
		    }
		    
		    if(!isDel&&!isAdd){
			usecases.add(new Double(row));
			
			Cell c = srcSheet.getCell(2,row);//用例名字
			System.out.println(c.getContents());
			
			
			    //计算当前版本每个用例的寿命 life
			    for (int t = BEGIN; t <= currColumn; t++){ // t = [3,evolution]
				grid = srcSheet.getCell(t,row);
				if (grid.getContents().equals("2")){
				    life.put(row+0.0, currColumn-t+0.0);
				    isFirst = false;
				    }
			    }
			    if(isFirst)
				life.put(row+0.0, currColumn-BEGIN-1.0); //第一个版本中新增的
			    
			    //计算当前版本的连续变更的次数
			    int sequence = 0;
			    int last = 0;
			    
			    for (int t = BEGIN; t <= currColumn; t++){ // t = [3,evolution]
				grid = srcSheet.getCell(t,row);
				String cont = grid.getContents();
				
				if(cont.equals("2")||cont.equals("1")){
				    if(last == t-1)
					sequence++;
				    else
					sequence = 1;
				    
				    last = t;
				}
			    }
			    seq.put(row+0.0, sequence/(currColumn-2.0)); //
			}
		}

		// 读取前几个版本的值
		for (int prev = BEGIN; prev <= currColumn; prev++) {

		    // 逐行读取每个版本的用例状态
		    for (Double row : usecases) {
			
			grid = srcSheet.getCell(prev, row.intValue());

			// 计算freq, occur
			// Cell中的值为1或者2时，Freq++， Occur++
			if (grid.getContents().equals("1")||grid.getContents().equals("2")) {//||grid.getContents().equals("2")
			    Double d = freq.get(row);
			    
			    if(d == null)
				d = 0.0;
			    d++;
			    freq.put(row, d);

			    Double tmp = distance.get(row);
			    if(tmp == null)
				tmp = 0.0;
			    
			    distance.put(row, tmp+ currColumn - prev);

			}
			// Cell 中的值为0，不管
		    }
		}
		

		// 整理occur
		for (Double row : usecases) {

		    if (freq.get(row) != null) {
			distance.put(row,currColumn - 1 - (distance.get(row) / freq.get(row)));
			
			if(minOccur > distance.get(row))
			    minOccur = distance.get(row);
			if(maxOccur < distance.get(row))
			    maxOccur = distance.get(row);
			}
		    else{
			Cell c = srcSheet.getCell(2,row.intValue());
			//System.out.println(c.getContents());
		    }
			
		}

		// 数据归一化
		
		for (Double row : usecases) {
		    if (freq.get(row) != null) {
		    Double b = freq.get(row);
		    freq.put(row, b /(currColumn ));}
		}
		
		for (Double row : usecases) {
		    if (distance.get(row) != null) {
		    Double b = distance.get(row);
		    distance.put(row, (b-minOccur) / (maxOccur-minOccur));}
		}

		// 计算FV
		
		for (Double row : usecases) {
		    if(currColumn <END){
		    grid = srcSheet.getCell(currColumn + 1, row.intValue());
		    // 当新版本中为修改的状态时，才为1
		    if (grid.getContents().equals("1"))
			fv.put(row, 1.0);
		    else
			fv.put(row, 0.0);
		    }
		    else{
			    fv.put(row, 0.0);
			}
		}
		

		// 输出
		System.out.println("\nWriting to Excel Evolution "+ Integer.toString(currColumn - 1));
		WritableSheet ws = wwb.createSheet("Model "
			+ Integer.toString(currColumn - 1), 0);

		// 写表头
		try {
		    ws.addCell(new Label(0, 0, "Module"));
		    ws.addCell(new Label(1, 0, "UC_ID"));
		    ws.addCell(new Label(2, 0, "UC_Name"));
		    ws.addCell(new Label(3, 0, "Frequency"));
		    ws.addCell(new Label(4, 0, "Occurence"));
		    ws.addCell(new Label(5, 0, "Lifecycle"));
		    ws.addCell(new Label(6, 0, "O/L"));
		    ws.addCell(new Label(7, 0, "Sequence"));
		    ws.addCell(new Label(8, 0, "FV_Actual"));
		    
		} catch (RowsExceededException e1) {
		    e1.printStackTrace();
		} catch (WriteException e1) {
		    e1.printStackTrace();
		}

		int currRow = 1;
		try {
		    for (Double j : usecases){
			//ws.addCell(new jxl.write.Label(0, currRow, srcSheet.getCell(0, j.intValue()).getContents()));
			//ws.addCell(new jxl.write.Label(1, currRow, srcSheet.getCell(1, j.intValue()).getContents()));
			ws.addCell(new jxl.write.Label(2, currRow, srcSheet.getCell(UCNAME, j.intValue()).getContents()));
			
			Double value = freq.get(j)==null?0:freq.get(j);
			Double value2 ;
			ws.addCell(new jxl.write.Number(3, currRow, value));
			
			value = distance.get(j)==null?0:distance.get(j);
			ws.addCell(new jxl.write.Number(4, currRow, value));
			
			value2 = life.get(j)==null?0:life.get(j);
			ws.addCell(new jxl.write.Number(5, currRow, value2));
			
			value2 = (value2==0?0:value/value2);
			ws.addCell(new jxl.write.Number(6, currRow, value2));
			
			value = seq.get(j)==null?0:seq.get(j);
			ws.addCell(new jxl.write.Number(7, currRow, value));			
			
			value = fv.get(j)==null?0:fv.get(j);
			ws.addCell(new jxl.write.Number(8, currRow, value));
			
			currRow++;
		    }
		    
		    for(;currRow < numberOfUC[currColumn - 1 ]+1;currRow++ ){
			ws.addCell(new jxl.write.Number(3, currRow, 0));
			ws.addCell(new jxl.write.Number(4, currRow, 0));
			ws.addCell(new jxl.write.Number(5, currRow, 1));
			ws.addCell(new jxl.write.Number(6, currRow, 0));
			ws.addCell(new jxl.write.Number(7, currRow, 0));
			ws.addCell(new jxl.write.Number(8, currRow, 0));
		    }

		    freq.clear();
		    distance.clear();
		    fv.clear();
		    life.clear();
		    minOccur = END-BEGIN+1;
		    maxOccur = 0;

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
}
