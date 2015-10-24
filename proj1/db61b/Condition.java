package db61b;

import java.util.List;

/**
 * Represents a single 'where' condition in a 'select' command.
 *
 * @author Tim Chan
 */
class Condition {

    /**
     * Internally, we represent our relation as a 3-bit value whose bits denote
     * whether the relation allows the left value to be greater than the right
     * (GT), equal to it (EQ), or less than it (LT).
     */
    private static final int GT = 1, EQ = 2, LT = 4;

    /**
     * A Condition representing COL1 RELATION COL2, where COL1 and COL2 are
     * column designators. and RELATION is one of the strings "<", ">", "<=",
     * ">=", "=", or "!=".
     */
    Condition(Column col1, String relation, Column col2) {
        _col1 = col1;
        _col2 = col2;
        switch (relation) {
        case "=":
            _rel = EQ;
            break;
        case "<":
            _rel = LT;
            break;
        case ">":
            _rel = GT;
            break;
        case "<=":
            _rel = LT + EQ;
            break;
        case ">=":
            _rel = GT + EQ;
            break;
        case "!=":
            _rel = 0;
            break;
        default:
            throw new DBException("Unsupported relation");
        }
    }

    /**
     * A Condition representing COL1 RELATION 'VAL2', where COL1 is a column
     * designator, VAL2 is a literal value (without the quotes), and RELATION is
     * one of the strings "<", ">", "<=", ">=", "=", or "!=".
     */
    Condition(Column col1, String relation, String val2) {
        this(col1, relation, new Literal(val2));
    }

    /**
     * Assuming that ROWS are rows from the respective tables from which my
     * columns are selected, returns the result of performing the test I denote.
     */
    boolean test() {
        switch (_rel) {
            case 0:  return !_col1.value().equals(_col2.value());
            case 1:  return _col1.value().compareTo(_col2.value()) > 0;
            case 2:  return _col1.value().compareTo(_col2.value()) == 0;
            case 3:  return _col1.value().compareTo(_col2.value()) > 0 || _col1.value().compareTo(_col2.value()) == 0;
            case 4:  return _col1.value().compareTo(_col2.value()) < 0;
            case 7:  return _col1.value().compareTo(_col2.value()) < 0 || _col1.value().compareTo(_col2.value()) == 0;
            default: throw new DBException("Unexpected operation.");
    }

    }

    /** Return true iff all CONDITIONS are satified. */
    static boolean test(List<Condition> conditions) {
        for (Condition c : conditions) {
            if (!c.test()) {
                return false;
            }
        }
        return true;
    }

    /** The operands of this condition. */
    private Column _col1, _col2;
    /** String describing the relation implemented by the condition. */
    private int _rel;
}
