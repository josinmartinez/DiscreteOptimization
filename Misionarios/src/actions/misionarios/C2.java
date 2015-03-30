package actions.misionarios;

import actions.IAction;
import state.IState;
import state.State;

public class C2 implements IAction {

	@Override
	public IState executeOn(IState state) {
		IState newState = null;
		State node = (State)state;
		if ( node.getLb() == 1 )
			 newState = new State(node.getLm(),node.getLc()-2,0,node.getRm(),node.getRc()+2,1);
		else newState = new State(node.getLm(),node.getLc()+2,1,node.getRm(),node.getRc()-2,0);
		return newState;
	}

	@Override
	public IState unexecuteOn(IState state) {
		return executeOn(state);
	}

	@Override
	public String toString(){
		return "2 canival";
	}

}
