import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MockitoSpyTest {
    // Create new list and wrap in using @Spy
    @Spy
    List<String> spy = new LinkedList<>();

    @Test
    void testLinkedListSpyCorrect() {
        // when(spy.get(0)).thenReturn("foo");
        // doesn't work as it calls spy.get(0),
        // which throws IndexOutOfBoundsException (list is still empty)

        // you have to use doReturn() for stubbing
        doReturn("foo").when(spy).get(0);

        assertEquals("foo", spy.get(0));
    }

}