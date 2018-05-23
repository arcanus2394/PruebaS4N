package com.s4n.fizzbuzztest;
import com.s4n.fizzbuzz.NumeroString;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;


public class NumeroStringTest {
	String fizz = "Fizz";
	String buzz = "Buzz";
	String FB   = "FizzBuzz";
	

    // creates the test data 
	@Test
	public void validatorTest1(){
		System.out.println("Verifacion del método de validacion con el numero 1");
		NumeroString numero1= new NumeroString(1);
		assertEquals(numero1.validator(),"1");
	}
	
	@Test
	public void validatorTest3(){
		System.out.println("Verifacion del método de validacion con el numero 3");
		NumeroString numero3= new NumeroString(3);
		assertEquals(numero3.validator(),fizz);
	}
	
	@Test
	public void validatorTest5(){
		System.out.println("Verifacion del método de validacion con el numero 5");
		NumeroString numero5= new NumeroString(5);
		assertEquals(numero5.validator(),buzz);
	}
	
	@Test
	public void validatorTest15(){
		System.out.println("Verifacion del método de validacion con el numero 15");
		NumeroString numero15= new NumeroString(15);
		assertEquals(numero15.validator(),FB);
	}
	
	@Test
	public void validatorTestN(){
		System.out.println("Verifacion del método de validacion con el numero 98");
		NumeroString numeroN= new NumeroString(98);
		assertEquals(numeroN.validator(),"98");
	}
	
	
	
	
}
