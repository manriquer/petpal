package com.example.petpal;

public class Animal {
    private String nombre;
    private String raza;
    private String fechaNacimiento;
    private int foto; // Aquí asumimos que la foto es un recurso drawable
    // Constructor, getters y setters

    public Animal(String nombre, String raza, String fechaNacimiento, int foto) {
        this.nombre = nombre;
        this.raza = raza;
        this.fechaNacimiento = fechaNacimiento;
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public Animal() {
    }
}

