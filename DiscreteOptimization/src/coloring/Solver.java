package coloring;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
//import java.util.LinkedList;
//import java.util.Deque;

/**
 * The class <code>Solver</code> is an implementation of a greedy algorithm
 *
 */
public class Solver {
    
    /**
     * The main class
     * @throws CloneNotSupportedException 
     */
	private Node [] graph;
	private Set<Integer> [] constraints;
	int maxcolor = 0;
	
    public static void main(String[] args) throws CloneNotSupportedException {
        Solver solver = new Solver();
    	try {
            solver.solve(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Read the instance, solve it, and print the solution in the standard output
     * @throws CloneNotSupportedException 
     */
    public void solve(String[] args) throws IOException, CloneNotSupportedException {
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
        int edges = Integer.parseInt(firstLine[1]);
        
        graph = new Node [nodes];
        constraints = new Set[nodes];
        
        for(int i=1; i <= edges; i++){
          String line = lines.get(i);
          String[] parts = line.split("\\s+");
          
          int id1 = Integer.parseInt(parts[0]);
          int id2 = Integer.parseInt(parts[1]);
          
          Node node1 = graph[id1];
          Node node2 = graph[id2];
          
          if ( node1 == null) {
        	  node1 = new Node(id1);
        	  graph[id1] = node1;
          }
          if (node2 == null){
        	  node2 = new Node(id2);
        	  graph[id2] = node2;
          }
          node1.addNode(node2);
          node2.addNode(node1);
        }

        // a trivial greedy algorithm for filling the knapsack
        // it takes items in-order until the knapsack is full
        greedy();
    }
    
    private void greedy() {
    	Arrays.sort(graph);
    	Set<Byte> colores = new HashSet<Byte>();
    	for (int i=0;i<graph.length;i++){
    		Node n = graph[i];
    		Byte color = checkColor(n); 
    		colores.add(color);
   			n.setColor(color);
    		n.setExplored(true);
    	}

    	// prepare the solution in the specified output format
    	Arrays.sort(graph, new Comparator<Node>(){
			@Override
			public int compare(Node arg0, Node arg1) {
				Integer n1 = arg0.id;
				Integer n2 = arg1.id;
				return n1.compareTo(n2);
			}
    	});
    	System.out.println(colores.size()+" 0");
    	for(int i=0; i < graph.length; i++){
    		System.out.print(graph[i].getColor()+" ");
    	}
    }

    private byte checkColor(Node node){
		Set<Integer> cn = constraints[node.id];
		Integer color = 0;
		if (cn == null){
			constraints[node.id] = new HashSet<Integer>();
		}else{
			while(true){
				if (constraints[node.id].contains(color)) color++;
				else break;
			}
		}
		node.setColor(color.byteValue());
		
		for (Node sig: node.getNodes()){
			Set<Integer> c = constraints[sig.id];
			if (c == null){
				constraints[sig.id] = new HashSet<Integer>();
			}
			constraints[sig.id].add(new Integer(color));
		}
		return color.byteValue();
	}

/*
    private void greedy() {
    	Deque<Node> fringe = new LinkedList<Node>();
    	Arrays.sort(graph);

    	for (int i=0;i<graph.length;i++){
    		Node n = graph[i];
    		if (n.explored) continue;
    		else{
    			//n.setColor(checkColor(n));
    			//n.setExplored(true);
    			fringe.push(n);
    			while (!fringe.isEmpty()){
    				Node current = fringe.poll();
    				current.setColor(checkColor(current));
    				current.setExplored(true);
    				System.out.println(current);

    				//for (Node sig: current.getNodes()){
					//	if (sig.explored) continue;
					//	else fringe.push(sig);
					//}
    				PriorityQueue<Node> sigs = current.getNodes();
    				while(!sigs.isEmpty()){
    					Node sig = sigs.poll();
    					if (sig.explored) continue;
    					else {
    						fringe.addLast(sig);
    						System.out.println("\t"+sig);
    					}
    				}
    			}
    		}
    	}

    	// prepare the solution in the specified output format
    	System.out.println((maxcolor+1)+" 0");
    	for(int i=0; i < graph.length; i++){
    		System.out.print(graph[i].getColor()+" ");
    	}
    }

    
    private byte checkColor(Node node){
		Set<Integer> cn = constraints[node.id];
		Integer color = 0;
		if (cn == null){
			constraints[node.id] = new HashSet<Integer>();
		}else{
			while(true){
				if (constraints[node.id].contains(color)) color++;
				else break;
			}
		}
		node.setColor(color.byteValue());
		
		for (Node sig: node.getNodes()){
			Set<Integer> c = constraints[sig.id];
			if (c == null){
				constraints[sig.id] = new HashSet<Integer>();
			}
			constraints[sig.id].add(new Integer(color));
		}
		if (maxcolor < color) maxcolor = color;
		return color.byteValue();
	}
*/
	public class Node implements Comparable<Node>{
		int id;
		byte color = 0;
		boolean explored = false;
		PriorityQueue<Node> nodes = new PriorityQueue<Node>();
		
		public Node(int id) {
			super();
			this.id = id;
		}

		@Override
		public String toString() {
			return "Node [" + id + ", color=" + color + ", explored="+ explored + ", nodes=" + nodes.size() + "]";
		}
		
		public String toStringids() {
			String strnodes = "";
			for (Node sig: this.nodes){
				strnodes += sig.getId()+", ";
			}
			return strnodes;
		}

		public void addNode(Node node){
			nodes.add(node);
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public byte getColor() {
			return color;
		}

		public void setColor(byte color) {
			this.color = color;
		}

		public boolean isExplored() {
			return explored;
		}

		public void setExplored(boolean explored) {
			this.explored = explored;
		}

		public PriorityQueue<Node> getNodes() {
			return nodes;
		}

		@Override
		public int compareTo(Node arg0) {
			Integer t1 = this.nodes.size();
			Integer t2 = arg0.nodes.size();
			if (t1 == t2) {
				t1=this.getColoredNodes();
				t2=arg0.getColoredNodes();
			}
			return -1*t1.compareTo(t2);
		}

		private Integer getColoredNodes() {
			int colored = 0;
			for (Node n: nodes){
				if (n.explored)
					colored++;
			}
			return colored;
		}
		
		
	}

}