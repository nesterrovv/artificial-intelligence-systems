package test;

import lombok.Builder;
import java.text.DecimalFormat;

/**
 * Class for encapsulating metrics about test information
 * @author Ivan Nesterov
 * @version 1.0
 * @since JDK 18.0
 */
@Builder
public class TestInformation {

    /** Formatter for decimal numbers */
    private static final DecimalFormat df = new DecimalFormat("0.000");
    /** True positive metric */
    private final int truePositive;
    /** True negative metric */
    private final int trueNegative;
    /** False positive metric */
    private final int falsePositive;
    /** False negative metric */
    private final int falseNegative;
    /** Key class metric */
    private final int keyClass;

    /**
     * Method for counting accuracy metric
     * @return counted accuracy metric
     */
    public double countAccuracy() {
        return (double) (truePositive + trueNegative) / (truePositive + trueNegative + falsePositive + falseNegative);
    }

    /**
     * Method for counting precision metric
     * @return counted precision metric
     */
    public double countPrecision() {
        return (double) truePositive / (truePositive + falsePositive);
    }

    /**
     * Method for counting recall metric
     * @return counted recall metric
     */
    public double countRecall() {
        return (double) truePositive / (truePositive + falseNegative);
    }

    /**
     * Method for counting true positive rate metric
     * @return counted true positive rate metric
     */
    public double countTruePositiveRate() {
        return countRecall();
    }

    /**
     * Method for counting false positive rate metric
     * @return counted false positive rate metric
     */
    public double countFalsePositiveRate() {
        return (double) falsePositive / (falsePositive + trueNegative);
    }

    /**
     * Method for giving text representation of object of this class
     * @return text view of this class
     */
    @Override
    public String toString() {
        return  "Key class:\t\t" + keyClass + "\n" +
                "Precision:\t" + df.format(countPrecision()) + "\n" +
                "Recall:\t\t" + df.format(countRecall()) + "\n" +
                "Accuracy:\t" + df.format(countAccuracy());
    }

}
