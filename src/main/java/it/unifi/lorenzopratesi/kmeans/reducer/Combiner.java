package it.unifi.lorenzopratesi.kmeans.reducer;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Combiner extends Reducer<LongWritable, Text, LongWritable, Text> {
    public static int nb_dimensions;

    public void reduce(LongWritable _key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        double[] sum;
        double[] sample;
        int num = 0;
        sum = IntStream.range(0, nb_dimensions).mapToDouble(i -> 0.0).toArray();
        for (Text val : values) {
            int first_comma_index = val.toString().indexOf(",");
            sample = getSample(val.toString().substring(first_comma_index + 1));
            for (int i = 0; i < nb_dimensions; i++) {
                sum[i] += sample[i];
            }
            num++;
        }
        String sb = IntStream.range(0, nb_dimensions).mapToObj(i -> sum[i] + ",")
                .collect(Collectors.joining("", "", String.valueOf(num)));
        context.write(_key, new Text(sb));
    }

    public double[] getSample(String sampleText) {
        return Arrays.stream(sampleText.split(","))
                .mapToDouble(Double::parseDouble)
                .toArray();
    }
}