import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Holidays {

	public String getCurrentDateExculdeHolidays(int days) {
		int numberOfDaysPrior=days;
		 DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(new Date());
		 cal.add(Calendar.DATE, numberOfDaysPrior);//current date + 1
		 boolean isHoliday=getCurrentDateholiday(cal);
		 while(isHoliday){
			 numberOfDaysPrior++;
			 Calendar cal1 = Calendar.getInstance();
			 cal1.setTime(new Date());
			 cal1.add(Calendar.DATE, numberOfDaysPrior);
			 System.out.println("in"+dateFormat.format(cal1.getTime()));
			 isHoliday=getCurrentDateholiday(cal1);
			 cal=cal1;
		 }
		 String FutureDate = dateFormat.format(cal.getTime());
		 System.out.println("FutureDate :" +FutureDate);
		return FutureDate;
	}
	
	

private  boolean getCurrentDateholiday(Calendar cal) {
	boolean isHoliday=false;
	if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
		isHoliday=true;
	}else if (cal.get(Calendar.MONTH) == Calendar.JANUARY
		&& cal.get(Calendar.DAY_OF_MONTH) == 1) {
		isHoliday=true;
	}
	
	// check if Christmas
	else if (cal.get(Calendar.MONTH) == Calendar.DECEMBER
		&& cal.get(Calendar.DAY_OF_MONTH) == 25) {
		isHoliday=true;
	}
	
	// check if 4th of July
	else if (cal.get(Calendar.MONTH) == Calendar.JULY
		&& cal.get(Calendar.DAY_OF_MONTH) == 4) {
		isHoliday=true;
	}
	
	// check Thanksgiving (4th Thursday of November)
	else if (cal.get(Calendar.MONTH) == Calendar.NOVEMBER
		&& cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 4
		&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
		isHoliday=true;
	}
	
	// check Memorial Day (last Monday of May)
	else if (cal.get(Calendar.MONTH) == Calendar.MAY
		&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
		&& cal.get(Calendar.DAY_OF_MONTH) > (31 - 7) ) {
		isHoliday=true;
	}
	
	// check Labor Day (1st Monday of September)
	else if (cal.get(Calendar.MONTH) == Calendar.SEPTEMBER
		&& cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 1
		&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
		isHoliday=true;
	}
	
	// check President's Day (3rd Monday of February)
	else if (cal.get(Calendar.MONTH) == Calendar.FEBRUARY
	&& cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 3
	&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
		isHoliday=true;
	}
	
	// check Veterans Day (November 11)
	else if (cal.get(Calendar.MONTH) == Calendar.NOVEMBER
	&& cal.get(Calendar.DAY_OF_MONTH) == 11) {
		isHoliday=true;
	}
	
	// check MLK Day (3rd Monday of January)
	else if(cal.get(Calendar.MONTH) == Calendar.JANUARY
	&& cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 3
	&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
		isHoliday=true;
	}
	
	
	return isHoliday;
}



}
