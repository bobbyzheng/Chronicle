#include <QtGui/QApplication>
#include "chroniclemainwindow.h"

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    ChronicleMainWindow w;
    w.show();
    
    return a.exec();
}
