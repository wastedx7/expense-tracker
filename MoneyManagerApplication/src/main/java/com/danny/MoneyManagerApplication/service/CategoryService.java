package com.danny.MoneyManagerApplication.service;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.danny.MoneyManagerApplication.DTO.CategoryDTO;
import com.danny.MoneyManagerApplication.entity.CategoryEntity;
import com.danny.MoneyManagerApplication.entity.ProfileEntity;
import com.danny.MoneyManagerApplication.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class  CategoryService {
    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;

    private static final List<CategorySeed> DEFAULT_CATEGORIES = List.of(
        new CategorySeed("Salary", "income", ""),
        new CategorySeed("Freelance", "income", ""),
        new CategorySeed("Bonus", "income", ""),
        new CategorySeed("Business", "income", ""),
        new CategorySeed("Investment", "income", ""),
        new CategorySeed("Travel", "expense", ""),
        new CategorySeed("Food", "expense", ""),
        new CategorySeed("Rent", "expense", ""),
        new CategorySeed("Groceries", "expense", ""),
        new CategorySeed("Utilities", "expense", ""),
        new CategorySeed("Shopping", "expense", ""),
        new CategorySeed("Health", "expense", ""),
        new CategorySeed("Entertainment", "expense", ""),
        new CategorySeed("Education", "expense", "")
    );

    public CategoryDTO saveCategory(CategoryDTO categoryDTO){
        ProfileEntity profile = profileService.getCurrentProfile();
        if(categoryRepository.existsByNameAndProfileId(categoryDTO.getName(),profile.getId())){
            throw new RuntimeException("category with this name already exists"); 
        }
        CategoryEntity newCategory = toEntity(categoryDTO, profile);
        newCategory =categoryRepository.save(newCategory);
        return toDTO(newCategory);
    }

    public List<CategoryDTO> getCategoryesByTypeForUser(String type){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> entities = categoryRepository.findByTypeAndProfileId(type,profile.getId());
        return entities.stream().map(this::toDTO).toList();
    }

    public CategoryDTO updateCategory(Long categoryId, CategoryDTO dto){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity existingCategory = categoryRepository.findByIdAndProfileId(categoryId, profile.getId())
        .orElseThrow(() -> new RuntimeException("Category not found or not accessible"));

        existingCategory.setName(dto.getName());
        existingCategory.setIcon(dto.getIcon());
        existingCategory.setType(dto.getType());
        existingCategory = categoryRepository.save(existingCategory);
        return toDTO(existingCategory);
    }

    private CategoryEntity toEntity(CategoryDTO categoryDTO , ProfileEntity profile){
        return CategoryEntity.builder()
                .name(categoryDTO.getName())
                .icon(categoryDTO.getIcon())
                .profile(profile)
                .type(categoryDTO.getType())
                .build();
    }

    public List<CategoryDTO> getCategoriesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categories = ensureDefaultCategories(profile);
        return categories.stream().map(this::toDTO).toList();
    }

    private List<CategoryEntity> ensureDefaultCategories(ProfileEntity profile) {
        List<CategoryEntity> existingCategories = categoryRepository.findByProfileId(profile.getId());

        if (existingCategories.isEmpty()) {
            List<CategoryEntity> defaults = DEFAULT_CATEGORIES.stream()
                    .map(seed -> CategoryEntity.builder()
                            .name(seed.name())
                            .type(seed.type())
                            .icon(seed.icon())
                            .profile(profile)
                            .build())
                    .toList();

            return categoryRepository.saveAll(defaults);
        }

        Set<String> existingKeys = existingCategories.stream()
                .map(this::buildCategoryKey)
                .collect(Collectors.toSet());

        List<CategoryEntity> missingDefaults = DEFAULT_CATEGORIES.stream()
                .filter(seed -> !existingKeys.contains(buildCategoryKey(seed.name(), seed.type())))
                .map(seed -> CategoryEntity.builder()
                        .name(seed.name())
                        .type(seed.type())
                        .icon(seed.icon())
                        .profile(profile)
                        .build())
                .toList();

        if (!missingDefaults.isEmpty()) {
            categoryRepository.saveAll(missingDefaults);
            return categoryRepository.findByProfileId(profile.getId());
        }

        return existingCategories;
    }

    private String buildCategoryKey(CategoryEntity category) {
        return buildCategoryKey(category.getName(), category.getType());
    }

    private String buildCategoryKey(String name, String type) {
        return (name == null ? "" : name.trim().toLowerCase(Locale.ROOT)) + "::"
                + (type == null ? "" : type.trim().toLowerCase(Locale.ROOT));
    }

    private CategoryDTO toDTO(CategoryEntity entity){  
        return CategoryDTO.builder()
        .id(entity.getId())
        .profileId(entity.getProfile() != null ? entity.getProfile().getId():null)
        .name(entity.getName())
        .icon(entity.getIcon())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .type(entity.getType())
        .build();
    }

    private record CategorySeed(String name, String type, String icon) {
    }
}
