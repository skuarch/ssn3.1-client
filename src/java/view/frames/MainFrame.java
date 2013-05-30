package view.frames;

import controllers.ControllerConfiguration;
import controllers.modeler.ControllerModelerInformer;
import controllers.modeler.ControllerModelerOptions;
import controllers.modeler.ControllerModelerTableManager;
import controllers.modeler.ControllerModelerTableSettings;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import model.beans.SubPiece;
import model.jms.JMSProccessor;
import model.util.PieceUtilities;
import view.dialogs.*;
import view.dialogs.modeler.LineChartBW;
import view.dialogs.modeler.LiveChartBandwidth;
import view.dialogs.modeler.LiveChartChains;
import view.dialogs.modeler.LiveChartPipes;
import view.helpers.ControlNavigators;
import view.notifications.Alert;
import view.notifications.Notifications;
import view.splits.Navigator;
import view.trees.TreeCollectors;
import view.trees.TreeViews;

/**
 *
 * @author skuarch
 */
public class MainFrame extends JFrame {

    private static MainFrame INSTANCE = null;
    private Notifications notifications = null;
    private About about = null;
    private E2E e2e = null;

    //==========================================================================
    /**
     * Creates new form MainFrame
     */
    private MainFrame() {

        notifications = new Notifications();
        e2e = E2E.getInstance(this, true);
        initComponents();
        setLocationRelativeTo(getContentPane());
        onLoad();
        jMenuItemModelerBandwidth.setVisible(true);

    } // end MainFrame

    //==========================================================================
    private synchronized static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MainFrame();
        }
    }

    //==========================================================================
    public static MainFrame getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    //==========================================================================
    private synchronized void onLoad() {

        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/view/images/ssnIcon.png")));

        try {

            //title
            setTitle("SSN " + new ControllerConfiguration().getInitialConfiguration().getProjectName().toUpperCase());

            Thread t = new Thread(new Runnable() {

                public void run() {

                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {

                            try {

                                Thread.sleep(15);
                                jTabbedPaneView.add(TreeViews.getInstance());
                                jTabbedPaneCollectors.add(TreeCollectors.getInstance());
                                jSplitPaneMain.setRightComponent(Navigator.getInstance());

                                //used master shaper
                                if (new ControllerConfiguration().getInitialConfiguration().getUsedMasterShaper().equalsIgnoreCase("no")) {
                                    jMenuModeler.setVisible(false);
                                }

                            } catch (Exception e) {
                                new Notifications().error("error loading interface", e);
                            }
                        }
                    });
                }
            });
            t.setName("onLoad");
            t.start();

        } catch (Exception e) {
            notifications.error("error loading interface", e);
        }

    } // end onLoad

    //==========================================================================
    public void control() {

        Thread t = new Thread(new ControlNavigators());
        t.start();
        t.setName("ControlNavigators");

    } // end control

    //==========================================================================
    private void callEventViewer() {

        EventViewer eventViewer = null;

        try {

            eventViewer = EventViewer.getInstance();

            if (eventViewer.isVisible()) {
                eventViewer.setVisible(false);
            } else {
                eventViewer.setVisible(true);
            }

        } catch (Exception e) {
            notifications.error("error opening window", e);
        }

    }

    //==========================================================================
    private void createPdf() {

        if (Navigator.getInstance().getTabCount() <= 0) {
            return;
        }

        new Thread(new Runnable() {

            public void run() {

                try {
                    String path = new ChooserFile().getPath();
                    if (path != null) {
                        SaveData saveData = new SaveData(MainFrame.getInstance(), false);
                        saveData.setVisible(true);
                        saveData.createPDFReport(Navigator.getInstance().getComponents(), path);
                    }
                } catch (Exception e) {
                    notifications.error("error creating pdf", e);
                }
            }
        }).start();


    } // end createPdf     

    //==========================================================================
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSplitPaneMain = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jTabbedPaneView = new javax.swing.JTabbedPane();
        jTabbedPaneCollectors = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuTools = new javax.swing.JMenu();
        jMenuItemEventViewer = new javax.swing.JMenuItem();
        jMenuSearch = new javax.swing.JMenu();
        jMenuItemSubnet = new javax.swing.JMenuItem();
        jMenuItemIP = new javax.swing.JMenuItem();
        jMenuItemPort = new javax.swing.JMenuItem();
        jMenuItemCloseAll = new javax.swing.JMenuItem();
        jMenuItemReport = new javax.swing.JMenuItem();
        jMenuItemE2E = new javax.swing.JMenuItem();
        jMenuSettings = new javax.swing.JMenu();
        jMenuItemConfiguration = new javax.swing.JMenuItem();
        jMenuModeler = new javax.swing.JMenu();
        jMenuManage = new javax.swing.JMenu();
        jMenuItemChain = new javax.swing.JMenuItem();
        jMenuItemPipes = new javax.swing.JMenuItem();
        jMenuItemFilters = new javax.swing.JMenuItem();
        jMenuModelerSettings = new javax.swing.JMenu();
        jMenuItemTargets = new javax.swing.JMenuItem();
        jMenuItemPorts = new javax.swing.JMenuItem();
        jMenuItemProtocols = new javax.swing.JMenuItem();
        jMenuItemServiceLevel = new javax.swing.JMenuItem();
        jMenuItemOptions = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemShow = new javax.swing.JMenuItem();
        jMenuItemLoad = new javax.swing.JMenuItem();
        jMenuItemLoadDebug = new javax.swing.JMenuItem();
        jMenuItemUnload = new javax.swing.JMenuItem();
        jMenuItemOverview = new javax.swing.JMenuItem();
        jMenuMonitoring = new javax.swing.JMenu();
        jMenuItemModelerChains = new javax.swing.JMenuItem();
        jMenuItemModelerPipes = new javax.swing.JMenuItem();
        jMenuItemModelerBandwidth = new javax.swing.JMenuItem();
        jMenuModelerTools = new javax.swing.JMenu();
        jMenuItemStopCollector = new javax.swing.JMenuItem();
        jMenuItemStartCollector = new javax.swing.JMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuItemAbout = new javax.swing.JMenuItem();

        jMenu2.setText("jMenu2");

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPaneMain.setDividerLocation(250);

        jSplitPane2.setDividerLocation(230);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setTopComponent(jTabbedPaneView);
        jSplitPane2.setRightComponent(jTabbedPaneCollectors);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
        );

        jSplitPaneMain.setLeftComponent(jPanel1);

        jMenuFile.setText("File");

        jMenuItemExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/images/shutdown-mini.png"))); // NOI18N
        jMenuItemExit.setText("exit");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemExit);

        jMenuBar1.add(jMenuFile);

        jMenuTools.setText("Tools");

        jMenuItemEventViewer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemEventViewer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/images/eventViewer.png"))); // NOI18N
        jMenuItemEventViewer.setText("event viewer");
        jMenuItemEventViewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemEventViewerActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItemEventViewer);

        jMenuSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/images/search.png"))); // NOI18N
        jMenuSearch.setText("search");

        jMenuItemSubnet.setText("subnet");
        jMenuItemSubnet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSubnetActionPerformed(evt);
            }
        });
        jMenuSearch.add(jMenuItemSubnet);

        jMenuItemIP.setText("ip");
        jMenuItemIP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemIPActionPerformed(evt);
            }
        });
        jMenuSearch.add(jMenuItemIP);

        jMenuItemPort.setText("port number");
        jMenuItemPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPortActionPerformed(evt);
            }
        });
        jMenuSearch.add(jMenuItemPort);

        jMenuTools.add(jMenuSearch);

        jMenuItemCloseAll.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemCloseAll.setText("close all");
        jMenuItemCloseAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseAllActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItemCloseAll);

        jMenuItemReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/images/pdf.png"))); // NOI18N
        jMenuItemReport.setText("create report");
        jMenuItemReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReportActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItemReport);

        jMenuItemE2E.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/images/e2e.png"))); // NOI18N
        jMenuItemE2E.setText("end to end");
        jMenuItemE2E.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemE2EActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItemE2E);

        jMenuBar1.add(jMenuTools);

        jMenuSettings.setText("Settings");

        jMenuItemConfiguration.setText("configuration");
        jMenuItemConfiguration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemConfigurationActionPerformed(evt);
            }
        });
        jMenuSettings.add(jMenuItemConfiguration);

        jMenuBar1.add(jMenuSettings);

        jMenuModeler.setText("Shaper");

        jMenuManage.setText("manage");

        jMenuItemChain.setText("chains");
        jMenuItemChain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemChainActionPerformed(evt);
            }
        });
        jMenuManage.add(jMenuItemChain);

        jMenuItemPipes.setText("pipes");
        jMenuItemPipes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPipesActionPerformed(evt);
            }
        });
        jMenuManage.add(jMenuItemPipes);

        jMenuItemFilters.setText("filters");
        jMenuItemFilters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFiltersActionPerformed(evt);
            }
        });
        jMenuManage.add(jMenuItemFilters);

        jMenuModeler.add(jMenuManage);

        jMenuModelerSettings.setText("settings");

        jMenuItemTargets.setText("targets");
        jMenuItemTargets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemTargetsActionPerformed(evt);
            }
        });
        jMenuModelerSettings.add(jMenuItemTargets);

        jMenuItemPorts.setText("ports");
        jMenuItemPorts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPortsActionPerformed(evt);
            }
        });
        jMenuModelerSettings.add(jMenuItemPorts);

        jMenuItemProtocols.setText("protocols");
        jMenuItemProtocols.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemProtocolsActionPerformed(evt);
            }
        });
        jMenuModelerSettings.add(jMenuItemProtocols);

        jMenuItemServiceLevel.setText("service level");
        jMenuItemServiceLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemServiceLevelActionPerformed(evt);
            }
        });
        jMenuModelerSettings.add(jMenuItemServiceLevel);

        jMenuItemOptions.setText("options");
        jMenuItemOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOptionsActionPerformed(evt);
            }
        });
        jMenuModelerSettings.add(jMenuItemOptions);

        jMenuModeler.add(jMenuModelerSettings);

        jMenu1.setText("rules");

        jMenuItemShow.setText("show");
        jMenuItemShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemShowActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemShow);

        jMenuItemLoad.setText("load");
        jMenuItemLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLoadActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemLoad);

        jMenuItemLoadDebug.setText("load debug");
        jMenuItemLoadDebug.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLoadDebugActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemLoadDebug);

        jMenuItemUnload.setText("unload");
        jMenuItemUnload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUnloadActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemUnload);

        jMenuItemOverview.setText("overview");
        jMenuItemOverview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOverviewActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemOverview);

        jMenuModeler.add(jMenu1);

        jMenuMonitoring.setText("monitoring");

        jMenuItemModelerChains.setText("chains");
        jMenuItemModelerChains.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemModelerChainsActionPerformed(evt);
            }
        });
        jMenuMonitoring.add(jMenuItemModelerChains);

        jMenuItemModelerPipes.setText("pipes");
        jMenuItemModelerPipes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemModelerPipesActionPerformed(evt);
            }
        });
        jMenuMonitoring.add(jMenuItemModelerPipes);

        jMenuItemModelerBandwidth.setText("bandwidth");
        jMenuItemModelerBandwidth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemModelerBandwidthActionPerformed(evt);
            }
        });
        jMenuMonitoring.add(jMenuItemModelerBandwidth);

        jMenuModeler.add(jMenuMonitoring);

        jMenuModelerTools.setText("tools");

        jMenuItemStopCollector.setText("stop collector");
        jMenuItemStopCollector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemStopCollectorActionPerformed(evt);
            }
        });
        jMenuModelerTools.add(jMenuItemStopCollector);

        jMenuItemStartCollector.setText("start collector");
        jMenuItemStartCollector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemStartCollectorActionPerformed(evt);
            }
        });
        jMenuModelerTools.add(jMenuItemStartCollector);

        jMenuModeler.add(jMenuModelerTools);

        jMenuBar1.add(jMenuModeler);

        jMenuHelp.setText("Help");

        jMenuItemAbout.setText("about");
        jMenuItemAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAboutActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemAbout);

        jMenuBar1.add(jMenuHelp);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPaneMain, javax.swing.GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPaneMain)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //==========================================================================
    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    //===================Default=======================================================
    private void jMenuItemEventViewerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEventViewerActionPerformed
        callEventViewer();
    }//GEN-LAST:event_jMenuItemEventViewerActionPerformed

    //==========================================================================
    private void jMenuItemConfigurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemConfigurationActionPerformed
        Configuration configuration = null;

        try {

            configuration = new Configuration(this, true);

            if (configuration.isVisible()) {
                configuration.setVisible(false);
            } else {
                configuration.setVisible(true);
            }

        } catch (Exception e) {
            notifications.error("error opening window", e);
        }
    }//GEN-LAST:event_jMenuItemConfigurationActionPerformed

    //==========================================================================
    private void jMenuItemIPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemIPActionPerformed
        new SearchIPAddress(this, true).setVisible(true);
    }//GEN-LAST:event_jMenuItemIPActionPerformed

    //==========================================================================
    private void jMenuItemSubnetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSubnetActionPerformed
        new SearchSubnet(this, true).setVisible(true);
    }//GEN-LAST:event_jMenuItemSubnetActionPerformed

    //==========================================================================
    private void jMenuItemCloseAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseAllActionPerformed
        Navigator.getInstance().closeAll();
    }//GEN-LAST:event_jMenuItemCloseAllActionPerformed

    //==========================================================================
    private void jMenuItemReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReportActionPerformed
        createPdf();
    }//GEN-LAST:event_jMenuItemReportActionPerformed

    //==========================================================================
    private void jMenuItemPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPortActionPerformed
        new SearchPort(this, true).setVisible(true);
    }//GEN-LAST:event_jMenuItemPortActionPerformed

    //==========================================================================
    private void jMenuItemAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAboutActionPerformed

        new Thread(new Runnable() {

            public void run() {
                about = new About(INSTANCE, true);

                if (about.isVisible()) {
                    about.toFront();
                } else {
                    about.setVisible(true);
                }
            }
        }).start();

    }//GEN-LAST:event_jMenuItemAboutActionPerformed

    //==========================================================================
    private void jMenuItemE2EActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemE2EActionPerformed

        e2e.setVisible(true);
        e2e.requestFocus();
        e2e.toFront();
        e2e.setAlwaysOnTop(true);
        e2e.requestFocus();
        e2e.setAlwaysOnTop(false);

    }//GEN-LAST:event_jMenuItemE2EActionPerformed

    //==========================================================================
    private void jMenuItemChainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemChainActionPerformed

        new Thread(new Runnable() {

            public void run() {
                try {

                    /*
                     * ControllerChainsBorrar cc = new ControllerChainsBorrar();
                     * cc.setInterface(); cc.setAutoCreateRowSorter(true);
                     * cc.setRowHeight(34); cc.setListenerButtonNew();
                     * cc.setVisible(true);
                     */

                    //the new
                    /*
                     * ControllerModelerTableManager cgt = new
                     * ControllerModelerTableManager("chains");
                     * cgt.setupInterface(); cgt.setVisible(true);
                     */

                    ControllerModelerTableManager cmtm = new ControllerModelerTableManager("chains");
                    cmtm.setupInterface();
                    cmtm.setVisible(true);


                } catch (Exception e) {
                    notifications.error("error loading chains", e);
                }
            }
        }).start();

    }//GEN-LAST:event_jMenuItemChainActionPerformed

    //==========================================================================
    private void jMenuItemPipesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPipesActionPerformed

        try {
            ControllerModelerTableManager cgt = new ControllerModelerTableManager("pipes");
            cgt.setupInterface();
            cgt.setVisible(true);
        } catch (Exception e) {
            notifications.error("error loading chains", e);
        }

    }//GEN-LAST:event_jMenuItemPipesActionPerformed

    //==========================================================================
    private void jMenuItemFiltersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemFiltersActionPerformed
        try {
            ControllerModelerTableManager cgt = new ControllerModelerTableManager("filters");
            cgt.setupInterface();
            cgt.setVisible(true);
        } catch (Exception e) {
            notifications.error("error loading chains", e);
        }
    }//GEN-LAST:event_jMenuItemFiltersActionPerformed

    //==========================================================================
    private void jMenuItemPortsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPortsActionPerformed

        try {

            ControllerModelerTableSettings cmts = new ControllerModelerTableSettings("ports");
            cmts.setupInterface();
            cmts.setVisible(true);

        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }

    }//GEN-LAST:event_jMenuItemPortsActionPerformed

    //==========================================================================
    private void jMenuItemTargetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTargetsActionPerformed
        try {

            ControllerModelerTableSettings cmts = new ControllerModelerTableSettings("targets");
            cmts.setupInterface();
            cmts.setVisible(true);

        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }
    }//GEN-LAST:event_jMenuItemTargetsActionPerformed

    //==========================================================================
    private void jMenuItemProtocolsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemProtocolsActionPerformed
        try {

            ControllerModelerTableSettings cmts = new ControllerModelerTableSettings("protocols");
            cmts.setupInterface();
            cmts.setVisible(true);

        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }
    }//GEN-LAST:event_jMenuItemProtocolsActionPerformed

    //==========================================================================
    private void jMenuItemServiceLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemServiceLevelActionPerformed
        try {

            ControllerModelerTableSettings cmts = new ControllerModelerTableSettings("service level");
            cmts.setupInterface();
            cmts.setVisible(true);

        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }
    }//GEN-LAST:event_jMenuItemServiceLevelActionPerformed

    //==========================================================================
    private void jMenuItemOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOptionsActionPerformed

        ControllerModelerOptions cmo = null;

        try {

            cmo = new ControllerModelerOptions();
            cmo.setupInterface();
            cmo.setVisible(true);

        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        } finally {
            cmo = null;
        }

    }//GEN-LAST:event_jMenuItemOptionsActionPerformed

    //==========================================================================
    private void jMenuItemShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemShowActionPerformed

        try {
            ControllerModelerInformer cmi = new ControllerModelerInformer("show");
            cmi.setupInterface();
            cmi.setVisible(true);
        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }

    }//GEN-LAST:event_jMenuItemShowActionPerformed

    private void jMenuItemLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLoadActionPerformed
        try {
            ControllerModelerInformer cmi = new ControllerModelerInformer("load");
            cmi.setupInterface();
            cmi.setVisible(true);
        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }
    }//GEN-LAST:event_jMenuItemLoadActionPerformed

    private void jMenuItemLoadDebugActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLoadDebugActionPerformed
        try {
            ControllerModelerInformer cmi = new ControllerModelerInformer("load debug");
            cmi.setupInterface();
            cmi.setVisible(true);
        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }
    }//GEN-LAST:event_jMenuItemLoadDebugActionPerformed

    //==========================================================================
    private void jMenuItemUnloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemUnloadActionPerformed
        try {
            ControllerModelerInformer cmi = new ControllerModelerInformer("unload");
            cmi.setupInterface();
            cmi.setVisible(true);
        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }
    }//GEN-LAST:event_jMenuItemUnloadActionPerformed

    //==========================================================================
    private void jMenuItemModelerBandwidthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemModelerBandwidthActionPerformed

        try {
            LiveChartBandwidth lcb = new LiveChartBandwidth(null, true);
            lcb.setupInterface();
            lcb.setVisible(true);
        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }        

    }//GEN-LAST:event_jMenuItemModelerBandwidthActionPerformed

    //==========================================================================
    private void jMenuItemModelerChainsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemModelerChainsActionPerformed
        
        try {
            LiveChartChains lcc =new LiveChartChains(null, true);
            lcc.setupInterface();
            lcc.setVisible(true);
        } catch (Exception e) {
        }
        
    }//GEN-LAST:event_jMenuItemModelerChainsActionPerformed

    //==========================================================================
    private void jMenuItemModelerPipesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemModelerPipesActionPerformed
        //new ModelerLineChart(null, true, "pipes").setVisible(true);
        try {
            LiveChartPipes lcp =new LiveChartPipes(null, true);
            lcp.setupInterface();
            lcp.setVisible(true);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jMenuItemModelerPipesActionPerformed

    //==========================================================================
    private void jMenuItemStopCollectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemStopCollectorActionPerformed

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                model.beans.Configuration configuration = null;
                SubPiece sp = new SubPiece();
                String message = "no response from server";
                Alert alert = new Alert(null, true);

                try {

                    configuration = new ControllerConfiguration().getInitialConfiguration();
                    message = (String) new JMSProccessor().sendReceive("stop collector", configuration.getMasterShaperServer(), "srs", configuration.getJmsTimeWaitMessage(), new PieceUtilities().subPieceToHashMap(sp));

                    alert.setLabel(message);
                    alert.setVisible(true);

                } catch (Exception e) {
                    notifications.error("error sending stop signal", e);
                }

                return null;
            }
        }.execute();

    }//GEN-LAST:event_jMenuItemStopCollectorActionPerformed

    //==========================================================================
    private void jMenuItemStartCollectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemStartCollectorActionPerformed

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                model.beans.Configuration configuration = null;
                SubPiece sp = new SubPiece();
                String message = "no response from server";
                Alert alert = new Alert(null, true);

                try {

                    configuration = new ControllerConfiguration().getInitialConfiguration();
                    message = (String) new JMSProccessor().sendReceive("start collector", configuration.getMasterShaperServer(), "srs", configuration.getJmsTimeWaitMessage(), new PieceUtilities().subPieceToHashMap(sp));

                    alert.setLabel(message);
                    alert.setVisible(true);

                } catch (Exception e) {
                    notifications.error("error sending start signal", e);
                }

                return null;
            }
        }.execute();

    }//GEN-LAST:event_jMenuItemStartCollectorActionPerformed

    //==========================================================================
    private void jMenuItemOverviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOverviewActionPerformed
        
        try {
            ControllerModelerInformer cmi = new ControllerModelerInformer("overview");
            cmi.setupInterface();
            cmi.setVisible(true);
        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }
    }//GEN-LAST:event_jMenuItemOverviewActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemAbout;
    private javax.swing.JMenuItem jMenuItemChain;
    private javax.swing.JMenuItem jMenuItemCloseAll;
    private javax.swing.JMenuItem jMenuItemConfiguration;
    private javax.swing.JMenuItem jMenuItemE2E;
    private javax.swing.JMenuItem jMenuItemEventViewer;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemFilters;
    private javax.swing.JMenuItem jMenuItemIP;
    private javax.swing.JMenuItem jMenuItemLoad;
    private javax.swing.JMenuItem jMenuItemLoadDebug;
    private javax.swing.JMenuItem jMenuItemModelerBandwidth;
    private javax.swing.JMenuItem jMenuItemModelerChains;
    private javax.swing.JMenuItem jMenuItemModelerPipes;
    private javax.swing.JMenuItem jMenuItemOptions;
    private javax.swing.JMenuItem jMenuItemOverview;
    private javax.swing.JMenuItem jMenuItemPipes;
    private javax.swing.JMenuItem jMenuItemPort;
    private javax.swing.JMenuItem jMenuItemPorts;
    private javax.swing.JMenuItem jMenuItemProtocols;
    private javax.swing.JMenuItem jMenuItemReport;
    private javax.swing.JMenuItem jMenuItemServiceLevel;
    private javax.swing.JMenuItem jMenuItemShow;
    private javax.swing.JMenuItem jMenuItemStartCollector;
    private javax.swing.JMenuItem jMenuItemStopCollector;
    private javax.swing.JMenuItem jMenuItemSubnet;
    private javax.swing.JMenuItem jMenuItemTargets;
    private javax.swing.JMenuItem jMenuItemUnload;
    private javax.swing.JMenu jMenuManage;
    private javax.swing.JMenu jMenuModeler;
    private javax.swing.JMenu jMenuModelerSettings;
    private javax.swing.JMenu jMenuModelerTools;
    private javax.swing.JMenu jMenuMonitoring;
    private javax.swing.JMenu jMenuSearch;
    private javax.swing.JMenu jMenuSettings;
    private javax.swing.JMenu jMenuTools;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPaneMain;
    private javax.swing.JTabbedPane jTabbedPaneCollectors;
    private javax.swing.JTabbedPane jTabbedPaneView;
    // End of variables declaration//GEN-END:variables
}
