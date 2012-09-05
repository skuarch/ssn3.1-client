package controllers.modeler;

import controllers.ControllerConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import model.beans.Configuration;
import model.beans.SubPiece;
import model.jms.JMSProccessor;
import model.util.PieceUtilities;
import view.notifications.Notifications;
import view.util.SwingUtilities;

/**
 *
 * @author skuarch
 */
public abstract class Modeler {

    private Configuration configuration = null;
    private SwingUtilities su = new SwingUtilities();
    protected static final Notifications notifications = new Notifications();

    //==========================================================================
    public Modeler() throws Exception {
        configuration = new ControllerConfiguration().getInitialConfiguration();
    } // end ControllerGeneric

    //==========================================================================
    /**
     * setup the interface.
     * @throws Exception 
     */
    public abstract void setupInterface() throws Exception;

    //==========================================================================
    /**
     * show the window
     * @param flag boolean
     * @throws Exception 
     */
    public abstract void setVisible(boolean flag) throws Exception;

    //==========================================================================
    /**
     * enabled or disabled components
     * @param flag boolean
     * @throws Exception 
     */
    public abstract void setEnabledComponents(boolean flag) throws Exception;

    //==========================================================================
    /**
     * add listener to buttons
     * @throws Exception 
     */
    public abstract void addListenersButtons() throws Exception;

    //==========================================================================
    /**
     * this method must called when button save on a window is pressed.
     * @throws Exception 
     */
    public abstract void actionButtonCreateInWindow() throws Exception;

    //==========================================================================
    /**
     * this method must called when button edit in a table is pressed.
     * @throws Exception 
     */
    public abstract void actionButtonEditInTable() throws Exception;

    //==========================================================================
    /**
     * this method must called when button delete in a table is pressed.
     * @throws Exception 
     */
    public abstract void actionButtonDeleteInTable() throws Exception;
    
    //==========================================================================
    /**
     * configure text for buttons
     * @throws Exception 
     */
    public abstract void setupButtonsInWindow() throws Exception;

    //==========================================================================
    /**
     * request arrayList to modeler server with string getDefaultData.
     * @param type
     * @return ArrayList
     * @throws Exception 
     */
    public ArrayList requestDefaultData(String type) throws Exception {

        ArrayList arrayList = new ArrayList();
        HashMap hashMap = null;

        try {

            hashMap = new PieceUtilities().subPieceToHashMap(new SubPiece());
            hashMap.put("getDefaultData", type);
            arrayList = (ArrayList) new JMSProccessor().sendReceive(
                    type,
                    configuration.getMasterShaperServer(),
                    "srs",
                    configuration.getJmsTimeWaitMessage(), hashMap);

        } catch (Exception e) {
            throw e;
        }

        return arrayList;
    } // end requestDefaultData

    //==========================================================================
    /**
     * request arrayList to modeler server with string getData.
     * @param id String
     * @param type String
     * @return ArrayList
     * @throws Exception 
     */
    public ArrayList requestData(String id, String type) throws Exception {

        ArrayList arrayList = new ArrayList();
        HashMap hashMap = null;

        try {

            hashMap = new PieceUtilities().subPieceToHashMap(new SubPiece());
            hashMap.put("getData " + type, id);
            arrayList = (ArrayList) new JMSProccessor().sendReceive(
                    type,
                    configuration.getMasterShaperServer(),
                    "srs",
                    configuration.getJmsTimeWaitMessage(), hashMap);

        } catch (Exception e) {
            throw e;
        }

        return arrayList;

    } // end requestData

    //==========================================================================
    /**
     * request arrayList to modeler server with string getAllData.
     * @param type String
     * @return ArrayList
     * @throws Exception 
     */
    public ArrayList requestAllData(String type) throws Exception {

        ArrayList arrayList = new ArrayList();
        HashMap hashMap = null;

        try {
            System.out.println("nuevo " + type);
            hashMap = new PieceUtilities().subPieceToHashMap(new SubPiece());
            hashMap.put("getAllData", type);
            arrayList = (ArrayList) new JMSProccessor().sendReceive(
                    type,
                    configuration.getMasterShaperServer(),
                    "srs",
                    configuration.getJmsTimeWaitMessage(), hashMap);

        } catch (Exception e) {
            throw e;
        }

        return arrayList;
    } // end requestAllData
    
    //==========================================================================
    public ArrayList requestDelete(String id, String type) throws Exception {

        ArrayList arrayList = new ArrayList();
        HashMap hashMap = null;

        try {

            hashMap = new PieceUtilities().subPieceToHashMap(new SubPiece());
            hashMap.put("delete " + type, id);
            arrayList = (ArrayList) new JMSProccessor().sendReceive(
                    type,
                    configuration.getMasterShaperServer(),
                    "srs",
                    configuration.getJmsTimeWaitMessage(), hashMap);

        } catch (Exception e) {
            throw e;
        }

        return arrayList;

    } // end requestData

    //==========================================================================
    /**
     * send message to modeler server with string delete.
     * this method is called from a table
     * @param id
     * @param type
     * @return
     * @throws Exception 
     */
    public ArrayList deleteGeneric(String id, String type) throws Exception {

        ArrayList arrayList = new ArrayList();
        HashMap hashMap = null;

        try {

            hashMap = new PieceUtilities().subPieceToHashMap(new SubPiece());
            hashMap.put("delete " + type, id);
            arrayList = (ArrayList) new JMSProccessor().sendReceive(
                    type,
                    configuration.getMasterShaperServer(),
                    "srs",
                    configuration.getJmsTimeWaitMessage(), hashMap);

        } catch (Exception e) {
            throw e;
        }

        return arrayList;

    } // end deleteGeneric

    //==========================================================================
    /**
     * send message to modeler server with string edit.
     * this method is called from a table
     * @param id
     * @param type
     * @param hashMap
     * @return
     * @throws Exception 
     */
    public ArrayList editGeneric(String id, String type, HashMap hashMap) throws Exception {

        ArrayList arrayList = new ArrayList();

        try {

            hashMap.put("edit " + type, id);
            arrayList = (ArrayList) new JMSProccessor().sendReceive(
                    type,
                    configuration.getMasterShaperServer(),
                    "srs",
                    configuration.getJmsTimeWaitMessage(), hashMap);

        } catch (Exception e) {
            throw e;
        }

        return arrayList;

    } // end createGeneric

    //==========================================================================
    /**
     * send message to modeler server with string create.
     * this method is called from a window
     * @param type
     * @param hashMap
     * @return
     * @throws Exception 
     */
    public ArrayList createGeneric(String type, HashMap hashMap) throws Exception {

        ArrayList arrayList = new ArrayList();

        try {

            hashMap.put("create " + type, "null");            
            
            arrayList = (ArrayList) new JMSProccessor().sendReceive(
                    type,
                    configuration.getMasterShaperServer(),
                    "srs",
                    configuration.getJmsTimeWaitMessage(), hashMap);

        } catch (Exception e) {
            throw e;
        }

        return arrayList;

    } // end createGeneric
    
    //==========================================================================
    /**
     * default names for chains.
     *
     * @return String[]
     */
    public String[] getColumnNames(String type) throws Exception {

        String[] columnNames = null;

        try {

            if (type.equals("chains")) {
                columnNames = new String[]{"id", "Chain name", "Service level", "Fallback", "Status", "Options"};
            } else if (type.equals("pipes")) {
                columnNames = new String[]{"id", "Pipes", "Chains", "Filters","Status", "Options"};
            } else if (type.equals("filters")) {
                columnNames = new String[]{"id", "Filters", "Status", "Options"};
            }else if (type.equals("targets")) {
                columnNames = new String[]{"id", "Targets", "Detail", "Options"};
            }else if (type.equals("ports")) {
                columnNames = new String[]{"id", "Name", "Description", "Port-Number","Options"};
            }else if (type.equals("protocols")) {
                columnNames = new String[]{"id", "Name", "Number", "Options"};
            }else if (type.equals("service level")) {
                columnNames = new String[]{"id", "Service level", "Qdisc Parameters", "Options"};
            }

        } catch (Exception e) {
            throw e;
        }

        return columnNames;

    } // end getColumnNames

    //==========================================================================
    public Configuration getConfiguration() {
        return configuration;
    } // end getConfiguration

    //==========================================================================
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    } // end setConfiguration

    //==========================================================================
    public void fillJComboBox(JComboBox jComboBox, Object[] objects) throws Exception {
        su.fillJComboBox(jComboBox, objects);
    } // end fillJComboBox

    //==========================================================================
    public void fillJList(JList list, Object[] objects) throws Exception {
        su.fillJlist(list, objects);
    } // end fillJList

    //==========================================================================
    public void passItemsLeftRigth(JList left, JList right) throws Exception {
        su.passItemsLeftRigth(left, right);
    } // end passItemsLeftRigth

    //==========================================================================
    public Object[] getDataJList(JList list) throws Exception {
        return su.getDataJList(list);
    } // end getDataJList

    //==========================================================================
    /**
     * delete row in table.
     *
     * @param row int
     * @throws Exception
     */
    public void deleteRow(int row, JTable table) throws Exception {
        su.deleteTableRow(row, table);
    } // end deleteRow
    
    //==========================================================================
    /**
     * this method is only for controllers with tables
     */
    public abstract void putButtonsTable() throws Exception;
    
    //==========================================================================
    public void setTableModel(Object[][] objects, String[] columNames, JTable table)throws Exception{
        su.setTableModel(objects, columNames, table);        
    } // end
    
}  // end class