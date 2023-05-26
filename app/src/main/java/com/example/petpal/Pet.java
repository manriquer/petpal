package com.example.petpal;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Pet implements Serializable {

    private String animal;
    private String nombre;
    private String raza;
    private String peso;



    private String fechaNacimiento;
    private transient Bitmap foto; // Aquí asumimos que la foto es un recurso drawable
    // Constructor, getters y setters


    @Override
    public String toString() {
        return "Pet{" +
                "animal='" + animal + '\'' +
                ", nombre='" + nombre + '\'' +
                ", raza='" + raza + '\'' +
                ", peso='" + peso + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                '}';
    }

    public Pet(String animal, String nombre, String raza, String peso, String fechaNacimiento) {
        this.animal = animal;
        this.nombre = nombre;
        this.raza = raza;
        this.peso = peso;
        this.fechaNacimiento = fechaNacimiento;
    }

    public Pet(String nombre, String raza, String peso, String fechaNacimiento, Bitmap foto) {
        this.nombre = nombre;
        this.raza = raza;
        this.peso = peso;
        this.fechaNacimiento = fechaNacimiento;
        this.foto = foto;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
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

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public Pet() {
    }
}

