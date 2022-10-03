package tree;

import data.Dataset;
import metrics.InformationManager;
import lombok.Getter;
import java.util.ArrayList;

/**
 * Class for implementation of tree node.
 * @author Ivan Nesterov
 * @version 1.0
 * @since JDK 18.0
 */
@Getter
public class Node {

    /** Attribute of a node */
    private String attribute;
    /** Attributte value of a parent node */
    private Integer parentAttributeValue;
    /** List of children nodes */
    private ArrayList<Node> children;
    /** Flag if this node is leaf */
    private boolean isLeaf;
    /** Class label */
    private int classLabel;
    /** Dataset with data */
    private Dataset dataset;

    /**
     * Constructor of node.
     * @param dataset is dataset with data
     * @param parentAttributeValue attribute value of parent node
     */
    public Node(Dataset dataset, Integer parentAttributeValue) {
        this.dataset = dataset;
        this.parentAttributeValue = parentAttributeValue;
        children = new ArrayList<>();
        if (InformationManager.getMaxGainRatio(dataset) > 0.0000001) {
            isLeaf = false;
            this.attribute = InformationManager.getAttributeWithMaxGainRatio(dataset);
            var datasets = dataset.splitDatasetByAttributeValues(attribute);
            datasets.forEach(d -> {
                children.add(new Node(d, d.getObjects().get(0).getAttributeValue(attribute)));
            });
        } else {
            isLeaf = true;
            classLabel = dataset.getCommonClass();
        }
    }

    /**
     * Method for priniting decision tree.
     * @param treeRoot tree root of node
     */
    public static void printTree(Node treeRoot) {
        System.out.println(treeRoot.attribute);
        for (Node child : treeRoot.children) {
            Node.print(child, 1);
        }

    }

    /**
     * Method for printing node
     * @param node is node for priniting
     * @param tabCounter is counter of tab symbols
     */
    private static void print(Node node, int tabCounter) {
        for (int i = 0; i < tabCounter - 1; i++) {
            System.out.print("|\t");
        }
        System.out.print("|---" + node.parentAttributeValue + ":\n");
        if (node.isLeaf) {
            for (int i = 0; i < tabCounter; i++) {
                System.out.print("|\t");
            }
            System.out.print("Result: " + node.classLabel + "\n");
        } else {
            for (int i = 0; i < tabCounter; i++) {
                System.out.print("|\t");
            }
            System.out.println(node.attribute);
            for (Node child: node.children) {
                Node.print(child, tabCounter + 1);
            }
        }
    }

}