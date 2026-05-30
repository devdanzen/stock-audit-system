package util;

import db.DBConnection;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public final class JasperReportUtil {

    private JasperReportUtil() {}

    public static boolean isAvailable() {
        try {
            Class.forName("net.sf.jasperreports.engine.JasperFillManager");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

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

            viewer.getMethod("viewReport", printClass, boolean.class)
                  .invoke(null, jasperPrint, Boolean.FALSE);
            return true;
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            return false;
        } catch (Exception e) {
            System.out.println("Jasper report failed: " + e.getMessage());
            return false;
        }
    }
}
