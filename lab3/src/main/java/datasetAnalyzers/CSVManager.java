package datasetAnalyzers;

import data.DataObject;
import data.Dataset;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Class for manipulating for CSV file with necessary data.
 * @author Ivan Nesterov
 * @version 1.0
 * @since JDK 18.0
 */
public class CSVManager {

    /** Regular expression for split csv-row to pieces of data */
    private static final String regexpForSplitCSV = "([;,])(?=\\S)";

    /**
     * Method for reading data from .csv file
     * @param fileName is name of file for reading
     * @param attributesNumber is amount of attributes
     * @param randomSeed is random seed for split
     * @return dataset which are created via data from readable .csv file
     * @throws FileNotFoundException when impossible to find .csv file via entered path
     */
    public static Dataset readData(String fileName, int attributesNumber, long randomSeed)
            throws FileNotFoundException {
        ArrayList<DataObject> objects = new ArrayList<>();
        Scanner scanner = new Scanner(new File(fileName));
        String[] columnHeaders = scanner.nextLine().split(regexpForSplitCSV);
        columnHeaders = Arrays.copyOfRange(columnHeaders, 1, columnHeaders.length);
        int classIndex = columnHeaders.length - 1;
        List<Integer> attributes = new ArrayList<>();
        for (int i = 0; i < columnHeaders.length - 1; i++) {
            attributes.add(i);
        }
        Collections.shuffle(attributes, new Random(randomSeed));
        Set<Integer> attributesToHandle = new HashSet<>(attributes.subList(0, attributesNumber));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] stringData = line.split(regexpForSplitCSV);
            stringData = Arrays.copyOfRange(stringData, 1, stringData.length);
            int objectClass = Integer.parseInt(stringData[classIndex]) > 4 ? 1 : 0; // class adition
            stringData = Arrays.copyOfRange(stringData, 0, stringData.length - 1);
            DataObject object = new DataObject(objectClass);
            for (int i = 0; i < stringData.length; i++) {
                if(attributesToHandle.contains(i))
                    object.addAttribute(columnHeaders[i], Integer.parseInt(stringData[i]));
            }
            objects.add(object);
        }
        return new Dataset(objects); // "java-like" dataset creating
    }

}
