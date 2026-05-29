/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import dao.MasterItemDAO;
import dao.RecipeDAO;
import model.MasterItem;
import model.RecipeDetail;
import model.RecipeHeader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author wisdo
 */
public class RecipesPanel extends javax.swing.JPanel {

    // --- wiring (added; not part of generated form) ---
    // ASSUMPTION (generic names mapped by labels/position):
    //   jTextField2 = Recipe Code (auto, read-only), jComboBox2 = Finished Item,
    //   jComboBox1 = Item Class, jComboBox3 = Ingredient,
    //   jTextField1 = Initial Weight, jTextField3 = Final Weight,
    //   jButton1 = Add Ingredient, jButton3 = Save Recipe, jButton2 = Cancel.
    private List<MasterItem> finishedList = new ArrayList<>();
    private List<MasterItem> ingredientList = new ArrayList<>();
    private final List<RecipeDetail> lines = new ArrayList<>();
    private DefaultTableModel lineModel;

    /**
     * Creates new form RecipesPanel
     */
    public RecipesPanel() {
        initComponents();
        initRecipes();
    }

    private void initRecipes() {
        lineModel = new DefaultTableModel(
                new Object[]{"Code", "Description", "Initial Wt", "Final Wt", "Waste %", "Unit"}, 0);
        jTable1.setModel(lineModel);

        jTextField2.setText(new RecipeDAO().getNextRecipeCode());
        jTextField2.setEditable(false);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"Drink", "Dry", "Fish", "Menu"}));

        finishedList = new RecipeDAO().findItemsWithoutRecipe();
        jComboBox2.removeAllItems();
        for (MasterItem it : finishedList) jComboBox2.addItem(it.getItemCode() + " - " + it.getDescription());
        jComboBox2.addActionListener(e -> {
            MasterItem it = finishedItem();
            if (it != null && it.getItemClass() != null) jComboBox1.setSelectedItem(it.getItemClass());
        });

        ingredientList = new MasterItemDAO().findActive();
        jComboBox3.removeAllItems();
        for (MasterItem it : ingredientList) jComboBox3.addItem(it.getItemCode() + " - " + it.getDescription());

        jButton1.addActionListener(e -> addIngredient());
    }

    private MasterItem finishedItem() {
        int i = jComboBox2.getSelectedIndex();
        return (i >= 0 && i < finishedList.size()) ? finishedList.get(i) : null;
    }

    private MasterItem ingredientItem() {
        int i = jComboBox3.getSelectedIndex();
        return (i >= 0 && i < ingredientList.size()) ? ingredientList.get(i) : null;
    }

    private void addIngredient() {
        MasterItem ing = ingredientItem();
        if (ing == null) { JOptionPane.showMessageDialog(this, "Pilih ingredient dulu."); return; }
        MasterItem fin = finishedItem();
        if (fin != null && fin.getItemId() == ing.getItemId()) {
            JOptionPane.showMessageDialog(this, "Finished item tidak boleh jadi ingredient-nya sendiri.");
            return;
        }
        BigDecimal initial = parse(jTextField1.getText());
        BigDecimal finalW = parse(jTextField3.getText());
        if (initial.compareTo(BigDecimal.ZERO) <= 0 || finalW.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(this, "Initial & Final Weight harus > 0."); return;
        }
        if (finalW.compareTo(initial) > 0) {
            JOptionPane.showMessageDialog(this, "Final Weight tidak boleh lebih besar dari Initial Weight."); return;
        }
        BigDecimal waste = initial.subtract(finalW)
                .divide(initial, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

        RecipeDetail d = new RecipeDetail();
        d.setItemId(ing.getItemId());
        d.setItemCode(ing.getItemCode());
        d.setDescription(ing.getDescription());
        d.setInitialWeight(initial);
        d.setFinalWeight(finalW);
        d.setWastePercentage(waste);
        d.setUnit(ing.getBaseUnit());
        lines.add(d);

        lineModel.addRow(new Object[]{ ing.getItemCode(), ing.getDescription(),
                initial.toPlainString(), finalW.toPlainString(),
                waste.setScale(2, RoundingMode.HALF_UP).toPlainString(),
                ing.getBaseUnit() });
        jTextField1.setText("");
        jTextField3.setText("");
    }

    private void saveRecipe() {
        MasterItem fin = finishedItem();
        if (fin == null) { JOptionPane.showMessageDialog(this, "Pilih finished item dulu."); return; }
        if (lines.isEmpty()) { JOptionPane.showMessageDialog(this, "Tambahkan minimal 1 ingredient."); return; }

        RecipeHeader h = new RecipeHeader();
        h.setRecipeCode(jTextField2.getText());
        h.setItemId(fin.getItemId());
        h.setItemClass(jComboBox1.getSelectedItem() == null ? null : jComboBox1.getSelectedItem().toString());

        try {
            new RecipeDAO().insertWithDetails(h, lines);
            JOptionPane.showMessageDialog(this, "Recipe tersimpan: " + h.getRecipeCode());
            resetRecipe();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetRecipe() {
        lines.clear();
        lineModel.setRowCount(0);
        jTextField1.setText("");
        jTextField3.setText("");
        jTextField2.setText(new RecipeDAO().getNextRecipeCode());
        finishedList = new RecipeDAO().findItemsWithoutRecipe();
        jComboBox2.removeAllItems();
        for (MasterItem it : finishedList) jComboBox2.addItem(it.getItemCode() + " - " + it.getDescription());
    }

    private BigDecimal parse(String s) {
        try { return new BigDecimal(s.trim().replace(",", "")); }
        catch (Exception e) { return BigDecimal.ZERO; }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel2.setText("Recipe Entry--Manage Recipes");

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

        jButton2.setText("Cancel");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton3.setBackground(new java.awt.Color(7, 127, 207));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Save Recipe");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Recipe Header", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        jLabel4.setText("Recipe Code:");

        jTextField2.addActionListener(this::jTextField2ActionPerformed);

        jLabel6.setText("Finished Item:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MENU-001-Edamame(Kitchen)" }));

        jLabel7.setText("Item Class:");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MENU-001-Edamame(Kitchen)" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(32, 32, 32)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(62, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Add Ingredient", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        jLabel11.setText("Ingredient:");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DRY--0497---Edamame(raw)" }));
        jComboBox3.addActionListener(this::jComboBox3ActionPerformed);

        jLabel10.setText("Initial Weight:");

        jLabel8.setText("Final Weight:");

        jButton1.setText("+Add Ingredient");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1))
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Ingredients");

        jLabel5.setText("3 Ingredients-waste%=initial-Final) initial x100");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addGap(28, 28, 28))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1064, Short.MAX_VALUE)
                    .addComponent(jLabel5))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addGap(48, 48, 48)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(108, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        resetRecipe();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        saveRecipe();
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
