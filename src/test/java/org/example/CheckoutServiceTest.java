package org.example;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

class CheckoutServiceTest {

    @Test
    void constructorStoresPurchaseToBeCheckedOut() {
        Purchase purchase = new Purchase(
                new Employee("Jane", "Doe", 101),
                List.of(),
                0,
                0,
                0,
                Instant.now()
        );

        CheckoutService checkoutService = new CheckoutService(purchase);

        assertSame(purchase, checkoutService.purchaseToBeCheckedOut);
    }
}
