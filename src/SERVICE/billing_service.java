package SERVICE;

import MODELS.BILL;
import MODELS.FOOD;
import MODELS.ORDERITEM;
import MODELS.TABLE;
import REPO.billing_repository;
import REPO.food_repository;

import java.util.List;

public class billing_service {

    private final food_repository foodRepo;
    private final billing_repository billingRepo;
    private final table_service tableService;  // reuse existing table service

    public billing_service(table_service tableService) {
        this.tableService = tableService;
        this.foodRepo = new food_repository();
        this.billingRepo = new billing_repository();
    }

    // --- FOOD MENU ---

    public List<FOOD> getFoodMenu() {
        return foodRepo.getMenu();
    }

    public FOOD findFoodById(int id) {
        return foodRepo.findById(id);
    }

    // --- ORDERS ---

    /**
     * Add an order to the existing unpaid bill of a table,
     * or create a new bill if none exists.
     *
     * Returns:
     *   0 = table not found
     *   1 = success
     *   2 = table not occupied (cannot order)
     *   3 = food ID not found
     *   4 = invalid quantity
     */
    public int addOrder(int tableId, int foodId, int quantity) {
        // Validate quantity
        if (quantity <= 0) {
            return 4;
        }

        // Check table exists and is occupied
        TABLE table = findTableById(tableId);
        if (table == null) {
            return 0;
        }
        if (table.getStatus() != TABLE.availability.occupy) {
            return 2;
        }

        // Find food item
        FOOD food = foodRepo.findById(foodId);
        if (food == null) {
            return 3;
        }

        // Get or create the bill for this table
        BILL bill = billingRepo.findUnpaidBillByTable(tableId);
        if (bill == null) {
            int billNo = billingRepo.generateBillNumber();
            bill = new BILL(billNo, tableId);
            billingRepo.saveBill(bill);
        }

        // Add order item
        bill.addItem(new ORDERITEM(food, quantity));
        return 1;
    }

    // --- BILLING ---

    /**
     * Generate (retrieve) the current unpaid bill for a table.
     * Returns null if no unpaid bill exists for that table.
     */
    public BILL getBillForTable(int tableId) {
        return billingRepo.findUnpaidBillByTable(tableId);
    }

    // --- PAYMENT ---

    /**
     * Pay a bill by bill number.
     * Returns:
     *   0 = bill not found
     *   1 = success (table freed)
     *   2 = already paid
     *   3 = bill has no items
     */
    public int payBill(int billNumber) {
        BILL bill = billingRepo.findByBillNumber(billNumber);
        if (bill == null) {
            return 0;
        }
        if (bill.isPaid()) {
            return 2;
        }
        if (bill.getItems().isEmpty()) {
            return 3;
        }

        // Mark bill as paid
        bill.markPaid();

        // Release the table using existing freenow() logic via table_repository
        releaseTable(bill.getTableId());

        return 1;
    }

    // --- HELPER: find TABLE object by ID ---
    private TABLE findTableById(int tableId) {
        for (TABLE t : tableService.gatAll()) {
            if (t.getid() == tableId) {
                return t;
            }
        }
        return null;
    }

    // --- HELPER: release table (reuse TABLE.freenow()) ---
    private void releaseTable(int tableId) {
        for (TABLE t : tableService.gatAll()) {
            if (t.getid() == tableId) {
                t.freenow();   // reuses existing TABLE.freenow()
                return;
            }
        }
    }
}
