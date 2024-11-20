package com.zuro.Literalura.repository;

import com.zuro.Literalura.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibrosRepository extends JpaRepository<Libros, Long> {
    Optional<Libros> findByTitulo(String titulo);
}
