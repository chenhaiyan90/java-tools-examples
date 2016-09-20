package com.dova.dev.algorithems;

import java.awt.Dialog.ModalExclusionType;

import org.omg.PortableServer.AdapterActivator;
/*
 * 2 dollar = 1 beer; 2 empty bottle = 1 beer; 4 cap = 1 beer;
 * if you have N dollars, how many bottles of beer can you drink?
 * 
 */

public class BeerCount {

	static class Left{
		int money;
		int bottle;
		int cap;
		int num;
	}
	public static void add(Left a){
		while (a.money >= 2 || a.cap >= 4 || a.bottle >= 2) {
			if(a.money >= 2){
				a.bottle++;
				a.cap++;
				a.num++;
				a.money = a.money - 2;
			}
			if(a.bottle >= 2){
				a.bottle++;
				a.cap++;
				a.num++;
				a.bottle = a.bottle- 2;
			}
			if(a.cap >=4){
				a.bottle++;
				a.cap++;
				a.num++;
				a.cap = a.cap - 4;
			}
		}
	}
	public static int buy(int num){
		Left left = new Left();
		for(int i =0; i < num; i++){
			left.money++;
			add(left);
		}
		System.out.println(String.format("%d beer:%d bottle:%d cap:%d money:%d", num, left.num, left.bottle, left.cap, left.money));
		return left.num;
	}
	
	//a perfert solution
	public static int perfertBuy(int num) {
		int n = num/2,ping =num/2, gai = num/2, temp = 0;
		do{
			n += temp = ping/2 + gai/4;
			ping = temp + ping % 2;
			gai = temp + gai % 4;
		}while(ping >= 2 || gai >= 4);
		return n;
	}
	public static void main(String args[]) {
		for (int i = 0; i < 21; i++) {
			buy(i);
			System.out.println(String.format("%d %d %d", i, buy(i), perfertBuy(i)));
		}
	}
	
}
