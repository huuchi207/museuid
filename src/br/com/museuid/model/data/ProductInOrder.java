package br.com.museuid.model.data;

import br.com.museuid.dto.Product;

public class ProductInOrder extends Product {
    private int count;
    private String countString;
    private String moreRequirement;

    public ProductInOrder(String id, String productName, String description, Integer price, Integer inStock,
                           int count) {
        super(id, productName, description, price, inStock);
        this.count = count;
        this.countString = String.valueOf(count);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        this.countString = String.valueOf(count);
    }

    public String getCountString() {
        return countString;
    }

    public void setCountString(String countString) {
        this.countString = countString;
    }

    public String getMoreRequirement() {
        return moreRequirement;
    }

    public void setMoreRequirement(String moreRequirement) {
        this.moreRequirement = moreRequirement;
    }
}
