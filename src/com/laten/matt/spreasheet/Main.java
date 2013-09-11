package com.laten.matt.spreasheet;

public class Main {
	public static void main (String [] args) {
		Spreadsheet spreadsheet = new Spreadsheet(FileIO.readIn("input/in1"));
		//System.out.println("Value at (0,0) = " + spreadsheet.getValue(0, 0));
		//System.out.println(parser.parse("1 2 /"));
		//System.out.println(Parser.evaluate(Parser.parse(spreadsheet.flatten(0,0))));
		System.out.println(spreadsheet);
	}
}
