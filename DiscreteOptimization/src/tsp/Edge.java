package tsp;

class Edge{
	private Vertex v1;
	private Vertex v2;
	public Edge(Vertex v1, Vertex v2) {
		super();
		this.v1 = v1;
		this.v2 = v2;
	}
	
	public boolean crossWithEdge(Edge e){
		double A1,A2,B1,B2,C1,C2;
		A1=getA();A2=e.getA();
		B1=getB();B2=e.getB();
		C1=getC();C2=e.getC();
		
		if (B1 == 0 || B2 == 0) return false;
		else if (A1/B1 == A2/B2) return false; //parallel or coincidence
		else{ // they cross each other in a point (x,y)
			double x, y;
			if (A1==0){
				x = 0;
				y = -C2/B2;
			}else {
				y = (A2*C1/A1-C2)/(-A2*B1/A1+B2);
				x = (-B1*y-C1)/A1;
			}
			double minX1 = Math.min(v1.getX(), v2.getX());
			double minX2 = Math.min(e.getV1().getX(),e.getV2().getX()); 
			double maxX1 = Math.max(v1.getX(), v2.getX());
			double maxX2 = Math.max(e.getV1().getX(),e.getV2().getX()); 
			double minY1 = Math.min(v1.getY(), v2.getY());
			double minY2 = Math.min(e.getV1().getY(),e.getV2().getY()); 
			double maxY1 = Math.max(v1.getY(), v2.getY());
			double maxY2 = Math.max(e.getV1().getY(),e.getV2().getY()); 
			
			if (Math.abs(x - minX1) < 0.000001) x=minX1;
			if (Math.abs(x - maxX1) < 0.000001) x=maxX1;
			if (Math.abs(y - minY1) < 0.000001) y=minY1;
			if (Math.abs(y - maxY1) < 0.000001) y=maxY1;
			if (Math.abs(x - minX2) < 0.000001) x=minX2;
			if (Math.abs(x - maxX2) < 0.000001) x=maxX2;
			if (Math.abs(y - minY2) < 0.000001) y=minY2;
			if (Math.abs(y - maxY2) < 0.000001) y=maxY2;
			
			if ((x > minX1 && x < maxX1)&&(y > minY1 && y < maxY1)&&(x > minX2 && x < maxX2)&&(y > minY2 && y < maxY2)) return true;
			else return false;
		}
	}
	// Ax + By + C = 0 equation edge
	public double getA(){return this.v2.getY() - this.v1.getY();}
	public double getB(){return - this.v2.getX() + this.v1.getX();}
	public double getC(){return - this.v1.getX()*this.v2.getY() + this.v1.getY()*this.v2.getX();}
	
	
	public boolean useVertex(Vertex v){
		return v.equals(v1)||v.equals(v2);
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
