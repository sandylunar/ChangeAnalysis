package prepare;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * �ڶ������Ӱ汾�����ݻ�����
 * @author Administrator
 *
 */
public class UseCaseChangeAnalysis {
    
   private static final int COLUMNS = 21; //��ȡusecase.xlsʱ���ʵ�����
private static String[] versions = new String[]{"2.5","2.6","2.8","2.9","3.0","3.1","3.2","3.4","4.1","5.0","5.1","M3","M6","5.5"};

    /**��ȡExcel�ļ�������  
     * @param file  ����ȡ���ļ�  
     * @return  
     */  
    public static void changeAnalysis(String sourcefile, String targetfile){   
	
	
	
	//������ĵ�
	InputStream is = null;
	try {
	    is = new FileInputStream(sourcefile);
	} catch (FileNotFoundException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	
        String[] status = new String[20];
        Cell[] rowCells = null;   
        Workbook srcSheet = null;   
        try {   
            //����Workbook��������������   
            srcSheet=Workbook.getWorkbook(is);   
        } catch (BiffException e) {   
            e.printStackTrace();   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
           
        if(srcSheet==null)   
            return ;
        
        //������ĵ�
        WritableWorkbook tgtSheet = null;   
        try {   
            //����Ҫʹ��Workbook��Ĺ�����������һ����д��Ĺ�����(Workbook)����   
            tgtSheet = Workbook.createWorkbook(new File(targetfile));   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
        if(tgtSheet==null)   
            return;
         WritableSheet ws = tgtSheet.createSheet("Sheet1",0);  
           
        
         
         //�����Workbook����֮�󣬾Ϳ���ͨ�����õ�Sheet��������������   
        Sheet sheet = srcSheet.getSheet("Sheet1");   
           
        if(sheet!=null){   
                //�õ���ǰ�����������   
                int rowNum = sheet.getRows();   
                
                //�����һ��
                rowCells = sheet.getRow(0);
                
                //��Existed,added,deleted�ȴ���status[],@ֻ��V3.4
                for (int x = 3;x<COLUMNS;x++){
                    status[x-3] = rowCells[x].getContents();
                }
                
                //���ж�ȡ               
                for(int j=1;j<rowNum;j++){
                    rowCells = sheet.getRow(j);  
                    int len = rowCells.length;
                    
                    if(len<15)
                    {
                	for(Cell c : rowCells)
                	    System.out.println(c.getContents());
                	System.out.println("Row = "+j+"\n Length = "+len);
                    }
                    
                    
                    if(rowCells!=null&&len>0){
                	
                	//���˿���
                	if(rowCells[2].getType()==CellType.EMPTY)
                	    continue;
                	
                	//д���ͷ
                	
                	for(int i = 1; i < versions.length; i++){
                	    try {
				ws.addCell(new Label(i,0,versions[i]));
			    } catch (RowsExceededException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    } catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
                	}
                	
                	//״̬����
                 	statusAnalysis(status,rowCells,j,ws);               	
                        }   
                    }
                } 
        	srcSheet.close();  
        	
                try {   
                    //���ڴ���д���ļ���   
                    tgtSheet.write();   
                    //�ر���Դ���ͷ��ڴ�   
                    tgtSheet.close();   
                } catch (IOException e) {   
                    e.printStackTrace();   
                } catch (WriteException e) {   
                    e.printStackTrace();   
                }  
              }

    /**
     * 
     * @param status �洢 Existed, Added, Deleted, Modified ��3-15
     * @param rowCells ��ǰ�еļ��ϣ�1-���
     * @param row ��ǰ����
     * @param ws д��ı�
     */
    private static void statusAnalysis(String[] status, Cell[] rowCells,int row,WritableSheet ws) {
	
	
	LinkedList<Integer> filledlist = new LinkedList<Integer>();

         
 	//���������ݵĵ�Ԫ����
 	for(int i = 3; i<rowCells.length; i++){
 	    
 	    //�ո�������
 	    if(rowCells[i].getType() == CellType.EMPTY)
 		continue;
 	   
 	    String version = rowCells[i].getContents();
 	    
 	    //���ˣ��ո�V2.5
 	    if(version=="" || i==3)
 		continue;
 	    
 	    	//Begin ��row, position��д��״̬
 	    	
 	    	//��ȡ�汾��
 		int v = version.indexOf('V');
 		String versionNo = version.substring(v+1);
 		int position = 0;
 		
 		//����versions,�ҵ�����һ���汾
 		for(position = 0; position < versions.length; position++){
 		    if(versions[position].equals(versionNo))
 			break;
 		}
 		
 		if(position == 0){  // �����⣬Ҳ���ܱ�������Ҳû�ҵ�
 		    System.out.println("Error, no version number can march!");
 		    return;
 		}
 		
 		//�ޱ仯��Ĭ��Ϊ0
 		Number number = new Number(position,row,0.0);
 		
 		//��״̬
 		if(status[i-3].equals("Added"))
 		   number = new Number(position,row,2.0); 
 		   
 		if(status[i-3].equals("Modified"))
 		   number = new Number(position,row,1.0);
 		
 		if(status[i-3].equals("Deleted"))
  		   number = new Number(position,row,-1.0);
 		

 	    
 		try {   
                    //�����ɵĵ�Ԫ����ӵ���������   
                    ws.addCell(number);
                    ws.addCell(new Label(0,row,rowCells[2].getContents()));
                    filledlist.add(new Integer(position));
                } catch (RowsExceededException e) {   
                    e.printStackTrace();   
                } catch (WriteException e) {   
                    e.printStackTrace();   
                } 
              //End ��row, position��д��״̬
                
                
 	}
 	
	//��0���ո�positionֵ��[1,7]
        
        for (int m = 1; m < versions.length; m++){
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

    public static void main( String args[] ) {
	UseCaseChangeAnalysis.changeAnalysis("I:\\Data\\ChangeAnalysis\\usecase.xls","I:\\Data\\ChangeAnalysis\\result.xls");
    }
    
}
