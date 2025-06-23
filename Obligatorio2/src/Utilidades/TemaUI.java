package Utilidades;

/**
 *
 * @author dariansaldana 230846
 */
import java.awt.*;
import javax.swing.*;

public class TemaUI {

    private static boolean modoOscuro = false;

    public static void alternarModo() {
        modoOscuro = !modoOscuro;
    }

    public static boolean esModoOscuro() {
        return modoOscuro;
    }

    public static void aplicarTema(Component comp) {
        Color fondo = modoOscuro ? Color.DARK_GRAY : Color.WHITE;
        Color texto = modoOscuro ? Color.WHITE : Color.BLACK;

        if (comp instanceof JPanel || comp instanceof JFrame || comp instanceof JDialog) {
            comp.setBackground(fondo);
        }

        if (comp instanceof JLabel) {
            ((JLabel) comp).setForeground(texto);

        } else if (comp instanceof JTextField) {
            JTextField txt = (JTextField) comp;
            txt.setForeground(texto);
            txt.setBackground(modoOscuro ? Color.GRAY : Color.WHITE);
            txt.setCaretColor(texto);

        } else if (comp instanceof JList) {
            JList<?> list = (JList<?>) comp;
            list.setForeground(texto);
            list.setBackground(modoOscuro ? Color.GRAY : Color.WHITE);
            list.setSelectionBackground(modoOscuro ? Color.DARK_GRAY : Color.LIGHT_GRAY);
            list.setSelectionForeground(texto);

        } else if (comp instanceof JComboBox) {
            JComboBox<?> combo = (JComboBox<?>) comp;
            combo.setForeground(texto);
            combo.setBackground(modoOscuro ? Color.GRAY : Color.WHITE);
            combo.setOpaque(true);
        }

        if (comp instanceof Container) {
            for (Component hijo : ((Container) comp).getComponents()) {
                aplicarTema(hijo);
            }
        }
    }

    public static void aplicarTemaGlobal(JFrame frame) {
        aplicarTema(frame);
        SwingUtilities.updateComponentTreeUI(frame);
    }
}
