import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


class Database {
    int id = 42;

    public boolean isAvailable() {
        // not implemented
        return false;
    }

    public void setUniqueId(int id) {
        this.id = id;
    }

    public int getUniqueId() {
        return id;
    }

    public String getDatabaseName() {
        return "SomeName";
    }
}

class Service {
    private final Database database;

    public Service(Database database) {
        this.database = database;
    }

    public boolean query(String query) {
        return database.isAvailable();
    }

    @Override
    public String toString() {
        return "Using database with id: " + String.valueOf(database.getUniqueId());
    }
}

// Tells Mockito to create the mocks based on the @Mock annotation, this requires JUnit 5
// if you use an older version of JUnit, call Mock.init() in your setup method
@ExtendWith(MockitoExtension.class)
class ServiceTest {
    // Tells Mockito to mock the databaseMock instance
    @Mock
    Database databaseMock;

    @Test
    public void testQuery()  {
        assertNotNull(databaseMock);

        // Configure the Mock to return true when its isAvailable method is called
        when(databaseMock.isAvailable()).thenReturn(true);

        // Execute some code and test it
        Service t  = new Service(databaseMock);
        boolean check = t.query("* from t");
        assertTrue(check);
    }

    @Test
    void testGetUniqueId() {
        // default return value is empty (0, "", [], null)
        assertEquals(0, databaseMock.getUniqueId());

        // define return value for method getUniqueId()
        when(databaseMock.getUniqueId()).thenReturn(42);

        Service service = new Service(databaseMock);
        // use mock in test....
        assertEquals(service.toString(), "Using database with id: 42");
    }
}