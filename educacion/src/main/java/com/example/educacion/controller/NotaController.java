package com.example.educacion.controller;

import com.example.educacion.entity.Nota;
import com.example.educacion.service.NotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notas")
@CrossOrigin("*")
public class NotaController {

    @Autowired
    private NotaService notaService;

    @GetMapping
    public ResponseEntity<List<Nota>> listar(){
        return ResponseEntity.ok(notaService.listar());
    }

    @PostMapping
    public ResponseEntity<Nota> guardar(@RequestBody @NonNull Nota nota){
        return ResponseEntity.ok(notaService.guardar(nota));
    }

    @GetMapping("/estudiante/{id}")
    public ResponseEntity<List<Nota>> listarPorEstudiante(@PathVariable Long id){
        return ResponseEntity.ok(notaService.listarPorEstudiante(id));
    }
}