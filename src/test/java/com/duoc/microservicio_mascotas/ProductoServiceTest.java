package com.duoc.microservicio_mascotas;

import com.duoc.microservicio_mascotas.model.Producto;
import com.duoc.microservicio_mascotas.repository.ProductoRepository;
import com.duoc.microservicio_mascotas.repository.VentaRepository;
import com.duoc.microservicio_mascotas.service.ProductoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    // PRUEBA 1 - VALIDACIÓN DE PRECIOS CORRECTOS
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

    // PRUEBA 2 - VALIDACIÓN DE PRECIOS INCORRECTOS
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

    // PRUEBA 3 - CREAR PRODUCTO
    @Test
    void crear_debeGuardarProductoCuandoLosPreciosSonValidos() {

        // Arrange
        Producto producto = new Producto(
                null,
                "Cama para perro",
                "Perro",
                10000.0,
                18000.0,
                5
        );

        Producto productoGuardado = new Producto(
                3,
                "Cama para perro",
                "Perro",
                10000.0,
                18000.0,
                5
        );

        when(productoRepository.save(producto))
                .thenReturn(productoGuardado);

        // Act
        Optional<Producto> resultado =
                productoService.crear(producto);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(3, resultado.get().getId());
        assertEquals("Cama para perro", resultado.get().getNombre());
        assertEquals("Perro", resultado.get().getTipoAnimal());
        assertEquals(10000.0, resultado.get().getPrecioCompra());
        assertEquals(18000.0, resultado.get().getPrecioVenta());
        assertEquals(5, resultado.get().getStock());

        verify(productoRepository).save(producto);
    }

    // PRUEBA 4 - ELIMINAR PRODUCTO
    @Test
    void eliminar_debeEliminarProductoCuandoExisteYNoTieneVentas() {

        // Arrange
        Integer id = 1;

        when(productoRepository.existsById(id))
                .thenReturn(true);

        when(ventaRepository.existsByProductoId(id))
                .thenReturn(false);

        // Act
        boolean resultado = productoService.eliminar(id);

        // Assert
        assertTrue(resultado);

        verify(productoRepository).existsById(id);
        verify(ventaRepository).existsByProductoId(id);
        verify(productoRepository).deleteById(id);
    }
}