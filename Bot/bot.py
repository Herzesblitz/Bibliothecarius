from rasa_core.actions import *
from rasa_core.events import *
import subprocess


class ActionSearchSimilarBooks(Action):
    def name(self):
        return 'action_search_new_books'

    def run(self, dispatcher, tracker, domain):
        dispatcher.utter_message("here are the new books, I found.")
        return []

class ActionSearchInfoBook(Action):
    def name(self):
        return 'action_search_Info_book'

    def run(self, dispatcher, tracker, domain):
        dispatcher.utter_message('here are the information about the book.')
        return []


'Funktionen'
def run_jar(comment):
    print ("start")
    p=subprocess.Popen(comment, stdout=subprocess.PIPE,stderr=subprocess.STDOUT)
    return iter(p.stdout.readline, b'')


def read_file_line(name):
    fobj = open(name)
    for line in fobj:
        print(line.rstrip())
    fobj.close()

def read_file_to_String(name):
    text=open(name).read();
    print(name)

'Hauptprogramm'
for outline in run_jar('java -jar .\\jar\\Testphython.jar'):
    print(outline)

read_file_line(".\\testdatei")
