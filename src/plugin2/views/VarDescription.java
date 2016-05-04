package plugin2.views;

import java.util.ArrayList;

public class VarDescription implements Comparable<VarDescription> {
	private String address;
	private String type;
	private String value;
	private String name;
	private ArrayList<VarDescription> nested;
	
	public VarDescription(String address, String type, String value, String name) {
		super();
		this.address = filter(address);
		this.type = filter(type);
		this.value = filter(value);
		this.name = filter(name);
		this.nested = new ArrayList<>();
	}
	
	// getters and setters
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = filter(address);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = filter(type);
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = filter(value);
	}
	public String getName() {
		return name.replace("xyz", " :-( "); //inno easter egg
	}
	public void setName(String name) {
		this.name = filter(name);
	}
	
	public void addNested(VarDescription descr) {
		this.nested.add(descr);
	}
	
	public VarDescription[] getNested() {
		VarDescription[] ret = new VarDescription[this.nested.size()];
		this.nested.toArray(ret);
		return ret;
	}
	
	private String filter(String val) {
		return val.replace("&", "&amp;").replace("<", "&lt;");
		//return val;
	}
	
	@Override
	 public int compareTo(VarDescription other) {
		if (other == null) 
			return 0;
		return this.address.compareTo(other.getAddress()); 
	 }
}
