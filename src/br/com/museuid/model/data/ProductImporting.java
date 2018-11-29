package br.com.museuid.model.data;


import br.com.museuid.dto.Product;
import javafx.scene.control.TextField;

public class ProductImporting extends Product {
  public ProductImporting(String id, String productName, String description, Integer price, Integer inStock) {
    super(id, productName, description, price, inStock);
    numberToImport = 0;
    tfNumberToImport = new TextField();
    tfNumberToImport.setMaxSize(40, 30);
    tfNumberToImport.setText("0");
  }
  private int numberToImport;
  private TextField tfNumberToImport;

  public int getNumberToImport() {
    return numberToImport;
  }

  public void setNumberToImport(int numberToImport) {
    this.numberToImport = numberToImport;
  }

  public TextField getTfNumberToImport() {
    return tfNumberToImport;
  }

  public void setTfNumberToImport(TextField tfNumberToImport) {
    this.tfNumberToImport = tfNumberToImport;
  }
}
