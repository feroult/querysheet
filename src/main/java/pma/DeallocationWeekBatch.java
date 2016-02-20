package pma;

import org.apache.commons.lang3.StringUtils;

import java.sql.ResultSet;
import java.util.*;

public class DeallocationWeekBatch extends AllocationWeekBatch {

    private static final int COLUMN_OFFSET_TO_DATES = 1;

    private static final int ROW_OFFSET_TO_CUSTOMERS = 3;

    private static final int CUSTOMER_COLUMN = 1;

    private static final int TODAY_COLUMN = 2;

    private static final int FREE_ROW = 2;

    private static final String HEADER_CUSTOMER = "Cliente";

    private Map<Integer, List<String>> deallocations = new TreeMap<>();

    private Map<String, Boolean> unallocated = new HashMap<>();

    private List<DeallocationWeek> deallocationWeeks = new ArrayList<>();

    private List<String> customerGroups = new ArrayList<>();

    private List<String> freeToday = new ArrayList<>();

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
                    Integer allocation = getAllocation(person, weekIndex);

                    if (allocation.equals(0)) {
                        unallocated.put(person, Boolean.TRUE);
                        freeToday.add(person.trim());
                    }

                    previousAllocation.put(person, allocation);
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
        int rowOffset = 0;
        for (Integer weekIndex : deallocations.keySet()) {
            String weekLabel = weeks.get(weekIndex).getLabel();
            DeallocationWeek deallocationWeek = new DeallocationWeek(deallocations.get(weekIndex), personCustomers, weekLabel, rowOffset);
            deallocationWeeks.add(deallocationWeek);
            customerGroups.addAll(deallocationWeek.getCustomerGroups());
            rowOffset += deallocationWeek.getCustomerGroupsCount();
        }
    }

    @Override
    public int rows() {
        return customerGroups.size() + 2;
    }

    @Override
    public int cols() {
        return deallocationWeeks.size() + 2;
    }

    @Override
    protected String getHeader(int column) {
        if (column == CUSTOMER_COLUMN) {
            return HEADER_CUSTOMER;
        }

        if (column == TODAY_COLUMN) {
            return String.format("Hoje (%d)", freeToday.size());
        }

        int weekIndex = column - 3;
        DeallocationWeek deallocationWeek = deallocationWeeks.get(weekIndex);

        return String.format("%s (%d)", deallocationWeek.getWeekLabel(), deallocationWeek.getTotalPersonCount());
    }

    @Override
    protected String getValueInTable(int row, int column) {
        if (row == FREE_ROW) {
            if (column == CUSTOMER_COLUMN) {
                return "Livre";
            }

            if (column == TODAY_COLUMN) {
                return StringUtils.join(freeToday, "\n");
            }

            return "";
        }

        if (column == TODAY_COLUMN) {
            return "";
        }

        if (column == CUSTOMER_COLUMN) {
            return customerGroups.get(row - ROW_OFFSET_TO_CUSTOMERS);
        }

        int weekIndex = column - 3;
        DeallocationWeek deallocationWeek = deallocationWeeks.get(weekIndex);

        int groupIndex = row - deallocationWeek.getRowOffset() - ROW_OFFSET_TO_CUSTOMERS;

        if (row - 3 < deallocationWeek.getRowOffset() || groupIndex >= deallocationWeek.getCustomerGroupsCount()) {
            return "";
        }

        return deallocationWeek.getPersonGroup(groupIndex);
    }
}
