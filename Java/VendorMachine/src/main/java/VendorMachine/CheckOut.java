package VendorMachine;

import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.renderable.ContextualRenderedImageFactory;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class CheckOut extends JFrame {

    static CheckOut frame;
    private JPanel contentPane;


//    private Date startTime;
//    private Timer timer;
//    private DateFormat timeFormat;


    private JLabel totalAmountLabel;
    private JLabel payByCash;
    private JLabel payByCard;
    private JLabel timeLabel;

    private JComboBox cashKinds;

    JTextField enterAmount;


    JButton cancelButton = new JButton("Cancel Transaction");
    JButton enterButton = new JButton("Enter");
    JButton proceedButton = new JButton("Proceed");

    private static double amount = 0;
    double money_paid = 0;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
//                    amount = Double.parseDouble(args[1]);
                    amount = AddToBag.get_totalprice(VendingMachine.tableModel);
                    frame = new CheckOut();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);


                    System.out.println(amount);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public CheckOut(){
        Timer timer = Timer.getInstance();


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 580, 380);
        contentPane = new JPanel();
        contentPane.setForeground(Color.DARK_GRAY);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        String str;


        str = String.format("Amount need to pay : %.2f", amount);


        totalAmountLabel = new JLabel(str);
        totalAmountLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OwnerSummary ownerSummary = OwnerSummary.getInstance();
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                ownerSummary.addTransaction(simpleDateFormat.format(date),"user cancelled", "anonymous");

                System.out.println(simpleDateFormat.format(date));


                if(frame!= null){
                    frame.dispose();

                }
                String[] args = new String[]{};
                VendingMachine.main(args);
                timer.setCurrentFrame("vend");

            }
        });



        payByCash = new JLabel("Pay by Cash");

        JLabel cashKindLabel = new JLabel("Cash Kind");

        String cash[] = {"100$", "50$", "20$", "10$","5$", "2$", "1$" ,"50c", "20c","10c", "5c", "2c", "1c"};
        cashKinds = new JComboBox(cash);

        JLabel amountLabel = new JLabel("Amount");

        enterAmount = new JTextField();

        String str2 = String.format("Remained to pay : %.2f", amount);
        JLabel remainedLabel = new JLabel(str2);

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String amountString = enterAmount.getText();
                    int amountInt = Integer.parseInt(amountString);
                    String cashString = (String) cashKinds.getItemAt(cashKinds.getSelectedIndex());
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
                        case "2$":
                            cash = 2;
                            break;
                        case "1$":
                            cash = 1;
                            break;
                        case "50c":
                            cash = 0.5;
                            break;
                        case "20c":
                            cash = 0.2;
                            break;
                        case "10c":
                            cash = 0.1;
                            break;
                        case "5c":
                            cash = 0.05;
                            break;
                        case "2c":
                            cash = 0.02;
                            break;
                        case "1c":
                            cash = 0.01;
                            break;
                        default:
                            // code block
                    }


                    amount -= amountInt*cash;
                    money_paid += amountInt*cash;
                    String str = String.format("Remained to pay : %.2f", amount);

                    remainedLabel.setText(str);


                    if (amount <= 0) {
                        HashMap<Double, Integer> change =  Coin.getChange(-amount);

                        if(change.isEmpty()){
                            JOptionPane.showMessageDialog(CheckOut.this,
                                    "Sorry, Not enough Coin!",
                                    "Coin Error!",
                                    JOptionPane.ERROR_MESSAGE);

                            OwnerSummary ownerSummary = OwnerSummary.getInstance();
                            Date date = new Date();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                            ownerSummary.addTransaction(simpleDateFormat.format(date), "change not available", "anonymous");

                            ownerSummary.writeIntoHistory();

                            CheckOut.frame.dispose();
                            timer.setCurrentFrame("vend");
                            VendingMachine.main(new String[]{});
                        }else{
                            String args[] = new String[]{String.valueOf(change)};
                            OrderCompleted.main(args);
                            if(frame != null){
                                frame.dispose();
                            }

                            timer.setCurrentFrame("od");

                            PurchaseSum.UserPurchase(VendingMachine.tableModel);
                            double return_change = -amount;
                            TransactionsReport.write_to_file(
                                    TransactionsReport.get_current_date(),
                                    TransactionsReport.item_list(VendingMachine.tableModel),
                                    money_paid,
                                    return_change,
                                    "Cash");
                            AddToBag.reduce_stock(VendingMachine.tableModel);
                        }

                    }



                }catch (Exception exception){
                    if(amount >= 0){
                        JOptionPane.showMessageDialog(CheckOut.this,
                                "Sorry, Invalid Input!",
                                "Amount Error!",
                                JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        });



        payByCard = new JLabel("Pay by Card");

        JLabel accountLabel = new JLabel("Account Name");

        JTextField accountField = new JTextField();

        JLabel cardLabel = new JLabel("Card Number");

        JTextField cardField = new JTextField();



        proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean exist = false;
                try {
                    exist = CreditCard.cardExist(accountField.getText(), cardField.getText());
                } catch (IOException | ParseException ioException) {
                    ioException.printStackTrace();
                }

                if(exist == true){
                    String args[] = new String[]{};
                    OrderCompleted.main(args);
                    frame.dispose();

                    timer.setCurrentFrame("vend");
                    PurchaseSum.UserPurchase(VendingMachine.tableModel);
                    TransactionsReport.write_to_file(
                            TransactionsReport.get_current_date(),
                            TransactionsReport.item_list(VendingMachine.tableModel),
                            AddToBag.get_totalprice(VendingMachine.tableModel),
                            0,
                            "Credit card");
                    AddToBag.reduce_stock(VendingMachine.tableModel);
                }else{
                    JOptionPane.showMessageDialog(CheckOut.this,
                            "Sorry, username or password error!",
                            "Proceed error!",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.CENTER)
//                .addComponent(timeLabel, GroupLayout.Alignment.CENTER)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addComponent(totalAmountLabel)
                                .addGap(40)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(cancelButton, GroupLayout.Alignment.TRAILING)
                                )
                                .addGap(20)
                        )
                        .addGroup(gl_contentPane.createParallelGroup()
                                .addComponent(payByCash)
                                .addGroup(gl_contentPane.createSequentialGroup()
                                        .addGap(20)
                                        .addGroup(gl_contentPane.createParallelGroup()
                                                .addComponent(cashKindLabel)
                                                .addComponent(cashKinds)
                                        )
                                        .addGap(30)
                                        .addGroup(gl_contentPane.createParallelGroup()
                                                .addComponent(amountLabel)
                                                .addComponent(enterAmount)
                                        )
                                        .addGap(30)
                                        .addComponent(enterButton)
                                        .addGap(20)
                                )
                                .addComponent(remainedLabel)
                        )
                        .addGroup(gl_contentPane.createParallelGroup()
                                .addComponent(payByCard, GroupLayout.Alignment.LEADING)
                                .addGroup(gl_contentPane.createSequentialGroup()
                                        .addGap(20)
                                        .addComponent(accountLabel)
                                        .addGap(30)
                                        .addComponent(accountField)
                                        .addGap(80)
                                )
                                .addGroup(gl_contentPane.createSequentialGroup()
                                        .addGap(20)
                                        .addComponent(cardLabel)
                                        .addGap(35)
                                        .addComponent(cardField)
                                        .addGap(80)
                                )
                                .addComponent(proceedButton, GroupLayout.Alignment.TRAILING)
                        )


        );
        gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup()
                        .addGroup(gl_contentPane.createSequentialGroup()
//                        .addComponent(timeLabel)
                                        .addGroup(gl_contentPane.createParallelGroup()
                                                .addComponent(totalAmountLabel)
                                                .addComponent(cancelButton)
                                        )
                                        .addGap(20)
                                        .addGroup(gl_contentPane.createParallelGroup()
                                                .addComponent(payByCash)
                                        )
                                        .addGap(10)
                                        .addGroup(gl_contentPane.createParallelGroup()
                                                .addGroup(gl_contentPane.createSequentialGroup()
                                                        .addComponent(cashKindLabel)
                                                        .addComponent(cashKinds)
                                                )
                                                .addGroup(gl_contentPane.createSequentialGroup()
                                                        .addComponent(amountLabel)
                                                        .addComponent(enterAmount)
                                                )
                                                .addComponent(enterButton)
                                        )
                                        .addGap(10)
                                        .addComponent(remainedLabel)
                                        .addGap(30)
                                        .addComponent(payByCard)
                                        .addGap(10)
                                        .addGroup(gl_contentPane.createParallelGroup()
                                                .addComponent(accountLabel)
                                                .addComponent(accountField)
                                        )
                                        .addGap(20)
                                        .addGroup(gl_contentPane.createParallelGroup()
                                                .addComponent(cardLabel)
                                                .addComponent(cardField)
                                        )
                                        .addGap(10)
                                        .addGroup(gl_contentPane.createParallelGroup()
                                                .addComponent(proceedButton)
                                        )
                                        .addGap(10)

                        )

        );

        contentPane.setLayout(gl_contentPane);

    }

    public static void addCancelledTransaction() {
        OwnerSummary ownerSummary = OwnerSummary.getInstance();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        ownerSummary.addTransaction(simpleDateFormat.format(date), "time out", "anonymous");

        ownerSummary.writeIntoHistory();
    }

}
