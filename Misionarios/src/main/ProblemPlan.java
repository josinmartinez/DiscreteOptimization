package main;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import actions.*;
import state.*;
import actions.misionarios.*;
import actions.puzzle8.*;


public class ProblemPlan {
	public Set<IState> explored = new HashSet<IState>();

	private Map<IState,IAction> plan = new HashMap<IState,IAction>();
	public enum Strategy {lifo, fifo};
	private int n = 0;
	private int level=0;
	private StringBuilder sb = new StringBuilder();; 
	
	public Problem createProblem1(){
		IState initialState = new State(1,1,1,0,0,0);initialState.setLevel(level);
		IState goalState    = new State(0,0,0,1,1,1);goalState.setLevel(level);
		List<IAction> actions = new ArrayList<IAction>(Arrays.asList(new M1(),new M2(), new C1(), new C2(), new M1C1()));
		return new Problem(initialState, goalState, actions);
	}
	
	public Problem createProblem2(){
		IState initialState = new State8(new int[][]{{8,1,7},{4,5,6},{2,0,3}});
		IState goalState    = new State8(new int[][]{{0,1,2},{3,4,5},{6,7,8}});
		List<IAction> actions = new ArrayList<IAction>(Arrays.asList(new UpAction(),new DownAction(), new LeftAction(), new RightAction()));
		return new Problem(initialState, goalState, actions);
	}
	
	public static void main(String[] args) throws Exception {
		
		ProblemPlan plan = new ProblemPlan();
		String planning = "";
		//planning = plan.treeSearch(plan.createProblem2(), Strategy.fifo);
		planning = plan.aStarTreeSearch(plan.createProblem2());
		System.out.println(planning);		
	}
		
	public String treeSearch(Problem problem, Strategy strategy) throws Exception{
		Deque<IState> fringe = new LinkedList<IState>();
		fringe.add(problem.initialState());
		plan.put(problem.initialState(), null);
		while(true){
			if (fringe.isEmpty()){
				PrintWriter writer = new PrintWriter("exploredNodes.txt", "UTF-8");
				writer.println(sb.toString());
				writer.close();
				System.out.println(showExplored());
				throw new Exception("failure: "+ explored.size()); 
			}
			IState node = selectFrom(fringe, strategy);
			if (problem.goalTest(node)){
				System.out.println(node.toString());
				return showExplored();//pathTo(node);
			}
			Set<IState> expanded = expand(problem,node); 
			expanded.removeAll(fringe);
			fringe.addAll(expanded);
		}
	}
	
	public String aStarTreeSearch(Problem problem) throws Exception{
		PriorityQueue<IState> fringe = new PriorityQueue<IState>();
		fringe.add(problem.initialState());
		Set<IState> allNodes = new HashSet<IState>();
		while (true){
			if (fringe.isEmpty()) throw new Exception("failure: ");
			IState node = fringe.poll();
			if (problem.goalTest(node)){
				System.out.println(node.toString());
				return Integer.toString(node.getLevel());//pathTo(node);
			}
			for (IState succesor: expand(problem, node)){
				if (!allNodes.contains(succesor)){
					succesor.setF(succesor.getLevel()+heuristico(succesor));
					fringe.add(succesor);
					System.out.println(succesor.toString());
					allNodes.add(succesor);
				}
			}
		}
	}
	
	private int heuristico(IState state){
		return 0;
		//return manhattanD(state);
	}
	private int manhattanD(IState state){
		State8 state8 = (State8)state;
		int distance = 0;
		int n = state8.getState()[0].length;
		for (int i=0;i<n;i++)
			for (int j=0;j<n;j++){
				int value = state8.getValue(i, j);
				distance += Math.abs(i - Math.floor(value / n)) + Math.abs(j-(value % n));
			}
		return distance;
	}
	
	private IState selectFrom(Deque<IState> fringe, Strategy strategy) {
		switch(strategy){
			case fifo: return fringe.removeFirst();
			case lifo: return fringe.removeLast();
		}
		return null;
	}

	private Set<IState> expand(Problem problem, IState node){
		List<IAction> actions = problem.getActions();
		Set<IState> newNodes = new HashSet<IState>();
		if (!explored.contains(node)){
			explored.add(node);
			for (IAction action: actions){
				IState newState = action.executeOn(node);
				if (newState !=null && newState.isValid() && !explored.contains(newState)){
					newState.setLevel(node.getLevel()+1);
					newNodes.add(newState);
					//plan.put(newState, action);
					n++;
					sb.append(newState.toString()+"\n");
				}
			}
		}
		return newNodes;
	}

	private String pathTo(IState node) {
		boolean fin = false;
		List<String> result = new ArrayList<String>();
		do{
			IAction action = plan.get(node);
            result.add(node.toString()+" :"+explored.size()); 
			if (action!=null && (node=action.unexecuteOn(node))!=null){
				result.add("\n");
				result.add(" => ");
				result.add(action.toString());
			}else {
				fin = true;
			}
		}while (!fin);
		Collections.reverse(result);
		return result.toString();
	}
	
	private String showExplored(){
		int[] nlevel = new int[100];
		for (IState s: explored){
			nlevel[s.getLevel()]+=1;
		}
		for (int i=0;i<100;i++){
			System.out.println("["+i+"]="+nlevel[i]);
		}
		return nlevel.toString();
	}
}
