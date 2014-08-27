package properties;

import file.WriterFile;

/**
 * This class saves get properties for the GP algorithm.
 * If no value is found for any properties, then a default value is set.
 */
public class GPProperties {

    private String          outFolder;
    private int             numberOfRandomThreshold;
    private int             populationSize;
    private int             maxDepthSizeInitial;
    private int             maxDepthSize;
    private int             tournamentSize;
    private int             numberOfGeneration;
    private double          elitismPercentage;
    private double          reproductionProbability;
    private double          mutationProbability;
    private double          terminalNodeBias;
    private double          primProbability;

    private int             numberOfStock;
    private double          numberOfTrainingMoney;
    private double          numberOfTestingMoney;
    private int             numberOfTrainingValue;
    private int             numberOfTestingValue;

    public GPProperties(String configFile, String outFolder, int numberOfIteration)
    {
        Properties properties;

        if (configFile == null)
             properties = new Properties();
        else
            properties = new Properties(configFile);

        this.outFolder = outFolder;
        numberOfRandomThreshold = properties.getIntProperty("numberOfRandomThreshold", 30);
        populationSize = properties.getIntProperty("populationSize", 20);
        maxDepthSize = properties.getIntProperty("maxDepthSize", 8);
        maxDepthSizeInitial = properties.getIntProperty(" maxDepthSizeInitial", 5);
        tournamentSize = properties.getIntProperty("tournamentSize", 2);
        numberOfGeneration = properties.getIntProperty("numberOfGeneration", 30);
        elitismPercentage = properties.getDoubleProperty("elitismPercentage", 0.01);
        reproductionProbability = properties.getDoubleProperty("reproductionProbability", 0.01);
        mutationProbability = properties.getDoubleProperty("mutationProbability", 0.01);
        terminalNodeBias = properties.getDoubleProperty("terminalNodeBias", 0.1);
        primProbability = properties.getDoubleProperty("primProbability", 0.6);
        numberOfStock = properties.getIntProperty("numberOfStocks", 500);
        numberOfTrainingMoney = properties.getDoubleProperty("numberOfTrainingMoney", 500);
        numberOfTestingMoney = properties.getDoubleProperty("numberOfTestingMoney", 500);
        numberOfTrainingValue = properties.getIntProperty("numberOfTrainingValue", numberOfIteration/2);
        numberOfTestingValue = properties.getIntProperty("numberOfTestingValue", numberOfIteration/2);
        if (numberOfTrainingValue + numberOfTestingValue > numberOfIteration){
            numberOfTestingValue = numberOfIteration / 2;
            numberOfTrainingValue = numberOfIteration / 2;
        }
    }

    public String getOutFolder() {
        return outFolder;
    }

    public int getnumberOfRandomThreshold() {
        return numberOfRandomThreshold;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getMaxDepthSizeInitial() {
        return maxDepthSizeInitial;
    }

    public int getMaxDepthSize() {
        return maxDepthSize;
    }

    public int getTournamentSize() {
        return tournamentSize;
    }

    public int getNumberOfGeneration() {
        return numberOfGeneration;
    }

    public double getElitismPercentage() {
        return elitismPercentage;
    }

    public double getReproductionProbability() {
        return reproductionProbability;
    }

    public double getMutationProbability() {
        return mutationProbability;
    }

    public double getTerminalNodeBias() {
        return terminalNodeBias;
    }

    public double getPrimProbability() {
        return primProbability;
    }

    public int getNumberOfStock() {
        return numberOfStock;
    }

    public double getNumberOfTrainingMoney() {
        return numberOfTrainingMoney;
    }

    public double getNumberOfTestingMoney() {
        return numberOfTestingMoney;
    }

    public int getNumberOfTrainingValue() {
        return numberOfTrainingValue;
    }

    public int getNumberOfTestingValue() {
        return numberOfTestingValue;
    }

    @Override
    public String toString() {
        StringBuilder   str;

        str = new StringBuilder();
        str.append("===== Configuration GP =====\n");
        str.append("Output dir: ").append(outFolder).append("\n");
        str.append("Number of random threshold: ").append(numberOfRandomThreshold).append("\n");;
        str.append("Population size: ").append(populationSize).append("\n");
        str.append("Max depth size: ").append(maxDepthSize).append("\n");
        str.append("Max depth size initial: ").append(maxDepthSizeInitial).append("\n");
        str.append("Tournament size: ").append(tournamentSize).append("\n");
        str.append("Number of generation: ").append(numberOfGeneration).append("\n");
        str.append("Elitism percentage: ").append(elitismPercentage).append("\n");
        str.append("Reproduction probability: ").append(reproductionProbability).append("\n");
        str.append("Mutation probability: ").append(mutationProbability).append("\n");
        str.append("Terminal node bias: ").append(terminalNodeBias).append("\n");
        str.append("Prim probability: ").append(primProbability).append("\n");
        str.append("Number of stocks: ").append(numberOfStock).append("\n");
        str.append("Number of money (training): ").append(numberOfTrainingMoney).append("\n");
        str.append("Number of money (testing): ").append(numberOfTestingMoney).append("\n");
        str.append("Number of training value: ").append(numberOfTrainingValue).append("\n");
        str.append("Number of testing value: ").append(numberOfTestingValue).append("\n\n");
        return str.toString();
    }

    public void save(){
        WriterFile  writerFile;

        writerFile = new WriterFile(outFolder + "/configuration.txt", true);
        writerFile.write(toString());
        writerFile.close();
    }
}
