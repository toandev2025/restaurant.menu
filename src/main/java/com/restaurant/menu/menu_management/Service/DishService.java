package com.restaurant.menu.menu_management.Service;

import com.restaurant.menu.menu_management.Domain.Dish;
import com.restaurant.menu.menu_management.Repository.DishRepository;
import com.restaurant.menu.menu_management.Repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishService {
    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository, CategoryRepository categoryRepository) {
        this.dishRepository = dishRepository;
    }

    public List<Dish> fetchAllDishes() {
        return this.dishRepository.findAll();
    }

    public List<Dish> fetchDishesByCategory(Long categoryId) {
        return this.dishRepository.findByCategoryId(categoryId);
    }

    public Dish fetchDishById(long id) {
        Optional<Dish> dishOptional = this.dishRepository.findById(id);
        return dishOptional.orElse(null);
    }

    public Dish handleCreateDish(Dish dish) {
        return this.dishRepository.save(dish);
    }

    public void handleDeleteDish(long id) {
        this.dishRepository.deleteById(id);
    }

    public Dish handleUpdateDish(Dish dish) {
        Dish dishUpdate = this.fetchDishById(dish.getId());

        if (dishUpdate != null) {
            dishUpdate.setName(dish.getName());
            dishUpdate.setDescription(dish.getDescription());
            dishUpdate.setPrice(dish.getPrice());
            dishUpdate.setImageUrl(dish.getImageUrl());
            dishUpdate.setAvailable(dish.getAvailable());
            dishUpdate.setCategory(dish.getCategory());

            // update
            dishUpdate = this.dishRepository.save(dishUpdate);
        }

        return dishUpdate;
    }
}
