import java.util.Arrays;


public class State {
	int[][] state;
	public State(int[][] state) {
		super();
		this.state = state;
	}
	public int getValue(int i, int j){
		return state[i][j];
	}
	@Override
	public String toString() {
		
		return "State [state=" + show() + "]";
	}
	
	private String show(){
		String result = "";
		for (int i=0;i < state.length; i++){
			for (int j=0;j < state.length; j++)
				result += state[i][j]+" "; 
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.show().hashCode();
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (!this.show().equals(((State)obj).show()))
			return false;
		return true;
	}
	
	
}
