package directionalChanges.file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

/**
 * Created by jerem on 01/08/14.
 */
public class ReaderFile extends AFile<FileReader> {

    private TreeMap<Integer, String> hashTable;

    public ReaderFile(String filePath)
    {
        super(filePath);
    }

    @Override
    public FileReader open() {
        try {
            return new FileReader(getFilePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close()
    {
        if (getFileStream() != null)
            try {
                getFileStream().close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
    }

    public TreeMap read()
    {
        BufferedReader      in;
        String              line;
        int                 index;

        hashTable = new TreeMap<Integer, String>();
        if (getFileStream() != null)
        {
            in = new BufferedReader(getFileStream());

            index = 0;
            try {
                while ((line = in.readLine()) != null)
                {
                    hashTable.put(index, line);
                    index++;
                }
            } catch (IOException e) {
                System.out.println("Exception Reading File: " + getFilePath());
            }
        }
        return hashTable;
    }

   /* public Hashtable<Integer, Double> convertStringToDouble()
    {
        Hashtable<Integer, Double>   table;

        Number test;

        table = new Hashtable<Integer, Double>();
        for (Map.Entry<Integer, String> field : hashTable.entrySet())
            table.put(field.getKey(), Double.parseDouble(field.getValue()));
        return table;
    }
    */
    public TreeMap    getHashTable()
    {
        return this.hashTable;
    }
}
