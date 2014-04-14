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
}
