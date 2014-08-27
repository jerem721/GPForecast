package properties;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class to read the config file.
 */
public class Properties {

    private java.util.Properties properties;
    private FileReader fileReader;

    public Properties()
    {
        properties = new java.util.Properties();
        try {
            fileReader = new FileReader("config.txt");
            properties.load(fileReader);
        } catch (FileNotFoundException e) {
            System.out.println("/!\\ Config file not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties(String file)
    {
        properties = new java.util.Properties();
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
            if (fileReader != null)
		fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getIntProperty(String key, int defaultValue)
    {
        int     intValue;
        String  value;

	if (properties == null)
	    return defaultValue;
        value = properties.getProperty(key);
        if (value == null)
            return defaultValue;
        return Integer.parseInt(value);
    }

    public double getDoubleProperty(String key, double defaultValue)
    {
        double  doubleValue;
        String  value;

	if (properties == null)
	    return defaultValue;
        value = properties.getProperty(key);
        if (value == null)
            return defaultValue;
        return Double.parseDouble(value);
    }

    public String getStringProperty(String key, String defaultValue)
    {
        String  value;

	if (properties == null)
	    return defaultValue;
        value = properties.getProperty(key);
        if (value == null)
            return defaultValue;
        return value;
    }
}
