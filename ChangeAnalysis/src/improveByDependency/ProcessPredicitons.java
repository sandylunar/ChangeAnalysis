package improveByDependency;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public abstract class ProcessPredicitons {

    protected Workbook orignialResult;
    Workbook dependency;
    protected String savingPath;
    protected WritableWorkbook processedResult;
    String[] sheetNames = { "Model 11", "Model 10", "Model 9", "Model 8",
	    "Model 7", "Model 6", "Model 5", "Model 3" };
    String statName = "汇总";
    static String[] operators = { "S", "P", "C", "SP", "SC", "PC", "SPC" };

    public void setSavingPath(String path) {
	savingPath = path;
    }

    protected void setDependency(String sourcefile) {

	// 输入的文档
	InputStream is = null;
	try {
	    is = new FileInputStream(sourcefile);
	} catch (FileNotFoundException e1) {
	    e1.printStackTrace();
	}

	try {
	    // 构造Workbook（工作薄）对象
	    dependency = Workbook.getWorkbook(is);
	} catch (BiffException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void setOrignalResult(String sourcefile) {
	// 输入的文档
	InputStream is = null;
	try {
	    is = new FileInputStream(sourcefile);
	} catch (FileNotFoundException e1) {
	    e1.printStackTrace();
	}

	try {
	    // 构造Workbook（工作薄）对象
	    orignialResult = Workbook.getWorkbook(is);
	    System.out.println("Setting the original prediciton results path: "+sourcefile);
	    System.out.println("The original results has "+orignialResult.getNumberOfSheets()+" sheets");

	} catch (BiffException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * 读取关系表中的Precondition关系
     * 
     * @param reqName
     *            要查找的需求名称
     * 
     * @return 所有以reqName为前置条件的需求名称
     */
    protected String[] getPredictionDependencyP(String reqName) {

	Sheet depSheet = dependency.getSheet(0);

	HashSet<String> targetIDs = new HashSet<String>(); // reqName同名需求的ID
	HashSet<String> relatedNames = new HashSet<String>(); // reqName變化則同時變化的需求

	// 找出所有同名需求ID，放入targetIDs中备用
	for (int r = 0; r < depSheet.getRows(); r++) {
	    if (reqName.equals(depSheet.getCell(4, r).getContents().trim())) {
		String targetID = depSheet.getCell(0, r).getContents();// reqName同名需求的ID
		targetIDs.add(targetID);
	    }
	}

	// 对于targetIDs中的每一个ID，找出他们出现在preondition X 位置上的需求，需求名称存在 relatedNames
	Iterator<String> iterator = targetIDs.iterator();
	if (iterator.hasNext()) {
	    String currentID = iterator.next();

	    // 看每一行，找出现了targetID的precondition
	    for (int r = 0; r < depSheet.getRows(); r++) {
		for (int m = 5; m < depSheet.getColumns(); m = m + 2) {
		    String type = depSheet.getCell(m, r).getContents();
		    String pID = depSheet.getCell(m + 1, r).getContents();
		    if (type.equalsIgnoreCase("Precondition")
			    && pID.equalsIgnoreCase(currentID)) {
			String relatedName = depSheet.getCell(4, r)
				.getContents();
			relatedNames.add(relatedName);
		    }
		}
	    }
	}

	if (targetIDs.isEmpty() || relatedNames.isEmpty())
	    return null;

	String[] result = new String[relatedNames.size()];
	relatedNames.toArray(result);
	return result;

    }
/**
 * 
 * @param reqName
 * @param category = "similar_to" or "Constraint"
 * @return
 */
    protected String[] getRelateFromDependencySC(String reqName, String category) {

	Sheet depSheet = dependency.getSheet(0);

	HashSet<String> targetIDs = new HashSet<String>(); // reqName同名需求的ID
	HashSet<String> relatedNames = new HashSet<String>(); //

	for (int r = 0; r < depSheet.getRows(); r++) {
	    if (reqName.equals(depSheet.getCell(4, r).getContents().trim())) {

		String targetID = depSheet.getCell(0, r).getContents();// 與reqName同名需求的ID
		targetIDs.add(targetID);

		// 记录当前ID的相似关系
		for (int m = 5; m < depSheet.getColumns(); m = m + 2) {
		    String type = depSheet.getCell(m, r).getContents();
		    if (type.equalsIgnoreCase(category)) {
			String selfID = depSheet.getCell(m + 1, r)
				.getContents();

			// 找ID的名字
			for (int n = 0; n < depSheet.getRows(); n++) {
			    if (depSheet.getCell(0, n).getContents().equals(
				    selfID)) {
				relatedNames.add(depSheet.getCell(4, n)
					.getContents());
			    }
			}
		    }

		}

		// 遍历6,8,10,...,END,找targetID
		for (int m = 6; m < depSheet.getColumns(); m = m + 2) {
		    for (int n = 0; n < depSheet.getRows(); n++) {
			if (depSheet.getCell(m, n).getContents().equals(
				targetID)) {
			    String type = depSheet.getCell(m - 1, n)
				    .getContents();
			    if (type.equalsIgnoreCase(category))
				relatedNames.add(depSheet.getCell(4, n)
					.getContents().trim());
			}
		    }
		}
	    }
	}

	if (targetIDs.isEmpty() || relatedNames.isEmpty())
	    return null;

	String[] result = new String[relatedNames.size()];
	relatedNames.toArray(result);

	return result;

    }

    public void close() {
	try {
	    // 从内存中写入文件中
	    processedResult.write();
	    processedResult.close();

	} catch (IOException e) {
	    e.printStackTrace();
	} catch (WriteException e) {
	    e.printStackTrace();
	}
    }

    protected abstract void initProcessExcels(String tag);
    
}
