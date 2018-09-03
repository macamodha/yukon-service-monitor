/**
 * @author Chulan Madurapperuma
 * Initial class to display in the console and to get user entered inputs
 **/
package com.chulan.monitorservice.service;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InitialData {
	
	String [] serviceNames;
	
	//getting the user input first
	public int init(Scanner sc) {
		
		//Define the available Services to users
		serviceNames = new String[3];
		serviceNames[0]="1. Register a Service.";
		serviceNames[1]="2. Monitor  a Service.";
		serviceNames[2]="3. Monitor  Multiple Services.";
		
		System.out.println("Welcome To The Service Monitor application...");
		System.out.println("Please Select a number to use the below listed services");
		
		//List the available services
		for (String string : serviceNames) {
			System.out.println(string);
		}
		
		try {
			//capturing the user input
			int i = sc.nextInt();
			
			return i; 
			
		}
		catch (InputMismatchException ex) { // Exception handling
			 System.out.println("Invalid Input Entered.");
		}
		catch (Exception ex) { // Exception handling
			System.out.println("Error occured while getting the user input.");
		}
		return 0;
	}
	
}
