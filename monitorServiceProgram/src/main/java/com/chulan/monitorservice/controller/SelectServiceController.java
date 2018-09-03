/**
 * @author Chulan Madurapperuma
 * To Decide which service is user expecting 
 **/
package com.chulan.monitorservice.controller;

import java.util.List;
import java.util.Scanner;

import org.springframework.context.ApplicationContext;

import com.chulan.monitorservice.model.ServiceClass;
import com.chulan.monitorservice.service.InitialData;
import com.chulan.monitorservice.service.MonitorTheService;
import com.chulan.monitorservice.service.ReadFile;
import com.chulan.monitorservice.service.RegisterService;

public class SelectServiceController {

	public void getService(ApplicationContext context) {

		// initialize the scanner object to capture user inputs
		Scanner sc = new Scanner(System.in); 
		
		// get the initial data and identify the selected service.
		InitialData initialData = (InitialData) context.getBean("initData");
		int selectedService = initialData.init(sc);

		switch (selectedService) {
			case 0: // error occurred
			{
				System.out.println("Error occurred while identifing the selected service.");
				break;
			}
			case 1: // register a service
			{
				RegisterService registerService = (RegisterService) context.getBean("registerService"); // instantiate the ReadFile object
				registerService.registerData(sc, context);
				break;
			}
			case 2: // monitor a service
			{
				ReadFile readFile = (ReadFile)context.getBean("readFile"); // instantiate the ReadFile object with the defined beans
				List<String> servicesList = readFile.read("registered.txt",2); // read file for monitor one service
				
				//get the related service object to the input
				ServiceClass serviceClass = (ServiceClass) context.getBean("serviceClass"); // get the serviceClass object
				serviceClass = readFile.selectService(sc,servicesList,context);
				
				//instantiate and get the service monitor object
				//MonitorTheService monitorTheService = (MonitorTheService)context.getBean("monitorTheService");
				MonitorTheService monitorTheService = new MonitorTheService(serviceClass);
				
				monitorTheService.setName(serviceClass.getIp()+"-"+serviceClass.getPort()); //set the thread name
				monitorTheService.start();
				break;
			}
			case 3: // monitor multiple services
			{
				ReadFile readFile = (ReadFile)context.getBean("readFile"); // instantiate the ReadFile object with the defined beans
				List<String> servicesList = readFile.read("registered.txt",3); // read file for monitor multiple services
				
				//get the related services as a list of objects to the input
				List<ServiceClass> serviceClassList = readFile.selectMultiServices(sc,servicesList,context);
				
				//instantiate and get the service monitor object for each object in serviceClassList
				for (ServiceClass serviceClass : serviceClassList) {
					
					//creating new MonitorTheService class object because spring bean is using singleton MonitorTheService class object
					//because of that every-time the current object values are setting without creating a new object
					MonitorTheService monitorTheService = new MonitorTheService(serviceClass);
					
					monitorTheService.setName(serviceClass.getIp()+"-"+serviceClass.getPort()); //set the thread name
					monitorTheService.start();
				}
				break;
			}
		}
		
		sc.close(); // close the scanner object
	}
}
