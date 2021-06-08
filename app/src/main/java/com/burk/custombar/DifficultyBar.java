package com.burk.custombar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class DifficultyBar extends View {

    private int difficultyLevel = 1;
    private int minLevel = 1;
    private int maxLevel = 7;

    private int[] colors ={ Color.argb(255, 5,  136,  218),
            Color.argb(255, 148, 212, 255),
            Color.argb(255, 141, 216, 255),
            Color.argb(255, 231, 166, 255),
            Color.argb(255, 255, 87, 56) };

    private float[] positions ={ 0.0f, 0.28f, 0.44f, 0.7f, 1.0f };


    public DifficultyBar(Context context) {
        super(context);
        init(context, null, R.attr.difficultyLevel, 0);
    }

    public DifficultyBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, R.attr.difficultyLevel, 0);
    }

    public DifficultyBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public DifficultyBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DifficultyBar,
                defStyleAttr, defStyleRes);

        try {
            difficultyLevel = a.getInteger(R.styleable.DifficultyBar_difficultyLevel, minLevel);
            if (difficultyLevel < minLevel) difficultyLevel = minLevel;
            if (difficultyLevel > maxLevel) difficultyLevel = maxLevel;
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float barHeight = getResources().getDimensionPixelSize(R.dimen.barHeight);
        float barInset = getResources().getDimensionPixelSize(R.dimen.barInset);
        float barCornerRadius = getResources().getDimensionPixelSize(R.dimen.barCornerRadius);
        float circleRadius = getResources().getDimensionPixelSize(R.dimen.circleRadius);

        if (canvas.getHeight() < barHeight + barInset * 2)  { return; }
        if (canvas.getWidth() < circleRadius * 2 * maxLevel) { return; }

        Shader gradientShader = new LinearGradient(barInset, 0, canvas.getWidth() , 0, colors, positions, Shader.TileMode.MIRROR);
        Paint paint = new Paint();
        paint.setShader(gradientShader);
        paint.setAntiAlias(true);

        canvas.drawRoundRect(new RectF(barInset, barInset, canvas.getWidth() - barInset,  barHeight + barInset), barCornerRadius, barCornerRadius, paint);

        float barWidth = canvas.getWidth() - barInset * 2;
        float levelLocationX = barWidth / maxLevel * difficultyLevel + circleRadius ;
        float levelLocationY = barInset + barHeight / 2;

        canvas.drawCircle(levelLocationX, levelLocationY, circleRadius, paint);

        Paint circlePaint = new Paint();
        circlePaint.setColor(Color.WHITE);
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(2);
        circlePaint.setStyle(Paint.Style.STROKE);

        canvas.drawCircle(levelLocationX, levelLocationY, circleRadius, circlePaint);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.fontSize));
        textPaint.setTypeface(Typeface.create("Ubuntu",Typeface.NORMAL));
        textPaint.setAntiAlias(true);

        String levelText = String.valueOf(difficultyLevel);
        Rect bounds = new Rect();
        textPaint.getTextBounds(levelText, 0 , 1, bounds);

        canvas.drawText(levelText, levelLocationX, levelLocationY + (bounds.height() / 2), textPaint);

    }
}
