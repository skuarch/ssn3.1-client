package controllers;

import controllers.modeler.ControllerModelerTableManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import model.beans.Configuration;
import model.beans.SubPiece;
import model.jms.JMSProccessor;
import model.util.PieceUtilities;
import model.util.Thrower;
import view.dialogs.modeler.GenericFilter;
import view.notifications.Notifications;

/**
 *
 * @author skuarch
 */
public class ControllerNewFilter {

    private Configuration configuration = null;
    private GenericFilter gf = new GenericFilter(null, true);
    private String id = null;

    //==========================================================================
    public ControllerNewFilter() throws Exception {
        configuration = new ControllerConfiguration().getInitialConfiguration();
    } // end ControllerNewFilter

    //==========================================================================
    public void setupInterface() throws Exception {

        gf.getjProgressBar().setIndeterminate(true);

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                ArrayList arrayList = null;
                String status = null;
                String[] protocols = null;
                String[] unused = null;
                String[] used = null;
                String[] tos = null;
                String[] source = null;
                String[] direction = null;
                String[] destination = null;

                try {

                    //basic configuration 
                    gf.getjButtonClose().setText("close");
                    gf.getjButtonSave().setText("save");
                    addListenerButtons();

                    //data
                    arrayList = requestData();

                    status = (String) arrayList.get(0);
                    protocols = (String[]) arrayList.get(1);
                    unused = (String[]) arrayList.get(2);
                    used = (String[]) arrayList.get(3);
                    tos = (String[]) arrayList.get(4);
                    source = (String[]) arrayList.get(5);
                    direction = (String[]) arrayList.get(6);
                    destination = (String[]) arrayList.get(7);

                    if (status.equalsIgnoreCase("true")) {
                        gf.getjRadioButtonActive().setSelected(true);
                    }

                    if (status.equalsIgnoreCase("false")) {
                        gf.getjRadioButtonInactive().setSelected(true);
                    }

                    gf.getjComboBoxProtocols().removeAllItems();
                    for (String string : protocols) {
                        gf.getjComboBoxProtocols().addItem(string);
                    }

                    fillJlist(gf.getjListUnused(), unused);

                    fillJlist(gf.getjListUsed(), used);

                    gf.getjComboBoxTos().removeAllItems();
                    for (String string : tos) {
                        gf.getjComboBoxTos().addItem(string);
                    }

                    gf.getjComboBoxSource().removeAllItems();
                    for (String string : source) {
                        gf.getjComboBoxSource().addItem(string);
                    }

                    gf.getjComboBoxDirection().removeAllItems();
                    for (String string : direction) {
                        gf.getjComboBoxDirection().addItem(string);
                    }

                    gf.getjComboBoxDestination().removeAllItems();
                    for (String string : destination) {
                        gf.getjComboBoxDestination().addItem(string);
                    }


                } catch (Exception e) {
                    throw e;
                } finally {
                    gf.getjProgressBar().setIndeterminate(false);
                }

                return null;
            }
        }.execute();

    } // end setupInterface

    //==========================================================================
    private void fillJlist(JList list, Object[] objects) throws Exception {

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
    private void passItemsLeftRigth(JList left, JList rigth) throws Exception {

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
    public void addListenerButtons() throws Exception {

        try {

            gf.getjButtonClose().addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    gf.setVisible(false);
                }
            });

            gf.getjButtonSave().addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    try {
                        save();
                    } catch (Exception ex) {
                        new Thrower().exception(ex);
                    }

                }
            });

            gf.getjButtonLeft().addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    try {
                        passItemsLeftRigth(gf.getjListUsed(), gf.getjListUnused());
                    } catch (Exception ex) {
                        new Thrower().exception(ex);
                    }
                }
            });

            gf.getjButtonRight().addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        passItemsLeftRigth(gf.getjListUnused(), gf.getjListUsed());
                    } catch (Exception ex) {
                        new Thrower().exception(ex);
                    }

                }
            });


        } catch (Exception e) {
            throw e;
        }

    } // end addListenerButtons

    //==========================================================================
    private void save() throws Exception {

        gf.getjProgressBar().setIndeterminate(true);

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                HashMap hashMap = new PieceUtilities().subPieceToHashMap(new SubPiece());
                ArrayList arrayList = new ArrayList();
                ArrayList response = null;

                try {

                    //collect data and send
                    arrayList.add(null);
                    arrayList.add(gf.getjTextFieldName().getText());
                    arrayList.add(gf.getjRadioButtonActive().isSelected());
                    arrayList.add(gf.getjComboBoxProtocols().getModel().getSelectedItem());
                    arrayList.add(getDataList(gf.getjListUnused()));
                    arrayList.add(getDataList(gf.getjListUsed()));
                    arrayList.add(gf.getjComboBoxTos().getModel().getSelectedItem());
                    arrayList.add(gf.getjComboBoxSource().getModel().getSelectedItem());
                    arrayList.add(gf.getjComboBoxDirection().getModel().getSelectedItem());
                    arrayList.add(gf.getjComboBoxDestination().getModel().getSelectedItem());

                    hashMap.put("create filters", arrayList);

                    response = (ArrayList) new JMSProccessor().sendReceive(
                            "filters",
                            configuration.getMasterShaperServer(),
                            "srs",
                            configuration.getJmsTimeWaitMessage(),
                            hashMap);

                    if (response.get(0).equals(true)) {
                        ControllerModelerTableManager cmtm = new ControllerModelerTableManager("filters");
                        cmtm.setupInterface();
                        gf.setVisible(false);
                        cmtm.setVisible(true);
                    } else {
                        new Notifications().error(response.get(1).toString(), new Exception());
                    }

                } catch (Exception e) {
                    throw e;
                } finally {
                    gf.getjProgressBar().setIndeterminate(true);
                }

                return null;
            }
        }.execute();

    } // end save

    //==========================================================================
    private Object[] getDataCombo(JComboBox jComboBox) throws Exception {

        Object[] objects = null;
        ComboBoxModel cbm = null;

        try {

            cbm = jComboBox.getModel();
            objects = new Object[cbm.getSize()];

            for (int i = 0; i < cbm.getSize(); i++) {
                objects[i] = cbm.getSelectedItem();
            }

        } catch (Exception e) {
            throw e;
        }

        return objects;

    }

    //==========================================================================
    private Object[] getDataList(JList jList) throws Exception {

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
        }

        return objects;

    } // end getDataList

    //==========================================================================
    public ArrayList requestData() throws Exception {

        ArrayList arrayList = new ArrayList();
        HashMap hashMap = null;

        try {

            hashMap = new PieceUtilities().subPieceToHashMap(new SubPiece());
            hashMap.put("getDefaultData", "filters");
            arrayList = (ArrayList) new JMSProccessor().sendReceive(
                    "filters",
                    configuration.getMasterShaperServer(),
                    "srs",
                    configuration.getJmsTimeWaitMessage(), hashMap);

        } catch (Exception e) {
            throw e;
        }
        return arrayList;
    } // end requestData

    //==========================================================================
    public void enabledComponents(boolean flag) {
    } // end enabledComponents

    //==========================================================================
    public void setVisible(boolean flag) {
        gf.setVisible(flag);
    } // setVisible

    //==========================================================================    
    public void setEnabledComponents(boolean flag) throws Exception {
        gf.getjButtonSave().setEnabled(flag);
    }
} // end class
