package com.restaurant.menu.menu_management.Service;

import com.restaurant.menu.menu_management.Domain.Category;
import com.restaurant.menu.menu_management.Repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> fetchAllCategories() {
        return this.categoryRepository.findAll();
    }

    public Category fetchCategoryById(long id) {
        Optional<Category> categoryOptional = this.categoryRepository.findById(id);
        return categoryOptional.orElse(null);
    }

    public Category handleCreateCategory(Category category) {
        return this.categoryRepository.save(category);
    }

    public void handleDeleteCategory(long id) {
        this.categoryRepository.deleteById(id);
    }

    public Category handleUpdateCategory(Category category) {
        Category categoryUpdate = this.fetchCategoryById(category.getId());

        if (categoryUpdate != null) {
            categoryUpdate.setName(category.getName());
            categoryUpdate.setDescription(category.getDescription());

            // update
            categoryUpdate = this.categoryRepository.save(categoryUpdate);
        }

        return categoryUpdate;
    }
}
