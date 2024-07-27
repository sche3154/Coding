package VendorMachine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderCompleted extends JFrame {
    static OrderCompleted frame;
    JPanel contentPane;

    JButton backButton;
    JLabel statusLabel;

    static JLabel change = new JLabel();

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    if(args.length == 1){
                        change = new JLabel();
                        change.setText(args[0]);
                    }

                    frame = new OrderCompleted();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public OrderCompleted() {
        Timer timer = Timer.getInstance();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 580, 360);
        contentPane = new JPanel();
        contentPane.setForeground(Color.DARK_GRAY);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);


        String str = String.format("Thanks you for your payment!");
        statusLabel = new JLabel(str);
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));

        backButton = new JButton("back");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                VendingMachine.startTime = new Date();
                String str[] = new String[] {};
                VendingMachine.main(str);

                frame.dispose();
                timer.setCurrentFrame("vend");
            }
        });

        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.CENTER).
                addGroup(gl_contentPane.createSequentialGroup()
                        .addComponent(statusLabel)
                        .addGap(40)
                        .addGroup(gl_contentPane.createParallelGroup()
                                .addComponent(backButton, GroupLayout.Alignment.TRAILING)
                        )
                        .addGap(20)
                )
                .addComponent(change)


        );
        gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup()
                .addGroup(gl_contentPane.createSequentialGroup()
                        .addGroup(gl_contentPane.createParallelGroup()
                                .addComponent(statusLabel)
                                .addComponent(backButton)
                        )
                        .addGap(20)
                        .addComponent(change)
                        )

        );

        contentPane.setLayout(gl_contentPane);
    }

    public void doClick() {
    }
}
