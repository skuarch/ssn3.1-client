package controllers.modeler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.SwingWorker;
import view.dialogs.modeler.ModelerInformer;

/**
 *
 * @author skuarch
 */
public class ControllerModelerInformer extends Modeler {

    private ModelerInformer modelerInformer = new ModelerInformer(null, true);
    private String type = null;

    //==========================================================================
    public ControllerModelerInformer(String type) throws Exception {
        super();
        this.type = type;
    } // end ControllerModelerInformer

    //==========================================================================
    @Override
    public void setupInterface() throws Exception {

        setupButtonsInWindow();
        addListenersButtons();
        modelerInformer.getjProgressBar().setIndeterminate(true);
        modelerInformer.setTitle(type);

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                ArrayList arrayList = null;
                
                try {
                    
                    arrayList = requestDefaultData(type);
                    
                    if(arrayList == null){
                        return null;
                    }
                    
                    System.out.println(arrayList.get(0).toString());
                    
                    modelerInformer.getjTextArea().setText(arrayList.get(0).toString());                    
                    
                } catch (Exception e) {
                    notifications.error(e.getMessage(), e);
                } finally {
                    modelerInformer.getjProgressBar().setIndeterminate(false);
                }

                return null;
            }
        }.execute();

    } // end setupInterface

    //==========================================================================
    @Override
    public void setVisible(boolean flag) throws Exception {
        modelerInformer.setVisible(flag);
    }

    //==========================================================================
    @Override
    public void setEnabledComponents(boolean flag) throws Exception {
    }

    //==========================================================================
    @Override
    public void addListenersButtons() throws Exception {
        modelerInformer.getjButton1().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                modelerInformer.setVisible(false);
            }
        });
    }

    //==========================================================================
    @Override
    public void actionButtonCreateInWindow() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
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
        modelerInformer.getjButton1().setText("close");
    }

    //==========================================================================
    @Override
    public void putButtonsTable() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
} // end ControllerModelerInformer
