package pma;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeallocationWeekBatch extends AllocationWeekBatch {

    private static final int COLUMN_OFFSET_TO_DATES = 1;

    private static final int CUSTOMER_COLUMN = 1;

    private static final String HEADER_CUSTOMER = "Cliente";

    private Map<Integer, List<String>> deallocations = new HashMap<>();

    private Map<String, Boolean> unallocated = new HashMap<>();

    @Override
    public void load(ResultSet rs) {
        super.load(rs);
        findDeallocations();
        mergePersonsAndCustomers();
    }

    private void findDeallocations() {
        Map<String, Integer> previousAllocation = new HashMap<>();

        for (int weekIndex = 0; weekIndex < weeks.size(); weekIndex++) {
            for (String person : persons) {
                if (unallocated.containsKey(person)) {
                    continue;
                }

                if (weekIndex == 0) {
                    previousAllocation.put(person, getAllocation(person, weekIndex));
                    continue;
                }

                if (!hasAllocationEnded(person, previousAllocation, weekIndex)) {
                    previousAllocation.put(person, getAllocation(person, weekIndex));
                    continue;
                }

                addDeallocation(weekIndex, person);
            }
        }
    }

    private void addDeallocation(int weekIndex, String person) {
        if (!deallocations.containsKey(weekIndex)) {
            deallocations.put(weekIndex, new ArrayList<String>());
        }
        deallocations.get(weekIndex).add(person);
        unallocated.put(person, Boolean.TRUE);
    }

    private boolean hasAllocationEnded(String person, Map<String, Integer> previousAllocation, int weekIndex) {
        Integer currentAllocation = getAllocation(person, weekIndex);
        return currentAllocation == 0 && previousAllocation.get(person) != 0;
    }

    private void mergePersonsAndCustomers() {

    }

    @Override
    public int rows() {
        return 0;
    }

    @Override
    public int cols() {
        return 0;
    }

    @Override
    protected String getHeader(int column) {
        if (column == CUSTOMER_COLUMN) {
            return HEADER_CUSTOMER;
        }
        if (column % 2 == 0) {
            return weeks.get(column / 2 - COLUMN_OFFSET_TO_DATES).getLabel();
        }
        return "";
    }
}
