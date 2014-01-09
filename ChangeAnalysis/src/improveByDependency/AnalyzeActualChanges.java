package improveByDependency;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * ����������ϵ��ʵ�ʱ���еĴ������
 * 
 * @author shilin
 * 
 */
public class AnalyzeActualChanges extends ProcessPredicitons {

    static String outputName = "DendencyInChanges";
    static String templateFullPath = "F:\\PredictReqChange.RE.2013\\data\\analyze.tpl.xls";
    static String[] operators = { "S", "P", "C" };
    WritableSheet sheetS;
    WritableSheet sheetP;
    WritableSheet sheetC;
    WritableSheet sheetStat;
    static int startRowSPC = 2;
    static int startColSPC1 = 0;
    static int startColSPC2 = 3;

    LinkedList<String> actualChanges ;
    LinkedList<DependencyBean> dependedResultS1 = new LinkedList<DependencyBean>();
    LinkedList<DependencyBean> dependedResultS2 = new LinkedList<DependencyBean>();
    LinkedList<DependencyBean> dependedResultP1 = new LinkedList<DependencyBean>();
    LinkedList<DependencyBean> dependedResultP2 = new LinkedList<DependencyBean>();
    LinkedList<DependencyBean> dependedResultC1 = new LinkedList<DependencyBean>();
    LinkedList<DependencyBean> dependedResultC2 = new LinkedList<DependencyBean>();

    public static void analyzingDependencyInChange() {
	AnalyzeActualChanges analyzer = new AnalyzeActualChanges();
	analyzer.setDependency("F:\\PredictReqChange.RE.2013\\data\\dependency.xls");
	analyzer.setOrignalResult("F:\\PredictReqChange.RE.2013\\data\\PredictResult.xls");
	analyzer.setSavingPath("F:\\PredictReqChange.RE.2013\\data\\analyze\\");
	analyzer.initProcessExcels(outputName);
	analyzer.tellMeAboutDependencyInChanges();
	analyzer.writeToSPCSheet();
	analyzer.close();
    }

    private void tellMeAboutDependencyInChanges() {
	Sheet sheet;

	// ����ÿ��Model,����ÿ��Model��SPC���,
	for (int index = 0; index < sheetNames.length; index++) {
	    actualChanges = new LinkedList<String>();

	    // ͳ�Ʊ���
	    double[] numOfChanges = new double[3];
	    double[] totalDependency = new double[3];

	    String model = sheetNames[index];
	    sheet = orignialResult.getSheet(model); // ÿ�ζ�����һ��ģ��

	    int rowN = sheet.getRows();
	    int colN = sheet.getColumns();

	    // ��������FV_Predict��Ϊ1����������
	    int reqNameCol = -1; // UC_Name��
	    int actualCol = -1; // FV_Actual��

	    // ��FV_Actual�к�Name��
	    for (int j = 0; j < colN; j++) {
		if (sheet.getCell(j, 0).getContents().equalsIgnoreCase(
			"UC_Name"))
		    reqNameCol = j;
		else if (sheet.getCell(j, 0).getContents().equalsIgnoreCase(
			"FV_Actual"))
		    actualCol = j;
	    }

	    if (actualCol == -1 || reqNameCol == -1)
		System.out.println("Դ�ļ����ˣ�û���ҵ�FV_Actual�У�");

	    // �ҵ�ActualChanges,totalDependency[]
	    System.out
		    .println("===========Start finding actual changes and total denpendencies in Sheet: "
			    + model + " ==========");

	    for (int r = 1; r < rowN; r++) {
		String reqName;

		String actualValue = sheet.getCell(actualCol, r).getContents();

		if (actualValue.equals("1")) { // �ҵ���ʵ�ʱ�� ������
		    reqName = sheet.getCell(reqNameCol, r).getContents().trim();
		    actualChanges.add(reqName);
		}
	    }

	    // ��������ϵ��ʵ�ʱ���˵�
	    for (String reqName : actualChanges) {
		for (int i = 0; i < operators.length; i++) { // ��ÿ��ʵ�ʱ�����󣬲�����������
		    String[] dependencies = getDependedReqNames(reqName,
			    operators[i]);
		    if(dependencies == null)
			continue;
		    
			int currentSize = dependencies.length;
			totalDependency[i] += currentSize;

		    for (String dependency : dependencies) { // ����ÿ��ʵ�ʱ������������
			if (actualChanges.contains(dependency)) { // �ҵ�depend
								  // and change
			    numOfChanges[i]++;
			    writeToSPCListDC(model, reqName, dependency, i);
			} else
			    writeToSPCListDnC(model, reqName, dependency, i);
		    }
		}
	    }
	    
	    System.out.println("Find actual changes: " + actualChanges.size());
	    System.out.println("Find total dependencies: S = "
		    + totalDependency[0] + "  P = " + totalDependency[1]
		    + "  C = " + totalDependency[2]);

	    // д��ͳ��sheet
	    int currCol = index*3 + 1; // ��ǰ�༭��ʼ����
	    try {
		sheetStat.addCell(new Label(currCol, 0, model));
		sheetStat.addCell(new Label(currCol, 1, "S"));
		sheetStat.addCell(new Label(currCol + 1, 1, "P"));
		sheetStat.addCell(new Label(currCol + 2, 1, "C"));
		sheetStat.addCell(new jxl.write.Number(currCol, 2,
			numOfChanges[0]));
		sheetStat.addCell(new jxl.write.Number(currCol + 1, 2,
			numOfChanges[1]));
		sheetStat.addCell(new jxl.write.Number(currCol + 2, 2,
			numOfChanges[2]));
		sheetStat.addCell(new jxl.write.Number(currCol, 3,
			totalDependency[0]));
		sheetStat.addCell(new jxl.write.Number(currCol + 1, 3,
			totalDependency[1]));
		sheetStat.addCell(new jxl.write.Number(currCol + 2, 3,
			totalDependency[2]));
	    } catch (RowsExceededException e) {
		e.printStackTrace();
	    } catch (WriteException e) {
		e.printStackTrace();
	    }
	}
    }

    private void writeToSPCSheet() { // ������List�е�ֵ����sheet
	try {
	    //sheetS
	    int row = startRowSPC;
	    int col = startColSPC1;
	    
	    for (int i = 0; i < dependedResultS1.size(); i++) {
		DependencyBean dep = dependedResultS1.get(i);
		sheetS.addCell(new Label(col, row, dep.getModel()));
		sheetS.addCell(new Label(col + 1, row, dep.getRequirement()));
		sheetS.addCell(new Label(col + 2, row, dep.getDependedReq()));
		row++;
	    }
	    
	    row = startRowSPC;
	    col = startColSPC2;
	    
	    for (int i = 0; i < dependedResultS2.size(); i++) {
		DependencyBean dep = dependedResultS2.get(i);
		sheetS.addCell(new Label(col, row, dep.getModel()));
		sheetS.addCell(new Label(col + 1, row, dep.getRequirement()));
		sheetS.addCell(new Label(col + 2, row, dep.getDependedReq()));
		row++;
	    }
	    
	    //sheetP
	    row = startRowSPC;
	    col = startColSPC1;
	    
	    for (int i = 0; i < dependedResultP1.size(); i++) {
		DependencyBean dep = dependedResultP1.get(i);
		sheetP.addCell(new Label(col, row, dep.getModel()));
		sheetP.addCell(new Label(col + 1, row, dep.getRequirement()));
		sheetP.addCell(new Label(col + 2, row, dep.getDependedReq()));
		row++;
	    }
	    
	    row = startRowSPC;
	    col = startColSPC2;
	    
	    for (int i = 0; i < dependedResultP2.size(); i++) {
		DependencyBean dep = dependedResultP2.get(i);
		sheetP.addCell(new Label(col, row, dep.getModel()));
		sheetP.addCell(new Label(col + 1, row, dep.getRequirement()));
		sheetP.addCell(new Label(col + 2, row, dep.getDependedReq()));
		row++;
	    }
	    
	    //sheetC
	    row = startRowSPC;
	    col = startColSPC1;
	    
	    for (int i = 0; i < dependedResultC1.size(); i++) {
		DependencyBean dep = dependedResultC1.get(i);
		sheetC.addCell(new Label(col, row, dep.getModel()));
		sheetC.addCell(new Label(col + 1, row, dep.getRequirement()));
		sheetC.addCell(new Label(col + 2, row, dep.getDependedReq()));
		row++;
	    }
	    
	    row = startRowSPC;
	    col = startColSPC2;
	    
	    for (int i = 0; i < dependedResultC2.size(); i++) {
		DependencyBean dep = dependedResultC2.get(i);
		sheetC.addCell(new Label(col, row, dep.getModel()));
		sheetC.addCell(new Label(col + 1, row, dep.getRequirement()));
		sheetC.addCell(new Label(col + 2, row, dep.getDependedReq()));
		row++;
	    }	    
	    
	} catch (RowsExceededException e) {
	    e.printStackTrace();
	} catch (WriteException e) {
	    e.printStackTrace();
	}
    }

    private void writeToSPCListDnC(String model, String reqName,
	    String dependency, int operator) {
	switch (operator) {
	case 0:// S
	    dependedResultS2.add(new DependencyBean(model, reqName, dependency));
	    break;
	case 1:// P
	    dependedResultP2.add(new DependencyBean(model, reqName, dependency));
	    ;
	    break;
	case 2:// C
	    dependedResultC2.add(new DependencyBean(model, reqName, dependency));
	    ;
	    break;
	}

    }

    private void writeToSPCListDC(String model, String reqName,
	    String dependency, int operator) {
	switch (operator) {
	case 0:// S
	    dependedResultS1
		    .add(new DependencyBean(model, reqName, dependency));
	    break;
	case 1:// P
	    dependedResultP1
		    .add(new DependencyBean(model, reqName, dependency));
	    ;
	    break;
	case 2:// C
	    dependedResultC1
		    .add(new DependencyBean(model, reqName, dependency));
	    ;
	    break;
	}
    }

    private String[] getDependedReqNames(String reqName, String operator) {
	String[] result = null;
	if (operator.equalsIgnoreCase("P"))
	    result = getPredictionDependencyP(reqName);
	else if(operator.equalsIgnoreCase("s"))
	    result = getRelateFromDependencySC(reqName, "similar_to");
	else if(operator.equalsIgnoreCase("c"))
	    result = getRelateFromDependencySC(reqName, "Constraint");
	
	if(result == null)
	    return null;
	
	//ȥ�������ظ�
	HashSet<String> filtered = new HashSet<String>();
	int sameName = 0;
	int duplications = 0;
	for(String s : result){
	    if(s.equals(reqName)){
		sameName++;
		continue;}
	    filtered.add(s);
	}
	duplications = result.length-filtered.size()-sameName;
	if(duplications!=0)
	    System.out.println("Filtered the dependencies: "+ sameName+"  same requirements"+"; and "+duplications+" duplications.");
	
	return filtered.toArray(new String[1]);
    }

    @Override
    protected void initProcessExcels(String outputName) {
	try {
	    InputStream is = null;
	    is = new FileInputStream(templateFullPath);
	    Workbook templateExcel = Workbook.getWorkbook(is);
	    processedResult = Workbook.createWorkbook(new File(savingPath
		    + outputName + ".xls"), templateExcel);

	    sheetS = processedResult.getSheet("S");
	    sheetP = processedResult.getSheet("P");
	    sheetC = processedResult.getSheet("C");
	    sheetStat = processedResult.getSheet("Statistic");

	} catch (IOException e) {
	    e.printStackTrace();
	} catch (BiffException e) {
	    e.printStackTrace();
	}
    }
}
