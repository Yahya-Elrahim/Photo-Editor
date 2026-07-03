package com.johnapps.smartphotoeditor.event;

import android.view.MotionEvent;

import com.johnapps.smartphotoeditor.PolishStickerView;

public class ZoomIconEvent implements StickerIconEvent {
    public void onActionDown(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionMove(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
        paramStickerView.zoomAndRotateCurrentSticker(paramMotionEvent);
    }

    public void onActionUp(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
        if (paramStickerView.getOnStickerOperationListener() != null)
            paramStickerView.getOnStickerOperationListener().onStickerZoom(paramStickerView.getCurrentSticker());
    }
}
