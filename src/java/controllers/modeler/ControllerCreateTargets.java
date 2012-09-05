package controllers.modeler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.SwingWorker;
import model.beans.SubPiece;
import model.util.PieceUtilities;
import model.util.Thrower;
import view.dialogs.modeler.ModelerTargets;
import view.notifications.Notifications;

/**
 *
 * @author skuarch
 */
public class ControllerCreateTargets extends Modeler {

    private ModelerTargets modelerTargets = new ModelerTargets(null, true);

    //==========================================================================
    public ControllerCreateTargets() throws Exception {
        super();
    } // end ControllerCreateTargets

    //==========================================================================
    @Override
    public void setupInterface() throws Exception {

        setupButtonsInWindow();
                    addListenersButtons();
        setEnabledComponents(false);
        modelerTargets.getjProgressBar().setIndeterminate(true);

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                ArrayList arrayList = null;
                
                try {

                    arrayList = requestDefaultData("targets");
                    
                    fillJList(modelerTargets.getjListGroupLeft(), (Object[]) arrayList.get(0));
                    fillJList(modelerTargets.getjListGroupRight(), (Object[]) arrayList.get(1));

                } catch (Exception e) {
                    notifications.error(e.getMessage(), e);
                } finally {
                    setEnabledComponents(true);
                    modelerTargets.getjProgressBar().setIndeterminate(false);
                }

                return null;
            }
        }.execute();

    } // end setupInterface

    //==========================================================================
    @Override
    public void setVisible(boolean flag) throws Exception {

        try {
            modelerTargets.setVisible(flag);
        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }

    } // end setVisible

    //==========================================================================
    @Override
    public void setEnabledComponents(boolean flag) throws Exception {
        modelerTargets.getjButton2().setEnabled(flag);
    } // end setEnabledComponents

    //==========================================================================
    @Override
    public void addListenersButtons() throws Exception {
        modelerTargets.getjButton1().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                modelerTargets.setVisible(false);
            }
        });

        modelerTargets.getjButton2().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                try {
                    actionButtonCreateInWindow();
                } catch (Exception ex) {
                    notifications.error(ex.getMessage(), ex);
                }

            }
        });
        
        //left -----------------------------------------------------------------
        modelerTargets.getjButtonLeft().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                try {
                    passItemsLeftRigth(modelerTargets.getjListGroupLeft(), modelerTargets.getjListGroupRight());
                } catch (Exception ex) {
                    new Thrower().exception(ex);
                }

            }
        });

        //Right -----------------------------------------------------------------
        modelerTargets.getjButtonRight().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                try {
                    passItemsLeftRigth(modelerTargets.getjListGroupRight(), modelerTargets.getjListGroupLeft());
                } catch (Exception ex) {
                    notifications.error(ex.getMessage(), ex);
                }

            }
        });
    } // end addListenersButtons

    //==========================================================================
    @Override
    public void actionButtonCreateInWindow() throws Exception {

        setEnabledComponents(false);
        modelerTargets.getjProgressBar().setIndeterminate(true);

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                ArrayList arrayList = null;
                HashMap hashMap = new PieceUtilities().subPieceToHashMap(new SubPiece());
                ArrayList data = new ArrayList();

                try {

                    data.add("null");
                    data.add(modelerTargets.getjTextFieldName().getText());
                                        
                    if(modelerTargets.getjRadioButtonIP().isSelected()){
                        data.add("ip true");
                    }
                    
                    if(modelerTargets.getjRadioButtonMac().isSelected()){
                        data.add("mac true");
                    }
                    
                    
                    if(modelerTargets.getjRadioButtonGroup().isSelected()){
                        data.add("group true");
                    }
                    
                    data.add(modelerTargets.getjTextFieldIP().getText());
                    data.add(modelerTargets.getjTextFieldMac().getText());                    
                    data.add(getDataJList(modelerTargets.getjListGroupLeft()));
                    data.add(getDataJList(modelerTargets.getjListGroupRight()));

                    hashMap.put("data targets", data);

                    arrayList = createGeneric("targets", hashMap);

                    if (!arrayList.get(0).equals(true)) {
                        new Notifications().error(arrayList.get(1).toString(), new Exception());
                    } else {                        
                        ControllerModelerTableManager cmtm = new ControllerModelerTableManager("targets");
                        cmtm.setupInterface();
                        modelerTargets.setVisible(false);
                        cmtm.setVisible(true);           
                    }

                } catch (Exception e) {
                    throw e;
                } finally {
                    setEnabledComponents(true);
                    modelerTargets.getjProgressBar().setIndeterminate(false);
                }

                return null;
            }
        }.execute();

    } // end actionButtonCreateInWindow

    //==========================================================================
    @Override
    public void actionButtonEditInTable() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    } // end actionButtonEditInTable

    //==========================================================================
    @Override
    public void actionButtonDeleteInTable() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    } // end actionButtonDeleteInTable

    //==========================================================================
    @Override
    public void setupButtonsInWindow() throws Exception {
        modelerTargets.getjButton1().setText("close");
        modelerTargets.getjButton2().setText("create");
    } // end setupButtonsInWindow

    //==========================================================================
    @Override
    public void putButtonsTable() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    } // end putButtonsTable
} // end class
