import sys
from PyQt4 import QtCore, QtGui
from main import Ui_MainWindow

class StartQT4(QtGui.QMainWindow):
    def __init__(self, parent=None):
        QtGui.QWidget.__init__(self,parent)
        self.ui = Ui_MainWindow()
        self.ui.setupUi(self)
        self.event_widgets = []
        
        for i in range(0,5):
            self.ui.eventGrid.setRowMinimumHeight(i,30)
            self.ui.eventGrid.setColumnMinimumWidth(i,30)
            
        for i in range(0,5):
            for j in range(0,5):
                tmp_btn = QtGui.QPushButton(self.ui.centralwidget)
                tmp_btn.setObjectName("eventWidget"+str(len(self.event_widgets)))
                QtCore.QObject.connect(tmp_btn, QtCore.SIGNAL("clicked()"), tmp_btn.hide)
                self.ui.eventGrid.addWidget(tmp_btn, i,j)
                self.event_widgets.append(tmp_btn)
                

            

if __name__ == "__main__":
        app = QtGui.QApplication(sys.argv)
        myapp = StartQT4()
        myapp.show()
        sys.exit(app.exec_())

