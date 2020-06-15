package it.unifi.lorenzopratesi.kmeans;

import it.unifi.lorenzopratesi.kmeans.mapper.KMeansMapper;
import it.unifi.lorenzopratesi.kmeans.reducer.Combiner;
import it.unifi.lorenzopratesi.kmeans.reducer.KMeansReducer;
import it.unifi.lorenzopratesi.kmeans.utils.Utils;
import org.apache.commons.cli.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PointsKMeans {
    private Job job;
    private final ArrayList<double[]> centers;
    private final double epsilon;
    private final String src;
    private final String out;
    private final int nb_clusters;
    private final int nb_dimensions;
    private final Random random;
    private final Logger logger = Logger.getLogger(KMeansMapper.class);
    public static DecimalFormat dFormater = new DecimalFormat("0.000");
    private int nb_iter;
    private FileSystem fs;
    private static Options options;

    public static void main(String[] args) throws Exception {
        try {
            options = Utils.createOptions();
            CommandLineParser parser = new BasicParser();
            CommandLine cmd = parser.parse(options, args);

            double eps = Double.parseDouble(cmd.getOptionValue("epsilon", "1e-3"));
            int k = Integer.parseInt(cmd.getOptionValue('k'));
            int d = Integer.parseInt(cmd.getOptionValue('d'));
            String input = cmd.getOptionValue("input");
            String output = cmd.getOptionValue("output");

            PointsKMeans pointsKMeans = new PointsKMeans(k, d, eps, input, output);
            pointsKMeans.run();
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("PKMeans", options, true);
        }
    }

    public PointsKMeans(int nb_clusters, int nb_dimensions, double epsilon, String src, String out) {
        this.epsilon = epsilon;
        this.src = src;
        this.out = out;
        this.nb_clusters = nb_clusters;
        this.nb_dimensions = nb_dimensions;
        this.random = new Random(8513);
        this.centers = new ArrayList<>();
        this.nb_iter = 1;
        this.initCenters();
        KMeansReducer.centers.addAll(centers);
        Combiner.nb_dimensions = this.nb_dimensions;
        KMeansReducer.nb_dimensions = this.nb_dimensions;
    }

    public void initJob() throws Exception {
        Configuration conf = new Configuration();
        fs = FileSystem.get(new Configuration());
        job = Job.getInstance(conf, "JobName");
        job.setJarByClass(PointsKMeans.class);

        job.setMapperClass(KMeansMapper.class);
        job.setCombinerClass(Combiner.class);
        job.setReducerClass(KMeansReducer.class);

        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path(this.src));
        FileOutputFormat.setOutputPath(job, new Path(this.out));
    }

    public void initCenters() {
        for (int i = 0; i < this.nb_clusters; i++) {
            double[] center = new double[this.nb_dimensions];
            for (int j = 0; j < this.nb_dimensions; j++) {
                center[j] = this.random.nextDouble();
            }
            this.centers.add(i, center);
        }
    }

    public int run() throws Exception {
        Instant start = Instant.now();
        do {
            initJob();
            deleteOutputDirIfExists();
            centers.clear();
            centers.addAll(KMeansReducer.centers);
            KMeansMapper.centers.clear();
            KMeansMapper.centers.addAll(centers);

            if (!job.waitForCompletion(true)) {
                logger.info("job failed");
            }

            nb_iter++;
            logger.info("\n#iter=" + nb_iter + "\n");
            logger.info("\tOld_centers: " + centersToString(centers) + "\n");
            logger.info("\tNew_centers: " + centersToString(KMeansReducer.centers) + "\n");
        } while (isDifferent(centers, KMeansReducer.centers));

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        logger.info("Finished in " + timeElapsed + "ms with " + nb_iter + " iterations");
        return nb_iter;
    }

    public String centersToString(ArrayList<double[]> centers) {
        return centers.stream().map(center -> centerToString(center) + "\t").collect(Collectors.joining());
    }

    public String centerToString(double[] center) {
        return Arrays.stream(center).mapToObj(v -> dFormater.format(v) + " ").collect(Collectors.joining());
    }

    public boolean isDifferent(ArrayList<double[]> old_centers, ArrayList<double[]> new_centers) {
        if (old_centers.size() != new_centers.size()) {
            return true;
        }
        return IntStream.range(0, nb_clusters).anyMatch(i -> isDifferent(old_centers.get(i), new_centers.get(i)));
    }

    public void deleteOutputDirIfExists() {
        try {
            fs.delete(new Path(this.out), true);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public boolean isDifferent(double[] center1, double[] center2) {
        return IntStream.range(0, nb_dimensions).anyMatch(i -> Math.abs(center1[i] - center2[i]) > epsilon);
    }
}