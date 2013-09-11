package com.laten.matt.spreasheet;

import java.util.regex.Pattern;

public class Spreadsheet {
	Cell cells [][];
	int rows = 26;
	int columns = 9;
	
	public Spreadsheet(String [][] spreadsheetCells) throws Exception {
		cells = new Cell[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				cells[i][j] = new Cell();
				cells[i][j].expression = spreadsheetCells[i][j];
				if (cells[i][j].expression.equals("")) {
					cells[i][j].empty = true;
				} else {
					if (cells[i][j].expression.startsWith("=")) {
						
					} else {
						try {
							cells[i][j].value = Double.parseDouble(cells[i][j].expression);
						} catch (NumberFormatException e) {
							System.out.println(e.getMessage());
						}
					}
				}
					
			}
		}
		
		//flatten all expressions and
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (cells[i][j].expression.startsWith("=")) {
					cells[i][j].expression = flatten(i, j);
					cells[i][j].value = Parser.evaluate(Parser.parse(cells[i][j].expression));
				}
			}
		}
	}
	
	public String flatten(int row, int column) throws Exception {
		String expression = cells[row][column].expression;
		String result;
		if (expression.startsWith("=")) {
			//process it
			expression = expression.substring(1);
			result = expression;
			String [] refs = expression.split("[+-/*]+");
			for (String string : refs) {
				System.out.println(string);
				if (Pattern.matches("[A-Z][\\d]+", string)) {
					int r = string.charAt(0) - 'A';
					int c = Integer.parseInt(string.substring(1))-1;
					result = result.replaceAll(string, " ( " + flatten(r,c) + " ) " );
				} else {
					if (Parser.isDouble(string)) {
						result = result.replaceAll(string, " " + string + " ");
					} else {
						throw new Exception(String.format("Invalid Expression in cell %s: \"%s\"", rowColToCell(row,column), string));
					}
				}
			}
		} else {
			result = " " + cells[row][column].value + " ";
		}
		return result;
		
	}
	
	public String getValue(int row, int column) {
		if (!cells[row][column].isEmpty()) {
			return "" + cells[row][column].value;
		} else {
			return "";
		}
	}
	
	public String getExpression(int row, int column) {
		return "" + cells[row][column].expression;
	}
	
	public void setValue(int row, int column, String value) {
		cells[row][column].expression = value;
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
		return (char) (row+'A')+""+column;
	}
}
