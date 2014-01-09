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
 * ����source��sheet2
 * @author Administrator
 *
 */
public class CalFocusValueTrainSetWithFourParamN {

    /**
     * @param args
     */
    static final int[] numberOfUC = {186,189,195,221,276,310,303,339};
    public static void main(String[] args) {

	String source = "I:\\Data\\ChangeAnalysis\\UseCase Change Status2.xls";
	String target = "I:\\Data\\ChangeAnalysis\\FV Train Set 9.xls";

	readAndCalculate(source, target);

    }

    private static void readAndCalculate(String source, String target) {
	HashMap<Double,Double>  freq = new HashMap<Double,Double>  ();
	HashMap<Double,Double>  occur = new HashMap<Double,Double> ();
	HashMap<Double,Double>  life = new HashMap<Double,Double> (); // UC���ڵİ汾��ţ�[2-8]
	HashMap<Double,Double>  fv = new HashMap<Double,Double> ();
	HashMap<Double,Double> seq = new HashMap<Double,Double> ();
	

	// ������ĵ�
	InputStream is = null;
	try {
	    is = new FileInputStream(source);
	} catch (FileNotFoundException e1) {
	    e1.printStackTrace();
	}

	Workbook wb = null;
	try {
	    // ����Workbook��������������
	    wb = Workbook.getWorkbook(is);
	} catch (BiffException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	// ������ĵ�
	WritableWorkbook wwb = null;
	try {
	    // ����Ҫʹ��Workbook��Ĺ�����������һ����д��Ĺ�����(Workbook)����
	    wwb = Workbook.createWorkbook(new File(target));
	} catch (IOException e) {
	    e.printStackTrace();
	}
	if (wwb == null)
	    return;

	if (wb == null)
	    return;

	Sheet srcSheet = wb.getSheet("Sheet2");
	int nRow = srcSheet.getRows();

	if (srcSheet != null) {

	    Cell grid;

	    // ���ж�ȡ (3-8)
	    for (int evolution = 4; evolution <= 9; evolution++) { //evolution = [3,8], E2-E7, evolution - 1 = version
		
		LinkedList<Double> usecases = new LinkedList<Double>();
		double minOccur = 7;
		double maxOccur = 0;
		
		//����ǰEvolution���������usecase�б�,�������ǵ�ǰ�汾�з����޸ĵ��������к�
		//������ǰEvolution�������б�,row
		for (int row = 1; row < nRow; row++){
		    
		    boolean isDel = false;
		    boolean isAdd = false;
		    boolean isFirst = true;
		    
		    
		    //�ж���һ���Ƿ�Ϊɾ��������
		    for (int t = 3; t <= evolution; t++){
			grid = srcSheet.getCell(t,row);
			if (grid.getContents().equals("-1"))
			    isDel = true;
			
		    }
		    
		    //�ж���һ���Ƿ��Ǻ�������������
		    for (int t = evolution +1; t <= 9; t++){ //@ֻ��9
			grid = srcSheet.getCell(t,row);
			if (grid.getContents().equals("2"))
			    isAdd = true;
		    }
		    
		    if(!isDel&&!isAdd){
			usecases.add(new Double(row));
			
			Cell c = srcSheet.getCell(2,row);//��������
			System.out.println(c.getContents());
			
			
			    //���㵱ǰ�汾ÿ������������ life
			    for (int t = 3; t <= evolution; t++){ // t = [3,evolution]
				grid = srcSheet.getCell(t,row);
				if (grid.getContents().equals("2")){
				    life.put(row+0.0, (evolution+1.0-t-1)/(evolution-2));
				    isFirst = false;
				    }
			    }
			    if(isFirst)
				life.put(row+0.0, (evolution-1.0-1)/(evolution-2));
			    
			    //���㵱ǰ�汾����������Ĵ���
			    int sequence = 0;
			    int last = 0;
			    
			    for (int t = 3; t <= evolution; t++){ // t = [3,evolution]
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
			    seq.put(row+0.0, sequence/(evolution-2.0)); //
			}
		}

		// ��ȡǰ�����汾��ֵ
		for (int prev = 3; prev <= evolution; prev++) {

		    // ���ж�ȡÿ���汾������״̬
		    for (Double row : usecases) {
			
			grid = srcSheet.getCell(prev, row.intValue());

			// ����freq, occur
			// Cell�е�ֵΪ1����2ʱ��Freq++�� Occur++
			if (grid.getContents().equals("1")||grid.getContents().equals("2")) {//||grid.getContents().equals("2")
	
			    
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
			// Cell �е�ֵΪ0������
		    }
		}
		

		// ����occur
		for (Double row : usecases) {

		    if (freq.get(row) != null) {
			occur.put(row,evolution - 1 - (occur.get(row) / freq.get(row)));
			
			if(minOccur > occur.get(row))
			    minOccur = occur.get(row);
			if(maxOccur < occur.get(row))
			    maxOccur = occur.get(row);
			}
		    else{
			Cell c = srcSheet.getCell(2,row.intValue());
			//System.out.println(c.getContents());
		    }
			
		}

		// ���ݹ�һ��
		
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

		// ����FV
		
		for (Double row : usecases) {
		    if(evolution <9){
		    grid = srcSheet.getCell(evolution + 1, row.intValue());
		    // ���°汾��Ϊ�޸ĵ�״̬ʱ����Ϊ1
		    if (grid.getContents().equals("1"))
			fv.put(row, 1.0);
		    else
			fv.put(row, 0.0);
		    }
		    else{
			    fv.put(row, 0.0);
			}
		}
		

		// ���
		System.out.println("\nWriting to Excel Evolution "+ Integer.toString(evolution - 1));
		WritableSheet ws = wwb.createSheet("Evolution "
			+ Integer.toString(evolution - 1), 0);

		// д��ͷ
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
			ws.addCell(new jxl.write.Label(0, currRow, srcSheet.getCell(0, j.intValue()).getContents()));
			ws.addCell(new jxl.write.Label(1, currRow, srcSheet.getCell(1, j.intValue()).getContents()));
			ws.addCell(new jxl.write.Label(2, currRow, srcSheet.getCell(2, j.intValue()).getContents()));
			
			Double value = freq.get(j)==null?0:freq.get(j);
			Double value2 ;
			ws.addCell(new jxl.write.Number(3, currRow, value));
			
			value = occur.get(j)==null?0:occur.get(j);
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
		    
		    for(;currRow < numberOfUC[evolution - 2]+1;currRow++ ){
			ws.addCell(new jxl.write.Number(3, currRow, 0));
			ws.addCell(new jxl.write.Number(4, currRow, 0));
			ws.addCell(new jxl.write.Number(5, currRow, 1));
			ws.addCell(new jxl.write.Number(6, currRow, 0));
			ws.addCell(new jxl.write.Number(7, currRow, 0));
			ws.addCell(new jxl.write.Number(8, currRow, 0));
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
}
