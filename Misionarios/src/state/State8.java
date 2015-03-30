package state;

import java.util.Arrays;

public class State8 implements IState, Comparable<State8> {
	private int[][] state;
	private int level=0;
	private int f = 0;
	
	public State8(int[][] state) {
		super();
		this.state = state;
	}
	
	public int getValue(int i, int j){
		return state[i][j];
	}
	
	public int lenghtN(){
		return state[0].length;
	}
	
	public int[][] getState(){
		return state;
	}
	
	@Override
	public String toString() {
		return "State [" + Arrays.deepToString(state) + "]"+f+" - "+level;
	}

	@Override
	public boolean isValid() {
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(state);
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
		State8 other = (State8) obj;
		if (!Arrays.deepEquals(state, other.state))
			return false;
		return true;
	}

	@Override
	public int getLevel() {
		return level;
	}
	@Override
	public void setLevel(int level) {
		this.level=level;
	}

	public int getF() {
		return f;
	}

	public void setF(int f) {
		this.f = f;
	}

	@Override
	public int compareTo(State8 arg0) {
		return Integer.compare(f, arg0.getF());
	}

}
