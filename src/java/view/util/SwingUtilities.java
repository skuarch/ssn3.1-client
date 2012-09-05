package view.util;

import controllers.ControllerCollectors;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import model.beans.Collectors;
import view.notifications.Notifications;

/**
 *
 * @author skuarch
 */
public class SwingUtilities {

    //==========================================================================
    public static JButton getShutdownButton() {

        JButton button = null;

        try {

            button = new JButton();
            button.setIcon(new ImageIcon(SwingUtilities.class.getResource("/view/images/shutdown.png")));
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);

        } catch (Exception e) {
            new Notifications().error("error creating button shutdown", e);
        }

        return button;
    } // end getShutdownButton

    //==========================================================================
    public static JButton getEnableButton() {
        JButton button = null;

        try {

            button = new JButton();
            button.setIcon(new ImageIcon(SwingUtilities.class.getResource("/view/images/ok.png")));
            //button.setHorizontalTextPosition(SwingConstants.CENTER);
            //button.setVerticalTextPosition(SwingConstants.BOTTOM);

        } catch (Exception e) {
            new Notifications().error("error creating button enable", e);
        }

        return button;
    } // end getEnableButton

    //==========================================================================
    public static JButton getEditButton() {
        JButton button = null;

        try {

            button = new JButton();
            button.setIcon(new ImageIcon(SwingUtilities.class.getResource("/view/images/edit.png")));
            //button.setHorizontalTextPosition(SwingConstants.CENTER);
            ///button.setVerticalTextPosition(SwingConstants.BOTTOM);

        } catch (Exception e) {
            new Notifications().error("error creating button edit", e);
        }

        return button;
    } // end getEditButton

    //==========================================================================
    public static JButton getDeleteButton() {
        JButton button = null;

        try {

            button = new JButton();
            button.setIcon(new ImageIcon(SwingUtilities.class.getResource("/view/images/delete.png")));
            //button.setHorizontalTextPosition(SwingConstants.CENTER);
            //button.setVerticalTextPosition(SwingConstants.BOTTOM);

        } catch (Exception e) {
            new Notifications().error("error creating button delete", e);
        }

        return button;
    } // end getDeleteButton

    //==========================================================================
    public static JButton getCleanButton() {

        JButton button = null;

        try {

            button = new JButton();
            button.setIcon(new ImageIcon(SwingUtilities.class.getResource("/view/images/clear.png")));
            button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            button.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        } catch (Exception e) {
            new Notifications().error("error creating button clear", e);
        }

        return button;
    } // end getClearButton

    //==========================================================================
    public static JButton getOnTopButton() {

        JButton button = null;

        try {

            button = new JButton();
            button.setIcon(new ImageIcon(SwingUtilities.class.getResource("/view/images/onTop.png")));
            button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            button.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        } catch (Exception e) {
            new Notifications().error("error creating button on top", e);
        }

        return button;
    } // end getOnTopButton

    //==========================================================================
    public static JButton getMailButton() {

        JButton button = null;

        try {

            button = new JButton();
            button.setIcon(new ImageIcon(SwingUtilities.class.getResource("/view/images/forward.png")));
            button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            button.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        } catch (Exception e) {
            new Notifications().error("error creating button mail", e);
        }

        return button;
    } // end getMailButton

    //==========================================================================
    public static JButton getRefreshButton() {

        JButton button = null;

        try {

            button = new JButton();
            button.setIcon(new ImageIcon(SwingUtilities.class.getResource("/view/images/refresh.png")));
            button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            button.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        } catch (Exception e) {
            new Notifications().error("error creating button refresh", e);
        }

        return button;
    } // end getRefreshButton

    //==========================================================================
    public static JButton getFloatButton() {
        JButton buttonFloat = new JButton();
        buttonFloat.setPreferredSize(new Dimension(10, 10));
        buttonFloat.setBackground(Color.blue);
        return buttonFloat;
    } // end getFloatButton

    //==========================================================================
    public JButton getNextButton() {
        JButton next = new JButton();
        next.setPreferredSize(new Dimension(25, 25));
        next.setIcon(new ImageIcon(getClass().getResource("/view/images/next.png")));
        return next;
    } // end getNextButton

    //==========================================================================
    public JButton getBackButton() {
        JButton back = new JButton();
        back.setPreferredSize(new Dimension(25, 25));
        back.setIcon(new ImageIcon(getClass().getResource("/view/images/back.png")));
        return back;
    } // end getBackButton

    //==========================================================================
    public JButton getOpenButton() {
        JButton open = new JButton();
        open.setPreferredSize(new Dimension(18, 18));
        open.setIcon(new ImageIcon(getClass().getResource("/view/images/newWindow.png")));
        return open;
    } // end getOpenButton

    //==========================================================================
    public static JLabel getWhiteLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.BLUE);
        return label;
    } // end getWhiteLabel

    //==========================================================================
    public JButton getExportExcelButton() {
        JButton export = new JButton("");
        export.setToolTipText("export to excel");
        export.setIcon(new ImageIcon(getClass().getResource("/view/images/table-export.png")));
        return export;
    }

    //==========================================================================
    public JButton getTableButton() {
        JButton table = new JButton("");
        table.setToolTipText("show table");
        table.setIcon(new ImageIcon(getClass().getResource("/view/images/table.png")));
        return table;
    }

    //==========================================================================
    public JButton getDetailButton() {

        JButton detail = new JButton("");
        detail.setToolTipText("details");
        detail.setIcon(new ImageIcon(getClass().getResource("/view/images/detail.png")));

        return detail;
    }

    //==========================================================================
    public JButton getPdfButton() {

        JButton pdf = new JButton("");
        pdf.setToolTipText("pdf");
        pdf.setIcon(new ImageIcon(getClass().getResource("/view/images/pdf.png")));
        return pdf;
    } // end getPdfButton

    //==========================================================================
    public static JList getListLineChart() {
        JList list = new JList();
        list.setLayoutOrientation(JList.VERTICAL_WRAP);
        list.setVisibleRowCount(-1);
        list.setBackground(Color.WHITE);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        return list;
    } // end getListVertical

    //==========================================================================
    public static JList getListBarChart() {
        JList list = new JList();
        list.setVisibleRowCount(-1);
        list.setBackground(Color.WHITE);
        return list;
    } // end getListHorinzontal

    //==========================================================================
    public DefaultComboBoxModel getDefaultComboBoxModelCollectors() {

        DefaultComboBoxModel defaultComboBoxModel = null;
        String[] collectors = new String[]{"no collectors"};
        Collectors[] servers = null;

        try {

            servers = new ControllerCollectors().getActivesCollectorsArray();

            if (servers.length > 0) {

                collectors = new String[servers.length + 1];
                collectors[0] = "select a collector";

                for (int i = 1; i < collectors.length; i++) {
                    collectors[i] = servers[i - 1].getHost();
                }

            } else {

                collectors = new String[]{"no collectors"};

            }

        } catch (Exception e) {
            new Notifications().error("error creation combobox collectors", e);
        } finally {
            return defaultComboBoxModel = new DefaultComboBoxModel(collectors);
        }

    } // end getComboBoxServers

    //==========================================================================
    public boolean validateNetMask(String netMask) {

        boolean flag = false;
        int prefix = 0;

        try {

            prefix = Integer.parseInt(netMask);

            if (prefix > 32 || prefix < 0) {
                flag = false;
            } else {
                flag = true;
            }

        } catch (NumberFormatException nfe) {
            return false;
        } catch (Exception e) {
            new Notifications().error("error validating subnet", e);
        }

        return flag;

    } // end validateSubnet

    //==========================================================================
    public Object[] getDataJList(JList jList) throws Exception {

        Object[] objects = null;
        ListModel lm = null;

        try {

            lm = jList.getModel();
            objects = new Object[lm.getSize()];

            for (int i = 0; i < lm.getSize(); i++) {
                objects[i] = lm.getElementAt(i);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            lm = null;
        }

        return objects;

    } // end getDataList

    //==========================================================================
    public void passItemsLeftRigth(JList left, JList rigth) throws Exception {

        Object[] objectsLeft = null;
        Object[] objectsAllLeft = null;
        Object[] objectsRight = null;
        DefaultListModel defaultListModelRight = new DefaultListModel();
        DefaultListModel defaultListModelLeft = new DefaultListModel();

        try {

            objectsLeft = left.getSelectedValues();
            objectsRight = new Object[rigth.getModel().getSize()];

            //keep right data
            for (int i = 0; i < objectsRight.length; i++) {
                defaultListModelRight.addElement(rigth.getModel().getElementAt(i));
            }

            //add new data to right
            for (int i = 0; i < objectsLeft.length; i++) {
                defaultListModelRight.addElement(objectsLeft[i]);
            }

            rigth.clearSelection();
            rigth.setModel(defaultListModelRight);

            //drop off selected data in left
            objectsAllLeft = new Object[left.getModel().getSize()];
            for (int i = 0; i < left.getModel().getSize(); i++) {
                objectsAllLeft[i] = left.getModel().getElementAt(i);
            }

            for (int i = 0; i < objectsAllLeft.length; i++) {

                for (int o = 0; o < objectsLeft.length; o++) {
                    if (objectsLeft[o].equals(objectsAllLeft[i])) {
                        continue;
                    } else {
                        defaultListModelLeft.addElement(objectsAllLeft[i]);
                    }
                }

            }

            if (objectsLeft.length > 0) {
                left.setModel(defaultListModelLeft);
            }

        } catch (Exception e) {
            throw e;
        }

    } // end passItemsJlist

    //==========================================================================
    public void fillJlist(JList list, Object[] objects) throws Exception {

        DefaultListModel dlm = new DefaultListModel();

        try {

            list.removeAll();

            for (Object objs : objects) {
                dlm.addElement(objs);
            }

            list.setModel(dlm);
            list.repaint();

        } catch (Exception e) {
            throw e;
        }

    } // end fillJlist

    //==========================================================================
    public void fillJComboBox(JComboBox jComboBox, Object[] objects) throws Exception {

        try {

            for (Object object : objects) {
                jComboBox.addItem(object);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            objects = null;
        }

    } // end fillJComboBox

    //==========================================================================
    public void deleteTableRow(int row, JTable table) {

        DefaultTableModel model = null;

        try {
            model = (DefaultTableModel) table.getModel();

            if (model != null) {
                model.removeRow(row);
                model.fireTableDataChanged();
                table.repaint();
            }
        } catch (Exception e) {
            new Notifications().error("error deleting row", e);
        }

    } // end deleteTableRow
    
    //==========================================================================
    /**
     * null the current table model and set the new model.
     *
     * @param tableModel TableModel
     * @throws Exception
     */
    public void setTableModel(DefaultTableModel tableModel,JTable table) throws Exception {

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
    public void setTableModel(Object[][] data, String[] columnNames, JTable table) throws Exception {

        if(columnNames == null){
            throw new NullPointerException("columNames is null");
        }
        
        if (data == null) {
            throw new NullPointerException("data is null");
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
    
    
} // end class
