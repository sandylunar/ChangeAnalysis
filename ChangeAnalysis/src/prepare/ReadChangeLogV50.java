package prepare;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ReadChangeLogV50 {

    /**
     * @param args
     */
    public static int[] modifiedCells = { 6, 8, 10, 12, 14, 16 };

    public static void main(String[] args) {
	// TODO Auto-generated method stub
	ReadChangeLogV50.run("I:\\Data\\ChangeAnalysis\\UC Change log_2.5-5.4.xls","I:\\Data\\ChangeAnalysis\\usecase.xls");
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

	Cell[] rowCells = null;
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
	    tgtSheet = Workbook.createWorkbook(new File("I:\\Data\\ChangeAnalysis\\usecase_1.xls"),Workbook.getWorkbook(new FileInputStream(targetfile)));
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
	Sheet src = srcSheet.getSheet("4.1->5.0");

	if (src != null) {
	    // �õ���ǰ�����������
	    int rowNum = src.getRows();
	    System.out.println("Row Number = "+rowNum);

	    // �x 1-321�У�4.0�汾�еĬF������
	    for (int j = 1; j < 321; j++) {
		rowCells = src.getRow(j);
		int len = rowCells.length;

		if (rowCells != null && len > 0) {

		    int size = rowCells.length;
		    
		    // ׃������
		    if( size > 2 && rowCells[2].getContents().trim()!=null){
			
			String changeName = getUsecaseName(rowCells[1].getContents());
			int pos = searchIndex(changeName,tar);
			
			if(pos == -1){ // �]���ҵ�����ӡ
			    System.out.println("Not found "+ changeName);
			    
			} else if(pos <= -2){ // �зN�� ����ӡ
			    System.out.println("Too many " + changeName +" : "+pos);
			    
			}else{ // �ں��m��λ���ό��롰V5.0��
			    
			    Cell[] tarRow = tar.getRow(pos);
			    
			    int mod = 0;
			    
			    // �ҵ���һ���ǿյ�Modifiedλ��
			    for(int i = 0; i < modifiedCells.length; i++){
				mod = modifiedCells[i];
				// ���modifiedλ�ò���գ��t����һ��
				if(tarRow.length>=mod && tarRow[mod]!=null && tarRow[mod].getContents().length()!=0)			    
				    continue;
			}
			   Label version = new Label(mod,pos,"V5.0"); 
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
		    // statusAnalysis(status,rowCells,j,ws);
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
	
	for (int i = 1; i < names.length; i++){
	    //System.out.println("changeName: "+changeName);
	    //System.out.println("names: "+i+" :"+names[i]);
	    if(changeName.equals(names[i].getContents())){
		result = i;
		count++;
	    }
	}
	
	if(count > 1)
	    return 0-count;
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
	for(int i=0;i<charArray.length;i++){  
	  if ((charArray[i] >= 0x4e00)&&(charArray[i] <= 0x9fbb)){  
	    return contents.substring(i);  
	  }       
	}
	return null;
    }
}
