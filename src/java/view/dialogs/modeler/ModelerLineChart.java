package view.dialogs.modeler;

import controllers.ControllerConfiguration;
import controllers.ControllerDataset;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JList;
import model.beans.SubPiece;
import org.jfree.data.time.RegularTimePeriod;
import view.charts.LineChartLive;
import view.notifications.Notifications;

/**
 *
 * @author skuarch
 */
public class ModelerLineChart extends JDialog {

    private static final Notifications notifications = new Notifications();
    private Thread threadLive = null;
    private boolean shutdown = true;
    private int sleep = 0;
    private LineChartLive lineChartLive = null;
    private SubPiece subPiece = new SubPiece();
    private String type = null;

    //==========================================================================
    public ModelerLineChart(java.awt.Frame parent, boolean modal, String type) {
        super(parent, modal);

        try {

            setSize(new Dimension(600, 500));
            this.sleep = Integer.parseInt(new ControllerConfiguration().getInitialConfiguration().getSecondsLive()) * 1000;
            this.type = type;
            initComponents();
            setLocationRelativeTo(getContentPane());

        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }

    } // end ModelerLineChart

    //==========================================================================
    private void initComponents() {

        try {

            pack();
            setSize(new Dimension(600, 500));

            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            addWindowListener(new WindowAdapter() {

                public void windowClosed(WindowEvent e) {
                    shutdown = false;
                }
            });

            if (this.type.equalsIgnoreCase("bandwidth")) {
                subPiece.setView("modeler live bandwidth");
                subPiece.setCollector(new ControllerConfiguration().getInitialConfiguration().getMasterShaperServer());
                lineChartLive = new LineChartLive(subPiece, "Traffic Monitoring-Bandwidth", "Time", "bits/sec", new JList(), "out interface", "in interface");
                add(lineChartLive.getJPanel());
                lineChartLive.setTitle("Bandwidth");
                setTitle("Traffic Monitoring-Bandwidth");
                live();
            } else if (this.type.equalsIgnoreCase("chains")) {
                subPiece.setView("modeler live chains");
                subPiece.setCollector(new ControllerConfiguration().getInitialConfiguration().getMasterShaperServer());
                lineChartLive = new LineChartLive(subPiece, "Traffic Monitoring-Chains", "Time", "bits/sec", new JList(), "incoming", "outgoing");
                add(lineChartLive.getJPanel());
                lineChartLive.setTitle("chains");
                setTitle("Traffic Monitoring-Chains");
                live();
            } else if (this.type.equalsIgnoreCase("pipes")) {
                subPiece.setView("modeler live pipes");
                subPiece.setCollector(new ControllerConfiguration().getInitialConfiguration().getMasterShaperServer());
                lineChartLive = new LineChartLive(subPiece, "Traffic Monitoring-Pipes", "Time", "bits/sec", new JList(), "incoming", "outgoing");
                add(lineChartLive.getJPanel());
                lineChartLive.setTitle("pipe");
                setTitle("Traffic Monitoring-Pipes");
                live();
            }



        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }

    } // end initComponents

    //==========================================================================
    private void live() {

        threadLive = new Thread(new Runnable() {

            public void run() {

                try {

                    while (shutdown) {

                        ArrayList arrayList = null;
                        RegularTimePeriod rtp1 = null;
                        RegularTimePeriod rtp2 = null;
                        double num = 0;
                        String tmp = null;

                        try {

                            if (!shutdown) {
                                break;
                            }

                            arrayList = requestData();

                            if (arrayList != null) {
                                
                                if(arrayList.get(6).equals(true)){
                                    notifications.error(arrayList.get(7).toString(), new Exception());
                                    break;
                                }          

                                rtp1 = (RegularTimePeriod) arrayList.get(0);
                                tmp = arrayList.get(1).toString();

                                if (rtp1 != null && tmp != null) {
                                    num = Double.parseDouble(tmp);
                                    lineChartLive.addSeries1(rtp1, num);
                                }

                                rtp2 = (RegularTimePeriod) arrayList.get(3);
                                tmp = arrayList.get(4).toString();

                                if (rtp2 != null && tmp != null) {
                                    num = Double.parseDouble(tmp);
                                    lineChartLive.addSeries2(rtp2, num);
                                }

                                arrayList = null;
                                rtp1 = null;
                                rtp2 = null;
                            }

                        } catch (Exception e) {
                            notifications.error("error", e);
                        } finally {
                            arrayList = null;
                            rtp1 = null;
                            num = 0;
                            tmp = null;
                        }

                        threadLive.sleep(sleep);

                    } //  end while

                } catch (Exception e) {
                    notifications.error("error in chart live", e);
                } finally {
                    threadLive = null;
                }
            }
        });

        threadLive.start();

    } // end live

    //==========================================================================
    private ArrayList requestData() {

        ArrayList arrayList = null;

        try {
            arrayList = new ControllerDataset().getSeries(subPiece);
        } catch (Exception e) {
            notifications.error("error requesting new data", e);
        }

        return arrayList;
    } // end requestData
} // end class
