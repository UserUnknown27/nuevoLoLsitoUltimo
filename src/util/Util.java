package util;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Util {

    public static void error(String texto) {
        JOptionPane.showMessageDialog(null, texto, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void info(String titulo, String texto) {
        JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    // preguntar
    public static int preguntar(String texto, String titulo) {
        return JOptionPane.showConfirmDialog(null, texto, titulo, JOptionPane.YES_NO_OPTION);
    }

    public static JFileChooser elegirFichero() {
        JFileChooser fichero = new JFileChooser();
        fichero.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
        return fichero;
    }
}
