package com.campus.helpdesk.service;

import com.campus.helpdesk.model.Category;
import com.campus.helpdesk.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.DESC, "categoryId"));
    }

    public List<Category> getApprovedCategories() {
        return categoryRepository.findByStatusOrderByCategoryIdDesc("APPROVED");
    }

    public boolean addCategory(Category category) {
        category.setStatus("PENDING");
        categoryRepository.save(category);
        return true;
    }

    public boolean approveCategory(int categoryId, int slaHours) {
        Optional<Category> opt = categoryRepository.findById(categoryId);
        if (opt.isPresent()) {
            Category cat = opt.get();
            cat.setStatus("APPROVED");
            cat.setSlaHours(slaHours);
            categoryRepository.save(cat);
            return true;
        }
        return false;
    }

    public boolean deleteCategory(int categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
            return true;
        }
        return false;
    }

    public boolean updateCategory(Category category) {
        Optional<Category> opt = categoryRepository.findById(category.getCategoryId());
        if (opt.isPresent()) {
            Category existing = opt.get();
            existing.setCategoryName(category.getCategoryName());
            existing.setDescription(category.getDescription());
            existing.setRiskLevel(category.getRiskLevel());
            categoryRepository.save(existing);
            return true;
        }
        return false;
    }
}