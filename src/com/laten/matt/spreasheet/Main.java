package com.laten.matt.spreasheet;

import java.util.Scanner;

public class Main {
	public static void main (String [] args) { 
		try {
			System.out.println("Matt's Spreadsheet Processor");
			System.out.println("==============================================");
			System.out.println("Please enter in a filename (accepts csv only):");
			Scanner input = new Scanner(System.in);
			String filename = input.next();
			Spreadsheet spreadsheet = new Spreadsheet(FileIO.readIn(filename));
			boolean running = true;
			while (running) {
				System.out.println("Menu");
				System.out.println("----------------------------------------------");
				System.out.println("A) Get a cell value");
				System.out.println("B) Set a cell value");
				System.out.println("C) Print the spreadsheet");
				System.out.println("X) Exit");
				System.out.println("Your choice: ");				
				char choice = input.next().toUpperCase().charAt(0);
				String cell;
				int rc [];
				switch (choice) {
				case 'A':
					System.out.println("Please enter cell location:");
					cell = input.next().toUpperCase();
					rc = Spreadsheet.cellToRowCol(cell);
					System.out.println(String.format("Cell value in %s is %s", cell, spreadsheet.getValue(rc[0], rc[1])));
					System.out.println("");
					break;
				case 'B':
					System.out.println("Please enter cell location:");
					cell = input.next().toUpperCase();
					rc = Spreadsheet.cellToRowCol(cell);
					System.out.println("Please enter value:");
					String value = input.next().toUpperCase();
					spreadsheet.setValue(rc[0], rc[1], value);
					System.out.println(String.format("Value in cell %s set to %s", cell, value));
					break;
				case 'C':
					System.out.println(spreadsheet);
					System.out.println("");
					break;
				case 'X':
					input.close();
					System.exit(0);
					break;
				default:
					System.out.println("Invalid choice: " + choice);
					System.out.println("");
					break;
				}
			}
			
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}
	
	
}
