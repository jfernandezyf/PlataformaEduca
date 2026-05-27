package com.example.educacion.config;

import com.example.educacion.entity.Rol;
import com.example.educacion.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initRoles(RolRepository rolRepository) {
        return args -> {
            if (rolRepository.findByNombre("USER").isEmpty()) {
                rolRepository.save(new Rol(null, "USER"));
            }
            if (rolRepository.findByNombre("ADMIN").isEmpty()) {
                rolRepository.save(new Rol(null, "ADMIN"));
            }
            if (rolRepository.findByNombre("ESTUDIANTE").isEmpty()) {
                rolRepository.save(new Rol(null, "ESTUDIANTE"));
            }
            if (rolRepository.findByNombre("DOCENTE").isEmpty()) {
                rolRepository.save(new Rol(null, "DOCENTE"));
            }
            if (rolRepository.findByNombre("COORDINADOR").isEmpty()) {
                rolRepository.save(new Rol(null, "COORDINADOR"));
            }
        };
    }
}
