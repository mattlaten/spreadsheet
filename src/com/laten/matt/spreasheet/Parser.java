package com.laten.matt.spreasheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Parser {
	
	// Associativity constants for operators
		private static final int LEFT_ASSOC = 0;
		private static final int RIGHT_ASSOC = 1;

		// Supported operators
		private static final Map<String, int[]> OPERATORS = new HashMap<String, int[]>();
		static {
			OPERATORS.put("+", new int[] { 0, LEFT_ASSOC });
			OPERATORS.put("-", new int[] { 0, LEFT_ASSOC });
			OPERATORS.put("*", new int[] { 5, LEFT_ASSOC });
			OPERATORS.put("/", new int[] { 5, LEFT_ASSOC });
		}
	
	private static boolean isOperator(String token) {
		return OPERATORS.containsKey(token);
	}
	
	private static boolean isAssociative(String token, int type) {
		if (!isOperator(token)) {
			throw new IllegalArgumentException("Invalid token: " + token);
		}
		if (OPERATORS.get(token)[1] == type) {
			return true;
		}
		return false;
	}

	private static final int cmpPrecedence(String token1, String token2) {
		if (!isOperator(token1) || !isOperator(token2)) {
			throw new IllegalArgumentException("Invalied tokens: " + token1
					+ " " + token2);
		}
		return OPERATORS.get(token1)[0] - OPERATORS.get(token2)[0];
	}
		
	public Parser () {
		
	}
	
	public static String [] parse(String expression) {
		ArrayList<String> out = new ArrayList<String>();
		Stack<String> stack = new Stack<String>();
		String [] tokens = expression.split("[ ]+");
		// For all the input tokens [S1] read the next token [S2]
		for (String token : tokens) {
			if (token.equals("")) {
				continue;
			}
			if (isOperator(token)) {
				// If token is an operator (x) [S3]
				while (!stack.empty() && isOperator(stack.peek())) {
					// [S4]
					if ((isAssociative(token, LEFT_ASSOC) && cmpPrecedence(
							token, stack.peek()) <= 0)
							|| (isAssociative(token, RIGHT_ASSOC) && cmpPrecedence(
									token, stack.peek()) < 0)) {
						out.add(stack.pop()); 	// [S5] [S6]
						continue;
					}
					break;
				}
				// Push the new operator on the stack [S7]
				stack.push(token);
			} else if (token.equals("(")) {
				stack.push(token); 	// [S8]
			} else if (token.equals(")")) {
				// [S9]
				while (!stack.empty() && !stack.peek().equals("(")) {
					out.add(stack.pop()); // [S10]
				}
				stack.pop(); // [S11]
			} else {
				out.add(token); // [S12]
			}
		}
		while (!stack.empty()) {
			out.add(stack.pop()); // [S13]
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
