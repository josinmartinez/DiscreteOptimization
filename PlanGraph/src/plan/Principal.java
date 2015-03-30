package plan;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Principal {

	List<Set<Prop>> pLayer = new ArrayList<Set<Prop>>(); // P0....Pk
	List<Set<Action>> aLayer = new ArrayList<Set<Action>>(); // A1....Ak
	
	public void addAplicable(List<Action> actions, Action op, List<Prop> precond,Map<String,String> sub, Set<Prop> s){
		if (precond.isEmpty()) {
			Action a = op.sub(sub);
			if (a.toString().contains("?")||a.toString().contains("caca")||a.toString().contains("cbcb")||a.toString().contains("cccc")||a.toString().contains("cdcd")||a.toString().contains("cece")||a.toString().contains("cfcf")) 
				return;
			else {
				actions.add(a);
				//System.out.println("Accion: "+a.toString());
			}
		}
		else{
			Prop prec = precond.get(0);
			//System.out.println("Precond: "+prec.toString());
			Map<String,String> sub2;
			for (Prop prop: s){
				if (prec.equals2(prop)){
					sub2 = extend(sub,prop,prec);
					if (sub2 != null) {
						//if (!precond.isEmpty()) precond.remove(prec);
						List<Prop> newPrecond = new ArrayList<Prop>(precond); 
						newPrecond.remove(prec);
						//System.out.println("    extend: "+prec.toString()+"<-"+prop.toString());
						//System.out.println("    subs: "+sub2.toString());
						addAplicable(actions,op,newPrecond,sub2,s);
					}
				}
			}
		}
	}
	
	private Map<String, String> extend(Map<String, String> sub, Prop prop, Prop prec) {
		if (prop.equals(prec)) 
			return null;
		Map<String,String> sub2 = new HashMap<String,String>();
		for (String s:sub.keySet()){
			sub2.put(s, sub.get(s));
		}
		for (int i=0;i<prop.getValuenames().size();i++){
			String pv = prec.getValuenames().get(i);
			String vpv = prop.getValues().get(prop.getValuenames().get(i));
			String value = sub.get(pv);
			if (value != null){
				if (value.equals(vpv)) continue;
				else {
					return null;
				}
			}else sub2.put(pv,vpv);
		}
		return sub2;
	}

	public static void main(String[] args) {
		Principal plan = new Principal();
		Problem p = new Problem();

		plan.pLayer.add(new HashSet<Prop>(p.getInitialState()));
		plan.aLayer.add(new HashSet<Action>());
		
		List<Action> listActions = p.getListActions();
		Set<Prop> s = p.getInitialState();
		int i=0; boolean flag=true; boolean fixpoint=false;
		show(plan,i);
		do{
			List<Action> appActions = new ArrayList<Action>();
			for (Action ac : listActions){
				plan.addAplicable(appActions, ac, ac.precond, new HashMap<String,String>(), s);
			}
			plan.aLayer.add(new HashSet<Action>(appActions));
			Set<Prop> effects = new HashSet<Prop>(plan.pLayer.get(i));

			for (Action a: appActions){
				effects.addAll(a.getEffects());
			}
			
			if (plan.pLayer.get(i).containsAll(p.getGoal())) 
				flag=false;
			plan.pLayer.add(new HashSet<Prop>(effects));
			plan.pLayer.get(i+1).removeAll(p.getComplementary());
			i++; s = plan.pLayer.get(i); 
			show(plan,i);
			if (plan.pLayer.get(i).size()==plan.pLayer.get(i-1).size() && plan.aLayer.get(i).size()==plan.aLayer.get(i-1).size())
				fixpoint=true;
		}while(flag && !fixpoint);
		System.out.println(i+", "+flag+" ,"+fixpoint);
	}

	private static void show(Principal plan, int i) {
		System.out.print("A["+(i)+"]:("+plan.aLayer.get(i).size()+") ");
		for (Action a: plan.aLayer.get(i)){
			System.out.print(a+" - ");			
		}
		System.out.println();
		System.out.print("P["+(i)+"]:("+plan.pLayer.get(i).size()+") ");
		for (Prop p: plan.pLayer.get(i)){
			System.out.print(p+" - ");			
		}
		System.out.println();
	}

}
