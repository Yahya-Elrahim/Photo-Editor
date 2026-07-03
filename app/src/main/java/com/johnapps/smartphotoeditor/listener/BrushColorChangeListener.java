package com.johnapps.smartphotoeditor.listener;

import com.johnapps.smartphotoeditor.draw.BrushDrawingView;

public interface BrushColorChangeListener {
    void onStartDrawing();

    void onStopDrawing();

    void onViewAdd(BrushDrawingView brushDrawingView);

    void onViewRemoved(BrushDrawingView brushDrawingView);
}
