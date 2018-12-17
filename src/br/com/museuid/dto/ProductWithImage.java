package br.com.museuid.dto;

import br.com.museuid.model.data.ProductImporting;
import br.com.museuid.model.data.ProductInOrder;
import br.com.museuid.util.Link;
import javafx.scene.image.ImageView;

public class ProductWithImage  extends  Product{
  private ImageView productImage;
  private boolean selected;
  public ProductWithImage(String id, String productName, String description, Integer price, Integer inStock, String imageid, String type) {
    super(id, productName, description, price, inStock, type);
    this.imageid = imageid;
    createImage();
  }

  public ProductWithImage(String productName, String productid) {
    super(productName, productid);
  }

  public ProductWithImage(String productName, String productid, ImageView productImage) {
    super(productName, productid);
    this.productImage = productImage;
  }

  public ImageView getProductImage() {
    return productImage;
  }

  public void setProductImage(ImageView productImage) {
    this.productImage = productImage;
  }

  public void createImage(){
    productImage = new ImageView(Link.getImageLink(imageid));
    productImage.setFitHeight(150);
    productImage.setFitWidth(150);
  }

  public ProductInOrder convertToProductInOrder() {
    return new ProductInOrder(id, productName, description, price, inStock, imageid, productImage, 1, type);
  }

  public ProductImporting convertToProductImporting(){
    return new ProductImporting(id, productName, description, price, inStock, imageid, type);
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }
}
