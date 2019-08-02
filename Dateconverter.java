package com.students.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

//Use below method to convert date to any format by modifying highlighted one.

//08/01/19 to 2019-08-01


//Dateconverter("08/01/19");

public String Dateconverter(String Date) throws ParseException
       {
              Date DF = new Date();
              DateFormat FromDF = new SimpleDateFormat("MM/dd/yy"); //format change
        FromDF.setLenient(false);  // this is important!
        Date FromDate = FromDF.parse(Date);   //"08/01/19"
        String dateStringFrom = new SimpleDateFormat("yyyy-MM-dd").format(FromDate); //format change
        System.out.println(dateStringFrom);
        return dateStringFrom;
       
O/p:

2019-08-01


	
	@Test
	public void test() throws ParseException {
		
		Dateconverter("08/01/19");
	}
}
