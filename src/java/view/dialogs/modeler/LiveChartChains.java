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
import model.util.PieceUtilities;
import org.jfree.data.time.RegularTimePeriod;
import view.charts.LineChartLive2;
import view.notifications.Notifications;
import view.util.SwingUtilities;

/**
 *
 * @author skuarch
 */
public class LiveChartChains extends JDialog {

    private static final Notifications notifications = new Notifications();
    private Configuration configuration = null;
    private ArrayList initialData = null;
    private JPanel panelChartChains = null;
    private LineChartLive2 lcl = null;
    private Thread threadLive = null;
    private boolean shutdown = true;
    private int sleep = 0;
    private SubPiece subPiece = new SubPiece();

    //==========================================================================
    /**
     * Creates new form LiveChartChains
     */
    public LiveChartChains(java.awt.Frame parent, boolean modal) throws Exception {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(getContentPane());
        configuration = new ControllerConfiguration().getInitialConfiguration();
        buttonGroup1.add(jRadioButton1);
        buttonGroup1.add(jRadioButton3);
    }

    //==========================================================================
    public void setupInterface() {

        jProgressBar1.setIndeterminate(true);

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                try {

                    addWindowListener(new WindowAdapter() {

                        @Override
                        public void windowClosed(WindowEvent e) {
                            closeWindow();
                        }
                    });

                    setTitle("Chains");
                    jTabbedPane.removeAll();
                    initialData = requestsInitialData();

                    if (initialData != null) {
                        new SwingUtilities().fillJComboBox(jComboBox2, (Object[]) initialData.get(0));
                    }

                } catch (Exception e) {
                    notifications.error("error creating chart", e);
                } finally {
                    jProgressBar1.setIndeterminate(false);
                }

                return null;
            }
        }.execute();

    } // end setupInterface

    //==========================================================================
    private void closeWindow() {

        try {

            System.out.println("slienfo");
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
    private ArrayList requestsInitialData() {

        SubPiece subPiece = new SubPiece();
        ArrayList arrayList = null;

        try {

            subPiece.setView("live chains initial data");
            subPiece.setCollector(configuration.getMasterShaperServer());
            arrayList = (ArrayList) new JMSProccessor().sendReceive("live chains initial data", configuration.getMasterShaperServer(), "srs", configuration.getJmsTimeWaitMessage(), new PieceUtilities().subPieceToHashMap(subPiece));

        } catch (Exception e) {
            notifications.error("error requesting data", e);
        }

        return arrayList;

    }

    //==========================================================================
    private ArrayList requestData() {

        ArrayList arrayList = null;

        try {
            subPiece.setView("live chains data");
            subPiece.setCollector(new ControllerConfiguration().getInitialConfiguration().getMasterShaperServer());
            setTitle("Traffic Monitoring-Chains");
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

            data.add(jRadioButton1.isSelected());
            data.add(jComboBox1.getModel().getSelectedItem().toString());
            data.add(jComboBox2.getModel().getSelectedItem().toString());

            send.put("live chains data", data);

            configuration = new ControllerConfiguration().getInitialConfiguration();
            jmsp = new JMSProccessor();
            arrayList = (ArrayList) jmsp.sendReceive("live chains data", configuration.getMasterShaperServer(), "srs", configuration.getJmsTimeWaitMessage(), send);

        } catch (Exception e) {
            throw e;
        } finally {
            configuration = null;
            jmsp = null;
        }

        return arrayList;
    }

    //==========================================================================
    private void createChart() {

        try {

            lcl = new LineChartLive2(new SubPiece(), "", "", "", new JList(), "serie1");
            panelChartChains = lcl.getJPanel();
            panelChartChains.setName("chains");
            jTabbedPane.add(panelChartChains);
            live();

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

                    data.add(jRadioButton1.isSelected());
                    data.add(jComboBox1.getModel().getSelectedItem().toString());
                    data.add(jComboBox2.getModel().getSelectedItem().toString());

                    send.put("live chains data", data);

                    ArrayList response = (ArrayList) new JMSProccessor().sendReceive("live chains data", configuration.getMasterShaperServer(), "srs", configuration.getJmsTimeWaitMessage(), send);
                    createChart();
                    shutdown = true;

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

                                System.out.println("tipo " + arrayList.get(0));
                                
                                //rtp1 = (RegularTimePeriod) arrayList.get(0);
                                tmp = arrayList.get(1).toString();

                                if (rtp1 != null && tmp != null) {
                                    num = Double.parseDouble(tmp);
                                    lcl.addSeries1(rtp1, num);
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

                }catch(InterruptedException ie){
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
        jLabel1 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jButtonReload = new javax.swing.JButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jTabbedPane = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setText("close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setText("Traffic direction:");

        jRadioButton1.setSelected(true);
        jRadioButton1.setText("incoming");

        jLabel3.setText("Scale:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "bits/s", "bytes/s", "kbits/s", "kbytes/s", "mbits/s", "mbyte/ss" }));

        jButtonReload.setText("reload graph");
        jButtonReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReloadActionPerformed(evt);
            }
        });

        jRadioButton3.setText("outcoming");

        jLabel4.setText("Chain:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonReload, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1)
                            .addComponent(jRadioButton3)
                            .addComponent(jLabel4))
                        .addGap(0, 75, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 125, Short.MAX_VALUE)
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
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JTabbedPane jTabbedPane;
    // End of variables declaration//GEN-END:variables
} // end class
