package com.students.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class Dateconverted {

// converts 08/01/19 to 2019-08-01
	
	public static void Dateconverter(String Date) throws ParseException
    {
           Date DF = new Date();
           DateFormat FromDF = new SimpleDateFormat("MM/dd/yy");
     FromDF.setLenient(false);  // this is important!
     Date FromDate = FromDF.parse(Date);   //"08/01/19"
     String dateStringFrom = new SimpleDateFormat("yyyy-MM-dd").format(FromDate);
     System.out.println(dateStringFrom);
     //return dateStringFrom;
    }
	
	@Test
	public void test() throws ParseException {
		
		Dateconverter("08/01/19");
	}
}
