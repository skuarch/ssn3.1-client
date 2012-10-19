package view.dialogs.modeler;

import controllers.ControllerConfiguration;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import model.beans.Configuration;
import model.beans.SubPiece;
import model.jms.JMSProccessor;
import org.jfree.data.time.RegularTimePeriod;
import view.charts.LineChartLive3;
import view.notifications.Notifications;

/**
 *
 * @author skuarch
 */
public class LiveChartBandwidth extends JDialog {

    private static final Notifications notifications = new Notifications();
    private Configuration configuration = null;
    private ArrayList initialData = null;
    private JPanel panelChartbandwidth = null;
    private LineChartLive3 lcl = null;
    private Thread threadLive = null;
    private boolean shutdown = true;
    private int sleep = 0;
    private SubPiece subPiece = new SubPiece();

    //==========================================================================
    /**
     * Creates new form LiveChartbandwidth
     */
    public LiveChartBandwidth(java.awt.Frame parent, boolean modal) throws Exception {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(getContentPane());
        configuration = new ControllerConfiguration().getInitialConfiguration();        
    }

    //==========================================================================
    public void setupInterface() {

        jProgressBar1.setIndeterminate(true);

        //new SwingWorker<Void, Void>() {

            //@Override
            //protected Void doInBackground() throws Exception {

                try {

                    addWindowListener(new WindowAdapter() {

                        @Override
                        public void windowClosed(WindowEvent e) {
                            closeWindow();
                        }
                    });

                    setTitle("bandwidth");
                    //jTabbedPane.removeAll();                    

                } catch (Exception e) {
                    notifications.error("error creating chart", e);
                } finally {
                    jProgressBar1.setIndeterminate(false);                    
                }

                //return null;
            //}
        //}.execute();

    } // end setupInterface

    //==========================================================================
    private void closeWindow() {

        try {
            
            shutdown = false;

            if (threadLive != null && threadLive.isAlive()) {
                threadLive.interrupt();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            setVisible(false);
        }

    }
    
    //==========================================================================
    private ArrayList requestData() {

        ArrayList arrayList = null;

        try {
            subPiece.setView("live bandwidth data");
            subPiece.setCollector(new ControllerConfiguration().getInitialConfiguration().getMasterShaperServer());
            setTitle("Traffic Monitoring-bandwidth");
            arrayList = getSeriesLive();
        } catch (Exception e) {
            notifications.error("error requesting new data", e);
        }

        return arrayList;
    } // end requestData

    //==========================================================================
    private ArrayList getSeriesLive() throws Exception {

        if (subPiece == null) {
            throw new NullPointerException("subpiece is null");
        }

        ArrayList arrayList = null;
        JMSProccessor jmsp = null;
        Configuration configuration = null;
        ArrayList data = new ArrayList();
        HashMap send = new HashMap();

        try {

            
            data.add(jComboBox1.getModel().getSelectedItem().toString());
            

            send.put("live bandwidth data", data);

            configuration = new ControllerConfiguration().getInitialConfiguration();
            jmsp = new JMSProccessor();
            arrayList = (ArrayList) jmsp.sendReceive("live bandwidth data", configuration.getMasterShaperServer(), "srs", configuration.getJmsTimeWaitMessage(), send);
            
        } catch (Exception e) {
            throw e;
        } finally {
            configuration = null;
            jmsp = null;
        }

        return arrayList;
    }

    //==========================================================================
    private void createChart(ArrayList arrayList) {

        ArrayList arrayListSeriesNames = null;
        
        try {

             lcl = new LineChartLive3(new SubPiece(), "", "", "", new JList());
            panelChartbandwidth = lcl.getJPanel();
            panelChartbandwidth.setName("bandwidth");
            jTabbedPane.add(panelChartbandwidth);

            arrayListSeriesNames = (ArrayList) arrayList.get(2);
            lcl.createArrayListSeries(arrayListSeriesNames);
            lcl.addSeries();

        } catch (Exception e) {
            notifications.error("error creating chart", e);
        }


    } // end create chart

    //==========================================================================
    private void reload() {

        shutdown = false;
        jButtonReload.setEnabled(false);

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {


                jProgressBar1.setIndeterminate(true);
                ArrayList data = new ArrayList();
                HashMap send = new HashMap();

                try {


                    /*
                     * if(threadLive != null || threadLive.isAlive()){
                     * threadLive.interrupt(); }
                     */

                    jTabbedPane.removeAll();
                    jProgressBar1.setIndeterminate(true);                    
                    data.add(jComboBox1.getModel().getSelectedItem().toString());
                    

                    send.put("live bandwidth data", data);

                    ArrayList response = (ArrayList) new JMSProccessor().sendReceive("live bandwidth data", configuration.getMasterShaperServer(), "srs", configuration.getJmsTimeWaitMessage(), send);
                    createChart(response);
                    shutdown = true;
                    live();

                } catch (Exception e) {
                    notifications.error("error creating chart", e);
                } finally {
                    jProgressBar1.setIndeterminate(false);
                    jButtonReload.setEnabled(true);
                }

                return null;
            }
        }.execute();

    }

    //==========================================================================
    private void live() {

        sleep = Integer.parseInt(configuration.getSecondsLive()) * 1000;

        threadLive = new Thread(new Runnable() {

            public void run() {

                try {

                    while (shutdown) {

                        ArrayList arrayList = null;
                        RegularTimePeriod rtp1 = null;
                        RegularTimePeriod rtp2 = null;
                        ArrayList<Double> values = null;
                        double num = 0;
                        String tmp = null;

                        try {

                            if (!shutdown) {
                                break;
                            }

                            arrayList = requestData();

                            if (arrayList != null) {

                                if (arrayList.get(0).equals(true)) {
                                    notifications.error(arrayList.get(1).toString(), new Exception());
                                    break;
                                }
                                
                                rtp1 = (RegularTimePeriod) arrayList.get(3);
                                values = (ArrayList<Double>) arrayList.get(4);                                
                                lcl.addOrUpdate(rtp1, values);

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

                } catch (InterruptedException ie) {
                    threadLive = null;
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jButtonReload = new javax.swing.JButton();
        jTabbedPane = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setText("close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel3.setText("Scale:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "bit/s", "byte/s", "kbit/s", "kbyte/s", "mbit/s", "mbyte/s" }));

        jButtonReload.setText("reload graph");
        jButtonReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReloadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonReload, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 225, Short.MAX_VALUE)
                .addComponent(jButtonReload)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //==========================================================================
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        closeWindow();
    }//GEN-LAST:event_jButton1ActionPerformed

    //==========================================================================
    private void jButtonReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReloadActionPerformed
        reload();
    }//GEN-LAST:event_jButtonReloadActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonReload;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JTabbedPane jTabbedPane;
    // End of variables declaration//GEN-END:variables
}