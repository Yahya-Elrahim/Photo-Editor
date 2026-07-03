package com.johnapps.smartphotoeditor.utils;

import com.johnapps.smartphotoeditor.grid.PolishLayout;
import com.johnapps.smartphotoeditor.layer.slant.SlantLayoutHelper;
import com.johnapps.smartphotoeditor.layer.straight.StraightLayoutHelper;

import java.util.ArrayList;
import java.util.List;

public class CollageUtils {

    private CollageUtils() {}

    public static List<PolishLayout> getCollageLayouts(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(SlantLayoutHelper.getAllThemeLayout(i));
        arrayList.addAll(StraightLayoutHelper.getAllThemeLayout(i));
        return arrayList;
    }
}
