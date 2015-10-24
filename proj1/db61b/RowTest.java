package db61b;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests basics functionality including: The Row class
 *
 */

public class RowTest {

    /**
     * testTable() Test the constructor in the class Table.
     */
    @Test
    public void testTable() {
        Table testTable = new Table("testTable", new String[] { "col1",
            "col2", "col3" });
        assertEquals(testTable.title(0), "col1");
        assertEquals(testTable.title(1), "col2");
        assertEquals(testTable.title(2), "col3");
    }

    /**
     * testTableException() Test the exception case.
     */
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testTableException() {
        exception.expect(DBException.class);
        exception.expectMessage("Non-distinct Field Name.");
        new Table("testTable2", new String[] { "col1", "col1",
            "col3" });
    }

    /**
     * columnIndex() Test if correct index is returned.
     */
    @Test
    public void testColumnIndex() {
        Table testTable = new Table("testTable", new String[] { "col1",
            "col2", "col3" });
        assertEquals(testTable.columnIndex("col1"), 0);
        assertEquals(testTable.columnIndex("col2"), 1);
        assertEquals(testTable.columnIndex("col3"), 2);
        assertEquals(testTable.columnIndex("col4"), -1);
    }

    /**
     * add() / size() Test if row will be added. Test if size changes.
     */
    @Test
    public void testAddSize() {
        Table tT = new Table("testTable", new String[] { "col1",
            "col2", "col3" });
        assertEquals(tT.size(), 0);
        assertEquals(tT.add(new Row(new String[] { "a", "b", "c" })), true);
        assertEquals(tT.add(new Row(new String[] { "a", "b", "c" })), false);
        assertEquals(tT.add(new Row(new String[] { "a", "b", "e" })), true);
        assertEquals(tT.add(new Row(new String[] { "f", "b", "e" })), true);
        assertEquals(tT.size(), 3);
        assertEquals(tT.add(new Row(new String[] { "f", "p", "e" })), true);
        assertEquals(tT.add(new Row(new String[] { "f", "q", "e" })), true);
        assertEquals(tT.size(), 5);
    }

    /**
     * toCommaString() Test if row toCommaString()
     */
    @Test
    public void testToCommaString() {
        Row testRow = new Row(new String[] { "101", "Tim", "Chan",
            "S", "2049", "EECS" });
        System.out.println(testRow.toCommaString());
        assertEquals(testRow.toCommaString(), "101,Tim,Chan,S,2049,EECS");
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(RowTest.class));
    }
}
