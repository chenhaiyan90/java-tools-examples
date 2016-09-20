package com.dova.dev.algorithems;
/*
 * question：to print a 2-dimension array like a snake bite its tail
 * solution：to enumerate all the possibilities
 */
public class SnakePrint {
	
	//1代表向右，2代表向下，3代表向左，4代表向上
	public static void print(int m, int n, int i, int j, int[][] a,int direction){
		if(i == 0 && j==0){
			System.out.println(a[0][0] + ",");
		}
		//System.out.println(i + "==" + j);
		if(direction == 1){
			int sym_index = getSymmetryIndex(j, n);
			if(j >= sym_index){
				return;
			}
			if(i == 0){
				sym_index = sym_index + 1;
			}
			for(int k = j+1;k < sym_index;k++){
				System.out.print(a[i][k] + ",");
			}
			System.out.println();
			print(m, n, i, sym_index - 1, a, 2);
		}else if (direction == 2) {
			//向下
			int sym_index = getSymmetryIndex(i, m);
			if(i >= sym_index){
				return;
			}
			if(j == n-1){
				sym_index++;
			}
			for(int k = i+1;k < sym_index;k++){
				System.out.print(a[k][j] + ",");
			}
			System.out.println();
			print(m, n, sym_index - 1, j, a, 3);
		}else if (direction == 3){
			int sym_index = getSymmetryIndex(j, n);
			if(j <= sym_index){
				return;
			}
			if(i == m-1){
				sym_index--;
			}
			for(int k = j-1;k > sym_index;k--){
				System.out.print(a[i][k] + ",");
			}
			System.out.println();
			print(m, n, i, sym_index + 1, a, 4);
		}else if (direction == 4) {
			int sym_index = getSymmetryIndex(i, m);
			if(i <= sym_index){
				return;
			}
			for(int k = i-1;k > sym_index;k--){
				System.out.print(a[k][j] + ",");
			}
			System.out.println();
			print(m, n, sym_index + 1, j, a, 1);
		}
	}
	
	public static int getSymmetryIndex(int start, int max){
		return max - start - 1;
	}
	public static  void main(String[] args) {
		int[][] a = {
				{1,2,3,4},
				{10,11,12,5},
				{9,8,7,6},
		};
		print(3, 4, 0, 0, a, 1);
	}
}
