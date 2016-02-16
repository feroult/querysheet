package pma;

import org.junit.Ignore;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DeallocationWeekBatchTest {

    private static final String TODAY = "16/02/2016";

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
            addRow("joao", "Acme", "Proj A", dateFormat.parse("01/01/2016"), dateFormat.parse("20/02/2016"), 100);
            addRow("joao", "Ninjas", "Proj B", dateFormat.parse("01/01/2016"), dateFormat.parse("10/03/2016"), 100);
            addRow("pedro", "Ninjas", "Proj C", dateFormat.parse("09/09/2013"), dateFormat.parse("10/03/2016"), 100);
            addRow("pedro", "Beegos", "Proj D", dateFormat.parse("01/01/2016"), dateFormat.parse("20/10/2016"), 100);
            addRow("vanessa", "Beegos", "Proj D", dateFormat.parse("01/01/2016"), dateFormat.parse("20/10/2016"), 100);
            addRow("victor", "Ninjas", "Proj B", dateFormat.parse("01/01/2016"), dateFormat.parse("20/10/2016"), 100);
            addRow("zeh", "Beegos", "Proj D", dateFormat.parse("01/01/2016"), dateFormat.parse("20/10/2016"), 100);
        }
    }

    @Test
    public void testHeader() throws ParseException {
        DeallocationWeekBatch batch = new DeallocationWeekBatch();
        batch.load(new AllocationMockResultSet());

        assertBatchRow(batch, 1, "Cliente", "15/02/2016", "", "22/02/2016", "", "29/02/2016", "", "07/03/2016", "");
    }

    @Test
    @Ignore
    public void testDeallocationWeeks() throws ParseException {
        DeallocationWeekBatch batch = new DeallocationWeekBatch();
        batch.load(new AllocationMockResultSet());

        assertEquals(3, batch.rows());
        //assertEquals(7, batch.cols());

        assertBatchRow(batch, 2, "Acme", "1", "joao", "", "");
    }

    private void assertBatchRow(DeallocationWeekBatch batch, int row, String... columns) {
        for (int i = 0; i < columns.length; i++) {
            assertEquals(columns[i], batch.getValue(row, i + 1));
        }
    }


}
