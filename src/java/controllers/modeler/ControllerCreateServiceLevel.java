package controllers.modeler;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.SwingWorker;
import model.beans.SubPiece;
import model.util.PieceUtilities;
import model.util.Thrower;
import view.dialogs.modeler.ModelerServiceLevel;

/**
 *
 * @author skuarch
 */
public class ControllerCreateServiceLevel extends Modeler {

    private ModelerServiceLevel modelerServiceLevel = new ModelerServiceLevel(null, true);

    //==========================================================================
    public ControllerCreateServiceLevel() throws Exception {
        super();
        modelerServiceLevel.getjPanelHTB().setVisible(false);
        modelerServiceLevel.getjPanelCBQ().setVisible(false);
        modelerServiceLevel.getjPanelHFSC().setVisible(false);
        modelerServiceLevel.getjPanelESFQ().setVisible(false);
        modelerServiceLevel.getjPanelNETEM().setVisible(false);
        modelerServiceLevel.setSize(new Dimension(590, 510));
    } // end ControllerServiceLevel

    //==========================================================================
    @Override
    public void setupInterface() throws Exception {

        addListenersButtons();
        setupButtonsInWindow();
        setEnabledComponents(false);
        modelerServiceLevel.getjProgressBar().setIndeterminate(true);

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                ArrayList arrayList = null;

                try {

                    arrayList = requestDefaultData("service level");

                    if (arrayList == null) {
                        return null;
                    }

                    modelerServiceLevel.setTitle("service level");

                    modelerServiceLevel.getjTextFieldName().setText(arrayList.get(0).toString());
                    fillJComboBox(modelerServiceLevel.getjComboBoxClasifier(), (Object[]) arrayList.get(1));
                    modelerServiceLevel.getjTextFieldInBandwidth().setText(arrayList.get(2).toString());
                    modelerServiceLevel.getjTextFieldInBandwidthCeil().setText(arrayList.get(3).toString());
                    modelerServiceLevel.getjTextFieldInBandwidthBurst().setText(arrayList.get(4).toString());
                    modelerServiceLevel.getjTextFieldOutBandwidth().setText(arrayList.get(5).toString());
                    modelerServiceLevel.getjTextFieldOutBandwidthCeil().setText(arrayList.get(6).toString());
                    modelerServiceLevel.getjTextFieldOutBandwidthBurst().setText(arrayList.get(7).toString());
                    fillJComboBox(modelerServiceLevel.getjComboBoxPriority(), (Object[]) arrayList.get(8));

                    modelerServiceLevel.getjTextFieldCBQInBandwidth().setText(arrayList.get(9).toString());
                    fillJComboBox(modelerServiceLevel.getjComboBoxInPriority(), (Object[]) arrayList.get(10));

                    modelerServiceLevel.getjTextFieldCBQOutBandwidth().setText(arrayList.get(11).toString());
                    fillJComboBox(modelerServiceLevel.getjComboBoxOutPriority(), (Object[]) arrayList.get(12));

                    if (arrayList.get(13).equals(true)) {
                        modelerServiceLevel.getjRadioButtonBoundedYes().setSelected(true);
                    } else {
                        modelerServiceLevel.getjRadioButtonBoundedNo().setSelected(true);
                    }

                    modelerServiceLevel.getjTextFieldInWorkUnit().setText(arrayList.get(14).toString());
                    modelerServiceLevel.getjTextFieldInMaxDelay().setText(arrayList.get(15).toString());
                    modelerServiceLevel.getjTextFieldInulRate().setText(arrayList.get(16).toString());
                    modelerServiceLevel.getjTextFieldHFSCInulRate().setText(arrayList.get(17).toString());
                    modelerServiceLevel.getjTextFieldOutWorkUnit().setText(arrayList.get(18).toString());
                    modelerServiceLevel.getjTextFieldOutMaxDelay().setText(arrayList.get(19).toString());
                    modelerServiceLevel.getjTextFieldOutRate().setText(arrayList.get(20).toString());
                    modelerServiceLevel.getjTextFieldOutulRate().setText(arrayList.get(21).toString());


                    fillJComboBox(modelerServiceLevel.getjComboBoxQueuingDiscipline(), (Object[]) arrayList.get(22));
                    modelerServiceLevel.getjTextFieldPerturb().setText(arrayList.get(23).toString());

                    modelerServiceLevel.getjTextFieldLimit().setText(arrayList.get(24).toString());
                    modelerServiceLevel.getjTextFieldDepth().setText(arrayList.get(25).toString());
                    modelerServiceLevel.getjTextFieldDivisor().setText(arrayList.get(26).toString());
                    fillJComboBox(modelerServiceLevel.getjComboBoxHash(), (Object[]) arrayList.get(27));
                    modelerServiceLevel.getjTextFieldDelay().setText(arrayList.get(28).toString());
                    modelerServiceLevel.getjTextFieldJitter().setText(arrayList.get(29).toString());
                    modelerServiceLevel.getjTextFieldCorrelation().setText(arrayList.get(30).toString());
                    fillJComboBox(modelerServiceLevel.getjComboBoxDistribution(), (Object[]) arrayList.get(31));
                    modelerServiceLevel.getjTextFieldPacketloss().setText(arrayList.get(32).toString());
                    modelerServiceLevel.getjTextFieldDuplication().setText(arrayList.get(33).toString());


                    modelerServiceLevel.getjTextFieldGap().setText(arrayList.get(34).toString());
                    modelerServiceLevel.getjTextFieldReorderPercentage().setText(arrayList.get(35).toString());
                    modelerServiceLevel.getjTextFieldReorderCorrelation().setText(arrayList.get(36).toString());


                } catch (Exception e) {
                    notifications.error(e.getMessage(), e);
                } finally {
                    setEnabledComponents(true);
                    modelerServiceLevel.getjProgressBar().setIndeterminate(false);
                }

                return null;
            }
        }.execute();
    }

    //==========================================================================
    @Override
    public void setVisible(boolean flag) throws Exception {
        try {
            modelerServiceLevel.setVisible(flag);
        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }
    }

    //==========================================================================
    @Override
    public void setEnabledComponents(boolean flag) throws Exception {
        modelerServiceLevel.getjButton2().setEnabled(flag);
    }

    //==========================================================================
    @Override
    public void addListenersButtons() throws Exception {

        //save -----------------------------------------------------------------
        modelerServiceLevel.getjButton2().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    actionButtonCreateInWindow();
                } catch (Exception ex) {
                    new Thrower().exception(ex);
                }
            }
        });

        //close ----------------------------------------------------------------
        modelerServiceLevel.getjButton1().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                modelerServiceLevel.setVisible(false);
            }
        });

        //Clasifier-------------------------------------------------------------
        modelerServiceLevel.getjComboBoxClasifier().addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {

                new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws Exception {

                        String selected = null;

                        try {

                            selected = modelerServiceLevel.getjComboBoxClasifier().getModel().getSelectedItem().toString();

                            System.out.println("selected " + selected);

                            modelerServiceLevel.getjPanelHTB().setVisible(false);
                            modelerServiceLevel.getjPanelCBQ().setVisible(false);
                            modelerServiceLevel.getjPanelHFSC().setVisible(false);

                            if (selected.equalsIgnoreCase("HTB")) {
                                modelerServiceLevel.getjPanelHTB().setVisible(true);
                                modelerServiceLevel.repaint();
                                return null;
                            }

                            if (selected.equalsIgnoreCase("CBQ")) {
                                modelerServiceLevel.getjPanelCBQ().setVisible(true);
                                modelerServiceLevel.repaint();
                                return null;
                            }

                            if (selected.equalsIgnoreCase("HFSC")) {
                                modelerServiceLevel.getjPanelHFSC().setVisible(true);
                                modelerServiceLevel.repaint();
                                return null;
                            }



                        } catch (Exception e) {
                            notifications.error(e.getMessage(), e);
                        }

                        return null;
                    }
                }.execute();

            }
        });

        //QueuingDiscipline-----------------------------------------------------
        modelerServiceLevel.getjComboBoxQueuingDiscipline().addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {

                new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws Exception {

                        String selected = null;

                        try {

                            selected = modelerServiceLevel.getjComboBoxQueuingDiscipline().getModel().getSelectedItem().toString();

                            System.out.println("selected " + selected);

                            modelerServiceLevel.getjPanelNETEM().setVisible(false);
                            modelerServiceLevel.getjPanelESFQ().setVisible(false);

                            if (selected.equalsIgnoreCase("ESFQ")) {
                                modelerServiceLevel.getjPanelESFQ().setVisible(true);
                                modelerServiceLevel.repaint();
                                return null;
                            }

                            if (selected.equalsIgnoreCase("NETEM")) {
                                modelerServiceLevel.getjPanelNETEM().setVisible(true);
                                modelerServiceLevel.repaint();
                                return null;
                            }

                        } catch (Exception e) {
                            notifications.error(e.getMessage(), e);
                        }

                        return null;
                    }
                }.execute();

            }
        });


    }

    //==========================================================================
    @Override
    public void actionButtonCreateInWindow() throws Exception {

        setEnabledComponents(false);
        modelerServiceLevel.getjProgressBar().setIndeterminate(true);

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                ArrayList arrayList = null;
                HashMap hashMap = new PieceUtilities().subPieceToHashMap(new SubPiece());
                ArrayList data = new ArrayList();

                try {

                    data.add("null");

                    data.add(modelerServiceLevel.getjTextFieldName().getText());
                    data.add(modelerServiceLevel.getjComboBoxClasifier().getModel().getSelectedItem().toString());
                    data.add(modelerServiceLevel.getjTextFieldInBandwidth().getText());
                    data.add(modelerServiceLevel.getjTextFieldInBandwidthCeil().getText());
                    data.add(modelerServiceLevel.getjTextFieldInBandwidthBurst().getText());
                    data.add(modelerServiceLevel.getjTextFieldOutBandwidth().getText());
                    data.add(modelerServiceLevel.getjTextFieldOutBandwidthCeil().getText());
                    data.add(modelerServiceLevel.getjTextFieldOutBandwidthBurst().getText());
                    data.add(modelerServiceLevel.getjComboBoxPriority().getModel().getSelectedItem().toString());

                    data.add(modelerServiceLevel.getjTextFieldCBQInBandwidth().getText());
                    data.add(modelerServiceLevel.getjComboBoxInPriority().getModel().getSelectedItem().toString());

                    data.add(modelerServiceLevel.getjTextFieldCBQOutBandwidth().getText());
                    data.add(modelerServiceLevel.getjComboBoxOutPriority().getModel().getSelectedItem().toString());

                    data.add(modelerServiceLevel.getjRadioButtonBoundedYes().isSelected());

                    data.add(modelerServiceLevel.getjTextFieldInWorkUnit().getText());
                    data.add(modelerServiceLevel.getjTextFieldInMaxDelay().getText());
                    data.add(modelerServiceLevel.getjTextFieldInulRate().getText());
                    data.add(modelerServiceLevel.getjTextFieldHFSCInulRate().getText());
                    data.add(modelerServiceLevel.getjTextFieldOutWorkUnit().getText());
                    data.add(modelerServiceLevel.getjTextFieldOutMaxDelay().getText());
                    data.add(modelerServiceLevel.getjTextFieldOutRate().getText());
                    data.add(modelerServiceLevel.getjTextFieldOutulRate().getText());

                    data.add(modelerServiceLevel.getjComboBoxQueuingDiscipline().getModel().getSelectedItem().toString());
                    data.add(modelerServiceLevel.getjTextFieldPerturb().getText());

                    data.add(modelerServiceLevel.getjTextFieldLimit().getText());
                    data.add(modelerServiceLevel.getjTextFieldDepth().getText());
                    data.add(modelerServiceLevel.getjTextFieldDivisor().getText());
                    data.add(modelerServiceLevel.getjComboBoxHash().getModel().getSelectedItem().toString());
                    data.add(modelerServiceLevel.getjTextFieldDelay().getText());
                    data.add(modelerServiceLevel.getjTextFieldJitter().getText());
                    data.add(modelerServiceLevel.getjTextFieldCorrelation().getText());
                    data.add(modelerServiceLevel.getjComboBoxDistribution().getModel().getSelectedItem().toString());
                    data.add(modelerServiceLevel.getjTextFieldPacketloss().getText());
                    data.add(modelerServiceLevel.getjTextFieldDuplication().getText());

                    data.add(modelerServiceLevel.getjTextFieldGap().getText());
                    data.add(modelerServiceLevel.getjTextFieldReorderPercentage().getText());
                    data.add(modelerServiceLevel.getjTextFieldReorderCorrelation().getText());

                    hashMap.put("data service level", data);

                    arrayList = createGeneric("service level", hashMap);

                    if (!arrayList.get(0).equals(true)) {
                        notifications.error(arrayList.get(1).toString(), new Exception());
                    } else {
                        modelerServiceLevel.setVisible(false);
                    }

                } catch (Exception e) {
                    throw e;
                } finally {
                    setEnabledComponents(true);
                    modelerServiceLevel.getjProgressBar().setIndeterminate(false);
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

        modelerServiceLevel.getjButton1().setText("close");
        modelerServiceLevel.getjButton2().setText("create");

    }

    //==========================================================================
    @Override
    public void putButtonsTable() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
