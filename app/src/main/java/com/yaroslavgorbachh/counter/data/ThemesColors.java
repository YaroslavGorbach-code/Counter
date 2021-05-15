package com.yaroslavgorbachh.counter.data;


import com.yaroslavgorbachh.counter.R;

public enum ThemesColors {
    RED(R.color.colorAccent),
    ORANGE(R.color.colorAccent_orange),
    BLUE(R.color.colorAccent_blue),
    YELLOW(R.color.colorAccent_yellow),
    PURPLE(R.color.colorAccent_purple),
    BLUE_LIGHT(R.color.colorAccent_blue_l),
    GREEN(R.color.colorAccent_green),
    GREEN_DARK(R.color.colorAccent_green_d),
    GREY(R.color.colorAccent_gray);

    private final int colorId;
    public int getColorId(){ return colorId;}
    ThemesColors(int colorId) {
        this.colorId = colorId;
    }
}
