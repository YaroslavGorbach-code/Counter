package com.yaroslavgorbachh.counter.counterSettings.themes;


import com.yaroslavgorbachh.counter.R;

public enum Themes {
    RED(R.style.AppTheme),
    ORANGE(R.style.AppTheme_orange),
    BLUE(R.style.AppTheme_blue),
    YELLOW(R.style.AppTheme_yellow),
    PURPLE(R.style.AppTheme_purple),
    BLUE_LIGHT(R.style.AppTheme_blue_light),
    GREEN(R.style.AppTheme_green),
    GREEN_DARK(R.style.AppTheme_green_dark),
    GREY(R.style.AppTheme_gray);

    private final int styleId;
    public int getStyleId(){ return styleId;}
    Themes(int styleId) {
        this.styleId = styleId;
    }
}
