package REPO;

import MODELS.BILL;
import java.util.ArrayList;
import java.util.List;

public class billing_repository {
    private final List<BILL> bills = new ArrayList<>();
    private int nextBillNumber = 1001; // auto-incrementing bill number

    // Save a new bill
    public void saveBill(BILL bill) {
        bills.add(bill);
    }

    // Find a bill by bill number; returns null if not found
    public BILL findByBillNumber(int billNumber) {
        for (BILL b : bills) {
            if (b.getBillNumber() == billNumber) {
                return b;
            }
        }
        return null;
    }

    // Find an unpaid bill for a given table; returns null if none
    public BILL findUnpaidBillByTable(int tableId) {
        for (BILL b : bills) {
            if (b.getTableId() == tableId && !b.isPaid()) {
                return b;
            }
        }
        return null;
    }

    // Generate and return the next unique bill number
    public int generateBillNumber() {
        return nextBillNumber++;
    }

    // Return all bills (for future use)
    public List<BILL> getAllBills() {
        return bills;
    }
}
