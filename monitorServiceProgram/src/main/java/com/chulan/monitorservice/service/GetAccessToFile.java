/**
 * @author Chulan Madurapperuma
 * Getting the text file ready upto the previous point of reading or writing to the text file
 **/
package com.chulan.monitorservice.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;


public class GetAccessToFile {

	String fileName;
	ClassLoader classLoader = getClass().getClassLoader(); // get the class loader
	

	// to read the registered text file
	public BufferedReader toRead(String name) {

		try {
			
			fileName = classLoader.getResource(name).getPath(); // append the text file name
			FileReader fileReader = new FileReader(fileName); // FileReader reads text files in the default encoding.
			BufferedReader bufferedReader = new BufferedReader(fileReader); // Always wrap FileReader in BufferedReader.
			return bufferedReader;

		} catch (FileNotFoundException e) { // Handling File not found exception

			System.out.println("Unable to open file '" + fileName + "'");
			e.printStackTrace();
		}
		return null;
	}
	
	// to write the service and port to registered text file
	public BufferedWriter toWrite(String name) {

		try {

			fileName = classLoader.getResource(name).getPath(); // append the text file name
			FileWriter fileWriter = new FileWriter(fileName,true); // Assume default encoding and set for appending.
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter); // Always wrap FileWriter in BufferedWriter.
			return bufferedWriter;
			
		}  catch (Exception ex) { //handling the exceptions
			System.out.println("Error occured related to  '" + fileName + "'");
			// ex.printStackTrace();
		}
		
		return null;
	}

}
