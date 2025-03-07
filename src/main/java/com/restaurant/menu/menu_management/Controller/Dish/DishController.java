package com.restaurant.menu.menu_management.Controller.Dish;

import com.restaurant.menu.menu_management.Domain.Dish;
import com.restaurant.menu.menu_management.Service.Dish.DishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DishController {
    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("dishes")
    public ResponseEntity<List<Dish>> getAllDishes() {
        return ResponseEntity.ok(this.dishService.fetchAllDishes());
    }

    @GetMapping("dishes/category/{categoryId}")
    public ResponseEntity<List<Dish>> getDishesByCategory(@PathVariable("categoryId") long categoryId) {
        return ResponseEntity.ok(this.dishService.fetchDishesByCategory(categoryId));
    }

    @GetMapping("dishes/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable("id") long id) {
        return ResponseEntity.ok(this.dishService.fetchDishById(id));
    }

    @PostMapping("dishes/create")
    public ResponseEntity<Dish> createNewDish(@RequestBody Dish dish) {
        return ResponseEntity.ok(this.dishService.handleCreateDish(dish));
    }

    @DeleteMapping("dishes/{id}")
    public ResponseEntity<String> deleteDish(@PathVariable("id") long id) {
        this.dishService.handleDeleteDish(id);
        return ResponseEntity.ok("Dish Deleted");
    }

    @PutMapping("dishes")
    public ResponseEntity<Dish> updateDish(@RequestBody Dish dish) {
        Dish dishUpdate = this.dishService.handleUpdateDish(dish);
        return ResponseEntity.ok().body(dishUpdate);
    }
}
