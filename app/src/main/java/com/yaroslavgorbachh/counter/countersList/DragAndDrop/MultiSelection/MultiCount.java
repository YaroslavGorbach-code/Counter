package com.yaroslavgorbachh.counter.countersList.DragAndDrop.MultiSelection;

import com.yaroslavgorbachh.counter.counterHistory.recyclerView.ItemTouchHelperSwipeListener;

public interface MultiCount extends MultiSelection{
    void decAll();
    void incAll();
    void resetAll();
    void undoResetAll();
    void deleteAll();
}
