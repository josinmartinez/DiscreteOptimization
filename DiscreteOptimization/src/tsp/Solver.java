package tsp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;



public class Solver {

	//Initialized in readFileInput
	boolean enoughtSpace = true; //We can not store a distance matrix
	private double[] distances;
	private List<Vertex> vertexList;
	//private List<Edge> edgeList; 
	
    public static void main(String[] args) throws CloneNotSupportedException {
        Solver solver = new Solver();
    	try {
            solver.solve(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	private void readFileInput(String[] args) throws FileNotFoundException, IOException {
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
        if (((nodes*nodes-nodes)/2)/(1.0*8*1024*1024*1024) <= 1) enoughtSpace = false;//Memory <=1GB
        if (enoughtSpace) distances = new double[(nodes*nodes-nodes)/2]; //only allocated space for a triangle of a matrix node*node
        vertexList = new ArrayList<Vertex>(nodes);
        //edgeList = new ArrayList<Edge>((nodes*nodes-nodes)/2);
        
        for(int i=1; i <= nodes; i++){
          String line = lines.get(i);
          String[] parts = line.split("\\s+");
          double x = Double.parseDouble(parts[0]);
          double y = Double.parseDouble(parts[1]);
          int index = i - 1; //Vertices start in 0
          Vertex v = new Vertex(index,x,y); 
          for (Vertex v1: vertexList){
        	  if (enoughtSpace) setDistance(index,v1.getId(),v.distanceTo(v1));
        	  //edgeList.add(new Edge(v,v1));
          }
          vertexList.add(v);
        }
	}
	
	private double getDistance(int A, int B){
		if (A == B) return Double.MAX_VALUE;
		if (enoughtSpace){
			int i = (A*A - A)/2 + B;
			int j = (B*B - B)/2 + A;
			if (i >= distances.length || j >= distances.length) throw new IllegalArgumentException();
			return (B > A)? distances[j]:distances[i];
		}else 
			return vertexList.get(A).distanceTo(vertexList.get(B));
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
        //System.out.print("Leyendo fichero.......");
		readFileInput(args);
        //System.out.println("[DONE]");
        //First Solution Greedy Algorithm
        //System.out.print("Construyendo grafo.......");
        int[] path = greedyAlgo(vertexList);//Greedy
        //System.out.println("[DONE]");
        //System.out.print("Aleatorizando.......");
        //Random Improvements Phase II
        double totalDistance = calculeDistante(path);
        //path = runPhaseII(path, totalDistance, Math.min((int)Math.round(path.length*0.10),10),Math.min(path.length*100,1000), 0.7);
        //System.out.println("Phase II:"+totalDistance);
        showSolution(path, calculeDistante(path));
        //System.out.println("[DONE]");
    }

	private int[] greedyAlgo(List<Vertex> vertexList) {
		int[] path = new int[vertexList.size()];
		Map<Integer,List<Integer>> graph = new HashMap<Integer,List<Integer>>(vertexList.size());
		//graph with three vertices
		Integer v1 = vertexList.get(0).getId();
		Integer v2 = vertexList.get(1).getId();
		Integer v3 = vertexList.get(2).getId();
		graph.put(v1, Arrays.asList(v2,v3));
		graph.put(v2, Arrays.asList(v1,v3));
		graph.put(v3, Arrays.asList(v1,v2));
		for (int i=3; i<vertexList.size();i++){
			Integer x = vertexList.get(i).getId();
			Integer vC = closesVertexTo(x,graph.keySet());
			Integer vI = graph.get(vC).get(0);
			Integer vD = graph.get(vC).get(1);
			if ((getDistance(vI, x) + getDistance(vC, vD))< (getDistance(vD,x)+getDistance(vI,vC))){
				graph.put(x, Arrays.asList(vI,vC));
				graph.replace(vC, change(graph.get(vC),vI,x));
				graph.replace(vI, change(graph.get(vI),vC,x));
			}else{
				graph.put(x, Arrays.asList(vD,vC));
				graph.replace(vC, change(graph.get(vC),vD,x));
				graph.replace(vD, change(graph.get(vD),vC,x));
			}
		}
		
		Set<Integer> explored = new HashSet<Integer>(vertexList.size());
		Integer next = v1;
		for (int i=0; i<vertexList.size();i++){
			path[i]=next;explored.add(next);
			for (Integer vertex:graph.get(next)){
				if (!explored.contains(vertex)){
					next=vertex;break;
				}
			}
		}
		//double totalDistance = calculeDistante(path);
	    //System.out.println("Algo:"+totalDistance);
        //showSolution(path, totalDistance);
        return path;
	}

	private List<Integer> change(List<Integer> list, Integer oldValue, Integer newValue) {
		for (int i=0; i<list.size();i++){
			if (list.get(i) == oldValue) {
				list.set(i, newValue);
				break;
			}
		}
		return list;
	}

	private Integer closesVertexTo(int id, Set<Integer> keySet) {
		Integer closesVertex = -1;
		double minDistance = Double.MAX_VALUE;
		for (Integer vId: keySet){
			double dist = getDistance(vId, id);
			if (dist < minDistance){
				minDistance =  dist;
				closesVertex = vId;
			}
		}
		return closesVertex;
	}

	private void showSolution(int[] path, double totalDistance) {
		System.out.println(totalDistance+"  0");
        for (int i=0;i<path.length;i++)
        	System.out.print(path[i]+" ");
        System.out.println();
	}

	private int[] runPhaseII(int[] path, double totalDistance, int maxTrials, int maxSwaps, double coef) {
		int []bestpath = new int[path.length];	System.arraycopy(path, 0, bestpath, 0, path.length);
		double bestpathDistance = totalDistance;
		double temp = totalDistance*coef;

		//maxTrials=2;
        for (int i=0;i < maxTrials; i++){
		    int[]pathtemp = new int[path.length];
		    System.arraycopy(bestpath, 0, pathtemp, 0, path.length);
		    double pathtempdistance = bestpathDistance;
			int tries = 0; 
		    while(tries < maxSwaps && temp < pathtempdistance){
		    	Random e =new Random();
		    	int a = e.nextInt(path.length);int b = e.nextInt(path.length);
		    	int x = pathtemp[a];int y = pathtemp[b];
		    	pathtemp[a]=y;pathtemp[b]=x;
		    	double temptotalDistance = calculeDistante(pathtemp);
		    	if (temptotalDistance >= pathtempdistance){
		    		pathtemp[a]=x;pathtemp[b]=y;
		    	}else {
		    		pathtempdistance = temptotalDistance;
		    	}
		    	tries++;
		    }
		    if (pathtempdistance < bestpathDistance){
		    	System.arraycopy(pathtemp, 0, bestpath, 0, path.length);
		    	bestpathDistance = pathtempdistance;
		    	//System.out.println(bestpathDistance+" "+tries);
		    }
        }
        //System.out.println(tries);
		return bestpath;
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
}