package db61b;

import java.util.regex.Pattern;

/** Various utilities used by other sources.
 *  @author P. N. Hilfinger
 */
class Utils {

    /** Return a pattern created by formatting S with arguments ARGS (as for
     *  String.format). */
    static Pattern mkPatn(String s, Object ... args) {
        s = s.replace(" *", "\\s*").replace(" ", "\\s+");
        return Pattern.compile(String.format(s, args));
    }

    /** Shorthand that returns String.format(S, ARGS). */
    static String format(String s, Object ... args) {
        return String.format(s, args);
    }

    /** Return a DBException whose message is formed from S and ARGS as for
     *  String.format. */
    static DBException error(String s, Object ... args) {
        return new DBException(format(s, args));
    }

}


