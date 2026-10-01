package com.duoc.microservicio_mascotas.repository;

import com.duoc.microservicio_mascotas.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository
        extends JpaRepository<Producto, Integer> {
}