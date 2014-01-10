package android;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class Run {

    /**
     * @param args
     */
    public static void main(String[] args) {
    	
	String userDir = System.getProperty("user.dir");
	String targetDir = userDir+"\\output\\android-frameworks";
	 ArrayList<String> tags = null;
	 String tagPath = userDir+"\\data\\tag-frameworks-trim.txt";
	 String changeTableName = "change_history";
	 String tagTableName = "android_tags";
	
	 if(args[0].equalsIgnoreCase("prepare-raw-data")){
	    //读取tag文件中各个版本号，存入tags;
	    try {
		System.out.println("1. Reading tags...");
		tags = PrepareRawData.readTags(tagPath);
		System.out.println("Tags are: \n"+tags.toString());
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    
	    //生成bat文件
	    String batPath = userDir+"\\output\\git-diff.bat";
	    String execDir = "G:\\android-src\\frameworks";
	    System.out.println("2. Generate Bat file:"+batPath);
	    try {
		PrepareRawData.generateBat(batPath,targetDir,execDir,tags);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	
	if(args[0].equalsIgnoreCase("dump-raw-data")){
		// 将txt存入数据库
		
		
		try {
		tags = PrepareRawData.readTags(tagPath);
		//PrepareRawData.dumpTagToMySQL(tagTableName, tags);
		PrepareRawData.dumpRawTxtToMySQL(changeTableName, targetDir,
			tags);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	}
	if(args[0].equalsIgnoreCase("prepare-trainset")){
	     
		//String source = "F:\\PredictReqChange.RE.2013\\data\\predict\\ChangeMetrix_13v.xls";
	     //String target = "F:\\PredictReqChange.RE.2013\\data\\predict\\PredictorValues.xls";
	     //String targetDIR = "F:\\PredictReqChange.RE.2013\\data\\predict\\trainset\\";

	    CalculatePredictFactorsForSQL c = new CalculatePredictFactorsForSQL();
		try {
			tags = PrepareRawData.readTags(tagPath);
			c.readAndCalculateFromMySQL(changeTableName,tags.size(),0);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    }
    
    public static void  callCmd(String locationCmd){
        try {
        Process child = Runtime.getRuntime().exec("cmd.exe /C start "+locationCmd);
        InputStream in = child.getInputStream();
        int c;
        while ((c = in.read()) != -1) {
    }
     in.close();
     try {
         child.waitFor();
     } catch (InterruptedException e) {
         e.printStackTrace();
     }
     System.out.println("done");
   } catch (IOException e) {
         e.printStackTrace();
   }
}

}
