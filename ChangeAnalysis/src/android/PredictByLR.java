package android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.GreedyStepwise;
import weka.attributeSelection.WrapperSubsetEval;
import weka.classifiers.functions.Logistic;
import weka.core.Instances;
import weka.core.Utils;
import weka.experiment.InstanceQuery;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NominalToBinary;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Remove;
import database.Connector;

public class PredictByLR {

	public static final int BACKWARD = 1;
	public static final int SINGLE_DATASET = 2;
	public static final int CUMULATIVE_DATASET = 3;

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
	 * To Test 
	 * (starting with 0)
	 */
	public ArrayList<Integer> selectParameters() throws Exception {
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		ArrayList<Integer> selected = new ArrayList<Integer>();
		for (int i = startVersion; i < finalVersion - 1; i++) {
			int[] singlePara = getParameters(i);
			for(int p:singlePara){
				int v = map.get(p);
				map.put(p, v+1);
			}
		}
		
		double threshold = (finalVersion - 1-startVersion)/2;
		Set<Integer> keys = map.keySet();
		for(int key: keys){
			int val = map.get(key);
			if(val>threshold)
				selected.add(key);
		}
		
		System.out.println("Selected Parameter Indices are: "+selected.toString());
		return selected;
	}

	private int[] getParameters(int version) throws Exception {
		Instances data = assembleDataset(version);
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
		System.out.println("Set up Attribute Selection..");
		
		int[] indices = attsel.selectedAttributes();
		System.out.println(version+": selected attribute indices (starting with 0):\n"
				+ Utils.arrayToString(indices));
		System.out.println(version+": number of instance attribute = "
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
			nnFilter.setAttributeIndices(Integer.toString(classIndex+1));
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
		
		for(int modelIndex = startVersion; modelIndex<finalVersion-1; modelIndex++){
			trainset=assembleDataset(modelIndex);
			Remove remove = getRemove(parameters);
			remove.setInputFormat(trainset);
			Filter.useFilter(trainset, remove);
		}
		


		//int numAttr = 

	}

	private Remove getRemove(ArrayList<Integer> parameters) {
		// TODO Auto-generated method stub
		Remove remove = new Remove();
		remove.setAttributeIndicesArray(removeAttributes);
		return null;
	}

	public void selectCutoffs() {
		// TODO Auto-generated method stub

	}

}
