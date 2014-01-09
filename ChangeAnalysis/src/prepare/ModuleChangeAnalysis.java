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
	
	//输入的文档
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
            //构造Workbook（工作薄）对象   
            wb=Workbook.getWorkbook(is);    
        } catch (BiffException e) {   
            e.printStackTrace();   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
           
        if(wb==null)   
            return ;
        
        
        
        //输出的文档
        WritableWorkbook wwb = null;   
        try {   
            //首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象   
            wwb = Workbook.createWorkbook(new File(targetfile));   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
        if(wwb==null)   
            return;
         WritableSheet ws = wwb.createSheet("Sheet1",0);  
           
        
         
         //获得了Workbook对象之后，就可以通过它得到Sheet（工作表）对象了   
        Sheet sheet = wb.getSheet("Sheet1");   
        
           
        if(sheet!=null){   
                //得到当前工作表的行数   
                int rowNum = sheet.getRows();   
                
                //处理V2.5
                rowCells = sheet.getColumn(2);
                
                
                
                //处理第一行
                rowCells = sheet.getRow(0);
                
                for (int x = 3;x<15;x++){
                    status[x-3] = rowCells[x].getContents();
                }
                
                //逐行读取               
                for(int j=1;j<rowNum;j++){
                    rowCells = sheet.getRow(j);  
                    int len = rowCells.length;
                    
                    if(rowCells!=null&&len>0){
                	
                	//过滤空行
                	if(rowCells[2].getContents().length()==0)
                	    continue;
                	
                	//状态分析
                 	statusAnalysis(status,rowCells,j,buffer);               	
                        }   
                    }
                } 
        	wb.close();  
        	
                try {   
                    //从内存中写入文件中   
                    wwb.write();   
                    //关闭资源，释放内存   
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
	

         
 	//遍历有内容的单元格编号,i为3-15之间的整数
 	for(int i = 3; i<rowCells.length; i++){
 	    String version = rowCells[i].getContents();
 	    
 	    //过滤：空格
 	    if(version=="")
 		continue;
 	    
 	    	//Begin 往module, position上写入状态
 	    	
 	    	//获取版本号
 		int v = version.indexOf('V');
 		String versionNo = version.substring(v+1, v+4);
 		int position = 0; //列编号,0-7之间的整数
 		int module =0;    //行编号,0-9之间的整数
 		
 		for(; position < versions.length; position++){
 		    if(versions[position].equals(versionNo))
 			break;
 		}
 		
 		//找到模块行
 		for(; module<modules.length; module++){
 		    if(modules[module].equals(rowCells[0]))
 			break;
 		}
 		
 		
 		//填状态
 		if(status[i-3].equals("Existed"))
 		    buffer[module][3]++;
 		
 		if(status[i-3].equals("Added"))
 		   buffer[module][(position-1)*6+0]++;
 		   
 		if(status[i-3].equals("Modified"))
 		   buffer[module][(position-1)*6+2]++;
 		
 		if(status[i-3].equals("Deleted"))
 		   buffer[module][(position-1)*6+1]++;
 		
 		
 		

 	    
 		try {   
                    //将生成的单元格添加到工作表中   
                    ws.addCell(number);
                    filledlist.add(new Integer(position));
                } catch (RowsExceededException e) {   
                    e.printStackTrace();   
                } catch (WriteException e) {   
                    e.printStackTrace();   
                } 
              //End 往row, position上写入状态
                
                
 	}
 	
	//用0填充空格，position值域[1,7]
        
        for (int m = 1; m < 8; m++){
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
    *//**
     * @param args
     *//*
    public static void main(String[] args) {
	ModuleChangeAnalysis.changeAnalysis("I:\\Data\\ChangeAnalysis\\Module Input.xls","I:\\Data\\ChangeAnalysis\\Module Output.xls");

    }

}
*/