package pma;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

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

    public class AllocationMockResultSet extends AllocationMockBase {
        public AllocationMockResultSet() throws ParseException {
            addRow("joao", "Acme", "Proj A", dateFormat.parse("04/09/2013"), dateFormat.parse("12/09/2013"), 100);
            addRow("joao", "Ninjas", "Proj B", dateFormat.parse("16/09/2013"), dateFormat.parse("10/10/2013"), 100);
            addRow("pedro", "Ninjas", "Proj C", dateFormat.parse("09/09/2013"), dateFormat.parse("20/09/2013"), 100);
            addRow("pedro", "Beegos", "Proj D", dateFormat.parse("20/09/2013"), dateFormat.parse("20/10/2013"), 100);
            addRow("vanessa", "Beegos", "Proj D", dateFormat.parse("10/09/2013"), dateFormat.parse("17/09/2013"), 100);
            addRow("victor", "Ninjas", "Proj B", dateFormat.parse("10/09/2013"), dateFormat.parse("28/09/2013"), 100);
            addRow("zeh", "Beegos", "Proj D", dateFormat.parse("01/10/2013"), dateFormat.parse("10/10/2013"), 100);
        }
    }

    @Test
    public void testHeader() throws ParseException {
        AllocationWeekBatch batch = new AllocationWeekBatchMock();
        batch.load(new AllocationMockResultSet());

        assertBatchRow(batch, 1, "Status", "Colaborador", "Cliente", "09/09/2013", "16/09/2013", "23/09/2013", "30/09/2013", "07/10/2013");
    }

    @Test
    public void testAllocation() throws ParseException {
        AllocationWeekBatch batch = new AllocationWeekBatchMock();
        batch.load(new AllocationMockResultSet());

        assertEquals(6, batch.rows());
        assertEquals(7, batch.cols());

        assertBatchRow(batch, 2, "", "joao", "Acme, Ninjas", "80", "100", "100", "100", "80");
        assertBatchRow(batch, 3, "", "pedro", "Beegos, Ninjas", "100", "120", "100", "100", "100");
        assertBatchRow(batch, 4, "Atenção", "vanessa", "Beegos", "80", "40", "0");
        assertBatchRow(batch, 5, "", "victor", "Ninjas", "80", "100", "100", "0");
        assertBatchRow(batch, 6, "Livre", "zeh", "Beegos", "0", "0", "0", "80");
    }

    private void assertBatchRow(AllocationWeekBatch batch, int row, String... columns) {
        for (int i = 0; i < columns.length; i++) {
            assertEquals(columns[i], batch.getValue(row, i + 1));
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
