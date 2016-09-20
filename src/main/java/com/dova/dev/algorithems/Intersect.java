package com.dova.dev.algorithems;


/*
 * give a list of arrays, find all the object which exists in every arrays;
 * this technique is a little like the join of two tables in database applications
 */
public class Intersect {
	
	public static int[] getIntersect(int[][] a){
		if(a.length < 2){
			return null;
		}
		int[] res = new int[a[0].length];
		int size = 0;
		int[] index = new int[a.length];
		boolean finish = false;
		while (!finish) {
			boolean find = true;
			for(int i = 1;i < index.length;i++){
				if(a[i-1][index[i-1]] > a[i][index[i]]){
					index[i]++;
					find = false;
					if(index[i] >= a[i].length){
						finish = true;
					}
				}else if(a[i-1][index[i-1]] < a[i][index[i]]){
					index[i-1]++;
					find = false;
					if(index[i-1] >= a[i-1].length){
						finish = true;
					}
				}
			}
			if(find){
				if(size != 0 && a[0][index[0]] == res[size - 1]){
					//go
				}else {
					res[size++] = a[0][index[0]];
				}
				for(int i = 1;i < index.length;i++){
					index[i]++;
					if(index[i] >= a[i].length){
						finish = true;
					}
				}
			}
		}
		int[] real_res = new int[size];
		System.arraycopy(res, 0, real_res, 0, size);
		return real_res;
	}

	
	public static void main(String args[]){
		int[][] a = {
				{1,2,3,4,4},
				{2,3,4,4},
				{3,4,4}
		};
		int[] res = getIntersect(a);
		for(int i : res){
			System.out.print(i + "\t");
		}
	}
}
