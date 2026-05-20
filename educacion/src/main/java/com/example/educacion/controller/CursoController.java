package com.example.educacion.controller;

import com.example.educacion.entity.Curso;
import com.example.educacion.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@CrossOrigin("*")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<Curso>> listar(){
        return ResponseEntity.ok(cursoService.listar());
    }

    @PostMapping
    public ResponseEntity<Curso> guardar(@RequestBody @NonNull Curso curso){
        return ResponseEntity.ok(cursoService.guardar(curso));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> editar(@PathVariable @NonNull Long id,
                                        @RequestBody @NonNull Curso curso){

        Curso cursoDB = cursoService.buscar(id);

        cursoDB.setNombre(curso.getNombre());
        cursoDB.setDescripcion(curso.getDescripcion());
        cursoDB.setCreditos(curso.getCreditos());

        return ResponseEntity.ok(cursoService.guardar(cursoDB));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable @NonNull Long id){

        cursoService.eliminar(id);

        return ResponseEntity.ok("Curso eliminado");
    }
}
