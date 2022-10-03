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
 * Class for drawing ROC-AUC graph
 * @author Ivan Nesterov
 * @version 1.0
 * @since JDK 18.0
 */
public class ROCGraph extends ApplicationFrame {

    /**
     * Contructor of this class
     * @param title is title of graph
     * @param information is meta-info about graph
     */
    public ROCGraph(String title, TestInformation information) {
        super(title);
        final XYSeries aucSeries = new XYSeries("AUC-ROC");
        aucSeries.add(0, 0);
        aucSeries.add(information.countFalsePositiveRate(), information.countTruePositiveRate());
        aucSeries.add(1, 1);
        final XYSeriesCollection data = new XYSeriesCollection(aucSeries);
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "AUC",
                "False Positive Rate",
                "True Positive Rate",
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
