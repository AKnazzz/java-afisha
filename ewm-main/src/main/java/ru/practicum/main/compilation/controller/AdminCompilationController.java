package ru.practicum.main.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.compilation.dto.CompilationRequestDto;
import ru.practicum.main.compilation.dto.CompilationResponseDto;
import ru.practicum.main.compilation.service.CompilationService;
import ru.practicum.main.validation.group.Create;
import ru.practicum.main.validation.group.Update;

@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationResponseDto> create(
            @RequestBody @Validated(Create.class) CompilationRequestDto compilationRequestDto) {
        log.info("Получен POST запрос по эндпоинту /admin/compilations на создание новой Compilation = {}.",
                compilationRequestDto);
        return new ResponseEntity<>(compilationService.create(compilationRequestDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationResponseDto> update(
            @RequestBody @Validated(Update.class) CompilationRequestDto compilationRequestDto,
            @PathVariable Long compId) {
        log.info("Получен PATCH запрос по эндпоинту /admin/compilations/{} на обновление Compilation с ID {}.", compId,
                compId);
        return new ResponseEntity<>(compilationService.update(compilationRequestDto, compId), HttpStatus.OK);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<?> delete(@PathVariable Long compId) {
        compilationService.delete(compId);
        log.info("Получен DELETE запрос по эндпоинту /admin/compilations/{} на обновление Compilation с ID {}.", compId,
                compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
