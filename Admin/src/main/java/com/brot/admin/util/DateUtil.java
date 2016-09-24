package com.brot.admin.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class DateUtil {
	
    public static String UTC_DATE_FORMAT_PATTERN = "^\\d{4}-[0-1]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d:[0-6]\\dZ$";
    
	private static ThreadLocalDateFormat tlDateFormat = new ThreadLocalDateFormat();
	private static ThreadLocalDatePattern tlDatePattern = new ThreadLocalDatePattern();
	private static ThreadLocalDDMMMYY tlDDMMMYY = new ThreadLocalDDMMMYY();
	private static ThreadLocalDDMMMYYYY tlDDMMMYYYY = new ThreadLocalDDMMMYYYY();
	

	public static String[] MMM = {"", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

	public static String[] DD = {"", "01", "02", "03", "04", "05", "06", "07", "08", "09",
								 "10","11","12","13","14","15","16","17","18","19","20",
								 "21","22","23","24","25","26","27","28","29","30","31"};
	
	//--------------------------------------------------------------------------//


	/**
	 * Formats given date value into UTC format ex: 1997-07-16T19:20Z
	 * 
	 * @param date
	 * @return
	 */
	public static String date2UTC(Date date) {
		return getThreadLocalDateFormat().format(date);
	}
	
	/**
	 * Returns date object for the given value in UTC format.
	 * 
	 * @param utc string in format ex: 1997-07-16T19:20+01:00
	 * @return parsed date object
	 * @throws ParseException 
	 */
	public static Date utc2Date(String utc) throws ParseException {
		return getThreadLocalDateFormat().parse(utc);
	}
	
	/**
	 * Returns date object for the given value in UTC format.
	 * 
	 * @param utc string in format ex: 1997-07-16T19:20+01:00
	 * @return Returns parsed date, returns given value for param:'otherwise' if could not be parsed.  
	 * @throws ParseException 
	 */
	public static Date utc2Date(String utc, Date otheriwse) {
		try {
			return getThreadLocalDateFormat().parse(utc);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return otheriwse;
	}
	
	public static String utc2DDMMMYY(String utc) throws ParseException {
		Date date = utc2Date(utc);
		return getThreadLocalDDMMMYY().format(date);
	}
	
   public static String date2DDMMMYY(Date date) throws ParseException {
        return getThreadLocalDDMMMYY().format(date);
    }
   
   public static String date2DDMMMYYYY(Date date) throws ParseException {
       return getThreadLocalDDMMMYYYY().format(date);
   }


	
	
	//--------------------------------------------------------------------------//

	
	/**
	 * Returns Month Name for the given Month Number {1=JAN, 2=FEB, 3=MAR.....12=DEC}
	 * @param mm
	 * @return
	 */
	public static String mm2MMM(int mm) {
		return MMM[mm];
	}
	
	/**
	 * Return double digit number for the month number.
	 * 
	 * @param dd
	 * @return
	 */
	public static String dd2DD(int dd) {
		return DD[dd];
	}
	
	/**
	 * Returns Double Digit year for the given year
	 * @param yyyy
	 * @return
	 */
	public static String yyyy2YY(int yyyy) {
		if (yyyy > 1000) {
			return String.valueOf(yyyy%100);
		} else if(yyyy > 100) {
			return String.valueOf(yyyy%10);			
		} else {
			return String.valueOf(yyyy);
		}
	}
	
	//--------------------------------------------------------------------------//

	
	/**
	 * Return a DateFormat local to the current thread.
	 * 
	 * @return
	 */
	public static DateFormat getThreadLocalDateFormat() {
		return tlDateFormat.get();
	}	

	/**
	 * Thread Local Simple Date Format Factory.
	 * 
	 * @author msathaia
	 *
	 */
	private static class ThreadLocalDateFormat extends ThreadLocal<DateFormat> {
		DateFormat proto;

		public ThreadLocalDateFormat() {
			super();
			// 2007-04-26T08:05:04Z
			SimpleDateFormat tmp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			proto = tmp;
		}
		



		@Override
		protected DateFormat initialValue() {
			return (DateFormat) proto.clone();
		}
	}
	
	//--------------------------------------------------------------------------//
	
	/**
	 * Return a Pattern local to the current thread.
	 * 
	 * @return
	 */
	public static Pattern getThreadLocalDatePattern() {
		return tlDatePattern.get();
	}
	

	/**
	 * Thread Local Simple Date Pattern Factory.
	 * 
	 * @author msathaia
	 *
	 */
	private static class ThreadLocalDatePattern extends ThreadLocal<Pattern> {
		Pattern proto;

		public ThreadLocalDatePattern() {
			super();
		}

		@Override
		protected Pattern initialValue() {
			return  Pattern.compile(UTC_DATE_FORMAT_PATTERN);
		}
	}

	//--------------------------------------------------------------------------//
	
	/**
	 * Return a DateFormat local to the current thread.
	 * 
	 * @return
	 */
	public static DateFormat getThreadLocalDDMMMYY() {
		return tlDDMMMYY.get();
	}	
	
   public static DateFormat getThreadLocalDDMMMYYYY() {
        return tlDDMMMYYYY.get();
    }   


	/**
	 * Thread Local Simple Date Format Factory.
	 * 
	 * @author msathaia
	 *
	 */
	private static class ThreadLocalDDMMMYY extends ThreadLocal<DateFormat> {
		DateFormat proto;

		public ThreadLocalDDMMMYY() {
			super();
			SimpleDateFormat tmp = new SimpleDateFormat("dd-MMM-yy");
			proto = tmp;
		}

		@Override
		protected DateFormat initialValue() {
			return (DateFormat) proto.clone();
		}
	}
	
    private static class ThreadLocalDDMMMYYYY extends ThreadLocal<DateFormat> {
        DateFormat proto;

        public ThreadLocalDDMMMYYYY() {
            super();
            SimpleDateFormat tmp = new SimpleDateFormat("dd-MMM-yyyy");
            proto = tmp;
        }

        @Override
        protected DateFormat initialValue() {
            return (DateFormat) proto.clone();
        }
    }

	
}


