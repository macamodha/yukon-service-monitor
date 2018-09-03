/**
 * @author Chulan Madurapperuma
 * Main Class which is to be run 
 **/

package com.chulan.monitorservice;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.chulan.monitorservice.controller.SelectServiceController;

public class App {

	public static void main(String[] args) {
		
		ApplicationContext context = new FileSystemXmlApplicationContext("beans.xml"); //initializing the bean xml
		
		//initialize the controller
		SelectServiceController selectServiceController = (SelectServiceController)context.getBean("selectServiceController");
		
		selectServiceController.getService(context); // get the user selected service
		
		((FileSystemXmlApplicationContext)context).close(); //close the bean
		
	}

}
