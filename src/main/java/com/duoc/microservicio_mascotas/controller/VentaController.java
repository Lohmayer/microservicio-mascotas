        package com.duoc.microservicio_mascotas.controller;

        import com.duoc.microservicio_mascotas.model.Venta;
        import com.duoc.microservicio_mascotas.service.VentaService;
        import jakarta.validation.Valid;
        import org.springframework.format.annotation.DateTimeFormat;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;

        import java.time.LocalDate;
        import java.util.LinkedHashMap;
        import java.util.List;
        import java.util.Map;
        import java.util.Optional;

        @RestController
        @RequestMapping("/ventas")
        public class VentaController {

        private final VentaService ventaService;

        public VentaController(VentaService ventaService) {
                this.ventaService = ventaService;
        }

        // Obtener todas las ventas
        @GetMapping
        public ResponseEntity<List<Venta>> obtenerVentas() {
                return ResponseEntity.ok(
                        ventaService.obtenerVentas()
                );
        }

        // Obtener una venta por ID
        @GetMapping("/{id}")
        public ResponseEntity<Venta> obtenerVentaPorId(
                @PathVariable Integer id) {

                Optional<Venta> venta =
                        ventaService.obtenerVentaPorId(id);

                if (venta.isPresent()) {
                return ResponseEntity.ok(venta.get());
                }

                return ResponseEntity.notFound().build();
        }

        // Obtener ventas de una fecha
        @GetMapping("/fecha/{fecha}")
        public ResponseEntity<List<Venta>> obtenerVentasPorFecha(
                @PathVariable
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate fecha) {

                return ResponseEntity.ok(
                        ventaService.obtenerVentasPorFecha(fecha)
                );
        }

        // Crear una venta
        @PostMapping
        public ResponseEntity<Venta> crearVenta(
                @Valid @RequestBody Venta venta) {

                Optional<Venta> ventaCreada =
                        ventaService.crear(venta);

                if (ventaCreada.isEmpty()) {
                return ResponseEntity.badRequest().build();
                }

                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(ventaCreada.get());
        }

        // Actualizar una venta
        @PutMapping("/{id}")
        public ResponseEntity<Venta> actualizarVenta(
                @PathVariable Integer id,
                @Valid @RequestBody Venta venta) {

                if (ventaService.obtenerVentaPorId(id).isEmpty()) {
                return ResponseEntity.notFound().build();
                }

                Optional<Venta> ventaActualizada =
                        ventaService.actualizar(id, venta);

                if (ventaActualizada.isEmpty()) {
                return ResponseEntity.badRequest().build();
                }

                return ResponseEntity.ok(
                        ventaActualizada.get()
                );
        }

        // Eliminar una venta
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminarVenta(
                @PathVariable Integer id) {

                boolean eliminada =
                        ventaService.eliminar(id);

                if (eliminada) {
                return ResponseEntity.noContent().build();
                }

                return ResponseEntity.notFound().build();
        }

        // Calcular ganancia diaria
        @GetMapping("/ganancias/diaria")
        public ResponseEntity<Map<String, Object>>
                obtenerGananciaDiaria(
                        @RequestParam
                        @DateTimeFormat(
                                iso = DateTimeFormat.ISO.DATE
                        )
                        LocalDate fecha) {

                double ganancia =
                        ventaService.calcularGananciaDiaria(fecha);

                Map<String, Object> respuesta =
                        new LinkedHashMap<>();

                respuesta.put("periodo", "Diario");
                respuesta.put("fecha", fecha);
                respuesta.put("ganancia", ganancia);

                return ResponseEntity.ok(respuesta);
        }

        // Calcular ganancia mensual
        @GetMapping("/ganancias/mensual")
        public ResponseEntity<Map<String, Object>>
                obtenerGananciaMensual(
                        @RequestParam int anio,
                        @RequestParam int mes) {

                if (anio < 1 || mes < 1 || mes > 12) {
                return ResponseEntity.badRequest().build();
                }

                double ganancia =
                        ventaService.calcularGananciaMensual(
                                anio,
                                mes
                        );

                Map<String, Object> respuesta =
                        new LinkedHashMap<>();

                respuesta.put("periodo", "Mensual");
                respuesta.put("anio", anio);
                respuesta.put("mes", mes);
                respuesta.put("ganancia", ganancia);

                return ResponseEntity.ok(respuesta);
        }

        // Calcular ganancia anual
        @GetMapping("/ganancias/anual")
        public ResponseEntity<Map<String, Object>>
                obtenerGananciaAnual(
                        @RequestParam int anio) {

                if (anio < 1) {
                return ResponseEntity.badRequest().build();
                }

                double ganancia =
                        ventaService.calcularGananciaAnual(anio);

                Map<String, Object> respuesta =
                        new LinkedHashMap<>();

                respuesta.put("periodo", "Anual");
                respuesta.put("anio", anio);
                respuesta.put("ganancia", ganancia);

                return ResponseEntity.ok(respuesta);
        }
        }