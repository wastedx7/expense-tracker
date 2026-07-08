package com.micro.expense_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.micro.expense_service.DTO.CategoryRequest;
import com.micro.expense_service.DTO.CategoryResponse;
import com.micro.expense_service.model.Category;
import com.micro.expense_service.repository.CategoryRepository;
import com.micro.expense_service.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public CategoryResponse createCategory(String profileEmail, CategoryRequest request) {
        if (categoryRepository.existsByProfileEmailAndName(profileEmail, request.getName())) {
            throw new IllegalArgumentException("Category with this name already exists");
        }

        Category category = Category.builder()
                .profileEmail(profileEmail)
                .name(request.getName())
                .type(request.getType())
                .icon(request.getIcon())
                .color(request.getColor())
                .build();

        return toResponse(categoryRepository.save(category));
    }

    public List<CategoryResponse> getCategories(String profileEmail) {
        return categoryRepository.findByProfileEmail(profileEmail)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CategoryResponse getCategory(String profileEmail, Long id) {
        Category category = categoryRepository.findByIdAndProfileEmail(id, profileEmail)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        return toResponse(category);
    }

    public CategoryResponse updateCategory(String profileEmail, Long id, CategoryRequest request) {
        Category category = categoryRepository.findByIdAndProfileEmail(id, profileEmail)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        if (!category.getName().equals(request.getName())
                && categoryRepository.existsByProfileEmailAndName(profileEmail, request.getName())) {
            throw new IllegalArgumentException("Category with this name already exists");
        }

        category.setName(request.getName());
        category.setType(request.getType());
        category.setIcon(request.getIcon());
        category.setColor(request.getColor());

        return toResponse(categoryRepository.save(category));
    }

    public void deleteCategory(String profileEmail, Long id) {
        Category category = categoryRepository.findByIdAndProfileEmail(id, profileEmail)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        if (!transactionRepository.findByProfileEmailAndCategoryId(profileEmail, id).isEmpty()) {
            throw new IllegalStateException("Cannot delete category with existing transactions");
        }

        categoryRepository.delete(category);
    }

    private CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .icon(category.getIcon())
                .color(category.getColor())
                .createdAt(category.getCreatedAt())
                .build();
    }
}
