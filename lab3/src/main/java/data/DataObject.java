package data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for implementation one object from dataset.
 * @author Ivan Nesterov
 * @version 1.0
 * @since JDK 18.0
 */
@AllArgsConstructor
@Getter
public class DataObject {

    /** Data-class identifier */
    private int dataClassID;
    /** Map of current data object attributes */
    private Map<String, Integer> attributes;

    /**
     * Constructor of this class.
     * Creates object via given data class ID and empty map with attributes.
     * @param dataClassID is data class identifier
     */
    public DataObject(int dataClassID) {
        this.dataClassID = dataClassID;
        this.attributes = new HashMap<>();
    }

    /**
     * Method for adding new attribute for the data class object.
     * @param attributeName is name of attribute
     * @param value is value which will be added after method call
     */
    public void addAttribute(String attributeName, Integer value) {
        attributes.put(attributeName, value);
    }

    /**
     * Method for receiving attribute value which will be found
     * via entered attribute name
     * @param attributeName is attribute name
     * @return value of given attribute name
     */
    public Integer getAttributeValue(String attributeName) {
        return attributes.get(attributeName);
    }

}
