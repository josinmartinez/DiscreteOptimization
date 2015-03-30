package actions;
import state.IState;


public interface IAction {
	public IState executeOn(IState state);
	public IState unexecuteOn(IState state);
	public String toString();
	
}
