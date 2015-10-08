package db61b;

import java.util.Scanner;
import java.util.HashMap;

/** The main program for db61b.
 *  @author
 */
public class Main {

    /** Version designation for this program. */
    private static final String VERSION = "3.0";

    /** Starting with an empty database, read and execute commands from
     *  System.in until receiving a 'quit' ('exit') command or until
     *  reaching the end of input. */
    public static void main(String[] unused) {
        System.out.printf("DB61B System.  Version %s.%n", VERSION);

        HashMap<String, Table> db = new HashMap<>();

        Scanner input = new Scanner(System.in);
        CommandInterpreter interpreter =
            new CommandInterpreter(db, input, System.out);

        while (true) {
            try {
                if (!interpreter.statement()) {
                    break;
                }
            } catch (DBException e) {
                System.out.printf("Error: %s%n", e.getMessage());
                interpreter.skipCommand();
            }
        }
    }

}

