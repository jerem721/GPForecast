package logger;

/**
 * Created by jerem on 12/08/14.
 */
public class Log {

    private static Log instance = null;
    private boolean            log = true;

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
    }
}
