import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FileTest {

    @BeforeAll
    public static void BeforeAlTests() {
        // Everything that needs to run before all the tests begin
    }

    @BeforeEach
    public void beforeEachTest() {
        // Everything that needs to run before each test
    }

    @Test
    public void fileCreationTest() {
        File testfile = new File("testfile.txt", 1028, false);

        assertEquals("testfile.txt", testfile.getName());
        assertEquals(1028, testfile.getSize());
        assertTrue(testfile.isWritable());
    }

    @Test
    public void illegalNameTest() {
        File testfile = new File("testFile&CO");

        assertEquals("testFileCO", testfile.getName());
    }


    @Test
    public void enlargeTest() {
        File testfile = new File("testfile.txt", 1028, false);
        testfile.enlarge(20);
        assertEquals(1048,  testfile.getSize());
    }

    @Test
    public void shortenTest() {
        File testfile = new File("testfile.txt", 1028, false);
        testfile.shorten(20);
        assertEquals(1008,  testfile.getSize());
    }

    @Test
    public void testConstructorDefault() {
        File file = new File("test.txt");

        assertEquals("test.txt", file.getName());
        assertEquals(0, file.getSize());
        assertTrue(file.isWritable());
        assertNotNull(file.getCreationTime());
        assertNull(file.getModificationTime());
    }


    @Test
    public void illegalWritingTest() {
        assertThrows(IllegalStateException.class, () -> {
            File testFile = new File("mySafeFile",44,false);
            testFile.enlarge(50);
        });
    }

    // Overlapping test ???

}

