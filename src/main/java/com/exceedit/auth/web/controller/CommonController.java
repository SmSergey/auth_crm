package com.exceedit.auth.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface CommonController<E, R> {

    @PostMapping
    ResponseEntity<E> save(@RequestBody E entity);

    @PostMapping("/all")
    ResponseEntity<List<E>> saveAll(@RequestBody List<E> entities);

    @PutMapping
    ResponseEntity<E> update(@RequestBody E entity);

    @GetMapping
    ResponseEntity<E> get(@RequestParam Long id);

    @GetMapping("/all")
    ResponseEntity<List<E>> getAll();

    @DeleteMapping
    Boolean delete(@RequestParam Long id);

    @DeleteMapping("/all")
    Boolean deleteAll();
}
