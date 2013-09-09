package pma;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class AllocationWeekBatchTest {

	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public class AllocationMockResultSet extends MockResultSet {
		public AllocationMockResultSet() throws ParseException {
			addRow("joao", dateFormat.parse("04/09/2013"), dateFormat.parse("12/09/2013"), 100);
			addRow("joao", dateFormat.parse("16/09/2013"), dateFormat.parse("10/10/2013"), 100);
			addRow("pedro", dateFormat.parse("09/09/2013"), dateFormat.parse("20/09/2013"), 100);
			addRow("pedro", dateFormat.parse("20/09/2013"), dateFormat.parse("20/09/2013"), 100);
		}

		private void addRow(String personId, Date start, Date end, int percentage) {
			addRow();
			addDate(AllocationWeekBatch.COLUMN_NAME_START, start);
			addString(AllocationWeekBatch.COLUMN_NAME_PERSON, personId);
			addDate(AllocationWeekBatch.COLUMN_NAME_END, end);
			addInt(AllocationWeekBatch.COLUMN_NAME_PERCENTAGE, percentage);
		}
	}
	
	@Test
	public void testHeader() throws ParseException {
		AllocationWeekBatch batch = new AllocationWeekBatch(new AllocationMockResultSet());

		assertEquals("colaborador", batch.getValue(1, 1));
		assertEquals("02/09 - 06/09", batch.getValue(1, 2));
		assertEquals("09/09 - 13/09", batch.getValue(1, 3));
		assertEquals("16/09 - 20/09", batch.getValue(1, 4));
		assertEquals("23/09 - 27/09", batch.getValue(1, 5));
		assertEquals("30/09 - 04/10", batch.getValue(1, 6));
		assertEquals("07/10 - 11/10", batch.getValue(1, 7));
	}

	@Test
	public void testAllocation() throws ParseException {
		AllocationWeekBatch batch = new AllocationWeekBatch(new AllocationMockResultSet());

		assertEquals(3, batch.rows());
		assertEquals(7, batch.cols());
		
		assertEquals("joao", batch.getValue(2, 1));
		assertEquals("60", batch.getValue(2, 2));
		assertEquals("80", batch.getValue(2, 3));
		assertEquals("100", batch.getValue(2, 4));
		assertEquals("100", batch.getValue(2, 5));
		assertEquals("100", batch.getValue(2, 6));		
		assertEquals("80", batch.getValue(2, 7));
				
		assertEquals("pedro", batch.getValue(3, 1));
		assertEquals("0", batch.getValue(3, 2));
		assertEquals("100", batch.getValue(3, 3));
		assertEquals("120", batch.getValue(3, 4));
		assertEquals("0", batch.getValue(3, 5));
	}	
}
