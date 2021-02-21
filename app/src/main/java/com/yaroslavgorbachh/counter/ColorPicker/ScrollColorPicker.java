package com.yaroslavgorbachh.counter.ColorPicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yaroslavgorbachh.counter.ColorPicker.ColorPicker;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;

public class ScrollColorPicker implements ColorPicker {
    private final int[] colors;
    private final View[] marks;
    private int mSelectedColor;

    public ScrollColorPicker(View root){

        colors = new int[] {root.getContext().getResources().getColor(R.color.colorAccent),
                root.getContext().getResources().getColor(R.color.colorAccent_orange),
                root.getContext().getResources().getColor(R.color.colorAccent_blue),
                root.getContext().getResources().getColor(R.color.colorAccent_yellow),
                root.getContext().getResources().getColor(R.color.colorAccent_purple),
                root.getContext().getResources().getColor(R.color.colorAccent_blue_l),
                root.getContext().getResources().getColor(R.color.colorAccent_green_d),
                root.getContext().getResources().getColor(R.color.colorAccent_green),
                root.getContext().getResources().getColor(R.color.colorAccent_gray)};
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
        setColor(Utility.fetchAccentColor(root.getContext()));
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
    public int getColor(Context context) {
        return mSelectedColor;
    }
}
