package util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Small formatting helpers shared across panels.
 * Currency: "Rp 1,234,567" (space after Rp, comma thousands, no decimals).
 * Dates: ISO yyyy-MM-dd.
 */
public final class Fmt {

    private static final DecimalFormat MONEY;
    static {
        DecimalFormatSymbols s = new DecimalFormatSymbols(Locale.US);
        s.setGroupingSeparator(',');
        MONEY = new DecimalFormat("#,##0", s);
    }

    private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Fmt() {}

    /** "Rp 1,234,567" — null/zero safe. */
    public static String rupiah(BigDecimal v) {
        if (v == null) v = BigDecimal.ZERO;
        return "Rp " + MONEY.format(v);
    }

    public static String rupiah(double v) {
        return rupiah(BigDecimal.valueOf(v));
    }

    /** "1,234,567" without the Rp prefix (for table cells). */
    public static String number(BigDecimal v) {
        if (v == null) v = BigDecimal.ZERO;
        return MONEY.format(v);
    }

    public static String date(LocalDate d) {
        return d == null ? "" : d.format(ISO);
    }

    /** Parse a user-typed money string ("485,000.00" / "485000") to BigDecimal; 0 on blank/bad. */
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
