package com.johnapps.smartphotoeditor.event;

import android.view.MotionEvent;

import com.johnapps.smartphotoeditor.PolishStickerView;

public class EditTextIconEvent implements StickerIconEvent {
    public void onActionDown(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionMove(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionUp(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
        paramStickerView.editTextSticker();
    }
}
