package improveByTerm;

import java.util.LinkedList;

public class ModelTopics {
    String model;
    LinkedList<ChangeTopic> topics;
    public String getModel() {
	return model;
    }

    public void setModel(String model) {
	this.model = model;
    }

    public LinkedList<ChangeTopic> getTopics() {
	return topics;
    }

    public void setTopics(LinkedList<ChangeTopic> topics) {
	this.topics = topics;
    }


    public ModelTopics(String model, LinkedList<ChangeTopic> topics) {
	this.model = model;
	this.topics = topics;
    }

    public ModelTopics(String model) {
	this.model = model;
	topics = new LinkedList<ChangeTopic>();
    }

    public void addTopic(ChangeTopic topic) {
	topics.add(topic);
    }
    
    public boolean matchTopic(String reqName, String component){
	for(int i = 0 ; i<topics.size();i++){
	    ChangeTopic cTopic = topics.get(i);
	    if(cTopic.matchTopic(reqName, component))
		return true;
	}
	return false;
    }
}
