package plugin2.views;

public class ActivationRecord {
	private String lineNumber;
	private String functionName;
	private String fileName;
	private String startAddress;
	private String endAddress;
	private String staticLink;
	private VarDescription[] vars;
	private VarDescription[] args;


	public ActivationRecord(String lineNumber, String functionName, String fileName, String startAddress, String endAddress, String staticLink, 
			VarDescription[] vars, VarDescription[] args) {
		super();
		this.lineNumber = lineNumber;
		this.functionName = functionName;
		this.fileName = fileName;
		this.startAddress = startAddress;
		this.endAddress = endAddress;
		this.staticLink = staticLink;
		this.vars = vars;
		this.args = args;
	}
	
	// getters and setters
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getStartAddress() {
		return startAddress;
	}
	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
	}
	public String getEndAddress() {
		return endAddress;
	}
	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
	}
	public VarDescription[] getVars() {
		return vars;
	}
	public void setVars(VarDescription[] vars) {
		this.vars = vars;
	}
	public VarDescription[] getArgs() {
		return args;
	}
	public void setArgs(VarDescription[] args) {
		this.args = args;
	}
	public String getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getStaticLink() {
		return staticLink;
	}
	public void setStaticLink(String staticLink) {
		this.staticLink = staticLink;
	}
}
