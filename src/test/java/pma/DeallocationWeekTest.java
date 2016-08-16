package pma;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DeallocationWeekTest {

    @Test
    public void testMergePersonsAndCustomers() {
        DeallocationWeek deallocationWeek = new DeallocationWeek(persons(), personCustomers(), "", 0);

        assertEquals("Acme, Hackers, Ninjas", deallocationWeek.getCustomerGroup(0));
        assertEquals(2, deallocationWeek.getPersonGroupCount(0));
        assertEquals("joao\npedro", deallocationWeek.getPersonGroup(0));

        assertEquals("Beegos", deallocationWeek.getCustomerGroup(1));
        assertEquals(2, deallocationWeek.getPersonGroupCount(1));
        assertEquals("vanessa\nzeh", deallocationWeek.getPersonGroup(1));
    }

    private Map<String, List<String>> personCustomers() {
        Map<String, List<String>> personCustomers = new HashMap<>();
        personCustomers.put("joao", new ArrayList<String>());
        personCustomers.get("joao").add("Acme");
        personCustomers.get("joao").add("Ninjas");

        personCustomers.put("pedro", new ArrayList<String>());
        personCustomers.get("pedro").add("Ninjas");
        personCustomers.get("pedro").add("Hackers");

        personCustomers.put("vanessa", new ArrayList<String>());
        personCustomers.get("vanessa").add("Beegos");

        personCustomers.put("zeh", new ArrayList<String>());
        personCustomers.get("zeh").add("Beegos");
        return personCustomers;
    }

    private List<String> persons() {
        List<String> persons = new ArrayList<>();
        persons.add("joao");
        persons.add("pedro");
        persons.add("vanessa");
        persons.add("zeh");
        return persons;
    }

}
