/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tampilan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;

/**
 *
 * @author dayen
 */
public class MasterItemForm extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MasterItemForm.class.getName());

    private Connection conn;

    /**
     * Creates new form MasterItemForm
     */
    public MasterItemForm() {
        initComponents();
        conn = Koneksi.connect();
        loadCombo();
        datatable();
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked();
            }
        });
    }

    private void kosong() {
        txtItemNumber.setText("");
        txtItemDesc.setText("");
        txtCategoryId.setText("");
        txtOutletId.setText("");
        txtBaseUoM.setText("");
        txtPurchUoM.setText("");
        txtQtyPurchUoM.setText("");
        txtStandCost.setText("");
        txtCurrCost.setText("");
        if (jComboBoxItemClass.getItemCount() > 0)   jComboBoxItemClass.setSelectedIndex(0);
        if (jComboBoxStatus.getItemCount() > 0)      jComboBoxStatus.setSelectedIndex(0);
        if (jComboBoxCategory.getItemCount() > 0)    jComboBoxCategory.setSelectedIndex(0);
        if (jComboBoxOutletName.getItemCount() > 0)  jComboBoxOutletName.setSelectedIndex(0);
        if (jComboBoxClassification.getItemCount() > 0) jComboBoxClassification.setSelectedIndex(0);
        aktif();
    }

    private void aktif() {
        txtItemNumber.requestFocus();
    }

    private void loadCombo() {
        if (conn == null) return;
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT name FROM category ORDER BY name")) {
            jComboBoxCategory.removeAllItems();
            while (rs.next()) {
                jComboBoxCategory.addItem(rs.getString("name"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal load Category: " + e.getMessage());
        }
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT name FROM user_classification ORDER BY name")) {
            jComboBoxClassification.removeAllItems();
            while (rs.next()) {
                jComboBoxClassification.addItem(rs.getString("name"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal load Classification: " + e.getMessage());
        }
    }

    private void datatable() {
        if (conn == null) return;
        String[] cols = {
            "Item Number", "Description", "Item Class", "UoM Schedule",
            "Base UoM", "Purch UoM", "Std Cost", "Curr Cost",
            "Qty Purch UoM", "Price List", "Status", "User Class",
            "Category", "Outlet"
        };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM item ORDER BY item_number")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("item_number"),
                    rs.getString("item_description"),
                    rs.getString("item_class"),
                    rs.getString("uom_schedule"),
                    rs.getString("base_uom"),
                    rs.getString("purchasing_uom"),
                    rs.getBigDecimal("standard_cost"),
                    rs.getBigDecimal("current_cost"),
                    rs.getBigDecimal("qty_purchase_uom"),
                    rs.getBigDecimal("price_list"),
                    rs.getString("status"),
                    rs.getString("user_classification"),
                    rs.getString("item_category"),
                    rs.getString("outlet_name")
                });
            }
            jTable1.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal load tabel: " + e.getMessage());
        }
    }

    private double parseNum(String s) {
        if (s == null) return 0;
        s = s.trim().replace(",", ".");
        if (s.isEmpty()) return 0;
        try { return Double.parseDouble(s); } catch (NumberFormatException e) { return 0; }
    }

    private void tableMouseClicked() {
        int row = jTable1.getSelectedRow();
        if (row < 0) return;
        txtItemNumber.setText(String.valueOf(jTable1.getValueAt(row, 0)));
        txtItemDesc.setText(String.valueOf(jTable1.getValueAt(row, 1)));
        jComboBoxItemClass.setSelectedItem(String.valueOf(jTable1.getValueAt(row, 2)));
        txtQtyPurchUoM.setText(String.valueOf(jTable1.getValueAt(row, 3)));
        txtCategoryId.setText(String.valueOf(jTable1.getValueAt(row, 4)));
        txtOutletId.setText(String.valueOf(jTable1.getValueAt(row, 5)));
        txtBaseUoM.setText(String.valueOf(jTable1.getValueAt(row, 6)));
        txtPurchUoM.setText(String.valueOf(jTable1.getValueAt(row, 7)));
        txtStandCost.setText(String.valueOf(jTable1.getValueAt(row, 8)));
        txtCurrCost.setText(String.valueOf(jTable1.getValueAt(row, 9)));
        Object st = jTable1.getValueAt(row, 10);
        jComboBoxStatus.setSelectedItem(st == null ? "YES" : st.toString().equalsIgnoreCase("Yes") ? "YES" : "NO");
        jComboBoxClassification.setSelectedItem(String.valueOf(jTable1.getValueAt(row, 11)));
        jComboBoxCategory.setSelectedItem(String.valueOf(jTable1.getValueAt(row, 12)));
        jComboBoxOutletName.setSelectedItem(String.valueOf(jTable1.getValueAt(row, 13)));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtItemNumber = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtBaseUoM = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtOutletId = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jComboBoxCategory = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jComboBoxStatus = new javax.swing.JComboBox<>();
        txtCategoryId = new javax.swing.JTextField();
        txtPurchUoM = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtQtyPurchUoM = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtStandCost = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtCurrCost = new javax.swing.JTextField();
        btnDelete = new java.awt.Button();
        btnReset = new java.awt.Button();
        btnAdd = new java.awt.Button();
        scrollbar1 = new java.awt.Scrollbar();
        btnUpdate = new java.awt.Button();
        jComboBoxItemClass = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jComboBoxClassification = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        txtPriceList = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        txtItemDesc = new javax.swing.JTextField();

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Master Item Form");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Item Description");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Item number");

        txtItemNumber.addActionListener(this::txtItemNumberActionPerformed);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Item class");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Category ID");

        txtBaseUoM.addActionListener(this::txtBaseUoMActionPerformed);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Outlet ID");

        txtOutletId.addActionListener(this::txtOutletIdActionPerformed);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Base UoM");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Category");

        jComboBoxCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CENTRAL KITCHEN", "DIRECT ITEM", "FOOD", "NON FOOD", "OTHERS" }));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Status");

        jComboBoxStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "NonActive" }));
        jComboBoxStatus.addActionListener(this::jComboBoxStatusActionPerformed);

        txtCategoryId.addActionListener(this::txtCategoryIdActionPerformed);

        txtPurchUoM.addActionListener(this::txtPurchUoMActionPerformed);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Purchasing UoM");

        txtQtyPurchUoM.addActionListener(this::txtQtyPurchUoMActionPerformed);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Qty Purchase UoM");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Standard Cost");

        txtStandCost.addActionListener(this::txtStandCostActionPerformed);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Current Cost");

        txtCurrCost.addActionListener(this::txtCurrCostActionPerformed);

        btnDelete.setLabel("Delete Item");
        btnDelete.addActionListener(this::btnDeleteActionPerformed);

        btnReset.setLabel("Reset");
        btnReset.addActionListener(this::btnResetActionPerformed);

        btnAdd.setLabel("Add Item");
        btnAdd.addActionListener(this::btnAddActionPerformed);

        btnUpdate.setActionCommand("Update Item");
        btnUpdate.setLabel("Update Item");
        btnUpdate.addActionListener(this::btnUpdateActionPerformed);

        jComboBoxItemClass.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ALKOHOL", "ASSET", "ATK", "EQUIPMENT", "CHEMICAL", "CK", "DESSERT", "DRY GOODS", "IKAN", "OLAHAN", "DRY CHINA", "GARNISH", "SAYUR", "NONALKOHOL", "PACKAGING", "UTENSIL", "SAKE WINE", "SOFTDRINK", "SUPPLIES", "UNIFORM", "UTN IMP", "ALKOHOL", "ASSET", "ATK", "EQUIPMENT", "CHEMICAL", "CK", "DESSERT", "DRY GOODS", "IKAN", "OLAHAN", "DRY CHINA", "GARNISH", "SAYUR", "NONALKOHOL", "PACKAGING", "UTENSIL", "SAKE WINE", "SOFTDRINK", "SUPPLIES", "UNIFORM", "UTN IMP" }));
        jComboBoxItemClass.addActionListener(this::jComboBoxItemClassActionPerformed);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("User Classification");

        jComboBoxClassification.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "FOOD", "NON-FOOD" }));
        jComboBoxClassification.addActionListener(this::jComboBoxClassificationActionPerformed);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Price List");

        txtPriceList.addActionListener(this::txtPriceListActionPerformed);

        jTextField1.setText("jTextField1");

        txtItemDesc.addActionListener(this::txtItemDescActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(127, 127, 127)
                .addComponent(scrollbar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtItemNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jComboBoxItemClass, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(txtPurchUoM, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jComboBoxCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(txtQtyPurchUoM, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtOutletId, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtPriceList, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(226, 226, 226)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBoxClassification, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtCategoryId, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtItemDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtBaseUoM, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtCurrCost, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtStandCost, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 155, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtItemNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxItemClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxClassification, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCategoryId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(txtPurchUoM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtQtyPurchUoM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtOutletId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(txtStandCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(119, 119, 119)
                                .addComponent(scrollbar1, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtPriceList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(38, 38, 38)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(196, 196, 196)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBaseUoM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtItemDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCurrCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtItemNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtItemNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtItemNumberActionPerformed

    private void txtBaseUoMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBaseUoMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBaseUoMActionPerformed

    private void txtOutletIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOutletIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOutletIdActionPerformed

    private void txtCategoryIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCategoryIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCategoryIdActionPerformed

    private void txtPurchUoMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPurchUoMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPurchUoMActionPerformed

    private void txtQtyPurchUoMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQtyPurchUoMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQtyPurchUoMActionPerformed

    private void txtStandCostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStandCostActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStandCostActionPerformed

    private void txtCurrCostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCurrCostActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCurrCostActionPerformed

    private void jComboBoxStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxStatusActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        String key = txtItemNumber.getText().trim();
        if (key.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih baris atau isi Item Number dulu");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "Hapus item " + key + " ?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;
        String sql = "DELETE FROM item WHERE item_number=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, key);
            int n = ps.executeUpdate();
            if (n > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
                kosong();
                datatable();
            } else {
                JOptionPane.showMessageDialog(this, "Item tidak ditemukan");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Data gagal dihapus: " + e.getMessage());
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        kosong();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        if (txtItemNumber.getText().trim().isEmpty() || txtItemDesc.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Item Number dan Description wajib diisi");
            return;
        }
        String sql = "INSERT INTO item (item_number, item_description, item_class, uom_schedule, "
                   + "base_uom, purchasing_uom, standard_cost, current_cost, qty_purchase_uom, "
                   + "price_list, status, user_classification, item_category, outlet_name) "
                   + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtItemNumber.getText().trim());
            ps.setString(2, txtItemDesc.getText().trim());
            ps.setString(3, String.valueOf(jComboBoxItemClass.getSelectedItem()));
            ps.setString(4, txtQtyPurchUoM.getText().trim());
            ps.setString(5, txtCategoryId.getText().trim());
            ps.setString(6, txtOutletId.getText().trim());
            ps.setDouble(7, parseNum(txtBaseUoM.getText()));
            ps.setDouble(8, parseNum(txtPurchUoM.getText()));
            ps.setDouble(9, parseNum(txtStandCost.getText()));
            ps.setDouble(10, parseNum(txtCurrCost.getText()));
            ps.setString(11, "YES".equalsIgnoreCase(String.valueOf(jComboBoxStatus.getSelectedItem())) ? "Yes" : "No");
            ps.setString(12, String.valueOf(jComboBoxClassification.getSelectedItem()));
            ps.setString(13, String.valueOf(jComboBoxCategory.getSelectedItem()));
            ps.setString(14, String.valueOf(jComboBoxOutletName.getSelectedItem()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan");
            kosong();
            datatable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Data gagal disimpan: " + e.getMessage());
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        String key = txtItemNumber.getText().trim();
        if (key.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih baris dulu untuk update");
            return;
        }
        String sql = "UPDATE item SET item_description=?, item_class=?, uom_schedule=?, "
                   + "base_uom=?, purchasing_uom=?, standard_cost=?, current_cost=?, "
                   + "qty_purchase_uom=?, price_list=?, status=?, user_classification=?, "
                   + "item_category=?, outlet_name=? "
                   + "WHERE item_number=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtItemDesc.getText().trim());
            ps.setString(2, String.valueOf(jComboBoxItemClass.getSelectedItem()));
            ps.setString(3, txtQtyPurchUoM.getText().trim());
            ps.setString(4, txtCategoryId.getText().trim());
            ps.setString(5, txtOutletId.getText().trim());
            ps.setDouble(6, parseNum(txtBaseUoM.getText()));
            ps.setDouble(7, parseNum(txtPurchUoM.getText()));
            ps.setDouble(8, parseNum(txtStandCost.getText()));
            ps.setDouble(9, parseNum(txtCurrCost.getText()));
            ps.setString(10, "YES".equalsIgnoreCase(String.valueOf(jComboBoxStatus.getSelectedItem())) ? "Yes" : "No");
            ps.setString(11, String.valueOf(jComboBoxClassification.getSelectedItem()));
            ps.setString(12, String.valueOf(jComboBoxCategory.getSelectedItem()));
            ps.setString(13, String.valueOf(jComboBoxOutletName.getSelectedItem()));
            ps.setString(14, key);
            int n = ps.executeUpdate();
            if (n > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diubah");
                kosong();
                datatable();
            } else {
                JOptionPane.showMessageDialog(this, "Item tidak ditemukan");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Data gagal diubah: " + e.getMessage());
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void jComboBoxItemClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxItemClassActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxItemClassActionPerformed

    private void jComboBoxClassificationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxClassificationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxClassificationActionPerformed

    private void txtPriceListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPriceListActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPriceListActionPerformed

    private void txtItemDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtItemDescActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtItemDescActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MasterItemForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button btnAdd;
    private java.awt.Button btnDelete;
    private java.awt.Button btnReset;
    private java.awt.Button btnUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JComboBox<String> jComboBoxCategory;
    private javax.swing.JComboBox<String> jComboBoxClassification;
    private javax.swing.JComboBox<String> jComboBoxItemClass;
    private javax.swing.JComboBox<String> jComboBoxStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jTextField1;
    private java.awt.Scrollbar scrollbar1;
    private javax.swing.JTextField txtBaseUoM;
    private javax.swing.JTextField txtCategoryId;
    private javax.swing.JTextField txtCurrCost;
    private javax.swing.JTextField txtItemDesc;
    private javax.swing.JTextField txtItemNumber;
    private javax.swing.JTextField txtOutletId;
    private javax.swing.JTextField txtPriceList;
    private javax.swing.JTextField txtPurchUoM;
    private javax.swing.JTextField txtQtyPurchUoM;
    private javax.swing.JTextField txtStandCost;
    // End of variables declaration//GEN-END:variables
}
