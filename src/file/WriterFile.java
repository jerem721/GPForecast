package file;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by jerem on 01/08/14.
 */
public class WriterFile extends AFile<FileWriter>
{

    public WriterFile(String filePath)
    {
        super(filePath);
    }

    @Override
    public FileWriter open() {
        if (!AFile.isExist(getFilePath()))
            AFile.createFile(getFilePath());
        try {
            return new FileWriter(getFilePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FileWriter openToAppend() {
        if (!AFile.isExist(getFilePath()))
            AFile.createFile(getFilePath());
        try {
            return new FileWriter(getFilePath(), true);
        } catch (IOException e) {
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

    public void write(String record)
    {
        try
        {
            if (getFileStream() != null) {
                getFileStream().write(record + "\n");
                getFileStream().flush();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
