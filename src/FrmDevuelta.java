import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrmDevuelta extends JFrame {
    JTextField txtMonto;
    JTable tabla;
    DefaultTableModel modelo;
    int[] denominaciones = { 100000, 50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50 };
    String[] tipos = { "Billete", "Billete", "Billete", "Billete", "Billete", "Billete", "Billete", "Moneda", "Moneda",
            "Moneda", "Moneda" };
    int[] existencia = new int[denominaciones.length];
    int[] ultimaCantidad = new int[denominaciones.length];

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
        for (int i = 0; i < denominaciones.length; i++) {
            String input = JOptionPane.showInputDialog("Ingrese la existencia de " + (tipos[i] + " de " + denominaciones[i]));
            if (esNumeroValido(input)) {
                existencia[i] = Integer.parseInt(input);
            } else {
                existencia[i] = 0;
            }
        }
    }

    private void calcularDevuelta() {
        limpiarTabla();
        String ingresoMonto = txtMonto.getText();
        if (esNumeroValido(ingresoMonto)) {
            int monto = Integer.parseInt(ingresoMonto);
            for (int i = 0; i < denominaciones.length; i++) {
                ultimaCantidad[i] = 0;
                if (monto >= denominaciones[i] && existencia[i] > 0) {
                    int cantidad = Math.min(monto / denominaciones[i], existencia[i]);
                    monto -= cantidad * denominaciones[i];
                    existencia[i] -= cantidad;
                    ultimaCantidad[i] = cantidad;
                    if (cantidad > 0) {
                        agregarFila(denominaciones[i], tipos[i], cantidad, existencia[i]);
                    }
                }
            }
            if (monto > 0) {
                JOptionPane.showMessageDialog(null, "No hay suficiente cambio disponible.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Ingrese un monto válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarUltimaDevolucion() {
        limpiarTabla();
        for (int i = 0; i < denominaciones.length; i++) {
            agregarFila(denominaciones[i], tipos[i], ultimaCantidad[i], existencia[i]);
        }
    }

    private boolean esNumeroValido(String input) {
        return input != null && input.chars().allMatch(Character::isDigit);
    }

    private void limpiarTabla() {
        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
    }

    private void agregarFila(int denominacion, String tipo, int cantidad, int existencia) {
        modelo.addRow(new Object[] { denominacion, tipo, cantidad, existencia });
    }

    private void actualizarTablaExistencia() {
        limpiarTabla();
        for (int i = 0; i < denominaciones.length; i++) {
            agregarFila(denominaciones[i], tipos[i], 0, existencia[i]);
        }
    }

    private void editarExistencia() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada >= 0) {
            String nuevaCantidad = JOptionPane.showInputDialog("Ingrese la nueva existencia para " + denominaciones[filaSeleccionada]);
            if (esNumeroValido(nuevaCantidad)) {
                existencia[filaSeleccionada] = Integer.parseInt(nuevaCantidad);
                actualizarTablaExistencia();
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una fila para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new FrmDevuelta().setVisible(true);
    }
}
