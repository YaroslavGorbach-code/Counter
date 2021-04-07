package com.yaroslavgorbachh.counter.counterSettings.themes;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;

public class ScrollColorPicker implements ColorPicker {
    private final int[] colors;
    private final View[] marks;
    private int mSelectedColor;

    public ScrollColorPicker(View root, Resources resources) {
        colors = new int[]{resources.getColor(ThemesColors.RED.getColorId()),
                resources.getColor(ThemesColors.ORANGE.getColorId()),
                resources.getColor(ThemesColors.BLUE.getColorId()),
                resources.getColor(ThemesColors.YELLOW.getColorId()),
                resources.getColor(ThemesColors.PURPLE.getColorId()),
                resources.getColor(ThemesColors.BLUE_LIGHT.getColorId()),
                resources.getColor(ThemesColors.GREEN_DARK.getColorId()),
                resources.getColor(ThemesColors.GREEN.getColorId()),
                resources.getColor(ThemesColors.GREY.getColorId())};
        marks = new View[colors.length];

        ViewGroup content = root.findViewById(R.id.colors);
        LayoutInflater inflater = LayoutInflater.from(content.getContext());
        for (int i = 0; i < colors.length; i++) {
            View item = inflater.inflate(R.layout.color_picker_i, content, false);
            ImageView bg = item.findViewById(R.id.i_color_bg);
            ImageView mark = item.findViewById(R.id.i_color_selected);
            marks[i] = mark;
            final int color = colors[i];
            bg.setColorFilter(color);
            item.setOnClickListener(v -> setColor(color));
            content.addView(item, i);
        }
        setColor(ThemeUtility.fetchAccentColor(root.getContext()));
    }

    @Override
    public void setColor(int color) {
        mSelectedColor = color;
        for (int i = 0; i < colors.length; i++) {
            if (color == colors[i]) {
                marks[i].setVisibility(View.VISIBLE);
            } else {
                marks[i].setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getColor(Context context) {
        return mSelectedColor;
    }
}
