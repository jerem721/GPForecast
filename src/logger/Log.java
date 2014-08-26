package logger;

import file.AFile;
import file.WriterFile;

/**
 * Created by jerem on 12/08/14.
 */
public class Log {

    private static Log          instance = null;
    private boolean             log = true;
    private boolean             logInFile = false;

    private WriterFile          writer;

    public static Log getInstance()
    {
        if (instance == null)
            instance = new Log();
        return instance;
    }

    public void enableLog(boolean enable)
    {
        log = enable;
    }

    public void log(String string)
    {
        if (log == true)
            System.out.println(string);
        if (logInFile == true)
            writer.write(string);
    }

    public void logInFile(boolean enable, String dir)
    {
        logInFile = enable;
        if (enable == true)
            AFile.deleteDirectory(dir);
    }

    public void changePathLog(String nameDir, String nameFile)
    {
        AFile.createDirectory(nameDir);
        if (writer != null)
            writer.close();
        writer = new WriterFile(nameDir + "/" + nameFile);
    }
}
