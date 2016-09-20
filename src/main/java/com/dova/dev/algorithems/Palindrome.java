package com.dova.dev.algorithems;

import javax.management.MXBean;

/*
 * question: find the longest palindrome in a string,e.g for 'abacdedc', the result is 'cdedc' ,not 'aba'
 * 
 */
public class Palindrome {
	
	public static String findCommonSubstr(String s1, String s2){
		
		if(s1.length() == 0 || s2.length() == 0){
			return "";
		}
		int max = 0;
		int start = 0;
		int end = 0;
		for(int i =0; i < s1.length();i++){
			if(s1.length() - i < max){
				break;
			}
			for (int j = 0; j < s2.length(); j++) {
				if(s2.length() - j < max){
					break;
				}
				int s1_i = i;
				int s2_i = j;
				while (s1_i < s1.length() && s2_i < s2.length()) {
					if(s1.charAt(s1_i) == s2.charAt(s2_i)){
						s1_i++;
						s2_i++;
					}else {
						if(max < s1_i - i){
							start = i;
							end = s1_i;
							max = s1_i - i;
						}
						break;
					}
				}
				if(max < s1_i - i){
					start = i;
					end = s1_i;
					max = s1_i - i;
				}
			}
		}
		return s1.substring(start, end);
	}
	
	public static String findPalindrome(String s){
		char[] chars = s.toCharArray();
		char[] revert = new char[chars.length];
		for(int i =0; i < revert.length;i++){
			revert[i] = chars[revert.length - i - 1];
		}
		return findCommonSubstr(s, String.copyValueOf(revert));
	}
	
	public static void  main(String args[]) {
		String s = "abacdedcsssabcdedcba";
		System.out.println(findPalindrome(s));
	}
}
