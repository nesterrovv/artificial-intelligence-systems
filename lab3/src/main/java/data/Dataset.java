package data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.*;

/**
 * Class for implementation dataset.
 * @author Ivan Nesterov
 * @version 1.0
 * @since JDK 18.0
 */
@AllArgsConstructor
@Getter
public class Dataset {

    /** List of objects which are included in current dataset */
    private List<DataObject> objects;

    /**
     * Method for counting amount of rows in dataset
     * @return amount of dataset row's
     */
    public int getRowAmount() {
        return objects.size();
    }

    /**
     * Method for counting amount of classes in dataset
     * @return amount of classes in dataset
     */
    public int countClassAmount() {
        Set<Integer> classes = new HashSet<>();
        for (DataObject object : objects) {
            classes.add(object.getDataClassID());
        }
        return classes.size();
    }

    /**
     * Method for receiving set of attributes of dataset
     * @return set of attributes of dataset
     */
    public Set<String> getObjectAttributes() {
        return objects.get(0).getAttributes().keySet();
    }

    /**
     * Method for split dataset via given parameter.
     * @param attributeName is name of attribute for split dataset
     * @return list of dataset
     */
    public List<Dataset> splitDatasetByAttributeValues(String attributeName) {
        Map<Integer, List<DataObject>> datasets = new HashMap<>();
        for (DataObject object: objects) {
            int attributeValue = object.getAttributeValue(attributeName);

            if (!datasets.containsKey(attributeValue))
                datasets.put(attributeValue, new ArrayList<>());
            List<DataObject> objects = datasets.get(attributeValue);
            objects.add(object);
            datasets.put(attributeValue, objects);
        }
        List<Dataset> datasetList = new ArrayList<>();
        for (int key : datasets.keySet()) {
            datasetList.add(new Dataset(datasets.get(key)));
        }
        return datasetList;
    }

    /**
     * Method for founding max common class in dataset
     * @return found common class
     */
    public int getCommonClass() {
        Map<Integer, Integer> classCounts = new HashMap<>();
        objects.forEach(object -> classCounts.merge(object.getDataClassID(), 1, Integer::sum));
        var max = classCounts.entrySet().stream().max(Map.Entry.comparingByValue());
        return max.get().getKey();
    }

    /**
     * Method for split dataset to array of dataset
     * @param trainToTestRatio test ratio for split
     * @param randomSeed is random seed for split
     * @return array of dataset which is consists from initial dataset
     */
    public Dataset[] splitDataset(double trainToTestRatio, long randomSeed) {
        int trainObjectAmount = (int) (objects.size() * trainToTestRatio);
        Collections.shuffle(objects);
        Dataset train = new Dataset(new ArrayList<>(objects.subList(0, trainObjectAmount)));
        Dataset test = new Dataset(new ArrayList<>(objects.subList(trainObjectAmount + 1, objects.size())));
        return new Dataset[]{train, test};
    }

}
