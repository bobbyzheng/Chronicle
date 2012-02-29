import sys
from PyQt4 import QtCore, QtGui
from main import Ui_MainWindow

class StartQT4(QtGui.QMainWindow):
    def __init__(self, parent=None):
        QtGui.QWidget.__init__(self,parent)
        self.ui = Ui_MainWindow()
        self.ui.setupUi(self)
        self.cols = 24
        self.rows = 7
        self.event_widgets = []
        self.setupCalendar(self)
        QtCore.QObject.connect(self.ui.addEventButton, QtCore.SIGNAL("clicked()"), self.addEvent)
        
    def setupCalendar(self):
        for i in range(0,self.rows):
            for j in range(0,self.cols):
                new_event_widget = QtGui.QPushButton(self.ui.centralwidget)
                new_event_widget.setGeometry(self.ui.eventGrid.cellRect(i,j))
                new_event_widget.setObjectName("tmpWidget"+str(len(self.event_widgets)+1))
                self.event_widgets.append(new_event_widget)
                self.ui.eventGrid.addWidget(new_event_widget, i, j)
       


    def addEvent(self):
        


if __name__ == "__main__":
        app = QtGui.QApplication(sys.argv)
        myapp = StartQT4()
        myapp.show()
        sys.exit(app.exec_())

