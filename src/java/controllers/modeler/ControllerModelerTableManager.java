package controllers.modeler;

import controllers.ControllerEditFilter;
import controllers.ControllerNewChain;
import controllers.ControllerNewFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.table.TableColumn;
import model.util.Thrower;
import view.dialogs.modeler.ModelerTable;
import view.notifications.Notifications;
import view.tables.modeler.ButtonsEditor;
import view.tables.modeler.ButtonsRenderer;

/**
 *
 * @author skuarch
 */
public class ControllerModelerTableManager extends Modeler {

    private ModelerTable modelerTable = new ModelerTable(null, true);
    private String type = null;

    //==========================================================================
    public ControllerModelerTableManager(String type) throws Exception {
        super();
        this.type = type;
    } // end ControllerModelerTableManager

    //==========================================================================
    @Override
    public void setupInterface() throws Exception {

        modelerTable.getjProgressBar().setIndeterminate(true);
        setEnabledComponents(false);

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                ArrayList arrayList = null;
                Object[][] objects = null;

                try {

                    modelerTable.setTitle(type);
                    setupButtonsInWindow();
                    addListenersButtons();

                    arrayList = requestAllData(type);
                    objects = (Object[][]) arrayList.get(0);

                    if (objects != null) {                        
                        setTableModel(objects, getColumnNames(type), modelerTable.getjTable());
                        putButtonsTable(); //add delete and edit buttons
                    }


                } catch (Exception e) {
                    notifications.error(e.getMessage(), e);
                } finally {
                    modelerTable.getjProgressBar().setIndeterminate(false);
                    setEnabledComponents(true);
                }

                return null;
            }
        }.execute();
    }

    //==========================================================================
    @Override
    public void setVisible(boolean flag) throws Exception {

        try {
            modelerTable.setVisible(flag);
        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }

    }

    //==========================================================================
    @Override
    public void setEnabledComponents(boolean flag) throws Exception {
        modelerTable.getjTable().setEnabled(flag);
    }

    //==========================================================================
    @Override
    public void addListenersButtons() throws Exception {
        //buttons in window

        try {

            //close ------------------------------------------------------------
            modelerTable.getjButton1().addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    modelerTable.setVisible(false);
                }
            });

            //create -----------------------------------------------------------
            modelerTable.getjButton3().addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    try {

                        if (type.equals("chains")) {

                            ControllerNewChain cnc = new ControllerNewChain();
                            modelerTable.setVisible(false);
                            cnc.setInterface();
                            cnc.isStatusActive(true);
                            cnc.setActionListenerButtonSave();
                            cnc.setVisible(true);


                        } else if (type.equals("pipes")) {
                            
                            //open create new pipe
                            ControllerCreatePipes ccp = new ControllerCreatePipes();                            
                            ccp.setupInterface();
                            modelerTable.setVisible(false);
                            ccp.setVisible(true);


                        } else if (type.contains("filters")) {
                            //open create new filter
                            ControllerNewFilter cnf = new ControllerNewFilter();
                            cnf.setupInterface();
                            modelerTable.setVisible(false);
                            cnf.setVisible(true);

                        }

                    } catch (Exception ex) {
                        notifications.error(ex.getMessage(), ex);
                    }

                }
            });

        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }

    }

    //==========================================================================
    @Override
    public void actionButtonCreateInWindow() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //==========================================================================
    @Override
    public void actionButtonEditInTable() throws Exception {
        System.out.println("no ammes");
        new SwingWorker<Void, Void>() {

            
            @Override
            protected Void doInBackground() throws Exception {
System.out.println("entrando");
                int row = 0;
                Object id = null;
                ArrayList arrayList = null;

                try {

                    row = modelerTable.getjTable().convertRowIndexToModel(modelerTable.getjTable().getEditingRow());
                    id = modelerTable.getjTable().getModel().getValueAt(row, 0);

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

                return null;
            }
        }.execute();
    }

    //==========================================================================
    @Override
    public void actionButtonDeleteInTable() throws Exception {
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                int row = 0;
                Object id = null;
                ArrayList arrayList = null;

                try {

                    row = modelerTable.getjTable().convertRowIndexToModel(modelerTable.getjTable().getEditingRow());
                    id = modelerTable.getjTable().getModel().getValueAt(row, 0);

                    int response = JOptionPane.showConfirmDialog(modelerTable, "delete " + type + " " + id + "?");

                    if (response == 0) {

                        setEnabledComponents(false);
                        modelerTable.getjProgressBar().setIndeterminate(true);

                        arrayList = deleteGeneric(id.toString(), type);

                        if (arrayList.get(0).equals(true)) {
                            //delete row in table                            
                            deleteRow(row, modelerTable.getjTable());
                        } else {
                            //show message
                            new Notifications().error("In this moment is imposible delete " + type, new Exception());
                        }
                    }

                } catch (Exception e) {
                    throw e;
                } finally {
                    setEnabledComponents(true);
                    modelerTable.getjProgressBar().setIndeterminate(false);
                }

                return null;
            }
        }.execute();
    }

    //==========================================================================
    @Override
    public void setupButtonsInWindow() throws Exception {
        modelerTable.getjButton1().setText("close");
        modelerTable.getjButton3().setText("create " + type);
    }

    //==========================================================================
    @Override
    public void putButtonsTable() throws Exception {

        ButtonsEditor buttonsEditor = null;
        ButtonsRenderer buttonsRenderer = null;

        try {

            if (modelerTable.getjTable().getRowCount() > 0) {

                TableColumn column = null;
                buttonsEditor = new ButtonsEditor(modelerTable.getjTable());
                buttonsRenderer = new ButtonsRenderer();

                column = modelerTable.getjTable().getColumnModel().getColumn(getColumnNames(type).length - 1);
                column.setCellRenderer(buttonsRenderer);
                column.setCellEditor(buttonsEditor);
                modelerTable.getjTable().setRowHeight(43); //size of row, it's necesary

                //button delete-----------------------------------------------------
                buttonsEditor.addListenerButtonDelete(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        try {
                            actionButtonDeleteInTable();
                        } catch (Exception ex) {
                            new Thrower().exception(ex);
                        }
                    }
                });

                //button editChain--------------------------------------------------
                buttonsEditor.addListenerButtonEdit(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        try {
                            System.out.println("el action");
                            actionButtonEditInTable();
                        } catch (Exception ex) {
                            new Thrower().exception(ex);
                        }
                    }
                });

            } // end if

        } catch (Exception e) {
            throw e;
        }
    } //end putButtonsTable
} // end class

