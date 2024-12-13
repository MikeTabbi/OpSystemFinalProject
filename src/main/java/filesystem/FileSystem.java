package filesystem;
import filesystem.Disk;
import filesystem.INode;

import java.io.IOException;

public class FileSystem {
    private Disk diskDevice;

    private int iNodeNumber;
    private int fileDescriptor;
    private INode iNodeForFile;

    public FileSystem() throws IOException {
        diskDevice = new Disk();
        diskDevice.format();
    }

    /***
     * Create a file with the name <code>fileName</code>
     *
     * @param fileName - name of the file to create
     * @throws IOException
     */
    public int create(String fileName) throws IOException {
        INode tmpINode = null;

        boolean isCreated = false;

        for (int i = 0; i < Disk.NUM_INODES && !isCreated; i++) {
            tmpINode = diskDevice.readInode(i);
            String name = tmpINode.getFileName();
            if (name != null && name.trim().equals(fileName)) {
                throw new IOException("FileSystem::create: " + fileName +
                        " already exists");
            } else if (tmpINode.getFileName() == null) {
                this.iNodeForFile = new INode();
                this.iNodeForFile.setFileName(fileName);
                this.iNodeNumber = i;
                this.fileDescriptor = i;
                isCreated = true;
            }
        }
        if (!isCreated) {
            throw new IOException("FileSystem::create: Unable to create file");
        }

        return fileDescriptor;
    }

    /**
     * Removes the file
     *
     * @param fileName
     * @throws IOException
     */
    public void delete(String fileName) throws IOException {
        INode tmpINode = null;
        boolean isFound = false;
        int inodeNumForDeletion = -1;

        for (int i = 0; i < Disk.NUM_INODES && !isFound; i++) {
            tmpINode = diskDevice.readInode(i);

            String fName = tmpINode.getFileName();

            if (fName != null && fName.trim().compareTo(fileName.trim()) == 0) {
                isFound = true;
                inodeNumForDeletion = i;
                break;
            }
        }

        if (isFound) {
            deallocateBlocksForFile(inodeNumForDeletion);
            tmpINode.setFileName(null);
            diskDevice.writeInode(tmpINode, inodeNumForDeletion);
            this.iNodeForFile = null;
            this.fileDescriptor = -1;
            this.iNodeNumber = -1;
        }
    }

    /***
     * Makes the file available for reading/writing
     *
     * @return
     * @throws IOException
     */
    public int open(String fileName) throws IOException {
        this.fileDescriptor = -1;
        this.iNodeNumber = -1;
        INode tmpINode = null;
        boolean isFound = false;
        int iNodeContainingName = -1;

        for (int i = 0; i < Disk.NUM_INODES && !isFound; i++) {
            tmpINode = diskDevice.readInode(i);
            String fName = tmpINode.getFileName();
            if (fName != null) {
                if (fName.trim().compareTo(fileName.trim()) == 0) {
                    isFound = true;
                    iNodeContainingName = i;
                    this.iNodeForFile = tmpINode;
                }
            }
        }

        if (isFound) {
            this.fileDescriptor = iNodeContainingName;
            this.iNodeNumber = fileDescriptor;
        }

        return this.fileDescriptor;
    }

    /***
     * Closes the file
     *
     * @throws IOException If disk is not accessible for writing
     */
    public void close(int fileDescriptor) throws IOException {
        if (fileDescriptor != this.iNodeNumber) {
            throw new IOException("FileSystem::close: file descriptor, " +
                    fileDescriptor + " does not match file descriptor " +
                    "of open file");
        }
        diskDevice.writeInode(this.iNodeForFile, this.iNodeNumber);
        this.iNodeForFile = null;
        this.fileDescriptor = -1;
        this.iNodeNumber = -1;
    }

    /**
     * Reads the content of a file based on its file descriptor.
     *
     * @param fileDescriptor The file descriptor of the file
     * @return The content of the file as a String
     * @throws IOException If an I/O error occurs
     */
    public String read(int fileDescriptor) throws IOException {
        INode inode = diskDevice.readInode(fileDescriptor);
        if (inode.getFileName() == null) {
            throw new IOException("File not found");
        }

        StringBuilder data = new StringBuilder();

        for (int i = 0; i < INode.NUM_BLOCK_POINTERS; i++) {
            int blockPointer = inode.getBlockPointer(i);
            if (blockPointer != -1) {
                byte[] blockData = diskDevice.readDataBlock(blockPointer);
                data.append(new String(blockData).trim());
            }
        }

        return data.toString();
    }

    /**
     * Writes data to a file based on its file descriptor.
     *
     * @param fileDescriptor The file descriptor of the file
     * @param data           The data to write to the file
     * @throws IOException If an I/O error occurs
     */
    public void write(int fileDescriptor, String data) throws IOException {
        // Implement write logic here
    }

    /**
     * Allocates blocks for a file based on its inode number and required bytes.
     *
     * @param iNodeNumber The inode number of the file
     * @param numBytes    The number of bytes to allocate
     * @return The allocated block indices
     * @throws IOException If an I/O error occurs
     */
    public int[] allocateBlocksForFile(int iNodeNumber, int numBytes) throws IOException {
        // Implement allocation logic here
        return null;
    }

    /**
     * Deallocates the blocks used by a file and updates the inode and free block list.
     *
     * @param iNodeNumber The inode number of the file whose blocks are to be deallocated
     */
    public void deallocateBlocksForFile(int iNodeNumber) {
        try {
            // Step 1: Read the inode for the file
            INode inode = diskDevice.readInode(iNodeNumber);

            // Step 2: Read the free block list from the disk
            byte[] freeBlockList = diskDevice.readFreeBlockList();

            // Step 3: Loop through all block pointers in the inode
            for (int i = 0; i < INode.NUM_BLOCK_POINTERS; i++) {
                int blockPointer = inode.getBlockPointer(i);

                if (blockPointer != -1) {
                    int byteIndex = blockPointer / 8;
                    int bitIndex = blockPointer % 8;

                    freeBlockList[byteIndex] &= ~(1 << bitIndex);

                    inode.setBlockPointer(i, -1);
                }
            }

            // Step 4: Write back the updated free block list to the disk
            diskDevice.writeFreeBlockList(freeBlockList);

            // Step 5: Update the inode to clear its file name and size
            inode.setFileName(null);
            inode.setSize(0);

            // Step 6: Write the updated inode back to the disk
            diskDevice.writeInode(inode, iNodeNumber);

        } catch (IOException e) {
            System.err.println("Error during deallocation: " + e.getMessage());
        }
    }

    public Disk getDiskDevice() {
        return this.diskDevice;
    }
}
