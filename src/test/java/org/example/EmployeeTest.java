package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EmployeeTest {

    @Test
    void employeeConstructorSetsCoreFields() {
        Employee employee = new Employee("Jane", "Doe", 101);

        assertEquals("Jane", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals(101, employee.getEmployeeId());
        assertNull(employee.getMiddleName());
        assertNull(employee.getPreferredName());
    }

    @Test
    void employeeConstructorWithMiddleNameSetsMiddleName() {
        Employee employee = new Employee("Jane", "Doe", "M", 101);

        assertEquals("M", employee.getMiddleName());
    }

    @Test
    void employeeConstructorWithPreferredNameSetsPreferredNameAndLastNameCanBeUpdated() {
        Employee employee = new Employee("Jane", "Doe", "M", "J", 101);

        assertEquals("J", employee.getPreferredName());

        employee.setLastName("Smith");

        assertEquals("Smith", employee.getLastName());
    }
}
