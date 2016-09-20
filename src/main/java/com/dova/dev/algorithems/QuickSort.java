package com.dova.dev.algorithems;

/*
 * an implementation for the quick sort
 */

public class QuickSort {
	
	public static void sort(int a[], int start,int end) {
		if(start >= end){
			return;
		}
		int base = a[start];
		int i = start + 1,j = end;
		while (j >= i) {
			if(a[j] >= base){
				j--;
				continue;
			}
			while(i < j){
				if(a[i] <= base){
					i++;
					continue;
				}
				int tmp = a[i];
				a[i] = a[j];
				a[j] = tmp;
				break;
			}
			if(i >= j){
				break;
			}
		}
		if(j != start){
			a[start] = a[j];
			a[j] = base;
			sort(a, start, j - 1);
		}
		sort(a, j+1, end);
	}
	
	public static void testCal(){
		long start = System.currentTimeMillis();
		for (int i = 0; i < (1 << 10); i++) {
			int num = 100000;
			int[] a = new int[num];
			for (int j = 0; j < num; j++) {
				a[j] = (int)(num * 100 * Math.random());
			}
			sort(a, 0, a.length - 1);
		}
		long cal_end = System.currentTimeMillis();
		System.out.println(cal_end - start);
	}
	public static void main(String args[]){
		testCal();
	}
}
