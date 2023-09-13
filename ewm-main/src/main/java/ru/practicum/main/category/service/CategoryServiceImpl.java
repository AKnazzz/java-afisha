package ru.practicum.main.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.dto.NewCategoryDto;
import ru.practicum.main.category.mapper.CategoryMapper;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.category.repository.CategoryRepository;
import ru.practicum.main.error.exception.CantDoException;
import ru.practicum.main.error.exception.EntityNotExistException;
import ru.practicum.main.event.repository.EventRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        log.info("Создна новая категория {}.", newCategoryDto);
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(newCategoryDto)));
    }

    @Override
    @Transactional
    public CategoryDto update(NewCategoryDto newCategoryDto, Long catId) {
        Category category = categoryMapper.toCategory(newCategoryDto);
        checkIfCategoryExists(catId);
        category.setId(catId);
        log.info("Категория с ID {} обновлена на параметры {}.", catId, newCategoryDto);
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void delete(Long catId) {
        checkIfCategoryExistsAndGet(catId);
        checkIfEmpty(catId);
        categoryRepository.deleteById(catId);
        log.info("Категория с ID {} удалена", catId);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getById(Long catId) {
        CategoryDto categoryDto = categoryMapper.toCategoryDto(checkIfCategoryExistsAndGet(catId));
        log.info("Категория с ID {} получена", catId);
        return categoryDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAll(Integer from, Integer size) {
        log.info("Получен список всех категорий с параметрами from={}, size={}", from, size);
        return categoryMapper.toCategoryDto(categoryRepository.findAll(PageRequest.of(from / size, size)).toList());
    }

    private void checkIfEmpty(Long catId) {
        if (!eventRepository.findEventsByCategoryId(catId).isEmpty()) {
            throw new CantDoException("Категория не пустая");
        }
    }

    private Category checkIfCategoryExistsAndGet(Long catId) {
        Optional<Category> category = categoryRepository.findById(catId);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw new EntityNotExistException(Category.class, catId);
        }
    }

    private void checkIfCategoryExists(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new EntityNotExistException(Category.class, catId);
        }
    }

}
