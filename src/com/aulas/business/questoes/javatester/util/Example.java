package com.aulas.business.questoes.javatester.util;

@ClassTest(name = "Example")
public class Example {
	@FieldTest
	private static int a;
	
	@FieldTest(withSetter=false)
	private int b;
	
	@MethodTest
	public static int foo(int a, int b, int c) {
	    return 0;
	}
	
	@MethodTest
	public int getA() {
	    return 0;
	}
	
	@MethodTest
	public int work(int b, int c, int a) {
	    return 0;
	}
	
	public void setA(int t) {
	    
	}
	
	public int getB() {
	    return 0;
	}
}
