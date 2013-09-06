package pma;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

public class WeekUtilsTest {
	
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	@Test
	public void testAdjustToMonday() throws ParseException {
		assertEquals("02/09/2013", dateFormat.format(WeekUtils.adjustToMonday(dateFormat.parse("02/09/2013"))));
		assertEquals("02/09/2013", dateFormat.format(WeekUtils.adjustToMonday(dateFormat.parse("03/09/2013"))));
		assertEquals("02/09/2013", dateFormat.format(WeekUtils.adjustToMonday(dateFormat.parse("04/09/2013"))));
		assertEquals("02/09/2013", dateFormat.format(WeekUtils.adjustToMonday(dateFormat.parse("05/09/2013"))));
		assertEquals("02/09/2013", dateFormat.format(WeekUtils.adjustToMonday(dateFormat.parse("06/09/2013"))));
		assertEquals("09/09/2013", dateFormat.format(WeekUtils.adjustToMonday(dateFormat.parse("07/09/2013"))));
		assertEquals("09/09/2013", dateFormat.format(WeekUtils.adjustToMonday(dateFormat.parse("08/09/2013"))));
		assertEquals("09/09/2013", dateFormat.format(WeekUtils.adjustToMonday(dateFormat.parse("09/09/2013"))));
	}
	
	@Test
	public void testAdjustToFriday() throws ParseException {
		assertEquals("06/09/2013", dateFormat.format(WeekUtils.adjustToFriday(dateFormat.parse("02/09/2013"))));
		assertEquals("06/09/2013", dateFormat.format(WeekUtils.adjustToFriday(dateFormat.parse("03/09/2013"))));
		assertEquals("06/09/2013", dateFormat.format(WeekUtils.adjustToFriday(dateFormat.parse("04/09/2013"))));
		assertEquals("06/09/2013", dateFormat.format(WeekUtils.adjustToFriday(dateFormat.parse("05/09/2013"))));
		assertEquals("06/09/2013", dateFormat.format(WeekUtils.adjustToFriday(dateFormat.parse("06/09/2013"))));
		assertEquals("13/09/2013", dateFormat.format(WeekUtils.adjustToFriday(dateFormat.parse("07/09/2013"))));
		assertEquals("13/09/2013", dateFormat.format(WeekUtils.adjustToFriday(dateFormat.parse("08/09/2013"))));
		assertEquals("13/09/2013", dateFormat.format(WeekUtils.adjustToFriday(dateFormat.parse("09/09/2013"))));
	}

	@Test
	public void testGetWeeksSameWeek() throws ParseException {
		List<Week> weeks = WeekUtils.getWeeks(dateFormat.parse("04/09/2013"), dateFormat.parse("05/09/2013"));
		assertEquals(1, weeks.size());
		assertEquals("02/09 - 06/09", weeks.get(0).getWeekLabel());
	}
	
	@Test
	public void testGetWeeks() throws ParseException {
		List<Week> weeks = WeekUtils.getWeeks(dateFormat.parse("04/09/2013"), dateFormat.parse("18/09/2013"));
		assertEquals(3, weeks.size());
		assertEquals("02/09 - 06/09", weeks.get(0).getWeekLabel());
		assertEquals("09/09 - 13/09", weeks.get(1).getWeekLabel());
		assertEquals("16/09 - 20/09", weeks.get(2).getWeekLabel());
		
		assertEquals("04/09/2013", dateFormat.format(weeks.get(0).getStart()));
		assertEquals("18/09/2013", dateFormat.format(weeks.get(2).getEnd()));
	}
}
