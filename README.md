# Employee Clothing Management System

A Java application that replaces the spreadsheet used to track employee clothing allowances and clothing inventory.

Each employee starts with a clothing allowance of $40.00. An employee can request items such as hoodies, winter jackets, T-shirts, vests, and caps. The current code is the business logic for selecting items, calculating the total, applying the remaining allowance, and recording a completed purchase. A user interface is planned later.

## Requirements

- Java 27
- Maven
- JUnit 5.10.2, already declared in `pom.xml`

## Build and test

```bash
mvn test
```

`mvn test` compiles the project and runs the unit tests. `mvn compile` compiles the main source only.

## Money

Every price, total, allowance, and payroll deduction is an integer number of cents. `$40.00` is stored as `4000`, and `$25.99` is stored as `2599`.

`Account.STANDARD_CLOTHING_ALLOWANCE_IN_CENTS` is the starting allowance. `Account.formatCentsAsDollars(int)` turns a cent amount into a dollar string such as `$25.99`. The balance cannot become negative.

## Checkout

`CheckoutService` takes the employee and the list of `CheckoutRequestItem` objects.

1. `prepareCheckout()` checks the request and the available stock, then returns a `PendingCheckout`. Preparation copies the current catalogue prices and calculates the total, the allowance used, and the payroll deduction. It does not change stock or the account balance.
2. The caller shows `pendingCheckout.getAmountToBeDeductedFromPayroll()` to the employee. A deduction greater than zero needs the employee's approval.
3. `completeCheckout(pendingCheckout, employeeApprovesPayrollDeduction)` completes that same pending checkout. The purchase keeps the captured prices even if the catalogue price changes after preparation.

Before changing anything, completion checks that the pending checkout belongs to the employee, has not already been completed, still has stock, and that the account can still cover the captured allowance. It then deducts the allowance, reduces stock, and creates a `Purchase`. If a later step fails, the stock and allowance updates from that attempt are reversed.

```java
Employee employee = new Employee("Jane", "Doe", 101);
Clothing jacket = new Clothing("Winter Jacket", "Outerwear", 3000, 2);
CheckoutService checkoutService = new CheckoutService(
        employee, List.of(new CheckoutRequestItem(jacket, 2))
);

PendingCheckout pendingCheckout = checkoutService.prepareCheckout();
int payrollDeduction = pendingCheckout.getAmountToBeDeductedFromPayroll();

boolean employeeApprovesPayrollDeduction = true;
if (payrollDeduction > 0) {
    employeeApprovesPayrollDeduction = employeeSaidYes;
}

Purchase purchase = checkoutService.completeCheckout(
        pendingCheckout, employeeApprovesPayrollDeduction
);
```

Two jackets at `$30.00` cost `6000` cents. The allowance covers `4000` cents, so the payroll deduction is `2000` cents, which is `$20.00`.

## Classes

| Class | Responsibility |
| --- | --- |
| `Employee` | The person using the system. Creates and keeps one `Account`. |
| `Account` | The clothing allowance balance, in cents. |
| `Clothing` | A catalogue item: name, type, price in cents, and stock. |
| `CheckoutRequestItem` | A requested clothing item and quantity before preparation. |
| `PendingCheckout` | The prices and amounts shown to the employee for approval. |
| `PurchaseItem` | One line with the price captured at preparation. |
| `Purchase` | A checkout that finished successfully. |
| `CheckoutService` | Validates, prepares, confirms, and records a checkout. |

## Business rules

- The allowance used cannot exceed the employee's current balance.
- The payroll deduction is the part of the total that the allowance does not cover.
- The employee must approve a payroll deduction before that purchase is completed.
- Declining the deduction, or failing a stock or balance check, leaves the stock and balance unchanged.
- Inventory cannot become negative.
- Stock is checked again at completion because it may have changed since preparation.
- A completed purchase keeps the prices from its pending checkout.
- The same pending checkout cannot be completed twice.

## Project layout

```text
src/main/java/org/example    Application classes
src/test/java/org/example    JUnit tests
pom.xml                      Maven build
```

`Main` prints the application name and an example employee last name. It does not run a checkout.
