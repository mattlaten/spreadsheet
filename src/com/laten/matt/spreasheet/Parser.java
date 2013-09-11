package com.laten.matt.spreasheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Parser {
	private Map<String, Integer> operators = new HashMap<String, Integer>();
	
	public Parser() {
		operators.put("+", 0);
		operators.put("-", 0);
		operators.put("*", 5);
		operators.put("/", 5);
	}
	
	private boolean isOperator(String token) {
		return operators.containsKey(token);
	}

	private int compare(String token1, String token2) {
		if (!isOperator(token1) || !isOperator(token2)) {
			throw new IllegalArgumentException("Invalied tokens: " + token1 + " " + token2);
		}
		return operators.get(token1) - operators.get(token2);
	}
	
	//Shunting yard algorithm
	public String [] convert(String expression) {
		ArrayList<String> out = new ArrayList<String>();
		Stack<String> stack = new Stack<String>();
		String [] tokens = expression.split("[ ]+");
		for (String token : tokens) {
			if (token.equals("")) {
				continue;
			}
			if (isOperator(token)) {
				while (!stack.empty() && isOperator(stack.peek())) {
					if (compare(token, stack.peek()) <= 0) {
						out.add(stack.pop());
						continue;
					}
					break;
				}
				stack.push(token);
			} else if (token.equals("(")) {
				stack.push(token); 
			} else if (token.equals(")")) {
				while (!stack.empty() && !stack.peek().equals("(")) {
					out.add(stack.pop()); 
				}
				stack.pop();
			} else {
				out.add(token); 
			}
		}
		while (!stack.empty()) {
			out.add(stack.pop());
		}
		String[] output = new String[out.size()];
		return out.toArray(output);
	}
	
	//parse expression in Reverse Polish Notation
	public static double evaluate(String [] tokens) {
		Stack<Double> stack = new Stack<Double>();
		for (String token : tokens) {
			if (isDouble(token)) {
				stack.push(Double.parseDouble(token));
			} else {
				double b = stack.pop();
				double a = stack.pop();
				switch(token.charAt(0)) {
				case '+': 
					stack.push(a+b);
					break;
				case '-': 
					stack.push(a-b);
					break;
				case '*': 
					stack.push(a*b);
					break;
				case '/': 
					stack.push(a/b);
					break;
				}
			}
		}
		return stack.pop();
	}
	
	public static boolean isDouble(String value) {
	    try {
	        Double.parseDouble(value);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
}
