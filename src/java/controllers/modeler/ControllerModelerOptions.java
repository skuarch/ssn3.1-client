package controllers.modeler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.SwingWorker;
import model.beans.SubPiece;
import model.util.PieceUtilities;
import view.dialogs.modeler.ModelerOptions;

/**
 *
 * @author skuarch
 */
public class ControllerModelerOptions extends Modeler {

    private ModelerOptions modelerOptions = new ModelerOptions(null, true);

    //==========================================================================
    public ControllerModelerOptions() throws Exception {
        super();
    } // end ControllerModelerOptions

    //==========================================================================
    @Override
    public void setupInterface() throws Exception {

        setEnabledComponents(false);
        modelerOptions.getjProgressBar().setIndeterminate(true);

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                addListenersButtons();
                setupButtonsInWindow();

                ArrayList arrayList = null;

                try {

                    arrayList = requestDefaultData("options");

                    if (arrayList == null) {
                        return null;
                    }

                    modelerOptions.getjTextFieldInboundBandwidth().setText(arrayList.get(0).toString());
                    modelerOptions.getjTextFieldOutboundBandwidth().setText(arrayList.get(1).toString());

                    modelerOptions.getjTextFieldIncomingInterface().setText(arrayList.get(2).toString());
                    modelerOptions.getjTextFieldOutgoingInterface().setText(arrayList.get(3).toString());

                    if (arrayList.get(4).equals(true)) {
                        modelerOptions.getjRadioButtonIMQYes().setSelected(true);
                    } else {
                        modelerOptions.getjRadioButtonIMQNo().setSelected(true);
                    }

                    fillJComboBox(modelerOptions.getjComboBoxACKPackets(), (Object[]) arrayList.get(5));
                    fillJComboBox(modelerOptions.getjComboBoxClassifier(), (Object[]) arrayList.get(6));
                    fillJComboBox(modelerOptions.getjComboBoxDefaultQueuingDiscipline(), (Object[]) arrayList.get(7));

                    modelerOptions.getjTextFieldESFQPerturb().setText(arrayList.get(8).toString());
                    modelerOptions.getjTextFieldESFQLimit().setText(arrayList.get(9).toString());
                    modelerOptions.getjTextFieldESFQDepth().setText(arrayList.get(10).toString());
                    modelerOptions.getjTextFieldESFQDivisor().setText(arrayList.get(11).toString());

                    fillJComboBox(modelerOptions.getjComboBoxESFQHash(), (Object[]) arrayList.get(12));


                    if (arrayList.get(13).equals(true)) {
                        modelerOptions.getjRadioButtonTCFilter().setSelected(true);
                    } else {
                        modelerOptions.getjRadioButtonIptables().setSelected(true);
                    }

                    if (arrayList.get(14).equals(true)) {
                        modelerOptions.getjRadioButtonRouter().setSelected(true);
                    } else {
                        modelerOptions.getjRadioButtonBridge().setSelected(true);
                    }

                } catch (Exception e) {
                    notifications.error(e.getMessage(), e);
                } finally {
                    setEnabledComponents(true);
                    modelerOptions.getjProgressBar().setIndeterminate(false);
                }

                return null;
            }
        }.execute();

    } // end setupInterface

    //==========================================================================
    @Override
    public void setVisible(boolean flag) throws Exception {
        modelerOptions.setVisible(true);
        modelerOptions = null;
    }

    //==========================================================================
    @Override
    public void setEnabledComponents(boolean flag) throws Exception {
        modelerOptions.getjButton2().setEnabled(flag);
    }

    //==========================================================================
    @Override
    public void addListenersButtons() throws Exception {

        // close ---------------------------------------------------------------
        modelerOptions.getjButton1().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    modelerOptions.setVisible(false);
                } catch (Exception ex) {
                    notifications.error(ex.getMessage(), ex);
                }
            }
        });

        //create ---------------------------------------------------------------
        modelerOptions.getjButton2().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {

                    actionButtonCreateInWindow();
                } catch (Exception ex) {
                    notifications.error(ex.getMessage(), ex);
                }

            }
        });

        //change combo
        modelerOptions.getjComboBoxDefaultQueuingDiscipline().addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                changeComboDefaultQueuingDiscipline();
            }
        });
    }

    //==========================================================================
    @Override
    public void actionButtonCreateInWindow() throws Exception {

        setEnabledComponents(false);
        modelerOptions.getjProgressBar().setIndeterminate(true);

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                ArrayList arrayList = null;
                HashMap hashMap = new PieceUtilities().subPieceToHashMap(new SubPiece());
                ArrayList data = new ArrayList();
                int trafficFilter = 1;
                int mode = 1;

                try {

                    data.add(modelerOptions.getjTextFieldInboundBandwidth().getText());
                    data.add(modelerOptions.getjTextFieldOutboundBandwidth().getText());

                    data.add(modelerOptions.getjTextFieldIncomingInterface().getText());
                    data.add(modelerOptions.getjTextFieldOutgoingInterface().getText());

                    data.add(modelerOptions.getjRadioButtonIMQYes().isSelected());

                    data.add(modelerOptions.getjComboBoxACKPackets().getModel().getSelectedItem());
                    data.add(modelerOptions.getjComboBoxClassifier().getModel().getSelectedItem());
                    data.add(modelerOptions.getjComboBoxDefaultQueuingDiscipline().getModel().getSelectedItem());

                    data.add(modelerOptions.getjTextFieldESFQPerturb().getText());
                    data.add(modelerOptions.getjTextFieldESFQLimit().getText());
                    data.add(modelerOptions.getjTextFieldESFQDepth().getText());
                    data.add(modelerOptions.getjTextFieldESFQDivisor().getText());
                    data.add(modelerOptions.getjComboBoxESFQHash().getModel().getSelectedItem().toString());

                    if (modelerOptions.getjRadioButtonTCFilter().isSelected()) {
                        trafficFilter = 0;
                    }

                    if (modelerOptions.getjRadioButtonRouter().isSelected()) {
                        mode = 0;
                    }

                    data.add(trafficFilter);
                    data.add(mode);

                    hashMap.put("data options", data);

                    arrayList = createGeneric("options", hashMap);

                    if (!arrayList.get(0).equals(true)) {
                        notifications.error(arrayList.get(1).toString(), new Exception());
                    } else {
                        modelerOptions.setVisible(false);
                    }

                } catch (Exception e) {
                    notifications.error(e.getMessage(), e);
                } finally {
                    setEnabledComponents(true);
                    modelerOptions.getjProgressBar().setIndeterminate(false);
                    modelerOptions.setVisible(false);
                }

                return null;
            }
        }.execute();

    }

    //==========================================================================
    private void changeComboDefaultQueuingDiscipline() {

        String selected = null;

        try {

            selected = modelerOptions.getjComboBoxDefaultQueuingDiscipline().getModel().getSelectedItem().toString();
            
            if (selected.equalsIgnoreCase("ESFQ")) {
                setEnableESFQComponents(true);
            } else {
                setEnableESFQComponents(false);
            }

        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }
    }

    //==========================================================================
    private void setEnableESFQComponents(boolean flag) {

        modelerOptions.getjComboBoxESFQHash().setEnabled(flag);
        modelerOptions.getjTextFieldESFQDepth().setEnabled(flag);
        modelerOptions.getjTextFieldESFQDivisor().setEnabled(flag);
        modelerOptions.getjTextFieldESFQLimit().setEnabled(flag);
        modelerOptions.getjTextFieldESFQPerturb().setEnabled(flag);

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
        modelerOptions.getjButton1().setText("close");
        modelerOptions.getjButton2().setText("save");
    }

    //==========================================================================
    @Override
    public void putButtonsTable() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
} // end class
