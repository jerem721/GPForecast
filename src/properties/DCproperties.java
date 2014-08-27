package properties;

import file.WriterFile;

/**
 * This class saves get properties for the Directional-Change algorithm.
 * If no value is found for any properties, then a default value is set.
 */
public class DCProperties {

    private double          maxThresholdDC;
    private double          minThresholdDC;
    private String          inputFile;
    private String          outFolder;
    private String          thresholdTest;

    public DCProperties(String configFile, String inputFile, String outFolder)
    {
        Properties properties;

        if (configFile == null)
            properties = new Properties();
        else
            properties = new Properties(configFile);

        maxThresholdDC = properties.getDoubleProperty("maxThresholdDC", 100.0);
        minThresholdDC = properties.getDoubleProperty("minThresholdDC", 0);
        thresholdTest = properties.getStringProperty("thresholdWithoutGP", "0.02 0.05 0.10 0.20 0.50 1");
        this.inputFile = inputFile;
        this.outFolder = outFolder;
        properties.close();
    }

    public double getMaxThresholdDC() {
        return maxThresholdDC;
    }

    public double getMinThresholdDC() {
        return minThresholdDC;
    }

    public String getInputFile() {
        return inputFile;
    }

    public String getOutFolder() {
        return outFolder;
    }

    public String getThresholdTest() {
        return thresholdTest;
    }

    @Override
    public String toString() {
        StringBuilder   str;

        str = new StringBuilder();
        str.append("===== Configuration DC =====\n");
        str.append("Data file: ").append(inputFile).append("\n");
        str.append("Output dir: ").append(outFolder).append("\n");
        str.append("Maximum threshold: ").append(maxThresholdDC).append("\n");
        str.append("Minimum threshold: ").append(minThresholdDC).append("\n\n");
        return str.toString();
    }

    public void save(){
        WriterFile  writerFile;

        writerFile = new WriterFile(outFolder + "/configuration.txt", true);
        writerFile.write(toString());
        writerFile.close();
    }
}
