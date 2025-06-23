package interfaz;

import Utilidades.TemaUI;
import dominio.*;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.ButtonGroup;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

/**
 *
 * @author dariansaldana 230846
 */
public class VentanaReporte extends javax.swing.JFrame implements Observer {

    public VentanaReporte(Sistema sis) {
        modelo = sis;
        modelo.addObserver(this);
        initComponents();
        TemaUI.aplicarTema(this);
        cargarListaVehiculos();
        cargarFiltros();
        configurarOrden();
        setTitle("Reportes");
        actualizarEstadisticasGenerales();
    }

    private void cargarListaVehiculos() {

        // Vehículos
        DefaultListModel modeloVehiculos = new DefaultListModel();
        for (Vehiculo v : modelo.getListaVehiculos()) {
            modeloVehiculos.addElement(v);
        }
        listaVehiculosReporte.setModel(modeloVehiculos);
    }

    private void cargarFiltros() {
        filtroMovimientos.removeAllItems();
        filtroMovimientos.addItem("Todos");
        filtroMovimientos.addItem("Entrada");
        filtroMovimientos.addItem("Salida");
        filtroMovimientos.addItem("Servicio");
    }

    private void configurarOrden() {
        ButtonGroup group = new ButtonGroup();
        group.add(ordenAscendente);
        group.add(ordenDescendente);
        ordenAscendente.setSelected(true);
    }

    private void actualizarHistorial() {
        Vehiculo seleccionado = (Vehiculo) listaVehiculosReporte.getSelectedValue();
        if (seleccionado != null) {
            cargarHistorial(seleccionado);
        }
    }

    private void cargarHistorial(Vehiculo vehiculo) {
        List<MovimientoVehiculo> historial = new ArrayList<>();

        for (EntradaVehiculo entrada : modelo.getHistorialEntradas()) {
            if (entrada.getVehiculo().equals(vehiculo)) {
                historial.add(new MovimientoVehiculo("Entrada", entrada.getFecha() + " " + entrada.getHora(), "Notas: " + entrada.getNotas()));
            }
        }

        for (SalidaVehiculo salida : modelo.getHistorialSalidas()) {
            if (salida.getVehiculo().equals(vehiculo)) {
                historial.add(new MovimientoVehiculo("Salida", salida.getFecha() + " " + salida.getHora(), "Empleado: " + salida.getEmpleado().getNombre()));
            }
        }

        for (ServicioAdicional servicio : modelo.getServiciosAdicionales()) {
            if (servicio.getVehiculo().equals(vehiculo)) {
                historial.add(new MovimientoVehiculo("Servicio", servicio.getFecha() + " " + servicio.getHora(), servicio.getTipoServicio() + " - $" + servicio.getCosto()));
            }
        }

        // Filtro
        String filtro = (String) filtroMovimientos.getSelectedItem();
        if (!"Todos".equals(filtro)) {
            historial.removeIf(m -> !m.getTipo().equalsIgnoreCase(filtro));
        }

        // Orden
        historial.sort((m1, m2) -> {
            int comparacion = m1.getFechaHora().compareTo(m2.getFechaHora());
            return ordenAscendente.isSelected() ? comparacion : -comparacion;
        });

        // Mostrar en tabla
        DefaultTableModel model = new DefaultTableModel(new String[]{"Tipo", "Fecha/Hora", "Detalle"}, 0);
        for (MovimientoVehiculo m : historial) {
            model.addRow(new Object[]{m.getTipo(), m.getFechaHora(), m.getDescripcion()});
        }
        tablaHistorial.setModel(model);
    }

    private class MovimientoVehiculo {

        private String tipo;
        private String fechaHora;
        private String descripcion;

        public MovimientoVehiculo(String tipo, String fechaHora, String descripcion) {
            this.tipo = tipo;
            this.fechaHora = fechaHora;
            this.descripcion = descripcion;
        }

        public String getTipo() {
            return tipo;
        }

        public String getFechaHora() {
            return fechaHora;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    private void actualizarEstadisticasGenerales() {
        // Servicios más utilizados
        Map<String, Integer> contadorServicios = new HashMap<>();
        for (ServicioAdicional sa : modelo.getServiciosAdicionales()) {
            contadorServicios.put(sa.getTipoServicio(), contadorServicios.getOrDefault(sa.getTipoServicio(), 0) + 1);
        }

        DefaultListModel<String> modeloServicios = new DefaultListModel<>();
        contadorServicios.entrySet()
                .stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .forEach(e -> modeloServicios.addElement(e.getKey() + ": " + e.getValue() + " usos"));
        listaServicios.setModel(modeloServicios);

        // Estadías más largas
        List<String> estadias = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (EntradaVehiculo ev : modelo.getHistorialEntradas()) {
            if (ev.estaFinalizada()) {
                SalidaVehiculo salida = modelo.getHistorialSalidas().stream()
                        .filter(sv -> sv.getEntrada().equals(ev))
                        .findFirst().orElse(null);

                if (salida != null) {
                    try {
                        Date entrada = sdf.parse(ev.getFecha() + " " + ev.getHora());
                        Date salidaDate = sdf.parse(salida.getFecha() + " " + salida.getHora());
                        long duracion = salidaDate.getTime() - entrada.getTime();
                        long horas = duracion / (1000 * 60 * 60);

                        Vehiculo vehiculo = ev.getVehiculo();
                        String detalle = vehiculo.getMatricula() + " - " + vehiculo.getMarca() + " " + vehiculo.getModelo() + " - " + horas + " horas";
                        estadias.add(detalle);
                    } catch (Exception e) {
                        estadias.add("Error en fechas para " + ev.getVehiculo().getMatricula());
                    }
                }
            }
        }

        DefaultListModel<String> modeloEstadias = new DefaultListModel<>();
        estadias.stream()
                .sorted((a, b) -> {
                    int horasA = Integer.parseInt(a.replaceAll("\\D+", ""));
                    int horasB = Integer.parseInt(b.replaceAll("\\D+", ""));
                    return Integer.compare(horasB, horasA);
                })
                .limit(5)
                .forEach(modeloEstadias::addElement);
        listaEstadias.setModel(modeloEstadias);

        // Empleados con menos movimientos
        Map<Empleado, Integer> movimientosEmpleado = new HashMap<>();
        for (EntradaVehiculo ev : modelo.getHistorialEntradas()) {
            movimientosEmpleado.put(ev.getEmpleado(), movimientosEmpleado.getOrDefault(ev.getEmpleado(), 0) + 1);
        }
        for (SalidaVehiculo sv : modelo.getHistorialSalidas()) {
            movimientosEmpleado.put(sv.getEmpleado(), movimientosEmpleado.getOrDefault(sv.getEmpleado(), 0) + 1);
        }

        DefaultListModel<String> modeloEmpleados = new DefaultListModel<>();
        movimientosEmpleado.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(5)
                .forEach(e -> modeloEmpleados.addElement(e.getKey().getNombre() + ": " + e.getValue() + " movs"));
        listaEmpleados.setModel(modeloEmpleados);

        // Clientes con más contratos
        Map<Cliente, Integer> contratosPorCliente = new HashMap<>();
        for (Contrato c : modelo.getListaContratos()) {
            contratosPorCliente.put(c.getCliente(), contratosPorCliente.getOrDefault(c.getCliente(), 0) + 1);
        }

        DefaultListModel<String> modeloClientes = new DefaultListModel<>();
        contratosPorCliente.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(5)
                .forEach(e -> modeloClientes.addElement(e.getKey().getNombre() + ": " + e.getValue() + " contratos"));
        listaClientes.setModel(modeloClientes);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabReportes = new javax.swing.JTabbedPane();
        panelHistorial = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        listaVehiculosReporte = new javax.swing.JList();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaHistorial = new javax.swing.JTable();
        filtroMovimientos = new javax.swing.JComboBox<>();
        ordenDescendente = new javax.swing.JRadioButton();
        ordenAscendente = new javax.swing.JRadioButton();
        btnExportarArchivo = new javax.swing.JButton();
        panelMovimientos = new javax.swing.JPanel();
        panelEstadisticasGenerales = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listaEstadias = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        listaServicios = new javax.swing.JList<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        listaEmpleados = new javax.swing.JList<>();
        jScrollPane6 = new javax.swing.JScrollPane();
        listaClientes = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabReportes.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N

        panelHistorial.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel1.setText("Vehículo");
        panelHistorial.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        listaVehiculosReporte.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        listaVehiculosReporte.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listaVehiculosReporte.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listaVehiculosReporteValueChanged(evt);
            }
        });
        jScrollPane5.setViewportView(listaVehiculosReporte);

        panelHistorial.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 170, 180));

        tablaHistorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Entradas", "Salidas", "Servicios Adicionales"
            }
        ));
        jScrollPane1.setViewportView(tablaHistorial);

        panelHistorial.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 70, 490, 250));

        filtroMovimientos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        filtroMovimientos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filtroMovimientosActionPerformed(evt);
            }
        });
        panelHistorial.add(filtroMovimientos, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, 180, -1));

        ordenDescendente.setText("Orden Decreciente");
        ordenDescendente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ordenDescendenteActionPerformed(evt);
            }
        });
        panelHistorial.add(ordenDescendente, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 30, -1, -1));

        ordenAscendente.setText("Orden Creciente");
        ordenAscendente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ordenAscendenteActionPerformed(evt);
            }
        });
        panelHistorial.add(ordenAscendente, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 30, -1, -1));

        btnExportarArchivo.setText("Exportar Archivo");
        btnExportarArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarArchivoActionPerformed(evt);
            }
        });
        panelHistorial.add(btnExportarArchivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 340, -1, -1));

        tabReportes.addTab("Historial", panelHistorial);
        tabReportes.addTab("Movimientos", panelMovimientos);

        panelEstadisticasGenerales.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        listaEstadias.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        listaEstadias.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(listaEstadias);

        panelEstadisticasGenerales.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 240, 450, 130));

        listaServicios.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        listaServicios.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(listaServicios);

        panelEstadisticasGenerales.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 210, 150));

        listaEmpleados.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        listaEmpleados.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(listaEmpleados);

        panelEstadisticasGenerales.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, 220, 150));

        listaClientes.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        listaClientes.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane6.setViewportView(listaClientes);

        panelEstadisticasGenerales.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 50, 220, 150));

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 1, 16)); // NOI18N
        jLabel2.setText("Clientes más contratos");
        panelEstadisticasGenerales.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 20, 210, 20));

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 1, 16)); // NOI18N
        jLabel3.setText("Estadía mas larga");
        panelEstadisticasGenerales.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 210, 200, 20));

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 1, 16)); // NOI18N
        jLabel4.setText("Emp. menos movimientos");
        panelEstadisticasGenerales.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 210, 20));

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 1, 16)); // NOI18N
        jLabel5.setText("Servicios más utilizados");
        panelEstadisticasGenerales.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 200, 20));

        tabReportes.addTab("Estadísticas Generales", panelEstadisticasGenerales);

        getContentPane().add(tabReportes, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 690, 420));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void listaVehiculosReporteValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listaVehiculosReporteValueChanged
        if (!evt.getValueIsAdjusting()) {
            Vehiculo seleccionado = (Vehiculo) listaVehiculosReporte.getSelectedValue();
            if (seleccionado != null) {
                cargarHistorial(seleccionado);
            }
        }
    }//GEN-LAST:event_listaVehiculosReporteValueChanged

    private void btnExportarArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarArchivoActionPerformed
        Vehiculo seleccionado = (Vehiculo) listaVehiculosReporte.getSelectedValue();
        if (seleccionado == null) {
            return;
        }

        try (PrintWriter writer = new PrintWriter(new File(seleccionado.getMatricula() + ".txt"))) {
            for (int i = 0; i < tablaHistorial.getRowCount(); i++) {
                String linea = "";
                for (int j = 0; j < tablaHistorial.getColumnCount(); j++) {
                    linea += tablaHistorial.getValueAt(i, j).toString();
                    if (j < tablaHistorial.getColumnCount() - 1) {
                        linea += " | ";
                    }
                }
                writer.println(linea);
            }
            javax.swing.JOptionPane.showMessageDialog(this, "Archivo exportado exitosamente.");
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al exportar archivo: " + e.getMessage());
        }
    }//GEN-LAST:event_btnExportarArchivoActionPerformed

    private void filtroMovimientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filtroMovimientosActionPerformed
        actualizarHistorial();
    }//GEN-LAST:event_filtroMovimientosActionPerformed

    private void ordenDescendenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ordenDescendenteActionPerformed
        actualizarHistorial();
    }//GEN-LAST:event_ordenDescendenteActionPerformed

    private void ordenAscendenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ordenAscendenteActionPerformed
        actualizarHistorial();
    }//GEN-LAST:event_ordenAscendenteActionPerformed

    @Override
    public void update(Observable o, Object arg) {
        cargarListaVehiculos();

        Vehiculo seleccionado = (Vehiculo) listaVehiculosReporte.getSelectedValue();
        if (seleccionado != null) {
            cargarHistorial(seleccionado);
        }
        actualizarEstadisticasGenerales();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExportarArchivo;
    private javax.swing.JComboBox<String> filtroMovimientos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JList<String> listaClientes;
    private javax.swing.JList<String> listaEmpleados;
    private javax.swing.JList<String> listaEstadias;
    private javax.swing.JList<String> listaServicios;
    private javax.swing.JList listaVehiculosReporte;
    private javax.swing.JRadioButton ordenAscendente;
    private javax.swing.JRadioButton ordenDescendente;
    private javax.swing.JPanel panelEstadisticasGenerales;
    private javax.swing.JPanel panelHistorial;
    private javax.swing.JPanel panelMovimientos;
    private javax.swing.JTabbedPane tabReportes;
    private javax.swing.JTable tablaHistorial;
    // End of variables declaration//GEN-END:variables
    private Sistema modelo;
}
