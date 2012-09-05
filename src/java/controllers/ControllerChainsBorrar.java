package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import model.beans.Configuration;
import model.beans.SubPiece;
import model.jms.JMSProccessor;
import model.util.PieceUtilities;
import model.util.Thrower;
import view.dialogs.modeler.GenericBorrar;
import view.tables.modeler.ButtonsEditor;
import view.tables.modeler.ButtonsRenderer;

/**
 *
 *
 * @author skuarch
 */
public class ControllerChainsBorrar {

    private GenericBorrar generic1 = new GenericBorrar(null, true);
    private JTable table = generic1.getTable();
    private ButtonsEditor buttonsEditor = new ButtonsEditor(table);
    private ButtonsRenderer buttonsRenderer = new ButtonsRenderer();
    private Configuration configuration = null;

    //==========================================================================
    public ControllerChainsBorrar() throws Exception {
        configuration = new ControllerConfiguration().getInitialConfiguration();
    } // end ControllerChainsBorrar

    //==========================================================================
    public void createTable() throws Exception {

        if (table.getRowCount() > 0) {

            TableColumn column = table.getColumnModel().getColumn(5);
            column.setCellRenderer(buttonsRenderer);
            column.setCellEditor(buttonsEditor);

            //button delete---------------------------------------------------------
            buttonsEditor.addListenerButtonDelete(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        delete();
                    } catch (Exception ex) {
                        new Thrower().exception(ex);
                    }
                }
            });

            //button editChain-----------------------------------------------------------
            buttonsEditor.addListenerButtonEdit(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        editChain();
                    } catch (Exception ex) {
                        new Thrower().exception(ex);
                    }
                }
            });

        } // end if

    } // end createTable

    //==========================================================================
    private void editChain() throws Exception {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                int row = table.convertRowIndexToModel(table.getEditingRow());
                Object id = table.getModel().getValueAt(row, 0);
                SubPiece subPiece = new SubPiece();
                subPiece.setView("getChain");
                subPiece.setCollector(configuration.getMasterShaperServer());
                //subPiece.setIdChain(id.toString());

                try {

                    generic1.setEnabled(false);
                    generic1.setIndeterminateProgressBar(true);

                    ControllerNewChain cnc = new ControllerNewChain();
                    setVisible(false);
                    cnc.setInterface(cnc.requestDataChain(id.toString()));
                    cnc.setActionListenerButtonSave();
                    cnc.setVisible(true);

                } catch (Exception e) {
                    new Thrower().exception(e);
                }

            }
        });

    } // end editChain

    //==========================================================================
    private void delete() throws Exception {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                int row = table.convertRowIndexToModel(table.getEditingRow());
                Object id = table.getModel().getValueAt(row, 0);
                Object nameChain = table.getModel().getValueAt(row, 1);
                SubPiece subPiece = new SubPiece();
                subPiece.setView("deleteChain");
                subPiece.setCollector(configuration.getMasterShaperServer());
                //subPiece.setIdChain(id.toString());
                int response = JOptionPane.showConfirmDialog(generic1, "delete " + nameChain + "?");

                if (response == 0) {

                    try {

                        generic1.setEnabled(false);
                        generic1.setIndeterminateProgressBar(true);

                        // delete chain                        
                        new JMSProccessor().sendReceive(
                                "chains",
                                configuration.getMasterShaperServer(),
                                "srs", 1,
                                new PieceUtilities().subPieceToHashMap(subPiece));

                        ///delete row in table
                        deleteRow(row);

                    } catch (Exception e) {
                        new Thrower().exception(e);
                    } finally {
                        setEnableWindow(true);
                        setIndeterminateProgressBar(false);
                    }
                }
            }
        });
    } // end delete  

    //==========================================================================
    public void setInterface() throws Exception {

        new Thread(new Runnable() {

            public void run() {
                try {

                    setIndeterminateProgressBar(true);
                    generic1.setEnabled(false);
                    setTableModel(getDefaultColumnNamesChains(), requestChains());
                    createTable();

                } catch (Exception e) {
                    new Thrower().exception(e);
                } finally {
                    setIndeterminateProgressBar(false);
                    generic1.setEnabled(true);
                }
            }
        }).start();

    } // end setInterface

    //==========================================================================
    private void deleteRow(int row) throws Exception {

        DefaultTableModel model = null;

        try {
            model = (DefaultTableModel) table.getModel();

            if (model != null) {
                model.removeRow(row);
            }

        } catch (Exception e) {
            throw e;
        }
    }

    //==========================================================================
    public void setListenerButtonNew() throws Exception {

        generic1.addActionListenerButtonNew(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
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
            }
        });

    } // end setListenerButtonNew

    //==========================================================================
    public Object[][] requestChains() throws Exception {

        Object[][] objects = null;
        int time = 0;
        String masterShaperServer = null;
        SubPiece subPiece = new SubPiece();

        try {

            time = new ControllerConfiguration().getInitialConfiguration().getJmsTimeWaitMessage();
            masterShaperServer = new ControllerConfiguration().getInitialConfiguration().getMasterShaperServer();
            subPiece.setView("getChains");
            subPiece.setCollector(masterShaperServer);
            objects = (Object[][]) new JMSProccessor().sendReceive("chains", masterShaperServer, "srs", time, new PieceUtilities().subPieceToHashMap(subPiece));

        } catch (Exception e) {
            throw e;
        }

        return objects;

    }

    //==========================================================================
    public String[] getDefaultColumnNamesChains() {
        return new String[]{"id", "Chain name", "Service level", "Fallback", "Status", "Options"};
    }

    //==========================================================================
    public void setTableModel(final String[] columnNames, final Object[][] data) throws Exception {
        TableModel tm = (TableModel) generic1.getTable().getModel();
        tm = null;
        generic1.setTableModel(new DefaultTableModel(data, columnNames));
    } // end setTableModel

    //==========================================================================
    public void setEnableButtonNewChain(boolean flag) throws Exception {
        generic1.setEnableButtonNewChain(flag);
    }

    //==========================================================================
    public void setEnableButtonClose(boolean flag) throws Exception {
        generic1.setEnableButtonClose(flag);
    }

    //==========================================================================
    public void setAutoCreateRowSorter(boolean flag) throws Exception {
        generic1.getTable().setAutoCreateRowSorter(flag);
    }

    //==========================================================================
    public void setVisible(boolean flag) throws Exception {
        generic1.setVisible(flag);
    } // end setVisible

    //==========================================================================
    public void setRowHeight(int height) {
        table.setRowHeight(height);
    }

    //==========================================================================
    public void setEnableWindow(boolean flag) {
        generic1.setEnabled(flag);
    }

    //==========================================================================
    public void setIndeterminateProgressBar(boolean flag) {
        generic1.setIndeterminateProgressBar(flag);
    }
} 