package visulization;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import test.TestInformation;

/**
 * Class for drawing precision-recall graph
 * @author Ivan Nesterov
 * @version 1.0
 * @since JDK 18.0
 */
public class PrecisionRecallGraph extends ApplicationFrame {

    /**
     * Contructor of this class
     * @param title is title of graph
     * @param information is meta-info about graph
     */
    public PrecisionRecallGraph(String title, TestInformation information) {
        super(title);
        final XYSeries aucSeries = new XYSeries("Precision-Recall curve graph");
        aucSeries.add(0, 1);
        aucSeries.add(information.countRecall(), information.countPrecision());
        aucSeries.add(1, 0);
        final XYSeriesCollection data = new XYSeriesCollection(aucSeries);
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Precision-recall curve graph",
                "Recall",
                "Precision",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(320, 320));
        setContentPane(chartPanel);
    }

}
