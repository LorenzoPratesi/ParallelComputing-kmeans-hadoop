package it.unifi.lorenzopratesi.kmeans.reducer;

import it.unifi.lorenzopratesi.kmeans.mapper.KMeansMapper;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class KMeansReducer extends Reducer<LongWritable, Text, LongWritable, Text> {
    public static int nb_dimensions;
    public static ArrayList<double[]> centers = new ArrayList<>();
    public static DecimalFormat dFormater = new DecimalFormat("#.###");
    private final Logger logger = Logger.getLogger(KMeansMapper.class);

    public void reduce(LongWritable _key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        double[] center;
        double[] sample;
        int num = 0, last_comma_index;
        center = IntStream.range(0, nb_dimensions).mapToDouble(i -> 0.0).toArray();

        for (Text val : values) {
            last_comma_index = val.toString().lastIndexOf(",");
            if (last_comma_index < 1) {
                logger.info("\n---->\nNo comma found in text: " + val.toString() + "\n----->\n");
                continue;
            }
            sample = getSample(val.toString().substring(0, last_comma_index));
            for (int i = 0; i < nb_dimensions; i++) {
                center[i] += sample[i];
            }
            num += Integer.parseInt(val.toString().substring(last_comma_index + 1));
        }
        StringBuilder center_sb = new StringBuilder();
        for (int i = 0; i < nb_dimensions; i++) {
            center[i] /= num;
            center_sb.append(i < nb_dimensions - 1 ? center[i] + " " : center[i]);

        }
        centers.set((int) _key.get(), center);
        context.write(_key, new Text(center_sb.toString()));
    }

    public double[] getSample(String sampleText) {
        return Arrays.stream(sampleText.split(","))
                .mapToDouble(Double::parseDouble)
                .toArray();
    }
}