package com.revature.util;

/**
 * The Masker class is solely responsible for masking the sensitive data.
 *
 * @author Evan Diamond
 * @version 1.0
 * @since 2018-11-30
 */
public class Masker {
	/***
	* This static method masks the true nature of the data.
	* 
	* @param String This receives an unmasked value.
	* @return String This returns a masked version of the unmasked value.
	*/
	public static String mask(String unmasked){
		switch(unmasked){
    	case "Employed": return "Passed";
    	case "Dropped": return "Failed";
    	case "Training": return "Other";
    	case "Marketing": return "Store";
    	default: return unmasked;
		}
	}
}
