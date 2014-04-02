package linux;

public class Commit {
	String sha;
	String evolutionID;
	String author;
	String authorDate;
	String committer;
	String commitDate;
	StringBuffer title = new StringBuffer();
	StringBuffer summary = new StringBuffer();
	StringBuffer status = new StringBuffer();
	String detailChanges = new String();
	public void appendDetailChanges(String line){
		detailChanges+=line;
	}
	public void appendSummary(String line){
		summary.append(line);
	}
	public void appendStatus(String line){
		status.append(line);
	}
	public void appendTitle(String line){
		title.append(line);
	}
	public String getAuthor() {
		return author;
	}
	public String getAuthorDate() {
		return authorDate;
	}
	public String getCommitDate() {
		return commitDate;
	}
	public String getCommitter() {
		return committer;
	}
	public String getDetailChanges() {
		return detailChanges.toString();
	}
	public String getEvolutionID() {
		return evolutionID;
	}
	
	public String getSha() {
		return sha;
	}
	
	public String getStatus() {
		return status.toString();
	}
	
	public String getSummary() {
		return summary.toString();
	}
	
	public String getTitle() {
		return title.toString();
	}
	
	public boolean isEmpity(){
		if(sha==null||sha.isEmpty())
			return true;
		else
			return false;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public void setAuthorDate(String authorDate) {
		this.authorDate = authorDate;
	}
	public void setCommitDate(String commitDate) {
		this.commitDate = commitDate;
	}
	public void setCommitter(String committer) {
		this.committer = committer;
	}
	public void setDetailChanges(String detailChanges) {
		this.detailChanges = detailChanges;
	}
	public void setEvolutionID(String evolutionID) {
		this.evolutionID = evolutionID;
	}
	public void setSha(String sha) {
		this.sha = sha;
	} 
	public void setStatus(StringBuffer status) {
		this.status = status;
	}
	public void setSummary(StringBuffer summary) {
		this.summary = summary;
	}

	public void setTitle(StringBuffer title) {
		this.title = title;
	}
	public String toString(){
		String commit = "\nTitle: "+title+"\nSummary: "+summary+"\nStatus: "+status+"\nChanges: "+detailChanges;
		return commit;
				
	}
	public void flush() {
		// TODO Auto-generated method stub
		 sha = "";
		 author= "";
		 authorDate= "";
		 committer= "";
		 commitDate= "";
		 title=  new StringBuffer();
		 summary = new StringBuffer();
		 status = new StringBuffer();
		 detailChanges = new String();
		
		
	}

}
