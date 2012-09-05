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
public class ControllerEditTargets extends Modeler {

    private ModelerTargets modelerTargets = new ModelerTargets(null, true);
    private String id = null;
    //==========================================================================

    public ControllerEditTargets(String id) throws Exception {
        super();
        this.id = id;
    } // end ControllerEditTargets    

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

                    arrayList = requestData(id, "targets");

                    if (arrayList == null) {
                        return null;
                    }

                    modelerTargets.getjTextFieldName().setText(arrayList.get(0).toString());
                    modelerTargets.getjTextFieldIP().setText(arrayList.get(1).toString());
                    modelerTargets.getjTextFieldMac().setText(arrayList.get(2).toString());

                    if (arrayList.get(3).equals("ip true")) {
                        modelerTargets.getjRadioButtonIP().setSelected(true);
                    }
                    
                    if (arrayList.get(3).equals("mac true")) {
                        modelerTargets.getjRadioButtonMac().setSelected(true);
                    }
                    
                    if (arrayList.get(3).equals("group true")) {
                        modelerTargets.getjRadioButtonGroup().setSelected(true);
                    }

                    fillJList(modelerTargets.getjListGroupLeft(), (Object[]) arrayList.get(4));
                    fillJList(modelerTargets.getjListGroupRight(), (Object[]) arrayList.get(5));

                } catch (Exception e) {
                    notifications.error(e.getMessage(), e);
                } finally {
                    setEnabledComponents(true);
                    modelerTargets.getjProgressBar().setIndeterminate(false);
                }

                return null;
            }
        }.execute();
    }

    //==========================================================================
    @Override
    public void setVisible(boolean flag) throws Exception {
        try {
            modelerTargets.setVisible(flag);
        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }
    }

    //==========================================================================
    @Override
    public void setEnabledComponents(boolean flag) throws Exception {
        modelerTargets.getjButton2().setEnabled(flag);
    }

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
    }

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

                    data.add(id);
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
                        ControllerModelerTableSettings cmtm = new ControllerModelerTableSettings("targets");
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

    }

    //==========================================================================
    @Override
    public void actionButtonEditInTable() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //==========================================================================
    @Override
    public void actionButtonDeleteInTable() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //==========================================================================
    @Override
    public void setupButtonsInWindow() throws Exception {
        modelerTargets.getjButton1().setText("close");
        modelerTargets.getjButton2().setText("save");
    }

    //==========================================================================
    @Override
    public void putButtonsTable() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
} // end ControllerEditTargets
