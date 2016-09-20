package com.dova.dev.algorithems;

/*
 * give a list of chars,print all the possible strings made up of these chars.
 */
public class ArrangeString {
	
	public static void printAllStrings(char[] a,int index){
		if(index == a.length -1){
			System.out.println(String.copyValueOf(a));
			return;
		}
		for(int i = index;i < a.length;i++){
			if(i != a.length - 1){
				char[] tmp = a.clone();
				char tmpc = tmp[index];
				tmp[index] = tmp[i];
				tmp[i] = tmpc;
				printAllStrings(tmp, index + 1);
			}else {
				char tmp = a[index];
				a[index] = a[i];
				a[i] = tmp;
				printAllStrings(a, index + 1);
			}
		}
	}
	public static void main(String[] args){
		char[] a = {'a','b','c','在'};
		printAllStrings(a, 0);
	}
}
