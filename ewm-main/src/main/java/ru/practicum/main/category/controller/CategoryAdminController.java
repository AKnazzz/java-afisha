package ru.practicum.main.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.dto.NewCategoryDto;
import ru.practicum.main.category.service.CategoryService;

import javax.validation.Valid;

@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("Получен POST запрос по эндпоинту /admin/categories на создание ногового CategoryDto {}.",
                newCategoryDto);
        return new ResponseEntity<>(categoryService.create(newCategoryDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> update(@RequestBody @Valid NewCategoryDto newCategoryDto,
            @PathVariable Long catId) {
        log.info("Получен PATCH запрос по эндпоинту /admin/categories/{} на обновление Category с ID {}.", catId,
                catId);
        return new ResponseEntity<>(categoryService.update(newCategoryDto, catId), HttpStatus.OK);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<?> delete(@PathVariable Long catId) {
        categoryService.delete(catId);
        log.info("Получен DELETE запрос по эндпоинту /admin/categories/{} на удаление Category с ID {}.", catId,
                catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

