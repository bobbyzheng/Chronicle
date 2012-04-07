#include "chroniclemainwindow.h"
#include "ui_chroniclemainwindow.h"

ChronicleMainWindow::ChronicleMainWindow(QWidget *parent) :
    QMainWindow(parent), ui(new Ui::ChronicleMainWindow) {
    ui->setupUi(this);
}

ChronicleMainWindow::~ChronicleMainWindow() {
    delete ui;
}
