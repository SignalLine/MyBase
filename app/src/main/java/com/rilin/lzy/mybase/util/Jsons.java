package com.rilin.lzy.mybase.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Jsons {

	 public static String hashMapToJson(HashMap map) {  
	        String string = "{";  
	        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {  
	            Entry e = (Entry) it.next();  
	            string += "'" + e.getKey() + "':";  
	            string += "'" + e.getValue() + "',";  
	        }  
	        string = string.substring(0, string.lastIndexOf(","));  
	        string += "}";  
	        return string;  
	    }  
	 
	 
	 public static String getDateToString(long time) {
	        Date d = new Date(time*1000);
	        SimpleDateFormat sf  = new SimpleDateFormat("yyyy年MM月dd日");
	        String str=sf.format(d);
	        return str;
	    }
}
