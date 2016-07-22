#!/usr/bin/env python
# -*-Python-*-

import testing
import sys, re
import io
import getopt
from os.path import join

class Proj1_Tester(testing.Tester):
    def output_filter(self, id, content):
        """This is a simple filtering script, written in Python, that filters 
        unimportant potential differences between the output of your program 
        and a standard test file so that the two can be compared meaningfully.

        Output filter for project 1: 
         1. Drops the first line. 
         2. Removes all prompts
         3. Removes trailing blanks and converts multiple
            blanks to single blanks.
         4. Removes blank lines
         5. Converts any error line to the single word ERROR, and removes any
            lines that follow it.
         6. Sorts any cluster of indented lines."""

        out = io.StringIO()
        indented = []

        # Sort and print any accumulated lines in the array @indented.
        def flushIndented():
           if indented:
              indented.sort()
              for line in indented:
                 print(line, file=out)
              indented.clear()

        for line in re.split(r'\r?\n', content)[1:]:

           line = re.sub(r'^(> |\.\.\.)*', '', line)

           line = line.rstrip()
           line = re.sub(r' +', ' ', line)
           if line == '':
              continue
           if re.search(r'(?i)error', line):
              flushIndented()
              print('ERROR', file=out)
              break
           elif re.match(r'\s', line):
              indented.append(line)
           else:
              flushIndented()
              print(line, file=out)
        flushIndented()

        return out.getvalue()

    def input_files(self, id):
        result = list(super().input_files(id))
        content = testing.contents(self.standard_input_file(id))
        for db in re.findall(r'(?:^\s*load|;\s*load)\s+(\w+)', content):
            result.append((db + ".db",
                           join(self.base_dir(id), db + ".db"), None))
        return result

show=None
try:
    opts, args = getopt.getopt(sys.argv[1:], '', ['show='])
    for opt, val in opts:
        if opt == '--show':
            show = int(val)
        else:
            assert False
except:
    print("Usage: python3 tester.py [--show=N] TEST.in...",
          file=sys.stderr)
    sys.exit(1)

tester = Proj1_Tester(tested_program="java -ea db61b.Main",
                      report_limit=show)

sys.exit(0 if tester.test_all(args) else 1)

