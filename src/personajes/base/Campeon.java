package personajes.base;

import java.time.LocalDate;

public abstract class Campeon {
    private String nombre;
    private String region;
    private LocalDate fecha;
    private int mana;
    private String descripcion;

    public Campeon(String nombre, String region, LocalDate fecha, int mana, String descripcion) {
        this.nombre = nombre;
        this.region = region;
        this.fecha = fecha;
        this.mana = mana;
        this.descripcion = descripcion;
    }

    // cada hijo dice cual es
    public abstract String getTipo();

    public String getNombre() {
        return nombre;
    }

    public String getRegion() {
        return region;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getMana() {
        return mana;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String toString() {
        return getTipo() + " - " + nombre;
    }
}
