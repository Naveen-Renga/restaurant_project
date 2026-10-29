import CLI.TABLECLI;
import SERVICE.billing_service;
import SERVICE.table_service;

public class Main {
    public static void main(String[] args) {
        table_service tableServ = new table_service();
        billing_service billingServ = new billing_service(tableServ);
        TABLECLI cli = new TABLECLI(tableServ, billingServ);
        cli.start();
    }
}