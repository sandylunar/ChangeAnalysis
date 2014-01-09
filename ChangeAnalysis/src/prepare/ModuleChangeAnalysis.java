package prepare;
/*import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class ModuleChangeAnalysis {

    
  public static void changeAnalysis(String sourcefile, String targetfile){   
	
	//������ĵ�
	InputStream is = null;
	try {
	    is = new FileInputStream(sourcefile);
	} catch (FileNotFoundException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	
        String[] status = new String[12];
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
        
        
        
        //������ĵ�
        WritableWorkbook wwb = null;   
        try {   
            //����Ҫʹ��Workbook��Ĺ�����������һ����д��Ĺ�����(Workbook)����   
            wwb = Workbook.createWorkbook(new File(targetfile));   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
        if(wwb==null)   
            return;
         WritableSheet ws = wwb.createSheet("Sheet1",0);  
           
        
         
         //�����Workbook����֮�󣬾Ϳ���ͨ�����õ�Sheet��������������   
        Sheet sheet = wb.getSheet("Sheet1");   
        
           
        if(sheet!=null){   
                //�õ���ǰ�����������   
                int rowNum = sheet.getRows();   
                
                //����V2.5
                rowCells = sheet.getColumn(2);
                
                
                
                //�����һ��
                rowCells = sheet.getRow(0);
                
                for (int x = 3;x<15;x++){
                    status[x-3] = rowCells[x].getContents();
                }
                
                //���ж�ȡ               
                for(int j=1;j<rowNum;j++){
                    rowCells = sheet.getRow(j);  
                    int len = rowCells.length;
                    
                    if(rowCells!=null&&len>0){
                	
                	//���˿���
                	if(rowCells[2].getContents().length()==0)
                	    continue;
                	
                	//״̬����
                 	statusAnalysis(status,rowCells,j,buffer);               	
                        }   
                    }
                } 
        	wb.close();  
        	
                try {   
                    //���ڴ���д���ļ���   
                    wwb.write();   
                    //�ر���Դ���ͷ��ڴ�   
                    wwb.close();   
                } catch (IOException e) {   
                    e.printStackTrace();   
                } catch (WriteException e) {   
                    e.printStackTrace();   
                }  
              }

    private static void statusAnalysis(String[] status, Cell[] rowCells,int row,double[][] buffer) {
	
	String[] versions = new String[]{"2.5","2.6","2.8","2.9","3.0","3.1","3.2","3.4"};
	String[] modules = new String[]{"Change management","Measure management",
		"Project management",
		"Process management",
		"Product management",
		"Quality management",
		"Risk management",
		"System management",
		"Task management",
		"Test management"
};
	LinkedList<Integer> filledlist = new LinkedList<Integer>();
	

         
 	//���������ݵĵ�Ԫ����,iΪ3-15֮�������
 	for(int i = 3; i<rowCells.length; i++){
 	    String version = rowCells[i].getContents();
 	    
 	    //���ˣ��ո�
 	    if(version=="")
 		continue;
 	    
 	    	//Begin ��module, position��д��״̬
 	    	
 	    	//��ȡ�汾��
 		int v = version.indexOf('V');
 		String versionNo = version.substring(v+1, v+4);
 		int position = 0; //�б��,0-7֮�������
 		int module =0;    //�б��,0-9֮�������
 		
 		for(; position < versions.length; position++){
 		    if(versions[position].equals(versionNo))
 			break;
 		}
 		
 		//�ҵ�ģ����
 		for(; module<modules.length; module++){
 		    if(modules[module].equals(rowCells[0]))
 			break;
 		}
 		
 		
 		//��״̬
 		if(status[i-3].equals("Existed"))
 		    buffer[module][3]++;
 		
 		if(status[i-3].equals("Added"))
 		   buffer[module][(position-1)*6+0]++;
 		   
 		if(status[i-3].equals("Modified"))
 		   buffer[module][(position-1)*6+2]++;
 		
 		if(status[i-3].equals("Deleted"))
 		   buffer[module][(position-1)*6+1]++;
 		
 		
 		

 	    
 		try {   
                    //�����ɵĵ�Ԫ����ӵ���������   
                    ws.addCell(number);
                    filledlist.add(new Integer(position));
                } catch (RowsExceededException e) {   
                    e.printStackTrace();   
                } catch (WriteException e) {   
                    e.printStackTrace();   
                } 
              //End ��row, position��д��״̬
                
                
 	}
 	
	//��0���ո�positionֵ��[1,7]
        
        for (int m = 1; m < 8; m++){
            if(!isContians(filledlist, new Integer(m)))
        	try {   
                    //�����ɵĵ�Ԫ����ӵ���������   
                    ws.addCell(new Number(m,row,0.0));
                } catch (RowsExceededException e) {   
                    e.printStackTrace();   
                } catch (WriteException e) {   
                    e.printStackTrace();   
                } 
        }
    }



    private static boolean isContians(LinkedList<Integer> filledlist,
	    Integer integer) {
	for (Integer curr: filledlist){
	    if(curr.compareTo(integer)==0)
		return true;
	}
	return false;
    }
    *//**
     * @param args
     *//*
    public static void main(String[] args) {
	ModuleChangeAnalysis.changeAnalysis("I:\\Data\\ChangeAnalysis\\Module Input.xls","I:\\Data\\ChangeAnalysis\\Module Output.xls");

    }

}
*/