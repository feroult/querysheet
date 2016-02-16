package pma;

import querysheet.mock.MockResultSet;

import java.util.Date;

public class AllocationMockBase extends MockResultSet {
    protected void addRow(String personId, String customer, String project, Date start, Date end, int percentage) {
        addRow();
        addDate(AllocationWeekBatch.COLUMN_NAME_START, start);
        addString(AllocationWeekBatch.COLUMN_NAME_CUSTOMER, customer);
        addString(AllocationWeekBatch.COLUMN_NAME_PROJECT, project);
        addString(AllocationWeekBatch.COLUMN_NAME_PERSON, personId);
        addDate(AllocationWeekBatch.COLUMN_NAME_END, end);
        addInt(AllocationWeekBatch.COLUMN_NAME_PERCENTAGE, percentage);
    }
}
