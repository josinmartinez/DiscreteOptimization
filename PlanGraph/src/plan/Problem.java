package plan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Problem {
	private List<Action> listActions;
	private Set<Prop>  s;
	private Set<Prop>  goal;
	private Set<Prop>  cmp;
	
	

	public List<Action> getListActions() {
		Action move = new Action("M",new ArrayList<String>(Arrays.asList("r","from","to")));
		move.addprecond(new Prop("adjacent","from",null,"to",null));
		move.addprecond(new Prop("at","r",null,"from",null));
		move.addprecond(new Prop("free","to",null));
		move.addeffec(new Prop("at","r",null,"to",null));
		move.addeffec(new Prop("free","from",null));
		move.addeffec(new Prop("free","to",null));
		move.addeffec(new Prop("at","r",null,"from",null));

		Action load = new Action("L",new ArrayList<String>(Arrays.asList("k","l","c","r")));
		load.addprecond(new Prop("at","r",null,"l",null));
		load.addprecond(new Prop("belong","k",null,"l",null));
		load.addprecond(new Prop("holding","k",null,"c",null));
		load.addprecond(new Prop("unloaded","r",null));
		load.addeffec(new Prop("loaded","r",null,"c",null));
		load.addeffec(new Prop("unloaded","r",null));
		load.addeffec(new Prop("empty","k",null));
		load.addeffec(new Prop("holding","k",null,"c",null));

		Action unload = new Action("U",new ArrayList<String>(Arrays.asList("k","l","c","r")));
		unload.addprecond(new Prop("belong","k",null,"l",null));
		unload.addprecond(new Prop("at","r",null,"l",null));
		unload.addprecond(new Prop("loaded","r",null,"c",null));
		unload.addprecond(new Prop("empty","k",null));
		unload.addeffec(new Prop("unloaded","r",null));
		unload.addeffec(new Prop("holding","k",null,"c",null));
		unload.addeffec(new Prop("loaded","r",null,"c",null));
		unload.addeffec(new Prop("empty","k",null));

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
		
		listActions = new ArrayList<Action>(Arrays.asList(take,move,load,unload,put));
		return listActions;
	}

	public Set<Prop> getInitialState() {
		s = new HashSet<Prop>();
		s.add(new Prop("adjacent","l1","l1","l2","l2"));
		s.add(new Prop("adjacent","l1","l2","l2","l1"));

		s.add(new Prop("attached","p","p1","l","l1"));
		s.add(new Prop("attached","p","q1","l","l1"));
		s.add(new Prop("attached","p","p2","l","l2"));
		s.add(new Prop("attached","p","q2","l","l2"));

		s.add(new Prop("belong","k","k1","l","l1"));
		s.add(new Prop("belong","k","k2","l","l2"));
		
		s.add(new Prop("in","c","ca","p","p1"));
		s.add(new Prop("in","c","cb","p","p1"));
		s.add(new Prop("in","c","cc","p","p1"));
		s.add(new Prop("in","c","cd","p","q1"));
		s.add(new Prop("in","c","ce","p","q1"));
		s.add(new Prop("in","c","cf","p","q1"));

		s.add(new Prop("on","c1","ca","c2","pallet"));
		s.add(new Prop("on","c1","cb","c2","ca"));
		s.add(new Prop("on","c1","cc","c2","cb"));
		s.add(new Prop("on","c1","cd","c2","pallet"));
		s.add(new Prop("on","c1","ce","c2","cd"));
		s.add(new Prop("on","c1","cf","c2","ce"));

		s.add(new Prop("top","c","cc","p","p1"));
		s.add(new Prop("top","c","cf","p","q1"));
		s.add(new Prop("top","c","pallet","p","p2"));
		s.add(new Prop("top","c","pallet","p","q2"));
		
		s.add(new Prop("at","r","r1","l","l1"));
		s.add(new Prop("unloaded","r","r1"));
		s.add(new Prop("free","l","l2"));

		s.add(new Prop("empty","k","k1"));
		s.add(new Prop("empty","k","k2"));
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
	
	public Set<Prop> getComplementary(){
		cmp = new HashSet<Prop>();
		cmp.add(new Prop("on","c1","ca","c2","ca"));
		cmp.add(new Prop("on","c1","cb","c2","cb"));
		cmp.add(new Prop("on","c1","cc","c2","cc"));
		cmp.add(new Prop("on","c1","cd","c2","cd"));
		cmp.add(new Prop("on","c1","ce","c2","ce"));
		cmp.add(new Prop("on","c1","cf","c2","cf"));
		
		return cmp;
	}
	
	
	
}
