package com.yaroslavgorbachh.counter.screen.countersList.DragAndDrop.MultiSelection;

public interface MultiCount extends MultiSelection{
    void decAll();
    void incAll();
    void resetAll();
    void undoResetAll();
    void deleteAll();
}
