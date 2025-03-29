package com.mingle.entity;

import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.ColorInt;


/**
 * @author zzz40500
 * @version 1.0
 * @date 2015/8/5.
 * @github: https://github.com/zzz40500
 */
public class MenuEntity {

    public @DrawableRes int iconId;
    public @ColorInt int titleColor;
    public CharSequence title;
    public Drawable icon;

    public @ColorInt int addressColor;
    public CharSequence address;

    public @ColorInt int scoreColor;
    public CharSequence score;


}
