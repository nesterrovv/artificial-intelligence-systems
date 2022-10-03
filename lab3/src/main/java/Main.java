import datasetAnalyzers.CSVManager;
import data.Dataset;
import org.jfree.ui.RefineryUtilities;
import visulization.PrecisionRecallGraph;
import visulization.ROCGraph;
import test.Tester;
import tree.Node;
import java.io.FileNotFoundException;

/**
 * Main class of this project
 * @author Ivan Nesterov
 * @version 1.0
 * @since JDK 18.0
 */
public class Main {

    /**
     * Method which are used as entry point of this project
     * @param args is command lines arguments. Not uses here
     */
    public static void main(String[] args) {
        try {
            Dataset objects = CSVManager.readData("src/main/resources/data.csv", 12, 7);

            var split = objects.splitDataset(0.8, 13);
            var train = split[0];
            var test = split[1];

            Node root = new Node(train, null);
            Node.printTree(root);
            System.out.println('\n');

            var information = Tester.testTree(root, test, 1);
            System.out.println(information);

            ROCGraph roc = new ROCGraph("AUC-ROC graph", information);
            roc.pack();
            RefineryUtilities.centerFrameOnScreen(roc);
            roc.setVisible(true);

            PrecisionRecallGraph plot = new PrecisionRecallGraph("Precision-Recall curve graph", information);
            plot.pack();
            RefineryUtilities.centerFrameOnScreen(plot);
            plot.setVisible(true);
        } catch (FileNotFoundException exception) {
            System.err.println("File not found. Check \"resources\" folder content and try again.");
        }
    }

}
