package com.dova.dev.algorithems;

import java.util.Stack;

/*
 * give an array of  M*N,  which only contains the 0-1 values. value 1 menas the node can be got through,otherwise cannot.
 * find a path from node (0,0) to node (M-1,N-1) if it  exists  
 */
public class SimpleMaze {
	
	public static void findPath(int[][] a, int m, int n){
		if(a.length != m || a[0].length != n){
			return;
		}
		Stack<Integer> stack = new Stack<Integer>();
		int[][] visits = new int[m][n];
		stack.push(0);
		while (true) {
			if(stack.empty()){
				break;
			}
			int ele = stack.peek();
			int i = (int)(ele / n);
			int j = ele % n;
			visits[i][j] = 1;
			if( i == m-1 && j == n-1){
				break;
			}
			if(i+1 <= m-1){
				if(visits[i+1][j] == 0 && a[i+1][j] == 1){
					stack.push((i+1) * n + j);
					continue;
				}
			}
			if(j+1 <= n-1){
				if(visits[i][j+1] == 0 && a[i][j+1] == 1){
					stack.push( i * n + j + 1);
					continue;
				}
			}
			if(i - 1 >= 0){
				if(visits[i-1][j] == 0 && a[i-1][j] == 1){
					stack.push((i-1) * n + j);
					continue;
				}
			}
			if(j-1 >= 0){
				if(visits[i][j-1] == 0 && a[i][j-1] == 1){
					stack.push( i * n + j -1);
					continue;
				}
			}
			
			stack.pop();
		}
		
		if(stack.empty()){
			System.out.println("cannot find the path");
		}else {
			for(int k : stack){
				int i = k / n;
				int j = k % n;
				System.out.print(i +"-" + j + ",");
			}
		}
	}
	public static void main(String[] args){
		int[][] a = {
				{1,1,1,0},
				{0,0,1,0},
				{0,1,1,0},
				{1,1,0,1},
				{1,1,1,1}
		};
		findPath(a, 5, 4);
		
	}
}
