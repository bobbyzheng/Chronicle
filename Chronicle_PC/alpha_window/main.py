# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'main0.1.ui'
#
# Created: Tue Feb 28 18:44:41 2012
#      by: PyQt4 UI code generator 4.9
#
# WARNING! All changes made in this file will be lost!

from PyQt4 import QtCore, QtGui

try:
    _fromUtf8 = QtCore.QString.fromUtf8
except AttributeError:
    _fromUtf8 = lambda s: s

class Ui_MainWindow(object):
    def setupUi(self, MainWindow):
        MainWindow.setObjectName(_fromUtf8("MainWindow"))
        MainWindow.resize(814, 696)
        MainWindow.setStyleSheet(_fromUtf8("#MainWindow {\n"
"background: rgb(128,128,128);\n"
"}\n"
"\n"
"#searchBar {\n"
"border: 1px solid rgb(200,200,200);\n"
"border-radius: 10px;\n"
"background: white;\n"
"}\n"
"\n"
"#dayView {\n"
"}\n"
"\n"
"#weekView {\n"
"}\n"
"\n"
"#monthView {\n"
"}"))
        self.centralwidget = QtGui.QWidget(MainWindow)
        self.centralwidget.setObjectName(_fromUtf8("centralwidget"))
        self.searchBar = QtGui.QTextEdit(self.centralwidget)
        self.searchBar.setGeometry(QtCore.QRect(600, 20, 175, 25))
        self.searchBar.setObjectName(_fromUtf8("searchBar"))
        self.tableView = QtGui.QTableView(self.centralwidget)
        self.tableView.setGeometry(QtCore.QRect(25, 50, 750, 575))
        self.tableView.setObjectName(_fromUtf8("tableView"))
        self.addButton = QtGui.QPushButton(self.centralwidget)
        self.addButton.setGeometry(QtCore.QRect(40, 20, 75, 23))
        self.addButton.setObjectName(_fromUtf8("addButton"))
        MainWindow.setCentralWidget(self.centralwidget)
        self.menubar = QtGui.QMenuBar(MainWindow)
        self.menubar.setGeometry(QtCore.QRect(0, 0, 814, 21))
        self.menubar.setObjectName(_fromUtf8("menubar"))
        MainWindow.setMenuBar(self.menubar)
        self.statusbar = QtGui.QStatusBar(MainWindow)
        self.statusbar.setObjectName(_fromUtf8("statusbar"))
        MainWindow.setStatusBar(self.statusbar)

        self.retranslateUi(MainWindow)
        QtCore.QMetaObject.connectSlotsByName(MainWindow)

    def retranslateUi(self, MainWindow):
        MainWindow.setWindowTitle(QtGui.QApplication.translate("MainWindow", "MainWindow", None, QtGui.QApplication.UnicodeUTF8))
        self.searchBar.setHtml(QtGui.QApplication.translate("MainWindow", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">\n"
"<html><head><meta name=\"qrichtext\" content=\"1\" /><style type=\"text/css\">\n"
"p, li { white-space: pre-wrap; }\n"
"</style></head><body style=\" font-family:\'MS Shell Dlg 2\'; font-size:8.25pt; font-weight:400; font-style:normal;\">\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">Search...</span></p></body></html>", None, QtGui.QApplication.UnicodeUTF8))
        self.addButton.setText(QtGui.QApplication.translate("MainWindow", "PushButton", None, QtGui.QApplication.UnicodeUTF8))

