package controllers.modeler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.SwingWorker;
import model.beans.SubPiece;
import model.util.PieceUtilities;
import view.dialogs.modeler.ModelerPorts;

/**
 *
 * @author skuarch
 */
public class ControllerCreatePorts extends Modeler {

    private ModelerPorts modelerPorts = new ModelerPorts(null, true);

    //==========================================================================
    public ControllerCreatePorts() throws Exception {
        super();
    } // end ControllerCreatePorts

    //==========================================================================
    @Override
    public void setupInterface() throws Exception {

        setupButtonsInWindow();
        addListenersButtons();

    } // end setupInterface

    //==========================================================================
    @Override
    public void setVisible(boolean flag) throws Exception {
        try {
            modelerPorts.setVisible(flag);
        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }
    }

    //==========================================================================
    @Override
    public void setEnabledComponents(boolean flag) throws Exception {
        modelerPorts.getjButton2().setEnabled(flag);
    }

    //==========================================================================
    @Override
    public void addListenersButtons() throws Exception {

        modelerPorts.getjButton1().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                modelerPorts.setVisible(false);
            }
        });

        modelerPorts.getjButton2().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                try {
                    actionButtonCreateInWindow();
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
        modelerPorts.getjProgressBar().setIndeterminate(true);

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                ArrayList arrayList = null;
                HashMap hashMap = new PieceUtilities().subPieceToHashMap(new SubPiece());
                ArrayList data = new ArrayList();

                try {

                    data.add("null");
                    data.add(modelerPorts.getjTextFieldName().getText());
                    data.add(modelerPorts.getjTextFieldDescription().getText());
                    data.add(modelerPorts.getjTextFieldNumber().getText());

                    hashMap.put("data ports", data);

                    arrayList = createGeneric("ports", hashMap);

                    if (!arrayList.get(0).equals(true)) {
                        notifications.error(arrayList.get(1).toString(), new Exception());
                    } else {
                        ControllerModelerTableSettings cmtm = new ControllerModelerTableSettings("ports");
                        cmtm.setupInterface();
                        modelerPorts.setVisible(false);
                        cmtm.setVisible(true);                        
                    }

                } catch (Exception e) {
                    throw e;
                } finally {
                    setEnabledComponents(false);
                    modelerPorts.getjProgressBar().setIndeterminate(true);
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
        modelerPorts.getjButton1().setText("close");
        modelerPorts.getjButton2().setText("create");
    }

    //==========================================================================
    @Override
    public void putButtonsTable() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
} // end ControllerCreatePorts

