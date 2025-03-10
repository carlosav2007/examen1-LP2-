import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrmDevuelta extends JFrame {
    JTextField txtMonto;
    JTable tabla;
    DefaultTableModel modelo;
    int[] denominacionesByM = { 100000, 50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50 };
    String[] tipos = { "Billetes", "Billetes", "Billetes", "Billetes", "Billetes", "Billetes", "Billetes", "Monedas",
            "Moneda", "Monedas", "Monedas" };
    int[] existencia = new int[denominacionesByM.length];
    int[] ultimaCantidad = new int[denominacionesByM.length];

    public FrmDevuelta() {
        setTitle("Calculadora de Devuelta");
        setSize(500, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblMonto = new JLabel("Monto a devolver:");
        lblMonto.setBounds(10, 10, 150, 25);
        getContentPane().add(lblMonto);

        txtMonto = new JTextField();
        txtMonto.setBounds(150, 10, 100, 25);
        getContentPane().add(txtMonto);

        JButton btnCalcular = new JButton("Calcular");
        btnCalcular.setBounds(260, 10, 100, 25);
        getContentPane().add(btnCalcular);

        JButton btnEditar = new JButton("Editar Existencia");
        btnEditar.setBounds(10, 320, 200, 25);
        getContentPane().add(btnEditar);

        JButton btnVolver = new JButton("Volver");
        btnVolver.setBounds(220, 320, 200, 25);
        getContentPane().add(btnVolver);

        String[] columnas = { "Denominación", "Tipo", "Cantidad", "Existencia" };
        modelo = new DefaultTableModel(columnas, 0);

        tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBounds(10, 50, 350, 250);
        getContentPane().add(scrollPane);

        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularDevuelta();
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarExistencia();
            }
        });

        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarUltimaDevolucion();
            }
        });

        ingresarExistencia();
        actualizarTablaExistencia();
    }

    private void ingresarExistencia() {
        for (int i = 0; i < denominacionesByM.length; i++) {
            String input = JOptionPane
                    .showInputDialog("Ingrese la existencia de " + (tipos[i] + " de " + denominacionesByM[i]));
            if (esNumeroValido(input) == "Válido") {
                existencia[i] = Integer.parseInt(input);
            } else {
                existencia[i] = 0;
            }
        }
    }

    private void calcularDevuelta() {
        limpiarTabla();
        String ingresoMonto = txtMonto.getText();
        if (esNumeroValido(ingresoMonto) == "Válido") {
            int monto = Integer.parseInt(ingresoMonto);
            for (int i = 0; i < denominacionesByM.length; i++) {
                ultimaCantidad[i] = 0;
                if (monto >= denominacionesByM[i] && existencia[i] > 0) {
                    int cantidad = Math.min(monto / denominacionesByM[i], existencia[i]);
                    monto -= cantidad * denominacionesByM[i];
                    existencia[i] -= cantidad;
                    ultimaCantidad[i] = cantidad;
                    if (cantidad > 0) {
                        agregarFila(denominacionesByM[i], tipos[i], cantidad, existencia[i]);
                    }
                }
            }
            if (monto > 0) {
                JOptionPane.showMessageDialog(null, "No hay suficiente cambio disponible.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Ingrese un monto válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarUltimaDevolucion() {
        limpiarTabla();
        for (int i = 0; i < denominacionesByM.length; i++) {
            agregarFila(denominacionesByM[i], tipos[i], ultimaCantidad[i], existencia[i]);
        }
    }

    private String esNumeroValido(String input) {
        if (input == null) {
            return "Inválido";
        }
        for (int i = 0; i < input.length(); i++) {
            if (!Character.isDigit(input.charAt(i))) {
                return "Inválido";
            }
        }
        return "Válido";
    }

    private void limpiarTabla() {
        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
    }

    private void agregarFila(int denominacion, String tipo, int cantidad, int existencia) {
        modelo.addRow(new Object[] {  denominacion,tipo, cantidad, existencia });
    }

    private void actualizarTablaExistencia() {
        limpiarTabla();
        for (int i = 0; i < denominacionesByM.length; i++) {
            agregarFila(denominacionesByM[i], tipos[i], 0, existencia[i]);
        }
    }

    private void editarExistencia() {
        int filaSeleccionada = tabla.getSelectionModel().getLeadSelectionIndex();
        if (filaSeleccionada >= 0) {
            String nuevaCantidad = JOptionPane
                    .showInputDialog("Ingrese la nueva existencia para " + denominacionesByM[filaSeleccionada]);
            if (esNumeroValido(nuevaCantidad) == "Válido") {
                existencia[filaSeleccionada] = Integer.parseInt(nuevaCantidad);
                actualizarTablaExistencia();
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una fila para editar.", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new FrmDevuelta().setVisible(true);
    }
}

