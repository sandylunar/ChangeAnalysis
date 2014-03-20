package android;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.GreedyStepwise;
import weka.attributeSelection.WrapperSubsetEval;
import weka.classifiers.functions.Logistic;
import weka.core.Instances;
import weka.core.Utils;
import weka.experiment.InstanceQuery;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Remove;
import database.Connector;

public class PredictByLR {

	public static final int BACKWARD = 1;
	public static final int SINGLE_DATASET = 2;
	public static final int CUMULATIVE_DATASET = 3;
	public static final boolean p_debug = true;

	public int parameterSelection;
	public int datasetConfig;
	public int startVersion;
	public int finalVersion;
	public int classIndex;
	public int[] removeAttributes;

	public int[] getRemoveAttributes() {
		return removeAttributes;
	}

	public void setRemoveAttributes(int[] removeAttributes) {
		this.removeAttributes = removeAttributes;
	}

	public int getClassIndex() {
		return classIndex;
	}

	public void setClassIndex(int classIndex) {
		this.classIndex = classIndex;
	}

	public int getStartVersion() {
		return startVersion;
	}

	public void setStartVersion(int startVersion) {
		this.startVersion = startVersion;
	}

	public int getFinalVersion() {
		return finalVersion;
	}

	public void setFinalVersion(int finalVersion) {
		this.finalVersion = finalVersion;
	}

	public int getParameterSelection() {
		return parameterSelection;
	}

	public void setParameterSelection(int parameterSelection) {
		this.parameterSelection = parameterSelection;
	}

	public int getDatasetConfig() {
		return datasetConfig;
	}

	public void setDatasetConfig(int datasetConfig) {
		this.datasetConfig = datasetConfig;
	}

	/**
	 * To Test (starting with 0)
	 */
	public ArrayList<Integer> selectParameters() throws Exception {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		ArrayList<Integer> selected = new ArrayList<Integer>();
		for (int i = startVersion; i < finalVersion - 1; i++) {
			int[] singlePara = getParameters(i);
			for (int p : singlePara) {
				int v = map.get(p);
				map.put(p, v + 1);
			}
		}

		double threshold = (finalVersion - 1 - startVersion) / 2;
		Set<Integer> keys = map.keySet();
		for (int key : keys) {
			int val = map.get(key);
			if (val > threshold)
				selected.add(key);
		}

		if (p_debug)
			System.out.println("Selected Parameter Indices are: "
					+ selected.toString());
		return selected;
	}

	private int[] getParameters(int version) throws Exception {
		Instances data = assembleDataset(version);
		if (p_debug)
			System.out.println("Data assumbling finishied: numAttributes = "
					+ data.numAttributes());
		Logistic model = new Logistic();

		AttributeSelection attsel = new AttributeSelection();
		WrapperSubsetEval eval = new WrapperSubsetEval();
		GreedyStepwise search = new GreedyStepwise();
		if (parameterSelection == BACKWARD)
			search.setSearchBackwards(true);
		eval.setClassifier(model);
		attsel.setEvaluator(eval);
		attsel.setSearch(search);
		attsel.SelectAttributes(data);
		if (p_debug)
			System.out.println("Set up Attribute Selection..");

		int[] indices = attsel.selectedAttributes();
		if (p_debug)
			System.out.println(version
					+ ": selected attribute indices (starting with 0):\n"
					+ Utils.arrayToString(indices));
		if (p_debug)
			System.out.println(version + ": number of instance attribute = "
					+ data.numAttributes());
		return indices;
	}

	private Instances assembleDataset(int version) throws Exception {
		InstanceQuery query = new InstanceQuery();
		query.setDatabaseURL("jdbc:mysql://localhost:3306/android_frameworks_base");
		query.setUsername("root");
		query.setPassword("123456");
		Instances data = null;
		if (this.datasetConfig == SINGLE_DATASET) {
			String querySQL = "select * from " + version + "_" + (version + 1);
			query.setQuery(querySQL);
			data = query.retrieveInstances();
		} else if (this.datasetConfig == CUMULATIVE_DATASET) {
			for (int i = startVersion; i <= version; i++) {
				String querySQL = "select * from " + i + "_" + (i + 1);
				query.setQuery(querySQL);
				Instances subdata = query.retrieveInstances();
				data = Instances.mergeInstances(data, subdata);
			}
		}
		// assign the class attribute
		Remove remove = new Remove();
		if (data != null) {
			NumericToNominal nnFilter = new NumericToNominal();
			nnFilter.setInputFormat(data);
			nnFilter.setAttributeIndices(Integer.toString(classIndex + 1));
			data = Filter.useFilter(data, nnFilter);

			data.setClassIndex(classIndex);
			remove.setInputFormat(data);
			remove.setAttributeIndicesArray(removeAttributes);
		}
		return Filter.useFilter(data, remove);
	}

	public void runForLRModels(ArrayList<Integer> parameters) throws Exception {
		// TODO Auto-generated method stub
		Instances trainset = null;

		for (int modelIndex = startVersion; modelIndex < finalVersion - 1; modelIndex++) {
			trainset = assembleDataset(modelIndex);
			Remove remove = getRemove(parameters);
			remove.setInputFormat(trainset);
			Filter.useFilter(trainset, remove);
		}

		// int numAttr =

	}

	private Remove getRemove(ArrayList<Integer> parameters) {
		// TODO Auto-generated method stub
		Remove remove = new Remove();
		remove.setAttributeIndicesArray(removeAttributes);
		return null;
	}

	public static List<String> selectParametersFromCLEResults(String sourceDir,
			int numTags, int startVersion) throws IOException {
		 PrintWriter pw = new PrintWriter(new FileWriter(new File(sourceDir+"predictors-stat.txt")));  
		 
		double threshold = (numTags - startVersion - 1) / 2;
		String filePath = null;
		LREquation eq;
		HashMap<String, Integer> predictorsStack = new HashMap<String, Integer>();
		Set<String> predictors;
		ArrayList<String> selectedPredictors = new ArrayList<String>();

		for (int i = startVersion; i < numTags - 1; i++) {
			filePath = sourceDir + i + "-model.txt";
			File dir = new File(filePath);
			if(!dir.exists())
				continue;

			eq = readEquation(filePath);
			if (p_debug)
				System.out.println("Processing " + filePath + " - " + eq);
			pw.println(filePath + " - " + eq);
			
			predictors = eq.getVariables();

			for (String variable : predictors) {
				if (predictorsStack == null
						|| !predictorsStack.containsKey(variable)) {
					predictorsStack.put(variable, 1);
				} else {
					Integer counter = predictorsStack.get(variable);
					predictorsStack.put(variable, counter + 1);
				}
			}
		}

		Set<Entry<String, Integer>> set = predictorsStack.entrySet();
		for (Entry<String, Integer> entry : set) {
			if (entry.getValue() > threshold) {
				selectedPredictors.add(entry.getKey());
				if (p_debug)
					System.out.println(entry.getKey() + ":" + entry.getValue());
				pw.println(entry.getKey() + ":" + entry.getValue());
			}
		}

		pw.println("Select predictors from CLE results: "+selectedPredictors);
		pw.close();
		return selectedPredictors;
	}

	private static LREquation readEquation(String filePath) throws IOException {
		LREquation equation = new LREquation();

		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line = "";
		int numToken;
		StringBuffer eqStr = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			if (line.contains("Equation For 1")) {
				while ((line = reader.readLine()) != null) {
					if (!line.trim().isEmpty()) {
						eqStr.append(line + "\n");
						Pattern p = Pattern.compile("\\s*|\t|\r|\n");
						Matcher m = p.matcher(line);
						line = m.replaceAll("");

						String[] tokens = line.split("\\*|\\+");
						numToken = tokens.length;
						if (numToken == 0 || numToken > 2) {
							System.err.println("Format cannot identify: "
									+ line);
							break;
						} else if (tokens[0].isEmpty()) {
							equation.setInterpret(Double.valueOf(tokens[1]));
						} else {
							equation.addElement(tokens[1],
									Double.valueOf(tokens[0]));
						}
					}
				}
			}
		}
		reader.close();
		equation.setEquation(eqStr.toString());
		return equation;
	}

	//TODO
	public static void selectCutoffsFromCLEModels(List<LREquation> equations,
			int numTags, int startVersion, String cutoffTablename,boolean drop) throws SQLException {
		String tablename;
		Connector c = new Connector();
		Statement stmt = c.getNewStatement();
		ResultSet rs;
		
		String dropSQL = "drop table if exists "+cutoffTablename;
		String createSQL = "create table if not exists "
				+ cutoffTablename
				+ "(id int primary key AUTO_INCREMENT, model varchar(10), cutoff double(8,4), m_precision double(20,4), recall double(20,4), positive double(20,4),f_measure double(20,4))";		
		if(p_debug){
			System.out.println(dropSQL+"/n"+createSQL);
			}
		
		if(drop)
			stmt.executeUpdate(dropSQL);
		stmt.executeUpdate(createSQL);
		String insertPreSQL = "insert into "+ cutoffTablename+" (model, cutoff, m_precision, recall, positive,f_measure) values (?,?,?,?,?,?)";
		

		
		double logis,actual,cutoff,predict,recall = 0,precision = 0,positive = 0,fMeasure=0;
		double c_start=0,c_end=1,c_range=0.01; //todo
		double[][] cutoffTable;
		int times = 0;
		
		LREquation eq;
		Set<String> predictors;
		
		if((c_end-c_start)%c_range == 0){
			times = (int) ((c_end-c_start)/c_range-1);
		}else{
			times = (int) Math.abs((c_end-c_start)/c_range);
		}
		
		cutoffTable = new double[times][4]; //tt,ff,tf,ft
		if(p_debug){
			System.out.println("The number of equations is : "+equations.size());}
		
		for(int i = startVersion; i < numTags; i++ ){
			tablename = i+"_"+(i+1);
			if(p_debug)
				System.out.println("Now work on: "+tablename);
				
			eq = equations.get(i-startVersion);
			if(p_debug)
				System.out.println(eq);
			if(eq.isNull())
				continue;
			predictors = eq.getVariables();
			
			rs = stmt.executeQuery("select * from "+tablename);
			while(rs.next()){
				if(p_debug){
					int id = rs.getInt("id");
					System.out.println("\t "+tablename+": scaning "+id);
				}
				
				for(String attr:predictors){
					Double value = rs.getDouble(attr);
					eq.addVariableValue(attr, value);
				}
				
				logis = eq.getLRProbability();
				
				actual = rs.getDouble("actual_change");//Todo
				rs.updateDouble("logistic", logis);
				rs.updateRow();
				
				for(int time = 1; time < times; time++ ){
					cutoff = c_range * time;
					predict = logis >= cutoff ? 1 : 0;
					if(actual==predict){
						if(actual == 1)
							cutoffTable[time][0]++; //tt
						else
							cutoffTable[time][1]++; //ff
					}else{
						if(actual == 1)
							cutoffTable[time][2]++; //tf miss 
						else 
							cutoffTable[time][3]++; //ft wrong
					}
				}
			}
			
			for(int time = 1; time < times; time++ ){
				double rc = cutoffTable[time][0]+cutoffTable[time][2];
				if(rc!=0)
					recall = cutoffTable[time][0]/(rc);
				double pc = cutoffTable[time][0]+cutoffTable[time][3];
				if(pc!=0)
					precision = cutoffTable[time][0]/(pc);
				
				positive = (pc)/(pc+cutoffTable[time][1]+cutoffTable[time][2]);
				
				if(positive+recall!=0)
					fMeasure = 2*positive*recall/(positive+recall);
				PreparedStatement preStmt = c.getNewPreparedStatement(insertPreSQL);
				preStmt.setString(1, tablename);
				preStmt.setDouble(2, c_range * time);
				preStmt.setDouble(3, precision);
				preStmt.setDouble(4, recall);
				preStmt.setDouble(5, positive);
				preStmt.setDouble(6,fMeasure);
				if(p_debug)
					System.out.println(preStmt.toString());
				preStmt.executeUpdate();
			}
		}
		c.close();
	}

	public static List<LREquation> getLREquationsFromCLEResults(
			String sourceDir, int numTags, int startVersion) throws IOException {
		ArrayList<LREquation> equations = new ArrayList<LREquation>();
		String filePath;
		LREquation eq;
		for (int i = startVersion; i < numTags ; i++) {
			filePath = sourceDir + i + "-model.txt";
			File dir = new File(filePath);
			if(!dir.exists()){
				equations.add(new LREquation());
				continue;
			}
			eq = readEquation(filePath);
			equations.add(eq);
			if (p_debug)
				System.out
						.println("Processing " + filePath + " - " + eq);
		}
		return equations;
	}

	public static void selectFinalCufoffs(int numTags, int startVersion,
			String initCutoffTable, String finalCutoffTable) throws SQLException {
		String tablename;
		Connector c = new Connector();
		Statement stmt = c.getNewStatement();
		ResultSet rs;
		
		//TODO 0.05-0.5
		for(double f_threshold=0.05; f_threshold<=0.5; f_threshold+=0.05){
			String cutoffTablename = finalCutoffTable+"_"+f_threshold;
			cutoffTablename = cutoffTablename.replace('.', '_');
			
			String dropSQL = "drop table if exists "+cutoffTablename;
			String createSQL = "create table if not exists "
					+ cutoffTablename
					+ "(id int primary key AUTO_INCREMENT, model varchar(10), cutoff double(8,4), m_precision double(20,4), recall double(20,4), positive double(20,4),f_measure double(20,4))";		
			
			if(p_debug)
				System.out.println(dropSQL+"\n"+createSQL);
			
			stmt.executeUpdate(dropSQL);
			stmt.executeUpdate(createSQL);
			
			for(int i = startVersion; i<numTags-1;i++){
				tablename = i+"_"+(i+1);

				
				rs = stmt.executeQuery("select * from "+initCutoffTable+" where f_measure = (select max(f_measure) from "+initCutoffTable+ " where model='"+tablename+"' and positive < +"+f_threshold+")");
				while(rs.next()){
					String insertPreSQL = "insert into "+ cutoffTablename+" (model, cutoff, m_precision, recall, positive,f_measure) values (?,?,?,?,?,?)";

					PreparedStatement preStmt = c.getNewPreparedStatement(insertPreSQL);
					preStmt.setString(1, tablename);
					preStmt.setDouble(2, rs.getDouble("cutoff"));
					preStmt.setDouble(3, rs.getDouble("m_precision"));
					preStmt.setDouble(4, rs.getDouble("recall"));
					preStmt.setDouble(5, rs.getDouble("positive"));
					preStmt.setDouble(6,rs.getDouble("f_measure"));
					if(p_debug)
						System.out.println(preStmt.toString());
					
					preStmt.executeUpdate();
					preStmt.close();
				}
				rs.close();
			}
		}
	}

	public static void predictFinalVersion(double cutoff, int numTags, String lr_sourceDir,
			String outputFile,String changeTableName) throws IOException, SQLException {
		
		Connector c = new Connector();
		Statement stmt = c.getNewStatement();
		Statement stmt2 = c.getNewStatement();
		ResultSet rs,rs2;
		
		StringBuffer predictions = new StringBuffer();
		String input = "";
		for(int i = numTags-1; i>0;i--){
			input = lr_sourceDir+i+"-model.txt";
			File file = new File(input);
			if(file.exists())
				break;
		}
		LREquation eq = readEquation(input);
		if(p_debug){
			System.out.println("Reading equation from "+input);
			System.out.println("Equation = "+eq);
		}
		rs = stmt.executeQuery("select * from "+(numTags-1)+"_"+numTags);
		int sum = 0;
		while(rs.next()){
			for(String attr: eq.getVariables()){
				Double value = rs.getDouble(attr);
				eq.addVariableValue(attr, value);
			}
			double logis = eq.getLRProbability();
			rs.updateDouble("logistic", logis);
			
			if(logis>=cutoff){
				rs.updateDouble("predict_change", 1);
				rs.updateRow();	
				rs2 = stmt2.executeQuery("select name from "+changeTableName+" where id="+rs.getInt("id"));
				if(rs2.next()){
					predictions.append(rs2.getString("name")+"\n");
					sum++;
				}
			}
		}
		
		c.close();
		
		predictions.append("Count: "+sum);
		File file = new File(outputFile);
		if (file.exists())
		    file.delete();
		file.createNewFile();
		BufferedWriter output = new BufferedWriter(new FileWriter(file));
		output.write(predictions.toString());
		if(p_debug){
			System.out.println("Write to file: "+outputFile);
		}
		output.close();
	}

	public static void filterFinalCutoffs(int numTags, int startVersion,String finalCutoffTable) throws SQLException {
			String tablename;
			Connector c = new Connector();
			Statement stmt = c.getNewStatement();
			ResultSet rs;
			
			for(double f_threshold=0.05; f_threshold<=0.5; f_threshold+=0.05){
				String cutoffTablename = finalCutoffTable+"_"+f_threshold;
				cutoffTablename = cutoffTablename.replace('.', '_');
				String query = "";
				
				for(int i = startVersion; i<numTags-1;i++){
					tablename = i+"_"+(i+1);
					
					rs = stmt.executeQuery("select count(*) as rowCount from "+cutoffTablename+" where model = '"+tablename+"'");
		            rs.next();
		            int rowCount = rs.getInt("rowCount");
		            if(rowCount>1){
		            	if(f_threshold<=0.25){
		            		query = "select * from "+cutoffTablename+" where model = '"+tablename+"' order by recall desc, positive" ;
	            		}else{
		            		query = "select * from "+cutoffTablename+" where model = '"+tablename+"' order by m_precision desc, positive" ;
	            		}
		            	
	            		rs = stmt.executeQuery(query);
		            	rs.first();
	            		while(rs.next()){
		            		rs.deleteRow();
		            		if(p_debug){
		    					System.out.println("Delete "+rs.getInt("id"));
		    				}
		            	}
		            }
				}
				
			}
			c.close();
	}
}
