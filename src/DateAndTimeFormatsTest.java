import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;


public class DateAndTimeFormatsTest {
	DateAndTimeFormats formats = new DateAndTimeFormats();
	
	@Test
	public void test() {
		String date = "21st july";
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test2() {
		String date = "24 july";
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals("Jul 24, 2014 12:00:00 AM", formats.isLazyDate(date).toLocaleString());
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test3() {
		String date = "12-3";
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals("Mar 12, 2014 12:00:00 AM", formats.isLazyDate(date).toLocaleString());
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test4() {
		String date = "32/3";
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test5() {
		String date = "18/12";
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals("Dec 18, 2014 12:00:00 AM", formats.isLazyDate(date).toLocaleString());
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test6() {
		String date = "17Mar";
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals("Mar 17, 2014 12:00:00 AM", formats.isLazyDate(date).toLocaleString());
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test7() {
		String date = "2pm";
		Calendar testTime = Calendar.getInstance();
		testTime.set(Calendar.HOUR_OF_DAY, 14);
		testTime.set(Calendar.MINUTE, 0);
		testTime.set(Calendar.SECOND, 0);
		testTime.set(Calendar.MILLISECOND, 0);
		assertEquals(testTime.getTime(), formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test8() {
		String date = "13pm";
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test9() {
		String date = "-1pm";
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test10() {
		String date = "6am";
		Calendar testTime = Calendar.getInstance();
		testTime.set(Calendar.HOUR_OF_DAY, 6);
		testTime.set(Calendar.MINUTE, 0);
		testTime.set(Calendar.SECOND, 0);
		testTime.set(Calendar.MILLISECOND, 0);
		assertEquals(testTime.getTime(), formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test11() {
		String date = "-1.30pm";
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test12() {
		String date = "11.30pm";
		Calendar testTime = Calendar.getInstance();
		testTime.set(Calendar.HOUR_OF_DAY, 23);
		testTime.set(Calendar.MINUTE, 30);
		testTime.set(Calendar.SECOND, 0);
		testTime.set(Calendar.MILLISECOND, 0);
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(testTime.getTime(), formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test13() {
		String date = "9.61pm";
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test14() {
		String date = "3:27am";
		Calendar testTime = Calendar.getInstance();
		testTime.set(Calendar.HOUR_OF_DAY, 3);
		testTime.set(Calendar.MINUTE, 27);
		testTime.set(Calendar.SECOND, 0);
		testTime.set(Calendar.MILLISECOND, 0);
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(testTime.getTime(), formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test15() {
		String date = "13:00";
		Calendar testTime = Calendar.getInstance();
		testTime.set(Calendar.HOUR_OF_DAY, 13);
		testTime.set(Calendar.MINUTE, 0);
		testTime.set(Calendar.SECOND, 0);
		testTime.set(Calendar.MILLISECOND, 0);
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(testTime.getTime(), formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test16() {
		String date = "26:00";
		Calendar testTime = Calendar.getInstance();
		testTime.set(Calendar.HOUR_OF_DAY, 13);
		testTime.set(Calendar.MINUTE, 0);
		testTime.set(Calendar.SECOND, 0);
		testTime.set(Calendar.MILLISECOND, 0);
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test17() {
		String date = "02:49";
		Calendar testTime = Calendar.getInstance();
		testTime.set(Calendar.HOUR_OF_DAY, 2);
		testTime.set(Calendar.MINUTE, 49);
		testTime.set(Calendar.SECOND, 0);
		testTime.set(Calendar.MILLISECOND, 0);
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(testTime.getTime(), formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test18() {
		String date = "23:-12";
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test19() {
		String date = "13:00 by something";
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test20() {
		String date = "13/4/13";
		Calendar testTime = Calendar.getInstance();
		testTime.set(Calendar.DATE, 13);
		testTime.set(Calendar.MONTH, 3);
		testTime.set(Calendar.YEAR, 2013);
		testTime.set(Calendar.HOUR_OF_DAY, 0);
		testTime.set(Calendar.MINUTE, 0);
		testTime.set(Calendar.SECOND, 0);
		testTime.set(Calendar.MILLISECOND, 0);
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(testTime.getTime(), formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test21() {
		String date = "9/2/19";
		Calendar testTime = Calendar.getInstance();
		testTime.set(Calendar.DATE, 9);
		testTime.set(Calendar.MONTH, 1);
		testTime.set(Calendar.YEAR, 2019);
		testTime.set(Calendar.HOUR_OF_DAY, 0);
		testTime.set(Calendar.MINUTE, 0);
		testTime.set(Calendar.SECOND, 0);
		testTime.set(Calendar.MILLISECOND, 0);
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(testTime.getTime(), formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test22() {
		String date = "-1/4/13";
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test23() {
		String date = "13/13/13";
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test24() {
		String date = "31/4/13";
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test25() {
		String date = "29/2/16";
		Calendar testTime = Calendar.getInstance();
		testTime.set(Calendar.DATE, 29);
		testTime.set(Calendar.MONTH, 1);
		testTime.set(Calendar.YEAR, 2016);
		testTime.set(Calendar.HOUR_OF_DAY, 0);
		testTime.set(Calendar.MINUTE, 0);
		testTime.set(Calendar.SECOND, 0);
		testTime.set(Calendar.MILLISECOND, 0);
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(testTime.getTime(), formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test26() {
		String date = "12/4/2013";
		Calendar testTime = Calendar.getInstance();
		testTime.set(Calendar.DATE, 12);
		testTime.set(Calendar.MONTH, 3);
		testTime.set(Calendar.YEAR, 2013);
		testTime.set(Calendar.HOUR_OF_DAY, 0);
		testTime.set(Calendar.MINUTE, 0);
		testTime.set(Calendar.SECOND, 0);
		testTime.set(Calendar.MILLISECOND, 0);
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(testTime.getTime(), formats.isLazyYearDate(date));
		assertEquals(testTime.getTime(), formats.isProperDate(date));
	}
	
	@Test
	public void test27() {
		String date = "1/16/13";
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test28() {
		String date = "46/4/13";
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
	
	@Test
	public void test29() {
		String date = "27 oct 2016";
		Calendar testTime = Calendar.getInstance();
		testTime.set(Calendar.DATE, 27);
		testTime.set(Calendar.MONTH, 9);
		testTime.set(Calendar.YEAR, 2016);
		testTime.set(Calendar.HOUR_OF_DAY, 0);
		testTime.set(Calendar.MINUTE, 0);
		testTime.set(Calendar.SECOND, 0);
		testTime.set(Calendar.MILLISECOND, 0);
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(testTime.getTime(), formats.isLazyDate(date));
		assertEquals(testTime.getTime(), formats.isLazyYearDate(date));
		assertEquals(testTime.getTime(), formats.isProperDate(date));
	}
	
	@Test
	public void test30() {
		String date = "13 mar2014";
		assertEquals(null, formats.isHourOnly(date));
		assertEquals(null, formats.isComplete12Hr(date));
		assertEquals(null, formats.isComplete24Hr(date));
		assertEquals(null, formats.isLazyDate(date));
		assertEquals(null, formats.isLazyYearDate(date));
		assertEquals(null, formats.isProperDate(date));
	}
}
