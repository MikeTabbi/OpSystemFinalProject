package filesystem;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemTest {

    @Test
    void read() {
        try {
            FileSystem fs = new FileSystem();
            int[] expected = {1, 2, 3, 4};
            int[] actual = fs.allocateBlocksForFile(23, 200);
            assertEquals(expected, actual);
        } catch (IOException ioe){
            System.out.println(ioe);
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
}