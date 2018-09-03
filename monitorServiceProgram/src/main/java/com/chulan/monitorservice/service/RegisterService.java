/**
 * @author Chulan Madurapperuma
 * Register a service and write back to the text file containing the services
 **/
package com.chulan.monitorservice.service;

import java.io.*;
import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.chulan.monitorservice.model.ServiceClass;

public class RegisterService extends GetAccessToFile {
	
	//get the required data from the user
	public void registerData(Scanner sc, ApplicationContext context) {
		
		ServiceClass serviceClass = (ServiceClass) context.getBean("serviceClass"); // get the serviceClass object
		
		String inputReceive; //variable to receive the user inputs
		
		//1. IP Address attribute
		System.out.println("Enter the service ip address: ");
		
		boolean line = false; // for validation purpose
		inputReceive = sc.nextLine(); // to avoid the auto jump(skip) in the first time
		//IP validation goes below
		while(line==false) { 
			
			inputReceive = sc.nextLine();
			line = InputValidate.validateIp(inputReceive);
			if (line==false) {
				System.out.println("Entered IP address is not valid, Please try again.. ");
				System.out.println("Enter the service ip address: ");
			}
			else {
				serviceClass.setIp(inputReceive);
				System.out.println(inputReceive);
				break;
			}
		}
		
		//2. PORT attribute
		System.out.println("Enter the service port: ");
		line = false; // for validation purpose
		//PORT validation goes below
		while(line==false) { 
			
			inputReceive = sc.nextLine();
			line = InputValidate.validatePort(inputReceive);
			if (line==false) {
				System.out.println("Entered service port is not valid, Please try again.. ");
				System.out.println("Enter the service port: ");
			}
			else {
				serviceClass.setPort(Integer.valueOf(inputReceive));
				System.out.println(inputReceive);
				break;
			}
		}
		
		//3. POLL FREQUENCY attribute
		System.out.println("Enter the polling frequency in seconds: ");
		inputReceive = null; 
		//POLL FREQUENCY validation goes below
		while(inputReceive==null) { 
			
			inputReceive = sc.nextLine();
			try {
				serviceClass.setPollFrequency(Integer.valueOf(inputReceive));
				break;
			}
			catch (NumberFormatException ex) {
				inputReceive = null;
				System.out.println("Entered polling frequency is not valid, Please try again.. ");
				System.out.println("Enter the polling frequency in seconds: ");
			}
		}
		
		//4. CONFIRMING TO CREATE AN OUTAGE FOR THE SERVICE
		System.out.println("Do you want to create a service outage? : (y|n)");
		line = false; // for validation purpose
		//YES-NO validation goes below
		while(line==false) { 
			
			inputReceive = sc.nextLine();
			line = InputValidate.validateYesNo(inputReceive);
			if (line==false) {
				System.out.println("Entered answer is not valid, Please try again.. ");
				System.out.println("Do you want to create a service outage? : (y|n)");
			}
			else {
				if(inputReceive.equals("y")) {
					serviceClass.setOutageStart(createServiceOutage("start",sc)); ; // to create service outage start time
					serviceClass.setOutageEnd(createServiceOutage("end",sc)); ; // to create service outage end time
				}
				else {
					serviceClass.setOutageStart("0");
					serviceClass.setOutageEnd("0");
				}
				//
				
				// create the service and write the service in to the file (passing the converted object to a string)
				write("registered.txt", serviceClass.toSaveAsString());
				break;
			}
		}
		
		((FileSystemXmlApplicationContext) context).close(); // close the bean
		System.out.println(serviceClass.toString());
	}
	
	//SERVICE OUTAGE START AND END TIME DEFINITION GOES HERE 
	public String createServiceOutage(String outageType, Scanner sc) {
		
		String inputReceive;
		//1. OUTAGE START TIME attribute
		System.out.println("Enter the service outage " + outageType +" time: [HH:MM] 24 hour format");
		boolean line = false; // for validation purpose
		//PORT validation goes below
		while(line==false) { 
			
			inputReceive = sc.nextLine();
			line = InputValidate.validateTime(inputReceive);
			if (line==false) {
				System.out.println("Entered service outage " + outageType + " time is not valid, Please try again.. ");
				System.out.println("Enter the service outage " + outageType +" time: [HH:MM] 24 hour format");
			}
			else {
				System.out.println(inputReceive);
				return inputReceive;
			}
		}
		return null;
	}
	
	//TO WRITE THE SERVICE INTO THE FILE 
	public void write(String name, String serviceObj) {

		try {

			BufferedWriter bufferedWriter = toWrite(name);// assign the text file to the buffer writer
			if (bufferedWriter != null) { // checking for a null buffer writer

				bufferedWriter.write(serviceObj); //append to the text file
	            bufferedWriter.close(); // Always close files.
	            System.out.println("Service Registered Successfully.");
			}
			
		} catch (IOException ex) {
			
			System.out.println("Error writing to file '" + fileName + "'");
			// ex.printStackTrace();
		}
	}

}
