package com.gpergrossi.spaceinvaders.ui;

import com.gpergrossi.spaceinvaders.animation.AnimationSystem;
import com.gpergrossi.spaceinvaders.game.Input;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Button extends Component {

    private int x;
    private int y;
    private int width;
    private int height;
    private String text;
    private Font font;

    private boolean hover;
    private boolean pressed;

    private MouseListener mouseListener;
    private MouseMotionListener mouseMotionListener;

    public Button(int x, int y, int width, int height, String text, Font font) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.font = font;

        this.hover = false;
        this.pressed = false;

        this.mouseListener = createMouseListener();
        this.mouseMotionListener = createMouseMotionListener();
    }

    @Override
    public void registerInputListeners(Input input) {
        input.addMouseListener(mouseListener);
        input.addMouseMotionListener(mouseMotionListener);
    }

    @Override
    public void unregisterInputListeners(Input input) {
        input.removeMouseListener(mouseListener);
        input.removeMouseMotionListener(mouseMotionListener);
    }

    @Override
    public void registerAnimations(AnimationSystem animationSystem) {
        // No animations for Button
    }

    @Override
    public void unregisterAnimations(AnimationSystem animationSystem) {
        // No animations for Button
    }

    @Override
    public void render(Graphics2D g) {
        // Background color
        if (pressed) {
            g.setColor(new Color(0, 0, 0, 200));
        } else if (hover) {
            g.setColor(new Color(32, 32, 32, 200));
        } else {
            g.setColor(new Color(32, 32, 32, 127));
        }

        // Draw background
        g.fillRoundRect(x, y, width, height, 3, 3);

        // Foreground color
        if (pressed) {
            g.setColor(Color.LIGHT_GRAY);
        } else if (hover) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(new Color(255, 255, 255, 200));
        }

        // Draw text
        g.setFont(font);
        int strWidth = g.getFontMetrics().stringWidth(text);
        int ascent = g.getFontMetrics().getAscent();
        g.drawString(text, x + (width - strWidth) * 0.5f, y + ascent);

        // Draw outline
        g.drawRoundRect(x, y, width, height, 3, 3);
    }

    private MouseListener createMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getX() >= x && e.getX() < x+width && e.getY() >= y && e.getY() < y+height) {
                    pressed = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
            }
        };
    };

    private MouseMotionListener createMouseMotionListener() {
        return new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.getX() < x || e.getX() >= x+width && e.getY() < y && e.getY() >= y+height) {
                    pressed = false;
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (e.getX() >= x && e.getX() < x+width && e.getY() >= y && e.getY() < y+height) {
                    hover = true;
                } else {
                    hover = false;
                }
            }
        };
    };

}
