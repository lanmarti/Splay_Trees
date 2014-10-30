package semisplay;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Read the input files which were generated by the programs on Minerva.
 * Those files contain a list of records.
 * Each record consists of: a command character + an integer number.
 * A command character can either be:
 * 't' (toevoegen): add the number to the tree.
 * 'z' (zoeken): do a lookup for the number in the tree.
 * 'v' (verwijderen): remove the number from the tree.
 */
public class NumberReader {

    /** The input stream */
    private DataInputStream in = null;
    /** Whether we have more input. */
    private boolean haveMore = false;
    /** The next input byte. */
    private byte type;
    /** The next input number. */
    private int number;
    /** The number of bytes in this file. */
    private long fileSize;

    /**
     * Create a new NumberReader
     * @param fileName The file name of the file that will be read
     */
    public NumberReader(String fileName) {
        try {
            File file = new File(fileName);
            fileSize = file.length();
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);
            in = dis;
        } catch (FileNotFoundException e) {
            System.err.println("Number file not found: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * @return Filesize in bytes of the current file.
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * Please, close this file when done!
     */
    public void close() {
        try {
            in.close();
        } catch (IOException e) {
            System.err.println("Closing number file: " + e.getMessage());
        }
    }

    /**
     * @return Whether file contains numbers that are still unread.
     */
    public boolean hasNext() {
        if ( ! haveMore) {
            try {
                type = in.readByte();
                number = in.readInt();
                haveMore = true;
            } catch (IOException ex) {
            }
        }
        return haveMore;
    }

    /**
     * returns the type of the current number without reading it. This is
     * 't' for numbers that should be added to the trees,
     * 'z' for numbers that should be searched for, and
     * 'v' for numbers to be removed from the tree.
     *
     * @return 't' for 'toevoegen', 'z' for 'zoeken' or 'v' for 'verwijderen'.
     */
    public byte getType() {
        return type;
    }

    /**
     * @return the next number in the file, or exit noisily
     * if no such numbers exists.
     */
    public int next() {
        if (haveMore) {
            haveMore = false;
        } else {
            System.err.printf("%s.next without hasNext\n", getClass().getName());
            System.exit(1);
        }
        return number;
    }

    /**
     * Read an entire input file and store the input data in two lists.
     *
     * @param fileName The file name of the input data
     * @param addList An empty list that will be filled with integers that need
     * to be added to the binary search trees
     * @param getList An empty list that will be filled with integers that
     * need to be searched in the binary search trees
     */
    public static void readFile(String fileName,
            ArrayList<Integer> addList, ArrayList<Integer> getList) {
        final NumberReader nr = new NumberReader(fileName);
        try {
            final int maxNum = (int) (nr.getFileSize() / 5);
            addList.ensureCapacity(maxNum);
            getList.ensureCapacity(maxNum);
            int maxErr = 3;
            for (int i = 0; i < maxNum && nr.hasNext(); ++i) {
                switch (nr.getType()) {
                    case 't':
                        addList.add(nr.next());
                        break;
                    case 'z':
                        getList.add(nr.next());
                        break;
                    default:
                        System.err.printf("Unknown number type '%c'(%d).\n",
                                nr.getType(), (int) nr.getType());
                        if (--maxErr <= 0) {
                            System.exit(1);
                        }
                }
            }
        } finally {
            nr.close();
        }
    }

    /**
     * A simple main for test purposes.
     * Read all files given as command-line arguments.
     * @param args Filenames of input numbers in 't'/'z'-format.
     */
    public static void main(String args[]) {
        for (String filename : args) {
            readFile(filename, new ArrayList<Integer>(), new ArrayList<Integer>());
        }
    }
}
