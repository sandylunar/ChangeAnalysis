package android;

import java.util.HashMap;
import java.util.Set;

class LREquation{
	HashMap<String,Double> elements;
	Double interpret;
	String equation;
	HashMap<String,Double> inputs;
	
	public String getEquation() {
		return equation;
	}

	public void setEquation(String equation) {
		this.equation = equation;
	}

	public LREquation(){
		elements = new HashMap<String,Double>();
		inputs = new HashMap<String,Double>();
		interpret = 0.0;
	}
	
	public HashMap<String,Double> getEquationExcludeInterpret() {
		return elements;
	}

	public Double getInterpret() {
		return interpret;
	}
	public void setInterpret(Double interpret) {
		this.interpret = interpret;
	}

	public void addElement(String variable,Double weight){
		elements.put(variable, weight);
	}
	
	public Set<String> getVariables(){
		return elements.keySet();
	}
	
	public void addVariableValue(String variable,Double value){
		inputs.put(variable, value);
	}
	
	public double getLRProbability(){
		Set<String> keys =  elements.keySet();
		double sum = 0;
		for(String var : keys){
			sum += elements.get(var)*inputs.get(var);
		}
		sum+=interpret;
		double prob = 1/(1+Math.exp(-sum));
		return prob;
	}
	
	public String toString(){
		return "interpret = "+ interpret+"; elements = "+elements.toString()+"\t Inputs = "+inputs.toString();
	}
}
