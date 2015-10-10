package db61b;

import java.util.List;

import static db61b.Utils.*;

/** A Column accesses a specific, named column in a TableIterator, which in
 *  turn is an object that iterates through the rows of a Table.
 *  @author P. N. Hilfinger
*/
class Column {

    /** A Column named NAME selected from TABLE.  The Column is
     *  initially unresolved; that is, it is not attached to a
     *  particular TableIterator.  TABLE may be null, indicating that it
     *  is unspecified.  Otherwise, NAME must be the name of a column
     *  in TABLE. */
    Column(Table table, String name) {
        _columnName = name;
        _table = table;
        if (_table != null && _table.columnIndex(name) == -1) {
            throw error("%s is not a column in %s", name, table.name());
        }
    }

    /** Return my name. */
    String name() {
        return _columnName;
    }

    /** Attach me to an appropriate TableIterator out of
     *  ITERATORS.  If my Table is unspecified, there must be a unique
     *  TableIterator with a column having my name.  Otherwise, my
     *  Table must be the table of one of ITERATORS. */
    void resolve(List<TableIterator> iterators) {
        if (_table == null) {
            _index = -1;
            for (TableIterator it : iterators) {
                int k = it.columnIndex(_columnName);
                if (k >= 0) {
                    if (_rowSource != null) {
                        throw error("%s is ambiguous", _columnName);
                    }
                    _index = k;
                    _rowSource = it;
                }
            }
            if (_index == -1) {
                throw error("unknown column: %s", _columnName);
            }
        } else {
            for (TableIterator it : iterators) {
                if (it.table() == _table) {
                    _rowSource = it;
                    _index = it.columnIndex(_columnName);
                    return;
                }
            }
            throw error("%s is not being selected from", _table.name());
        }
    }

    /** Return my column value from the current row of my
     *  TableIterator.  This Column must be resolved. */
    String value() {
        assert _rowSource != null;
        return _rowSource.value(_index);
    }

    /** Column name denoted by THIS. */
    private String _columnName;
    /** Index of the column from which to extract a value. */
    private int _index;
    /** The Table of which I am a Column. */
    private Table _table;
    /** Source for rows of the table. */
    private TableIterator _rowSource;
}
