package filesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemTest {

    private FileSystem fs;

    @BeforeEach
    void setUp() throws IOException {
        // Initialize a new FileSystem instance before each test
        fs = new FileSystem();
    }

    @Test
    void testRead() throws IOException {
        String fileName = "testFile.txt";
        int fileDescriptor = fs.create(fileName);
        String dataToWrite = "This is test data for the file system.";
        fs.write(fileDescriptor, dataToWrite);

        String dataRead = fs.read(fileDescriptor);

        assertEquals(dataToWrite, dataRead, "Read data should match written data.");
    }

    @Test
    void testWrite() throws IOException {
        String fileName = "writeTestFile.txt";
        int fileDescriptor = fs.create(fileName);
        String dataToWrite = "Testing the write function.";
        fs.write(fileDescriptor, dataToWrite);

        String dataRead = fs.read(fileDescriptor);

        assertEquals(dataToWrite, dataRead, "Written data should match read data.");
    }

    @Test
    void testAllocateBlocksForFile() throws IOException {
        String fileName = "allocationTestFile.txt";
        int fileDescriptor = fs.create(fileName);
        String dataToWrite = "This is a test for block allocation.";
        fs.write(fileDescriptor, dataToWrite);

        String dataRead = fs.read(fileDescriptor);

        assertEquals(dataToWrite, dataRead, "Block allocation failed. Data mismatch.");
    }

    @Test
    void testDeallocateBlocksForFile() throws IOException {
        String fileName = "deallocationTestFile.txt";
        int fileDescriptor = fs.create(fileName);
        String dataToWrite = "This data will be deallocated.";
        fs.write(fileDescriptor, dataToWrite);

        // Delete the file
        fs.delete(fileName);

        // Assert that reading after deletion throws an IOException
        IOException readException = assertThrows(IOException.class, () -> fs.read(fileDescriptor));
        assertEquals("FileSystem::read: Invalid file descriptor or inode is null.", readException.getMessage());

        // Assert that writing after deletion throws an IOException
        IOException writeException = assertThrows(IOException.class, () -> fs.write(fileDescriptor, "New Data"));
        assertEquals("FileSystem::write: Invalid file descriptor or inode is null.", writeException.getMessage());
    }
}
