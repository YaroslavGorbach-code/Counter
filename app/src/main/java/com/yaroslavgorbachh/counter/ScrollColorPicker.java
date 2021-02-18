package com.yaroslavgorbachh.counter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ScrollColorPicker implements ColorPicker{
    public static final String THEME_VALUE_KEY = "THEME_VALUE_KEY";

    private final int[] colors;
    private final View[] marks;
    private int mSelectedColor;

    public ScrollColorPicker(View root){
        SharedPreferences preferences = root.getContext().getSharedPreferences(THEME_VALUE_KEY, Context.MODE_PRIVATE);
        colors = new int[] {root.getContext().getResources().getColor(R.color.colorAccent),
                 root.getContext().getResources().getColor(R.color.colorAccent_orange)};
         marks = new View[colors.length];

        ViewGroup content = root.findViewById(R.id.colors);
        LayoutInflater inflater = LayoutInflater.from(content.getContext());
        for (int i = 0; i < colors.length ; i++) {
            View item = inflater.inflate(R.layout.color_picker_i, content, false);
            ImageView bg = item.findViewById(R.id.i_color_bg);
            ImageView mark = item.findViewById(R.id.i_color_selected);
            marks[i] = mark;
            final int color = colors[i];
            bg.setColorFilter(color);
            item.setOnClickListener(v -> setColor(color));
            content.addView(item, i);
        }
        setColor(preferences.getInt(THEME_VALUE_KEY, root.getContext().getResources().getColor(R.color.colorAccent)));
    }
    @Override
    public void setColor(int color) {
        mSelectedColor = color;
        for (int i = 0; i <colors.length; i++) {
            if (color == colors[i]){
                marks[i].setVisibility(View.VISIBLE);
            }else {
                marks[i].setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getColor() {
        return mSelectedColor;
    }
}
