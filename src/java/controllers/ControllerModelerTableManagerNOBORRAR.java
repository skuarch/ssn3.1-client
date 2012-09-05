package controllers;

import controllers.modeler.ControllerCreatePipes;
import controllers.modeler.ControllerEditPipes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import model.beans.Configuration;
import model.beans.SubPiece;
import model.jms.JMSProccessor;
import model.util.PieceUtilities;
import model.util.Thrower;
import view.dialogs.modeler.ModelerTable;
import view.notifications.Notifications;
import view.tables.modeler.ButtonsEditor;
import view.tables.modeler.ButtonsRenderer;

/**
 *
 * @author skuarch
 */
public class ControllerModelerTableManagerNOBORRAR {

    private ModelerTable genericTable = new ModelerTable(null, true);
    private String type = null;
    private Configuration configuration = null;
    private JTable table = null;
    private JButton button1 = null;
    private JButton button3 = null;
    private JProgressBar progressBar = null;
    private ButtonsEditor buttonsEditor = null;
    private ButtonsRenderer buttonsRenderer = null;

    //==========================================================================
    /**
     * create a instance.
     *
     * @param type String example chains, filters, pipes [always in plural]
     */
    public ControllerModelerTableManagerNOBORRAR(String type) throws Exception {
        this.type = type;
        configuration = new ControllerConfiguration().getInitialConfiguration();
        table = genericTable.getjTable();
        button1 = genericTable.getjButton1();
        button3 = genericTable.getjButton3();
        progressBar = genericTable.getjProgressBar();
        buttonsEditor = new ButtonsEditor(table);
        buttonsRenderer = new ButtonsRenderer();
    } // end ControllerModelerTableManager

    //==========================================================================
    /**
     * request the initial data, create a TableModel and JTable. <br /> this
     * method disable the table while is executing
     *
     * @throws Exception
     */
    public void setupInterface() throws Exception {

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                setEnabledComponents(false);
                ArrayList arrayList = null;
                Object[][] objects = null;

                try {

                    //basic configuration
                    genericTable.setTitle(type);
                    button1.setText("close");
                    button3.setText("create " + type);
                    addListenersButtons(); //add listeners for buttons

                    //add new data to table
                    arrayList = requestData();
                    objects = (Object[][]) arrayList.get(0);

                    if (objects != null) {
                        setTableModel(objects, getColumnNames());
                        putButtonsTable(); //add delete and edit buttons
                    }

                } catch (Exception e) {
                    throw e;
                } finally {
                    arrayList = null;
                    setEnabledComponents(true);
                }

                return null;
            }
        }.execute();

    } // end setupInterface

    //==========================================================================
    /**
     * add buttons to table
     */
    private void putButtonsTable() throws Exception {

        try {

            if (table.getRowCount() > 0) {

                TableColumn column = null;
                column = table.getColumnModel().getColumn(getColumnNames().length - 1);
                column.setCellRenderer(buttonsRenderer);
                column.setCellEditor(buttonsEditor);
                table.setRowHeight(43); //size of row, it's necesary

                //button delete-----------------------------------------------------
                buttonsEditor.addListenerButtonDelete(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        try {
                            delete();
                        } catch (Exception ex) {
                            new Thrower().exception(ex);
                        }
                    }
                });

                //button editChain--------------------------------------------------
                buttonsEditor.addListenerButtonEdit(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        try {
                            edit();
                        } catch (Exception ex) {
                            new Thrower().exception(ex);
                        }
                    }
                });

            } // end if

        } catch (Exception e) {
            throw e;
        }

    }//put buttons to table 

    //==========================================================================
    private void delete() throws Exception {

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                HashMap hashMap = null;
                int row = 0;
                Object id = null;
                ArrayList arrayList = null;
                boolean deleted = false;

                try {

                    row = table.convertRowIndexToModel(table.getEditingRow());
                    id = table.getModel().getValueAt(row, 0);

                    hashMap = new PieceUtilities().subPieceToHashMap(new SubPiece());
                    hashMap.put("delete " + type, id);

                    int response = JOptionPane.showConfirmDialog(genericTable, "delete " + type + " " + id + "?");

                    if (response == 0) {

                        setEnabledComponents(false);
                        progressBar.setIndeterminate(true);

                        //delete row from master shaper
                        arrayList = (ArrayList) new JMSProccessor().sendReceive(
                                type,
                                configuration.getMasterShaperServer(),
                                "srs",
                                configuration.getJmsTimeWaitMessage(),
                                hashMap);

                        deleted = (Boolean) arrayList.get(0);

                        if (deleted) {
                            //delete row in table                            
                            deleteRow(row);
                        } else {
                            //show message
                            new Notifications().error("In this moment is imposible delete " + type, new Exception());
                        }
                    }

                } catch (Exception e) {
                    throw e;
                } finally {
                    setEnabledComponents(true);
                    progressBar.setIndeterminate(false);
                }

                return null;
            }
        }.execute();

    } // end delete

    //==========================================================================
    private void edit() throws Exception {

        new Thread(new Runnable() {

            public void run() {

                int row = 0;
                Object id = null;
                ArrayList arrayList = null;

                try {
                    
                    row = table.convertRowIndexToModel(table.getEditingRow());
                        id = table.getModel().getValueAt(row, 0);

                    if (type.equals("chains")) {

                        ControllerNewChain cnc = new ControllerNewChain();
                        arrayList = cnc.requestDataChain(id.toString());
                        cnc.setInterface(arrayList);
                        cnc.setActionListenerButtonSave();
                        setVisible(false);
                        cnc.setVisible(true);

                    } else if (type.equals("filters")) {

                        ControllerEditFilter cef = new ControllerEditFilter(id.toString());
                        cef.setupInterface();
                        setVisible(false);
                        cef.setVisible(true);

                    } else if (type.equals("pipes")) {
                        
                        ControllerEditPipes cep = new ControllerEditPipes(id.toString());
                        cep.setupInterface();
                        setVisible(false);
                        cep.setVisible(true);
                        
                    }

                } catch (Exception e) {
                    new Notifications().error("error", e);
                    new Thrower().exception(e);
                }
            }
        }).start();


    } //end delete

    //==========================================================================
    /**
     * delete row in table.
     *
     * @param row int
     * @throws Exception
     */
    private void deleteRow(int row) throws Exception {

        DefaultTableModel model = null;

        try {
            model = (DefaultTableModel) table.getModel();

            if (model != null) {
                model.removeRow(row);
                model.fireTableDataChanged();
                table.repaint();
            }

        } catch (Exception e) {
            throw e;
        }
    }

    //==========================================================================
    /**
     * add Actions Listeners to the buttons.
     *
     * @throws Exception
     */
    private void addListenersButtons() throws Exception {

        try {

            //close for evething
            button1.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    genericTable.setVisible(false);
                }
            });

            //opening new windows, depends of type
            button3.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    if (type.equals("chains")) {

                        try {

                            ControllerNewChain cnc = new ControllerNewChain();
                            setVisible(false);
                            cnc.setInterface();
                            cnc.isStatusActive(true);
                            cnc.setActionListenerButtonSave();
                            cnc.setVisible(true);

                        } catch (Exception ex) {
                            new Thrower().exception(ex);
                        }

                    } else if (type.equals("pipes")) {
                        //open create new pipe
                        try {
                            ControllerCreatePipes ccp = new ControllerCreatePipes();
                            ccp.setupInterface();
                            setVisible(false);
                            ccp.setVisible(true);
                        } catch (Exception ex) {
                            new Thrower().exception(ex);
                        }
                        
                        
                    } else if (type.contains("filters")) {
                        //open create new filter
                        try {

                            ControllerNewFilter cnf = new ControllerNewFilter();
                            cnf.setupInterface();
                            genericTable.setVisible(false);
                            cnf.setVisible(true);

                        } catch (Exception ex) {
                            new Thrower().exception(ex);
                        }
                    }

                }
            });

        } catch (Exception e) {
            throw e;
        }

    } // end setButtonsListeners

    //==========================================================================
    /**
     * request data with [view] getAll
     *
     * @return ArrayList
     * @throws Exception
     */
    public ArrayList requestData() throws Exception {

        ArrayList arrayList = null;
        progressBar.setIndeterminate(true);
        HashMap hashMap = null;

        try {

            hashMap = new PieceUtilities().subPieceToHashMap(new SubPiece());
            hashMap.put("getAllData", type);
            System.out.println("viejo: " + type);
            arrayList = (ArrayList) new JMSProccessor().sendReceive(
                    type,
                    configuration.getMasterShaperServer(),
                    "srs",
                    configuration.getJmsTimeWaitMessage(),
                    hashMap);

        } catch (Exception e) {
            throw e;
        } finally {
            progressBar.setIndeterminate(false);
        }

        return arrayList;

    } // end requestData

    //==========================================================================
    /**
     * null the current table model and set the new model.
     *
     * @param tableModel TableModel
     * @throws Exception
     */
    public void setTableModel(DefaultTableModel tableModel) throws Exception {

        if (tableModel == null) {
            throw new NullPointerException("tableModel is null");
        }

        TableModel tmpModel = null;

        try {

            tmpModel = table.getModel();
            tmpModel = null;
            table.setModel(tableModel);

            tableModel.fireTableDataChanged();
            table.repaint();

        } catch (Exception e) {
            throw e;
        }

    } // end setTableModel

    //==========================================================================
    /**
     * null the current table model and set the new model.
     *
     * @param data Object[][]
     * @param columnNames String[]
     * @throws Exception
     */
    public void setTableModel(Object[][] data, String[] columnNames) throws Exception {

        if (data == null || columnNames == null) {
            throw new NullPointerException("data is null or columNames");
        }

        TableModel tmpModel = null;
        DefaultTableModel dtm = null;

        try {

            tmpModel = table.getModel();
            tmpModel = null;
            dtm = new DefaultTableModel(data, columnNames);
            table.setModel(dtm);
            dtm.fireTableDataChanged();
            table.repaint();

        } catch (Exception e) {
            throw e;
        }

    } // end setTableModel

    //==========================================================================
    /**
     * default names for chains.
     *
     * @return String[]
     */
    public String[] getColumnNames() throws Exception {

        String[] columnNames = null;

        try {

            if (type.equals("chains")) {
                columnNames = new String[]{"id", "Chain name", "Service level", "Fallback", "Status", "Options"};
            } else if (type.equals("pipes")) {
                columnNames = new String[]{"id", "Pipes", "Chains", "Filters","Status", "Options"};
            } else if (type.equals("filters")) {
                columnNames = new String[]{"id", "Filters", "Status", "Options"};
            }


        } catch (Exception e) {
            throw e;
        }

        return columnNames;

    } // end getColumnNames

    //==========================================================================
    /**
     * set enabled or disabled components like table and button refresh.
     *
     * @param flag boolean
     */
    public void setEnabledComponents(boolean flag) {
        table.setEnabled(flag);
    } // end setEnabledComponents

    //==========================================================================
    /**
     * show or hides the window.
     *
     * @param flag boolean
     */
    public void setVisible(boolean flag) {
        genericTable.setVisible(flag);
    } // end setVisible
} 