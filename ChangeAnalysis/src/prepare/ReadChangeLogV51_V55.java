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
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * 第一步，从各个版本的changelog标记到版本矩阵
 * @author Administrator
 *
 */
public class ReadChangeLogV51_V55 {

    private static final String VERSION = "M12->5.5";
    private static final String WRITTENVERSION = "V5.5";
    private static final int MODIFIEDCOL = 22;
    /**
     * @param args
     */

    public static void main(String[] args) {
	// TODO Auto-generated method stub
	ReadChangeLogV51_V55.run(
		"I:\\Data\\ChangeAnalysis\\UC Change log_2.5-5.4.xls",
		"I:\\Data\\ChangeAnalysis\\usecase_M12.xls");
    }

    private static void run(String sourcefile, String targetfile) {
	// TODO Auto-generated method stub
	// 输入的文档
	InputStream is = null;
	try {
	    is = new FileInputStream(sourcefile);
	} catch (FileNotFoundException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	Workbook srcSheet = null;
	try {
	    // 构造Workbook（工作薄）对象
	    srcSheet = Workbook.getWorkbook(is);
	} catch (BiffException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	if (srcSheet == null)
	    return;

	// 输出的文档
	WritableWorkbook tgtSheet = null;
	try {
	    // 首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象
	    tgtSheet = Workbook.createWorkbook(new File(
		    "I:\\Data\\ChangeAnalysis\\usecase_TMP.xls"), Workbook
		    .getWorkbook(new FileInputStream(targetfile)));
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (BiffException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	if (tgtSheet == null)
	    return;
	WritableSheet tar = tgtSheet.getSheet("Sheet1");

	// 获得了Workbook对象之后，就可以通过它得到Sheet（工作表）对象了
	Sheet src = srcSheet.getSheet(VERSION);

	if (src != null) {
	    // 得到当前工作表的行数
	    int rowNum = src.getRows();
	    System.out.println("Row Number = " + rowNum);

	    // 讀 1-321行：4.0版本中的現有需求
	    
	    for (int j = 1; j < rowNum; j++) {
		//读取changelog的Cell
		Cell changelog = src.getCell(2, j);
		
		if(changelog == null ||changelog.getType() == CellType.EMPTY ||changelog.getContents() == null)
		    continue;
		
		System.out.println("Changelog = "+changelog);
		
		String changeName = getUsecaseName(src.getCell(1, j)
			.getContents());
		
		if(changeName == null){
		    System.out.println("Error: changelog = "+changelog+"; but ucname is null");
		    break;
		}

		    // 變更分析

			int pos = searchIndex(changeName, tar);

			if (pos == -1) { // 沒有找到，打印
			    System.out.println("Not found " + changeName);

			} else if (pos <= -2) { // 有種名 ，打印
			    System.out.println("Too many " + changeName + " : "
				    + pos);

			} else { // 在合適的位置上寫入“V5.0”

			    Label version = new Label(MODIFIEDCOL, pos,
				    WRITTENVERSION);
			    try {
				tar.addCell(version);
			    } catch (RowsExceededException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    } catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
			}
	    }
	}
	srcSheet.close();

	try {
	    // 从内存中写入文件中
	    tgtSheet.write();
	    // 关闭资源，释放内存
	    tgtSheet.close();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (WriteException e) {
	    e.printStackTrace();
	}
    }

    /**
     * 
     * @param changeName
     * @param tar
     * @return
     */
    private static int searchIndex(String changeName, WritableSheet tar) {
	// TODO Auto-generated method stub
	Cell[] names = tar.getColumn(2);
	int result = -1;
	int count = 0;

	for (int i = 1; i < names.length; i++) {
	    // System.out.println("changeName: "+changeName);
	    // System.out.println("names: "+i+" :"+names[i]);
	    String name = names[i].getContents();
	    if (changeName.equals(name)) {
		result = i;
		count++;
	    }
	}

	if (count > 1)
	    return 0 - count;
	return result;
    }

    /**
     * 
     * @param contents
     * @return
     */
    private static String getUsecaseName(String contents) {
	// TODO Auto-generated method stub
	char[] charArray = contents.toCharArray();
	for (int i = 0; i < charArray.length; i++) {
	    if ((charArray[i] >= 0x4e00) && (charArray[i] <= 0x9fbb)) {
		return contents.substring(i);
	    }
	}
	return null;
    }
}
