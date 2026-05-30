package util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import javax.swing.AbstractButton;
import javax.swing.UIManager;

public final class Theme {

    public static final Color ACCENT = new Color(0, 102, 204);
    public static final Color DANGER = new Color(204, 0, 0);

    private Theme() {}

    public static void setup() {
        try {
            Class<?> flat = Class.forName("com.formdev.flatlaf.FlatLightLaf");
            flat.getMethod("setup").invoke(null);
            UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 13));
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 6);
            UIManager.put("ScrollBar.thumbArc", 8);
            UIManager.put("ScrollBar.width", 12);
            UIManager.put("Table.rowHeight", 24);
            UIManager.put("TableHeader.height", 26);
            return;
        } catch (Throwable ignore) {
        }
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignore) {
        }
    }

    public static final String FONT_FAMILY = "Segoe UI";

    private static final Color TABLE_GRID   = new Color(224, 224, 224);
    private static final Color TABLE_ALT    = new Color(248, 248, 248);
    private static final Color TABLE_HEADER = new Color(232, 232, 232);
    private static final Color TABLE_SELECT = new Color(204, 228, 247);
    private static final Color FIELD_BORDER = new Color(160, 160, 160);

    public static void unify(Component c) {
        Font f = c.getFont();
        if (f != null && !FONT_FAMILY.equalsIgnoreCase(f.getFamily())) {
            c.setFont(new Font(FONT_FAMILY, f.getStyle(), f.getSize()));
        }
        if (c instanceof AbstractButton) {
            AbstractButton b = (AbstractButton) c;
            if (b.isContentAreaFilled() && b.getBackground() != null) {
                Color bg = b.getBackground();
                int r = bg.getRed(), g = bg.getGreen(), bl = bg.getBlue();
                int max = Math.max(r, Math.max(g, bl)), min = Math.min(r, Math.min(g, bl));
                boolean saturated = (max - min) > 40;
                if (saturated && r > 150 && g < 140 && bl < 140) {
                    b.setBackground(DANGER);
                    b.setForeground(Color.WHITE);
                } else if (saturated && bl >= r && bl >= g - 30) {
                    b.setBackground(ACCENT);
                    b.setForeground(Color.WHITE);
                }
            }
        }
        if (c instanceof javax.swing.JTable) {
            styleTable((javax.swing.JTable) c);
        }
        if (c instanceof javax.swing.JScrollPane) {
            ((javax.swing.JScrollPane) c).setBorder(
                    javax.swing.BorderFactory.createLineBorder(FIELD_BORDER));
        }
        if (c instanceof Container) {
            for (Component child : ((Container) c).getComponents()) unify(child);
        }
    }

    public static void styleTable(javax.swing.JTable t) {
        t.setRowHeight(24);
        t.setShowGrid(true);
        t.setGridColor(TABLE_GRID);
        t.setIntercellSpacing(new java.awt.Dimension(1, 1));
        t.setSelectionBackground(TABLE_SELECT);
        t.setSelectionForeground(Color.BLACK);
        t.setFillsViewportHeight(true);
        t.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        t.setFont(new Font(FONT_FAMILY, Font.PLAIN, 12));

        javax.swing.table.JTableHeader h = t.getTableHeader();
        if (h != null) {
            h.setFont(new Font(FONT_FAMILY, Font.BOLD, 12));
            h.setReorderingAllowed(false);
            h.setBackground(TABLE_HEADER);
            h.setPreferredSize(new java.awt.Dimension(h.getPreferredSize().width, 26));
        }
        t.setDefaultRenderer(Object.class, new StripeRenderer());
    }

    private static final java.util.regex.Pattern NUMERIC =
            java.util.regex.Pattern.compile("^-?\\s*(Rp\\s*)?[\\d.,]+\\s*%?$");

    private static class StripeRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(javax.swing.JTable t, Object v,
                boolean sel, boolean foc, int row, int col) {
            Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
            if (!sel) c.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ALT);
            String s = v == null ? "" : v.toString().trim();
            setHorizontalAlignment(NUMERIC.matcher(s).matches() ? RIGHT : LEFT);
            setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 8));
            return c;
        }
    }
}
