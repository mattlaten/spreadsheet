package com.laten.matt.spreasheet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileIO {
	
	public static String[][] readIn(String filename) {
		;
		String [][] cells = new String[26][9];
		BufferedReader input;
		try {
			input = new BufferedReader(new FileReader(filename));
			for (int i = 0; i < 26; i++) { 
				System.out.print(i + ": ");
				cells[i] = input.readLine().split(",", -1);
				for (int j = 0; j < 9; j++) {
					System.out.print(cells[i][j] + ":");
					if (cells[i][j] == null) {
						System.out.println("HERP");
						cells[i][j] = "";
					}
				}
				System.out.println();
			}
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cells;
	}
}
