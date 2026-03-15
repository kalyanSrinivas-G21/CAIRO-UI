package com.uiframework.cairo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A minimal sanity test to verify that the JUnit 5 test runner
 * is correctly configured and executing within the Gradle build lifecycle.
 */
public class FrameworkSanityTest {

    /**
     * Proves that basic assertions run correctly on the standard JVM.
     */
    @Test
    void testFrameworkTestEnvironment() {
        // Arrange & Act
        int expected = 2;
        int actual = 1 + 1;

        // Assert
        assertEquals(expected, actual, "The JUnit 5 test environment should be able to perform basic assertions.");
    }
}