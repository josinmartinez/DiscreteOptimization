package actions.misionarios;

import actions.IAction;
import state.IState;
import state.State;



public class M1 implements IAction {

	@Override
	public IState executeOn(IState state) {
		IState newState = null;
		State node = (State)state;
		if ( node.getLb() == 1 )
			 newState = new State(node.getLm()-1,node.getLc(),0,node.getRm()+1,node.getRc(),1);
		else newState = new State(node.getLm()+1,node.getLc(),1,node.getRm()-1,node.getRc(),0); 
		return newState;
	}

	@Override
	public IState unexecuteOn(IState state) {
		return executeOn(state);
	}

	@Override
	public String toString(){
		return "1 misionero";
	}

}
