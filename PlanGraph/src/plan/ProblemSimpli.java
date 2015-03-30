package plan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProblemSimpli {
	private List<Action> listActions;
	private Set<Prop>  s;
	private Set<Prop>  goal;
	

	public List<Action> getListActions() {
		Action move = new Action("M",new ArrayList<String>(Arrays.asList("r","from","to")));
		move.addprecond(new Prop("adjacent","from",null,"to",null));
		move.addprecond(new Prop("at","r",null,"from",null));
		move.addeffec(new Prop("at","r",null,"to",null));
		move.addeffec(new Prop("at","r",null,"from",null));

		Action load = new Action("L",new ArrayList<String>(Arrays.asList("c","r","l")));
		load.addprecond(new Prop("at","r",null,"l",null));
		load.addprecond(new Prop("in","c",null,"l",null));
		load.addprecond(new Prop("unloaded","r",null));
		load.addeffec(new Prop("loaded","c",null,"r",null));
		load.addeffec(new Prop("unloaded","r",null));
		load.addeffec(new Prop("in","c",null,"l",null));

		Action unload = new Action("U",new ArrayList<String>(Arrays.asList("c","r","l")));
		unload.addprecond(new Prop("at","r",null,"l",null));
		unload.addprecond(new Prop("loaded","c",null,"r",null));
		unload.addeffec(new Prop("unloaded","r",null));
		unload.addeffec(new Prop("in","c",null,"l",null));
		unload.addeffec(new Prop("loaded","c",null,"r",null));

/*
		Action take = new Action("T",new ArrayList<String>(Arrays.asList("k","l","c","else","p")));
		take.addprecond(new Prop("belong","k",null,"l",null));
		take.addprecond(new Prop("attached","p",null,"l",null));
		take.addprecond(new Prop("empty","k",null));
		take.addprecond(new Prop("in","c",null,"p",null));
		take.addprecond(new Prop("top","c",null,"p",null));
		take.addprecond(new Prop("on","c",null,"else",null));
		take.addeffec(new Prop("holding","k",null,"c",null));
		take.addeffec(new Prop("top","else",null,"p",null));
		take.addeffec(new Prop("in","c",null,"p",null));
		take.addeffec(new Prop("top","c",null,"p",null));
		take.addeffec(new Prop("on","c",null,"else",null));
		take.addeffec(new Prop("empty","k",null));

		Action put = new Action("P",new ArrayList<String>(Arrays.asList("k","l","c","else","p")));
		put.addprecond(new Prop("belong","k",null,"l",null));
		put.addprecond(new Prop("attached","p",null,"l",null));
		put.addprecond(new Prop("holding","k",null,"c",null));
		put.addprecond(new Prop("top","else",null,"p",null));
		put.addeffec(new Prop("in","c",null,"p",null));
		put.addeffec(new Prop("top","c",null,"p",null));
		put.addeffec(new Prop("on","c",null,"else",null));		
		put.addeffec(new Prop("top","else",null,"p",null));		
		put.addeffec(new Prop("holding","k",null,"c",null));		
		put.addeffec(new Prop("empty","k",null));
*/		
		listActions = new ArrayList<Action>(Arrays.asList(move,load,unload));
		return listActions;
	}

	public Set<Prop> getInitialState() {
		s = new HashSet<Prop>();
		
		s.add(new Prop("adjacent","l1","1","l2","2"));
		s.add(new Prop("adjacent","l1","2","l2","1"));
		s.add(new Prop("at","r","r","l","1"));
		s.add(new Prop("at","r","q","l","2"));
		s.add(new Prop("unloaded","r","r"));
		s.add(new Prop("unloaded","r","q"));
		s.add(new Prop("in","c","a","l","1"));
		s.add(new Prop("in","c","b","l","2"));
		return s;
	}

	public Set<Prop> getGoal() {
		goal = new HashSet<Prop>();
		goal.add(new Prop("in","c","ca","p","p2"));
		goal.add(new Prop("in","c","cb","p","q2"));
		goal.add(new Prop("in","c","cc","p","p2"));
		goal.add(new Prop("in","c","cd","p","q2"));
		goal.add(new Prop("in","c","ce","p","q2"));
		goal.add(new Prop("in","c","cf","p","q2"));
		return goal;
	}
	
	
	
}
