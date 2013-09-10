package com.laten.matt.spreasheet;

import java.util.Stack;

public class Parser {
	public Parser () {
		
	}
	
	public double parse(String expression) {
		Stack<Double> stack = new Stack<Double>();
		for (String token : expression.split(" ")) {
			try {
				stack.push(Double.parseDouble(token));
			} catch (NumberFormatException e) {
				
			}
		}
		return 0;
	}
}
