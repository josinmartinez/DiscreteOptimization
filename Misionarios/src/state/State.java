package state;


public class State implements IState{
	//State
	private int lm,lc,lb,rm,rc,rb;

	public State(int lm, int lc, int lb, int rm, int rc, int rb) {
		super();
		this.lm = lm;
		this.lc = lc;
		this.lb = lb;
		this.rm = rm;
		this.rc = rc;
		this.rb = rb;
	}
	
	public boolean isValid(){
		if (lb+rb != 1 ) return false;
		if ((lc > lm && lm > 0)|| (rc > rm  && rm > 0)) return false;
		if (lc < 0 || lm < 0 || rc < 0 || rm < 0) return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lb;
		result = prime * result + lc;
		result = prime * result + lm;
		result = prime * result + rb;
		result = prime * result + rc;
		result = prime * result + rm;
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
		if (lb != other.lb)
			return false;
		if (lc != other.lc)
			return false;
		if (lm != other.lm)
			return false;
		if (rb != other.rb)
			return false;
		if (rc != other.rc)
			return false;
		if (rm != other.rm)
			return false;
		return true;
	}

	public int getLm() {
		return lm;
	}

	public int getLc() {
		return lc;
	}

	public int getLb() {
		return lb;
	}

	public int getRm() {
		return rm;
	}

	public int getRc() {
		return rc;
	}

	public int getRb() {
		return rb;
	}

	@Override
	public String toString() {
		return "State [lm=" + lm + ", lc=" + lc + ", lb=" + lb + ", rm=" + rm
				+ ", rc=" + rc + ", rb=" + rb + "]";
	}

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setLevel(int level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getF() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setF(int f) {
		// TODO Auto-generated method stub
		
	}

}
