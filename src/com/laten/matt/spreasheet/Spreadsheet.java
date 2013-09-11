package com.laten.matt.spreasheet;

import java.util.regex.Pattern;

public class Spreadsheet {
	Cell cells [][];
	int rows = 26;
	int columns = 9;
	
	public Spreadsheet(String [][] spreadsheetCells) {
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
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (cells[i][j].expression.startsWith("=")) {
					cells[i][j].expression = flatten(i, j);
					cells[i][j].value = Parser.evaluate(Parser.parse(cells[i][j].expression));
				}
			}
		}
	}
	
	public String flatten(int row, int column) {
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
					result = result.replaceAll(string, " " + string + " ");
				}
			}
		} else {
			result = " " + cells[row][column].value + " ";
		}
		return result;
		
	}
	
	public String getValue(int row, int column) {
		if (cells[row][column].isEmpty()) {
			return "";
		} else {
			return "" + cells[row][column].value;
		}
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
				result += String.format("%1$5s |", cells[i][j].value);
			}
			result += "\n---";
			for (int j = 0; j < columns; j++) {
				result += "-------";
			}
			result += "\n";
		}
		return result;
	}
}
