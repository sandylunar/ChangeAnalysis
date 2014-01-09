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

public class CalFocusValueTrainSetWithThreeParam {

    /**
     * @param args
     */
    static final int[] numberOfUC = {186,189,195,221,276,310,303,339};
    public static void main(String[] args) {

	String source = "I:\\Data\\ChangeAnalysis\\UseCase Change Status2.xls";
	String target = "I:\\Data\\ChangeAnalysis\\FV Train Set with New Normalize3.xls";

	readAndCalculate(source, target);

    }

    private static void readAndCalculate(String source, String target) {
	HashMap<Double,Double>  freq = new HashMap<Double,Double>  ();
	HashMap<Double,Double>  occur = new HashMap<Double,Double> ();
	HashMap<Double,Double>  life = new HashMap<Double,Double> (); // UC存在的版本编号：[2-8]
	HashMap<Double,Double>  fv = new HashMap<Double,Double> ();
	

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

	Sheet sheet = wb.getSheet("Sheet2");

	if (sheet != null) {

	    Cell grid;

	    // 逐列读取 (3-8)
	    for (int evolution = 3; evolution <= 8; evolution++) { //evolution = [3,8], E2-E7, evolution - 1 = version
		
		LinkedList<Double> usecases = new LinkedList<Double>();
		double minOccur = 7;
		double maxOccur = 0;
		
		 //将当前Evolution的需求读入usecase列表,里面存的是当前版本中用例的行号
		for (int row = 1; row <= 322; row++){
		    
		    boolean isDel = false;
		    boolean isAdd = false;
		    boolean isFirst = true;
		    
		    
		    //排除删除的
		    for (int t = 3; t <= evolution; t++){
			grid = sheet.getCell(t,row);
			if (grid.getContents().equals("-1"))
			    isDel = true;
			
		    }
		    
		    //排除新增的
		    for (int t = evolution +1; t <= 9; t++){
			grid = sheet.getCell(t,row);
			if (grid.getContents().equals("2"))
			    isAdd = true;
		    }
		    
		    
		    
		    if(!isDel&&!isAdd){
			usecases.add(new Double(row));
			
			    //计算当前版本每个用例的寿命 life
			
			    for (int t = 3; t <= evolution; t++){ // t = [3,evolution]
				grid = sheet.getCell(t,row);
				if (grid.getContents().equals("2")){
				    life.put(row+0.0, evolution+1.0-t);
				    isFirst = false;
				    }
			    }
			    if(isFirst)
				life.put(row+0.0, evolution-1.0);
			
			}
		}

		// 读取前几个版本的值
		for (int prev = 3; prev <= evolution; prev++) {

		    // 逐行读取每个版本的用例状态
		    for (Double row : usecases) {
			
			grid = sheet.getCell(prev, row.intValue());

			// 计算freq, occur
			// Cell中的值为1或者2时，Freq++， Occur++
			if (grid.getContents().equals("1")||grid.getContents().equals("2")) {
	
			    
			    Double d = freq.get(row);
			    
			    if(d == null)
				d = 0.0;
			    d++;
			    freq.put(row, d);

			    Double tmp = occur.get(row);
			    if(tmp == null)
				tmp = 0.0;
			    
			    occur.put(row, tmp + prev - 2);

			}
			// Cell 中的值为0，不管
		    }
		}
		

		// 整理occur
		for (Double row : usecases) {

		    if (freq.get(row) != null) {
			occur.put(row,evolution - 1 - (occur.get(row) / freq.get(row)));
			
			if(minOccur > occur.get(row))
			    minOccur = occur.get(row);
			if(maxOccur < occur.get(row))
			    maxOccur = occur.get(row);
			}
		    else{
			Cell c = sheet.getCell(2,row.intValue());
			System.out.println(c.getContents());
		    }
			
		}

		// 数据归一化
		
		for (Double row : usecases) {
		    if (freq.get(row) != null) {
		    Double b = freq.get(row);
		    freq.put(row, b /(evolution ));}
		}
		
		for (Double row : usecases) {
		    if (occur.get(row) != null) {
		    Double b = occur.get(row);
		    occur.put(row, (b-minOccur) / (maxOccur-minOccur));}
		}

		// 计算FV
		for (Double row : usecases) {
		    grid = sheet.getCell(evolution + 1, row.intValue());
		    // 当新版本中为修改的状态时，才为1
		    if (grid.getContents().equals("1"))
			fv.put(row, 1.0);
		    else
			fv.put(row, 0.0);
		}

		// 输出
		System.out.println("\nWriting to Excel Evolution "+ Integer.toString(evolution - 1));
		WritableSheet ws = wwb.createSheet("Evolution "
			+ Integer.toString(evolution - 1), 0);

		// 写表头
		try {
		    ws.addCell(new Label(0, 0, "Frequency"));
		    ws.addCell(new Label(1, 0, "Occurence"));
		    ws.addCell(new Label(2, 0, "Lifecycle"));
		    ws.addCell(new Label(3, 0, "FV_Actual"));
		    
		} catch (RowsExceededException e1) {
		    e1.printStackTrace();
		} catch (WriteException e1) {
		    e1.printStackTrace();
		}

		int currRow = 1;
		try {
		    for (Double j : usecases){
			
			Double value = freq.get(j)==null?0:freq.get(j);
			ws.addCell(new jxl.write.Number(0, currRow, value));
			
			value = occur.get(j)==null?0:occur.get(j);
			ws.addCell(new jxl.write.Number(1, currRow, value));
			
			value = life.get(j)==null?0:life.get(j);
			ws.addCell(new jxl.write.Number(2, currRow, value));
			
			value = fv.get(j)==null?0:fv.get(j);
			ws.addCell(new jxl.write.Number(3, currRow, value));
			
			
			currRow++;
		    }
		    
		    for(;currRow < numberOfUC[evolution - 2]+1;currRow++ ){
			ws.addCell(new jxl.write.Number(0, currRow, 0));
			ws.addCell(new jxl.write.Number(1, currRow, 0));
			ws.addCell(new jxl.write.Number(2, currRow, evolution-1));
			ws.addCell(new jxl.write.Number(3, currRow, 0));
		    }

		    freq.clear();
		    occur.clear();
		    fv.clear();
		    life.clear();
		    minOccur = 7;
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
