package Pruebas.LocalSearch;

import java.util.Random;

public class EightQueens {
	 public static void main(String[] args){
		 EightQueens q8 = new EightQueens();
		 q8.solve();
	 }
	 private final int N = 8;
	 private int[] queens = new int[]{0,1,2,3,4,5,6,7};
	 private boolean notsolution = true;
	 
	 public void solve(){
		 
		 int column = maxViolationColumn();
		 while(notsolution){
			 //imprimir();
			 int row = rowLessViolationsByColumn(column);
			 queens[column] = row;
			 column = maxViolationColumn();
			 System.out.print(".");
		 }
		 System.out.println();
		 imprimir();

	 }
	 
	private void imprimir() {
		 System.out.print(" ");
		 for (int i=0;i<N;i++) System.out.print(i);
		 System.out.println();
		 for (int i=0;i<N;i++){
			 System.out.print(i);
			 for (int j=0;j<N;j++){
				 if (queens[j] == i) System.out.print("*");
				 else System.out.print(" ");
			 }
			 System.out.println("");
		 }
	}

	private int rowLessViolationsByColumn(int column){
		int[]values = new int[8];
		int originalrow = queens[column];
		int originalviolations = violations(column);
		int value = originalviolations; 
		int toPos = 0;
		for (int i=0;i<N;i++){
			//if (i == originalrow) continue;
			queens[column] = i;
			int temp =  violations(column) - originalviolations;
			//System.out.print(temp+" ");
			if (temp < value) {value = temp;}
			values[i] = temp;
		}
		toPos = randomChoose(values,value);
		//System.out.println(toPos);
		return toPos;
	}
	
	private int randomChoose(int[] choose, int max){
		int[] values = new int[8];
		int k=0;
		for (int i=0;i<N;i++){
			if (choose[i] == max){
				values[k]=i; 
				k++;
			}
		}
		k--;
		Random r = new Random();
		return values[r.nextInt(k+1)];
	}
	 
	private int maxViolationColumn(){
		int[]values = new int[8];
		int sumviolations = 0;
		int value=0;
		int column = 0;
		for (int i=0;i<N;i++){
			int temp = violations(i);
			sumviolations += (temp > 0)?1:0;
			//System.out.print(temp+" ");
			if (temp > value) {value = temp;}
			values[i] = temp;
		}
		if (value == 0) 
			notsolution = false;
		column = randomChoose(values,value);
		//System.out.println("    ->"+sumviolations+"["+column+"]");
		return column;
	}

	private int violations(int column) {
		int violations = 0;
		for (int i=0;i<N;i++){
			if (i != column){
				if (queens[i] == queens[column]) violations++;
				if (queens[i] == (queens[column]-Math.abs(i-column))) violations++;				
				if (queens[i] == (queens[column]+Math.abs(i-column))) violations++; 
			}
		}
		return violations;
	}
	 
	 
}
