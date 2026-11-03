package personajes.base;

import java.time.LocalDate;

public class Asesino extends Campeon {

    public Asesino(String nombre, String region, LocalDate fecha, int mana, String descripcion) {
        super(nombre, region, fecha, mana, descripcion);
    }

    @Override
    public String getTipo() {
        return "Asesino";
    }
}
