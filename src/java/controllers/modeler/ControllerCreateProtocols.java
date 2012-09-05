package controllers.modeler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.SwingWorker;
import model.beans.SubPiece;
import model.util.PieceUtilities;
import model.util.Thrower;
import view.dialogs.modeler.ModelerProtocols;

/**
 *
 * @author skuarch
 */
public class ControllerCreateProtocols extends Modeler {

    private ModelerProtocols modelerProtocols = new ModelerProtocols(null, true);

    //==========================================================================
    public ControllerCreateProtocols() throws Exception {
        super();
    } //end ControllerCreateProtocols

    //==========================================================================
    @Override
    public void setupInterface() throws Exception {

        try {

            addListenersButtons();
            setupButtonsInWindow();

        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }

    } // end setupInterface

    //==========================================================================
    @Override
    public void setVisible(boolean flag) throws Exception {

        try {
            modelerProtocols.setVisible(flag);
        } catch (Exception e) {
            notifications.error(e.getMessage(), e);
        }

    } // end setVisible

    //==========================================================================
    @Override
    public void setEnabledComponents(boolean flag) throws Exception {
        modelerProtocols.getjButton2().setEnabled(flag);
    }

    //==========================================================================
    @Override
    public void addListenersButtons() throws Exception {
        //save -----------------------------------------------------------------
        modelerProtocols.getjButton2().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    actionButtonCreateInWindow();
                } catch (Exception ex) {
                    new Thrower().exception(ex);
                }
            }
        });

        //close ----------------------------------------------------------------
        modelerProtocols.getjButton1().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                modelerProtocols.setVisible(false);
            }
        });
    }

    //==========================================================================
    @Override
    public void actionButtonCreateInWindow() throws Exception {

        setEnabledComponents(false);
        modelerProtocols.getjProgressBar().setIndeterminate(true);

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                ArrayList arrayList = null;
                HashMap hashMap = new PieceUtilities().subPieceToHashMap(new SubPiece());
                ArrayList data = new ArrayList();

                try {

                    data.add("null");
                    data.add(modelerProtocols.getjTextFieldName().getText());
                    data.add(modelerProtocols.getjTextFieldNumber().getText());

                    hashMap.put("data protocols", data);

                    arrayList = createGeneric("protocols", hashMap);

                    if (!arrayList.get(0).equals(true)) {
                        notifications.error(arrayList.get(1).toString(), new Exception());
                    } else {
                        
                        ControllerModelerTableSettings cmtm = new ControllerModelerTableSettings("protocols");
                        cmtm.setupInterface();
                        modelerProtocols.setVisible(false);
                        cmtm.setVisible(true);             
                        
                    }

                } catch (Exception e) {
                    throw e;
                } finally {
                    setEnabledComponents(true);
                    modelerProtocols.getjProgressBar().setIndeterminate(false);
                }

                return null;
            }
        }.execute();

    } // end actionButtonCreateInWindow

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
        modelerProtocols.getjButton1().setText("close");
        modelerProtocols.getjButton2().setText("create");
    }

    //==========================================================================
    @Override
    public void putButtonsTable() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
} // end ControllerCreateProtocols
