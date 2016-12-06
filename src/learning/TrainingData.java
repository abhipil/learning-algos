package learning;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author abhishek
 *         created on 12/5/16.
 */
public class TrainingData implements Iterable<County>{
    private List<County> counties = new ArrayList<>(2000);
    private double[] max;
    private double[] min;

    public TrainingData(List<String[]> counties) {
        double[] max = new double[9];
        double[] min = new double[9];
        for (int i = 0; i <9; i++) {
            max[i]=Double.MIN_VALUE;
            min[i]=Double.MAX_VALUE;
        }
        for (String[] county : counties) {
            for (int i = 1; i < county.length; i++) {
                double val;
                try {
                    val = Double.parseDouble(county[i]);
                } catch (NumberFormatException nfe) {
                    continue;
                }
                if (max[i-1]<val) {
                    max[i-1] = val;
                }
                if (min[i-1]>val) {
                    min[i-1] = val;
                }
            }
        }
        this.max = max;
        this.min = min;
        for (String[] county : counties) {
            County c = convertFrom(county);
            if (c!=null)
                this.counties.add(c);
        }
    }

    public County convertFrom(String[] county) {
        int votedFor;
        double attributes[] = new double[9];
        try {
            votedFor = Integer.parseInt(county[0]);
            for (int i = 1; i < county.length; i++) {
                double val = Integer.parseInt(county[0]);
                attributes[i - 1] = (val - min[i - 1]) / (max[i - 1] - min[i - 1]);
            }
        } catch (NumberFormatException nfe) {
            return null;
        }
        return new County(votedFor,attributes);
    }

    public void add(County county) {
        counties.add(county);
    }


    @Override
    public Iterator<County> iterator() {
        return counties.iterator();
    }

    public int size() {
        return counties.size();
    }
}
