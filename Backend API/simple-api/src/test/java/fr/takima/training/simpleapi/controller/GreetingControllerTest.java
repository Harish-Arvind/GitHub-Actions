package fr.takima.training.simpleapi.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GreetingControllerTest {

    @InjectMocks
    private GreetingController greetingController;

    @Test
    void testGreetingWithDefaultName() {
        var response = greetingController.greeting("World");
        assertNotNull(response);
        assertEquals("Hello, World!", response.content());
        assertTrue(response.id() > 0);
    }

    @Test
    void testGreetingWithCustomName() {
        var response = greetingController.greeting("Harish");
        assertNotNull(response);
        assertEquals("Hello, Harish!", response.content());
    }

    @Test
    void testGreetingCounterIncrements() {
        var first = greetingController.greeting("A");
        var second = greetingController.greeting("B");
        assertEquals(first.id() + 1, second.id());
    }
}
