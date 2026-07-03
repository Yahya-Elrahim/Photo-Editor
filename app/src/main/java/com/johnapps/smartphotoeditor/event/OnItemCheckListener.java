package com.johnapps.smartphotoeditor.event;

import com.johnapps.smartphotoeditor.entity.Photo;

public interface OnItemCheckListener {
    boolean onItemCheck(int i, Photo photo, int i2);
}
