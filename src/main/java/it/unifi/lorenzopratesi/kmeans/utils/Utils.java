package it.unifi.lorenzopratesi.kmeans.utils;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class Utils {

    @SuppressWarnings("static-access")
    public static Options createOptions() {
        return new Options()
                .addOption(OptionBuilder.withArgName("NB_CLUSTERS").hasArg()
                        .withDescription("The number of clusters").withType(1).isRequired(true).create("k"))

                .addOption(OptionBuilder.withArgName("DIM").hasArg()
                        .withDescription("The number of dimensions of each data point").withType(1).isRequired(true).create("d"))

                .addOption(OptionBuilder.withLongOpt("input").withArgName("IN").hasArg()
                        .withDescription("The folder that contains the dataset files").isRequired(true).create("i"))

                .addOption(OptionBuilder.withLongOpt("output").withArgName("OUT").hasArg()
                        .withDescription("The folder where the output will be written").isRequired(true).create("o"))

                .addOption(OptionBuilder.withLongOpt("epsilon").withArgName("EPS").hasArg()
                        .withDescription("The number of decimals to consider while comparing decimal. ex: 1e-3").withType(1e-3).isRequired(false).create("e"));
    }
}
