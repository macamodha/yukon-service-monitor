/**
 * @author Chulan Madurapperuma
 * Monitor a Service
 **/
package com.chulan.monitorservice.service;

import java.net.Socket;
import java.util.Calendar;

import com.chulan.monitorservice.model.ServiceClass;

public class MonitorTheService extends Thread {

	//Service object 
	private ServiceClass service;
	
	private int outageMinutesLeft;
	
	
	
	// constructor to get the service object as a parameter
	public MonitorTheService(ServiceClass service) { 
		this.service = 	service;	
	}
	
	// TO CHECK WHETHER THE SERVICE IS UP OR NOT
	public boolean isServiceConnected(){
		try{
			Socket socket = new Socket(service.getIp(),service.getPort());
			boolean outputStat = socket.isConnected();
			socket.close();
			
			return outputStat;
		} 
		catch (Exception ex) {
			return false;
		} 
	}
	
	//TO CHECK WHETHER TIME IS IN THE OUTAGE
	public boolean appliedOutage() {
		
		// Get calendar set to the current date and time
		Calendar startOutage = Calendar.getInstance();
		 
		String [] st = service.getOutageStart().split(":");
		// Set Start outage time 
		startOutage.set(Calendar.HOUR_OF_DAY, Integer.valueOf(st[0]));
		startOutage.set(Calendar.MINUTE, Integer.valueOf(st[1]));
		
		Calendar endOutage = Calendar.getInstance();
		 
		String [] et = service.getOutageEnd().split(":");
		// Set End outage time 
		endOutage.set(Calendar.HOUR_OF_DAY, Integer.valueOf(et[0]));
		endOutage.set(Calendar.MINUTE, Integer.valueOf(et[1]));
		
		// Check if current time is in the outage time
		Calendar now = Calendar.getInstance();
		boolean isOutageStarted = now.after(startOutage);
		boolean isOutageEnded 	= now.after(endOutage);
		
		if(isOutageStarted==true && isOutageEnded==false) {
			
			//calculate the minute that outage left
			int he=Integer.valueOf(et[0]);
			int hs=Integer.valueOf(now.get(Calendar.HOUR_OF_DAY));
			int me=Integer.valueOf(et[1]);
			int ms=Integer.valueOf(now.get(Calendar.MINUTE));
			
			outageMinutesLeft = (he-hs) *60;
			if(me < ms) {
				outageMinutesLeft -=60;
				me+=60;
			}
			
			outageMinutesLeft +=(me-ms);
			
			return true;
		}
		return false;
	}
	
	public void run() {
		//TO RUN IN THE POLLING FREQUENCY
		while (true) {
			if((service.getOutageStart().equals("0") && service.getOutageEnd().equals("0")) ||appliedOutage()==false) {
				// For Services with no OUTAGES
				if(isServiceConnected()==true) {
					System.out.println("Service is Up -> "+ service.getIp() +"-"+ service.getPort());
				}
				else {
					if(service.getGracePeriod()>0) {
						try {
							Thread.sleep(1000*service.getGracePeriod());
							if(isServiceConnected()==true) {
								//requirement is to send no notification if the service goes up after the grace period
								//System.out.println("Service is Up -> "+ service.getIp() +"-"+ service.getPort());
							}
							else {
								System.out.println("Service is not Up -> "+ service.getIp() +"-"+ service.getPort());
							}
						} catch (InterruptedException ex) {
							System.out.println("Exception Occured - ");
							ex.printStackTrace();
						}
					}
				}
			}
			else {
				// For Services with OUTAGES
				try {
					Thread.sleep(1000*outageMinutesLeft*60); //milliseconds to thread to sleep
					
					if(isServiceConnected()==true) {
						System.out.println("Service is Up -> "+ service.getIp() +"-"+ service.getPort());
					}
					else {
						if(service.getGracePeriod()>0) {
							try {
								Thread.sleep(1000*service.getGracePeriod());
								if(isServiceConnected()==false) {
									System.out.println("Service is not Up -> "+ service.getIp() +"-"+ service.getPort());
								}
							} catch (InterruptedException ex) {
								System.out.println("Exception Occured - ");
								ex.printStackTrace();
							}
						}
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//to check for the service after poll frequency
			try {
				Thread.sleep(service.getPollFrequency()*1000); //polling frequency 
			} 
			catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	
	
}
