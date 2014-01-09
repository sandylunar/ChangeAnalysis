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
 * 第二步，从版本矩阵到演化矩阵
 * @author Administrator
 *
 */
public class UseCaseChangeAnalysis {
    
   private static final int COLUMNS = 21; //读取usecase.xls时访问的列数
private static String[] versions = new String[]{"2.5","2.6","2.8","2.9","3.0","3.1","3.2","3.4","4.1","5.0","5.1","M3","M6","5.5"};

    /**读取Excel文件的内容  
     * @param file  待读取的文件  
     * @return  
     */  
    public static void changeAnalysis(String sourcefile, String targetfile){   
	
	
	
	//输入的文档
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
            //构造Workbook（工作薄）对象   
            srcSheet=Workbook.getWorkbook(is);   
        } catch (BiffException e) {   
            e.printStackTrace();   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
           
        if(srcSheet==null)   
            return ;
        
        //输出的文档
        WritableWorkbook tgtSheet = null;   
        try {   
            //首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象   
            tgtSheet = Workbook.createWorkbook(new File(targetfile));   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
        if(tgtSheet==null)   
            return;
         WritableSheet ws = tgtSheet.createSheet("Sheet1",0);  
           
        
         
         //获得了Workbook对象之后，就可以通过它得到Sheet（工作表）对象了   
        Sheet sheet = srcSheet.getSheet("Sheet1");   
           
        if(sheet!=null){   
                //得到当前工作表的行数   
                int rowNum = sheet.getRows();   
                
                //处理第一行
                rowCells = sheet.getRow(0);
                
                //把Existed,added,deleted等存入status[],@只到V3.4
                for (int x = 3;x<COLUMNS;x++){
                    status[x-3] = rowCells[x].getContents();
                }
                
                //逐行读取               
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
                	
                	//过滤空行
                	if(rowCells[2].getType()==CellType.EMPTY)
                	    continue;
                	
                	//写入表头
                	
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
                	
                	//状态分析
                 	statusAnalysis(status,rowCells,j,ws);               	
                        }   
                    }
                } 
        	srcSheet.close();  
        	
                try {   
                    //从内存中写入文件中   
                    tgtSheet.write();   
                    //关闭资源，释放内存   
                    tgtSheet.close();   
                } catch (IOException e) {   
                    e.printStackTrace();   
                } catch (WriteException e) {   
                    e.printStackTrace();   
                }  
              }

    /**
     * 
     * @param status 存储 Existed, Added, Deleted, Modified 等3-15
     * @param rowCells 当前行的集合，1-最后
     * @param row 当前行数
     * @param ws 写入的表
     */
    private static void statusAnalysis(String[] status, Cell[] rowCells,int row,WritableSheet ws) {
	
	
	LinkedList<Integer> filledlist = new LinkedList<Integer>();

         
 	//遍历有内容的单元格编号
 	for(int i = 3; i<rowCells.length; i++){
 	    
 	    //空格，则跳过
 	    if(rowCells[i].getType() == CellType.EMPTY)
 		continue;
 	   
 	    String version = rowCells[i].getContents();
 	    
 	    //过滤：空格，V2.5
 	    if(version=="" || i==3)
 		continue;
 	    
 	    	//Begin 往row, position上写入状态
 	    	
 	    	//获取版本号
 		int v = version.indexOf('V');
 		String versionNo = version.substring(v+1);
 		int position = 0;
 		
 		//遍历versions,找到是哪一个版本
 		for(position = 0; position < versions.length; position++){
 		    if(versions[position].equals(versionNo))
 			break;
 		}
 		
 		if(position == 0){  // 有问题，也可能遍历完了也没找到
 		    System.out.println("Error, no version number can march!");
 		    return;
 		}
 		
 		//无变化则默认为0
 		Number number = new Number(position,row,0.0);
 		
 		//填状态
 		if(status[i-3].equals("Added"))
 		   number = new Number(position,row,2.0); 
 		   
 		if(status[i-3].equals("Modified"))
 		   number = new Number(position,row,1.0);
 		
 		if(status[i-3].equals("Deleted"))
  		   number = new Number(position,row,-1.0);
 		

 	    
 		try {   
                    //将生成的单元格添加到工作表中   
                    ws.addCell(number);
                    ws.addCell(new Label(0,row,rowCells[2].getContents()));
                    filledlist.add(new Integer(position));
                } catch (RowsExceededException e) {   
                    e.printStackTrace();   
                } catch (WriteException e) {   
                    e.printStackTrace();   
                } 
              //End 往row, position上写入状态
                
                
 	}
 	
	//用0填充空格，position值域[1,7]
        
        for (int m = 1; m < versions.length; m++){
            if(!isContians(filledlist, new Integer(m)))
        	try {   
                    //将生成的单元格添加到工作表中   
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
