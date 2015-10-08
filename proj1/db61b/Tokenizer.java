package db61b;

import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Scanner;

import static db61b.Utils.*;

/** Represents a stream of db61b tokens read from a given Scanner.
 *  @author P. N. Hilfinger. */
class Tokenizer {

    /** Text of regular expressions that represent literals (possibly
     *  unterminated), identifiers, and comments (possibly
     *  unterminated). */
    private static final String
        LITERAL_TEXT = "'(?:[^,'\n\r]*)'?",
        IDENTIFIER_TEXT = "[\\p{Alpha}_]\\w*",
        COMMENT_TEXT = "(?:/\\*.*?\\*/|/\\*.*)";
    /** Matches potential tokens, including valid or unterminated
     *  literals, valid or unterminated comments, identifiers,
     *  relation symbols (=, <, <=, >=, and !=), end-of-line
     *  sequences, or other single characters.  The pattern matches a
     *  prefix of any string. */
    private static final Pattern
        TOKEN_PATN = mkPatn("(?s)[<>!]?=|%s|%s|%s|\r?\n|\\S",
                            LITERAL_TEXT, IDENTIFIER_TEXT, COMMENT_TEXT);

    /** Patterns matching specific kinds of token.  These are intended
     *  to be used with methods such as CommandInterpreter.name. */
    static final Pattern
        IDENTIFIER = mkPatn(IDENTIFIER_TEXT),
        LITERAL = mkPatn("'.*"),
        RELATION = mkPatn("[<>!]?=|[<>]");

    /** A Tokenizer that reads tokens from S, and prompts on PROMPTER,
     *  if it is non-null. */
    Tokenizer(Scanner s, PrintStream prompter) {
        _input = s;
        _buffer = new ArrayList<>();
        _prompter = prompter;
        _continued = false;
        _shouldPrompt = true;
        _k = 0;
        _mat = Pattern.compile(".").matcher("");
    }

    /** Read the next potential token and add it to _buffer.  Filters
     *  out comments and ends-of-line. Adds "*EOF*" at the end of input.
     *  Throws DBException on encountering a lexical error (such as an
     *  unterminated literal). */
    private void readToken() {
        while (true) {
            prompt();
            String token = _input.findWithinHorizon(TOKEN_PATN, 0);
            if (token == null) {
                token = "*EOF*";
            } else if (token.startsWith("'")) {
                if (token.length() == 1 || !token.endsWith("'")) {
                    throw error("unterminated literal constant");
                }
            } else if (token.startsWith("/*")) {
                if (token.length() < 4 || !token.endsWith("*/")) {
                    throw error("unterminated comment");
                }
                continue;
            } else if (token.endsWith("\n")) {
                _shouldPrompt = true;
                continue;
            }
            _buffer.add(token);
            _continued = !token.equals(";");
            return;
        }
    }

    /** Print an appropriate prompt, if there is a prompter: either ">"
     *  when expecting the start of a new command, or "..."
     *  otherwise. */
    private void prompt() {
        if (_shouldPrompt && _prompter != null) {
            if (_continued) {
                _prompter.print("...");
            } else {
                _prompter.print("> ");
            }
            _prompter.flush();
            _shouldPrompt = false;
        }
    }

    /** Read and return the next token, if it matches P.  Otherwise throw
     *  DBException */
    String next(Pattern p) {
        if (!nextIs(p)) {
            if (nextIs("*EOF*")) {
                throw error("unexpected end of input");
            } else {
                throw error("unexpected token: '%s'", peek());
            }
        }
        return next();
    }

    /** Read and return the next token, if it equals P.  Otherwise throw
     *  DBException */
    String next(String p) {
        if (!nextIs(p)) {
            if (nextIs("*EOF*")) {
                throw error("unexpected end of input");
            } else {
                throw error("unexpected token: '%s'", peek());
            }
        }
        return next();
    }

    /** Read the next token, if it matches P, and return true.  Otherwise
     *  return false.  Still throws DBException on detecting lexical errors. */
    boolean nextIf(Pattern p) {
        if (nextIs(p)) {
            next();
            return true;
        }
        return false;
    }

    /** Read the next token, if it equals P, and return true.  Otherwise
     *  return false.  Still throws DBException on detecting lexical errors. */
    boolean nextIf(String p) {
        if (nextIs(p)) {
            next();
            return true;
        }
        return false;
    }


    /** Return true iff the next token matches P.  Throws DBException on
     *  encountering a lexical error. */
    boolean nextIs(Pattern p) {
        String token = peek();
        return _mat.usePattern(p).reset(token).matches();
    }

    /** Return true iff the next token equals P.  Throws DBException on
     *  encountering a lexical error. */
    boolean nextIs(String p) {
        String token = peek();
        return token.equals(p);
    }

    /** Return and read past the next token. */
    String next() {
        if (_k == _buffer.size()) {
            readToken();
        }
        _k += 1;
        return _buffer.get(_k - 1);
    }

    /** Returns the next token without changing the position of THIS. */
    String peek() {
        while (_k >= _buffer.size()) {
            readToken();
        }
        return _buffer.get(_k);
    }

    /** Matcher used for pattern matching. */
    private Matcher _mat;
    /** The character input source. */
    private Scanner _input;
    /** All tokens read since the last flush or beginning of input. */
    private ArrayList<String> _buffer;
    /** Output for prompts.  Null if prompts not used. */
    private PrintStream _prompter;
    /** False iff the next token is expected to start a command. */
    private boolean _continued;
    /** True iff prompt is needed for the next token. */
    private boolean _shouldPrompt;
    /** Current position in the token stream as an offset within _buffer. */
    private int _k;
}
