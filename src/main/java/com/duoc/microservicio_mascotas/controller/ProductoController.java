package com.duoc.microservicio_mascotas.controller;

import com.duoc.microservicio_mascotas.model.Producto;
import com.duoc.microservicio_mascotas.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(
            ProductoService productoService) {

        this.productoService = productoService;
    }

    // Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerProductos() {
        return ResponseEntity.ok(
                productoService.obtenerProductos()
        );
    }

    // Obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(
            @PathVariable Integer id) {

        Optional<Producto> producto =
                productoService.obtenerProductoPorId(id);

        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.get());
        }

        return ResponseEntity.notFound().build();
    }

    // Crear un producto
    @PostMapping
    public ResponseEntity<Producto> crearProducto(
            @Valid @RequestBody Producto producto) {

        Optional<Producto> productoCreado =
                productoService.crear(producto);

        if (productoCreado.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productoCreado.get());
    }

    // Actualizar un producto
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Integer id,
            @Valid @RequestBody Producto producto) {

        if (!productoService.existeProducto(id)) {
            return ResponseEntity.notFound().build();
        }

        if (!productoService.preciosValidos(producto)) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Producto> productoActualizado =
                productoService.actualizar(id, producto);

        if (productoActualizado.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                productoActualizado.get()
        );
    }

    // Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable Integer id) {

        if (!productoService.existeProducto(id)) {
            return ResponseEntity.notFound().build();
        }

        if (productoService.tieneVentas(id)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }

        boolean eliminado =
                productoService.eliminar(id);

        if (eliminado) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.badRequest().build();
    }
}