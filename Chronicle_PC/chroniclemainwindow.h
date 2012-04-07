#ifndef CHRONICLEMAINWINDOW_H
#define CHRONICLEMAINWINDOW_H

#include <QMainWindow>

namespace Ui {
class ChronicleMainWindow;
}

class ChronicleMainWindow : public QMainWindow
{
    Q_OBJECT
    
public:
    explicit ChronicleMainWindow(QWidget *parent = 0);
    ~ChronicleMainWindow();
    
private:
    Ui::ChronicleMainWindow *ui;
};

#endif // CHRONICLEMAINWINDOW_H
