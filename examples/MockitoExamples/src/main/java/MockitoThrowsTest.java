import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class MockitoThrowsTest {
    // demonstrates mock(), anyString(), thenThrow()
    @Test
    void testMockitoThrows() {
        // using the static mock() method
        Properties properties = Mockito.mock(Properties.class);

        when(properties.get(Mockito.anyString())).thenThrow(new IllegalArgumentException("Stuff"));

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> properties.get("A"));
        assertEquals("Stuff", exception.getMessage());
    }
}
