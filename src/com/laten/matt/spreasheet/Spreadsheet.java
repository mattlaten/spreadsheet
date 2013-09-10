package com.laten.matt.spreasheet;

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
				}
				if (cells[i][j].expression.startsWith("=")) {
				
				} else {
					try {
						cells[i][j].value = Double.parseDouble(cells[i][j].expression);
					} catch (NumberFormatException e) {
						
					}
				}
					
			}
		}
	}
	
	public double getValue(int i, int j) {
		return 0;
	}
}
