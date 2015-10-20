package db61b;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/** Tests basics functionality including:
 *1. The Row class
 * 
 * 
 * 
 */

public class BasicTests {
	
	/** testTable()
	 * Test the constructor in the class Table.
	 */
	@Test
	public void testTable() {
		Table testTable = new Table("testTable", new String[]{"col1", "col2", "col3"});
		assertEquals(testTable.title(0), "col1");
		assertEquals(testTable.title(1), "col2");
		assertEquals(testTable.title(2), "col3");
	}

	
	/** testTableException()
	 * Test the exception case.
	 */
	@Rule
	  public final ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testTableException() {
		exception.expect(DBException.class);
		exception.expectMessage("Non-distinct Field Name.");
		new Table("testTable2", new String[]{"col1", "col1", "col3"});
	}
	
	/** columnIndex()
	 * Test if correct index is returned.
	 */
	@Test
	public void testColumnIndex() {
		Table testTable = new Table("testTable", new String[]{"col1", "col2", "col3"});
		assertEquals(testTable.columnIndex("col1"), 0);
		assertEquals(testTable.columnIndex("col2"), 1);
		assertEquals(testTable.columnIndex("col3"), 2);
		assertEquals(testTable.columnIndex("col4"), -1);
	}

	/** add() / size()
	 * Test if row will be added.
	 * Test if size changes.
	 */
	@Test
	public void testAddSize() {
		Table testTable = new Table("testTable", new String[]{"col1", "col2", "col3"});
		assertEquals(testTable.size(), 0);
		assertEquals(testTable.add(new Row(new String[]{"a", "b", "c"})), true);
		assertEquals(testTable.add(new Row(new String[]{"a", "b", "c"})), false);
		assertEquals(testTable.add(new Row(new String[]{"a", "b", "e"})), true);
		assertEquals(testTable.add(new Row(new String[]{"f", "b", "e"})), true);
		assertEquals(testTable.size(), 3);
		assertEquals(testTable.add(new Row(new String[]{"f", "p", "e"})), true);
		assertEquals(testTable.add(new Row(new String[]{"f", "q", "e"})), true);
		assertEquals(testTable.size(), 5);
	}
	
	public static void main(String[] args) {
		System.exit(ucb.junit.textui.runClasses(BasicTests.class));
	}
}
