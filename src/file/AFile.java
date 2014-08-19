package file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by jerem on 01/08/14.
 */
public abstract class AFile<T> {

    protected String      filePath;
    protected T           fileStream;

    public AFile(String filePath)
    {
        this.filePath = filePath;
        fileStream = open();
    }

    public String getFilePath()
    {
        return this.filePath;
    }

    public T getFileStream()
    {
        return fileStream;
    }

    public static boolean isExist(String filePath)
    {
        Path path = Paths.get(filePath);
        return Files.notExists(path);
    }

    public static boolean delete(String filePath)
    {
        Path path = Paths.get(filePath);
        try {
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
        return false;
    }

    public static void createFile(String filePath){
        Path path = Paths.get(filePath);
        try {
            Files.createFile(path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public abstract T       open();
    public abstract void    close();
}
