package android;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.RecoveryAndUpdate;

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
		String datasetAll = "dataset_all";
		ArrayList<String> tags = PrepareRawData.readTags(tagPath);
		numTags = tags.size();
		startVersion = 1;

		/*
		 * Step 1
		 * input: tags
		 * output: bat, git-diff to txt
		 */
		if (args[0].equalsIgnoreCase("prepare-git-diff")) {
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

		/*
		 * Step 2
		 * input: tags,git-diff in txt format
		 * output: Tables>android_tags,change_history
		 */
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

		/*if (args[0].equalsIgnoreCase("predict-lr")) {
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
		}*/
		
		if(args[0].equalsIgnoreCase("add-dataset-assemble")){
			PrepareRawData.alterTableColumnDataset(numTags);
			PrepareRawData.assembleAllForCle(datasetAll,numTags);
			System.out.println("Done...");
		}
		
		/**
		 * ChangeAnalysis done, next need Clementine 12 to the job: 
		 * input: dataset_all
		 * output: backward stepwise models
		 */
		
		/*
		 * Input: backward models
		 * output: predictors
		 */
		if(args[0].equalsIgnoreCase("select-predictors-from-backwards")){
			String fp_sourceDir = userDir + "\\output\\cle_frameworks_one\\backward-7\\";
			predictors = PredictByLR.selectParametersFromCLEResults(fp_sourceDir,numTags,startVersion);
			System.out.println("Select predictors from CLE results: "+predictors);
		}
		
		/**
		 * ChangeAnalysis done, next need Clementine 12 to the job: 
		 * input: predictors
		 * output: enter models
		 */
		
		/*
		 * input: enter models
		 * output: initCutoffTable,finalCutoffTable
		 */
		if(args[0].equalsIgnoreCase("build-init-cutoff")){
			String lr_sourceDir = userDir + "\\output\\cle_frameworks_one\\enter-7\\";
			String initCutoffTable = "cutoff_all_single_7"; 
			equations = PredictByLR.getLREquationsFromCLEResults(lr_sourceDir,numTags,startVersion);
			PredictByLR.selectCutoffsFromCLEModels(equations,numTags,startVersion,initCutoffTable);
			
		}
		
		if(args[0].equalsIgnoreCase("build-final-cutoff")){
			String initCutoffTable = "cutoff_all_single_7"; //"cutoff_all_single_7"
			String finalCutoffTable = "cutoff_final_single_7"; //"cutoff_final_single_7"
			PredictByLR.selectFinalCufoffs(numTags,startVersion,initCutoffTable,finalCutoffTable);
			PredictByLR.filterFinalCutoffs(numTags,startVersion,finalCutoffTable);
		}
		
		if(args[0].equalsIgnoreCase("output-prediction-results")){
			String lr_sourceDir = userDir + "\\output\\cle_frameworks_one\\enter-6\\";
			String outputFile = lr_sourceDir + "final-version-predictions-single.txt";
			double cutoff = 0.04;
			PredictByLR.predictFinalVersion(cutoff,numTags,lr_sourceDir,outputFile,changeTableName);
		}
		
		if(args[0].equalsIgnoreCase("recovery")){
			//recovery from the last table to make descriptive statistic
			//transform: freq*33, seq*32, distance/(freq*33),recency,lifecycle,id,predict
			String targetTableName = "32_33";
			String outputTableName = "32_33_recovery";
			RecoveryAndUpdate.recoveryFromTheLastTable(targetTableName,outputTableName);
		}
		
		if(args[0].equalsIgnoreCase("update-new-database")){
			//update the change_history table to filter source code
			
			if(args[1].equalsIgnoreCase("-two")){
				String[] includeTypes = {".c",".cpp",".h",".cs",".java",".js"};
				RecoveryAndUpdate.filterDataFromDBTwo(changeTableName,numTags,includeTypes);
				}
			else{
				String[] includeTypes = {".c",".cpp",".h",".cs",".java",".js",".conf",".xml",".txt",".properties","bat","notice","readme","makefile"};
				RecoveryAndUpdate.filterDataToNewDB(changeTableName,includeTypes);
				}
		}
		if(args[0].equalsIgnoreCase("build-all-changes")){
			String tablename = "changes_in_all";
			RecoveryAndUpdate.generateAllChanges(tablename,datasetAll);
		}if(args[0].equalsIgnoreCase("scan-last-files")){
			String root = "L:\\android\\frameworks";
			System.out.println("Start..");
			String[] includeTypes = {".c",".cpp",".h",".cs",".java",".js"};
			RecoveryAndUpdate rau = new RecoveryAndUpdate();
			rau.scanFile(root,"29_30",includeTypes,0);
		}if(args[0].equalsIgnoreCase("scan-stable-2-29")){
			String output = userDir + "\\output\\stable-files-2-29.txt";
			RecoveryAndUpdate.scanStableFiles(output,"29_30");
		}if(args[0].equalsIgnoreCase("scan-freq-29")){
			String output = userDir + "\\output\\freq-files-29-30.txt";
			RecoveryAndUpdate rau = new RecoveryAndUpdate();
			rau.scanFreqFiles(output,"29_30",0);
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
