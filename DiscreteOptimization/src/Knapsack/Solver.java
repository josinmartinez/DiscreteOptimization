package Knapsack;
import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;

import Knapsack.Solver.Data;

/**
 * The class <code>Solver</code> is an implementation of a greedy algorithm to solve the knapsack problem.
 *
 */
public class Solver {
    
    /**
     * The main class
     * @throws CloneNotSupportedException 
     */
    public static void main(String[] args) throws CloneNotSupportedException {
        try {
            solve(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Read the instance, solve it, and print the solution in the standard output
     * @throws CloneNotSupportedException 
     */
    public static void solve(String[] args) throws IOException, CloneNotSupportedException {
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
        int items = Integer.parseInt(firstLine[0]);
        int capacity = Integer.parseInt(firstLine[1]);

        int[] values = new int[items];
        int[] weights = new int[items];

        for(int i=1; i < items+1; i++){
          String line = lines.get(i);
          String[] parts = line.split("\\s+");

          values[i-1] = Integer.parseInt(parts[0]);
          weights[i-1] = Integer.parseInt(parts[1]);
        }

        // a trivial greedy algorithm for filling the knapsack
        // it takes items in-order until the knapsack is full
        //dinamicprogramming(items, capacity, values, weights);
        //dinamicprogramming2(items, capacity, values, weights);
        //branchandbound(items, capacity, values, weights);
        //System.out.println(dinamicprogramming2(items, capacity, values, weights));
		greedy2(items, capacity, values, weights);
        /*
        double memoryGB = 32.0*((items/1000.0)*(capacity/1000.0))/8000.0;
        if (memoryGB < 1)
        	dinamicprogramming(items, capacity, values, weights);
        else greedy2(items, capacity, values, weights);
        */
    }

	private static void greedy(int items, int capacity, int[] values, int[] weights) {
		int value = 0;
        int weight = 0;
        int[] taken = new int[items];

        for(int i=0; i < items; i++){
            if(weight + weights[i] <= capacity){
                taken[i] = 1;
                value += values[i];
                weight += weights[i];
            } else {
                taken[i] = 0;
            }
        }
        
        // prepare the solution in the specified output format
        System.out.println(value+" 0");
        for(int i=0; i < items; i++){
            System.out.print(taken[i]+" ");
        }
        System.out.println("");
	}

	public class Data implements Cloneable,Comparable<Data>{
		int id;
		int pos=0;
		Double vperw = 0.0;
		Integer value= 0;
		Integer weight= 0;
		Integer realcost= 0;
		Double gcost= 0.0;
		Integer realweight= 0;;
		boolean included = false;
		
		public Data(int id, Double vperw, Integer value, Integer weight) {
			super();
			this.id = id;
			this.vperw = vperw;
			this.weight = weight;
			this.value=value;
		}
		
/*
		@Override
		public int compareTo(Data arg0) {
			int value = this.vperw.compareTo(arg0.vperw);
			if (value == 0) value = this.value.compareTo(arg0.value);
			if (value == 0) value = this.weight.compareTo(arg0.weight);
			return -1*value;
		}
*/
		@Override
		public int compareTo(Data arg0) {
			double diff = Math.abs(this.vperw-arg0.vperw);
			if (diff <= 0.00009) value = 0;
			else 
				value = this.vperw.compareTo(arg0.vperw);
			//if (value == 0) value = this.value.compareTo(arg0.value);
			if (value == 0) value = this.weight.compareTo(arg0.weight);
			if (value == 0) value = this.value.compareTo(arg0.value);
			return -1*value;
		}


		@Override
		public String toString() {
			return "Data [id=" + id + ", vperw=" + vperw + ", value=" + value
					+ ", weight=" + weight + ", realcost=" + realcost
					+ ", gcost=" + gcost + ", realweight=" + realweight
					+ ", included=" + included + "]";
		}

		@Override
		protected Data clone() throws CloneNotSupportedException {
			Data d = new Data(this.id,this.vperw,this.value,this.weight);
			d.realcost = this.realcost;
			d.gcost = this.gcost;
			d.included = !(this.included);
			d.realweight = this.realweight;
			return d;
		}
		
	}
	
	private static int greedy2(int items, int capacity, int[] values, int[] weights) {
		int value = 0;
        int weight = 0;
        int[] taken = new int[items];
        List<Data> data = new ArrayList<Data>(items);
        Solver x= new Solver();
        for(int i=0; i < items; i++){
        	data.add(x.new Data(i,1.0*values[i]/weights[i],values[i],weights[i]));
        }
        Collections.sort(data);
        
        for (Data d:data){
        	int i = d.id;
            if(weight + weights[i] <= capacity){
                taken[i] = 1;
                value += values[i];
                weight += weights[i];
            } else {
                taken[i] = 0;
            }
        }
        
        // prepare the solution in the specified output format
        System.out.println(value+" 0");
        for(int i=0; i < items; i++){
            System.out.print(taken[i]+" ");
        }
        System.out.println("");
        return value;
	}

	
	private static void dinamicprogramming(int items, int capacity, int[] values, int[] weights) {
		int[][] data = new int[capacity+1][items+1];
		
		for (int k=0;k<=capacity;k++){
			data[k][0] = 0;
		}

		for (int j=1;j<=items;j++){
			for (int k=0;k<=capacity;k++){
				int wj = weights[j-1];
				int vj = values[j-1];
				if (wj <= k) data[k][j] = Math.max(data[k][j-1],vj+data[k-wj][j-1]);
				else data [k][j] = data[k][j-1];
			}
		}
		
		short[] taken = new short[items];
		int k = capacity;
		for (int j = items;j > 0;j--){
			if (data[k][j] == data[k][j-1]) 
				taken[j-1] = 0;
			else {
				taken[j-1] = 1;
				k -= weights[j-1];
			}
		}

		// prepare the solution in the specified output format
        System.out.println(data[capacity][items]+" 0");
        for(int i=0; i < items; i++){
            System.out.print(taken[i]+" ");
        }
        System.out.println("");
	}
	
	private static int dinamicprogramming2(int items, int capacity, int[] values, int[] weights) {
		int[][] data = new int[capacity+1][2];
		
		for (int k=0;k<=capacity;k++){
			data[k][0] = 0;
		}

		for (int j=1;j<=items;j++){
			for (int k=0;k<=capacity;k++){
				int wj = weights[j-1];
				int vj = values[j-1];
				int nj = j % 2;
				int njmenos1 = (nj==0)?1:0;
				if (wj <= k) data[k][nj] = Math.max(data[k][njmenos1],vj+data[k-wj][njmenos1]);
				else data [k][nj] = data[k][njmenos1];
			}
		}
		
		// prepare the solution in the specified output format
        //System.out.println(data[capacity][items%2]+" 0");
		return data[capacity][items%2];
	}

	
	private static double bestValue(List<Data> data,int items, int capacity){
        int totalw = 0;
        double totalv = 0;
        for (int i=0;i<items;i++){
        	totalw += data.get(i).weight;
        	if (totalw > capacity){
        		totalv += 1.0*data.get(i).value * (capacity - (totalw - data.get(i).weight))/data.get(i).weight;  
        		totalw = capacity;
        		break;        		
        	}else totalv += data.get(i).value;
        }
       return totalv;
	}
	
	private static void branchandbound(int items, int capacity, int[] values, int[] weights) throws CloneNotSupportedException {
		int value = 0;
        int weight = 0;
        int[] taken = new int[items];
        List<Data> data = new ArrayList<Data>(items);
        Solver x= new Solver();
        
        for(int i=0; i < items; i++){
        	data.add(x.new Data(i,1.0*values[i]/weights[i],values[i],weights[i]));
        }
        Collections.sort(data);
        int solution = dinamicprogramming2(items, capacity, values, weights);
        double bestsolution = bestValue(data,items, capacity);
        int greedysolution = greedy2(items, capacity, values, weights);
        
        System.out.println(solution);

        Comparator<Data> compare = new Comparator<Solver.Data>(){
			@Override
			public int compare(Data arg0, Data arg1) {
				return -1*(arg0.gcost.compareTo(arg1.gcost));
			}
        	
        };
        PriorityQueue<Data> fringe = new PriorityQueue<Data>(100,compare);
        Data initial = x.new Data(-1,0.0,0,0); initial.pos=-1; 
        fringe.add(initial);
        while(true){
        	if (fringe.isEmpty()) {System.out.println("FAIL");break;}
        	Data current = fringe.poll();
        	if (current.realcost == solution) {System.out.println("Solution:"+current.realcost);break;}
        	if ((current.pos+1) >= data.size()) continue;
        	Data succesor0 = data.get(current.pos+1);
        	Data succesor1 = succesor0.clone();
        	Data[] succesors = new Data[]{succesor0,succesor1};
        	for (int k=0;k<succesors.length;k++){
            	if (succesors[k].included){
	        		if (capacity >= current.realweight+succesors[k].weight) {
	        			succesors[k].realweight = current.realweight+succesors[k].weight;
	        			succesors[k].realcost = current.realcost+succesors[k].value;
	        			succesors[k].gcost = bestsolution - succesors[k].realcost;
	            		fringe.add(succesors[k]);
	            		if (succesors[k].realcost > greedysolution) System.out.println("Greedy:"+greedysolution+" < "+succesors[k].realcost);
	        		}
	            } else {
	        			succesors[k].realweight = current.realweight;
	        			succesors[k].realcost = current.realcost;
	        			succesors[k].gcost = current.gcost;
	            		fringe.add(succesors[k]);
	            }
            	succesors[k].pos = current.pos +1;
        	}
        }
        
        
/*        
        for (Data d:data){
        	int i = d.id;
            if(weight + weights[i] <= capacity){
                taken[i] = 1;
                value += values[i];
                weight += weights[i];
            } else {
                taken[i] = 0;
            }
        }
        
        // prepare the solution in the specified output format
        System.out.println(value+" 0");
        for(int i=0; i < items; i++){
            System.out.print(taken[i]+" ");
        }
        System.out.println("");
*/
	}
}