package com.johnapps.smartphotoeditor.event;

public class FlipHorizontallyEvent extends AbstractFlipEvent {
    protected int getFlipDirection() {
        return 1;
    }
}
