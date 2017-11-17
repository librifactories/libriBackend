import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Thread;

public class LIBRIGUI {

    public static Thread thread;
    private JButton btnIniciar;

    public LIBRIGUI() {
        btnIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LIBRI Factories");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton but1 = new JButton("Iniciar servidor");
        but1.setSize(100, 100);

        JButton but2 = new JButton("Parar servidor");
        but2.setSize(100, 100);

        JLabel status = new JLabel("Status do servidor: Parado");

        but1.addActionListener(e -> {
            try {
                thread = new Thread(new FORMServer());
                thread.start();
                status.setText("Status do servidor: Rodando");
            }
            catch (Exception ex) {

            }
        });

        but2.addActionListener(e -> {
            try {
                thread.interrupt();
                status.setText("Status do servidor: Parado");
            }
            catch (Exception ex) {

            }
        });

        panel.add(but1);
        panel.add(but2);
        panel.add(status);

        panel.setPreferredSize(new Dimension(250, 200));
        contentPane.add(panel, BorderLayout.LINE_START);

        frame.pack();
        frame.setVisible(true);
    }

}
