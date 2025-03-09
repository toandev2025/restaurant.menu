package com.restaurant.menu.menu_management.Controller;

import com.restaurant.menu.menu_management.Domain.Category;
import com.restaurant.menu.menu_management.Service.CategoryService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(this.categoryService.fetchAllCategories());
    }

    @GetMapping("categories/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") long id) {
        return ResponseEntity.ok(this.categoryService.fetchCategoryById(id));
    }

    @PostMapping("categories/create")
    public ResponseEntity<Category> createNewCategory(@RequestBody Category category) {
        return ResponseEntity.ok(this.categoryService.handleCreateCategory(category));
    }

    @DeleteMapping("categories/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") long id) {
        this.categoryService.handleDeleteCategory(id);
        return ResponseEntity.ok("Category Deleted");
    }

    @PutMapping("categories")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) {
        Category categoryUpdate = this.categoryService.handleUpdateCategory(category);
        return ResponseEntity.ok().body(categoryUpdate);
    }
}
