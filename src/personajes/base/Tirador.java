package personajes.base;

import java.time.LocalDate;

public class Tirador extends Campeon {

    public Tirador(String nombre, String region, LocalDate fecha, int mana, String descripcion) {
        super(nombre, region, fecha, mana, descripcion);
    }

    @Override
    public String getTipo() {
        return "Tirador";
    }
}
