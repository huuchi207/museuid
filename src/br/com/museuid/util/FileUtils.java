package br.com.museuid.util;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileUtils {
  public static File configureImageFileChooser(final FileChooser fileChooser, Stage stage) {
    fileChooser.setTitle("View Pictures");
    fileChooser.setInitialDirectory(
      new File(System.getProperty("user.home"))
    );
    fileChooser.getExtensionFilters().addAll(
      new FileChooser.ExtensionFilter("All Images", "*.*"),
      new FileChooser.ExtensionFilter("JPG", "*.jpg"),
      new FileChooser.ExtensionFilter("PNG", "*.png")
    );
    return fileChooser.showOpenDialog(stage);
  }

}
