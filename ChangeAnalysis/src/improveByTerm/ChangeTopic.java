package improveByTerm;

public class ChangeTopic {
    public String[] getKeywords() {
        return keywords;
    }
    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }
    public String getComponent() {
        return component;
    }
    public void setComponent(String component) {
        this.component = component;
    }
    String[] keywords;
    String component;
    
    public ChangeTopic(String[] keywords, String component){
	this.keywords = keywords;
	this.component = component;
    }
    
    public boolean matchTopic(String reqName, String component){
	boolean result = false;
	boolean s = false;
	boolean c = false;
	if(reqName == null)
	    return false;
	
	if(component!=null){
	    c = component.equalsIgnoreCase(this.component);
	}
	
	for(String ss:keywords){
	    if(reqName.contains(ss)){
		s = true;
	    }
	}
	
	if(component!=null){
	    if(s&&c)
		result = true;
	}else{
	    result = s;
	}
	return result;
    }
}
