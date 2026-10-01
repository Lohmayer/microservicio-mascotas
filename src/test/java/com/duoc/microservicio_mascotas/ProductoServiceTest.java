package com.duoc.microservicio_mascotas;

import com.duoc.microservicio_mascotas.model.Producto;
import com.duoc.microservicio_mascotas.repository.ProductoRepository;
import com.duoc.microservicio_mascotas.repository.VentaRepository;
import com.duoc.microservicio_mascotas.service.ProductoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ProductoServiceTest {

    private ProductoRepository productoRepository;
    private VentaRepository ventaRepository;
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        productoRepository = Mockito.mock(ProductoRepository.class);
        ventaRepository = Mockito.mock(VentaRepository.class);

        productoService = new ProductoService(
                productoRepository,
                ventaRepository
        );
    }

    @Test
    void preciosValidos_debeRetornarTrueCuandoVentaEsMayorQueCompra() {

        // Arrange
        Producto producto = new Producto(
                1,
                "Alimento para perro",
                "Perro",
                10000.0,
                15000.0,
                20
        );

        // Act
        boolean resultado = productoService.preciosValidos(producto);

        // Assert
        assertTrue(resultado);
    }

    @Test
void preciosValidos_debeRetornarFalseCuandoVentaEsMenorQueCompra() {

    // Arrange
    Producto producto = new Producto(
            2,
            "Juguete para gato",
            "Gato",
            15000.0,
            10000.0,
            10
    );

    // Act
    boolean resultado = productoService.preciosValidos(producto);

    // Assert
    assertFalse(resultado);
}
}