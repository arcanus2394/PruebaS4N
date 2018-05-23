package com.s4n.fizzbuzz;
import org.junit.Test;

/*Clase encargada del manejo de todo lo referente al numero a imprimir*/
public class NumeroString {
	private String strNumero = "";
	private int numero;
	
	public NumeroString(int i){
		this.numero=i;
	}
	
	/*Metodo encargado de la validacion del numero de la iteracion*/
	public String validator() {
		if(numero%3==0) {
			strNumero +="Fizz";
		}
		if(numero%5==0) {
			strNumero +="Buzz";
		}
		if(strNumero=="") {
			strNumero +=numero;
		}
		return strNumero;	
			
	}
	
}
