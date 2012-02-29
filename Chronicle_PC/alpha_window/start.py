import sys
from PyQt4 import QtCore, QtGui
from main import Ui_MainWindow

class StartQT4(QtGui.QMainWindow):
    def __init__(self, parent=None):
        QtGui.QWidget.__init__(self,parent)
        self.ui = Ui_MainWindow()
        self.ui.setupUi(self)
        self.event_widgets = []
        QtCore.QObject.connect(self.ui.addEventButton, QtCore.SIGNAL("clicked()"), self.addEvent)
        
    def addEvent(self):
        cols = self.ui.eventGrid.columnCount()
        rows = self.ui.eventGrid.rowCount()
        eventindex = (int(len(self.event_widgets)/rows),len(self.event_widgets)%cols)

        new_event_widget = QtGui.QPushButton(self.ui.centralwidget)
        new_event_widget.setGeometry(self.ui.eventGrid.cellRect(eventindex[0], eventindex[1]))
        new_event_widget.setObjectName("tmpWidget"+str(len(self.event_widgets)+1))
        
        self.event_widgets.append(new_event_widget)
        self.ui.eventGrid.addWidget(new_event_widget)


if __name__ == "__main__":
        app = QtGui.QApplication(sys.argv)
        myapp = StartQT4()
        myapp.show()
        sys.exit(app.exec_())

