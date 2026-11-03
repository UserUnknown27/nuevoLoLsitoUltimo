package personajes.gui;

import com.github.lgooddatepicker.components.DatePicker;
import personajes.base.Campeon;
import personajes.base.Regiones;

import javax.swing.*;

public class Ventana {
    private JPanel panel1;
    public JRadioButton radioMago;
    public JRadioButton radioLuchador;
    public JRadioButton radioAsesino;
    public JRadioButton radioTirador;
    public JRadioButton radioSoporte;
    public JRadioButton radioTanque;
    public JTextField txtNombre;
    public JComboBox<String> comboRegion;
    public DatePicker fecha;
    public JTextField txtMana;
    public JTextArea txtDescripcion;
    public JButton btnNuevo;
    public JButton btnMostrar;
    public JButton btnExportar;
    public JButton btnImportar;
    public JList<Campeon> lista;

    public JFrame frame;
    public DefaultListModel<Campeon> datosLista = new DefaultListModel<>();

    public Ventana() {
        frame = new JFrame("LoL");
        frame.setContentPane(panel1);
        // la X la maneja el controlador
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        lista.setModel(datosLista);
        comboRegion.setModel(new DefaultComboBoxModel<>(Regiones.LISTA));
        radioMago.setSelected(true);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
