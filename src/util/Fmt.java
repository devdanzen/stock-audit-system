package util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class Fmt {

    private static final DecimalFormat MONEY;
    private static final DecimalFormat QTY;
    static {
        DecimalFormatSymbols s = new DecimalFormatSymbols(Locale.US);
        s.setGroupingSeparator(',');
        s.setDecimalSeparator('.');
        MONEY = new DecimalFormat("#,##0", s);
        QTY = new DecimalFormat("#,##0.####", s);
    }

    private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Fmt() {}

    public static String rupiah(BigDecimal v) {
        if (v == null) v = BigDecimal.ZERO;
        return "Rp " + MONEY.format(v);
    }

    public static String rupiah(double v) {
        return rupiah(BigDecimal.valueOf(v));
    }

    public static String number(BigDecimal v) {
        if (v == null) v = BigDecimal.ZERO;
        return MONEY.format(v);
    }

    public static String qty(BigDecimal v) {
        if (v == null) v = BigDecimal.ZERO;
        return QTY.format(v);
    }

    public static String date(LocalDate d) {
        return d == null ? "" : d.format(ISO);
    }

    public static BigDecimal money(String text) {
        if (text == null) return BigDecimal.ZERO;
        String cleaned = text.replace(",", "").replace("Rp", "").trim();
        if (cleaned.isEmpty()) return BigDecimal.ZERO;
        try {
            return new BigDecimal(cleaned);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}
