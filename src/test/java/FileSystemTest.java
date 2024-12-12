import filesystem.FileSystem;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.fail;


class FileSystemTest {

    @org.junit.jupiter.api.Test
    void read() {
        try {
            // Step 1: Initialize the file system
            FileSystem fs = new FileSystem();

            // Step 2: Create a file
            String fileName = "testFile.txt";
            int fileDescriptor = fs.create(fileName);
            assertTrue(fileDescriptor >= 0, "File descriptor should be non-negative.");

            // Step 3: Write data to the file
            String dataToWrite = "This is test data for the file system.";
            fs.write(fileDescriptor, dataToWrite);

            // Step 4: Read data from the file
            String dataRead = fs.read(fileDescriptor);

            // Step 5: Verify the data read matches the data written
            assertEquals(dataToWrite, dataRead, "Data read from the file should match the data written.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            fail("IOException occurred: " + ioe.getMessage());
        }
    }

    @Test
    void write() {
        try {
            // Step 1: Initialize the file system
            FileSystem fs = new FileSystem();

            // Step 2: Create a file
            String fileName = "testWriteFile.txt";
            int fileDescriptor = fs.create(fileName);
            assertTrue(fileDescriptor >= 0, "File descriptor should be non-negative.");

            // Step 3: Write data to the file
            String dataToWrite = "Sample data for write test.";
            fs.write(fileDescriptor, dataToWrite);

            // Step 4: Read back the data to verify the write operation
            String dataRead = fs.read(fileDescriptor);
            assertEquals(dataToWrite, dataRead, "Data read from the file should match the data written.");

            // Step 5: Attempt to write to an invalid file descriptor and expect an exception
            try {
                fs.write(-1, "This should fail.");
                fail("Expected an IOException when writing to an invalid file descriptor.");
            } catch (IOException e) {
                assertTrue(e.getMessage().contains("Invalid file descriptor"), "Error message should indicate invalid file descriptor.");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            fail("IOException occurred: " + ioe.getMessage());
        }
    }

    @Test
    void allocateBlocksForFile() {
    }

    @Test
    void deallocateBlocksForFile() {
    }

    @org.junit.jupiter.api.Test
    void create() {
    }

    @org.junit.jupiter.api.Test
    void delete() {
    }

    @org.junit.jupiter.api.Test
    void open() {
    }

    @org.junit.jupiter.api.Test
    void close() {
    }

    @org.junit.jupiter.api.Test
    void testRead() {
    }

    @org.junit.jupiter.api.Test
    void testWrite() {
    }

    @org.junit.jupiter.api.Test
    void testAllocateBlocksForFile() {
    }

    @org.junit.jupiter.api.Test
    void testDeallocateBlocksForFile() {
    }
}