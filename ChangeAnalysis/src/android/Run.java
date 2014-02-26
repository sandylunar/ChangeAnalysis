package android;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Run {
	
	public static int numTags;
	public static int startVersion;
	public static List<String> predictors = null;
	public static List<LREquation> equations = null;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		String userDir = System.getProperty("user.dir");
		String targetDir = userDir + "\\output\\android-frameworks";
		String tagPath = userDir + "/data/tag-frameworks-trim.txt";
		String changeTableName = "change_history";
		String tagTableName = "android_tags";
		ArrayList<String> tags = PrepareRawData.readTags(tagPath);
		numTags = tags.size();
		startVersion = 1;

		if (args[0].equalsIgnoreCase("prepare-raw-data")) {
			System.out.println("1. Reading tags...");

			System.out.println("Tags are: \n" + tags.toString());

			// 生成bat文件
			String batPath = userDir + "\\output\\git-diff.bat";
			String execDir = "G:\\android-src\\frameworks";
			System.out.println("2. Generate Bat file:" + batPath);
			try {
				PrepareRawData.generateBat(batPath, targetDir, execDir, tags);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (args[0].equalsIgnoreCase("dump-raw-data")) {
			// 将txt存入数据库
			try {
				if (args.length > 1 && args[1].equalsIgnoreCase("-t")) {
					PrepareRawData.dumpTagToMySQL(tagTableName, tags);
				}
				PrepareRawData.dumpRawTxtToMySQL(changeTableName, targetDir,
						tags);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (args[0].equalsIgnoreCase("prepare-trainset")) {
			CalculatePredictFactorsForSQL c = new CalculatePredictFactorsForSQL();
			try {
				c.readAndCalculateFromMySQL(changeTableName, numTags, 0);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (args[0].equalsIgnoreCase("predict-lr")) {
			if (args.length < 3) {
				System.err.println("Please input other parameters");
				return;
			}

			PredictByLR predictor = new PredictByLR();

			if (args[1].equalsIgnoreCase("-b")) {
				predictor.setParameterSelection(PredictByLR.BACKWARD);
			} else
				System.err.println("Unknown input:" + args[1]);

			if (args[2].equalsIgnoreCase("-s")) {
				predictor.setDatasetConfig(PredictByLR.SINGLE_DATASET);

			} else if (args[2].equalsIgnoreCase("-c"))
				predictor.setDatasetConfig(PredictByLR.CUMULATIVE_DATASET);
			else
				System.err.println("Unknown input:" + args[2]);
			
			predictor.setStartVersion(1);
			predictor.setFinalVersion(3);//tags.size();
			predictor.setClassIndex(10);
			predictor.setRemoveAttributes(new int[]{0,6,7,9,11,12});

			ArrayList<Integer> parameters = predictor.selectParameters();
			System.out.println("Seleted parameters are at columns(start from 0): "+parameters.toString());
			//predictor.runForLRModels(parameters);
			//predictor.selectCutoffs();
		}
		
		if(args[0].equalsIgnoreCase("add-column-dataset")){
			PrepareRawData.addColumnDataset(numTags);
		}
		
		if(args[0].equalsIgnoreCase("assemble-all-cle")){
			PrepareRawData.assembleAllForCle("dataset_all",numTags);
		}
		if(args[0].equalsIgnoreCase("select-predictors")){
			String fp_sourceDir = userDir + "\\output\\thirdLRforResults\\";
			String lr_sourceDir = userDir + "\\output\\fourthLRforResults\\";
			String initCutoffTable = "cutoff_all";
			//String predictCufoff = "cutoff_predict";
			if(args[1].equalsIgnoreCase("-f")){
				predictors = PredictByLR.selectParametersFromCLEResults(fp_sourceDir,numTags,startVersion);
				System.out.println("Select predictors from CLE results: "+predictors);
			}

			if(args[1].equalsIgnoreCase("-s")){
				equations = PredictByLR.getLREquationsFromCLEResults(lr_sourceDir,numTags,startVersion);
				
				PredictByLR.selectCutoffsFromCLEModels(equations,numTags,startVersion,initCutoffTable);
			}
			if(args[1].equalsIgnoreCase("-a")){
				PredictByLR.selectFinalCufoffs(numTags,startVersion,initCutoffTable,"cutoff_final");
			}
			
			//table 'cutoff_predict' must be prepared in db
			if(args[1].equalsIgnoreCase("-p")){
				String outputFile = userDir + "\\output\\final-version-predictions.txt";
				PredictByLR.predictFinalVersion(0.04,numTags,lr_sourceDir,outputFile,changeTableName);
			}
		}
	}

	public static void callCmd(String locationCmd) {
		try {
			Process child = Runtime.getRuntime().exec(
					"cmd.exe /C start " + locationCmd);
			InputStream in = child.getInputStream();
			while (((int) in.read()) != -1) {
			}
			in.close();
			try {
				child.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
