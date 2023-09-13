package ru.practicum.main.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryPublicController {

    private final CategoryService categoryService;

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getById(@PathVariable Long catId) {
        log.info("Получен GET запрос по эндпоинту /categories на получение Category с ID {}.", catId);
        return new ResponseEntity<>(categoryService.getById(catId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll(
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        log.info("Получен GET запрос по эндпоинту /categories на получение всех Categoryс параметрами from={}, size={}.", from, size);
        return new ResponseEntity<>(categoryService.getAll(from, size), HttpStatus.OK);
    }

}
