package plan;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Action implements Cloneable{
	private String name;
	List<Prop> precond = new ArrayList<Prop>();
	List<Prop> effects = new ArrayList<Prop>();
	List<String> vnames = new ArrayList<String>();
	Map<String, String> values = new HashMap<String,String>();
	
	public Action(String name,List<String> vnames) {
		super();
		this.name = name;
		this.vnames = vnames;
	}
	public String getName() {
		return name;
	}
	public List<Prop> getPrecond() {
		return precond;
	}
	public void addprecond(Prop p){
		precond.add(p);
	}
	public void addeffec(Prop p){
		effects.add(p);
	}
	public List<Prop> getEffects() {
		List<Prop> list = new ArrayList<Prop>();
		for (Prop p: effects){
			Prop t = (Prop) p.copy();
			t.Substitute(values);
			list.add(t);
		}
		return list;
	}
	public String toString() {
		String temp="";
		for (String value: vnames) temp += (values.get(value)==null?"?"+value:values.get(value));
		return name+"("+temp+")";
	}
	public Action sub(Map<String, String> sub) {
		Action a;
		try {
			a = (Action)this.clone();
			a.values = sub;
			return a;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((values == null) ? 0 : values.values().toString().hashCode());
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
		Action other = (Action) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!this.values.values().toString().equals(other.values.values().toString())) 
			return false;
		return true;
	}
	
}
