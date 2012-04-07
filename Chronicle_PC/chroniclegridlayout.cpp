#include "chroniclegridlayout.h"

ChronicleGridLayout::ChronicleGridLayout(QWidget *parent, int rows, int cols) :
    QLayout(parent) , m_rows(rows), m_cols(cols) {
}

ChronicleGridLayout::~ChronicleGridLayout() {
    for(int i=0 ; i<itemlist.size() ; i++) {
        delete itemlist[i];
    }
}

void ChronicleGridLayout::addItem(QLayoutItem* item, int row, int col) {
    if(row>m_rows || col>m_cols) return;
    EventItem* tmp_item = new EventItem;
    tmp_item->row = row;
    tmp_item->col = col;
    tmp_item->item = item;
    itemlist.append(tmp_item);
}

int ChronicleGridLayout::count() const {
    return itemlist.size();
}

QLayoutItem* ChronicleGridLayout::itemAt(int index) const {
    return itemlist.value(index)->item;
}

QLayoutItem* ChronicleGridLayout::itemAt(int col, int row) const {
    for(int i=0 ; i<itemlist.size() ; i++) {
        if(itemlist[i]->col == col && itemlist[i]->row == row)
            return itemlist[i]->item;
    }
    return 0;
}

QLayoutItem* ChronicleGridLayout::takeAt(int index) {
    if(index<0 || index>=itemlist.size()) return 0;
    else return itemlist.value(index)->item;
}

QLayoutItem* ChronicleGridLayout::takeAt(int col, int row) {
    for(int i=0 ; i<itemlist.size() ; i++) {
        if(itemlist[i]->col == col && itemlist[i]->row == row)
            return itemlist[i]->item;
    }
    return 0;
}

QSize ChronicleGridLayout::sizeHint() const {
    QSize size;
    EventItem *item;
    foreach (item, itemlist)
        size = size.expandedTo(item->item->minimumSize());

    size += QSize(2*margin(), 2*margin());
    return size;
}

void ChronicleGridLayout::setGeometry(const QRect &r) {
    QLayout::setGeometry(r);
}

QSize ChronicleGridLayout::minimumSize() {
    QSize size;
    EventItem *item;
    foreach (item, itemlist)
        size = size.expandedTo(item->item->minimumSize());

    size += QSize(2*margin(), 2*margin());
    return size;
}

int ChronicleGridLayout::getRows() {
    return m_rows;
}

int ChronicleGridLayout::getCols() {
    return m_cols;
}
