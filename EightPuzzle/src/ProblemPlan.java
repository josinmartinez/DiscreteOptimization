import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class ProblemPlan {
	int n=0;
	private List<State> path = new ArrayList<State>();
	int[][] ini = {{4,1,2},{3,0,5},{6,7,8}};//{{2,7,4},{8,1,6},{5,3,0}};
	int[][] goal = {{0,1,2},{3,4,5},{6,7,8}};
	State initialState 	= new State(ini);
	State goalState  	= new State(goal);
	
	Problem problem = new Problem(initialState,goalState);
	public enum Strategy {lifo, fifo};
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ProblemPlan plan = new ProblemPlan();
		plan.treeSearch(plan.problem, Strategy.lifo);
	}
	
	public String treeSearch(Problem problem, Strategy strategy) throws Exception{
		Deque<State> fringe = new LinkedList<State>();
		fringe.add(problem.initialState());

		while(true){
			if (fringe.isEmpty()) throw new Exception("failure"); 
			State node = selectFrom(fringe, strategy);
			System.out.println(node +" "+n++);
			if (n%10 == 0)
				System.out.println();
			if (problem.goalTest(node)) return pathTo(node);
			Set<State> uniques = expand(problem,node);
			uniques.removeAll(fringe);
			//fringe.addAll(expand(problem,node));
			fringe.addAll(uniques);
		}
	}
	
	private State selectFrom(Deque<State> fringe, Strategy strategy) {
		switch(strategy){
		case fifo: return fringe.removeFirst();
		case lifo: return fringe.removeLast();
		}
		return null;
	}

	private String pathTo(State node) {
		//problem.showMoves(node);
		return "FIN";
	}

	private Set<State> expand(Problem problem, State node){
		Set<State> newNodes = problem.expand(node);
		return newNodes;
	}
}
