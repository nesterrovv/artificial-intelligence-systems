package test;

import data.DataObject;
import data.Dataset;
import exceptions.UnknownClassException;
import tree.Node;
import java.util.Comparator;

/**
 * Class for testing decision tree
 * @author Ivan Nesterov
 * @version 1.0
 * @since JDK 18.0
 */
public class Tester {

    /**
     * Method for testing decision free
     * @param treeRoot is tree-toot node
     * @param testData is dataset as a material for testing decision tree
     * @param keyClass is necessary key class
     * @return object of class, some metrics are encapsulated in which
     */
    public static TestInformation testTree(Node treeRoot, Dataset testData, int keyClass) {
        int TP = 0, TN = 0, FP = 0, FN = 0;
        for (DataObject object : testData.getObjects()) {
            int predictedClass = getTreeClassPrediction(treeRoot, object);
            if (predictedClass == object.getDataClassID()) {
                if (predictedClass == keyClass) TP++;
                else TN++;
            } else {
                if (predictedClass == keyClass) FP++;
                else FN++;
            }
        }
        return TestInformation
                .builder()
                .keyClass(keyClass)
                .truePositive(TP)
                .trueNegative(TN)
                .falsePositive(FP)
                .falseNegative(FN)
                .build();
    }

    /**
     * Method for getting tree decision class prediction
     * @param treeRoot in tree root node
     * @param object is object form dataset for analyze
     * @return decision tree class prediction
     * @throws UnknownClassException when impossible to find .csv file via entered path
     */
    private static int getTreeClassPrediction(Node treeRoot, DataObject object) throws UnknownClassException{
        Node node = treeRoot;
        while (node != null && !node.isLeaf()) {
           int objectAttributeValue = object.getAttributeValue(node.getAttribute());

            node = node.getChildren()
                    .stream()
                    .filter(n -> n.getParentAttributeValue() == objectAttributeValue)
                    .findFirst()
                    .orElse(
                            node.getChildren()
                                    .stream()
                                    .min(Comparator.comparing(a -> Math.abs(objectAttributeValue
                                            - a.getParentAttributeValue())))
                                    .orElse(null));
        }
    if(node != null) return node.getClassLabel();
    else throw new UnknownClassException("Unknown class.");
    }

}
