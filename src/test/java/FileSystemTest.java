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

            FileSystem fs = new FileSystem();
            String fileName = "testFile.txt";
            int fileDescriptor = fs.create(fileName);
            String dataToWrite = "This is test data for the file system.";
            fs.write(fileDescriptor, dataToWrite);
            String dataRead = fs.read(fileDescriptor);
            assertEquals(dataToWrite, dataRead);

        } catch (IOException ioe) {
            ioe.printStackTrace();
            fail("IOException occurred: " + ioe.getMessage());
        }
    }

    @org.junit.jupiter.api.Test
    void write() {
        try {
            FileSystem fs = new FileSystem();
            String fileName = "writeTestFile.txt";
            int fileDescriptor = fs.create(fileName);
            String dataToWrite = "Testing the write function.";
            fs.write(fileDescriptor, dataToWrite);
            String dataRead = fs.read(fileDescriptor);
            assertEquals(dataToWrite, dataRead, "Testing the write function.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            fail("IOException occurred: " + ioe.getMessage());
        }
    }

    @org.junit.jupiter.api.Test
    void testAllocateBlocksForFile() {
        try {
            FileSystem fs = new FileSystem();
            String fileName = "allocationTestFile.txt";
            int fileDescriptor = fs.create(fileName);
            String dataToWrite = "This is a test for block allocation.";
            fs.write(fileDescriptor, dataToWrite);
            String dataRead = fs.read(fileDescriptor);
            assertEquals(dataToWrite, dataRead, "This is a test for block allocation.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            fail("IOException occurred: " + ioe.getMessage());
        }
    }

    @org.junit.jupiter.api.Test
    void testDeallocateBlocksForFile() {
        try {
            FileSystem fs = new FileSystem();
            String fileName = "deallocationTestFile.txt";
            int fileDescriptor = fs.create(fileName);
            String dataToWrite = "This data will be deallocated.";
            fs.write(fileDescriptor, dataToWrite);

            // Deallocate the file and try to read/write again
            fs.delete(fileName);

            IOException exception = assertThrows(IOException.class, () -> fs.read(fileDescriptor));
            assertEquals("FileSystem::read: Invalid file descriptor or inode is null.", exception.getMessage());

            exception = assertThrows(IOException.class, () -> fs.write(fileDescriptor, "New Data"));
            assertEquals("FileSystem::write: Invalid file descriptor or inode is null.", exception.getMessage());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            fail("IOException occurred: " + ioe.getMessage());
        }
    }
}
