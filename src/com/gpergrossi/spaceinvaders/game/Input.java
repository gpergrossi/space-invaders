package com.gpergrossi.spaceinvaders.game;

import java.awt.event.*;
import java.util.ArrayList;

/**
 * The input class is responsible for providing the various input listeners for mouse and keyboard.
 * It will also provide convenient methods to query the status of the various game-specific inputs.
 *
 * <p>
 * For example:
 *   isLeftPressed()
 *   isRightPressed()
 *   isFirePressed()
 * </p>
 *
 * Some input may be handled by sending input events directly to the game. These input events
 * can be listened for by adding an appropriate listener to the Input object with one of the
 * add*Listener() methods.
 *
 * The waitKey method also has an optional callback argument that can be used to respond
 * immediately when a key is finally pressed by the user.
 *
 */
public class Input {

    private ArrayList<KeyListener> keyListeners;
    private ArrayList<MouseListener> mouseListeners;
    private ArrayList<MouseMotionListener> mouseMotionListeners;
    private ArrayList<MouseWheelListener> mouseWheelListeners;

    /** True if we're holding up game play until a key has been pressed */
    private boolean waitingForKeyPress;

    /** A callback that is run() when the user presses a key while the input system is in a waitKey state. */
    private Runnable waitKeyCallback;

    /** Flag is set to let the checkCallbacks() method know it's time to run */
    private boolean waitKeyCleared;

    /** True if the left cursor key is currently pressed */
    private boolean leftPressed;

    /** True if the right cursor key is currently pressed */
    private boolean rightPressed;

    /** True if we are firing */
    private boolean firePressed;

    /** True if the escape key is currently pressed */
    private boolean escapePressed;

    /** True if the escape key is currently locked, meaning it won't register again until it is released */
    private boolean escapeLocked;

    /** True if the enter key is currently pressed */
    private boolean enterPressed;

    /** True if the enter key is currently locked, meaning it won't register again until it is released */
    private boolean enterLocked;


    public Input() {
        keyListeners = new ArrayList<>();
        mouseListeners = new ArrayList<>();
        mouseMotionListeners = new ArrayList<>();
        mouseWheelListeners = new ArrayList<>();

        reset();
    }


    /** Resets the state of the input flags, does NOT affect registered listeners. */
    public void reset() {
        waitingForKeyPress = false;
        leftPressed = false;
        rightPressed = false;
        firePressed = false;
        escapePressed = false;
        enterPressed = false;
        enterLocked = false;
    }

    /** Tells the input system to ignore all input until a key is typed. */
    public void waitKey() {
        reset();
        waitingForKeyPress = true;
    }

    /**
     * Tells the input system to ignore all input until a key is typed.
     *
     * Please note that if we are already waiting for a key press, the
     * original callback function will be overwritten and never called.
     *
     * @param callback A callback function that will be called when the
     *                 user finally does press a key.
     */
    public void waitKey(Runnable callback) {
        reset();
        waitingForKeyPress = true;
        waitKeyCallback = callback;
    }

    /**
     * Stops the wait key without processing its callback.
     */
    public void stopWaitKey() {
        this.waitingForKeyPress = false;
        this.waitKeyCallback = null;
        this.waitKeyCleared = false;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isFirePressed() {
        return firePressed;
    }

    public boolean wasEscapePressed() {
        if (!escapePressed) return false;
        if (escapeLocked) return false;
        escapeLocked = true;
        return true;
    }

    public boolean wasEnterPressed() {
        if (!enterPressed) return false;
        if (enterLocked) return false;
        enterLocked = true;
        return true;
    }

    public boolean isWaitingForKeyPress() {
        return waitingForKeyPress;
    }

    public KeyListener getKeyListener() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // If we're waiting for "press any key", then we don't want to
                // process any input events except for keyTyped events.
                if (!waitingForKeyPress) {

                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        leftPressed = true;
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        rightPressed = true;
                    } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        firePressed = true;
                    } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        escapePressed = true;
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        enterPressed = true;
                    }

                    // This needs to be thread-safe in case an event comes through
                    // while the main thread is adding/removing listeners.
                    synchronized (this) {
                        // Pass event to other listeners that may have been registered
                        for (KeyListener kl : keyListeners) {
                            kl.keyPressed(e);
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // If we're waiting for "press any key", then we don't want to
                // process any input events except for keyTyped events.
                if (!waitingForKeyPress) {

                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        leftPressed = false;
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        rightPressed = false;
                    } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        firePressed = false;
                    } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        escapePressed = false;
                        escapeLocked = false;
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        enterPressed = false;
                        enterLocked = false;
                    }

                    // This needs to be thread-safe in case an event comes through
                    // while the main thread is adding/removing listeners.
                    synchronized (this) {
                        // Pass event to other listeners that may have been registered
                        for (KeyListener kl : keyListeners) {
                            kl.keyReleased(e);
                        }
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // The first time a key is type, we can clear the waitingForKeyPress flag,
                // but we still don't want to process the keyTyped event until the next
                // keyTypedEvent is fired.
                if (waitingForKeyPress) {

                    // No longer waiting for a key press.
                    waitingForKeyPress = false;
                    waitKeyCleared = true;

                } else {
                    // This needs to be thread-safe in case an event comes through
                    // while the main thread is adding/removing listeners.
                    synchronized(this) {
                        // Pass event to other listeners that may have been registered
                        for (KeyListener kl : keyListeners) {
                            kl.keyTyped(e);
                        }
                    }
                }
            }
        };
    }

    public void checkCallbacks() {
        if (waitKeyCleared) {
            waitKeyCleared = false;

            // Call the callback if it was provided.
            if (waitKeyCallback != null) {
                // We have to do things in a weird order in case the callback
                // method itself tries to set a new wait-key callback.
                Runnable cb = waitKeyCallback;
                waitKeyCallback = null;
                cb.run();
            }
        }
    }

    public MouseListener getMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                // This needs to be thread-safe in case an event comes through
                // while the main thread is adding/removing listeners.
                synchronized(this) {
                    // Pass event to other listeners that may have been registered
                    for (MouseListener ml : mouseListeners) {
                        ml.mousePressed(e);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                // This needs to be thread-safe in case an event comes through
                // while the main thread is adding/removing listeners.
                synchronized(this) {
                    // Pass event to other listeners that may have been registered
                    for (MouseListener ml : mouseListeners) {
                        ml.mouseReleased(e);
                    }
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {

                // This needs to be thread-safe in case an event comes through
                // while the main thread is adding/removing listeners.
                synchronized(this) {
                    // Pass event to other listeners that may have been registered
                    for (MouseListener ml : mouseListeners) {
                        ml.mouseClicked(e);
                    }
                }
            }
        };
    };

    public MouseMotionListener getMouseMotionListener() {
        return new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

                // This needs to be thread-safe in case an event comes through
                // while the main thread is adding/removing listeners.
                synchronized(this) {
                    // Pass event to other listeners that may have been registered
                    for (MouseMotionListener mml : mouseMotionListeners) {
                        mml.mouseMoved(e);
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {

                // This needs to be thread-safe in case an event comes through
                // while the main thread is adding/removing listeners.
                synchronized(this) {
                    // Pass event to other listeners that may have been registered
                    for (MouseMotionListener mml : mouseMotionListeners) {
                        mml.mouseDragged(e);
                    }
                }
            }
        };
    };

    public MouseWheelListener getMouseWheelListener() {
        return new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {

                // This needs to be thread-safe in case an event comes through
                // while the main thread is adding/removing listeners.
                synchronized(this) {
                    // Pass event to other listeners that may have been registered
                    for (MouseWheelListener mwl : mouseWheelListeners) {
                        mwl.mouseWheelMoved(e);
                    }
                }
            }
        };
    };

    public synchronized void addKeyListener(KeyListener kl) {
        this.keyListeners.add(kl);
    }

    public synchronized void removeKeyListener(KeyListener kl) {
        this.keyListeners.remove(kl);
    }


    public synchronized void addMouseListener(MouseListener ml) {
        this.mouseListeners.add(ml);
    }

    public synchronized void removeMouseListener(MouseListener kl) {
        this.mouseListeners.remove(kl);
    }


    public synchronized void addMouseMotionListener(MouseMotionListener mml) {
        this.mouseMotionListeners.add(mml);
    }

    public synchronized void removeMouseMotionListener(MouseMotionListener mml) {
        this.mouseMotionListeners.remove(mml);
    }


    public synchronized void addMouseWheelListener(MouseWheelListener mwl) {
        this.mouseWheelListeners.add(mwl);
    }

    public synchronized void removeMouseWheelListener(MouseWheelListener mwl) {
        this.mouseWheelListeners.remove(mwl);
    }

}
