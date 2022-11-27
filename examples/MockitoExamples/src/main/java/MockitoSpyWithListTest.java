import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;


class MockitoSpyWithListTest {
    @Test
    void ensureSpyForListWorks() {
        var list = new ArrayList<String>();
        // wrap real object with spy method
        var spiedList = spy(list);

        // when(spy.get(99)).thenReturn("42");
        // doesn't work as it calls spy.get(99),
        // which throws IndexOutOfBoundsException (list is still empty)

        // you have to use doReturn() for stubbing
        doReturn("42").when(spiedList).get(99);
        String value = (String) spiedList.get(99);

        assertEquals("42", value);
    }
}