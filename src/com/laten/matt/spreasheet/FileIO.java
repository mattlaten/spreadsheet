package com.laten.matt.spreasheet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileIO {
	
	public static String[][] readIn(String filename) throws IOException {
		String [][] cells = new String[26][9];
		BufferedReader input;
			input = new BufferedReader(new FileReader(filename));
			for (int i = 0; i < 26; i++) {
				cells[i] = input.readLine().split(",", -1);				
			}
			input.close();
		
		return cells;
	}
}
