package it.unifi.lorenzopratesi.kmeans.utils;

public class EuclideanDistance {
    // Let Vector[i] be the i'th position of the Vector

    public static double calculateDistance(double[] sample, double[] center) {
        double sum = 0.0;

        int length = center.length;
        for (int i = 0; i < length; i++) {
            sum += Math.pow((sample[i] - center[i]), 2.0);
        }
        return Math.sqrt(sum);
    }
}
