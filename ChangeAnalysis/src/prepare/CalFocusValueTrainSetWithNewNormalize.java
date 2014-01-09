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

public class CalFocusValueTrainSetWithNewNormalize {

    /**
     * @param args
     */
    public static void main(String[] args) {
	// TODO Auto-generated method stub

	String source = "I:\\Data\\ChangeAnalysis\\UseCase Change Status2.xls";
	String target = "I:\\Data\\ChangeAnalysis\\FV Train Set with New Normalize.xls";

	readAndCalculate(source, target);

    }

    private static void readAndCalculate(String source, String target) {
	double[] freq = new double[322];
	double[] occur = new double[322];
	double[] fv = new double[322];
	double maxFreq = 0;
	double maxOccur = 0;

	// ������ĵ�
	InputStream is = null;
	try {
	    is = new FileInputStream(source);
	} catch (FileNotFoundException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	Workbook wb = null;
	try {
	    // ����Workbook��������������
	    wb = Workbook.getWorkbook(is);
	} catch (BiffException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	// ������ĵ�
	WritableWorkbook wwb = null;
	try {
	    // ����Ҫʹ��Workbook��Ĺ�����������һ����д��Ĺ�����(Workbook)����
	    wwb = Workbook.createWorkbook(new File(target));
	} catch (IOException e) {
	    e.printStackTrace();
	}
	if (wwb == null)
	    return;

	if (wb == null)
	    return;

	Sheet sheet = wb.getSheet("Sheet2");

	if (sheet != null) {

	    Cell grid;

	    // ���ж�ȡ
	    for (int evolution = 3; evolution <= 8; evolution++) {

		// ��ȡǰ�����汾��ֵ
		for (int prev = 3; prev <= evolution; prev++) {

		    // ���ж�ȡÿ���汾������״̬
		    for (int row = 1; row <= 322; row++) {

			// �ж��Ƿ���ɾ��������
			boolean isDelUC = false;
			for (int i = 3; i <= evolution; i++) {
			    grid = sheet.getCell(i, row);
			    if (grid.getContents().equals("-1")) {
				isDelUC = true;
				freq[row - 1] = -1;
				occur[row - 1] = -1;
				fv[row - 1] = -1;
				break;
			    }
			}

			// �����Ѿ�ɾ����UC������һ��
			if (isDelUC)
			    continue;

			// �����ǣ���ȡ���У�����
			grid = sheet.getCell(prev, row);

			// ����freq, occur
			if (grid.getContents().equals("1")||grid.getContents().equals("2")) {
			    freq[row - 1]++;

			    if (maxFreq < freq[row - 1])
				maxFreq = freq[row - 1];

			    double tmp = occur[row - 1];
			    occur[row - 1] = tmp + prev - 2;
			}
		    }
		}

		// ����occur
		for (int row = 0; row < 322; row++) {
		    if (occur[row] == -1)
			continue;

		    if (freq[row] != 0) 
			occur[row] = evolution - 1 - (occur[row] / freq[row]);
		    else{
			Cell c = sheet.getCell(2,row);
			System.out.println(c.getContents());
		    }
			
		}

		// ���ݹ�һ��
		
		for (int row = 0; row < 322; row++) {
		    if (freq[row] == -1)
			continue;

		    freq[row] = (freq[row]) / (evolution );
		}

		// ����FV
		for (int row = 1; row <= 322; row++) {
		    grid = sheet.getCell(evolution + 1, row);
		    // ���°汾��Ϊ�޸ĵ�״̬ʱ����Ϊ1
		    if (grid.getContents().equals("1"))
			fv[row - 1] = 1;
		    else
			fv[row - 1] = 0;
		}

		// ���
		System.out.println("\nWriting to Excel Evolution "+ Integer.toString(evolution - 2));
		WritableSheet ws = wwb.createSheet("Evolution "
			+ Integer.toString(evolution - 2), 0);

		// д��ͷ
		try {
		    ws.addCell(new Label(0, 0, "Frequency"));
		    ws.addCell(new Label(1, 0, "Occurence"));
		    ws.addCell(new Label(2, 0, "FV_Actual"));
		} catch (RowsExceededException e1) {
		    e1.printStackTrace();
		} catch (WriteException e1) {
		    e1.printStackTrace();
		}

		int currRow = 1;
		try {
		    for (int j = 0; j < 322; j++) {
			if (freq[j] == -1)
			    continue;
			ws.addCell(new jxl.write.Number(0, currRow, freq[j]));
			ws.addCell(new jxl.write.Number(1, currRow, occur[j]));
			ws.addCell(new jxl.write.Number(2, currRow, fv[j]));
			currRow++;
		    }

		    freq = new double[322];
		    occur = new double[322];
		    fv = new double[322];
		    maxFreq = 0;
		    maxOccur = 0;

		} catch (RowsExceededException e) {
		    e.printStackTrace();
		} catch (WriteException e) {
		    e.printStackTrace();
		}
	    }
	}

	wb.close();

	// д�� Excel ����
	try {
	    wwb.write();
	    // �رտ�д��� Excel ����
	    wwb.close();
	    // �ر�ֻ���� Excel ����
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (WriteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
