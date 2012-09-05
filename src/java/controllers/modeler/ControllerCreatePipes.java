package controllers.modeler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.SwingWorker;
import model.beans.SubPiece;
import model.util.PieceUtilities;
import model.util.Thrower;
import view.dialogs.modeler.ModelerPipes;
import view.notifications.Notifications;

/**
 *
 * @author skuarch
 */
public class ControllerCreatePipes extends Modeler {

    private ModelerPipes modelerPipes = new ModelerPipes(null, true);

    //==========================================================================
    public ControllerCreatePipes() throws Exception {
        super();
    } // end ControllerCreatePipes

    //==========================================================================
    @Override
    public void setupInterface() throws Exception {
        
        addListenersButtons();
        setupButtonsInWindow();
        modelerPipes.getjProgressBar().setIndeterminate(true);
        setEnabledComponents(false);

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                ArrayList arrayList = null;

                try {

                    arrayList = requestDefaultData("pipes");
                    
                    if (arrayList == null) {
                        return null;
                    }

                    if (arrayList.get(0).equals(true)) {
                        modelerPipes.getjRadioButtonActive().setSelected(true);
                    } else {
                        modelerPipes.getjRadioButtonInactive().setSelected(true);
                    }

                    fillJComboBox(modelerPipes.getjComboBoxChain(), (String[]) arrayList.get(1));

                    fillJComboBox(modelerPipes.getjComboBoxDirection(), (String[]) arrayList.get(2));

                    fillJList(modelerPipes.getjListFilterLeft(), (String[]) arrayList.get(3));
                    fillJList(modelerPipes.getjListFilterRight(), (String[]) arrayList.get(4));

                    fillJComboBox(modelerPipes.getjComboBoxServiceLevel(), (String[]) arrayList.get(5));


                } catch (Exception e) {
                    notifications.error(e.getMessage(), e);
                    throw e;
                } finally {
                    modelerPipes.getjProgressBar().setIndeterminate(false);
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
            modelerPipes.setVisible(flag);
        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }


    } // setVisible

    //==========================================================================*
    @Override
    public void setEnabledComponents(boolean flag) throws Exception {
        modelerPipes.getjButton2().setEnabled(flag);
        modelerPipes.getjButtonLeft().setEnabled(flag);
        modelerPipes.getjButtonRight().setEnabled(flag);
        modelerPipes.getjComboBoxChain().setEnabled(flag);
        modelerPipes.getjComboBoxDirection().setEnabled(flag);
        modelerPipes.getjComboBoxServiceLevel().setEnabled(flag);
        modelerPipes.getjRadioButtonActive().setEnabled(flag);
        modelerPipes.getjRadioButtonInactive().setEnabled(flag);
    } // end setEnabledComponents

    //==========================================================================*
    @Override
    public void addListenersButtons() throws Exception {
        //save -----------------------------------------------------------------
        modelerPipes.getjButton2().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    actionButtonCreateInWindow();
                } catch (Exception ex) {
                    new Thrower().exception(ex);
                }
            }
        });

        //close ----------------------------------------------------------------
        modelerPipes.getjButton1().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                modelerPipes.setVisible(false);
            }
        });

        //left -----------------------------------------------------------------
        modelerPipes.getjButtonLeft().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                try {
                    passItemsLeftRigth(modelerPipes.getjListFilterLeft(), modelerPipes.getjListFilterRight());
                } catch (Exception ex) {
                    new Thrower().exception(ex);
                }

            }
        });

        //Right -----------------------------------------------------------------
        modelerPipes.getjButtonRight().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                try {
                    passItemsLeftRigth(modelerPipes.getjListFilterRight(), modelerPipes.getjListFilterLeft());
                } catch (Exception ex) {
                    notifications.error(ex.getMessage(), ex);
                }

            }
        });
    } // addListenersButtons

    //==========================================================================
    @Override
    public void actionButtonCreateInWindow() throws Exception {

        setEnabledComponents(false);
        modelerPipes.getjProgressBar().setIndeterminate(true);

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                ArrayList arrayList = null;
                HashMap hashMap = new PieceUtilities().subPieceToHashMap(new SubPiece());
                ArrayList data = new ArrayList();

                try {

                    data.add("null");
                    data.add(modelerPipes.getjTextFieldName().getText());
                    data.add(modelerPipes.getjRadioButtonActive().isSelected());
                    data.add(modelerPipes.getjComboBoxChain().getSelectedItem());
                    data.add(modelerPipes.getjComboBoxDirection().getSelectedItem());
                    data.add(getDataJList(modelerPipes.getjListFilterLeft()));
                    data.add(getDataJList(modelerPipes.getjListFilterRight()));
                    data.add(modelerPipes.getjComboBoxServiceLevel().getSelectedItem());
                    hashMap.put("data pipes", data);

                    arrayList = createGeneric("pipes", hashMap);

                    if (!arrayList.get(0).equals(true)) {
                        notifications.error(arrayList.get(1).toString(), new Exception());
                    }else{
                        modelerPipes.setVisible(false);
                    }

                } catch (Exception e) {
                    notifications.error(e.getMessage(), e);
                } finally {
                    setEnabledComponents(true);
                    modelerPipes.getjProgressBar().setIndeterminate(false);
                }

                return null;
            }
        }.execute();

    } // end actionButtonCreateInWindow

    //==========================================================================*
    @Override
    public void actionButtonEditInTable() throws Exception {

        setEnabledComponents(false);
        modelerPipes.getjProgressBar().setIndeterminate(true);

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                
                ArrayList arrayList = null;
                HashMap hashMap = new PieceUtilities().subPieceToHashMap(new SubPiece());
                ArrayList data = new ArrayList();

                try {

                    data.add(modelerPipes.getjTextFieldName().getText());
                    data.add(modelerPipes.getjRadioButtonActive().isSelected());
                    data.add(modelerPipes.getjComboBoxChain().getSelectedItem());
                    data.add(modelerPipes.getjComboBoxDirection().getSelectedItem());
                    data.add(getDataJList(modelerPipes.getjListFilterLeft()));
                    data.add(getDataJList(modelerPipes.getjListFilterRight()));
                    data.add(modelerPipes.getjComboBoxServiceLevel().getSelectedItem());
                    hashMap.put("data pipes", data);

                    arrayList = createGeneric("pipes", hashMap);

                    if (!arrayList.get(0).equals(true)) {
                        new Notifications().error(arrayList.get(1).toString(), new Exception());
                    }else{                        
                        ControllerModelerTableManager cmtm = new ControllerModelerTableManager("pipes");
                        cmtm.setupInterface();
                        modelerPipes.setVisible(false);
                        cmtm.setVisible(true);         
                    }

                } catch (Exception e) {
                    notifications.error(e.getMessage(), e);
                } finally {
                    setEnabledComponents(true);
                    modelerPipes.getjProgressBar().setIndeterminate(false);
                }

                return null;

            }
        };



    } // end edit

    //==========================================================================*
    @Override
    public void setupButtonsInWindow() throws Exception {
        try {

            modelerPipes.getjButton1().setText("close");
            modelerPipes.getjButton2().setText("create");

        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }
    } // end setupButtons

    @Override
    public void putButtonsTable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionButtonDeleteInTable() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
} // end class
