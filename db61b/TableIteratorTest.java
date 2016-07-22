/** Tests basics functionality including:
 * The TableIterator class
 */
package db61b;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class TableIteratorTest {

    /**
     * test TableIterator() Test TableIterator.
     */
    @Test
    public void testTableIterator() {
        Table testTable = Table.readTable("students");

        List<String> table = new ArrayList<String>();
        table.add("101 Knowles Jason F 2003 EECS");
        table.add("102 Chan Valerie S 2003 Math");
        table.add("103 Xavier Jonathan S 2004 LSUnd");
        table.add("104 Armstrong Thomas F 2003 EECS");
        table.add("105 Brown Shana S 2004 EECS");
        table.add("106 Chan Yangfan F 2003 LSUnd");

        Iterator<String> tableIt = table.iterator();

        for (Row r : testTable) {
            assertEquals(r.toString(), tableIt.next());
        }
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(TableIteratorTest.class));
    }

}
