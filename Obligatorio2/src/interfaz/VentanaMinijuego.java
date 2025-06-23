package interfaz;

import Utilidades.TemaUI;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

/**
 *
 * @author dariansaldana 230846
 */
public class VentanaMinijuego extends javax.swing.JFrame {

    public VentanaMinijuego() {
        setTitle("MiniJuego - Esquivar Obstáculos creado por ChatGPT");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        PanelJuego panel = new PanelJuego();
        getContentPane().add(panel);
        pack(); // <- ajusta el JFrame al tamaño preferido del panel
        setLocationRelativeTo(null);
        setVisible(true);

        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                requestFocusInWindow();
            }
        });
        TemaUI.aplicarTema(this);
    }

    // Clase interna para el juego
    class PanelJuego extends JPanel implements ActionListener, KeyListener {

        private Timer timer;
        private int jugadorX = 200, jugadorY = 400;
        private int jugadorW = 50, jugadorH = 50;
        private ArrayList<Rectangle> obstaculos = new ArrayList<>();
        private Random rand = new Random();
        private boolean gameOver = false;
        private int velocidad = 10;
        private int ticks = 0;
        private int score = 0;
        private Font scoreFont = new Font("Arial", Font.BOLD, 16);

        public PanelJuego() {
            setFocusable(true);
            addKeyListener(this);
            timer = new Timer(20, this);
            timer.start();

            // 💡 Esto asegura que el panel reciba foco luego de ser agregado al JFrame
            addHierarchyListener(e -> {
                if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                    requestFocusInWindow();
                }
            });

            setPreferredSize(new Dimension(500, 500));
            setBackground(Color.WHITE);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!gameOver) {
                moverObstaculos();
                detectarColisiones();
                if (rand.nextInt(20) == 0) {
                    obstaculos.add(new Rectangle(rand.nextInt(450), 0, 30, 30));
                }

                ticks++;
                score++;

                if (ticks % 300 == 0) {
                    velocidad += 2;
                }

                repaint();
            }
        }

        private void moverObstaculos() {
            for (Rectangle r : obstaculos) {
                r.y += 5;
            }
        }

        private void detectarColisiones() {
            Rectangle jugador = new Rectangle(jugadorX, jugadorY, jugadorW, jugadorH);
            for (Rectangle r : obstaculos) {
                if (r.intersects(jugador)) {
                    gameOver = true;
                    timer.stop();
                }
            }
        }

        private void reiniciarJuego() {
            jugadorX = 200;
            jugadorY = 400;
            obstaculos.clear();
            gameOver = false;
            velocidad = 10;
            ticks = 0;
            score = 0;
            timer.start();
            repaint();
            requestFocusInWindow();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.BLUE);
            g.fillRect(jugadorX, jugadorY, jugadorW, jugadorH);

            g.setColor(Color.RED);
            for (Rectangle r : obstaculos) {
                g.fillRect(r.x, r.y, r.width, r.height);
            }

            // Puntaje en pantalla
            g.setColor(Color.BLACK);
            g.setFont(scoreFont);
            g.drawString("Puntaje: " + score, 10, 20);

            if (gameOver) {
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.drawString("¡Perdiste!", 160, 220);
                g.setFont(scoreFont);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (!gameOver) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT && jugadorX > 0) {
                    jugadorX -= velocidad;
                }
                if (key == KeyEvent.VK_RIGHT && jugadorX < getWidth() - jugadorW) {
                    jugadorX += velocidad;
                }
                if (key == KeyEvent.VK_UP && jugadorY > 0) {
                    jugadorY -= velocidad;
                }
                if (key == KeyEvent.VK_DOWN && jugadorY < getHeight() - jugadorH) {
                    jugadorY += velocidad;
                }
            } else {
                int opcion = JOptionPane.showConfirmDialog(
                        VentanaMinijuego.this,
                        "¿Deseás reiniciar el juego?",
                        "Reiniciar",
                        JOptionPane.YES_NO_OPTION
                );
                if (opcion == JOptionPane.YES_OPTION) {
                    reiniciarJuego();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 774, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 402, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
