package com.laten.matt.spreasheet;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Spreadsheet {
	Cell cells [][];
	int rows = 26;
	int columns = 9;
	Parser parser;
	
	public Spreadsheet(String [][] spreadsheetCells) throws Exception {
		cells = new Cell[rows][columns];
		parser = new Parser();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				cells[i][j] = new Cell();
				cells[i][j].expression = spreadsheetCells[i][j];
				if (cells[i][j].expression.equals("")) {
					cells[i][j].empty = true;
				} else {
					if (!cells[i][j].expression.startsWith("=")) {
						try {
							cells[i][j].value = Double.parseDouble(cells[i][j].expression);
						} catch (NumberFormatException e) {
							System.out.println(e.getMessage());
						}
					}
				}		
			}
		}
		
		//flatten all expressions and evaluate them
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (cells[i][j].expression.startsWith("=")) {
					cells[i][j].value = parser.evaluate(parser.convert(flatten(i, j)));
				}
			}
		}
	}
	
	public String flatten(int row, int column) throws Exception {
		if (cells[row][column].visited) {
			throw new Exception("Circular reference containing cell " + rowColToCell(row,column));
		}
		String expression = cells[row][column].expression;
		String result;
		if (expression.startsWith("=") && !cells[row][column].processed) {
			expression = expression.substring(1);
			result = expression;
			String[] refs = expression.split("[+-/*]+");
			for (String string : refs) {
				if (Pattern.matches("[A-Z][\\d]+", string)) {
					cells[row][column].visited = true;
					int rc[] = cellToRowCol(string);
					cells[rc[0]][rc[1]].dependents.add(rowColToCell(row, column));
					result = result.replaceAll(string, " ( " + flatten(rc[0], rc[1]) + " ) ");
					cells[row][column].visited = false;
				} else {
					if (Parser.isDouble(string)) {
						result = result.replaceAll(string, " " + string + " ");
					} else {
						throw new Exception(String.format("Invalid Expression in cell %s: \"%s\"", rowColToCell(row, column), string));
					}
				}
			}
			cells[row][column].processed = true;
		} else {
			result = " " + cells[row][column].value + " ";
		}
		cells[row][column].value = parser.evaluate(parser.convert(result));
		return result;
	}
	
	public void removeDeps (int row, int column) {
		String expression = cells[row][column].expression;
		String[] refs = expression.split("[+-/*]+");
		for (String string : refs) {
			if (Pattern.matches("[A-Z][\\d]+", string)) {
				int rc[] = cellToRowCol(string);
				cells[rc[0]][rc[1]].dependents.remove(string);
			}
		}
	}
	
	public boolean processUnprocessed (int row, int column) throws Exception {
		ArrayList<String> toProcess =  new ArrayList<String>();
		for (String string : cells[row][column].dependents) {
			int rc [] = cellToRowCol(string);
			cells[rc[0]][rc[1]].processed = false;
		}
		for (String string : cells[row][column].dependents) {
			int rc [] = cellToRowCol(string);
			if (processUnprocessed(rc[0],rc[1])) {
				toProcess.add(string);
			}
		}
		for (String proc : toProcess) {
			int rc [] = cellToRowCol(proc);
			flatten(rc[0],rc[1]);
		}
		if (cells[row][column].dependents.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	public double getValue(int row, int column) {
		return cells[row][column].value;
	}
	
	public String getExpression(int row, int column) {
		return "" + cells[row][column].expression;
	}
	
	public void setValue(int row, int column, String value) throws Exception {
		removeDeps(row,column);
		cells[row][column].expression = value;
		cells[row][column].processed = false;
		if (!Parser.isDouble(value)) {
			cells[row][column].value = parser.evaluate(parser.convert(flatten(row, column)));
		} else {
			cells[row][column].value = Double.parseDouble(value);
		}
		processUnprocessed(row,column);
	}
	
	public String toString() {
		String result = "  |";
		for (int j = 0; j < columns; j++) {
			result += String.format("%1$5d |", j+1);
		}
		result += "\n---";
		for (int j = 0; j < columns; j++) {
			result += "-------";
		}
		result += "\n";
		for (int i = 0; i < rows; i++) {
			result += String.format("%s |",(char) ('A' + i));
			for (int j = 0; j < columns; j++) {
				result += String.format("%1$5s |", getValue(i,j));
			}
			result += "\n---";
			for (int j = 0; j < columns; j++) {
				result += "-------";
			}
			result += "\n";
		}
		return result;
	}
	
	public static int [] cellToRowCol(String cell) {
		int r = cell.charAt(0) - 'A';
		int c = Integer.parseInt(cell.substring(1))-1;
		return new int [] {r,c};
	}
	
	public static String rowColToCell (int row, int column) {
		return (char) (row+'A')+""+(column+1);
	}
}
