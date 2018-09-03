/**
 * @author Chulan Madurapperuma
 * Regiestered services read from the text file
 **/
package com.chulan.monitorservice.service;

import java.io.*;
import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;

import org.springframework.context.ApplicationContext;

import com.chulan.monitorservice.model.ServiceClass;


public class ReadFile extends GetAccessToFile {

	
	String line = null;
	List<String> serviceList = new ArrayList<String>();
	int mCount =1;

	public List<String> read(String name,int mCase) {
		try {

			BufferedReader bufferedReader = toRead(name); // assign the text file to the buffer reader
			if (bufferedReader != null) { //checking for a null bufferReader
				
				while ((line = bufferedReader.readLine()) != null) {
					System.out.print("Service No = " +mCount +". "); 
					String[] serviceComp = line.split("-");
					System.out.println( "IP: " +serviceComp[0] +" PORT: " + serviceComp[1]);
					serviceList.add(line);
					mCount+=1;
				}
				bufferedReader.close(); // Always close files.
				if(mCase==2) {
					System.out.println("Please select a Service No from the above list to monitor a service");
				}
				else if(mCase==3) {
					System.out.println("Please select Service No from the above list to monitor multiple services");
					System.out.println("Enter service No's as comma seperated values. eg= x,x,x");
				}
			}

		} catch (IOException ex) { //handling input output exceptions
			System.out.println("Error reading file '" + fileName + "'");
			// ex.printStackTrace();
		}
		return serviceList;
	}
	
	public ServiceClass selectService(Scanner sc, List<String> sList, ApplicationContext context) {
		int inputReceive = 0; 
		int mCount =0;
		
		//POLL FREQUENCY validation goes below
		while(inputReceive==0) { 
			
			inputReceive = sc.nextInt();
			try {
				inputReceive = Integer.valueOf(inputReceive);
				mCount = sList.size();
				
				if(inputReceive > mCount || inputReceive ==0) {
					inputReceive = 0;
					System.out.println("Entered Service No is not Available, Please try again.. ");
					System.out.println("Enter the Service No want to monitor: ");
				}
				else {
					//Get the Grace period from user input
					int gracePeriod =getGracePeriod(sc);
					
					ServiceClass serviceClass = (ServiceClass) context.getBean("serviceClass"); // get the serviceClass object
					String objLine = sList.get(inputReceive-1);
					String[] serviceComp = objLine.split("-");
					
					//set the service object values
					serviceClass.setIp(serviceComp[0]);
					serviceClass.setPort(Integer.valueOf(serviceComp[1]));
					serviceClass.setPollFrequency(Integer.valueOf(serviceComp[2]));
					serviceClass.setOutageStart(serviceComp[3]);
					serviceClass.setOutageEnd(serviceComp[4]);
					serviceClass.setGracePeriod(gracePeriod);
					
					return serviceClass;
				}
			}
			catch (NumberFormatException ex) {
				inputReceive = 0;
				System.out.println("Entered Service No is not valid, Please try again.. ");
				System.out.println("Enter the Service No want to monitor: ");
			}
		}
		return null;
	}
	
	//Get the Grace period from user input
	public int getGracePeriod(Scanner sc) {
		
		// Grace period attribute
		System.out.println("Enter the grace period in seconds: ");
		String inputReceive = null; 
		//Grace period validation goes below
		while(inputReceive==null) { 
			
			inputReceive = sc.next();
			try {
				return Integer.valueOf(inputReceive);
			}
			catch (NumberFormatException ex) {
				inputReceive = null;
				System.out.println("Entered grace period is not valid, Please try again.. ");
				System.out.println("Enter the grace period in seconds: ");
			}
		}
		return 0;
	}
	
	//GET SERVICES List
	public List<ServiceClass> selectMultiServices(Scanner sc, List<String> sList, ApplicationContext context) {
		String inputReceive = null; 
		int mCount =sList.size();
		List<ServiceClass> serviceList = new ArrayList<ServiceClass>();
		
		//POLL FREQUENCY validation goes below
		while(inputReceive==null) { 
			
			inputReceive = sc.next();
			try {
				String [] sNos = inputReceive.split(",");
				int [] mNos = new int[sNos.length];
				
				//Get an Integer array for the services
				for (int i = 0; i < sNos.length; i++) {
					
					mNos[i] = Integer.valueOf(sNos[i]);
					
					// validate each service no
					if(mNos[i] > mCount || mNos[i] ==0) {
						inputReceive=null;
						System.out.println("Service No - "+ mNos[i] + " is not Available, Please try again.. ");
						System.out.println("Enter the Service No's want to monitor as comma seperated values: ");
						break;
					}
				}
				
				if(inputReceive!=null) {
					
					//Get the Grace period for all services
					int gracePeriod =getGracePeriod(sc);
					
					//creating service objects from a loop
					for (int i = 0; i < mNos.length; i++) {
						
						String objLine = sList.get(mNos[i]-1);
						String[] serviceComp = objLine.split("-");
						
						//creating new service class object because spring bean is using singleton service class object
						//because of that every-time the current object values are setting without creating a new object
						ServiceClass serviceClass = new ServiceClass();
						
						//set the service object values
						serviceClass.setIp(serviceComp[0]);
						serviceClass.setPort(Integer.valueOf(serviceComp[1]));
						serviceClass.setPollFrequency(Integer.valueOf(serviceComp[2]));
						serviceClass.setOutageStart(serviceComp[3]);
						serviceClass.setOutageEnd(serviceComp[4]);
						serviceClass.setGracePeriod(gracePeriod);
						
						serviceList.add(serviceClass);
					}
					
					//Detect multiple callers registering interest in the same service
					MultipleCallersForSameService multipleCallers = (MultipleCallersForSameService) context.getBean("multipleCallers"); // get the Multiple Callers For Same Service object
					return multipleCallers.detectMultiple(serviceList);
				}
			}
			catch (NumberFormatException ex) {
				inputReceive = null;
				System.out.println("Entered Service No is not valid, Please try again.. ");
				System.out.println("Enter the Service No's want to monitor as comma seperated values: ");
			}
		}
		return null;
	}
}