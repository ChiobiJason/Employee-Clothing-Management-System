package org.example;

/**
 * An employee who can use a clothing allowance. The employee owns one account,
 * and other classes can read that account but cannot replace it.
 */
public class Employee {
    private String firstName;
    private String lastName;
    private String middleName;
    private String preferredName;
    private int employeeId;
    private Account employeeAccount;

    /**
     * Creates an employee with a first name, last name, and id.
     * The new employee receives a standard clothing allowance account.
     *
     * @param firstName - employee first name
     * @param lastName - employee last name
     * @param employeeId - employee id
     */
    public Employee(String firstName, String lastName, int employeeId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeId = employeeId;
        this.employeeAccount = new Account();
    }

    /**
     * Creates an employee with a middle name.
     * The new employee receives a standard clothing allowance account.
     *
     * @param firstName - employee first name
     * @param lastName - employee last name
     * @param middleName - employee middle name
     * @param employeeId - employee id
     */
    public Employee(String firstName, String lastName, String middleName, int employeeId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.employeeId = employeeId;
        this.employeeAccount = new Account();
    }

    /**
     * Creates an employee with a middle name and a preferred name.
     * The new employee receives a standard clothing allowance account.
     *
     * @param firstName - employee first name
     * @param lastName - employee last name
     * @param middleName - employee middle name
     * @param preferredName - name the employee prefers to use
     * @param employeeId - employee id
     */
    public Employee(String firstName, String lastName, String middleName, String preferredName, int employeeId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.preferredName = preferredName;
        this.employeeId = employeeId;
        this.employeeAccount = new Account();
    }

    /**
     * Returns the employee first name.
     *
     * @return - employee first name
     */
    public String getFirstName() {
        return firstName;
    } //getFirstName

    /**
     * Returns the employee last name.
     *
     * @return - employee last name
     */
    public String getLastName() {
        return lastName;
    } //getLastName

    /**
     * Changes the employee last name.
     *
     * @param lastName - new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    } //setLastName

    /**
     * Returns the employee middle name.
     *
     * @return - employee middle name
     */
    public String getMiddleName() {
        return middleName;
    } //getMiddleName

    /**
     * Returns the name the employee prefers to use.
     *
     * @return - preferred name
     */
    public String getPreferredName() {
        return preferredName;
    } //getPreferredName

    /**
     * Changes the name the employee prefers to use.
     *
     * @param preferredName - new preferred name
     */
    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    } //setPreferredName

    /**
     * Returns the employee id.
     *
     * @return - employee id
     */
    public int getEmployeeId() {
        return employeeId;
    } //getEmployeeId

    /**
     * Returns the employee's clothing allowance account.
     *
     * @return - clothing allowance account
     */
    public Account getEmployeeAccount() {
        return employeeAccount;
    } //getEmployeeAccount
}
