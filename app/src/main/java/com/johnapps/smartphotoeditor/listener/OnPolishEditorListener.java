package com.johnapps.smartphotoeditor.listener;

import com.johnapps.smartphotoeditor.draw.Drawing;

public interface OnPolishEditorListener {
    void onAddViewListener(Drawing viewType, int i);


    void onRemoveViewListener(int i);

    void onRemoveViewListener(Drawing viewType, int i);

    void onStartViewChangeListener(Drawing viewType);

    void onStopViewChangeListener(Drawing viewType);
}
