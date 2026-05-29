/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;
//import java.awt.Color;
import java.awt.Cursor;
/**
 *
 * @author dayen
 */
public class MainFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainFrame.class.getName());

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        navDashboard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        navItems.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        navCategories.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        navOutlets.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        navVendor.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        navUsers.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        navReceiving.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        navSales.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        navMovement.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        navRecipes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        navStockOnHand.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        navAudit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        navEndBal.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        navReports.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        navCharts.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        contentPanel.add(new DashboardPanel(), "DASHBOARD");
        contentPanel.add(new MasterItemPanel(), "ITEMS");
        contentPanel.add(new CategoriesPanel(), "CATEGORIES");
        contentPanel.add(new OutletsPanel(), "OUTLETS");
        contentPanel.add(new VendorsPanel(), "VENDORS");
        contentPanel.add(new UsersPanel(), "USERS");
        contentPanel.add(new ReceivingPanel(), "RECEIVING");
        contentPanel.add(new SalesPanel(), "SALES");
        contentPanel.add(new MovementPanel(), "MOVEMENT");
        contentPanel.add(new RecipesPanel(), "RECIPES");
        contentPanel.add(new StockOnHandPanel(), "STOCKONHAND");
        contentPanel.add(new EndBalancePanel(), "ENDBAL");
        contentPanel.add(new AuditPanel(), "AUDIT");
        contentPanel.add(new ChartsPanel(), "CHARTS");
        contentPanel.add(new ReportPanel(), "REPORTS");

        applyUserAndRole();
        addLogoutButton();
        ((java.awt.CardLayout) contentPanel.getLayout()).show(contentPanel, "DASHBOARD");
        setSize(1280, 800);
        setLocationRelativeTo(null);
    }

    private void addLogoutButton() {
        javax.swing.JButton logout = new javax.swing.JButton("Logout");
        logout.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));
        logout.setContentAreaFilled(false);
        logout.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        logout.setMaximumSize(new java.awt.Dimension(190, 30));
        logout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logout.addActionListener(e -> {
            util.SessionManager.get().logout();
            dispose();
            new FormLoginFrame().setVisible(true);
        });
        sidebarPanel.add(javax.swing.Box.createVerticalStrut(12));
        sidebarPanel.add(logout);
    }

    /** Show the logged-in user in the header and hide nav items the role can't use. */
    private void applyUserAndRole() {
        util.SessionManager sm = util.SessionManager.get();
        if (sm.getCurrentUser() != null) {
            jLabel5.setText("User : " + sm.getCurrentUser().getUsername()
                    + " (" + sm.getRole() + ")");
        }
        // Staff: no Users, no End Balance. Manager: no Users. Administrator: all.
        if (sm.isStaff()) {
            navUsers.setVisible(false);
            navEndBal.setVisible(false);
        } else if (sm.isManager()) {
            navUsers.setVisible(false);
        }
    }

    /** Public hook so panels (e.g. Dashboard quick actions) can switch screens. */
    public void showScreen(String friendly) {
        String card;
        switch (friendly) {
            case "Sales":      card = "SALES"; break;
            case "Receiving":  card = "RECEIVING"; break;
            case "Audit":      card = "AUDIT"; break;
            case "Reports":    card = "REPORTS"; break;
            case "Items":      card = "ITEMS"; break;
            case "Dashboard":  card = "DASHBOARD"; break;
            default:           card = friendly; break;
        }
        ((java.awt.CardLayout) contentPanel.getLayout()).show(contentPanel, card);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        sidebarPanel = new javax.swing.JPanel();
        navDashboard = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        navItems = new javax.swing.JButton();
        navCategories = new javax.swing.JButton();
        navOutlets = new javax.swing.JButton();
        navVendor = new javax.swing.JButton();
        navUsers = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        navReceiving = new javax.swing.JButton();
        navSales = new javax.swing.JButton();
        navMovement = new javax.swing.JButton();
        navRecipes = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        navStockOnHand = new javax.swing.JButton();
        navAudit = new javax.swing.JButton();
        navEndBal = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        navReports = new javax.swing.JButton();
        navCharts = new javax.swing.JButton();
        contentPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        headerPanel.setBackground(new java.awt.Color(238, 238, 238));
        headerPanel.setPreferredSize(new java.awt.Dimension(1280, 40));

        jLabel5.setText("User : ");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Stock Audit");

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                .addContainerGap(1198, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(headerPanelLayout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(jLabel6)
                    .addContainerGap(1199, Short.MAX_VALUE)))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(headerPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        getContentPane().add(headerPanel, java.awt.BorderLayout.PAGE_START);

        sidebarPanel.setBackground(new java.awt.Color(255, 255, 255));
        sidebarPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        sidebarPanel.setPreferredSize(new java.awt.Dimension(200, 720));
        sidebarPanel.setLayout(new javax.swing.BoxLayout(sidebarPanel, javax.swing.BoxLayout.Y_AXIS));

        navDashboard.setText("Dashboard");
        navDashboard.setBorderPainted(false);
        navDashboard.setContentAreaFilled(false);
        navDashboard.addActionListener(this::navDashboardActionPerformed);
        sidebarPanel.add(navDashboard);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("-- Master Data --");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 10, 4, 10));
        jLabel1.setMaximumSize(new java.awt.Dimension(190, 29));
        jLabel1.setPreferredSize(new java.awt.Dimension(190, 29));
        sidebarPanel.add(jLabel1);

        navItems.setText("Items");
        navItems.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 12, 5, 12));
        navItems.setBorderPainted(false);
        navItems.setContentAreaFilled(false);
        navItems.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        navItems.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        navItems.setMargin(new java.awt.Insets(0, 0, 0, 0));
        navItems.setMaximumSize(new java.awt.Dimension(190, 26));
        navItems.setPreferredSize(new java.awt.Dimension(190, 26));
        navItems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                navItemsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                navItemsMouseExited(evt);
            }
        });
        navItems.addActionListener(this::navItemsActionPerformed);
        sidebarPanel.add(navItems);

        navCategories.setText("Categories");
        navCategories.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 12, 5, 12));
        navCategories.setBorderPainted(false);
        navCategories.setContentAreaFilled(false);
        navCategories.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        navCategories.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        navCategories.setMargin(new java.awt.Insets(0, 0, 0, 0));
        navCategories.setMaximumSize(new java.awt.Dimension(190, 26));
        navCategories.setPreferredSize(new java.awt.Dimension(190, 26));
        navCategories.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                navCategoriesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                navCategoriesMouseExited(evt);
            }
        });
        navCategories.addActionListener(this::navCategoriesActionPerformed);
        sidebarPanel.add(navCategories);

        navOutlets.setText("Outlets");
        navOutlets.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 12, 5, 12));
        navOutlets.setBorderPainted(false);
        navOutlets.setContentAreaFilled(false);
        navOutlets.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        navOutlets.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        navOutlets.setMargin(new java.awt.Insets(0, 0, 0, 0));
        navOutlets.setMaximumSize(new java.awt.Dimension(190, 26));
        navOutlets.setPreferredSize(new java.awt.Dimension(190, 26));
        navOutlets.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                navOutletsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                navOutletsMouseExited(evt);
            }
        });
        navOutlets.addActionListener(this::navOutletsActionPerformed);
        sidebarPanel.add(navOutlets);

        navVendor.setText("Vendors");
        navVendor.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 12, 5, 12));
        navVendor.setBorderPainted(false);
        navVendor.setContentAreaFilled(false);
        navVendor.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        navVendor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        navVendor.setMargin(new java.awt.Insets(0, 0, 0, 0));
        navVendor.setMaximumSize(new java.awt.Dimension(190, 26));
        navVendor.setPreferredSize(new java.awt.Dimension(190, 26));
        navVendor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                navVendorMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                navVendorMouseExited(evt);
            }
        });
        navVendor.addActionListener(this::navVendorActionPerformed);
        sidebarPanel.add(navVendor);

        navUsers.setText("Users");
        navUsers.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 12, 5, 12));
        navUsers.setBorderPainted(false);
        navUsers.setContentAreaFilled(false);
        navUsers.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        navUsers.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        navUsers.setMargin(new java.awt.Insets(0, 0, 0, 0));
        navUsers.setMaximumSize(new java.awt.Dimension(190, 26));
        navUsers.setPreferredSize(new java.awt.Dimension(190, 26));
        navUsers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                navUsersMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                navUsersMouseExited(evt);
            }
        });
        navUsers.addActionListener(this::navUsersActionPerformed);
        sidebarPanel.add(navUsers);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("-- Transactions --");
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 10, 4, 10));
        jLabel2.setMaximumSize(new java.awt.Dimension(190, 29));
        jLabel2.setPreferredSize(new java.awt.Dimension(190, 29));
        sidebarPanel.add(jLabel2);

        navReceiving.setText("Receiving");
        navReceiving.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 12, 5, 12));
        navReceiving.setBorderPainted(false);
        navReceiving.setContentAreaFilled(false);
        navReceiving.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        navReceiving.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        navReceiving.setMargin(new java.awt.Insets(0, 0, 0, 0));
        navReceiving.setMaximumSize(new java.awt.Dimension(190, 26));
        navReceiving.setPreferredSize(new java.awt.Dimension(190, 26));
        navReceiving.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                navReceivingMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                navReceivingMouseExited(evt);
            }
        });
        navReceiving.addActionListener(this::navReceivingActionPerformed);
        sidebarPanel.add(navReceiving);

        navSales.setText("Sales");
        navSales.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 12, 5, 12));
        navSales.setBorderPainted(false);
        navSales.setContentAreaFilled(false);
        navSales.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        navSales.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        navSales.setMargin(new java.awt.Insets(0, 0, 0, 0));
        navSales.setMaximumSize(new java.awt.Dimension(190, 26));
        navSales.setPreferredSize(new java.awt.Dimension(190, 26));
        navSales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                navSalesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                navSalesMouseExited(evt);
            }
        });
        navSales.addActionListener(this::navSalesActionPerformed);
        sidebarPanel.add(navSales);

        navMovement.setText("Movement");
        navMovement.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 12, 5, 12));
        navMovement.setBorderPainted(false);
        navMovement.setContentAreaFilled(false);
        navMovement.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        navMovement.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        navMovement.setMargin(new java.awt.Insets(0, 0, 0, 0));
        navMovement.setMaximumSize(new java.awt.Dimension(190, 26));
        navMovement.setPreferredSize(new java.awt.Dimension(190, 26));
        navMovement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                navMovementMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                navMovementMouseExited(evt);
            }
        });
        navMovement.addActionListener(this::navMovementActionPerformed);
        sidebarPanel.add(navMovement);

        navRecipes.setText("Recipes");
        navRecipes.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 12, 5, 12));
        navRecipes.setBorderPainted(false);
        navRecipes.setContentAreaFilled(false);
        navRecipes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        navRecipes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        navRecipes.setMargin(new java.awt.Insets(0, 0, 0, 0));
        navRecipes.setMaximumSize(new java.awt.Dimension(190, 26));
        navRecipes.setPreferredSize(new java.awt.Dimension(190, 26));
        navRecipes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                navRecipesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                navRecipesMouseExited(evt);
            }
        });
        navRecipes.addActionListener(this::navRecipesActionPerformed);
        sidebarPanel.add(navRecipes);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("-- Stock Operations --");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 10, 4, 10));
        jLabel3.setMaximumSize(new java.awt.Dimension(190, 29));
        jLabel3.setPreferredSize(new java.awt.Dimension(190, 29));
        sidebarPanel.add(jLabel3);

        navStockOnHand.setText("Stock on Hand");
        navStockOnHand.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 12, 5, 12));
        navStockOnHand.setBorderPainted(false);
        navStockOnHand.setContentAreaFilled(false);
        navStockOnHand.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        navStockOnHand.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        navStockOnHand.setMargin(new java.awt.Insets(0, 0, 0, 0));
        navStockOnHand.setMaximumSize(new java.awt.Dimension(190, 26));
        navStockOnHand.setPreferredSize(new java.awt.Dimension(190, 26));
        navStockOnHand.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                navStockOnHandMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                navStockOnHandMouseExited(evt);
            }
        });
        navStockOnHand.addActionListener(this::navStockOnHandActionPerformed);
        sidebarPanel.add(navStockOnHand);

        navAudit.setText("Audit");
        navAudit.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 12, 5, 12));
        navAudit.setBorderPainted(false);
        navAudit.setContentAreaFilled(false);
        navAudit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        navAudit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        navAudit.setMargin(new java.awt.Insets(0, 0, 0, 0));
        navAudit.setMaximumSize(new java.awt.Dimension(190, 26));
        navAudit.setPreferredSize(new java.awt.Dimension(190, 26));
        navAudit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                navAuditMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                navAuditMouseExited(evt);
            }
        });
        navAudit.addActionListener(this::navAuditActionPerformed);
        sidebarPanel.add(navAudit);

        navEndBal.setText("End Balance");
        navEndBal.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 12, 5, 12));
        navEndBal.setBorderPainted(false);
        navEndBal.setContentAreaFilled(false);
        navEndBal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        navEndBal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        navEndBal.setMargin(new java.awt.Insets(0, 0, 0, 0));
        navEndBal.setMaximumSize(new java.awt.Dimension(190, 26));
        navEndBal.setPreferredSize(new java.awt.Dimension(190, 26));
        navEndBal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                navEndBalMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                navEndBalMouseExited(evt);
            }
        });
        navEndBal.addActionListener(this::navEndBalActionPerformed);
        sidebarPanel.add(navEndBal);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("-- Reports & Charts  --");
        jLabel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 10, 4, 10));
        jLabel4.setMaximumSize(new java.awt.Dimension(190, 29));
        jLabel4.setPreferredSize(new java.awt.Dimension(190, 29));
        sidebarPanel.add(jLabel4);

        navReports.setText("Reports");
        navReports.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 12, 5, 12));
        navReports.setBorderPainted(false);
        navReports.setContentAreaFilled(false);
        navReports.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        navReports.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        navReports.setMargin(new java.awt.Insets(0, 0, 0, 0));
        navReports.setMaximumSize(new java.awt.Dimension(190, 26));
        navReports.setPreferredSize(new java.awt.Dimension(190, 26));
        navReports.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                navReportsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                navReportsMouseExited(evt);
            }
        });
        navReports.addActionListener(this::navReportsActionPerformed);
        sidebarPanel.add(navReports);

        navCharts.setText("Charts");
        navCharts.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 12, 5, 12));
        navCharts.setBorderPainted(false);
        navCharts.setContentAreaFilled(false);
        navCharts.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        navCharts.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        navCharts.setMargin(new java.awt.Insets(0, 0, 0, 0));
        navCharts.setMaximumSize(new java.awt.Dimension(190, 26));
        navCharts.setPreferredSize(new java.awt.Dimension(190, 26));
        navCharts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                navChartsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                navChartsMouseExited(evt);
            }
        });
        navCharts.addActionListener(this::navChartsActionPerformed);
        sidebarPanel.add(navCharts);

        getContentPane().add(sidebarPanel, java.awt.BorderLayout.LINE_START);

        contentPanel.setBackground(new java.awt.Color(255, 255, 255));
        contentPanel.setLayout(new java.awt.CardLayout());
        getContentPane().add(contentPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void navDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_navDashboardActionPerformed
        // TODO add your handling code here:
        
        ((java.awt.CardLayout) contentPanel.getLayout()).show(contentPanel, "DASHBOARD");
        
    }//GEN-LAST:event_navDashboardActionPerformed

    private void navItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_navItemsActionPerformed
        // TODO add your handling code here:
        ((java.awt.CardLayout) contentPanel.getLayout()).show(contentPanel, "ITEMS");
    }//GEN-LAST:event_navItemsActionPerformed

    private void navCategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_navCategoriesActionPerformed
        // TODO add your handling code here:
        ((java.awt.CardLayout) contentPanel.getLayout()).show(contentPanel, "CATEGORIES");
         
    }//GEN-LAST:event_navCategoriesActionPerformed

    private void navOutletsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_navOutletsActionPerformed
        // TODO add your handling code here:
        ((java.awt.CardLayout) contentPanel.getLayout()).show(contentPanel, "OUTLETS");
       
    }//GEN-LAST:event_navOutletsActionPerformed

    private void navVendorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_navVendorActionPerformed
        // TODO add your handling code here:
         ((java.awt.CardLayout) contentPanel.getLayout()).show(contentPanel, "VENDORS");
    }//GEN-LAST:event_navVendorActionPerformed

    private void navUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_navUsersActionPerformed
        // TODO add your handling code here:
         ((java.awt.CardLayout) contentPanel.getLayout()).show(contentPanel, "USERS");
    }//GEN-LAST:event_navUsersActionPerformed

    private void navReceivingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_navReceivingActionPerformed
       
        ((java.awt.CardLayout) contentPanel.getLayout()).show(contentPanel, "RECEIVING");
    }//GEN-LAST:event_navReceivingActionPerformed

    private void navSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_navSalesActionPerformed
        // TODO add your handling code here:
        ((java.awt.CardLayout) contentPanel.getLayout()).show(contentPanel, "SALES");
    }//GEN-LAST:event_navSalesActionPerformed

    private void navMovementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_navMovementActionPerformed
        // TODO add your handling code here:    
        ((java.awt.CardLayout) contentPanel.getLayout()).show(contentPanel, "MOVEMENT");
    }//GEN-LAST:event_navMovementActionPerformed

    private void navRecipesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_navRecipesActionPerformed
        // TODO add your handling code here:         
        ((java.awt.CardLayout) contentPanel.getLayout()).show(contentPanel, "RECIPES");
    }//GEN-LAST:event_navRecipesActionPerformed

    private void navStockOnHandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_navStockOnHandActionPerformed
        // TODO add your handling code here:
        ((java.awt.CardLayout) contentPanel.getLayout()).show(contentPanel, "STOCKONHAND");
       
        
    }//GEN-LAST:event_navStockOnHandActionPerformed

    private void navAuditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_navAuditActionPerformed
        // TODO add your handling code here:
     ((java.awt.CardLayout) contentPanel.getLayout()).show(contentPanel, "AUDIT");
    }//GEN-LAST:event_navAuditActionPerformed

    private void navEndBalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_navEndBalActionPerformed
        // TODO add your handling code here:
         ((java.awt.CardLayout) contentPanel.getLayout()).show(contentPanel, "ENDBAL");
    }//GEN-LAST:event_navEndBalActionPerformed

    private void navReportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_navReportsActionPerformed
        // TODO add your handling code here:
        ((java.awt.CardLayout) contentPanel.getLayout()).show(contentPanel, "REPORTS");
      
    }//GEN-LAST:event_navReportsActionPerformed

    private void navChartsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_navChartsActionPerformed
        // TODO add your handling code here:
       
        ((java.awt.CardLayout) contentPanel.getLayout()).show(contentPanel, "CHARTS");
    }//GEN-LAST:event_navChartsActionPerformed

    private void navItemsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navItemsMouseEntered
        // TODO add your handling code here:
       
    }//GEN-LAST:event_navItemsMouseEntered

    private void navItemsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navItemsMouseExited
        // TODO add your handling code here:
       
    }//GEN-LAST:event_navItemsMouseExited

    private void navCategoriesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navCategoriesMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_navCategoriesMouseEntered

    private void navCategoriesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navCategoriesMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_navCategoriesMouseExited

    private void navOutletsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navOutletsMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_navOutletsMouseEntered

    private void navOutletsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navOutletsMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_navOutletsMouseExited

    private void navVendorMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navVendorMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_navVendorMouseEntered

    private void navVendorMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navVendorMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_navVendorMouseExited

    private void navUsersMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navUsersMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_navUsersMouseEntered

    private void navUsersMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navUsersMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_navUsersMouseExited

    private void navReceivingMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navReceivingMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_navReceivingMouseEntered

    private void navReceivingMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navReceivingMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_navReceivingMouseExited

    private void navSalesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navSalesMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_navSalesMouseEntered

    private void navSalesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navSalesMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_navSalesMouseExited

    private void navMovementMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navMovementMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_navMovementMouseEntered

    private void navMovementMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navMovementMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_navMovementMouseExited

    private void navRecipesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navRecipesMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_navRecipesMouseEntered

    private void navRecipesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navRecipesMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_navRecipesMouseExited

    private void navStockOnHandMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navStockOnHandMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_navStockOnHandMouseEntered

    private void navStockOnHandMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navStockOnHandMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_navStockOnHandMouseExited

    private void navAuditMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navAuditMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_navAuditMouseEntered

    private void navAuditMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navAuditMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_navAuditMouseExited

    private void navEndBalMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navEndBalMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_navEndBalMouseEntered

    private void navEndBalMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navEndBalMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_navEndBalMouseExited

    private void navReportsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navReportsMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_navReportsMouseEntered

    private void navReportsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navReportsMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_navReportsMouseExited

    private void navChartsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navChartsMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_navChartsMouseEntered

    private void navChartsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navChartsMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_navChartsMouseExited

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
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JButton navAudit;
    private javax.swing.JButton navCategories;
    private javax.swing.JButton navCharts;
    private javax.swing.JButton navDashboard;
    private javax.swing.JButton navEndBal;
    private javax.swing.JButton navItems;
    private javax.swing.JButton navMovement;
    private javax.swing.JButton navOutlets;
    private javax.swing.JButton navReceiving;
    private javax.swing.JButton navRecipes;
    private javax.swing.JButton navReports;
    private javax.swing.JButton navSales;
    private javax.swing.JButton navStockOnHand;
    private javax.swing.JButton navUsers;
    private javax.swing.JButton navVendor;
    private javax.swing.JPanel sidebarPanel;
    // End of variables declaration//GEN-END:variables
}
