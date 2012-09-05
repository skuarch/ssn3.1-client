package controllers.modeler;

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
public class ControllerModelerTableSettings extends Modeler {

    private ModelerTable modelerTable = new ModelerTable(null, true);
    private String type = null;

    //==========================================================================
    public ControllerModelerTableSettings(String type) throws Exception {
        super();
        this.type = type;
    } // end ControllerModelerTableSettings

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
                    
                    if(arrayList == null || arrayList.size() < 1){
                        return null;
                    }

                    objects = (Object[][]) arrayList.get(0);
                    
                    if (objects == null) {                        
                        return null;
                    }

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

    } // setupInterface

    //==========================================================================
    @Override
    public void setVisible(boolean flag) throws Exception {
        try {
            modelerTable.setVisible(true);
        } catch (Exception e) {
            notifications.error(e.toString(), e);
        }
    } // setVisible

    //==========================================================================
    @Override
    public void setEnabledComponents(boolean flag) throws Exception {
        modelerTable.getjTable().setEnabled(flag);
    } // setEnabledComponents

    //==========================================================================
    @Override
    public void addListenersButtons() throws Exception {

        //close ----------------------------------------------------------------
        modelerTable.getjButton1().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                modelerTable.setVisible(false);
            }
        });

        //create ---------------------------------------------------------------
        modelerTable.getjButton3().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                try {

                    // open window create target, ports ...
                    if (type.equalsIgnoreCase("targets")) {

                        ControllerCreateTargets cct = new ControllerCreateTargets();
                        cct.setupInterface();
                        modelerTable.setVisible(false);
                        cct.setVisible(true);

                    } else if (type.equalsIgnoreCase("ports")) {

                        ControllerCreatePorts ccp = new ControllerCreatePorts();
                        ccp.setupInterface();
                        modelerTable.setVisible(false);
                        ccp.setVisible(true);

                    } else if (type.equalsIgnoreCase("protocols")) {

                        ControllerCreateProtocols ccp = new ControllerCreateProtocols();
                        ccp.setupInterface();
                        modelerTable.setVisible(false);
                        ccp.setVisible(true);

                    } else if (type.equalsIgnoreCase("service level")) {

                        ControllerCreateServiceLevel ccsl = new ControllerCreateServiceLevel();
                        ccsl.setupInterface();
                        modelerTable.setVisible(false);
                        ccsl.setVisible(true);

                    }

                } catch (Exception ex) {
                    notifications.error(ex.getMessage(), ex);
                }

            }
        });

    } // end addListenersButtons

    //==========================================================================
    @Override
    public void actionButtonCreateInWindow() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    } // end actionButtonCreateInWindow

    //==========================================================================
    @Override
    public void actionButtonEditInTable() throws Exception {

        setEnabledComponents(false);
        
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                int row = 0;
                Object id = null;

                try {

                    row = modelerTable.getjTable().convertRowIndexToModel(modelerTable.getjTable().getEditingRow());
                    id = modelerTable.getjTable().getModel().getValueAt(row, 0);                    

                    if (type.equals("targets")) {

                        ControllerEditTargets cet = new ControllerEditTargets(id.toString());
                        cet.setupInterface();
                        modelerTable.setVisible(false);
                        cet.setVisible(true);

                    } else if (type.equals("ports")) {

                        ControllerEditPort cep = new ControllerEditPort(id.toString());
                        cep.setupInterface();
                        modelerTable.setVisible(false);
                        cep.setVisible(true);

                    } else if (type.equals("protocols")) {

                        ControllerEditProtocols cep = new ControllerEditProtocols(id.toString());
                        cep.setupInterface();
                        modelerTable.setVisible(false);
                        cep.setVisible(true);

                    } else if (type.equalsIgnoreCase("service level")) {

                        ControllerEditServiceLevel cesl = new ControllerEditServiceLevel(id.toString());
                        cesl.setupInterface();
                        modelerTable.setVisible(false);
                        cesl.setVisible(true);

                    }

                } catch (Exception e) {
                    new Notifications().error("error", e);
                    new Thrower().exception(e);
                }finally{
                    setEnabledComponents(true);
                }

                return null;
            }
        }.execute();
    } // end actionButtonEditInTable

    //==========================================================================
    @Override
    public void setupButtonsInWindow() throws Exception {
        modelerTable.getjButton1().setText("close");
        modelerTable.getjButton3().setText("create " + type);
    } // end setupButtonsInWindow

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

                //button edit --------------------------------------------------
                buttonsEditor.addListenerButtonEdit(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        try {
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

    } // end putButtonsTable

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

    } // end actionButtonDeleteInTable
} // end class
