package pma;

import java.util.*;

public class DeallocationWeek {

    private final List<String> persons;

    private final Map<String, List<String>> personCustomers;

    private String weekLabel;

    private int rowOffset;

    private final List<String> customers;

    private Map<String, List<String>> deallocationByCustomer;

    private List<String> groups;

    private Map<String, List<String>> deallocationByGroup;

    public DeallocationWeek(List<String> persons, Map<String, List<String>> personCustomers, String weekLabel, int rowOffset) {
        this.persons = persons;
        this.personCustomers = personCustomers;
        this.weekLabel = weekLabel;
        this.rowOffset = rowOffset;
        this.customers = new ArrayList<>();
        this.deallocationByCustomer = new HashMap<>();
        this.groups = new ArrayList<>();
        this.deallocationByGroup = new HashMap<>();
        merge();
    }

    private void merge() {
        mergeCustomers();

        for (String customer : customers) {
            List<String> group = new ArrayList<>();
            List<String> groupPersons = mergePersons(customer, group);

            if (group.size() == 0) {
                continue;
            }

            Collections.sort(group);
            Collections.sort(groupPersons);
            String groupLabel = commaSeparated(group);
            groups.add(groupLabel);
            deallocationByGroup.put(groupLabel, groupPersons);
        }
    }

    private String commaSeparated(List<String> strings) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (String s : strings) {
            if (!first) {
                sb.append(", ");
            } else {
                first = false;
            }
            sb.append(s != null ? s.trim() : s);
        }
        return sb.toString();
    }

    private List<String> mergePersons(String customer, List<String> group) {
        if (!deallocationByCustomer.containsKey(customer)) {
            return Collections.emptyList();
        }

        group.add(customer);

        Set<String> groupPersons = new HashSet<>();
        List<String> customers = deallocationByCustomer.get(customer);
        deallocationByCustomer.remove(customer);

        for (String person : customers) {
            groupPersons.add(person);

            for (String otherCustomer : personCustomers.get(person)) {
                groupPersons.addAll(mergePersons(otherCustomer, group));
            }
        }

        return new ArrayList<>(groupPersons);
    }

    private void mergeCustomers() {
        for (String person : persons) {
            for (String customer : personCustomers.get(person)) {
                if (!deallocationByCustomer.containsKey(customer)) {
                    customers.add(customer);
                    deallocationByCustomer.put(customer, new ArrayList<String>());
                }
                deallocationByCustomer.get(customer).add(person);
            }
        }

        Collections.sort(customers);
    }

    public List<String> getCustomerGroups() {
        return groups;
    }

    public int getCustomerGroupsCount() {
        return getCustomerGroups().size();
    }

    public String getCustomerGroup(int i) {
        return groups.get(i);
    }

    public String getPersonGroup(int i) {
        List<String> persons = deallocationByGroup.get(getCustomerGroup(i));
        return commaSeparated(persons);
    }

    public int getPersonGroupCount(int i) {
        return deallocationByGroup.get(getCustomerGroup(i)).size();
    }

    public int getRowOffset() {
        return rowOffset;
    }

    public String getWeekLabel() {
        return weekLabel;
    }

}
