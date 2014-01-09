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
 * ��һ�����Ӹ����汾��changelog��ǵ��汾����
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
	// ������ĵ�
	InputStream is = null;
	try {
	    is = new FileInputStream(sourcefile);
	} catch (FileNotFoundException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	Workbook srcSheet = null;
	try {
	    // ����Workbook��������������
	    srcSheet = Workbook.getWorkbook(is);
	} catch (BiffException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	if (srcSheet == null)
	    return;

	// ������ĵ�
	WritableWorkbook tgtSheet = null;
	try {
	    // ����Ҫʹ��Workbook��Ĺ�����������һ����д��Ĺ�����(Workbook)����
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

	// �����Workbook����֮�󣬾Ϳ���ͨ�����õ�Sheet��������������
	Sheet src = srcSheet.getSheet(VERSION);

	if (src != null) {
	    // �õ���ǰ�����������
	    int rowNum = src.getRows();
	    System.out.println("Row Number = " + rowNum);

	    // �x 1-321�У�4.0�汾�еĬF������
	    
	    for (int j = 1; j < rowNum; j++) {
		//��ȡchangelog��Cell
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

		    // ׃������

			int pos = searchIndex(changeName, tar);

			if (pos == -1) { // �]���ҵ�����ӡ
			    System.out.println("Not found " + changeName);

			} else if (pos <= -2) { // �зN�� ����ӡ
			    System.out.println("Too many " + changeName + " : "
				    + pos);

			} else { // �ں��m��λ���ό��롰V5.0��

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
	    // ���ڴ���д���ļ���
	    tgtSheet.write();
	    // �ر���Դ���ͷ��ڴ�
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
