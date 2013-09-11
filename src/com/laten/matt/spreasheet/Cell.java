package com.laten.matt.spreasheet;

import java.util.HashSet;
import java.util.Set;

public class Cell {
	String expression;
	double value;
	boolean empty;
	boolean processed;
	Set<String> dependants;
	
	public Cell() {
		expression = "";
		empty = false;
		processed = false;
		dependants = new HashSet<String>();
	}
	
	public boolean isEmpty() {
		return empty;
	}
}
