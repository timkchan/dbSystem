# A Small Database System
![](demo.gif?raw=true)

### 1. Introduction

For this project we'll implement a miniature relational database management system (DBMS) that stores tables of data, where a table consists of some number of labeled columns of information. Our system will include a very simple query language for extracting information from these tables. For the purposes of this project, we will deal only with very small databases, and therefore will not consider speed and efficiency at all.

### 2. Commands

Our database system uses a very restricted dialect of SQL (Structured Query Language), a widely used notation for communicating with relational databases. When you run the database system, it will accept a sequence of commands from the standard input (i.e., normally the terminal), according to the following syntax:
```
<program> ::= <statement>*
<statement> ::=
    <create statement>
  | <exit statement>
  | <insert statement>
  | <load statement>
  | <print statement>
  | <select statement>
  | <store statement>

<create statement> ::=
    "create" "table" <name> <table definition> ";"
<table definition> ::=
    "(" <name>,+ ")"
  | "as" <select clause>
<print statement> ::= "print" <table name> ";"

<insert statement> ::= "insert" "into" <table name> "values" <literal>,+ ";"
<load statement> ::= "load" <name> ";"
<store statement> ::= "store" <table name> ";"
<exit statement> ::= "quit" ";" | "exit" ";"

<select statement> ::= <select clause> ";"
<select clause> ::= "select" <column spec>,+ "from" <tables> <condition clause>
<column spec> ::= 
    <column selector>
  | <column selector> "as" <name>
<condition clause> ::=
    "where" <condition> ("and" <condition>)*
  | <empty>
<tables> ::= <table name> | <table name> "," <table name>
<condition> ::=
    <column selector> <relation> <column selector>
  | <column selector> <relation> <literal>

<relation> ::= "<" | ">" | "=" | "!=" | "<=" | ">="
<table name> ::= <name>
<column selector> ::= <qualified selector> | <unqualified selector>
<qualified selector> ::= <table name> "." <name>
<unqualified selector> ::= <name>
```

### 3. Files

Files in this project:

* `ReadMe`:             This file
* `Makefile`:           A makefile (for the 'make' program) that will compile
                        your files and run tests.  You must turn in a Makefile,
                        'make' must compile all your files, and 
                        'make check' must perform all your tests.  Currently,
                        this makefile is set up to do just that with our
                        skeleton files.  Be sure to keep it up to date.
* `db61b`:              A subdirectory containing skeletons for the 
                        db61b package:

* `Main.java`:          The main program---entry point to the db61b system.
* `Utils.java`:         Assorted utility methods.
* `DBException.java`:   A custom exception to report user errors.
* `Table.java`:         Abstraction for one table.
* `Row.java`:           Abstraction for one row of a table.
* `Column.java`:        Abstraction for extracting column values from rows of a table.
* `Literal.java`:		A kind of Column that has a constant value.
* `Condition.java`:     Reprsents a comparison between column values.
* `TableIterator.java`: A modified iterator for obtaining the rows of a table.
* `CommandInterpreter.java`:
                        Translates and executes commands.
* `Tokenizer.py`:       Used by CommandInterpreter to read input and break it into meaningful pieces.
* `Makefile`:           A makefile that controls compilation and style checking.
* `testing`:            Subdirectory holding files for integration testing:

    * `Makefile`:           A makefile containing instructions for performing tests on your project.
    * `students.db`, `enrolled.db`, `courses.db`:
                            Sample database tables from the project handout.
    * `test1.in`, `test2.in`:
                            Input files for testing.  The makefile will respond
                            to 'make check' by running these files through your
                            program, filtering the output through 
                            testing/test-filter, and comparing the results with 
                            the corresponding .out files.  You should add more 
                            files to the list in Makefile.
                            REMINDER: These are samples only.  They DON'T 
                            constitute adequate tests.
    * `test1.out`, `test2.out`:  
                            Output that is supposed to result from `test1.in`
                            and `test2.in`, with the first line, all prompts,
                            and all blank lines removed (which is what 
                            test-filter does).
    * `testing.py`:         A Python 3 module containing a framework for integration
                            testing.   Used by `tester.py`.
    * `tester.py`:          A Python 3 program that tests your project.  It runs
                            your program with each .in file, comparing the output
                            with the corresponding .out file and producing a report
                            of the result.

### 4. Format of .db Files

A .db file starts with a line containing all column names (at least one) separated by commas, with any leading or trailing whitespace removed (see the .split and .trim methods of the String class). Column names must be valid identifiers and must be distinct. This is followed by any number of lines (zero of more), one for each row, containing the values for that row, separated by commas (again, leading or trailing whitespace is removed). For example, the students table shown previously would look like this in a file:

```
SID,Lastname,Firstname,SemEnter,YearEnter,Major
101,Knowles,Jason,F,2003,EECS
102,Chan,Valerie,S,2003,Math
103,Xavier,Jonathan,S,2004,LSUnd
104,Armstrong,Thomas,F,2003,EECS
105,Brown,Shana,S,2004,EECS
106,Chan,Yangfan,F,2003,LSUnd
```

### 5. Example
If the information in these tables exists in three files — `students.db`, `schedule.db`, and `enrolled.db` — then a session with our DBMS might look like the following transcript. Characters typed by the user are underlined.

```
DB61B System.  Version 3.0
> load students ;
Loaded students.db
> load enrolled ;
Loaded enrolled.db
> load schedule ;
Loaded schedule.db
> /* What are the names and SIDS of all students whose last name
     is 'Chan'? */
> select SID, Firstname from students
     where Lastname = 'Chan';
Search results:
  102 Valerie
  106 Yangfan
> /* Who took the course with CCN 21001, and what were their grades? */
> select Firstname, Lastname, Grade
         from students, enrolled
       where CCN = '21001' and students.SID = enrolled.SID; 
Search results:
  Jason Knowles B
  Shana Brown B+
  Yangfan Chan B
  Valerie Chan B+
> /* Who has taken the course named 61A from EECS? */
> /* First, create a table that contains SIDs and course names */
> create table enrolled2 as select SID
     from enrolled, schedule 
     where Dept = 'EECS' and Num = '61A' and enrolled.CCN = schedule.CCN;
> /* Print these SIDs */
> print enrolled2;
Contents of enrolled2:
  101
  102
  104
  105
  106
> /* Now print the names of the students in this list */
> select Firstname, Lastname from students, enrolled2
           where students.SID = enrolled2.SID;
Search results:
  Jason Knowles
  Valerie Chan
  Thomas Armstrong
  Shana Brown
  Yangfan Chan
> quit ;
> /* Save the enrolled2 table.
> store enrolled2;
Stored enrolled2.db
```


### 6. Running the Database System
Go into project root directory (you will see folders `db61b`), remove all old classes:
```sh
$ make clean
```

Compile all java files (A `make` file has been made to achieve this, go inside `make` to see more options):
```sh
$ make
```

Test Database source integrity (Not exhaustive) (Optional) (The database system will be run and the output will be compared against expected output):
```sh
$ make check
```

Now the system is ready to run. Like always:

```sh
$ java db61b.Main
```

### 7. Class Project Site
[here]

[here]: <https://inst.eecs.berkeley.edu/~cs61b/fa15/hw/proj1/>