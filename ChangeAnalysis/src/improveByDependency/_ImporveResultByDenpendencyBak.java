package improveByDependency;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


public class _ImporveResultByDenpendencyBak {
    
     Workbook orignialResult;
     Workbook dependency;
     String savingPath;
    
     String[] sheetNames = {"Model 11", "Model 10", "Model 9","Model 8","Model 7","Model 6","Model 5","Model 3"};
    
     WritableWorkbook[] improves = new WritableWorkbook[7];//improveS,improveP,improveC,improveSP,improveSC,improvePC,improveSPC;
    
    
    

    /**
     * @param args
     */

    
    void initImproveExcels() {
        try {   
            improves[0] = Workbook.createWorkbook(new File(savingPath+"\\improveS.xls"),orignialResult);   
            improves[1] = Workbook.createWorkbook(new File(savingPath+"\\improveP.xls"),orignialResult); 
            improves[2] = Workbook.createWorkbook(new File(savingPath+"\\improveC.xls"),orignialResult); 
            improves[3] = Workbook.createWorkbook(new File(savingPath+"\\improveSP.xls"),orignialResult); 
            improves[4] = Workbook.createWorkbook(new File(savingPath+"\\improveSC.xls"),orignialResult); 
            improves[5] = Workbook.createWorkbook(new File(savingPath+"\\improvePC.xls"),orignialResult); 
            improves[6] = Workbook.createWorkbook(new File(savingPath+"\\improveSPC.xls"),orignialResult); 
            
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
    }

    void setSavingPath(String path) {
	savingPath = path;
    }

    void setOrignalResult(String sourcefile) {
	//输入的文档
	InputStream is = null;
	try {
	    is = new FileInputStream(sourcefile);
	} catch (FileNotFoundException e1) {
	    e1.printStackTrace();
	}
           
        try {   
            //构造Workbook（工作薄）对象   
            orignialResult=Workbook.getWorkbook(is);
            
        } catch (BiffException e) {   
            e.printStackTrace();   
        } catch (IOException e) {   
            e.printStackTrace();   
        } 
    }

    void setDependency(String sourcefile) {
	
	//输入的文档
	InputStream is = null;
	try {
	    is = new FileInputStream(sourcefile);
	} catch (FileNotFoundException e1) {
	    e1.printStackTrace();
	}
           
        try {   
            //构造Workbook（工作薄）对象   
            dependency=Workbook.getWorkbook(is);   
        } catch (BiffException e) {   
            e.printStackTrace();   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
    }

    public  void improve(){
	WritableSheet [] sheets = new WritableSheet[7];
	
	//遍历9个Model，每次同时开7个sheet，依次为improveS,improveP,improveC,improveSP,improveSC,improvePC,improveSPC;
	for(String model : sheetNames){
	    for(int i = 0; i < 7; i++){
		    sheets[i] = improves[i].getSheet(model);
		    }
	    
	    int rowN = sheets[0].getRows();
	    int colN = sheets[0].getColumns();
	    HashSet<String> update0 = new HashSet<String> ();
	    HashSet<String> update1 = new HashSet<String> ();
	    HashSet<String> update2 = new HashSet<String> ();
	    HashSet<String> update3 = new HashSet<String> ();
	    HashSet<String> update4 = new HashSet<String> ();
	    HashSet<String> update5 = new HashSet<String> ();
	    HashSet<String> update6 = new HashSet<String> ();

	    
	    //读出所有FV_Predict列为1的用例名称
	    int predictCol = -1; //FV_Predict列
	    int reqNameCol = -1; // UC_Name列
	    
	    //找FV_Predict列和Name列
	    for (int j =0; j < colN; j++){
		if(sheets[0].getCell(j,0).getContents().equalsIgnoreCase("FV_Predict"))
		   predictCol = j;
		else if(sheets[0].getCell(j,0).getContents().equalsIgnoreCase("UC_Name"))
		    reqNameCol = j;
	    }
	    if(predictCol == -1 || reqNameCol == -1)
		System.out.println("源文件错了，居然没有找到FV_Predict列！");
	    
	    //读出每个FV_Predict列为1的用例名称,将需要更新的相关用例名称存在updates中，
	    for (int r = 1; r < rowN; r++) {
		String reqName;

		String predictValue = sheets[0].getCell(predictCol, r)
			.getContents();
		
		if (predictValue.equals("1")) { //找到了預測為1 的需求
		    reqName = sheets[0].getCell(reqNameCol, r).getContents().trim();

		    String[] improvedSimilar;
		    improvedSimilar = getRelateFromDependency(reqName,
			    "Similar");
		    if (improvedSimilar != null) {
			for (String s : improvedSimilar) {
			    update0.add(s);
			    update3.add(s);
			    update4.add(s);
			    update6.add(s);
			}
		    }

		    String[] improvedPrecondition;
		    improvedPrecondition = getRelateFromDependency(reqName,
			    "Precondition");
		    if (improvedPrecondition != null) {
			for (String s : improvedPrecondition) {
			    update1.add(s);
			    update3.add(s);
			    update5.add(s);
			    update6.add(s);
			}
		    }

		    String[] improvedConstraint;
		    improvedConstraint = getRelateFromDependency(reqName,
			    "Constraint");
		    if (improvedConstraint != null) {
			for (String s : improvedConstraint) {
			    
			    update2.add(s);
			    update4.add(s);
			    update5.add(s);
			    update6.add(s);
			}
		    }
		}

	    }
	    //遍历完每个预测变更的需求，遍历updates[i]，将sheets[i]中同样用例名称的预测结果改成1，加颜色
	    
	    for (int i = 0; i < 7; i++) {
		HashSet<String> tmp = null;
		
		switch(i){
		case 0: tmp = update0;
		break;
		case 1: tmp = update1;
		break;
		case 2: tmp = update2;
		break;
		case 3: tmp = update3;
		break;
		case 4: tmp = update4;
		break;
		case 5: tmp = update5;
		break;
		case 6: tmp = update6;
		break;
		}
		
		Iterator<String> iterator = tmp.iterator();
		
		if (iterator.hasNext()) {
		    String updateName = iterator.next();

		    for (int r = 1; r < rowN; r++) {
			String currentName = sheets[i].getCell(reqNameCol, r)
				.getContents();
			if (currentName != null
				&& currentName.trim().equals(updateName.trim())) {// ??
			    String currentPV = sheets[i].getCell(predictCol, r)
				    .getContents();
			    if (currentPV.equals("0")) { // 找到了一个

				WritableCellFormat wcfColor = new WritableCellFormat();
				try {
				    wcfColor
					    .setBackground(jxl.format.Colour.RED);
				    jxl.write.Number newPredict = new jxl.write.Number(
					    predictCol, r, 1, wcfColor);
				    sheets[i].addCell(newPredict);
				} catch (WriteException e) {
				    e.printStackTrace();
				}
			    }
			}
		    }
		}
	    }
	}
	
	

    }
    
    private  String[] getRelateFromDependency(String reqName,
	    String string) {
	
	Sheet depSheet = dependency.getSheet(0);

	
	
	if(string.equalsIgnoreCase("Precondition") ||string.equalsIgnoreCase("Constraint")){
		HashSet<String> targetIDs = new HashSet<String>();
		HashSet<String> relatedNames = new HashSet<String>();
		
	    //检索Col5-END中的出现目标需求ID的ID列表
	    
	    //查到目标需求的ID
		for(int r = 0; r < depSheet.getRows();r++){
		    if(reqName.equals(depSheet.getCell(4,r).getContents().trim())){
			String targetID = depSheet.getCell(0,r).getContents();
			targetIDs.add(targetID);
			
			//遍历6,8,10,...,END,找targetID
			for(int m = 6; m < depSheet.getColumns();m+=2){
			    for (int n = 0; n <depSheet.getRows();n++){
				if(depSheet.getCell(m, n).getContents().equals(targetID)){
				    String type = depSheet.getCell(m-1,n).getContents();
				    if(type.equalsIgnoreCase(string))
					relatedNames.add(depSheet.getCell(4,n).getContents().trim());
				}
			    }
			}
		    }
		}
		
		if(targetIDs.isEmpty()||relatedNames.isEmpty())
		    return null;
		
		String[] result = new String[relatedNames.size()];
		relatedNames.toArray(result);
		
		return result;
	}
	
	if(string.equalsIgnoreCase("Similar")){
		HashSet<String> targetIDs = new HashSet<String>();
		HashSet<String> relatedNames = new HashSet<String>();
		
	    for(int r = 0; r < depSheet.getRows();r++){
		    if(reqName.equals(depSheet.getCell(4,r).getContents().trim())){
			String targetID = depSheet.getCell(0,r).getContents();
			targetIDs.add(targetID);
			
			//记录当前ID的相似关系
			for(int m = 5; m < depSheet.getColumns();m+=2){
			    String type = depSheet.getCell(m,r).getContents();
			    if(type.equalsIgnoreCase("similar_to")){
				String selfID = depSheet.getCell(m+1,r).getContents();
				
				//找ID的名字
				for(int n =0; n < depSheet.getRows();n++){
				    if(depSheet.getCell(0, n).getContents().equals(selfID))
					relatedNames.add(depSheet.getCell(4, n).getContents());
				}
			    }
				    
			}
			
			
			//遍历6,8,10,...,END,找targetID
			for(int m = 6; m < depSheet.getColumns();m+=2){
			    for (int n = 0; n <depSheet.getRows();n++){
				if(depSheet.getCell(m, n).getContents().equals(targetID)){
				    String type = depSheet.getCell(m-1,n).getContents();
				    if(type.equalsIgnoreCase("similar_to"))
					relatedNames.add(depSheet.getCell(4,n).getContents().trim());
				}
			    }
			}
		    }
		}
		
		if(targetIDs.isEmpty()||relatedNames.isEmpty())
		    return null;
		
		String[] result = new String[relatedNames.size()];
		relatedNames.toArray(result);
		
		return result;
	}
	
	return null;
	
    }

    public  void close(){
        try {   
            //从内存中写入文件中   
            for (WritableWorkbook wwb : improves){
        	wwb.write();
        	wwb.close();
            }
            
        } catch (IOException e) {   
            e.printStackTrace();   
        } catch (WriteException e) {   
            e.printStackTrace();   
        }
    }

}
