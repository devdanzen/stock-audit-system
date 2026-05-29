package util;

import db.DBConnection;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Thin reflection wrapper around JasperReports so this project compiles
 * WHETHER OR NOT the JasperReports jars are on the classpath.
 *
 *  - If the jars are present: compiles the .jrxml, fills it from the DB,
 *    and opens JasperViewer (which has Print + Export PDF/Excel buttons).
 *  - If the jars are missing: returns false so the caller can fall back
 *    to the plain-text report. Nothing breaks either way.
 *
 * To enable: add the JasperReports 6.20.x jars (see project notes) to the
 * project Libraries. No code change needed.
 */
public final class JasperReportUtil {

    private JasperReportUtil() {}

    /** True if the JasperReports engine is on the classpath. */
    public static boolean isAvailable() {
        try {
            Class.forName("net.sf.jasperreports.engine.JasperFillManager");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Compiles the .jrxml at {@code jrxmlClasspath}, fills it with the live DB
     * connection, and shows it in JasperViewer. Returns false if Jasper is not
     * available or anything fails (so the caller can fall back to text).
     */
    public static boolean viewReport(String jrxmlClasspath, Map<String, Object> params) {
        if (params == null) params = new HashMap<>();
        InputStream is = JasperReportUtil.class.getResourceAsStream(jrxmlClasspath);
        if (is == null) {
            System.out.println("Report template not found on classpath: " + jrxmlClasspath);
            return false;
        }
        try {
            Class<?> compileMgr = Class.forName("net.sf.jasperreports.engine.JasperCompileManager");
            Class<?> reportClass = Class.forName("net.sf.jasperreports.engine.JasperReport");
            Class<?> printClass = Class.forName("net.sf.jasperreports.engine.JasperPrint");
            Class<?> fillMgr = Class.forName("net.sf.jasperreports.engine.JasperFillManager");
            Class<?> viewer = Class.forName("net.sf.jasperreports.view.JasperViewer");

            Object jasperReport = compileMgr.getMethod("compileReport", InputStream.class).invoke(null, is);

            Object jasperPrint;
            try (Connection conn = DBConnection.connect()) {
                jasperPrint = fillMgr.getMethod("fillReport", reportClass, Map.class, Connection.class)
                        .invoke(null, jasperReport, params, conn);
            }

            // JasperViewer.viewReport(jasperPrint, false)  -> false = don't exit app on close
            viewer.getMethod("viewReport", printClass, boolean.class)
                  .invoke(null, jasperPrint, Boolean.FALSE);
            return true;
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            return false; // jars not added yet
        } catch (Exception e) {
            System.out.println("Jasper report failed: " + e.getMessage());
            return false;
        }
    }
}
