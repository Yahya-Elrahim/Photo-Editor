package com.johnapps.smartphotoeditor.event;

import android.view.MotionEvent;

import com.johnapps.smartphotoeditor.PolishStickerView;

public interface StickerIconEvent {
    void onActionDown(PolishStickerView paramStickerView, MotionEvent paramMotionEvent);

    void onActionMove(PolishStickerView paramStickerView, MotionEvent paramMotionEvent);

    void onActionUp(PolishStickerView paramStickerView, MotionEvent paramMotionEvent);
}
