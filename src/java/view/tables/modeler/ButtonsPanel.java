package view.tables.modeler;

import javax.swing.JButton;
import javax.swing.JPanel;
import view.util.SwingUtilities;

/**
 *
 * @author skuarch
 */
public class ButtonsPanel extends JPanel {

    //public final java.util.List<JButton> buttons = java.util.Arrays.asList(new JButton("delete"), new JButton("edit"));
    private JButton deleteButton = SwingUtilities.getDeleteButton();    
    private JButton editButton = SwingUtilities.getEditButton();

    //==========================================================================
    public ButtonsPanel() {
        super();
        setOpaque(true);
        add(deleteButton);        
        add(editButton);
    }

    //==========================================================================
    public JButton getDeleteButton() {
        return deleteButton;
    }

    //==========================================================================
    public void setDeleteButton(JButton deleteButton) {
        this.deleteButton = deleteButton;
    }

    //==========================================================================
    public JButton getEditButton() {
        return editButton;
    }

    //==========================================================================
    public void setEditButton(JButton editButton) {
        this.editButton = editButton;
    }
}
