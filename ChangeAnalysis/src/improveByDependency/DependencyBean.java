package improveByDependency;

public class DependencyBean {
    
    public DependencyBean(String model, String requirement, String dependedReq){
	this.model = model;
	this.requirement = requirement;
	this.dependedReq = dependedReq;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getRequirement() {
        return requirement;
    }
    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }
    public String getDependedReq() {
        return dependedReq;
    }
    public void setDependedReq(String dependedReq) {
        this.dependedReq = dependedReq;
    }
    private String model;
    private String requirement;
    private String dependedReq;
}
