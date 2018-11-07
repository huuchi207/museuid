package br.com.museuid.util;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Utility with predefined animations
 */
public class AnimationUtils {

    private static FadeTransition fade;

    private AnimationUtils() {
    }

    //Create animation fade
    public static void fade(Node no) {
        fade = new FadeTransition(Duration.seconds(1), no);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setCycleCount(1);
        fade.setAutoReverse(true);
        fade.play();
    }

    // create fade animation by setting opacity of origin, destination, and duration of the animation
    public static void fade(Node no, double inicio, double fim, int tempo) {
        fade = new FadeTransition(Duration.seconds(tempo), no);
        fade.setFromValue(inicio);
        fade.setToValue(fim);
        fade.setCycleCount(1);
        fade.setAutoReverse(true);
        fade.play();
    }

    // create fade animation by setting opacity of origin, destination, and duration of the animation
    public static void stopfade() {
        fade.stop();
    }
}
