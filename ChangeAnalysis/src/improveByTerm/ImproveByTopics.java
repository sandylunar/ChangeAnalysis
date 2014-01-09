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
	
	modelTopics[0].addTopic(new ChangeTopic(new String[]{"������־"},"Task Management"));
	modelTopics[0].addTopic(new ChangeTopic(new String[]{"�Զ���"},"Measure management"));
	modelTopics[1].addTopic(new ChangeTopic(new String[]{"������Ʒ"},"Product management"));
	modelTopics[2].addTopic(new ChangeTopic(new String[]{"ȱ����"},"Quality management"));
	modelTopics[2].addTopic(new ChangeTopic(new String[]{"�ʼ�����"},"System management"));
	modelTopics[3].addTopic(new ChangeTopic(new String[]{"�ƻ����"},"Change management"));
	modelTopics[3].addTopic(new ChangeTopic(new String[]{"������Ʒ�ļ���"},"Product management"));
	modelTopics[4].addTopic(new ChangeTopic(new String[]{"�������ֲ�"},"Project management"));
	modelTopics[4].addTopic(new ChangeTopic(new String[]{"����","����"},"Task management"));
	modelTopics[5].addTopic(new ChangeTopic(new String[]{"����"},"Quality management"));
	modelTopics[5].addTopic(new ChangeTopic(new String[]{"��Ŀ�б�"},"Project management"));
	modelTopics[5].addTopic(new ChangeTopic(new String[]{"���"},"Change management"));
	modelTopics[6].addTopic(new ChangeTopic(new String[]{"����ͳ��"},""));
	modelTopics[6].addTopic(new ChangeTopic(new String[]{"���"},"Process management"));
	modelTopics[6].addTopic(new ChangeTopic(new String[]{"������Ʒ"},"Product management"));
	modelTopics[7].addTopic(new ChangeTopic(new String[]{"�ʲ�","����"},"Process management"));
	modelTopics[7].addTopic(new ChangeTopic(new String[]{"����"},"Quality management"));
	modelTopics[7].addTopic(new ChangeTopic(new String[]{"��չ����","Ԥ��"},"Project management"));
	

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

	    int predictCol = -1; // FV_Predict��
	    int reqNameCol = -1; // UC_Name��
	    int actualCol = -1; // FV_Actual��
	    int moduleCol = -1;

	    // ��FV_Predict�к�Name��
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
		System.out.println("What��Դ�ļ����ˣ���Ȼû���ҵ��У�");
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
