/**
 * @author Chulan Madurapperuma
 * Detect multiple callers registering interest in the same service 
 **/
package com.chulan.monitorservice.service;

import java.util.ArrayList;
import java.util.List;

import com.chulan.monitorservice.model.ServiceClass;

public class MultipleCallersForSameService {
	
	public List<ServiceClass> detectMultiple(List<ServiceClass> svcList) {
		
		List<ServiceClass> tempSvcList = new ArrayList<ServiceClass>(svcList) ; //temporary service list
		
		for (int i = 1; i < svcList.size(); i++) { //looping the no of services to be monitored
			
			ServiceClass serviceClass = tempSvcList.get(0); //take object to be compared
			tempSvcList.remove(0); //remove the first service
			
			for (ServiceClass svc : tempSvcList) {
				
				if(svc.getIp().equals(serviceClass.getIp())&&svc.getPort()==serviceClass.getPort()) { //same service multiple registering found
					
					System.out.println("Detect multiple callers in the same service ("+ svc.getIp()+"-"+svc.getPort()+")");
					svcList =setPollFrequency(svc.getIp(),svc.getPort(),svcList); //Set the frequency
				}
			}
		}
		return svcList;
	}
	
	//Set the poll frequency in multiple callers for same service
	public List<ServiceClass> setPollFrequency(String inpIp, int inpPort,List<ServiceClass> svcList) {
		
		for (ServiceClass serviceClass : svcList) {
			
			if(serviceClass.getIp().equals(inpIp) && serviceClass.getPort()==inpPort) {
				
				//should not poll any service more frequently than once a second.
				if(serviceClass.getPollFrequency()< 1) {
					serviceClass.setPollFrequency(1);
				}
			}
		}
		return svcList;
	}
}
