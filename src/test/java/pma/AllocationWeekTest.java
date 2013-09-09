package pma;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

public class AllocationWeekTest {
	
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	@Test
	public void testAdjustToMonday() throws ParseException {
		assertEquals("02/09/2013", dateFormat.format(AllocationWeek.adjustToMonday(dateFormat.parse("02/09/2013"))));
		assertEquals("02/09/2013", dateFormat.format(AllocationWeek.adjustToMonday(dateFormat.parse("03/09/2013"))));
		assertEquals("02/09/2013", dateFormat.format(AllocationWeek.adjustToMonday(dateFormat.parse("04/09/2013"))));
		assertEquals("02/09/2013", dateFormat.format(AllocationWeek.adjustToMonday(dateFormat.parse("05/09/2013"))));
		assertEquals("02/09/2013", dateFormat.format(AllocationWeek.adjustToMonday(dateFormat.parse("06/09/2013"))));
		assertEquals("09/09/2013", dateFormat.format(AllocationWeek.adjustToMonday(dateFormat.parse("07/09/2013"))));
		assertEquals("09/09/2013", dateFormat.format(AllocationWeek.adjustToMonday(dateFormat.parse("08/09/2013"))));
		assertEquals("09/09/2013", dateFormat.format(AllocationWeek.adjustToMonday(dateFormat.parse("09/09/2013"))));
	}
	
	@Test
	public void testAdjustToFriday() throws ParseException {
		assertEquals("06/09/2013", dateFormat.format(AllocationWeek.adjustToFriday(dateFormat.parse("02/09/2013"))));
		assertEquals("06/09/2013", dateFormat.format(AllocationWeek.adjustToFriday(dateFormat.parse("03/09/2013"))));
		assertEquals("06/09/2013", dateFormat.format(AllocationWeek.adjustToFriday(dateFormat.parse("04/09/2013"))));
		assertEquals("06/09/2013", dateFormat.format(AllocationWeek.adjustToFriday(dateFormat.parse("05/09/2013"))));
		assertEquals("06/09/2013", dateFormat.format(AllocationWeek.adjustToFriday(dateFormat.parse("06/09/2013"))));
		assertEquals("13/09/2013", dateFormat.format(AllocationWeek.adjustToFriday(dateFormat.parse("07/09/2013"))));
		assertEquals("13/09/2013", dateFormat.format(AllocationWeek.adjustToFriday(dateFormat.parse("08/09/2013"))));
		assertEquals("13/09/2013", dateFormat.format(AllocationWeek.adjustToFriday(dateFormat.parse("09/09/2013"))));
	}

	@Test
	public void testGetOneAllocationWeek() throws ParseException {
		List<AllocationWeek> weeks = AllocationWeek.getWeeks(dateFormat.parse("04/09/2013"), dateFormat.parse("05/09/2013"), 100);
		assertEquals(1, weeks.size());
		assertEquals("02/09 - 06/09", weeks.get(0).getLabel());
	}
	
	@Test
	public void testGetVariousAllocationWeeks() throws ParseException {
		List<AllocationWeek> weeks = AllocationWeek.getWeeks(dateFormat.parse("04/09/2013"), dateFormat.parse("18/09/2013"), 100);
		assertEquals(3, weeks.size());
		assertEquals("02/09 - 06/09", weeks.get(0).getLabel());
		assertEquals("09/09 - 13/09", weeks.get(1).getLabel());
		assertEquals("16/09 - 20/09", weeks.get(2).getLabel());
		
		assertEquals("02/09/2013", dateFormat.format(weeks.get(0).getWeekStart()));
		assertEquals("04/09/2013", dateFormat.format(weeks.get(0).getAllocationStart()));
		
		assertEquals("09/09/2013", dateFormat.format(weeks.get(1).getAllocationStart()));
		assertEquals("13/09/2013", dateFormat.format(weeks.get(1).getAllocationEnd()));
				
		assertEquals("20/09/2013", dateFormat.format(weeks.get(2).getWeekEnd()));
		assertEquals("18/09/2013", dateFormat.format(weeks.get(2).getAllocationEnd()));
	}
	
	@Test
	public void testEquals() throws ParseException {		
		AllocationWeek week1 = new AllocationWeek(dateFormat.parse("02/09/2013"), dateFormat.parse("06/09/2013"), 100);
		AllocationWeek week2 = new AllocationWeek(dateFormat.parse("02/09/2013"), dateFormat.parse("06/09/2013"), 100);
		
		assertTrue(week1.equals(week2));
	}
	
	@Test
	public void testAllocation() throws ParseException {		
		assertEquals(100, new AllocationWeek(dateFormat.parse("02/09/2013"), dateFormat.parse("06/09/2013"), 100).getAllocation());
		assertEquals(50, new AllocationWeek(dateFormat.parse("02/09/2013"), dateFormat.parse("06/09/2013"), 50).getAllocation());		
		assertEquals(60, new AllocationWeek(dateFormat.parse("04/09/2013"), dateFormat.parse("06/09/2013"), 100).getAllocation());
		assertEquals(30, new AllocationWeek(dateFormat.parse("04/09/2013"), dateFormat.parse("06/09/2013"), 50).getAllocation());		
		assertEquals(20, new AllocationWeek(dateFormat.parse("06/09/2013"), dateFormat.parse("06/09/2013"), 100).getAllocation());
	}
}
