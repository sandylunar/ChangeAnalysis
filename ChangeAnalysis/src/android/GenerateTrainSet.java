package android;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * ���Ĳ�������ѵ���õ�����
 * @author myhome
 *
 */

public class GenerateTrainSet {
    
static final int FISRTMODEL = 1;
static final int LASTMODEL = 11;

static WorkbookSettings workbookSettings = new WorkbookSettings();


    /**
     * @param args
     */
    public static void main(String[] args) {
	//String source = CalculatePredictFactors.target; //"I:\\Data\\Logit Reg for Changes\\FV Train Set 13.xls";
	String source = "I:\\Data\\ChangeAnalysis\\FV Train Set 14.xls";
	String targetDIR = "I:\\Data\\Logit Reg for Changes\\[10-2]\\";

	//generate(source, targetDIR);

    }

    protected static void generate(String source, String target) {
	// TODO Auto-generated method stub
	
	workbookSettings.setEncoding("UNICODE"); //����������룬��GBK
	
	//������ĵ�
	InputStream is = null;
	try {
	    is = new FileInputStream(source);
	} catch (FileNotFoundException e1) {
	    e1.printStackTrace();
	}
	
        Workbook srcWBook = null;   
        try {   
            //����Workbook��������������   

            srcWBook=Workbook.getWorkbook(is,workbookSettings);   
        } catch (BiffException e) {   
            e.printStackTrace();   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
           
        if(srcWBook==null)   
            return ;
        
        Sheet[] allSheet = new Sheet[LASTMODEL-FISRTMODEL+1]; 
        Sheet predictSheet = srcWBook.getSheet(new String("Model "+Integer.toString(LASTMODEL+1)));
        System.out.println(predictSheet.getRows());
        
        for (int i = FISRTMODEL; i <= LASTMODEL; i++){
            allSheet[i-FISRTMODEL] = srcWBook.getSheet(new String("Model "+Integer.toString(i)));
        }
        
        
        
	// ������ĵ�
        try {
        WritableWorkbook[] allWriteWBook = new WritableWorkbook[LASTMODEL-FISRTMODEL+1];
/*	WritableWorkbook predictWriteWBook = Workbook.createWorkbook(new File(target+"M"+Integer.toString(LASTMODEL+1)+".xls"));
	predictWriteWBook.importSheet("ToBePredicted", 0, predictSheet);
	predictWriteWBook.write();
	predictWriteWBook.close();*/
	
	
	    // ����Ҫʹ��Workbook��Ĺ�����������һ����д��Ĺ�����(Workbook)����
	    
	    for (int i = FISRTMODEL; i <= LASTMODEL; i++){
		allWriteWBook[i-FISRTMODEL] = Workbook.createWorkbook(new File(target+"M"+Integer.toString(i)+".xls"),workbookSettings);
		WritableSheet writeSheet = allWriteWBook[i-FISRTMODEL].createSheet("Sheet", 0);
		int row = 1;
		int cursor = 0; //��ǰ���Д�
		
		// д��ͷ
		try {
		    writeSheet.addCell(new Label(0, 0, "UC_Name"));
		    writeSheet.addCell(new Label(1, 0, "Module"));
		    writeSheet.addCell(new Label(2, 0, "ModuleVolality"));
		    writeSheet.addCell(new Label(3, 0, "ModuleVolalityAVG"));
		    writeSheet.addCell(new Label(4, 0, "Frequency"));
		    writeSheet.addCell(new Label(5, 0, "Distance"));
		    writeSheet.addCell(new Label(6, 0, "Lifecycle"));
		    writeSheet.addCell(new Label(7, 0, "Occurence"));
		    writeSheet.addCell(new Label(8, 0, "Sequence"));
		    writeSheet.addCell(new Label(9, 0, "LastChange"));
		    writeSheet.addCell(new Label(10, 0, "TopicSimilariy"));
		    writeSheet.addCell(new Label(11, 0, "TopicContain"));
		    writeSheet.addCell(new Label(12, 0, "FV_Actual"));
		    writeSheet.addCell(new Label(13, 0, "Dataset"));
		    
		} catch (RowsExceededException e1) {
		    e1.printStackTrace();
		} catch (WriteException e1) {
		    e1.printStackTrace();
		}
		
		//��allSheet[j]~[i-FISRTMODEL]���� writeSheet
		for (int j = 0; j <= i-FISRTMODEL; j++){
		   
		    int size = allSheet[j].getRows();
		    
		    //��allSheet[j]�ă��݌���writeSheet�����^��һ��
		    for(cursor = 1; cursor < size ;cursor++){
			Label cell = new Label(CalculatePredictFactors.UCNAME,cursor+row-1,allSheet[j].getCell(CalculatePredictFactors.UCNAME,cursor).getContents());
			writeSheet.addCell(cell);
			
			Label cellForM = new Label(CalculatePredictFactors.MODULE,cursor+row-1,allSheet[j].getCell(CalculatePredictFactors.MODULE,cursor).getContents());
			writeSheet.addCell(cellForM);
			
			String cont = allSheet[j].getCell(CalculatePredictFactors.DATASET,cursor).getContents();
			jxl.write.Number number = new jxl.write.Number(CalculatePredictFactors.DATASET,cursor+row-1, Integer.parseInt(cont));
		    writeSheet.addCell(number);
			
			for(int k = 2; k < 13; k++){
			    String value = allSheet[j].getCell(k,cursor).getContents();
			    jxl.write.Number number2 = new jxl.write.Number(k,cursor+row-1,Double.parseDouble(value));
			    
			    writeSheet.addCell(number2);
			    
			}
		    }
		    row += size-1;
		}
		
		allWriteWBook[i-FISRTMODEL].write();
		allWriteWBook[i-FISRTMODEL].close();
	    }
	    
	    srcWBook.close();
	    
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (WriteException e) {
	    e.printStackTrace();
	}
        
    }

}
