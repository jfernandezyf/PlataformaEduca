package com.example.educacion.service;

import com.example.educacion.entity.Curso;
import com.example.educacion.repository.CursoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public Curso guardar(@NonNull Curso curso) {
        return cursoRepository.save(curso);
    }

    public List<Curso> listar() {
        return cursoRepository.findAll();
    }

    public Curso buscar(@NonNull Long id) {
        return cursoRepository.findById(id).orElseThrow();
    }

    public void eliminar(@NonNull Long id) {
        cursoRepository.deleteById(id);
    }
}