package metrics;

import data.Dataset;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for receiving some info about dataset.
 * @author Ivan Nesterov
 * @version 1.0
 * @since JDK 18.0
 */
public class InformationManager {

    /**
     * Method for printing data about dataset
     * @param dataset is dataset for analyzing
     * @return info about dataset
     */
    private static double getInfo(Dataset dataset) {
        Map<Integer, Integer> objectOfClassAmount = new HashMap<>();
        dataset.getObjects().forEach(object -> objectOfClassAmount
                .merge(object.getDataClassID(), 1, Integer::sum));
        double info = 0;
        for (int className : objectOfClassAmount.keySet()) {
            info -= (double) objectOfClassAmount.get(className) / dataset.getRowAmount()
                    * (Math.log((double) objectOfClassAmount.get(className) / dataset.getRowAmount()) / Math.log(2));
        }
        return info;
    }

    /**
     * Method for counting gain ratio
     * @param dataset is dataset for analyzing
     * @param attribute is attribute which will used for counting gain ratio
     * @return counted gain ratio
     */
    public static double getGainRatio(Dataset dataset, String attribute) {
        Map<Integer, Integer> objectOfAttributeAmount = new HashMap<>();
        //Map to each attribute, which are contains Map of each class amount
        Map<Integer, Map<Integer, Integer>> attributeClassDistribution = new HashMap<>();

        dataset.getObjects().forEach(object -> {
            objectOfAttributeAmount.merge(object.getAttributeValue(attribute), 1, Integer::sum);
            if (!attributeClassDistribution.containsKey(object.getAttributeValue(attribute)))
                attributeClassDistribution.put(object.getAttributeValue(attribute), new HashMap<>());
            var objectsOfAttribute = attributeClassDistribution
                    .get(object.getAttributeValue(attribute));
            objectsOfAttribute.merge(object.getDataClassID(), 1, Integer::sum);
            attributeClassDistribution.put(object.getAttributeValue(attribute), objectsOfAttribute);
        });

        double infoAttribute = 0;
        double splitInfo = 0;

        for (int attributeValue : objectOfAttributeAmount.keySet()) {
            double coefficient = (double) objectOfAttributeAmount.get(attributeValue) / dataset.getRowAmount();
            splitInfo -= (double) objectOfAttributeAmount.get(attributeValue) / dataset.getRowAmount()
                           * (Math.log((double) objectOfAttributeAmount.get(attributeValue) / dataset.getRowAmount())
                    / Math.log(2));
            double innerPart = 0;
            var classValues = attributeClassDistribution.get(attributeValue);
            for (int classValue : classValues.keySet()) {
                innerPart -= (double) classValues.get(classValue) / objectOfAttributeAmount.get(attributeValue)
                        * (Math.log((double) classValues.get(classValue) / objectOfAttributeAmount.get(attributeValue))
                        / Math.log(2));
            }
            infoAttribute += coefficient * innerPart;
        }
        return (getInfo(dataset) - infoAttribute ) / splitInfo;
    }

    /**
     * Method for founding name of attribute which has max gain ratio
     * @param dataset is dataset for analyzing
     * @return found attribute name
     */
    public static String getAttributeWithMaxGainRatio(Dataset dataset) {
        double maxGainRatio = -1;
        String maxGainRatioAttribute = null;
        for (String attributeName : dataset.getObjectAttributes()) {
            if(getGainRatio(dataset, attributeName) > maxGainRatio) {
                maxGainRatio = getGainRatio(dataset, attributeName);
                maxGainRatioAttribute = attributeName;
            }
        }
        return maxGainRatioAttribute;
    }

    /**
     * Method for founding max gain ratio value
     * @param dataset is dataset for analyzing
     * @return found max in dataset gain ratio value
     */
    public static double getMaxGainRatio(Dataset dataset) {
        double maxGainRatio = -1;
        for (String attributeName : dataset.getObjectAttributes()) {
            if(getGainRatio(dataset, attributeName) > maxGainRatio) {
                maxGainRatio = getGainRatio(dataset, attributeName);
            }
        }
        return maxGainRatio;
    }

}
