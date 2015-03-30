import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Problem {

	public enum ActionsType {up,down,left,right};
	private int n =3 ;
	private Set<State> explored = new HashSet<State>();
	private Map<State,ActionsType> trans = new HashMap<State,ActionsType>();
	
	private List<ActionsType> actions = Arrays.asList(ActionsType.up,ActionsType.down,ActionsType.left,ActionsType.right);
	private State initialState;
	private State goalState;
	
	public Problem(State initialState, State goalState) {
		super();
		this.initialState = initialState;
		this.goalState = goalState;
		//trans.put(initialState, null);
	}

	public State initialState(){
		return initialState;
	}
	
	public boolean goalTest(State state){
		return goalState.equals(state);
	}
	
	public Set<State> expand(State node){
		explored.add(node);
		Set<State> states = new HashSet<State>();
		for (ActionsType action: actions){
			State newState = transiction(node, action);
			if (newState != null && !explored.contains(newState)) 
				states.add(newState);
		}
		return states;
	}
	
	private State transiction(State node, ActionsType action){
		State newState = null;
		newState = move(node,findEmpty(node),action);
		if (newState != null ){
			//trans.put(newState, action);
			return newState;
		} else return null;
	}

	private int[] findEmpty(State node){
		for (int i=0;i<n;i++)
			for (int j=0; j<n;j++){
				if (node.getValue(i, j) == 0){
					return new int[]{i,j}; 
				}
			}
		return null;
	}
	
	private State move(State node, int[] empty, ActionsType action){
		int i = empty[0];
		int j = empty[1];
		State newState = null;
		switch(action){
		case up: 
			if ( i > 0) newState=swap(node,i,j,i-1,j);
			break;	
		case down:
			if ( i < n-1) newState=swap(node,i,j,i+1,j);
			break;	
		case left:
			if ( j > 0) newState=swap(node,i,j,i,j-1);
			break;	
		case right:
			if ( j < n-1) newState=swap(node,i,j,i,j+1);
			break;	
		}
		return newState;
	}
	
	private State swap(State node, int i0, int j0, int i1, int j1){
		int[][] newState = new int[3][3];
		for (int i=0;i<n;i++)
			for (int j=0; j<n;j++){
				newState[i][j] = node.getValue(i, j);
				
			}
		int temp = newState[i1][j1];
		newState[i1][j1] = newState[i0][j0];
		newState[i0][j0] = temp;
		return new State(newState);
	}
	
	
	public void showMoves (State node){
		boolean fin = false;
		do{
			ActionsType actionS = trans.get(node);
			System.out.println(actionS + " => "+ node);
			if (actionS == null) fin = true;
			else node = detransiction(node,actionS);
			
		}while (!fin);
	}

	private State detransiction(State node, ActionsType action){
		State newState = null;
		switch(action){
		case up: 
			newState = move(node,findEmpty(node),ActionsType.down);
			break;	
		case down:
			newState = move(node,findEmpty(node),ActionsType.up);
			break;	
		case left:
			newState = move(node,findEmpty(node),ActionsType.right);
			break;	
		case right:
			newState = move(node,findEmpty(node),ActionsType.left);
			break;	
		}
		if (newState != null){
			return newState;
		} else return null;
	}


}
