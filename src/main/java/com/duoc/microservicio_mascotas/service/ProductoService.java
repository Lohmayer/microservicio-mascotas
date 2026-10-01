package com.duoc.microservicio_mascotas.service;

import com.duoc.microservicio_mascotas.model.Producto;
import com.duoc.microservicio_mascotas.repository.ProductoRepository;
import com.duoc.microservicio_mascotas.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;

    public ProductoService(
            ProductoRepository productoRepository,
            VentaRepository ventaRepository) {

        this.productoRepository = productoRepository;
        this.ventaRepository = ventaRepository;
    }

    // Obtener todos los productos
    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }

    // Obtener producto por ID
    public Optional<Producto> obtenerProductoPorId(Integer id) {
        return productoRepository.findById(id);
    }

    // Comprobar si existe
    public boolean existeProducto(Integer id) {
        return productoRepository.existsById(id);
    }

    // Comprobar si tiene ventas asociadas
    public boolean tieneVentas(Integer id) {
        return ventaRepository.existsByProductoId(id);
    }

    // Validar precios
    public boolean preciosValidos(Producto producto) {

        if (producto.getPrecioCompra() == null
                || producto.getPrecioVenta() == null) {

            return false;
        }

        return producto.getPrecioVenta()
                > producto.getPrecioCompra();
    }

    // Crear producto
    @Transactional
    public Optional<Producto> crear(Producto producto) {

        if (!preciosValidos(producto)) {
            return Optional.empty();
        }

        producto.setId(null);

        Producto productoGuardado =
                productoRepository.save(producto);

        return Optional.of(productoGuardado);
    }

    // Actualizar producto
    @Transactional
    public Optional<Producto> actualizar(
            Integer id,
            Producto datosNuevos) {

        if (!preciosValidos(datosNuevos)) {
            return Optional.empty();
        }

        Optional<Producto> productoExistente =
                productoRepository.findById(id);

        if (productoExistente.isEmpty()) {
            return Optional.empty();
        }

        Producto producto = productoExistente.get();

        producto.setNombre(datosNuevos.getNombre());
        producto.setTipoAnimal(datosNuevos.getTipoAnimal());
        producto.setPrecioCompra(
                datosNuevos.getPrecioCompra()
        );
        producto.setPrecioVenta(
                datosNuevos.getPrecioVenta()
        );
        producto.setStock(datosNuevos.getStock());

        Producto productoGuardado =
                productoRepository.save(producto);

        return Optional.of(productoGuardado);
    }

    // Eliminar producto si no tiene ventas
    @Transactional
    public boolean eliminar(Integer id) {

        if (!productoRepository.existsById(id)) {
            return false;
        }

        if (ventaRepository.existsByProductoId(id)) {
            return false;
        }

        productoRepository.deleteById(id);
        return true;
    }
}