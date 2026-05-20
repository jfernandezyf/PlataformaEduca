package com.example.educacion.service;

import com.example.educacion.entity.Nota;
import com.example.educacion.repository.NotaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class NotaService {

    @Autowired
    private NotaRepository notaRepository;

    public Nota guardar(@NonNull Nota nota) {
        return notaRepository.save(nota);
    }

    public List<Nota> listar() {
        return notaRepository.findAll();
    }

    public List<Nota> listarPorEstudiante(Long id) {
        return notaRepository.buscarPorEstudiante(id);
    }
}