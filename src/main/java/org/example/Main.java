package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        System.out.println("Employee Clothing Management System");

        Employee employee1 = new Employee("Chisom", "Chiobi", 1);
        System.out.println(employee1.getLastName());
    }
}
