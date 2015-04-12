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
    public static void main(String[] args) {
        try {
            new Solver().solve(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public  void solve(String[] args) throws IOException {
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

        //dinamicprogramming(items, capacity, values, weights);
        //dinamicprogramming2(items, capacity, values, weights);
        branchandbound(items, capacity, values, weights);
        //System.out.println(dinamicprogramming2(items, capacity, values, weights));
		//greedy2(items, capacity, values, weights);
        /*
        double memoryGB = 32.0*((items/1000.0)*(capacity/1000.0))/8000.0;
        if (memoryGB < 1)
        	dinamicprogramming(items, capacity, values, weights);
        else greedy2(items, capacity, values, weights);
        */
    }

    // a trivial greedy algorithm for filling the knapsack
    // it takes items in-order until the knapsack is full
	private void greedy(int items, int capacity, int[] values, int[] weights) {
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
		

		@Override
		public int compareTo(Data arg0) {
			int value = this.vperw.compareTo(arg0.vperw);
			if (value == 0) value = this.value.compareTo(arg0.value);
			if (value == 0) value = this.weight.compareTo(arg0.weight);
			return -1*value;
		}

/*
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
*/

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
	
    // a greedy algorithm for filling the knapsack
    // it takes sorted(value per weight) items until the knapsack is full
	private int greedy2(int items, int capacity, int[] values, int[] weights) {
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

	
    // a dinamic prorammingg algorithm for filling the knapsack
	private void dinamicprogramming(int items, int capacity, int[] values, int[] weights) {
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
	
    // a dinamic prorammingg algorithm for filling the knapsack
	// it use less memory only two columns instead of n (#items)
	// we only know the optimal value but not the chosen items
	private  int dinamicprogramming2(int items, int capacity, int[] values, int[] weights) {
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

	// a branch and bound algorithm (A*) for filling the knapsack
	private  void branchandbound(int items, int capacity, int[] values, int[] weights){
		List<Data> data = new ArrayList<Data>(items);

		for(int i=0; i < items; i++){
			data.add(new Data(i,1.0*values[i]/weights[i],values[i],weights[i]));
		}
		Collections.sort(data);
		int solution = dinamicprogramming2(items, capacity, values, weights);
		int greedysolution = greedy2(items, capacity, values, weights);
		
		int [] heuristic = calculeHeuristicData(items, data, capacity, values, weights);
		//double bestsolution = bestValue(data,items, capacity);
		System.out.println(solution+">"+greedysolution);
		//System.out.println(bestValue(data,items, capacity)+"="+heuristic[0]);
		Collections.reverse(data);
		long times=0;        

		Comparator<Data> compare = new Comparator<Solver.Data>(){
			@Override
			public int compare(Data arg0, Data arg1) {
				return -1*(arg0.gcost.compareTo(arg1.gcost));
			}

		};
		PriorityQueue<Data> fringe = new PriorityQueue<Data>(items/2,compare);
		Data initial = new Data(-1,0.0,0,0); initial.pos=-1;initial.gcost=bestValue(data,items, capacity); //first state is not any of the items
		fringe.add(initial);
		while(true){
			if (fringe.isEmpty()) {System.out.println("FAIL");return;}
			Data current = fringe.poll();
			times++;
			//System.out.print(current.id+" ");
			if (current.realcost > (solution*0.999)) {
				System.out.println("\nSolution:"+current.realcost);
				System.out.println(times);
				if (current.realcost == solution) 
					return;
			}
			if ((current.id+1) >= data.size()) continue; //There is not more successors
			else{
				int currentItem = current.id + 1;
				for (Data d: nextSuccesors(capacity,currentItem,current,data.get(currentItem),heuristic)){
					fringe.add(d);
				}
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

	private List<Data> nextSuccesors(int capacity, int currentItem, Data current, Data item, int []heuristic){    	
		List<Data> successors = new ArrayList<Data>();
		double max = heuristic[0];
		Data succesor0 = new Data(currentItem,item.vperw,item.value,item.weight);
		succesor0.realweight = current.realweight;
    	succesor0.realcost = current.realcost;
    	double temp = (currentItem+1 >= heuristic.length)?(double) heuristic[currentItem] - current.realcost:(double) heuristic[currentItem+1] - current.realcost  ;
    	succesor0.gcost = (double) Math.abs(temp);// - succesor0.realcost); 

    	Data succesor1 = new Data(currentItem,item.vperw,item.value,item.weight);
    	succesor1.realweight = current.realweight + succesor1.weight;
    	succesor1.realcost = current.realcost + succesor1.value;
    	succesor1.gcost = (double) Math.abs(heuristic[currentItem] - current.realcost);
    	succesor1.included = true;
		
    	successors.add(succesor0);
    	if (succesor1.realweight <= capacity) successors.add(succesor1);
    	return successors;
	}
	
	private static int[] calculeHeuristicData(int items, List<Data> data, int capacity,int[] values, int[] weights) {
		int[] heur = new int[items];
		int sumweights = 0;
		double sumvalues = 0;
		
		for (int indexheur = 0; indexheur < items; indexheur++){
			for (int indexitem = indexheur;  indexitem < items; indexitem++){
				int currentweight = sumweights + data.get(indexitem).weight;
				if (currentweight >= capacity){
					int partialweight = data.get(indexitem).weight - (currentweight - capacity);
					double partialvalue =  partialweight * 1.0 * data.get(indexitem).value / data.get(indexitem).weight;
					sumvalues += partialvalue;
					heur[indexheur] = (int) Math.round(sumvalues);
					break;
				}else {
					sumvalues += data.get(indexitem).value;
					sumweights += data.get(indexitem).weight;
				}
			}
			if (sumweights < capacity) heur[indexheur] = (int) Math.round(sumvalues);
			sumweights = 0;
			sumvalues = 0;
		}
		return heur;
	}
}