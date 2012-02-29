import sys
from PyQt4 import QtCore, QtGui
from main import Ui_MainWindow

class StartQT4(QtGui.QMainWindow):
    def __init__(self, parent=None):
        QtGui.QWidget.__init__(self,parent)
        self.ui = Ui_MainWindow()
        self.ui.setupUi(self)

        QtCore.QObject.connect(self.ui.addButton, QtCore.SIGNAL("clicked()"), self.blah)
        
    def blah(self):
        self.ui.searchBar.setText("AAAA")



if __name__ == "__main__":
        app = QtGui.QApplication(sys.argv)
        myapp = StartQT4()
        myapp.show()
        sys.exit(app.exec_())

