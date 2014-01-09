package improveByDependency;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import jxl.Workbook;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class ImporvePredictionResults extends ProcessPredicitons {

    Workbook orignialResult;
    Workbook dependency;
    String savingPath;
    
    int statRow = 61;
    int statCol = 3;

    /**
     * 	����������ϵ����Ԥ����
     */
    protected static void improvingResultsByDep() {

	for (String s : operators) {
	    ImporvePredictionResults imp = new ImporvePredictionResults();
	    imp.setDependency("F:\\PredictReqChange.RE.2013\\data\\dependency.xls");
	    imp.setOrignalResult("F:\\PredictReqChange.RE.2013\\data\\PredictResult.xls");
	    imp.setSavingPath("F:\\PredictReqChange.RE.2013\\data\\improve\\");
	    imp.improve(s);
	    imp.close();
	}
    }

    /**
     * @param category ��ʾ������ϵ��ѡ��S=similar, P=Precondition,C=Constraint
     * ��7����ϣ�S,P,C,SP,SC,PC,SPC
     */
    public void improve(String category) {
	initProcessExcels(category);
	String[] improvedSimilar;
	String[] improvedConstraint;
	String[] improvedPrecondition;
	
	
	WritableSheet sheet;

	// ����9��Model��ÿ��ͬʱ��7��sheet������ΪimproveS,improveP,improveC,improveSP,improveSC,improvePC,improveSPC;
	for (int index = 0; index < sheetNames.length;index++) {
	    
	    String model  = sheetNames[index];
	    
	    sheet = processedResult.getSheet(model);
	    
	    System.out.println("\n================================\nNow working on Sheet: "+ model +" for "+category);

	    int rowN = sheet.getRows();
	    int colN = sheet.getColumns();
	    
	    //���ͳ�Ʒ���ָ��
	    int redN = 0;
	    int greenN = 0;
	    int redActual = 0;

	    LinkedList<String> update6 = new LinkedList<String>(); //��ǰModel x����Ҫ���µ���������

	    // ��������FV_Predict��Ϊ1����������
	    int predictCol = -1; // FV_Predict��
	    int reqNameCol = -1; // UC_Name��
	    int actualCol = -1; //FV_Actual��

	    // ��FV_Predict�к�Name��
	    for (int j = 0; j < colN; j++) {
		if (sheet.getCell(j, 0).getContents().equalsIgnoreCase(
			"FV_Predict"))
		    predictCol = j;
		else if (sheet.getCell(j, 0).getContents().equalsIgnoreCase(
			"UC_Name"))
		    reqNameCol = j;
		else if(sheet.getCell(j, 0).getContents().equalsIgnoreCase(
		"FV_Actual"))
		    actualCol = j;
	    }
	    
	    if (predictCol == -1 || reqNameCol == -1)
		System.out.println("Դ�ļ����ˣ���Ȼû���ҵ�FV_Predict�У�");

	    // ����ÿ��FV_Predict��Ϊ1����������,����Ҫ���µ�����������ƴ���updates�У�
	    for (int r = 1; r < rowN; r++) {
		String reqName;

		String predictValue = sheet.getCell(predictCol, r)
			.getContents();

		if (predictValue.equals("1")) { // �ҵ����A�y��1 ������
		    reqName = sheet.getCell(reqNameCol, r).getContents().trim();
		    if (category.contains("S")) {
			improvedSimilar = getRelateFromDependencySC(reqName,
				"similar_to");
			if (improvedSimilar != null) {
			    for (String s : improvedSimilar) {
				update6.add(new String(s));
			    }
			}
		    }

		    if (category.contains("P")) {
			improvedPrecondition = getPredictionDependencyP(reqName);
			if (improvedPrecondition != null) {
			    for (String s : improvedPrecondition) {
				update6.add(new String(s));
			    }
			}
		    }

		    if (category.contains("C")) {
			improvedConstraint = getRelateFromDependencySC(reqName,
				"Constraint");
			if (improvedConstraint != null) {
			    for (String s : improvedConstraint) {
				update6.add(new String(s));
			    }
			}
		    }
		}
		
	    }
	    
	    System.out.println("Candidate requirements to be updated:");
	    for(int i =0; i<update6.size();i++){
		System.out.println((i+1)+": "+update6.get(i));
	    }
	    
	    // ������ÿ��Ԥ���������󣬱���updates[i]����sheets[i]��ͬ���������Ƶ�Ԥ�����ĳ�1������ɫ
	    // ���ж�ȡ��������
	    for (int r = 1; r < rowN; r++) {
		String currentName = sheet.getCell(reqNameCol, r).getContents();
		
		WritableCell cell = null;
		// ��ȡԤ��ֵ
		String currentPV = sheet.getCell(predictCol, r).getContents();
		
		//��ע����Ԥ�ⲻ���Ľ��Ϊ��ɫ
		String actl = sheet.getCell(actualCol,r).getContents();
		if(currentPV.equals("0")&&actl.equals("1")){
			cell = sheet.getWritableCell(reqNameCol, r);
			    try {
				WritableCellFormat wcfColor = new WritableCellFormat();
				wcfColor.setBackground(jxl.format.Colour.YELLOW);
				cell.setCellFormat(wcfColor);
				//sheet.addCell(cell);
			    } catch (WriteException e) {
				e.printStackTrace();
			    }
		}

		// ÿ�м�����������Ƿ��ڹ����б���,�����ǰ����������updates����
		if (currentName == null || !hasName(update6, currentName))
		    continue;

		// Ԥ��Ϊ1����Ҳ�����ڹ����б���
		if (currentPV.equals("1")) {
			cell = sheet.getWritableCell(predictCol, r);
		    try {
			WritableCellFormat wcfColor = new WritableCellFormat();
			wcfColor.setBackground(jxl.format.Colour.GREEN);
			cell.setCellFormat(wcfColor);
			greenN++;
			//sheet.addCell(cell);
		    } catch (WriteException e) {
			e.printStackTrace();
		    }
		    }
		

		// Ԥ��Ϊ0�������ڹ����б��У�����Ԥ��Ϊ1
		if (currentPV.equals("0")) {
		    try {
			WritableCellFormat wcfColor = new WritableCellFormat();
			wcfColor.setBackground(jxl.format.Colour.RED);
			jxl.write.Number newPredict = new jxl.write.Number(
				predictCol, r, 1, wcfColor);
			sheet.addCell(newPredict);
			redN++;
			System.out.println("Fixed: "+ currentName);
			if(actl.equals("1")){
			    redActual++;
			}
		    } catch (WriteException e) {
			e.printStackTrace();
		    }
		}
	    }
	    
	    //��ͳ�ƽ��д�������
	    WritableSheet statSheet = processedResult.getSheet(statName);
	    
	    try {
		statSheet.addCell(new Number(statCol+sheetNames.length-1-index,statRow,redN));
		statSheet.addCell(new Number(statCol+sheetNames.length-1-index,statRow+1,greenN));
		statSheet.addCell(new Number(statCol+sheetNames.length-1-index,statRow+2,redActual));
	    } catch (RowsExceededException e) {
		e.printStackTrace();
	    } catch (WriteException e) {
		e.printStackTrace();
	    }
	}
    }

    private boolean hasName(LinkedList<String> update6, String currentName) {
	for(int i =0; i<update6.size();i++){
	    String inList = update6.get(i);
	    if(inList.trim().equalsIgnoreCase(currentName.trim()))
		return true;
	}
	return false;
    }
    
    @Override
    protected void initProcessExcels(String tag) {
	try {
	    File file = new File(savingPath + tag + ".xls");
	    processedResult = Workbook.createWorkbook(file, orignialResult);

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
