package improveByTerm;

import jxl.write.WriteException;

public class Run {

    /**
     * @param args
     */
    public static void main(String[] args) {
	//找出预测不到的需求用例名称
	if(args[0].equals("label"))
	    AnalyzePredictResults.labelMissingChanges("PredictResults_labels");
	else if (args[0].equals("useterms")){
	    ImproveByTopics analyzer = new ImproveByTopics();
		analyzer.setOrignalResult("F:\\PredictReqChange.RE.2013\\data\\improveByTerm\\PredictResults_labels.xls");
		analyzer.setSavingPath("F:\\PredictReqChange.RE.2013\\data\\improveByTerm\\");
		analyzer.initProcessExcels("PredictResults_labels_useTopic");
		try {
		    analyzer.updatePredictResults();
		} catch (WriteException e) {
		    e.printStackTrace();
		}
		analyzer.close(); 
	}
	
    }

}
