package VendorMachine;



import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class OwnerToCashier extends JFrame {


    static OwnerToCashier frame;
    private JPanel contentPane;
    GroupLayout gl_contentPane;

//    private Date startTime;
//    private Timer timer;
//    private DateFormat timeFormat;

    JLabel pageTitle = new JLabel("Cashier Admin Page");

    JLabel cashKind  = new JLabel("Cash Kinds");

    JComboBox kinds;

    JLabel currentAmount = new JLabel();

    JRadioButton reduceButton = new JRadioButton("Reduce Selected Cash");
    JRadioButton fillButton = new  JRadioButton("Fill Selected cash");

    JLabel amountLabel = new JLabel("Amount");

    JTextField textField = new JTextField();

    JButton confirm = new JButton("Confirm");

    JButton generateCashDetailsButton = new JButton("Cash Report");
    JButton generateTransactionButton = new JButton("Payment Report");

    JButton back = new JButton("Back");


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    frame = new OwnerToCashier();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public OwnerToCashier(){
        Timer timer = Timer.getInstance();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 520, 360);
        contentPane = new JPanel();
        contentPane.setForeground(Color.DARK_GRAY);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        pageTitle.setFont(new Font("Tahoma", Font.PLAIN, 20));
        currentAmount.setFont(new Font("Tahoma", Font.PLAIN, 20));

        String cash[] = {"100$", "50$", "20$", "10$","5$", "2$", "1$" ,"50c", "20c","10c", "5c", "2c", "1c"};
        kinds = new JComboBox(cash);

        kinds.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAmount();
            }
        });



        displayAmount();

        gl_contentPane = addGroupLayout();

        contentPane.setLayout(gl_contentPane);


        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.dispose();

                String[] args = new String[]{};
                OwnerAdmin.main(args);
                timer.setCurrentFrame("oa");
            }
        });

        reduceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(reduceButton.isSelected()){
                    fillButton.setEnabled(false);
                }
                else{
                    fillButton.setEnabled(true);
                }

            }
        });

        fillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fillButton.isSelected()){
                    reduceButton.setEnabled(false);
                }
                else{
                    reduceButton.setEnabled(true);
                }
            }
        });


        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(reduceButton.isSelected()){
                    try{
                        String input = textField.getText();
                        int amount = Integer.parseInt(input);
                        if(amount < 0){
                            throw new Exception();
                        }
                        Coin.updateCoinStock(String.valueOf(displayAmount()), -1 *amount);
                        displayAmount();
                    }catch (Exception exception){
                        JOptionPane.showMessageDialog(OwnerToCashier.this,
                                "Sorry, Invalid Input!",
                                "Amount Error!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }else if(fillButton.isSelected()){
                    try{
                        String input = textField.getText();
                        int amount = Integer.parseInt(input);
                        if(amount < 0){
                            throw new Exception();
                        }
                        Coin.updateCoinStock(String.valueOf(displayAmount()), amount);
                        displayAmount();
                    }catch (Exception exception){
                        JOptionPane.showMessageDialog(OwnerToCashier.this,
                                "Sorry, Invalid Input!",
                                "Amount Error!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(OwnerToCashier.this,
                            "Sorry, Please select a behaviour",
                            "NO thing select!",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        generateTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().open(new File("transactions.csv"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        }
        );

    }

    public double displayAmount(){
        HashMap<Double, Integer> coins = Coin.getCurrentAmount();

        String cashString = (String) kinds.getItemAt(kinds.getSelectedIndex());
        double cash = 0;

        switch(cashString) {
            case "100$":
                cash = 100;
                break;
            case "50$":
                cash = 50;
                break;
            case "20$":
                cash = 20;
                break;
            case "10$":
                cash = 10;
                break;
            case "5$":
                cash = 5;
                break;
            case "10c":
                cash = 0.1;
                break;
            case "5c":
                cash = 0.05;
                break;
            default:
                // code block
        }

        currentAmount.setText(String.format("Current Amount : %d", coins.get(cash)));

        return cash;
    }


    public GroupLayout addGroupLayout(){
        GroupLayout gl_contentPane = new GroupLayout(contentPane);

        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup()
                        .addComponent(pageTitle, GroupLayout.Alignment.CENTER)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(20)
                                .addComponent(cashKind)
                        )
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(20)
                                .addComponent(kinds)
                                .addGap(40)
                                .addComponent(currentAmount)
                                .addGap(120)
                        )
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(20)
                                .addComponent(reduceButton)
                                .addGap(30)
                                .addComponent(fillButton)
                                .addGap(120)
                        )
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(20)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(amountLabel)
                                        .addComponent(textField)
                                )
                                .addGap(40)
                                .addComponent(confirm)
                                .addGap(120)
                        )
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(20)
                                .addComponent(generateCashDetailsButton)
                                .addGap(40)
                                .addComponent(generateTransactionButton)
                                .addGap(120)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(back, GroupLayout.Alignment.TRAILING)
                                )
                        )

        );

        gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup()
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(10)
                                .addComponent(pageTitle)
                                .addGap(40)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addComponent(cashKind)
                                                .addComponent(kinds)
                                        )
                                        .addComponent(currentAmount)
                                )
                                .addGap(40)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(reduceButton)
                                        .addComponent(fillButton)
                                )
                                .addGap(20)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addComponent(amountLabel)
                                                .addComponent(textField))
                                        .addComponent(confirm)
                                )
                                .addGap(30)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(generateCashDetailsButton)
                                        .addComponent(generateTransactionButton)
                                        .addComponent(back)
                                )
                                .addGap(20)


                        )

        );

        return gl_contentPane;
    }
}
