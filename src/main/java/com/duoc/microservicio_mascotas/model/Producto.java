package com.duoc.microservicio_mascotas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "PRODUCTOS")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(
            max = 120,
            message = "El nombre no puede superar los 120 caracteres"
    )
    @Column(name = "NOMBRE", nullable = false, length = 120)
    private String nombre;

    @NotBlank(message = "El tipo de animal es obligatorio")
    @Size(
            max = 80,
            message = "El tipo de animal no puede superar los 80 caracteres"
    )
    @Column(
            name = "TIPO_ANIMAL",
            nullable = false,
            length = 80
    )
    private String tipoAnimal;

    @NotNull(message = "El precio de compra es obligatorio")
    @DecimalMin(
            value = "0.01",
            message = "El precio de compra debe ser mayor que cero"
    )
    @Column(name = "PRECIO_COMPRA", nullable = false)
    private Double precioCompra;

    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(
            value = "0.01",
            message = "El precio de venta debe ser mayor que cero"
    )
    @Column(name = "PRECIO_VENTA", nullable = false)
    private Double precioVenta;

    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    @Column(name = "STOCK", nullable = false)
    private Integer stock;

    public Producto() {
    }

    public Producto(
            Integer id,
            String nombre,
            String tipoAnimal,
            Double precioCompra,
            Double precioVenta,
            Integer stock) {

        this.id = id;
        this.nombre = nombre;
        this.tipoAnimal = tipoAnimal;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stock = stock;
    }

    // Constructor compatible con el controlador original
    public Producto(
            int id,
            String nombre,
            String tipoAnimal,
            int precioCompra,
            int precioVenta,
            int stock) {

        this.id = id;
        this.nombre = nombre;
        this.tipoAnimal = tipoAnimal;
        this.precioCompra = (double) precioCompra;
        this.precioVenta = (double) precioVenta;
        this.stock = stock;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoAnimal() {
        return tipoAnimal;
    }

    public void setTipoAnimal(String tipoAnimal) {
        this.tipoAnimal = tipoAnimal;
    }

    public Double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}