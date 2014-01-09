package improveByTerm;

import java.io.File;
import java.io.IOException;

import jxl.Workbook;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import improveByDependency.ProcessPredicitons;

public class AnalyzePredictResults extends ProcessPredicitons {

    @Override
    protected void initProcessExcels(String outputFileName) {

	try {
	    processedResult = Workbook.createWorkbook(new File(savingPath
		    + outputFileName + ".xls"), orignialResult);

	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

    public static void labelMissingChanges(String outputFileName) {
	AnalyzePredictResults analyzer = new AnalyzePredictResults();
	analyzer
		.setOrignalResult("F:\\PredictReqChange.RE.2013\\data\\PredictResult.xls");
	analyzer
		.setSavingPath("F:\\PredictReqChange.RE.2013\\data\\improveByTerm\\");
	analyzer.initProcessExcels(outputFileName);
	analyzer.labelMissingChanges();
	analyzer.close();
    }

    private void labelMissingChanges() {
	WritableSheet sheet;
	for (int i = 0; i < processedResult.getNumberOfSheets(); i++) {
	    sheet = processedResult.getSheet(i);
	    System.out
		    .println("\n================================\nNow working on Sheet: "
			    + sheet.getName());

	    int rowN = sheet.getRows();
	    int colN = sheet.getColumns();

	    int predictCol = -1; // FV_Predict列
	    int reqNameCol = -1; // UC_Name列
	    int actualCol = -1; // FV_Actual列

	    // 找FV_Predict列和Name列
	    for (int j = 0; j < colN; j++) {
		if (sheet.getCell(j, 0).getContents().equalsIgnoreCase(
			"FV_Predict"))
		    predictCol = j;
		else if (sheet.getCell(j, 0).getContents().equalsIgnoreCase(
			"UC_Name"))
		    reqNameCol = j;
		else if (sheet.getCell(j, 0).getContents().equalsIgnoreCase(
			"FV_Actual"))
		    actualCol = j;
	    }

	    if (predictCol == -1 || reqNameCol == -1 || actualCol == -1) {
		System.out.println("What！源文件错了，居然没有找到列！");
		continue;
	    }

	    for (int r = 1; r < rowN; r++) {
		WritableCell cell = null;
		// 读取预测值
		String currentPV = sheet.getCell(predictCol, r).getContents();

		// 标注出来预测不到的结果为黄色
		String actl = sheet.getCell(actualCol, r).getContents();
		if (currentPV.equals("0") && actl.equals("1")) {
		    cell = sheet.getWritableCell(reqNameCol, r);
		    try {
			WritableCellFormat wcfColor = new WritableCellFormat();
			wcfColor.setBackground(jxl.format.Colour.YELLOW);
			cell.setCellFormat(wcfColor);
			System.out.println(cell.getContents()+"\t"+sheet.getCell(reqNameCol+1, r).getContents());
			//sheet.addCell(cell);
		    } catch (WriteException e) {
			e.printStackTrace();
		    }
		}
	    }
	    System.out.println();
	}
    }
}
