package R18A_Group7_Asm2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class OwnerAdmin extends JFrame {

    static OwnerAdmin frame;
    private JPanel contentPane;
    GroupLayout gl_contentPane;

    JLabel pageTitle = new JLabel("Owner Admin");

    JButton SellerButton = new JButton("Seller Admin");

    JButton CashierButton = new JButton("Cashier Admin");

    JRadioButton create = new JRadioButton("Create");

    JRadioButton remove = new JRadioButton("Remove");

    JLabel accountTypeLabel = new JLabel("Account Type");

    JComboBox comboBox = new JComboBox(new String[]{"Owner", "Cashier", "Seller"});

    JLabel nameLabel = new JLabel("Name");

    JTextField name_field = new JTextField();

    JLabel passwordLabel = new JLabel("Password");

    JTextField password_field = new JTextField();

    JButton confirmButton = new JButton("Confirm");

    JButton userDetail = new JButton("User Report");

    JButton cancelDetail = new JButton("Cancel Details Report");

    JButton logout = new JButton("Log Out");

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frame = new OwnerAdmin();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public OwnerAdmin(){
        Timer timer = Timer.getInstance();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel();
        contentPane.setForeground(Color.DARK_GRAY);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);


        pageTitle.setFont(new Font("Tahoma", Font.PLAIN, 20));

        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (create.isSelected()){
                    remove.setEnabled(false);
                }else{
                    remove.setEnabled(true);
                }
            }
        });

        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (remove.isSelected()){
                    create.setEnabled(false);
                }else{
                    create.setEnabled(true);
                }
            }
        });

        SellerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String args[] = new String[]{};
                OwnerToSeller.main(args);
                if(frame != null){
                    frame.dispose();
                }

                timer.setCurrentFrame("os");

            }
        });


        CashierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String args[] = new String[]{};
                OwnerToCashier.main(args);

                if(frame != null){
                    frame.dispose();
                }

                timer.setCurrentFrame("oc");
            }
        });


        cancelDetail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OwnerSummary ownerSummary = OwnerSummary.getInstance();
                try {
                    ownerSummary.writeIntoHistory();
                    ownerSummary.generateReport();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                timer.reset();
            }
        });

        userDetail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UserReport.generate_report();
                    Desktop.getDesktop().open(new File("user_report.csv"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        });

        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VendingMachine.main(new String[]{});

                if(frame != null){
                    frame.dispose();
                }

                timer.setCurrentFrame("vend");
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name= name_field.getText();
                String password=String.valueOf( password_field.getText());

                if(name.equals("") || password.equals("")){
                    JOptionPane.showMessageDialog(OwnerAdmin.this,
                            "Sorry, Invalid Input!",
                            "No Input",
                            JOptionPane.ERROR_MESSAGE);
                }else{
                    if(create.isSelected()){

                        boolean exist = false;

                        exist = User.nameExist(name);
                        if(exist){
                            JOptionPane.showMessageDialog(OwnerAdmin.this,
                                    "Sorry, Already Exist!",
                                    "Create Account Error!",
                                    JOptionPane.ERROR_MESSAGE);
                        }else{
                            User.addNewUser(name, password, (String) comboBox.getSelectedItem());

                            JOptionPane.showMessageDialog(OwnerAdmin.this,
                                    "Create Success",
                                    "Create Admin",
                                    JOptionPane.PLAIN_MESSAGE);
                        }
                    }else if(remove.isSelected()){
                        User.removeUser(name, (String) comboBox.getSelectedItem());

                        JOptionPane.showMessageDialog(OwnerAdmin.this,
                                "Remove Success",
                                "Remove Admin",
                                JOptionPane.PLAIN_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(OwnerAdmin.this,
                                "Sorry, Please Select Remove Or Create!",
                                "No Selection!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }


            }
        });

        gl_contentPane = addGroupLayout();
        contentPane.setLayout(gl_contentPane);
    }


    public GroupLayout addGroupLayout(){

        GroupLayout gl_contentPane = new GroupLayout(contentPane);

        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup()
                        .addComponent(pageTitle, GroupLayout.Alignment.CENTER)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(20)
                                .addComponent(SellerButton)
                                .addGap(80)
                                .addComponent(CashierButton)
                                .addGap(120)
                        )
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(20)
                                .addComponent(create)
                                .addGap(30)
                                .addComponent(remove)
                                .addGap(30)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(accountTypeLabel)
                                        .addComponent(comboBox)
                                )
                                .addGap(140)
                        )
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(20)
                                .addComponent(nameLabel)
                                .addGap(30)
                                .addComponent(name_field)
                                .addGap(230)
                        )
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(20)
                                .addComponent(passwordLabel)
                                .addGap(10)
                                .addComponent(password_field)
                                .addGap(50)
                                .addComponent(confirmButton)
                                .addGap(100)
                        )
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(20)
                                .addComponent(userDetail)
                                .addGap(40)
                                .addComponent(cancelDetail)
                                .addGap(100)
                        )
                        .addComponent(logout, GroupLayout.Alignment.TRAILING)
        );
        gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup()
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(10)
                                .addComponent(pageTitle)
                                .addGap(20)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(SellerButton)
                                        .addComponent(CashierButton)
                                )
                                .addGap(30)
                                .addComponent(accountTypeLabel)
                                .addGap(10)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(create)
                                        .addComponent(remove)
                                        .addComponent(comboBox)
                                )
                                .addGap(20)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(nameLabel)
                                        .addComponent(name_field)
                                )
                                .addGap(20)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(passwordLabel)
                                        .addComponent(password_field)
                                        .addComponent(confirmButton)
                                )
                                .addGap(30)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(userDetail)
                                        .addComponent(cancelDetail)
                                )
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(logout, GroupLayout.Alignment.TRAILING)
                                )

                        )



        );


        return gl_contentPane;
    }
}
