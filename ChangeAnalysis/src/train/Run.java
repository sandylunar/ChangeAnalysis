package train;

import java.io.IOException;


import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class Run {

    /**
     * @param args
     */
    static final String source = "F:\\PredictReqChange.RE.2013\\data\\predict\\ChangeMetrix_13v.xls";
    static final String target = "F:\\PredictReqChange.RE.2013\\data\\predict\\PredictorValues.xls";
    static final String targetDIR = "F:\\PredictReqChange.RE.2013\\data\\predict\\trainset\\";

    public static void main(String[] args) {

	CalculatePredictFactors c = new CalculatePredictFactors();

	try {
	 // 第三步 读取演化矩阵，计算预测因子的值;读入source的sheet2
	    c.readAndCalculate(source, target);
	} catch (BiffException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} 
	catch (RowsExceededException e) {
	    e.printStackTrace();
	} catch (WriteException e) {
	    e.printStackTrace();
	}

	GenerateTrainSet.generate(target, targetDIR);
    }

}
