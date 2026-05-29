/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import dao.AuditDAO;
import dao.CategoryDAO;
import dao.OutletDAO;
import dao.ReportDAO;
import model.Audit;
import model.Category;
import model.Outlet;
import model.StockOnHandRow;
import util.Fmt;
import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author andra
 */
public class AuditPanel extends javax.swing.JPanel {

    // --- wiring (added; not part of generated form) ---
    // ASSUMPTION: jDateChooser1=Audit Date, jComboBox3=Category, jComboBox4=Outlet,
    //   jButton2=Load Items, jTable2=audit grid, jButton4=Save, jButton3=Cancel,
    //   jLabel2=summary, jLabel3=total variance. Physical Count (col 5) editable -> Variance (col 6) auto.
    //   System SOH read from v_stock_on_hand via ReportDAO.
    private List<Category> categoryList = new ArrayList<>();
    private List<Outlet> outletList = new ArrayList<>();
    private List<StockOnHandRow> loadedRows = new ArrayList<>();
    private DefaultTableModel auditModel;
    private boolean updatingVariance = false;

    /**
     * Creates new form Audit
     */
    public AuditPanel() {
        initComponents();
        initAudit();
    }

    private void initAudit() {
        auditModel = new DefaultTableModel(new Object[]{
                "Item Code", "Description", "Category", "Unit", "System SOH", "Physical Count", "Variance"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return col == 5; }
        };
        jTable2.setModel(auditModel);
        jTable2.getColumnModel().getColumn(6).setCellRenderer(new VarianceRenderer());

        categoryList = new CategoryDAO().findAll();
        jComboBox3.removeAllItems();
        jComboBox3.addItem("All");
        for (Category c : categoryList) jComboBox3.addItem(c.getCategoryCode() + " - " + c.getCategoryName());

        outletList = new OutletDAO().findAll();
        jComboBox4.removeAllItems();
        jComboBox4.addItem("All");
        for (Outlet o : outletList) jComboBox4.addItem(o.getOutletCode() + " - " + o.getOutletName());

        auditModel.addTableModelListener(e -> {
            if (updatingVariance) return;
            if (e.getColumn() == 5) recomputeVariance(e.getFirstRow());
        });
    }

    private void loadItems() {
        Integer catId = jComboBox3.getSelectedIndex() > 0
                ? categoryList.get(jComboBox3.getSelectedIndex() - 1).getCategoryId() : null;
        Integer outId = jComboBox4.getSelectedIndex() > 0
                ? outletList.get(jComboBox4.getSelectedIndex() - 1).getOutletId() : null;
        try {
            loadedRows = new ReportDAO().stockOnHand(catId, outId, true, null);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        updatingVariance = true;
        auditModel.setRowCount(0);
        for (StockOnHandRow r : loadedRows) {
            auditModel.addRow(new Object[]{ r.getItemCode(), r.getDescription(), r.getCategoryName(),
                    r.getBaseUnit(), r.getOnHand().toPlainString(), "", "" });
        }
        updatingVariance = false;
        updateSummary();
    }

    private void recomputeVariance(int row) {
        if (row < 0 || row >= auditModel.getRowCount()) return;
        BigDecimal soh = parse(String.valueOf(auditModel.getValueAt(row, 4)));
        String pcRaw = String.valueOf(auditModel.getValueAt(row, 5)).trim();
        updatingVariance = true;
        if (pcRaw.isEmpty()) {
            auditModel.setValueAt("", row, 6);
        } else {
            BigDecimal variance = parse(pcRaw).subtract(soh);
            auditModel.setValueAt(variance.toPlainString(), row, 6);
        }
        updatingVariance = false;
        updateSummary();
    }

    private void updateSummary() {
        int audited = 0, withVar = 0;
        BigDecimal totalVarValue = BigDecimal.ZERO;
        for (int i = 0; i < auditModel.getRowCount(); i++) {
            String pc = String.valueOf(auditModel.getValueAt(i, 5)).trim();
            if (pc.isEmpty()) continue;
            audited++;
            BigDecimal variance = parse(String.valueOf(auditModel.getValueAt(i, 6)));
            if (variance.signum() != 0) {
                withVar++;
                BigDecimal cost = i < loadedRows.size() ? loadedRows.get(i).getUnitCost() : BigDecimal.ZERO;
                totalVarValue = totalVarValue.add(variance.multiply(cost == null ? BigDecimal.ZERO : cost));
            }
        }
        jLabel2.setText(audited + " items audited . " + withVar + " with variance");
        jLabel3.setText("Total Variance value: " + Fmt.rupiah(totalVarValue));
    }

    private void saveAudit() {
        LocalDate auditDate = chosenDate();
        List<Audit> rows = new ArrayList<>();
        for (int i = 0; i < auditModel.getRowCount(); i++) {
            String pc = String.valueOf(auditModel.getValueAt(i, 5)).trim();
            if (pc.isEmpty()) continue;
            Audit a = new Audit();
            a.setItemId(loadedRows.get(i).getItemId());
            a.setAuditDate(auditDate);
            a.setAuditQuantity(parse(pc));
            a.setVariance(parse(String.valueOf(auditModel.getValueAt(i, 6))));
            rows.add(a);
        }
        if (rows.isEmpty()) { JOptionPane.showMessageDialog(this, "Isi minimal satu Physical Count."); return; }
        try {
            int n = new AuditDAO().saveAll(rows);
            JOptionPane.showMessageDialog(this, n + " baris audit tersimpan.");
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearAudit() {
        auditModel.setRowCount(0);
        loadedRows.clear();
        updateSummary();
    }

    private LocalDate chosenDate() {
        java.util.Date d = jDateChooser1.getDate();
        return d == null ? LocalDate.now() : new java.sql.Date(d.getTime()).toLocalDate();
    }

    private BigDecimal parse(String s) {
        try { return new BigDecimal(s.trim().replace(",", "")); }
        catch (Exception e) { return BigDecimal.ZERO; }
    }

    /** Variance color: negative=red (shortage), positive=green (surplus), zero=black. */
    private static class VarianceRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object value, boolean sel,
                boolean focus, int row, int col) {
            Component c = super.getTableCellRendererComponent(t, value, sel, focus, row, col);
            String s = String.valueOf(value).trim();
            Color fg = Color.BLACK;
            try {
                if (!s.isEmpty()) {
                    double v = Double.parseDouble(s.replace(",", ""));
                    if (v < 0) fg = new Color(204, 0, 0);
                    else if (v > 0) fg = new Color(0, 140, 0);
                }
            } catch (NumberFormatException ignored) {}
            c.setForeground(sel ? Color.WHITE : fg);
            return c;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filter", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        jLabel7.setText("Audit Date:");

        jLabel8.setText("Category:");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", " " }));
        jComboBox3.addActionListener(this::jComboBox3ActionPerformed);

        jLabel9.setText("Outlet:");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SHLMP", "SHMGI", "SHPLU", "SHMKG", "SHMTA", " " }));
        jComboBox4.addActionListener(this::jComboBox4ActionPerformed);

        jButton2.setText("Load Items");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(330, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(jLabel8)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2)))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Audit Table");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Item Code", "Description", "Category", "Unit", "System SOH", "Physical Count", "Variance"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jButton3.setText("Cancel");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton4.setBackground(new java.awt.Color(51, 51, 255));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Save Audit Result");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Audit - Physical Count ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        jLabel2.setText("5 Items audited . 2 with variance");

        jLabel3.setText("Total Variance value: ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                .addGap(16, 16, 16)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton3))
                .addGap(214, 214, 214))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        clearAudit();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        loadItems();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        saveAudit();
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
