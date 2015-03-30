package plan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Prop implements Cloneable{
	private String name;
	private Map<String,String> values = new HashMap<String,String>();
	private List<String> valuenames = new ArrayList<String>();
	
	public Prop(String name, String n1,String v1) {
		super();
		this.name = name;
		this.values.put(n1,v1);
		this.valuenames.add(n1);
	}
	public Prop(String name, String n1,String v1,String n2,String v2) {
		super();
		this.name = name;
		this.values.put(n1,v1);
		this.valuenames.add(n1);
		this.values.put(n2,v2);
		this.valuenames.add(n2);
	}
	public void Substitute(Map<String,String> sub){
		for (String v: valuenames){
			values.put(v, sub.get(v));
		}
	}
	
	public Map<String, String> getValues() {
		return values;
	}
	public List<String> getValuenames() {
		return valuenames;
	}
	public boolean equals2(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Prop other = (Prop) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		//result = prime * result + ((values == null) ? 0 : values.keySet().hashCode());
		result = prime * result + ((values == null) ? 0 : stringValues().hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Prop other = (Prop) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!this.stringValues().equals(other.stringValues())) //!this.values.keySet().equals(other.values.keySet()) || 
			return false;
		return true;
	}
	private String stringValues(){
		String sVal="";
		for (String s:valuenames){
			sVal+=values.get(s);			
		}
		return sVal;
	}
	@Override
	public String toString() {
		String temp="";
		for (String value: valuenames) temp += (values.get(value)==null?"?"+value:values.get(value));
		return name+"("+temp+")";
	}
	protected Prop copy(){
		Prop p;
		if (valuenames.size() == 2){
			p = new Prop(name,valuenames.get(0),values.get(valuenames.get(0)),valuenames.get(1),values.get(valuenames.get(1)));
		}else p = new Prop(name,valuenames.get(0),values.get(valuenames.get(0)));
		return p;
	}
	
	
}
