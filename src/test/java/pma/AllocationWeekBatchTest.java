package pma;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AllocationWeekBatchTest {

	public class AllocationMockResultSet extends MockResultSet {

		public AllocationMockResultSet() {
			
			
		}		
	}

	@Test
	public void testHeader() {
		AllocationWeekBatch batch = new AllocationWeekBatch(new AllocationMockResultSet());

		assertEquals("colaborador", batch.getValue(1, 1));
		assertEquals("02/09 - 06/09", batch.getValue(1, 3));
		assertEquals("09/09 - 13/09", batch.getValue(1, 3));
	}

}
