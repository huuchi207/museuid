package br.com.museuid.util;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * Redimensionamento dos componentes de acordo com seu parente
 * permitindo dimensionar o tamanho de distancia entreo o nó e seu parent
 */
public class Resize {

    private Resize() {
    }

    /**
      * Function to facilitate the resizing of nodes to their size
      * according to the distance of your relative
      *
      * @param in the relative node of the component to be resized
      * @param top distance from top with parent node
      * @param right distance from the right with the parent node
      * @param bottom distance of the fundi with the relative node
      * @param left distance sa left with parent node
      */

    public static void margin(Node no, double top, double right, double bottom, double left) {
        AnchorPane.setTopAnchor(no, top);
        AnchorPane.setRightAnchor(no, right);
        AnchorPane.setBottomAnchor(no, bottom);
        AnchorPane.setLeftAnchor(no, left);
    }

    /**
      * Function to facilitate the resizing of nodes to their size
      * according to the distance of your relative
      *
      * @param in the relative node of the component to be resized
      * @param value values for all nodes
      */
    public static void margin(Node no, double valor) {
        AnchorPane.setTopAnchor(no, valor);
        AnchorPane.setRightAnchor(no, valor);
        AnchorPane.setBottomAnchor(no, valor);
        AnchorPane.setLeftAnchor(no, valor);
    }

    /**
     * Define top, right, and left margin values relative to relative
     */
    public static void margin(Node no, double top, double right, double left) {
        AnchorPane.setTopAnchor(no, top);
        AnchorPane.setRightAnchor(no, right);
        AnchorPane.setLeftAnchor(no, left);
    }

    /**
     * Define right and left margin values relative to relative
     */
    public static void margin(Node no, double right, double left) {
        AnchorPane.setRightAnchor(no, right);
        AnchorPane.setLeftAnchor(no, left);
    }

    /**
     * Set top margin values
     */
    public static void marginTop(Node no, double top) {
        AnchorPane.setTopAnchor(no, top);
    }

    /**
     * Set right margin values
     */
    public static void marginRight(Node no, double right) {
        AnchorPane.setRightAnchor(no, right);
    }

    /**
     * Define values of margin rodape
     */
    public static void marginBottom(Node no, double bottom) {
        AnchorPane.setBottomAnchor(no, bottom);
    }

    /**
     * Set left margin values
     */
    public static void marginLeft(Node no, double left) {
        AnchorPane.setLeftAnchor(no, left);
    }
}
