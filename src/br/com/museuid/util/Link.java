package br.com.museuid.util;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Create links to actions that open a page in the browser
 */
public class Link {

    private Link() {
    }

    /**
     * Helps display web addresses on standard desktop
     */
    public static void address(String link) {
        try {
            Desktop.getDesktop().browse(new URI(link));
        } catch (URISyntaxException | IOException ex) {
            Messenger.erro(BundleUtils.getResourceBundle().getString("txt_error"), ex.getMessage());
        }
    }

}
