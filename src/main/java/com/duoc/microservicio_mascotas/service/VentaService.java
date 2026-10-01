        package com.duoc.microservicio_mascotas.service;

        import com.duoc.microservicio_mascotas.model.Producto;
        import com.duoc.microservicio_mascotas.model.Venta;
        import com.duoc.microservicio_mascotas.repository.ProductoRepository;
        import com.duoc.microservicio_mascotas.repository.VentaRepository;
        import org.springframework.stereotype.Service;
        import org.springframework.transaction.annotation.Transactional;

        import java.time.LocalDate;
        import java.time.YearMonth;
        import java.util.List;
        import java.util.Optional;

        @Service
        public class VentaService {

        private final VentaRepository ventaRepository;
        private final ProductoRepository productoRepository;

        public VentaService(
                VentaRepository ventaRepository,
                ProductoRepository productoRepository) {

                this.ventaRepository = ventaRepository;
                this.productoRepository = productoRepository;
        }

        // Obtener todas las ventas
        public List<Venta> obtenerVentas() {
                return ventaRepository.findAll();
        }

        // Obtener una venta por ID
        public Optional<Venta> obtenerVentaPorId(Integer id) {
                return ventaRepository.findById(id);
        }

        // Obtener ventas de una fecha
        public List<Venta> obtenerVentasPorFecha(LocalDate fecha) {
                return ventaRepository.findByFecha(fecha);
        }

        // Crear una venta y descontar stock
        @Transactional
        public Optional<Venta> crear(Venta venta) {

                Optional<Producto> productoEncontrado =
                        productoRepository.findById(
                                venta.getProductoId()
                        );

                if (productoEncontrado.isEmpty()) {
                return Optional.empty();
                }

                Producto producto = productoEncontrado.get();

                if (producto.getStock() < venta.getCantidad()) {
                return Optional.empty();
                }

                producto.setStock(
                        producto.getStock() - venta.getCantidad()
                );

                productoRepository.save(producto);

                venta.setId(null);

                Venta ventaGuardada =
                        ventaRepository.save(venta);

                return Optional.of(ventaGuardada);
        }

        // Actualizar una venta y ajustar el stock
        @Transactional
        public Optional<Venta> actualizar(
                Integer id,
                Venta datosNuevos) {

                Optional<Venta> ventaEncontrada =
                        ventaRepository.findById(id);

                if (ventaEncontrada.isEmpty()) {
                return Optional.empty();
                }

                Optional<Producto> productoNuevoEncontrado =
                        productoRepository.findById(
                                datosNuevos.getProductoId()
                        );

                if (productoNuevoEncontrado.isEmpty()) {
                return Optional.empty();
                }

                Venta ventaExistente = ventaEncontrada.get();

                Optional<Producto> productoAnteriorEncontrado =
                        productoRepository.findById(
                                ventaExistente.getProductoId()
                        );

                if (productoAnteriorEncontrado.isEmpty()) {
                return Optional.empty();
                }

                Producto productoAnterior =
                        productoAnteriorEncontrado.get();

                Producto productoNuevo =
                        productoNuevoEncontrado.get();

                boolean mismoProducto =
                        productoAnterior.getId().equals(
                                productoNuevo.getId()
                        );

                if (mismoProducto) {

                int stockDisponible =
                        productoAnterior.getStock()
                                + ventaExistente.getCantidad();

                if (stockDisponible < datosNuevos.getCantidad()) {
                        return Optional.empty();
                }

                productoAnterior.setStock(
                        stockDisponible
                                - datosNuevos.getCantidad()
                );

                productoRepository.save(productoAnterior);

                } else {

                if (productoNuevo.getStock()
                        < datosNuevos.getCantidad()) {

                        return Optional.empty();
                }

                // Devolver al producto anterior la cantidad original
                productoAnterior.setStock(
                        productoAnterior.getStock()
                                + ventaExistente.getCantidad()
                );

                // Descontar la cantidad del producto nuevo
                productoNuevo.setStock(
                        productoNuevo.getStock()
                                - datosNuevos.getCantidad()
                );

                productoRepository.save(productoAnterior);
                productoRepository.save(productoNuevo);
                }

                ventaExistente.setProductoId(
                        datosNuevos.getProductoId()
                );
                ventaExistente.setCantidad(
                        datosNuevos.getCantidad()
                );
                ventaExistente.setFecha(
                        datosNuevos.getFecha()
                );

                Venta ventaGuardada =
                        ventaRepository.save(ventaExistente);

                return Optional.of(ventaGuardada);
        }

        // Eliminar una venta y devolver el stock
        @Transactional
        public boolean eliminar(Integer id) {

                Optional<Venta> ventaEncontrada =
                        ventaRepository.findById(id);

                if (ventaEncontrada.isEmpty()) {
                return false;
                }

                Venta venta = ventaEncontrada.get();

                Optional<Producto> productoEncontrado =
                        productoRepository.findById(
                                venta.getProductoId()
                        );

                if (productoEncontrado.isPresent()) {

                Producto producto = productoEncontrado.get();

                producto.setStock(
                        producto.getStock()
                                + venta.getCantidad()
                );

                productoRepository.save(producto);
                }

                ventaRepository.deleteById(id);
                return true;
        }

        // Ganancia de una lista de ventas
        private double calcularGanancia(
                List<Venta> ventas) {

                double gananciaTotal = 0.0;

                for (Venta venta : ventas) {

                Optional<Producto> productoEncontrado =
                        productoRepository.findById(
                                venta.getProductoId()
                        );

                if (productoEncontrado.isPresent()) {

                        Producto producto =
                                productoEncontrado.get();

                        double gananciaUnitaria =
                                producto.getPrecioVenta()
                                        - producto.getPrecioCompra();

                        gananciaTotal +=
                                gananciaUnitaria
                                        * venta.getCantidad();
                }
                }

                return Math.round(gananciaTotal * 100.0) / 100.0;
        }

        // Ganancia diaria
        public double calcularGananciaDiaria(
                LocalDate fecha) {

                List<Venta> ventas =
                        ventaRepository.findByFecha(fecha);

                return calcularGanancia(ventas);
        }

        // Ganancia mensual
        public double calcularGananciaMensual(
                int anio,
                int mes) {

                YearMonth periodo = YearMonth.of(anio, mes);

                LocalDate inicio = periodo.atDay(1);
                LocalDate fin = periodo.atEndOfMonth();

                List<Venta> ventas =
                        ventaRepository.findByFechaBetween(
                                inicio,
                                fin
                        );

                return calcularGanancia(ventas);
        }

        // Ganancia anual
        public double calcularGananciaAnual(int anio) {

                LocalDate inicio = LocalDate.of(anio, 1, 1);
                LocalDate fin = LocalDate.of(anio, 12, 31);

                List<Venta> ventas =
                        ventaRepository.findByFechaBetween(
                                inicio,
                                fin
                        );

                return calcularGanancia(ventas);
        }
        }