package REPO;
import MODELS.TABLE;
import java.util.*;
public class table_repository {
    private final List<TABLE>table=new ArrayList<>();
    public table_repository()
    {
        table.add(new TABLE(1,5));
        table.add(new TABLE(2,2));
        table.add(new TABLE(3,7));
    }
    public List<TABLE>show()
    {
        return table;
    }

    public boolean addTable(TABLE newTable) {
        for (TABLE t : table) {
            if (t.getid() == newTable.getid()) {
                return false;
            }
        }
        table.add(newTable);
        return true;
    }
    public boolean deleteTable(int id) {
        Iterator<TABLE> iterator = table.iterator();
        while (iterator.hasNext()) {
            TABLE t = iterator.next();
            if (t.getid() == id) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public int bookTable(int id) {
        for (TABLE t : table) {
            if (t.getid() == id) {
                if (t.getStatus() == TABLE.availability.occupy) {
                    return 2;
                }
                t.booking();
                return 1;
            }
        }
        return 0;
    }

}
