#ifndef CHRONICLEGRIDLAYOUT_H
#define CHRONICLEGRIDLAYOUT_H

#include <QLayout>

typedef struct {
    int row;
    int col;
    QLayoutItem* item;
} EventItem;

class ChronicleGridLayout : public QLayout
{
    Q_OBJECT
private:
    int m_rows;
    int m_cols;
    QList<EventItem*> itemlist;

public:
    explicit ChronicleGridLayout(QWidget *parent = 0, int rows=0, int cols=0);
    ~ChronicleGridLayout();
    void addItem(QLayoutItem* item, int col, int row);
    int count() const;
    QLayoutItem* itemAt(int index) const;
    QLayoutItem* itemAt(int col, int row) const;
    QLayoutItem* takeAt(int index);
    QLayoutItem* takeAt(int col, int row);
    QSize sizeHint() const;
    void setGeometry(const QRect &r);
    QSize minimumSize();
    int getRows();
    int getCols();
    
signals:
    
public slots:
    
};

#endif // CHRONICLEGRIDLAYOUT_H
