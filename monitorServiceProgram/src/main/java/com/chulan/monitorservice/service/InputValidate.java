/**
 * @author Chulan Madurapperuma
 * Contains regular expressions for the inputs and the validations for inputs
 **/
package com.chulan.monitorservice.service;

import java.util.regex.Pattern;

public class InputValidate {
	
	//IP Pattern Regular Expression
	private static final Pattern ipRegex = Pattern.compile(
	        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

	//IP validation
	public static boolean validateIp(final String ip) {
	    return ipRegex.matcher(ip).matches();
	}

	//PORT Pattern Regular Expression
	private static final Pattern portRegex = Pattern.compile(
			"^(6553[0-5]|655[0-2]\\d|65[0-4]\\d\\d|6[0-4]\\d{3}|[1-5]\\d{4}|[2-9]\\d{3}|1[1-9]\\d{2}|10[3-9]\\d|102[4-9])$");
	
	//PORT validation
	public static boolean validatePort(final String port) {
	    return portRegex.matcher(port).matches();
	}
	
	//Yes-No validation
	public static boolean validateYesNo(final String port) {
	    return Pattern.compile("^(?:y|n)$").matcher(port).matches();
	}
	
	//TIME Pattern Regular Expression
	private static final Pattern timeRegex = Pattern.compile(
			"([01]?[0-9]|2[0-3]):[0-5][0-9]");
	
	//TIME VALIDATION
	public static boolean validateTime(final String time){
		return timeRegex.matcher(time).matches();	    
	}
}
