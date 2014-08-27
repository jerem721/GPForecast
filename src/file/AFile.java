package file;

import java.io.File;
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

    protected AFile(String filePath, boolean append)
    {
        this.filePath = filePath;
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

    public static void createFile(String filePath)
    {
        Path path = Paths.get(filePath);
        try {
            Files.createFile(path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createDirectory(String dirPath)
    {
        File fb = new File(dirPath);
        if (!fb.exists())
            fb.mkdirs();
    }

    private static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files != null) {
            for(File f : files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                    f.delete();
                } else {
                    f.delete();
                }
            }
        }
    }

    public static void deleteDirectory(String dirPath)
    {
        File fb = new File(dirPath);
        if (fb.exists())
            deleteFolder(fb);
    }

    public abstract T       open();
    public abstract void    close();
}
