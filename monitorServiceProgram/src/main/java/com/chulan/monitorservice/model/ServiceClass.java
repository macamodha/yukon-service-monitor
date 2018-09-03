/**
 * @author Chulan Madurapperuma
 * Model class of the Service Object
 **/

package com.chulan.monitorservice.model;


public class ServiceClass {

	private String ip;
	private int port;
	private int pollFrequency;
	private String outageStart;
	private String outageEnd;
	private int gracePeriod;
	private String status;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getPollFrequency() {
		return pollFrequency;
	}
	public void setPollFrequency(int pollFrequency) {
		this.pollFrequency = pollFrequency;
	}
	public String getOutageStart() {
		return outageStart;
	}
	public void setOutageStart(String outageStart) {
		this.outageStart = outageStart;
	}
	public String getOutageEnd() {
		return outageEnd;
	}
	public void setOutageEnd(String outageEnd) {
		this.outageEnd = outageEnd;
	}
	public int getGracePeriod() {
		return gracePeriod;
	}
	public void setGracePeriod(int gracePeriod) {
		this.gracePeriod = gracePeriod;
	}
	@Override
	public String toString() {
		return "ServiceClass [ip=" + ip + ", port=" + port + ", pollFrequency=" + pollFrequency + ", outageStart="
				+ outageStart + ", outageEnd=" + outageEnd + ", status=" + status + "]";
	}
	
	
	public String toSaveAsString() {
	return 	"\n"			+
			ip 				+ "-" + 
			port 			+ "-" + 
			pollFrequency 	+ "-" + 
			outageStart 	+ "-" + 
			outageEnd;
	}
}
