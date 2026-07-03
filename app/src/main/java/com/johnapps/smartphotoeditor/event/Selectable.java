package com.johnapps.smartphotoeditor.event;

import com.johnapps.smartphotoeditor.entity.Photo;

public interface Selectable {

    int getSelectedItemCount();

    boolean isSelected(Photo photo);

}
