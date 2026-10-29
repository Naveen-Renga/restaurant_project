package MODELS;

import java.util.ArrayList;
import java.util.List;

public class BILL {
    private int billNumber;
    private int tableId;
    private List<ORDERITEM> items;
    private double totalAmount;
    private boolean paid;

    public BILL(int billNumber, int tableId) {
        this.billNumber = billNumber;
        this.tableId = tableId;
        this.items = new ArrayList<>();
        this.totalAmount = 0.0;
        this.paid = false;
    }

    public int getBillNumber() {
        return billNumber;
    }

    public int getTableId() {
        return tableId;
    }

    public List<ORDERITEM> getItems() {
        return items;
    }

    public void addItem(ORDERITEM item) {
        items.add(item);
        totalAmount += item.getSubtotal();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void markPaid() {
        this.paid = true;
    }
}
