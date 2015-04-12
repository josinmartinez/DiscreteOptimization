package tsp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
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
        
        //Greedy MST Phase I
        List<Vertex> MST = prim(vertexList, edgeList);
        int[] path = new int[vertexList.size()];
        for (int i=0;i<MST.size();i++){
        	path[i] = MST.get(i).getId();
        }
        double totalDistance = calculeDistante(path);
        System.out.println("Phase I:"+totalDistance);
        showSolution(path, totalDistance);

        
        //Random Improvements Phase II
        totalDistance = runPhaseII(path, totalDistance, 1000000, 0.3);
        System.out.println("Phase II:"+totalDistance);
        showSolution(path, totalDistance);

/*        
        //Remove Cross Edges Phase III
        Map<Integer, Vertex> mapV = new HashMap<Integer,Vertex>();
        for (Vertex v: vertexList) mapV.put(v.getId(), v);

        int veces=0;
        while(veces<10){
        List<Edge> listEdges = new ArrayList<Edge>();
        for (int i=1;i<path.length;i++){
        	Vertex v1 = mapV.get(path[i-1]);
        	Vertex v2 = mapV.get(path[i]);
        	listEdges.add(new Edge(v1,v2));
        }
        listEdges.add(new Edge(mapV.get(path[path.length-1]),mapV.get(path[0])));
        
        Set<Edge> badEdges = new HashSet<Edge>();
        Map<Integer,Integer> recalculateVertex = new HashMap<Integer,Integer>();
        for (int i=0; i<listEdges.size();i++){
        	Edge iEdge = listEdges.get(i);
        	if (badEdges.contains(iEdge)) continue;
        	boolean crossed = false;
        	Edge jEdge = null;
        	for (int j = i+1;j<listEdges.size();j++){
        		jEdge = listEdges.get(j);
        		if (iEdge.crossWithEdge(jEdge)){
        			if (!badEdges.contains(jEdge)){
        				extractVertex(recalculateVertex, jEdge);
        				badEdges.add(jEdge);
        				crossed = true;
        				break;
        			}else continue;
        		}
        	}
        	if (crossed) {
    			extractVertex(recalculateVertex, iEdge);
    			badEdges.add(iEdge);
    			OPT2(mapV,listEdges,iEdge,jEdge,path);
    			listEdges.remove(i);listEdges.remove(jEdge);
        	}
        }
        listEdges.removeAll(badEdges);
        veces++;
        }
/*        
        List<Integer> list = new ArrayList<Integer>();
        for (Integer i: recalculateVertex.keySet()){
        	for (int j=0;j<recalculateVertex.get(i);j++) list.add(i);
        }
        
        for (int i=0;i<list.size();i++){
        	int minId = -1;
        	double minDistance = Double.MAX_VALUE;
        	int id = list.get(i);
        	boolean add= false;
        	if (recalculateVertex.get(id) == 0) continue;
        	for (Integer id2: recalculateVertex.keySet()){
        		if (id==id2 || recalculateVertex.get(id2) == 0) continue;
        		else{
        			double distance = getDistance(id,id2);
        			if (distance < minDistance && !(listEdges.contains(new Edge(mapV.get(id),mapV.get(id2)))) && !badEdges.contains(new Edge(mapV.get(id),mapV.get(id2)))){
        				minDistance = distance;
        				minId = id2;
        				add=true;
        			}
        		}
        	}
        	recalculateVertex.put(minId,recalculateVertex.get(minId)-1);
        	recalculateVertex.put(id,recalculateVertex.get(id)-1);
        	if (add) listEdges.add(new Edge(mapV.get(id),mapV.get(minId)));
        }
        
        for (int i=0;i<path.length;i++){
        	path[i]=0;
        }
        path[0] = listEdges.get(0).getV1().getId();
        path[1] = listEdges.get(0).getV2().getId();
        Set<Edge> explored = new HashSet<Edge>();
        explored.add(listEdges.get(0));
        for (int i=2;i < path.length;i++){
        	Edge e = null; //= findNext(listEdges,mapV.get(path[i-1]));
    		for (int j=0;j<listEdges.size();j++){
    			if (listEdges.get(j).useVertex(mapV.get(path[i-1]))&&!explored.contains(listEdges.get(j))){
    				e = listEdges.get(j);
    				explored.add(e);
    				break;
    			}
    		}
        	if (e!=null) path[i] = (e.getV1().getId()==path[i-1])?e.getV2().getId():e.getV1().getId();
        	else
        		System.out.println();
        }
*/
        totalDistance = calculeDistante(path);
        System.out.println("Phase III:"+totalDistance);
        
       showSolution(path, totalDistance);
    }

	private void showSolution(int[] path, double totalDistance) {
		System.out.println(totalDistance+"  0");
        for (int i=0;i<path.length;i++)
        	System.out.print(path[i]+" ");
        System.out.println();
	}

	private void OPT2(Map<Integer, Vertex> mapV, List<Edge> listEdges, Edge iEdge, Edge jEdge, int[] path) {
		int iv1 = iEdge.getV1().getId();
		int iv2 = iEdge.getV2().getId();
		int jv1 = jEdge.getV1().getId();
		int jv2 = jEdge.getV2().getId();
		if (getDistance(iv1,jv1) < getDistance(iv1,jv2)){
			listEdges.add(new Edge(mapV.get(iv1),mapV.get(jv1)));
			listEdges.add(new Edge(mapV.get(iv2),mapV.get(jv2)));
			swap(iv2,jv1,path);
		}else{
			listEdges.add(new Edge(mapV.get(iv1),mapV.get(jv2)));
			listEdges.add(new Edge(mapV.get(iv2),mapV.get(jv1)));
			swap(iv2,jv2,path);
		}
	}
	
	private int findIndex(int[] path, int value){
		for (int i=0;i<path.length;i++){
			if (path[i] == value) return i;
		}
		return -1;
	}
	
	private void swap(int a, int b, int[] path){
		int idA = findIndex(path,a);
		int idB = findIndex(path,b);
		int temp = path[idA];
		path[idA] = path[idB];
		path[idB]=temp;
	}

	private void extractVertex(Map<Integer, Integer> recalculateVertex, Edge jEdge) {
		int idV1 = jEdge.getV1().getId();
		int idV2 = jEdge.getV2().getId();
		if (recalculateVertex.get(idV1) == null) recalculateVertex.put(idV1, 1);
		else recalculateVertex.put(idV1, recalculateVertex.get(idV1)+1);
		if (recalculateVertex.get(idV2) == null) recalculateVertex.put(idV2, 1);
		else recalculateVertex.put(idV2, recalculateVertex.get(idV2)+1);
	}

	private Edge findNext(List<Edge> edges, Vertex v){
		for (int i=0;i<edges.size();i++){
			if (edges.get(i).useVertex(v)){
				Edge e = edges.get(i);
				//edges.remove(i);
				return e;
			}
		}
		return null;
	}
	private double runPhaseII(int[] path, double totalDistance, int MaxTrials, double coef) {
		double temp = totalDistance*coef;
        int tries = 0; 
        while(tries < MaxTrials && temp < totalDistance){
        	Random e =new Random();
        	int a = e.nextInt(path.length);int b = e.nextInt(path.length);
        	int x = path[a];int y = path[b];
        	path[a]=y;path[b]=x;
        	double temptotalDistance = calculeDistante(path);
        	if (temptotalDistance >= totalDistance){
        		path[a]=x;path[b]=y;
        	}else {
        		totalDistance = temptotalDistance;
        	}
        	tries++;
        }
		return totalDistance;
	}

	private double calculeDistante(int[] path) {
		double totalDistance = 0;
        int start = path[0];
        for (int i=1;i<path.length;i++){
        	totalDistance += getDistance(path[i-1],path[i]);
        }
        totalDistance += getDistance(start,path[path.length-1]);
		return totalDistance;
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
			v.setMinEdge(new Edge(start,v));
			heap.add(v);
		}
	}

	private Vertex chooseInitialVertex(List<Vertex> V) {
		Vertex initial = V.get(0);
		return initial;
	}
	  
}
