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
import java.util.Queue;

public class KNN {

    private final String trainingDataPath;
    private final String testingDataPath;
    private final int K;

    private TrainingData trainingData;

    public static void main(String[] args) {
        Path train = FileSystems.getDefault().getPath("classification-data", "votes-train.csv");
        Path test = FileSystems.getDefault().getPath("classification-data", "votes-test.csv");
        new KNN(train.toString(), test.toString(), 20).find();
    }
    public KNN(String trainingDataPath, String testingDataPath, int k) {
        this.trainingDataPath = trainingDataPath;
        this.testingDataPath = testingDataPath;
        K = k;
        trainingData = new TrainingData(readRecordsFromFile(trainingDataPath));
    }

    public void find() {
        int correct=0;
        int total=0;
        for ( String[] string : readRecordsFromFile(testingDataPath)) {
            County test = trainingData.convertFrom(string);
            if (test==null) {
                continue;
            }
            PriorityQueue<County> neighbours = new PriorityQueue<>(K, new Comparator<County>() {
                @Override
                public int compare(County o1, County o2) {
                    return Double.compare(o1.distance, o2.distance);
                }
            });
            for (County county: trainingData) {
                county.distance = county.cosineDistanceFrom(test);
                if (neighbours.size() == K) {
                    if(neighbours.peek().distance > county.distance){
                        neighbours.poll();
                        neighbours.add(county);
                    }
                } else {
                    neighbours.add(county);
                }
            }
            if (test.votedFor==classify(neighbours)) {
                correct++;
            }
            total++;
        }
        System.out.println("Accuracy: "+(correct*100)/total);
    }

    private int classify(Queue<County> neighbours) {
        int votedFor=0;
        for (County county : neighbours) {
            if(county.votedFor==1){
                votedFor+=1;
            }
        }
        if (votedFor>neighbours.size()/2) {
            return 1;
        }
        return 0;
    }

    public List<String[]> readRecordsFromFile(String filePath) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(trainingDataPath));
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
