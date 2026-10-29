package REPO;

import MODELS.FOOD;
import java.util.ArrayList;
import java.util.List;

public class food_repository {
    private final List<FOOD> menu = new ArrayList<>();

    public food_repository() {
        // Sample food items
        menu.add(new FOOD(1, "Idli",      30.0));
        menu.add(new FOOD(2, "Dosa",      50.0));
        menu.add(new FOOD(3, "Meals",     80.0));
        menu.add(new FOOD(4, "Fried Rice",70.0));
    }

    // Return the full food list
    public List<FOOD> getMenu() {
        return menu;
    }

    // Find a food item by its ID; returns null if not found
    public FOOD findById(int id) {
        for (FOOD f : menu) {
            if (f.getId() == id) {
                return f;
            }
        }
        return null;
    }
}
