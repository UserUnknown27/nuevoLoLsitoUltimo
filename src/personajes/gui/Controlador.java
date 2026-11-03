package personajes.gui;

import personajes.base.Campeon;
import util.Util;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.time.LocalDate;
import java.util.Random;

public class Controlador implements ActionListener, ListSelectionListener {
    private Ventana ventana;
    private Modelo modelo;
    private Random random = new Random();

    // archivo
    private File archivo = new File("campeones.xml");

    public Controlador(Ventana ventana, Modelo modelo) {
        this.ventana = ventana;
        this.modelo = modelo;

        // cargar datos
        if (archivo.exists()) {
            try {
                modelo.importar(archivo);
                actualizar();
            } catch (Exception e) {
                Util.error("No se pudo leer campeones.xml");
            }
        }

        azar();

        ventana.btnNuevo.addActionListener(this);
        ventana.btnModificar.addActionListener(this);
        ventana.btnMostrar.addActionListener(this);
        ventana.btnExportar.addActionListener(this);
        ventana.btnImportar.addActionListener(this);
        ventana.lista.addListSelectionListener(this);

        // cerrar
        ventana.frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int resp = Util.preguntar("Quieres salir?", "Salir");
                if (resp == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    // botones
    public void actionPerformed(ActionEvent e) {
        String boton = e.getActionCommand();
        if (boton.equals("Nuevo")) {
            nuevo();
        } else if (boton.equals("Modificar")) {
            modificar();
        } else if (boton.equals("Mostrar")) {
            mostrar();
        } else if (boton.equals("Exportar")) {
            exportar();
        } else if (boton.equals("Importar")) {
            importar();
        }
    }

    // lista
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        Campeon c = ventana.lista.getSelectedValue();
        if (c != null) {
            ponerDatos(c);
        }
    }

    private void nuevo() {
        String nombre = ventana.txtNombre.getText().trim();
        String manaTexto = ventana.txtMana.getText().trim();
        LocalDate fecha = ventana.fecha.getDate();

        if (nombre.isEmpty() || manaTexto.isEmpty() || fecha == null) {
            Util.error("Faltan datos");
            return;
        }

        int mana;
        try {
            mana = Integer.parseInt(manaTexto);
        } catch (NumberFormatException ex) {
            Util.error("El mana tiene que ser un numero");
            return;
        }

        if (modelo.buscar(nombre) != null) {
            Util.error("Ese campeon ya existe");
            return;
        }

        String region = (String) ventana.comboRegion.getSelectedItem();
        String descripcion = ventana.txtDescripcion.getText().trim();
        modelo.agregar(tipo(), nombre, region, fecha, mana, descripcion);

        guardar();
        actualizar();
        limpiar();
    }

    private void modificar() {
        // comprobar
        int posicion = ventana.lista.getSelectedIndex();
        if (posicion == -1) {
            Util.error("Elige un campeon de la lista");
            return;
        }

        String nombre = ventana.txtNombre.getText().trim();
        String manaTexto = ventana.txtMana.getText().trim();
        LocalDate fecha = ventana.fecha.getDate();

        if (nombre.isEmpty() || manaTexto.isEmpty() || fecha == null) {
            Util.error("Faltan datos");
            return;
        }

        int mana;
        try {
            mana = Integer.parseInt(manaTexto);
        } catch (NumberFormatException ex) {
            Util.error("El mana tiene que ser un numero");
            return;
        }

        // comprobar nombre
        for (int i = 0; i < modelo.getLista().size(); i++) {
            if (i != posicion && modelo.getLista().get(i).getNombre().equalsIgnoreCase(nombre)) {
                Util.error("Ese nombre ya es de otro campeon");
                return;
            }
        }

        String region = (String) ventana.comboRegion.getSelectedItem();
        String descripcion = ventana.txtDescripcion.getText().trim();
        modelo.modificar(posicion, tipo(), nombre, region, fecha, mana, descripcion);

        guardar();
        actualizar();
        // seleccionar
        ventana.lista.setSelectedIndex(posicion);
    }

    private void mostrar() {
        String nombre = ventana.txtNombre.getText().trim();
        Campeon c = modelo.buscar(nombre);

        if (c == null) {
            Util.error("No existe ese campeon");
            return;
        }

        ponerDatos(c);
        Util.info(c.getNombre(),
                "Tipo: " + c.getTipo()
                        + "\nRegion: " + c.getRegion()
                        + "\nFecha: " + c.getFecha()
                        + "\nMana: " + c.getMana()
                        + "\n" + c.getDescripcion());
    }

    private void exportar() {
        JFileChooser fichero = Util.elegirFichero();
        if (fichero.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File f = fichero.getSelectedFile();
            // extension
            if (!f.getName().endsWith(".xml")) {
                f = new File(f.getPath() + ".xml");
            }
            try {
                modelo.exportar(f);
            } catch (Exception ex) {
                Util.error("Error al exportar");
            }
        }
    }

    private void importar() {
        JFileChooser fichero = Util.elegirFichero();
        if (fichero.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                modelo.importar(fichero.getSelectedFile());
                actualizar();
            } catch (Exception ex) {
                Util.error("Error al importar");
            }
        }
    }

    // guardar
    private void guardar() {
        try {
            modelo.exportar(archivo);
        } catch (Exception ex) {
            Util.error("No se pudo guardar en el xml");
        }
    }

    // tipo
    private String tipo() {
        if (ventana.radioMago.isSelected()) {
            return "Mago";
        } else if (ventana.radioLuchador.isSelected()) {
            return "Luchador";
        } else if (ventana.radioAsesino.isSelected()) {
            return "Asesino";
        } else if (ventana.radioTirador.isSelected()) {
            return "Tirador";
        } else if (ventana.radioSoporte.isSelected()) {
            return "Soporte";
        } else {
            return "Tanque";
        }
    }

    // poner tipo
    private void ponerTipo(String tipo) {
        if (tipo.equals("Mago")) {
            ventana.radioMago.setSelected(true);
        } else if (tipo.equals("Luchador")) {
            ventana.radioLuchador.setSelected(true);
        } else if (tipo.equals("Asesino")) {
            ventana.radioAsesino.setSelected(true);
        } else if (tipo.equals("Tirador")) {
            ventana.radioTirador.setSelected(true);
        } else if (tipo.equals("Soporte")) {
            ventana.radioSoporte.setSelected(true);
        } else {
            ventana.radioTanque.setSelected(true);
        }
    }

    private void ponerDatos(Campeon c) {
        ponerTipo(c.getTipo());
        ventana.txtNombre.setText(c.getNombre());
        ventana.comboRegion.setSelectedItem(c.getRegion());
        ventana.fecha.setDate(c.getFecha());
        ventana.txtMana.setText(String.valueOf(c.getMana()));
        ventana.txtDescripcion.setText(c.getDescripcion());
    }

    // datos al azar
    private void azar() {
        LocalDate dia = LocalDate.of(2009, 2, 21).plusDays(random.nextInt(6000));
        ventana.fecha.setDate(dia);
        ventana.txtMana.setText(String.valueOf(200 + random.nextInt(601)));
    }

    private void limpiar() {
        ventana.txtNombre.setText("");
        ventana.txtDescripcion.setText("");
        ventana.comboRegion.setSelectedIndex(0);
        ventana.radioMago.setSelected(true);
        azar();
    }

    private void actualizar() {
        ventana.datosLista.clear();
        for (Campeon c : modelo.getLista()) {
            ventana.datosLista.addElement(c);
        }
    }
}
