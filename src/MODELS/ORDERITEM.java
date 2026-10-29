package MODELS;

public class ORDERITEM {
    private FOOD food;
    private int quantity;

    public ORDERITEM(FOOD food, int quantity) {
        this.food = food;
        this.quantity = quantity;
    }

    public FOOD getFood() {
        return food;
    }

    public int getQuantity() {
        return quantity;
    }

    // subtotal = price x quantity
    public double getSubtotal() {
        return food.getPrice() * quantity;
    }
}
