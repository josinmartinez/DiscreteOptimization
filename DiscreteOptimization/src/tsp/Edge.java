package tsp;

class Edge{
	private Vertex v1;
	private Vertex v2;
	public Edge(Vertex v1, Vertex v2) {
		super();
		this.v1 = v1;
		this.v2 = v2;
	}
	@Override
	public String toString() {
		return "Edge [" + v1.getId() + ", " + v2.getId() + "]";
	}
	
	public Vertex getV1() {
		return v1;
	}
	public void setV1(Vertex v1) {
		this.v1 = v1;
	}
	public Vertex getV2() {
		return v2;
	}
	public void setV2(Vertex v2) {
		this.v2 = v2;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((v1 == null) ? 0 : v1.hashCode());
		result = prime * result + ((v2 == null) ? 0 : v2.hashCode());
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
		Edge other = (Edge) obj;
		if (v1 == null) {
			if (other.v1 != null)
				return false;
		} 
		if (v2 == null) {
			if (other.v2 != null)
				return false;
		} 
		if ((v1.equals(other.v1)&&v2.equals(other.v2))||(v1.equals(other.v2)&&v2.equals(other.v1)))
			return true;
		else return false;
	}
}
