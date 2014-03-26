package linux;

import java.io.IOException;
import java.util.ArrayList;

import android.PrepareRawData;

public class Run {
	static String userDir = System.getProperty("user.dir");
	static String targetDir = userDir + "\\output-linux\\diff";
	static String tagPath = userDir + "/data/tag.txt";
	public static int numTags;
	

	public static void main(String[] args)  {
		System.out.println("1. Reading tags...");
		ArrayList<String> tags = null;
		try {
			tags = PrepareRawData.readTags(tagPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		numTags = tags.size();
		System.out.println("Tag size: \n" + numTags);
		
/*		if (args[0].equalsIgnoreCase("prepare-git-diff")) {

			// 生成bat文件
			String batPath = userDir + "\\output-linux\\diff\\git-log.bat";
			String execDir = "G:\\android-src\\frameworks";
			System.out.println("2. Generate Bat file:" + batPath);
			
			//PrepareRawData.generateBat(batPath, targetDir, execDir, tags);
			
		}*/
	}
}


