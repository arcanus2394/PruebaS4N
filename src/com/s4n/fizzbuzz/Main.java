package com.s4n.fizzbuzz;

public class Main {

	public static void main(String[] args) {
		for(int i=1;i<=100;i++) {
			NumeroString comodin = new NumeroString(i);
			System.out.println(comodin.validator());
		}
	}
}
