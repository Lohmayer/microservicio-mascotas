        package com.duoc.microservicio_mascotas.repository;

        import com.duoc.microservicio_mascotas.model.Venta;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.stereotype.Repository;

        import java.time.LocalDate;
        import java.util.List;

        @Repository
        public interface VentaRepository
                extends JpaRepository<Venta, Integer> {

        List<Venta> findByFecha(LocalDate fecha);

        List<Venta> findByFechaBetween(
                LocalDate fechaInicio,
                LocalDate fechaFin
        );

        boolean existsByProductoId(Integer productoId);
        }