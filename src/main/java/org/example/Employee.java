package org.example;

public class Employee {
    String firstName;
    String lastName;
    String middleName;
    String preferredName;
    int employeeId;

    public Employee(String firstName,String lastName, int employeeId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeId = employeeId;
    }

    public Employee(String firstName,String lastName,String middleName, int employeeId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.employeeId = employeeId;
    }

    public Employee(String firstName,String lastName,String middleName, String preferredName, int employeeId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.preferredName = preferredName;
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    public int getEmployeeId() {
        return employeeId;
    }
}
