package main;
import java.util.List;

import state.*;
import actions.IAction;

public class Problem {
	private IState initialState;
	private IState goalState;
	private List<IAction> actions;
	
	public Problem(IState initialState, IState goalState, List<IAction> actions) {
		super();
		this.initialState = initialState;
		this.goalState = goalState;
		this.actions = actions;
	}

	public IState initialState(){
		return initialState;
	}
	
	public boolean goalTest(IState state){
		return goalState.equals(state);
	}
	
	public List<IAction> getActions(){
		return actions;
	}
}
