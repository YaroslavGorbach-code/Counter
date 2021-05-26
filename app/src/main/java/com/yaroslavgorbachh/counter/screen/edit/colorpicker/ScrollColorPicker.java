package com.yaroslavgorbachh.counter.screen.edit.colorpicker;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yaroslavgorbachh.counter.R;

public class ScrollColorPicker implements ColorPicker {
    private final int[] colorIds;
    private final View[] marks;
    private int mSelectedColorId;

    public ScrollColorPicker(View root, Resources resources) {
        colorIds = new int[]{R.color.red, R.color.pink, R.color.purple,
                R.color.deep_purple, R.color.blue, R.color.cyan,
                R.color.teal, R.color.green, R.color.light_green,
                R.color.lime, R.color.yellow, R.color.amber, R.color.orange,
                R.color.deep_orange, R.color.brown, R.color.grey, R.color.blue_grey};
        marks = new View[colorIds.length];

        ViewGroup content = root.findViewById(R.id.color_picker_parent);
        LayoutInflater inflater = LayoutInflater.from(content.getContext());
        for (int i = 0; i < colorIds.length; i++) {
            View item = inflater.inflate(R.layout.item_color_picker, content, false);
            ImageView bg = item.findViewById(R.id.bg);
            ImageView mark = item.findViewById(R.id.mark);
            marks[i] = mark;
            final int colorId = colorIds[i];
            bg.setColorFilter(resources.getColor(colorId));
            item.setOnClickListener(v -> setColorId(colorId));
            content.addView(item, i);
        }
        setColorId(colorIds[2]);
    }

    @Override
    public void setColorId(int colorId) {
        mSelectedColorId = colorId;
        for (int i = 0; i < colorIds.length; i++) {
            if (colorId == colorIds[i]) {
                marks[i].setVisibility(View.VISIBLE);
            } else {
                marks[i].setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getColorId() {
        return mSelectedColorId;
    }
}
