package filesystem;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class FileSystemTest {

    @Test
    void read() {
        try {
            FileSystem fs = new FileSystem();

            // Step 1: Create and allocate blocks
            System.out.println("Creating file...");
            int inodeNumber = fs.create("readTestFile");
            System.out.println("File created with inode: " + inodeNumber);

            System.out.println("Allocating blocks...");
            int[] actual = fs.allocateBlocksForFile(inodeNumber, 200);
            System.out.println("Allocated blocks: " + Arrays.toString(actual));

            assertNotNull(actual, "Blocks should be allocated");
            assertTrue(actual.length > 0, "Allocated block array should not be empty");

            // Step 2: Write test data
            String testData = "HelloWorld";
            System.out.println("Writing data: " + testData);
            fs.write(inodeNumber, testData);

            // Step 3: Read back the data
            System.out.println("Reading data...");
            String readData = fs.read(inodeNumber);
            System.out.println("Data read: " + readData);

            // Validate the content
            assertEquals(testData, readData, "File content does not match expected");

        } catch (IOException ioe) {
            System.out.println("Exception occurred: " + ioe.getMessage());
            fail("Test failed due to IOException: " + ioe.getMessage());
        }
    }

    @Test
    void write() {
        try {
            FileSystem fs = new FileSystem();

            // Step 1: Create a file
            System.out.println("Creating file...");
            int inodeNumber = fs.create("writeTestFile");
            System.out.println("File created with inode: " + inodeNumber);

            // Step 2: Write sample data
            String testData = "WriteTestData";
            System.out.println("Writing data: " + testData);
            fs.write(inodeNumber, testData);

            // Step 3: Read the file back to verify
            System.out.println("Reading data...");
            String readData = fs.read(inodeNumber);
            System.out.println("Data read: " + readData);

            // Validate the content
            assertEquals(testData, readData, "File content does not match after writing");

        } catch (IOException e) {
            System.out.println("Exception occurred: " + e.getMessage());
            fail("Test failed due to IOException: " + e.getMessage());
        }
    }

    @Test
    void allocateBlocksForFile() {
        try {
            FileSystem fs = new FileSystem();

            // Step 1: Create a file
            System.out.println("Creating file...");
            int inodeNumber = fs.create("allocateTestFile");
            System.out.println("File created with inode: " + inodeNumber);

            // Step 2: Allocate blocks
            System.out.println("Allocating blocks...");
            int[] allocatedBlocks = fs.allocateBlocksForFile(inodeNumber, 512);
            System.out.println("Blocks allocated: " + Arrays.toString(allocatedBlocks));

            // Verify allocation
            assertNotNull(allocatedBlocks, "Blocks should be allocated");
            assertTrue(allocatedBlocks.length > 0, "Allocated block array should not be empty");

        } catch (IOException e) {
            System.out.println("Exception occurred: " + e.getMessage());
            fail("Test failed due to IOException: " + e.getMessage());
        }
    }

    @Test
    void deallocateBlocksForFile() {
        try {
            FileSystem fs = new FileSystem();

            // Step 1: Create and allocate blocks
            String fileName = "deallocateTestFile";
            System.out.println("Creating file...");
            int inodeNumber = fs.create(fileName);
            System.out.println("File created with inode: " + inodeNumber);

            System.out.println("Allocating blocks...");
            fs.allocateBlocksForFile(inodeNumber, 1024); // Allocate 1024 bytes

            // Step 2: Deallocate the file
            System.out.println("Deallocating file...");
            fs.deallocateBlocksForFile(inodeNumber);
            System.out.println("File deallocated.");

            // Step 3: Verify inode is reset
            INode inode = fs.getDiskDevice().readInode(inodeNumber);
            assertNull(inode.getFileName(), "File name should be null after deallocation");
            assertEquals(0, inode.getSize(), "File size should be 0 after deallocation");

            // Verify all block pointers are reset
            for (int i = 0; i < INode.NUM_BLOCK_POINTERS; i++) {
                assertEquals(-1, inode.getBlockPointer(i), "Block pointer should be -1 after deallocation");
            }

            // Step 4: Verify free block list
            byte[] freeBlockList = fs.getDiskDevice().readFreeBlockList();
            for (int i = 0; i < Disk.NUM_BLOCKS; i++) {
                int byteIndex = i / 8;
                int bitIndex = i % 8;
                assertEquals(0, (freeBlockList[byteIndex] >> bitIndex) & 1, "Block should be free after deallocation");
            }

        } catch (IOException e) {
            System.out.println("Exception occurred: " + e.getMessage());
            fail("Test failed due to IOException: " + e.getMessage());
        }
    }
}
