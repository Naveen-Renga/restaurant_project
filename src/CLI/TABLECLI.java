package CLI;

import MODELS.BILL;
import MODELS.FOOD;
import MODELS.ORDERITEM;
import MODELS.TABLE;
import SERVICE.billing_service;
import SERVICE.table_service;

import java.util.List;
import java.util.Scanner;

public class TABLECLI {
    private final table_service service;
    private final billing_service billingService;
    Scanner s = new Scanner(System.in);

    public TABLECLI(table_service service, billing_service billingService) {
        this.service = service;
        this.billingService = billingService;
    }

    public void start() {
        int choice;
        do {
            System.out.println("\n--- Table Management System ---");
            System.out.println("1. Show All Tables");
            System.out.println("2. Create Table");
            System.out.println("3. Delete Table");
            System.out.println("4. Book Table");
            System.out.println("5. Show Food Menu");
            System.out.println("6. Add Order");
            System.out.println("7. Generate Bill");
            System.out.println("8. Pay Bill");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = s.nextInt();
            switch (choice) {
                case 1: showAllTables();  break;
                case 2: createTable();    break;
                case 3: deleteTable();    break;
                case 4: bookTable();      break;
                case 5: showFoodMenu();   break;
                case 6: addOrder();       break;
                case 7: generateBill();   break;
                case 8: payBill();        break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    // ── 1. Show All Tables ────────────────────────────────────────────────────
    private void showAllTables() {
        List<TABLE> tables = service.gatAll();
        if (tables.isEmpty()) {
            System.out.println("No tables available.");
            return;
        }
        System.out.println("\nID\tCapacity\tStatus");
        System.out.println("--\t--------\t------");
        for (TABLE t : tables) {
            System.out.printf("%d\t%d\t\t%s%n", t.getid(), t.getCapasity(), t.getStatus());
        }
    }

    // ── 2. Create Table ───────────────────────────────────────────────────────
    private void createTable() {
        System.out.print("Enter Table ID (positive integer): ");
        int id = s.nextInt();
        System.out.print("Enter Table Capacity (positive integer): ");
        int capacity = s.nextInt();
        if (id <= 0 || capacity <= 0) {
            System.out.println("Error: ID and capacity must be positive numbers.");
            return;
        }
        if (service.createTable(id, capacity)) {
            System.out.println("Table created successfully.");
        } else {
            System.out.println("Error: Table ID already exists.");
        }
    }

    // ── 3. Delete Table ───────────────────────────────────────────────────────
    private void deleteTable() {
        System.out.print("Enter Table ID to delete: ");
        int id = s.nextInt();
        if (id <= 0) {
            System.out.println("Error: ID must be a positive number.");
            return;
        }
        if (service.deleteTable(id)) {
            System.out.println("Table deleted successfully.");
        } else {
            System.out.println("Error: Table with given ID not found.");
        }
    }

    // ── 4. Book Table ─────────────────────────────────────────────────────────
    private void bookTable() {
        System.out.print("Enter Table ID to book: ");
        int id = s.nextInt();
        if (id <= 0) {
            System.out.println("Error: ID must be a positive number.");
            return;
        }
        int result = service.bookTable(id);
        if      (result == 1) System.out.println("Table booked successfully. Status set to: occupy");
        else if (result == 2) System.out.println("Error: Table is already occupied.");
        else                  System.out.println("Error: Table with given ID not found.");
    }

    // ── 5. Show Food Menu ─────────────────────────────────────────────────────
    private void showFoodMenu() {
        List<FOOD> menu = billingService.getFoodMenu();
        System.out.println("\nID\tItem\t\tPrice (Rs.)");
        System.out.println("--\t----\t\t-----------");
        for (FOOD f : menu) {
            System.out.printf("%d\t%-15s%.2f%n", f.getId(), f.getName(), f.getPrice());
        }
    }

    // ── 6. Add Order ──────────────────────────────────────────────────────────
    private void addOrder() {
        System.out.print("Enter Table ID: ");
        int tableId = s.nextInt();

        // Keep adding items until the user says no
        boolean addMore = true;
        while (addMore) {
            showFoodMenu();
            System.out.print("Enter Food ID: ");
            int foodId = s.nextInt();
            System.out.print("Enter Quantity: ");
            int qty = s.nextInt();

            int result = billingService.addOrder(tableId, foodId, qty);
            switch (result) {
                case 1: System.out.println("Item added to order."); break;
                case 0: System.out.println("Error: Table not found."); return;
                case 2: System.out.println("Error: Table is not occupied. Book the table first."); return;
                case 3: System.out.println("Error: Food ID not found."); break;
                case 4: System.out.println("Error: Quantity must be a positive number."); break;
            }

            System.out.print("Add more items? (1 = Yes / 0 = No): ");
            addMore = (s.nextInt() == 1);
        }
        System.out.println("Order recorded successfully.");
    }

    // ── 7. Generate Bill ──────────────────────────────────────────────────────
    private void generateBill() {
        System.out.print("Enter Table ID to generate bill: ");
        int tableId = s.nextInt();

        BILL bill = billingService.getBillForTable(tableId);
        if (bill == null) {
            System.out.println("Error: No active (unpaid) bill found for Table " + tableId + ".");
            return;
        }
        printBill(bill);
    }

    // ── 8. Pay Bill ───────────────────────────────────────────────────────────
    private void payBill() {
        System.out.print("Enter Bill Number to pay: ");
        int billNo = s.nextInt();

        int result = billingService.payBill(billNo);
        switch (result) {
            case 1:
                System.out.println("Payment successful! Table has been released (status: free).");
                break;
            case 0:
                System.out.println("Error: Bill number not found.");
                break;
            case 2:
                System.out.println("Error: Bill is already paid.");
                break;
            case 3:
                System.out.println("Error: Bill has no items. Add an order first.");
                break;
        }
    }

    // ── Helper: Print formatted bill ─────────────────────────────────────────
    private void printBill(BILL bill) {
        System.out.println("\n========================================");
        System.out.println("          RESTAURANT BILL               ");
        System.out.println("========================================");
        System.out.printf("Bill No  : %d%n", bill.getBillNumber());
        System.out.printf("Table ID : %d%n", bill.getTableId());
        System.out.printf("Status   : %s%n", bill.isPaid() ? "PAID" : "UNPAID");
        System.out.println("----------------------------------------");
        System.out.printf("%-15s %6s %8s %10s%n", "Item", "Price", "Qty", "Subtotal");
        System.out.println("----------------------------------------");
        for (ORDERITEM item : bill.getItems()) {
            System.out.printf("%-15s %6.2f %8d %10.2f%n",
                    item.getFood().getName(),
                    item.getFood().getPrice(),
                    item.getQuantity(),
                    item.getSubtotal());
        }
        System.out.println("----------------------------------------");
        System.out.printf("%-30s %10.2f%n", "GRAND TOTAL (Rs.)", bill.getTotalAmount());
        System.out.println("========================================\n");
    }
}
