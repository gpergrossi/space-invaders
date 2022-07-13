package com.gpergrossi.spaceinvaders.ui;

import com.gpergrossi.spaceinvaders.animation.*;
import com.gpergrossi.spaceinvaders.game.Input;

import java.awt.*;
import java.util.function.DoubleSupplier;

public class ScoreboardText extends Component {

    private int x;
    private int y;
    private int width;
    private int height;
    private int labelWidth;

    private String label;
    private String format;
    private DoubleSupplier valueSupplier;

    private Font font;
    private int lineIndex;

    private TweenSequence<Double> fadeInAnimation;
    private TweenSequence<Double> valueAnimation;

    public ScoreboardText(int x, int y, int width, int height, int labelWidth,
                               String label, String format, DoubleSupplier valueSupplier,
                               Font font, int lineIndex)
    {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.labelWidth = labelWidth;

        this.label = label;
        this.format = format;
        this.valueSupplier = valueSupplier;

        this.lineIndex = lineIndex;
        this.font = font;

        this.fadeInAnimation = new TweenSequence<>("Scoreboard Value Animation",
                new TweenStep<>(1.0, 0.0, 1.0, LerpFunction.LERP_DOUBLE, TweenFunction.EASE_OUT_QUAD)
        );

        this.valueAnimation = new TweenSequence<>("Scoreboard Value Animation",
                new TweenStep<>(3.0, 0.0, 1.0, LerpFunction.LERP_DOUBLE, TweenFunction.EASE_OUT_QUAD)
        );

        this.fadeInAnimation.setDefaultStartTime(lineIndex * -1.0f);
        this.valueAnimation.setDefaultStartTime(lineIndex * -1.0f);
    }

    @Override
    public void registerInputListeners(Input input) {
        // No input listeners for ScoreboardText
    }

    @Override
    public void unregisterInputListeners(Input input) {
        // No input listeners for ScoreboardText
    }

    @Override
    public void registerAnimations(AnimationSystem animationSystem) {
        animationSystem.start(fadeInAnimation, fadeInAnimation.getDefaultStartTime(), false, null);
        animationSystem.start(valueAnimation, valueAnimation.getDefaultStartTime(), false, null);
    }

    @Override
    public void unregisterAnimations(AnimationSystem animationSystem) {
        animationSystem.remove(fadeInAnimation);
        animationSystem.remove(valueAnimation);
    }

    @Override
    public void render(Graphics2D g) {
        g.setFont(font);

        float alpha = (float) (double) fadeInAnimation.getValue();
        g.setColor(new Color(255, 255, 255, (int) (255 * alpha)));

        // Left-aligned label
        g.drawString(label, x, y);

        // Value display
        double value = valueAnimation.getValue() * valueSupplier.getAsDouble();
        String valueStr = String.format(format, value);
        if (Double.isNaN(value)) valueStr = "N/A";

            // Right-aligned value
        int valueStrWidth = g.getFontMetrics().stringWidth(valueStr);
        float valueX = x + width - valueStrWidth;
        g.drawString(valueStr, valueX, y);
    }
}
