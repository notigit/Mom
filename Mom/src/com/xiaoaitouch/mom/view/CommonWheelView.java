
package com.xiaoaitouch.mom.view;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;

import antistatic.spinnerwheel.WheelVerticalView;

public final class CommonWheelView extends WheelVerticalView {
    public CommonWheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CommonWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonWheelView(Context context) {
        super(context);
    }

    @Override
    public void setSelectorPaintCoeff(float coeff) {
        LinearGradient shader;

        int h = getMeasuredHeight();
        int ih = getItemDimension();
        float p1 = (1 - ih / (float) h) / 2;
        float p2 = (1 + ih / (float) h) / 2;

        final int[] colors = {
                0x06000000, 0x50000000, 0xff000000, 0xff000000,
                0x50000000, 0x06000000
        };
        float[] positions = {
                0, p1, p1, p2, p2, 1
        };
        shader = new LinearGradient(0, 0, 0, h, colors, positions,
                Shader.TileMode.CLAMP);
        mSelectorWheelPaint.setShader(shader);
        invalidate();
    }
}
