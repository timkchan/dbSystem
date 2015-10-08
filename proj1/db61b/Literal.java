package db61b;

import java.util.List;

/** A Literal is a degenerate, anonymous, resolved Column whose get()
 *  method always returns a fixed value.
 *  @author
 */
class Literal extends Column {

    /** A Literal whose value is VALUE. */
    Literal(String value) {
        super(null, "<Literal>");
        _value = value;
    }

    @Override
    String value() {
        return _value;
    }

    @Override
    void resolve(List<TableIterator> iterators) {
    }

    /** My value. */
    private final String _value;
}
