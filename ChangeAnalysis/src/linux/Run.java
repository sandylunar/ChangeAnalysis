package linux;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.ArrayList;

public class Run {
	static String userDir = System.getProperty("user.dir");
	static String targetDir = userDir + "\\output-linux\\diff";
	static String tagPath = userDir + "/data/tag.txt";
	static String sortedTagPath = userDir + "/data/tag-sorted.txt";

	public static void main(String[] args) throws IOException, SQLException  {
		
		PrepareRawData prd = new PrepareRawData();
		ArrayList<String> tags = prd.readTags(sortedTagPath);
		int numTags = tags.size();
		
		
		if (args[0].equalsIgnoreCase("prepare-git-tags")){
			System.out.println("1. Reading tags...");
			prd.readTags(tagPath);
			tags = prd.bubbleSort();
			numTags = tags.size();
			prd.setTags(tags);
			System.out.println("Tag size: \n" + numTags);
			System.out.println("Writing sorted tags to: " + sortedTagPath);
			prd.generateSortedTags(sortedTagPath,tags);
		}
		if (args[0].equalsIgnoreCase("prepare-gitlog-bat")) {

			// 生成bat文件
			String batPath = userDir + "\\output-linux\\diff\\git-log";
			//String execDir = "G:\\linux-kernel\\ti-linux-kernel";
			String execDir = "/root/git/linuxkernel/linuxkernel";
			//String targetDir = "G:\\linux-kernel\\diff";
			String targetDir = "/root/git/linuxkernel/diff";
			System.out.println("2. Generate Bat file:" + batPath);
			prd.generateBatForLinux(batPath, targetDir, execDir, 4);
		}
		
		if (args[0].equalsIgnoreCase("export-txt-to-mysql")) {
			String targetDir ="G:\\Sharespace\\experiment\\";
			String commitTable = "commits_in_all";
			prd.createTable(commitTable);
			prd.dumpCommitsToMysql(targetDir, numTags,commitTable);
			prd.close();
			
			//testIsCommit();
		}
	}
	
	public static void test() throws FileNotFoundException, IOException{
		 final int BUFFER_SIZE = 0x1200000;// 缓冲大小为12M
	        
	        System.out.println(BUFFER_SIZE);

	        File f = new File("G:\\Sharespace\\experiment\\112-113.txt");
	 
	        int len = 0;
	        Long start = System.currentTimeMillis();
	        for (int z = 8; z >0; z--) {
	            MappedByteBuffer inputBuffer = new RandomAccessFile(f, "r")
	                    .getChannel().map(FileChannel.MapMode.READ_ONLY,
	                            f.length() * (z-1) / 8, f.length() * 1 / 8);
	            byte[] dst = new byte[BUFFER_SIZE];// 每次读出12M的内容
	            for (int offset = 0; offset < inputBuffer.capacity(); offset += BUFFER_SIZE) {
	                if (inputBuffer.capacity() - offset >= BUFFER_SIZE) {
	                    for (int i = 0; i < BUFFER_SIZE; i++)
	                        dst[i] = inputBuffer.get(offset + i);
	                } else {
	                    for (int i = 0; i < inputBuffer.capacity() - offset; i++)
	                        dst[i] = inputBuffer.get(offset + i);
	                }
	                int length = (inputBuffer.capacity() % BUFFER_SIZE == 0) ? BUFFER_SIZE
	                        : inputBuffer.capacity() % BUFFER_SIZE;

	                len += new String(dst, 0, length).length();
	                System.out.println(new String(dst, 0, length).length()+"-"+(z-1)+"-"+(8-z+1));
	            }
	        }
	        System.out.println(len);
	        long end = System.currentTimeMillis();
	        System.out.println("读取文件文件花费：" + (end - start) + "毫秒");		
	}
	
	public static void testIsCommit(){
		PrepareRawData prd = new PrepareRawData();
		String commit1 = "commit qwe12137jlkjlja12312j3lj1lj2lk3";
		String commit2 = "commit ";
		String commit3 = "commit !!!)@()*(@(*";
		String commit4 = "diff --git a/Makefile b/Makefile";
		
		System.out.println(prd.isCommitStart(commit1));
		System.out.println(prd.isCommitStart(commit2));
		System.out.println(prd.isCommitStart(commit3));
		System.out.println(prd.isCommitStart(commit4));
	}
}


