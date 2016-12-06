package learning.supervised;

import com.opencsv.CSVReader;
import learning.County;
import learning.TrainingData;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * @author abhishek
 *         created on 12/6/16.
 */
public class Perceptron {
    private static final int EPOCH = 200;
    private final String trainingDataPath;

    private TrainingData trainingData;

    private Random rand = new Random();
    private double[] weights = new double[9];
    private double theta;
    private double l_rate;

    public static void main(String[] args) {
        Path train = FileSystems.getDefault().getPath("classification-data", "votes-train.csv");
        Path test = FileSystems.getDefault().getPath("classification-data", "votes-test.csv");
        Perceptron perceptron = new Perceptron(train.toString(), 0.2,0.1d);
        perceptron.train();
        perceptron.percept(test.toString());
    }
    public Perceptron(String trainingDataPath, double theta, double l_rate) {
        this.trainingDataPath = trainingDataPath;
        trainingData = new TrainingData(readRecordsFromFile(trainingDataPath));
        for (int i = 0; i < weights.length; i++) {
            weights[i] = rand.nextDouble();
        }
        this.theta = theta;
        this.l_rate = l_rate;
    }

    public void train() {
        int error = -1, iterations = 0;
        while(error!=0 || iterations>EPOCH) {
            error=0;
            for (County county : trainingData) {
                int prediction = predict(county);
                int iter_error = county.votedFor - prediction;
                for (int i = 0; i < weights.length; i++) {
                    weights[i] += l_rate * iter_error * county.values[i];
                }
                error += (iter_error * iter_error);
            }
            iterations++;
        }
    }

    public void percept(String testingDataPath) {
        int correct=0;
        int total=0;
        for ( String[] string : readRecordsFromFile(testingDataPath)) {
            County test = trainingData.convertFrom(string);
            if (test==null) {
                continue;
            }
            if (test.votedFor == predict(test)) {
                correct++;
            }
            total++;
        }
        System.out.println("Accuracy: "+(correct*100)/total);
    }

    private int predict(County county) {
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i]*county.values[i];
        }
        return (sum>=theta) ? 1:0;
    }

    public List<String[]> readRecordsFromFile(String filePath) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Training data not found" + e);
        }
        try {
            return reader.readAll();
        } catch (IOException e) {
            throw new RuntimeException("Training data not found" + e);
        }
    }
}
