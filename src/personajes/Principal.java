package personajes;

import personajes.gui.Controlador;
import personajes.gui.Modelo;
import personajes.gui.Ventana;

public class Principal {
    public static void main(String[] args) {
        Ventana ventana = new Ventana();
        Modelo modelo = new Modelo();
        new Controlador(ventana, modelo);
    }
}
