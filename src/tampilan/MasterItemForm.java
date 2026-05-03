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
        txtBaseUoM.setText("");
        txtPurchUoM.setText("");
        txtStandardCost.setText("");
        txtCurrCost.setText("");
        txtSchUoM.setText("");
        txtQtyUoM.setText("");
        txtPriceList.setText("");
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
        txtSchUoM.setText(String.valueOf(jTable1.getValueAt(row, 3)));
        txtBaseUoM.setText(String.valueOf(jTable1.getValueAt(row, 4)));
        txtPurchUoM.setText(String.valueOf(jTable1.getValueAt(row, 5)));
        txtStandardCost.setText(String.valueOf(jTable1.getValueAt(row, 6)));
        txtCurrCost.setText(String.valueOf(jTable1.getValueAt(row, 7)));
        txtQtyUoM.setText(String.valueOf(jTable1.getValueAt(row, 8)));
        txtPriceList.setText(String.valueOf(jTable1.getValueAt(row, 9)));
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtItemDesc = new javax.swing.JTextField();
        txtItemNumber = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtStandardCost = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPurchUoM = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jComboBoxCategory = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jComboBoxStatus = new javax.swing.JComboBox<>();
        txtBaseUoM = new javax.swing.JTextField();
        txtCurrCost = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtSchUoM = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtQtyUoM = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtPriceList = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jComboBoxOutletName = new javax.swing.JComboBox<>();
        btnDelete = new java.awt.Button();
        btnReset = new java.awt.Button();
        btnAdd = new java.awt.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        scrollbar1 = new java.awt.Scrollbar();
        btnUpdate = new java.awt.Button();
        jComboBoxItemClass = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jComboBoxClassification = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Master Item Form");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Item Description");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Item number");

        txtItemDesc.addActionListener(this::txtItemDescActionPerformed);

        txtItemNumber.addActionListener(this::txtItemNumberActionPerformed);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Item class");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Base Unit of Measure");

        txtStandardCost.addActionListener(this::txtStandardCostActionPerformed);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Purchasing Unit of Measure");

        txtPurchUoM.addActionListener(this::txtPurchUoMActionPerformed);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Standard Cost");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Category");

        jComboBoxCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CENTRAL KITCHEN", "DIRECT ITEM", "FOOD", "NON FOOD", "OTHERS" }));
        jComboBoxCategory.addActionListener(this::jComboBoxCategoryActionPerformed);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Status");

        jComboBoxStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "YES", "NO" }));
        jComboBoxStatus.addActionListener(this::jComboBoxStatusActionPerformed);

        txtBaseUoM.addActionListener(this::txtBaseUoMActionPerformed);

        txtCurrCost.addActionListener(this::txtCurrCostActionPerformed);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Current Cost");

        txtSchUoM.addActionListener(this::txtSchUoMActionPerformed);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Schedule Unit of Measure");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Qty Purchase UOM");

        txtQtyUoM.addActionListener(this::txtQtyUoMActionPerformed);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Pricelist");

        txtPriceList.addActionListener(this::txtPriceListActionPerformed);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Outlate Name");

        jComboBoxOutletName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SHMGI", "SHSCI", "SHLMN", "SHMTA", "SHNEO" }));

        btnDelete.setLabel("Delete Item");
        btnDelete.addActionListener(this::btnDeleteActionPerformed);

        btnReset.setLabel("Reset");
        btnReset.addActionListener(this::btnResetActionPerformed);

        btnAdd.setLabel("Add Item");
        btnAdd.addActionListener(this::btnAddActionPerformed);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        btnUpdate.setActionCommand("Update Item");
        btnUpdate.setLabel("Update Item");
        btnUpdate.addActionListener(this::btnUpdateActionPerformed);

        jComboBoxItemClass.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ALKOHOL", "ASSET", "ATK", "EQUIPMENT", "CHEMICAL", "CK", "DESSERT", "DRY GOODS", "IKAN", "OLAHAN", "DRY CHINA", "GARNISH", "SAYUR", "NONALKOHOL", "PACKAGING", "UTENSIL", "SAKE WINE", "SOFTDRINK", "SUPPLIES", "UNIFORM", "UTN IMP", "ALKOHOL", "ASSET", "ATK", "EQUIPMENT", "CHEMICAL", "CK", "DESSERT", "DRY GOODS", "IKAN", "OLAHAN", "DRY CHINA", "GARNISH", "SAYUR", "NONALKOHOL", "PACKAGING", "UTENSIL", "SAKE WINE", "SOFTDRINK", "SUPPLIES", "UNIFORM", "UTN IMP" }));
        jComboBoxItemClass.addActionListener(this::jComboBoxItemClassActionPerformed);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("User Classification");

        jComboBoxClassification.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "FOOD", "NON-FOOD" }));
        jComboBoxClassification.addActionListener(this::jComboBoxClassificationActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 771, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollbar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 156, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtPurchUoM, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtCurrCost, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6)
                                            .addComponent(txtQtyUoM, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(46, 46, 46)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtPriceList, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtStandardCost, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtSchUoM, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtItemNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(46, 46, 46)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtItemDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jComboBoxItemClass, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(46, 46, 46)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtBaseUoM, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jComboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jComboBoxOutletName, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(46, 46, 46)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jComboBoxCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jComboBoxClassification, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 309, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtItemDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtItemNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxItemClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBaseUoM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPurchUoM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtStandardCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCurrCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSchUoM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtQtyUoM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPriceList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxOutletName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxClassification, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
                            .addComponent(scrollbar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtItemDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtItemDescActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtItemDescActionPerformed

    private void txtItemNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtItemNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtItemNumberActionPerformed

    private void txtStandardCostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStandardCostActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStandardCostActionPerformed

    private void txtPurchUoMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPurchUoMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPurchUoMActionPerformed

    private void txtBaseUoMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBaseUoMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBaseUoMActionPerformed

    private void txtCurrCostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCurrCostActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCurrCostActionPerformed

    private void txtSchUoMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSchUoMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSchUoMActionPerformed

    private void txtQtyUoMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQtyUoMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQtyUoMActionPerformed

    private void txtPriceListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPriceListActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPriceListActionPerformed

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
            ps.setString(4, txtSchUoM.getText().trim());
            ps.setString(5, txtBaseUoM.getText().trim());
            ps.setString(6, txtPurchUoM.getText().trim());
            ps.setDouble(7, parseNum(txtStandardCost.getText()));
            ps.setDouble(8, parseNum(txtCurrCost.getText()));
            ps.setDouble(9, parseNum(txtQtyUoM.getText()));
            ps.setDouble(10, parseNum(txtPriceList.getText()));
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
            ps.setString(3, txtSchUoM.getText().trim());
            ps.setString(4, txtBaseUoM.getText().trim());
            ps.setString(5, txtPurchUoM.getText().trim());
            ps.setDouble(6, parseNum(txtStandardCost.getText()));
            ps.setDouble(7, parseNum(txtCurrCost.getText()));
            ps.setDouble(8, parseNum(txtQtyUoM.getText()));
            ps.setDouble(9, parseNum(txtPriceList.getText()));
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

    private void jComboBoxCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxCategoryActionPerformed

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
    private javax.swing.JComboBox<String> jComboBoxCategory;
    private javax.swing.JComboBox<String> jComboBoxClassification;
    private javax.swing.JComboBox<String> jComboBoxItemClass;
    private javax.swing.JComboBox<String> jComboBoxOutletName;
    private javax.swing.JComboBox<String> jComboBoxStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private java.awt.Scrollbar scrollbar1;
    private javax.swing.JTextField txtBaseUoM;
    private javax.swing.JTextField txtCurrCost;
    private javax.swing.JTextField txtItemDesc;
    private javax.swing.JTextField txtItemNumber;
    private javax.swing.JTextField txtPriceList;
    private javax.swing.JTextField txtPurchUoM;
    private javax.swing.JTextField txtQtyUoM;
    private javax.swing.JTextField txtSchUoM;
    private javax.swing.JTextField txtStandardCost;
    // End of variables declaration//GEN-END:variables
}
