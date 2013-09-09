package pma;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;

public class AllocationWeekBatchTest {

	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public class AllocationMockResultSet extends MockResultSet {
		public AllocationMockResultSet() throws ParseException {
			addRow();
			addString(AllocationWeekBatch.COLUMN_NAME_PERSON_ID, "joao");
			addDate(AllocationWeekBatch.COLUMN_NAME_START, dateFormat.parse("04/09/2013"));
			addDate(AllocationWeekBatch.COLUMN_NAME_END, dateFormat.parse("12/09/2013"));
			addInt(AllocationWeekBatch.COLUMN_NAME_PERCENTAGE, 100);
		}
	}

	@Test
	public void testHeader() throws ParseException {
		AllocationWeekBatch batch = new AllocationWeekBatch(new AllocationMockResultSet());

		assertEquals("colaborador", batch.getValue(1, 1));
		assertEquals("02/09 - 06/09", batch.getValue(1, 2));
		assertEquals("09/09 - 13/09", batch.getValue(1, 3));
	}

}
