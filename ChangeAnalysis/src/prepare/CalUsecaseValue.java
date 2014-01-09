package prepare;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class CalUsecaseValue {

    /**
     * @param args
     */
    public static void main(String[] args) {
	// TODO Auto-generated method stub

	String source = "I:\\Data\\ChangeAnalysis\\UseCase Change Status2.xls";
	String target = "I:\\Data\\ChangeAnalysis\\UseCase Change Status-result.xls";
	double[] times = new double[322]; 

	readAndCalculate(source,target,times);
	

    }

    private static void readAndCalculate(String source,String target, double[] times) {
	//������ĵ�
	InputStream is = null;
	try {
	    is = new FileInputStream(source);
	} catch (FileNotFoundException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	
        Cell[] rowCells = null;   
        Workbook wb = null;   
        try {   
            //����Workbook��������������   
            wb=Workbook.getWorkbook(is);   
        } catch (BiffException e) {   
            e.printStackTrace();   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
           
        if(wb==null)   
            return ;
        
        Sheet sheet = wb.getSheet("Sheet2");   
        
        if(sheet!=null){   
                //���ж�ȡ               
                for(int j=1;j<323;j++){
                    rowCells = sheet.getRow(j);  
                    int len = rowCells.length;
                    if(rowCells!=null&&len>0){
                	
                	//���˿���
                	if(rowCells[2].getContents().length()==0)
                	    continue;
                	
                	//д��times
                	double sum = 0;
                	double no = 0;
                	for(int i = 0;i<7;i++){
                	    if(!rowCells[i+3].getContents().equals("0")){
                		sum+=i+1;
                		no++;
                		}
                	}
                	
                	times[j-1]=sum/no;
                	System.out.println(times[j-1]);
                 	              	
                        }   
                    }
                } 
        	wb.close();
        	
        	   //������ĵ�
                WritableWorkbook wwb = null;   
                try {   
                    //����Ҫʹ��Workbook��Ĺ�����������һ����д��Ĺ�����(Workbook)����   
                    wwb = Workbook.createWorkbook(new File(target));   
                } catch (IOException e) {   
                    e.printStackTrace();   
                }   
                if(wwb==null)   
                    return;
                 WritableSheet ws = wwb.createSheet("Sheet1",0);  
                           	
        	for (int i = 1; i < 323;i++){
        	    try {
			ws.addCell(new jxl.write.Number(11,i,times[i-1]));
		    } catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    } catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
        	}
        	

        	
        	// д�� Excel ����
        	try {
        	    wwb.write();
        		// �رտ�д��� Excel ����
        		wwb.close();
        		// �ر�ֻ���� Excel ����
        	} catch (IOException e) {
        	    // TODO Auto-generated catch block
        	    e.printStackTrace();
        	} catch (WriteException e) {
        	    // TODO Auto-generated catch block
        	    e.printStackTrace();
        	}
        	
    }



}
