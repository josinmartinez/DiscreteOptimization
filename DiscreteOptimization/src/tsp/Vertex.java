package tsp;


class Vertex{
	private int id;
	private double x;
	private double y;
	private Edge minEdge;
	
	public Vertex(int id, double x, double y) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		minEdge = null;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public Edge getMinEdge() {
		return minEdge;
	}

	public void setMinEdge(Edge minEdge) {
		this.minEdge = minEdge;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Vertex other = (Vertex) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public double distanceTo(Vertex v){
		return Math.sqrt(Math.pow(x - v.getX(), 2) + Math.pow(y - v.getY(), 2) );
	}

	@Override
	public String toString() {
		return "Vertex [id=" + id + ", x=" + x + ", y=" + y + "]";
	}
	
}

