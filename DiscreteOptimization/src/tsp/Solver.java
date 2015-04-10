package tsp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;


public class Solver {
		
    public static void main(String[] args) throws CloneNotSupportedException {
        Solver solver = new Solver();
    	try {
            solver.solve(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	private void readFileInput(String[] args) throws FileNotFoundException,
			IOException {
		String fileName = null;
        
        // get the temp file name
        for(String arg : args){
            if(arg.startsWith("-file=")){
                fileName = arg.substring(6);
            } 
        }
        if(fileName == null)
            return;
        
        // read the lines out of the file
        List<String> lines = new ArrayList<String>();

        BufferedReader input =  new BufferedReader(new FileReader(fileName));
        try {
            String line = null;
            while (( line = input.readLine()) != null){
                lines.add(line);
            }
        }
        finally {
            input.close();
        }
        
        // parse the data in the file
        String[] firstLine = lines.get(0).split("\\s+");
        int nodes = Integer.parseInt(firstLine[0]);
        //Initialize an 1-array since distances are symmetric
        distances = new double[(nodes*nodes-nodes)/2]; //only allocated space for a triangle of a matrix node*node
        vertexList = new ArrayList<Vertex>(nodes);
        edgeList = new ArrayList<Edge>((nodes*nodes-nodes)/2);
        
        for(int i=1; i <= nodes; i++){
          String line = lines.get(i);
          String[] parts = line.split("\\s+");
          double x = Double.parseDouble(parts[0]);
          double y = Double.parseDouble(parts[1]);
          int index = i - 1; //Vertices start in 0
          Vertex v = new Vertex(index,x,y); 
          for (Vertex v1: vertexList){
        	  setDistance(index,v1.getId(),v.distanceTo(v1));
        	  edgeList.add(new Edge(v,v1));
          }
          vertexList.add(v);
        }
	}
	
	//Initialized in readFileInput
	private double[] distances;
	private List<Vertex> vertexList;
	private List<Edge> edgeList; 
	
	private double getDistance(int A, int B){
		int i = (A*A - A)/2 + B;
		int j = (B*B - B)/2 + A;
		if (A == B) return Double.MAX_VALUE;
		if (i >= distances.length || j >= distances.length) throw new IllegalArgumentException();
		return (B > A)? distances[j]:distances[i];
	}
	
	private double getDistance(Edge e){
		return getDistance(e.getV1().getId(),e.getV2().getId());
	}
	
	
	private void setDistance(int A, int B, double distance){
		int i = (A*A - A)/2 + B;
		int j = (B*B - B)/2 + A;
		if (A == B) return;
		if (i >= distances.length || j >= distances.length) throw new IllegalArgumentException();
		if (B > A) distances[j] = distance; 
		else distances[i]=distance;
	}

	
	public void solve(String[] args) throws IOException{
        readFileInput(args);
        List<Vertex> MST = prim(vertexList, edgeList);
        int[] path = new int[vertexList.size()];
        int i = 0;
        double totalDistance = 0;
        int start = MST.get(0).getId();
        path[i]=start;
        for (i=1;i<MST.size();i++){
        	path[i] = MST.get(i).getId();
        	totalDistance += getDistance(path[i-1],path[i]);
        }
        totalDistance += getDistance(start,path[i-1]);
        System.out.println(totalDistance+"  0");
        for (i=0;i<path.length;i++)
        	System.out.print(path[i]+" ");

    }
	
	private List<Vertex> prim(List<Vertex> V, List<Edge>E){
		Vertex start = chooseInitialVertex(V);
		List<Vertex> X = new ArrayList<Vertex>();X.add(start);
		Set<Vertex> V_X = new HashSet<Vertex>(V);V_X.removeAll(X);
		List<Edge> T = new ArrayList<Edge>();

		PriorityQueue<Vertex> heap = new PriorityQueue<Vertex>(V.size(), new Comparator<Vertex>(){
			@Override
			public int compare(Vertex arg0, Vertex arg1) {
				Edge e1 = arg0.getMinEdge();
				Edge e2 = arg1.getMinEdge();
				Double d1 = getDistance(e1.getV1().getId(),e1.getV2().getId());
				Double d2 = getDistance(e2.getV1().getId(),e2.getV2().getId());
				return d1.compareTo(d2);
			}
		});
		
		computeAllMinEdges(start,V_X, heap);		
		
		while (X.size() != V.size()){
			Vertex v = heap.poll();
			Edge e = v.getMinEdge();
			T.add(e);
			X.add(v);V_X.remove(v);
			for (Edge edge:E){
				Vertex j = edge.getV1(); Vertex k = edge.getV2();
				Vertex w = null;
				if ((j.equals(v) && V_X.contains(k))) w = k;
				if ((k.equals(v) && V_X.contains(j))) w = j;
				if (w != null){
					heap.remove(w);
					if (getDistance(w.getMinEdge().getV1().getId(),w.getMinEdge().getV2().getId()) > getDistance(v.getId(),w.getId())){
						w.setMinEdge(new Edge(v,w));
					}
					heap.add(w);
				}
			}
		}
        return X;
	}
	
	private void computeAllMinEdges(Vertex start, Set<Vertex> V_X, PriorityQueue<Vertex> heap){
		for (Vertex v: V_X){
			//Vertex minVertex = computeMinEdgeByVertex(v, start);
			v.setMinEdge(new Edge(start,v));
			heap.add(v);
		}
	}

	private Vertex computeMinEdgeByVertex(Vertex v, Vertex start) {
		double minValue = Double.MAX_VALUE;
		Vertex minVertex = null;
		for (int i=0;i < vertexList.size();i++){
			double temp = getDistance(v.getId(),i);
			if (temp < minValue){
				minValue = temp;
				minVertex = vertexList.get(i);
			}
		}
		return minVertex;
	}

	
	

	private Vertex chooseInitialVertex(List<Vertex> V) {
		Vertex initial = V.get(0);
		return initial;
	}
	
    
}
