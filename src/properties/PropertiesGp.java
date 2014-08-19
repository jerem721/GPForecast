package properties;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by jerem on 12/08/14.
 */
public class PropertiesGp {

    private Properties      properties;
    private FileReader      fileReader;

    public PropertiesGp()
    {
        properties = new Properties();
        try {
            fileReader = new FileReader("config.txt");
            properties.load(fileReader);
        } catch (FileNotFoundException e) {
            System.out.println("/!\\ Config file not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PropertiesGp(String file)
    {
        properties = new Properties();
        try {
            fileReader = new FileReader(file);
            properties.load(fileReader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close()
    {
        try {
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getIntProperty(String key, int defaultValue)
    {
        int     intValue;
        String  value;

        value = properties.getProperty(key);
        if (value == null)
            return defaultValue;
        return Integer.parseInt(value);
    }

    public double getDoubleProperty(String key, double defaultValue)
    {
        double  doubleValue;
        String  value;

        value = properties.getProperty(key);
        if (value == null)
            return defaultValue;
        return Double.parseDouble(value);
    }
}
