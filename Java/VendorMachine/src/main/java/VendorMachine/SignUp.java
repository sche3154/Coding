package VendorMachine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUp extends JFrame {

    static SignUp frame;
    private JPanel contentPane;
    private JTextField textField;
    private JPasswordField passwordField;

    JButton create;

//    private Date startTime;
//    private Timer timer;
//    private DateFormat timeFormat;


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frame = new SignUp();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public SignUp() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Timer timer = Timer.getInstance();
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);


        JLabel loginLabel = new JLabel("Sign Up");
        loginLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        loginLabel.setForeground(Color.DARK_GRAY);

        JLabel name = new JLabel("Name:");

        textField = new JTextField();
        textField.setColumns(10);

        JLabel password = new JLabel("Password:");

        passwordField = new JPasswordField();

        create = new JButton("Create");
        create.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String name=textField.getText();
                String password=String.valueOf(passwordField.getPassword());

                boolean exist = false;

                exist = User.nameExist(name);
                if(exist){
                    JOptionPane.showMessageDialog(SignUp.this,
                            "Sorry, Already Exist!",
                            "Sign Up Error!",
                            JOptionPane.ERROR_MESSAGE);
                }else{
                    if(name.equals("") || password.equals("")){
                        JOptionPane.showMessageDialog(SignUp.this,
                                "Sorry, Invalid Input!",
                                "No Input",
                                JOptionPane.ERROR_MESSAGE);
                    }else{
                        User.addNewUser(name, password, "Customer");
                        frame.dispose();
                        VendingMachine.frame.dispose();
                        UserLoggedVendingMachine.main(new String[]{name});
                        timer.setCurrentFrame("lvend");
                    }


                }



            }
        });

        //      Timer init
//        add(timeLabel);
//        startTime = new Date();
//        timeFormat = new SimpleDateFormat("mm:ss");
//        timer = new Timer(1000, this::timerListener);
//        // to make sure it doesn't wait one second at the start
//        timer.setInitialDelay(0);
//        timer.start();



//		JButton backButton = new JButton("back");
//		backButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				String args[] = new String[]{};
//				VendingMachine.main(args);
//				frame.dispose();
//			}
//		});

        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGap(28)
                                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(password)
                                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                                .addComponent(name)
                                                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                                                .addGap(76)
                                                                                .addComponent(loginLabel))
                                                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                                                .addGap(54)
                                                                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                                        .addComponent(passwordField)
                                                                                        .addComponent(textField, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)))))))
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGap(158)
                                                .addComponent(create, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
                                                .addGap(52)
                                        ))
                                .addContainerGap(78, Short.MAX_VALUE))
        );
        gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(loginLabel)
                                .addGap(18)
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(name)
                                        .addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18)
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(password)
                                        .addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(create, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                )
                                .addContainerGap(96, Short.MAX_VALUE)
                        )
        );
        contentPane.setLayout(gl_contentPane);
    }

//    public void timerListener(ActionEvent e) {
//        Date actualTime = new Date();
//        String time = timeFormat.format(new Date(actualTime.getTime() - startTime.getTime()));
//
//        if(time.equals("02:00")) {
//            System.out.println("time over, order reset!");
//            timer.stop();
//            frame.dispose();
//            VendingMachine.startTime = new Date();
//            VendingMachine.timer.restart();
//
//        }
//    }
}
