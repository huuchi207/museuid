package br.com.museuid.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils {
  public static String reduceImg(String imgsrc, String folderDest , String type, int widthdest,
                                       int heightdest) {
    return reduceImg(new File(imgsrc), folderDest, type, widthdest, heightdest);
  }

  public static String reduceImg(File imgSrc, String folderDest ,String type, int widthdest,
                               int heightdest) {
    try {
      if (!imgSrc.exists()) {
        return "";
      }
      Image src = javax.imageio.ImageIO.read(imgSrc);

      BufferedImage tag= new BufferedImage(widthdest, heightdest,
        BufferedImage.TYPE_INT_RGB);

      tag.getGraphics().drawImage(src.getScaledInstance(widthdest, heightdest, Image.SCALE_SMOOTH), 0, 0, null);
      String filePath = folderDest+ "\\"+ imgSrc.getName();
      File outputFile = new File(filePath);
      outputFile.getParentFile().mkdirs();
      FileOutputStream out = new FileOutputStream(filePath);
      ImageIO.write(tag, type, out);
      out.close();
      return filePath;
    } catch (IOException ex) {
      ex.printStackTrace();
      return "";
    }
  }
}
