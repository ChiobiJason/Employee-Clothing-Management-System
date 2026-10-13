package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    @Test
    void mainPrintsApplicationNameAndExampleEmployeeLastName() {
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (PrintStream capturedOutput = new PrintStream(output, true, StandardCharsets.UTF_8)) {
            System.setOut(capturedOutput);

            Main.main();
        } finally {
            System.setOut(originalOutput);
        }

        assertEquals("Employee Clothing Management System" + System.lineSeparator() +
                "Chiobi" + System.lineSeparator(), output.toString(StandardCharsets.UTF_8));
    }
}
