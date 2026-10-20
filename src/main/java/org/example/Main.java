package org.example;

/**
 * Starts the Employee Clothing Management System and prints an example employee.
 */
public class Main {
    /**
     * Prints the application name and an example employee last name.
     */
    static void main() {
        System.out.println("Employee Clothing Management System");

        Employee employee1 = new Employee("Chisom", "Chiobi", 1);
        System.out.println(employee1.getLastName());
    } //main
}
