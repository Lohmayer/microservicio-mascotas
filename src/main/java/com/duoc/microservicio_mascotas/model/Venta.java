package com.duoc.microservicio_mascotas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Entity
@Table(name = "VENTAS")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @NotNull(message = "El ID del producto es obligatorio")
    @Positive(message = "El ID del producto debe ser mayor que cero")
    @Column(name = "PRODUCTO_ID", nullable = false)
    private Integer productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor que cero")
    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(
            message = "La fecha de venta no puede ser futura"
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "FECHA_VENTA", nullable = false)
    private LocalDate fecha;

    public Venta() {
    }

    public Venta(
            Integer id,
            Integer productoId,
            Integer cantidad,
            LocalDate fecha) {

        this.id = id;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    // Constructor compatible con el controlador original
    public Venta(
            int id,
            int productoId,
            int cantidad,
            LocalDate fecha) {

        this.id = id;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}