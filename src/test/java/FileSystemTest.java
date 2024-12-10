import filesystem.FileSystem;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.fail;


class FileSystemTest {

    @Test
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

    @Test
    void write() {
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