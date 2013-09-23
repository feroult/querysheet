package pma;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class AllocationWeekBatchTest {

	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	public class AllocationWeekBatchMock extends AllocationWeekBatch {

		@Override
		protected Date today() {
			try {
				return dateFormat.parse("10/09/2013");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}		
	}

	public class AllocationMockResultSet extends MockResultSet {
		public AllocationMockResultSet() throws ParseException {
			addRow("joao", "Acme", dateFormat.parse("04/09/2013"), dateFormat.parse("12/09/2013"), 100);
			addRow("joao", "Ninjas", dateFormat.parse("16/09/2013"), dateFormat.parse("10/10/2013"), 100);
			addRow("pedro", "Ninjas", dateFormat.parse("09/09/2013"), dateFormat.parse("20/09/2013"), 100);
			addRow("pedro", "Beegos", dateFormat.parse("20/09/2013"), dateFormat.parse("20/09/2013"), 100);
		}

		private void addRow(String personId, String customer, Date start, Date end, int percentage) {
			addRow();
			addDate(AllocationWeekBatch.COLUMN_NAME_START, start);
			addString(AllocationWeekBatch.COLUMN_NAME_CUSTOMER, customer);
			addString(AllocationWeekBatch.COLUMN_NAME_PERSON, personId);
			addDate(AllocationWeekBatch.COLUMN_NAME_END, end);
			addInt(AllocationWeekBatch.COLUMN_NAME_PERCENTAGE, percentage);
		}
	}
	
	@Test
	public void testHeader() throws ParseException {
		AllocationWeekBatch batch = new AllocationWeekBatchMock();
		batch.load(new AllocationMockResultSet());

		
		assertEquals("Colaborador", batch.getValue(1, 1));
		assertEquals("Cliente", batch.getValue(1, 2));
		assertEquals("02/09/2013", batch.getValue(1, 3));
		assertEquals("09/09/2013", batch.getValue(1, 4));
		assertEquals("16/09/2013", batch.getValue(1, 5));
		assertEquals("23/09/2013", batch.getValue(1, 6));
		assertEquals("30/09/2013", batch.getValue(1, 7));
		assertEquals("07/10/2013", batch.getValue(1, 8));
	}

	@Test
	public void testAllocation() throws ParseException {
		AllocationWeekBatch batch = new AllocationWeekBatchMock();
		batch.load(new AllocationMockResultSet());

		assertEquals(3, batch.rows());
		assertEquals(7, batch.cols());
		
		assertEquals("joao", batch.getValue(2, 1));
		assertEquals("Acme, Ninjas", batch.getValue(2, 2));
		assertEquals("60", batch.getValue(2, 3));
		assertEquals("80", batch.getValue(2, 4));
		assertEquals("100", batch.getValue(2, 5));
		assertEquals("100", batch.getValue(2, 6));
		assertEquals("100", batch.getValue(2, 7));		
		assertEquals("80", batch.getValue(2, 8));
				
		assertEquals("pedro", batch.getValue(3, 1));
		assertEquals("Beegos, Ninjas", batch.getValue(3, 2));
		assertEquals("0", batch.getValue(3, 3));
		assertEquals("100", batch.getValue(3, 4));
		assertEquals("120", batch.getValue(3, 5));
		assertEquals("0", batch.getValue(3, 6));
	}	

	@Test
	public void testValidStart() throws ParseException {
		AllocationWeekBatch batch = new AllocationWeekBatchMock();
		
		assertTrue(batch.validStart(dateFormat.parse("10/09/2013")));
		assertFalse(batch.validStart(dateFormat.parse("11/02/2014")));
	}
	
	@Test
	public void testAdjustStart() throws ParseException {		
		AllocationWeekBatch batch = new AllocationWeekBatchMock();
		
		assertEquals("01/09/2013", dateFormat.format(batch.adjustStart(dateFormat.parse("01/01/2013"))));		
		assertEquals("02/09/2013", dateFormat.format(batch.adjustStart(dateFormat.parse("02/09/2013"))));
		assertEquals("04/09/2013", dateFormat.format(batch.adjustStart(dateFormat.parse("04/09/2013"))));
		assertEquals("19/09/2013", dateFormat.format(batch.adjustStart(dateFormat.parse("19/09/2013"))));
	}
	
	public void testAdjustEnd() throws ParseException {		
		AllocationWeekBatch batch = new AllocationWeekBatchMock();
		
		assertEquals("13/12/2013", dateFormat.format(batch.adjustEnd(dateFormat.parse("01/01/2014"))));		
		assertEquals("11/11/2013", dateFormat.format(batch.adjustEnd(dateFormat.parse("11/11/2013"))));
	}	
	
}
