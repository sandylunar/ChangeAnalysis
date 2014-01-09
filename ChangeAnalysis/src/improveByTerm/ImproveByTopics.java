package improveByTerm;

import java.io.File;
import java.io.IOException;

import jxl.Workbook;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import improveByDependency.ProcessPredicitons;

public class ImproveByTopics extends ProcessPredicitons {
    
    String[] sheetNames = { "Model 11", "Model 10", "Model 9", "Model 8",
	    "Model 7", "Model 6", "Model 5", "Model 3" };
    ModelTopics[] modelTopics = new ModelTopics[sheetNames.length];
    
    
    @Override
    protected void initProcessExcels(String outputFileName) {

	try {
	    processedResult = Workbook.createWorkbook(new File(savingPath
		    + outputFileName + ".xls"), orignialResult);

	} catch (IOException e) {
	    e.printStackTrace();
	}
	
	for(int i = 0; i < sheetNames.length;i++){
	    modelTopics[i]=new ModelTopics(sheetNames[i]);
	}
	
	modelTopics[0].addTopic(new ChangeTopic(new String[]{"工作日志"},"Task Management"));
	modelTopics[0].addTopic(new ChangeTopic(new String[]{"自定义"},"Measure management"));
	modelTopics[1].addTopic(new ChangeTopic(new String[]{"工作产品"},"Product management"));
	modelTopics[2].addTopic(new ChangeTopic(new String[]{"缺陷项"},"Quality management"));
	modelTopics[2].addTopic(new ChangeTopic(new String[]{"邮件发送"},"System management"));
	modelTopics[3].addTopic(new ChangeTopic(new String[]{"计划变更"},"Change management"));
	modelTopics[3].addTopic(new ChangeTopic(new String[]{"工作产品文件夹"},"Product management"));
	modelTopics[4].addTopic(new ChangeTopic(new String[]{"工作量分布"},"Project management"));
	modelTopics[4].addTopic(new ChangeTopic(new String[]{"跟踪","批量"},"Task management"));
	modelTopics[5].addTopic(new ChangeTopic(new String[]{"批量"},"Quality management"));
	modelTopics[5].addTopic(new ChangeTopic(new String[]{"项目列表"},"Project management"));
	modelTopics[5].addTopic(new ChangeTopic(new String[]{"变更"},"Change management"));
	modelTopics[6].addTopic(new ChangeTopic(new String[]{"数据统计"},""));
	modelTopics[6].addTopic(new ChangeTopic(new String[]{"浏览"},"Process management"));
	modelTopics[6].addTopic(new ChangeTopic(new String[]{"工作产品"},"Product management"));
	modelTopics[7].addTopic(new ChangeTopic(new String[]{"资产","评估"},"Process management"));
	modelTopics[7].addTopic(new ChangeTopic(new String[]{"问题"},"Quality management"));
	modelTopics[7].addTopic(new ChangeTopic(new String[]{"进展报告","预警"},"Project management"));
	

    }

    public void updatePredictResults() throws WriteException {
	WritableSheet sheet;
	for (int i = 0; i < sheetNames.length; i++) {
	    sheet = processedResult.getSheet(sheetNames[i]);
	    System.out
		    .println("\n================================\nNow working on Sheet: "
			    + sheet.getName());

	    int rowN = sheet.getRows();
	    int colN = sheet.getColumns();

	    int predictCol = -1; // FV_Predict列
	    int reqNameCol = -1; // UC_Name列
	    int actualCol = -1; // FV_Actual列
	    int moduleCol = -1;

	    // 找FV_Predict列和Name列
	    for (int j = 0; j < colN; j++) {
		String cont = sheet.getCell(j, 0).getContents();
		if (cont.equalsIgnoreCase(
			"FV_Predict"))
		    predictCol = j;
		else if (cont.equalsIgnoreCase(
			"UC_Name"))
		    reqNameCol = j;
		else if (cont.equalsIgnoreCase(
			"FV_Actual"))
		    actualCol = j;
		else if(cont.equalsIgnoreCase("Module")){
		    moduleCol = j;
		}
		    
	    }

	    if (predictCol == -1 || reqNameCol == -1 || actualCol == -1) {
		System.out.println("What！源文件错了，居然没有找到列！");
		continue;
	    }

	    for (int r = 1; r < rowN; r++) {
		String reqName = sheet.getCell(reqNameCol,r).getContents();
		String module = sheet.getCell(moduleCol,r).getContents();
		
		String currentPV = sheet.getCell(predictCol, r).getContents();
		String actl = sheet.getCell(actualCol, r).getContents();
		
		if (currentPV.equals("0")) {
		    
		    if(modelTopics[i].matchTopic(reqName, module)){
			WritableCellFormat wcfColor = new WritableCellFormat();
			wcfColor.setBackground(jxl.format.Colour.ORANGE);
			jxl.write.Number newPredict = new jxl.write.Number(
				predictCol, r, 1, wcfColor);
			sheet.addCell(newPredict);
			System.out.println(sheet.getCell(reqNameCol, r).getContents()+"\t"+sheet.getCell(reqNameCol+1, r).getContents());
		    }
		}
	    }
	    System.out.println();
	}
	
    }
}
