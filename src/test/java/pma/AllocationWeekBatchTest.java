package pma;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class AllocationWeekBatchTest {

	private static final String TODAY = "10/09/2013";

	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public class AllocationWeekBatchMock extends AllocationWeekBatch {

		@Override
		protected Date today() {
			try {
				return dateFormat.parse(TODAY);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public class AllocationMockResultSet extends MockResultSet {
		public AllocationMockResultSet() throws ParseException {
			addRow("joao", "Acme", "Proj A", dateFormat.parse("04/09/2013"), dateFormat.parse("12/09/2013"), 100);
			addRow("joao", "Ninjas", "Proj B", dateFormat.parse("16/09/2013"), dateFormat.parse("10/10/2013"), 100);
			addRow("pedro", "Ninjas", "Proj C", dateFormat.parse("09/09/2013"), dateFormat.parse("20/09/2013"), 100);
			addRow("pedro", "Beegos", "Proj D", dateFormat.parse("20/09/2013"), dateFormat.parse("20/10/2013"), 100);
			addRow("vanessa", "Beegos", "Proj D", dateFormat.parse("10/09/2013"), dateFormat.parse("17/09/2013"), 100);
			addRow("victor", "Ninjas", "Proj B", dateFormat.parse("10/09/2013"), dateFormat.parse("28/09/2013"), 100);
			addRow("zeh", "Beegos", "Proj D", dateFormat.parse("01/10/2013"), dateFormat.parse("10/10/2013"), 100);
		}

		private void addRow(String personId, String customer, String project, Date start, Date end, int percentage) {
			addRow();
			addDate(AllocationWeekBatch.COLUMN_NAME_START, start);
			addString(AllocationWeekBatch.COLUMN_NAME_CUSTOMER, customer);
			addString(AllocationWeekBatch.COLUMN_NAME_PROJECT, project);
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
		assertEquals("09/09/2013", batch.getValue(1, 3));
		assertEquals("16/09/2013", batch.getValue(1, 4));
		assertEquals("23/09/2013", batch.getValue(1, 5));
		assertEquals("30/09/2013", batch.getValue(1, 6));
		assertEquals("07/10/2013", batch.getValue(1, 7));
	}

	@Test
	public void testAllocation() throws ParseException {
		AllocationWeekBatch batch = new AllocationWeekBatchMock();
		batch.load(new AllocationMockResultSet());

		assertEquals(6, batch.rows());
		assertEquals(7, batch.cols());

		assertAllocations(batch, 2, "joao", "Acme, Ninjas", "80", "100", "100", "100", "80");
		assertAllocations(batch, 3, "pedro", "Beegos, Ninjas", "100", "120", "100", "100", "100");
		assertAllocations(batch, 4, "vanessa (?)", "Beegos", "80", "40", "0");
		assertAllocations(batch, 5, "victor", "Ninjas", "80", "100", "100", "0");
		assertAllocations(batch, 6, "zeh (!)", "Beegos", "0", "0", "0", "80");
	}

	private void assertAllocations(AllocationWeekBatch batch, int row, String person, String project, String... allocations) {
		assertEquals(person, batch.getValue(row, 1));
		assertEquals(project, batch.getValue(row, 2));
		
		for (int i = 0; i < allocations.length; i++) {
			assertEquals(allocations[i], batch.getValue(row, i+3));
		}
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

		assertEquals("09/09/2013", dateFormat.format(batch.adjustStart(dateFormat.parse("01/01/2013"))));
		assertEquals("09/09/2013", dateFormat.format(batch.adjustStart(dateFormat.parse("04/09/2013"))));
		assertEquals("10/09/2013", dateFormat.format(batch.adjustStart(dateFormat.parse("10/09/2013"))));
		assertEquals("19/09/2013", dateFormat.format(batch.adjustStart(dateFormat.parse("19/09/2013"))));
	}

	public void testAdjustEnd() throws ParseException {
		AllocationWeekBatch batch = new AllocationWeekBatchMock();

		assertEquals("13/12/2013", dateFormat.format(batch.adjustEnd(dateFormat.parse("01/01/2014"))));
		assertEquals("11/11/2013", dateFormat.format(batch.adjustEnd(dateFormat.parse("11/11/2013"))));
	}

}
