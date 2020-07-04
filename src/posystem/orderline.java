package posystem;

public class orderline {
    private int orderID;
    private String product;
    private int productQuantity;

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuatity) {
        this.productQuantity = productQuatity;
    }
}
