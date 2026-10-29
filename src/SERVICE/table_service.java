
package SERVICE;
import MODELS.TABLE;
import REPO.table_repository;
import java.util.List;

public class table_service {
        private final table_repository repository;
        public table_service()
        {
            repository=new table_repository();
        }
        public List<TABLE>gatAll()
        {
            return repository.show();
        }

    public boolean createTable(int id, int capacity) {
        if (id <= 0 || capacity <= 0) {
            return false;
        }
        TABLE newTable = new TABLE(id, capacity);
        return repository.addTable(newTable);
    }

    public boolean deleteTable(int id) {
        if (id <= 0) {
            return false;
        }
        return repository.deleteTable(id);
    }

    // Book a table. Returns 0=not found, 1=success, 2=already occupied.
    public int bookTable(int id) {
        if (id <= 0) {
            return 0;
        }
        return repository.bookTable(id);
    }
}


