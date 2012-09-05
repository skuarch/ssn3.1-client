package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.SwingWorker;
import model.beans.Configuration;
import model.beans.SubPiece;
import model.jms.JMSProccessor;
import model.util.PieceUtilities;
import model.util.Thrower;
import view.dialogs.modeler.GenericChain;
import view.notifications.Notifications;

/**
 *
 * @author skuarch
 */
public class ControllerNewChain {

    private GenericChain gc = new GenericChain(null, true);
    private Configuration configuration = null;
    private String id = null; 

    //==========================================================================
    public ControllerNewChain() throws Exception {
        configuration = new ControllerConfiguration().getInitialConfiguration();
    } // end ControllerNewChain

    //==========================================================================
    public void setVisible(boolean flag) {
        gc.setVisible(flag);
    } // end setVisible

    //==========================================================================
    public void isStatusActive(boolean flag) {
        gc.isStatusActive(flag);
    }

    //==========================================================================
    /**
     * request data for a new chain
     */
    private ArrayList requestDataDefault() throws Exception {

        ArrayList arrayList = new ArrayList();
        HashMap hashMap = null;

        try {

            hashMap = new PieceUtilities().subPieceToHashMap(new SubPiece());
            hashMap.put("getDefaultData", "chains");
            arrayList = (ArrayList) new JMSProccessor().sendReceive(
                    "chains",
                    configuration.getMasterShaperServer(),
                    "srs",
                    configuration.getJmsTimeWaitMessage(), hashMap);

        } catch (Exception e) {
            throw e;
        }

        return arrayList;

    } // end requestDataDefault

    //==========================================================================
    /**
     * request data for a specific chain
     */
    public ArrayList requestDataChain(String idChain) throws Exception {

        ArrayList arrayList = new ArrayList();
        HashMap hashMap = null;

        try {

            hashMap = new PieceUtilities().subPieceToHashMap(new SubPiece());
            hashMap.put("getDataChains", idChain);

            arrayList = (ArrayList) new JMSProccessor().sendReceive(
                    "chains",
                    configuration.getMasterShaperServer(),
                    "srs",
                    configuration.getJmsTimeWaitMessage(),
                    hashMap);

        } catch (Exception e) {
            throw e;
        }

        return arrayList;

    } // end requestDataDefault

    //==========================================================================
    public void setInterface() throws Exception {

        new Thread(new Runnable() {

            public void run() {

                setIndeterminateProgressBar(true);
                gc.setEnabled(false);
                ArrayList arrayList = null;
                String[] serviceLevel = null;
                String[] fallback = null;
                String[] source = null;
                String[] direction = null;
                String[] destination = null;

                try {

                    arrayList = requestDataDefault();
                    serviceLevel = (String[]) arrayList.get(0);
                    fallback = (String[]) arrayList.get(1);
                    source = (String[]) arrayList.get(2);
                    direction = (String[]) arrayList.get(3);
                    destination = (String[]) arrayList.get(4);

                    gc.getjComboBoxServiceLevel().removeAllItems();
                    for (Object objects : serviceLevel) {
                        gc.getjComboBoxServiceLevel().addItem(objects);
                    }


                    gc.getjComboBoxFallback().removeAllItems();
                    for (Object objects : fallback) {
                        gc.getjComboBoxFallback().addItem(objects);
                    }

                    gc.getjComboBoxSource().removeAllItems();
                    for (Object objects : source) {
                        gc.getjComboBoxSource().addItem(objects);
                    }

                    gc.getjComboBoxDirection().removeAllItems();
                    for (Object objects : direction) {
                        gc.getjComboBoxDirection().addItem(objects);
                    }

                    gc.getjComboBoxDestination().removeAllItems();
                    for (Object objects : destination) {
                        gc.getjComboBoxDestination().addItem(objects);
                    }

                } catch (Exception e) {
                    new Thrower().exception(e);
                } finally {
                    setIndeterminateProgressBar(false);
                    gc.setEnabled(true);
                    arrayList = null;
                }
            }
        }).start();

    } // end setInterface

    //==========================================================================
    public void setInterface(final ArrayList arrayList) throws Exception {


        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                
                setIndeterminateProgressBar(true);
                gc.setEnabled(false);
                
                String chainName = null;
                String status = null;
                String[] serviceLevel = null;
                String[] fallback = null;
                String[] source = null;
                String[] direction = null;
                String[] destination = null;

                try {
                    
                    id = (String) arrayList.get(0);
                    chainName = (String) arrayList.get(1);
                    status = (String) arrayList.get(2);
                    serviceLevel = (String[]) arrayList.get(3);
                    fallback = (String[]) arrayList.get(4);
                    source = (String[]) arrayList.get(5);
                    direction = (String[]) arrayList.get(6);
                    destination = (String[]) arrayList.get(7);
                    
                    gc.getjTextFieldChainName().setText(chainName);
                    
                    if(status.equalsIgnoreCase("Y")){
                        gc.getjRadioButtonStatusActive().setSelected(true);
                    }else{
                        gc.getjRadioButtonStatusInactive().setSelected(true);
                    }
                    

                    gc.getjComboBoxServiceLevel().removeAllItems();
                    for (Object objects : serviceLevel) {
                        gc.getjComboBoxServiceLevel().addItem(objects);
                    }


                    gc.getjComboBoxFallback().removeAllItems();
                    for (Object objects : fallback) {
                        gc.getjComboBoxFallback().addItem(objects);
                    }

                    gc.getjComboBoxSource().removeAllItems();
                    for (Object objects : source) {
                        gc.getjComboBoxSource().addItem(objects);
                    }

                    gc.getjComboBoxDirection().removeAllItems();
                    for (Object objects : direction) {
                        gc.getjComboBoxDirection().addItem(objects);
                    }

                    gc.getjComboBoxDestination().removeAllItems();
                    for (Object objects : destination) {
                        gc.getjComboBoxDestination().addItem(objects);
                    }

                } catch (Exception e) {
                    new Thrower().exception(e);
                } finally {
                    setIndeterminateProgressBar(false);
                    gc.setEnabled(true);
                }
                
                return null;
                
            }
        }.execute();

    } // end setInterface

    //==========================================================================
    public void setIndeterminateProgressBar(boolean flag) {
        gc.setIndeterminateProgressBar(flag);
    }

    //==========================================================================
    public void setActionListenerButtonSave() throws Exception {

        try {

            gc.addListenerButtonSave(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    ArrayList arrayList = null;

                    try {

                        if (validate()) {

                            arrayList = sendData();
                            setVisible(false);

                            if (arrayList.get(0).equals(true)) {
                                ControllerModelerTableManagerNOBORRAR cgt = new ControllerModelerTableManagerNOBORRAR("chains");
                                cgt.setupInterface();
                                cgt.setVisible(true);
                            } else {
                                new Notifications().error(arrayList.get(1).toString(), new Exception());
                            }

                            /*
                             * ControllerChainsBorrar cc = new
                             * ControllerChainsBorrar(); cc.setInterface();
                             * cc.setAutoCreateRowSorter(true);
                             * cc.setRowHeight(34); cc.setListenerButtonNew();
                             * cc.setVisible(true);
                             */
                        }

                    } catch (Exception ex) {
                        new Thrower().exception(ex);
                    }

                }
            });
        } catch (Exception e) {
            throw e;
        }

    } // end setActionListenerButtonSave

    //==========================================================================
    private ArrayList sendData() throws Exception {

        StringBuilder data = new StringBuilder();        
        HashMap hashMap = null;
        ArrayList arrayList = null;


        try {
            
            hashMap = new PieceUtilities().subPieceToHashMap(new SubPiece());            
            
            data.append(id);
            data.append("##");
            data.append(gc.getjTextFieldChainName().getText());
            data.append("##");
            data.append(gc.getjRadioButtonStatusActive().isSelected());
            data.append("##");
            data.append(gc.getjComboBoxServiceLevel().getModel().getSelectedItem().toString());
            data.append("##");
            data.append(gc.getjComboBoxFallback().getModel().getSelectedItem().toString());
            data.append("##");
            data.append(gc.getjComboBoxSource().getModel().getSelectedItem().toString());
            data.append("##");
            data.append(gc.getjComboBoxDirection().getModel().getSelectedItem().toString());
            data.append("##");
            data.append(gc.getjComboBoxDestination().getModel().getSelectedItem().toString());

            hashMap.put("create chains", data.toString());

            arrayList = (ArrayList) new JMSProccessor().sendReceive(
                    "chains",
                    configuration.getMasterShaperServer(),
                    "srs",
                    configuration.getJmsTimeWaitMessage(),
                    hashMap);

        } catch (Exception e) {
            throw e;
        } finally {
            gc.setVisible(false);
        }

        return arrayList;

    } // end sendData

    //==========================================================================
    private boolean validate() throws Exception {

        boolean flag = false;
        String chainName = null;

        try {

            chainName = gc.getjTextFieldChainName().getText();

            if (chainName == null || chainName.length() < 1) {
                new Notifications().error("the name of chain is empty", new Exception());
                return false;
            }

            flag = true;

        } catch (Exception e) {
            throw e;
        }

        return flag;

    } // end validate
} // end class

