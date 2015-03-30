package actions.puzzle8;

import state.IState;
import state.State8;
import actions.IAction;

public class RightAction implements IAction {

	@Override
	public IState executeOn(IState state) {
		State8 state8 = (State8)state;
		int[] empty = findEmpty(state8);
		int i = empty[0]; int j = empty[1];
		if ( j < state8.lenghtN()-1) return swap(state8,i,j,i,j+1);
		else return null;
	}

	
	@Override
	public IState unexecuteOn(IState state) {
		return new RightAction().executeOn(state);
	}

	@Override
	public String toString(){
		return "Derecha";
	}

	private int[] findEmpty(State8 node){
		for (int i=0;i<node.lenghtN();i++)
			for (int j=0; j<node.lenghtN();j++){
				if (node.getValue(i, j) == 0){
					return new int[]{i,j}; 
				}
			}
		return null;
	}	

	private IState swap(State8 node, int i0, int j0, int i1, int j1){
		int[][] newState = new int[node.lenghtN()][node.lenghtN()];
		for (int i=0;i<node.lenghtN();i++)
			for (int j=0; j<node.lenghtN();j++){
				newState[i][j] = node.getValue(i, j);
				
			}
		int temp = newState[i1][j1];
		newState[i1][j1] = newState[i0][j0];
		newState[i0][j0] = temp;
		return new State8(newState);
	}


}
