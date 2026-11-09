package com.polina.love;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class HeartView extends View {
    private static final String DATE = "08.11.2026";
    private static final String MESSAGE =
        "любимая, мы прошли многое за эти 10 месяцев, и я горжусь тем что ты у меня есть, "
        + "я каждый день думаю о тебе, о том как ты себя чувствуешь, и пока меня не было я очень "
        + "переживал за тебя, я понимаю, я не лучший но я стараюсь быть рядом, я пытаюсь все "
        + "объяснять, чтобы было понятно, и даже через тысячи ссор, я надеюсь что мы будем рядом. "
        + "Я люблю тебя ❤️💖. Прости за все обиды, прости за все ссоры... Ты для меня стала частью "
        + "моей жизни, я надеюсь что с тобой все будет хорошо, и мы дождемся друг друга 💘";

    private final Paint tagPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint boxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint namePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint msgPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private final Paint datePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float[] tags;
    private boolean started = false;
    private float progress = 0f;
    private float cx, cy, scale;
    private StaticLayout msgLayout;

    public HeartView(Context c) {
        super(c);
        setBackgroundColor(Color.rgb(8, 0, 6));
        tagPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        namePaint.setColor(Color.WHITE);
        namePaint.setTypeface(Typeface.SERIF);
        namePaint.setTextAlign(Paint.Align.CENTER);
        namePaint.setShadowLayer(12f, 0, 0, Color.BLACK);
        datePaint.setColor(Color.WHITE);
        datePaint.setTextAlign(Paint.Align.CENTER);
        datePaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        msgPaint.setColor(Color.WHITE);
    }

    public void start() {
        started = true;
        ValueAnimator a = ValueAnimator.ofFloat(0f, 1f);
        a.setDuration(3500);
        a.setInterpolator(new DecelerateInterpolator(1.2f));
        a.addUpdateListener(v -> { progress = (float) v.getAnimatedValue(); invalidate(); });
        a.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        scale = w * 0.38f;
        cx = w / 2f;
        cy = h * 0.34f;
        tags = Native.heartTags(cx, cy, scale, 320, 2026L);
        namePaint.setTextSize(scale * 0.28f);
        datePaint.setTextSize(scale * 0.14f);
        // подбираем размер текста, чтобы он поместился под сердцем
        int textW = (int) (w * 0.88f);
        float availH = h - (cy + scale * 1.2f) - h * 0.03f;
        float sp = scale * 0.115f;
        do {
            msgPaint.setTextSize(sp);
            msgLayout = StaticLayout.Builder.obtain(MESSAGE, 0, MESSAGE.length(), msgPaint, textW)
                .setAlignment(Layout.Alignment.ALIGN_CENTER).setLineSpacing(0f, 1.1f).build();
            sp -= 1f;
        } while (msgLayout.getHeight() > availH && sp > 8f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (tags != null && started) {
            for (int i = 0; i < tags.length; i += 6) {
                float t = Math.max(0f, Math.min(1f, (progress - tags[i + 4] * 0.5f) / 0.5f));
                if (t <= 0f) continue;
                float x = cx + (tags[i] - cx) * t;
                float y = cy + (tags[i + 1] - cy) * t;
                float shade = tags[i + 5];
                tagPaint.setTextSize(tags[i + 3]);
                canvas.save();
                canvas.translate(x, y);
                canvas.rotate(tags[i + 2] * t);
                float w = tagPaint.measureText("I love you");
                float alpha = 255 * t;
                if (shade > 0.85f) {
                    boxPaint.setColor(Color.argb((int) alpha, 245, 245, 245));
                    canvas.drawRect(-w / 2 - 3, -tags[i + 3], w / 2 + 3, tags[i + 3] * 0.3f, boxPaint);
                    tagPaint.setColor(Color.argb((int) alpha, 40, 0, 10));
                } else if (shade > 0.45f) {
                    tagPaint.setColor(Color.argb((int) alpha, 255, 255, 255));
                } else {
                    tagPaint.setColor(Color.argb((int) alpha, 225, 40, 75));
                }
                canvas.drawText("I love you", -w / 2, 0, tagPaint);
                canvas.restore();
            }
        }
        canvas.drawText("Полина", cx, cy + namePaint.getTextSize() * 0.3f, namePaint);

        if (started && progress > 0.6f) {
            int a = (int) (255 * Math.min(1f, (progress - 0.6f) / 0.4f));
            datePaint.setAlpha(a);
            canvas.drawText(DATE, cx, cy - scale * 1.15f, datePaint);
            msgPaint.setAlpha(a);
            canvas.save();
            canvas.translate((getWidth() - msgLayout.getWidth()) / 2f, cy + scale * 1.2f);
            msgLayout.draw(canvas);
            canvas.restore();
        }
    }
}
