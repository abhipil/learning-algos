package learning;

import java.util.Comparator;

/**
 * @author abhishek
 *         created on 12/5/16.
 */

public class County {
    public int votedFor;
    public double[] values;

    public double distance;

    public County(int votedFor, double[] values) {
        this.votedFor = votedFor;
        this.values = values;
    }

    public double cosineDistanceFrom(County o) {
        if (o==null) {
            throw new IllegalArgumentException("Cannot compare to null");
        }
        double AB=0;
        double A=0;
        double B=0;
        for (int i = 0; i <this.values.length; i++) {
            AB += this.values[i]*o.values[i];
            A += this.values[i]*this.values[i];
            B += o.values[i]*o.values[i];
        }
        if (AB<0){
            AB = - AB;
        }
        return AB/(A*B);
    }


}
