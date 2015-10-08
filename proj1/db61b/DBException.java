package db61b;

/** Indicates some kind of user error.
 *  @author P. N. Hilfinger */
class DBException extends RuntimeException {
    /** A new exception without message. */
    public DBException() {
    }

    /** A new exception with message MSG. */
    public DBException(String msg) {
        super(msg);
    }
}
