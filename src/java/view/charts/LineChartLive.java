package view.charts;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.JList;
import javax.swing.JPanel;
import model.beans.SubPiece;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.event.AxisChangeListener;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import view.notifications.Notifications;

/**
 *
 * @author skuarch
 */
public class LineChartLive extends ApplicationFrame implements AxisChangeListener {

    private Notifications notifications = null;
    private SubPiece subPiece = null;
    private TimeSeriesCollection dataset = null;
    private DateAxis dateAxis = null;
    private double x1;
    private double x2;
    private String title = null;
    private String titlex = null;
    private String titley = null;
    private JList list = null;
    private TimeSeries series1 = null;
    private TimeSeries series2 = null;
    private String seriesName1 = null;
    private String seriesName2 = null;
    
    //==========================================================================
    public LineChartLive(SubPiece subPiece, String title, String titlex, String titley, JList list, String seriesName1, String seriesName2) {
        super(title);
        this.notifications = new Notifications();
        this.subPiece = subPiece;
        this.title = title;
        this.titlex = titlex;
        this.titley = titley;
        this.list = list;
        JPanel chartPanel = getJPanel();
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
        this.seriesName1 = seriesName1;
        this.seriesName2 = seriesName2;
        this.series1 = new TimeSeries(seriesName1);
        this.series2 = new TimeSeries(seriesName2);
        dataset = new TimeSeriesCollection(this.series1);
        dataset.addSeries(series2);
    }

    //==========================================================================
    public void addSeries1(final RegularTimePeriod regularTimePeriod, final double number) {
        Thread t = new Thread(new Runnable() {

            public void run() {
                series1.addOrUpdate(regularTimePeriod, number);                  
            }
        });
        t.start();        
    }
    
    //==========================================================================
    public void addSeries2(final RegularTimePeriod regularTimePeriod, final double number) {
        Thread t = new Thread(new Runnable() {

            public void run() {                
                series2.addOrUpdate(regularTimePeriod, number);            
            }
        });
        t.start();
    }

    //==========================================================================
    private JFreeChart createChart(XYDataset dataset) {

        NumberAxis yAxis = null;

        JFreeChart chart = ChartFactory.createXYAreaChart(
                title,
                titlex,
                titley,
                dataset,
                PlotOrientation.VERTICAL,
                true, // legend
                false, // tool tips
                false // URLs
                );
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        ValueAxis domainAxis = new DateAxis("Time");                
        plot.setNoDataMessage("waiting for data...");
        //domainAxis.setFixedAutoRange(5000.0);  // 5 seconds
        domainAxis.setLowerMargin(1.0);
        domainAxis.setUpperMargin(1.0);
        domainAxis.setFixedAutoRange(3600000.0);  // 60 seconds 900000
        plot.setDomainAxis(domainAxis);
        plot.setForegroundAlpha(0.5f);

        dateAxis = (DateAxis) plot.getDomainAxis();
        dateAxis.addChangeListener(this);

        XYItemRenderer renderer = plot.getRenderer();
        renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
                StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
                new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss"),
                new DecimalFormat("#,###")));

        /*yAxis = (NumberAxis) plot.getRangeAxis();
        double upperBound = yAxis.getRange().getUpperBound();
        yAxis.setNumberFormatOverride(new NumberFormatLabel(upperBound, false));*/

        ChartUtilities.applyCurrentTheme(chart);
        return chart;
    }

    //==========================================================================
    private void handlerZoom() {

        Object[] data = null;

        try {

            /*data = new Object[2];
            data[0] = DateUtilities.getDate(x1);
            data[1] = DateUtilities.getDate(x2);

            list.setListData(data);*/

        } catch (Exception e) {
            notifications.error("error in zoom", e);
        }

    } // end handlerZoom

    //==========================================================================
    @Override
    public void axisChanged(AxisChangeEvent ace) {
        /*x1 = dateAxis.getLowerBound();
        x2 = dateAxis.getUpperBound();*/
        handlerZoom();
    } // end axisChanged

    //==========================================================================
    public JPanel getJPanel() {
        ChartPanel cp = new ChartPanel(createChart(dataset));
        cp.setDomainZoomable(true);
        cp.setRangeZoomable(false);
        return cp;
    }
}
