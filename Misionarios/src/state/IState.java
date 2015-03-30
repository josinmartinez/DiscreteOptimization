package state;

public interface IState {
	public boolean isValid();
	public boolean equals(Object obj);
	public String toString(); 
	public int getLevel();
	public void setLevel(int level);
	public int getF();
	public void setF(int f);
}
