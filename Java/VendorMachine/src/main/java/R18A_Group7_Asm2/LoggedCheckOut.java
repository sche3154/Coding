package R18A_Group7_Asm2;

import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class LoggedCheckOut extends JFrame {

    static LoggedCheckOut frame;
    static String userName = "Null";

    private JPanel contentPane;

//    private Date startTime;
//    private Timer timer;
//    private DateFormat timeFormat;


    private JLabel totalAmountLabel;
    private JLabel payByCash;
    private JLabel payByCard;
    private JLabel timeLabel;

    private JComboBox cashKinds;

    JTextField accountField;
    JTextField cardField;

    JTextField enterAmount;


    JButton cancelButton = new JButton("Cancel Transaction");
    JButton enterButton = new JButton("Enter");
    JButton proceedButton = new JButton("Proceed");
    JButton saveBankAccount = new JButton("Save Bank Account");

    private static double amount = 0;
    double money_paid = 0;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    if(args.length != 0){
                        userName = args[0];

                        amount = Double.parseDouble(args[1]);

                    }
                    frame = new LoggedCheckOut();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public LoggedCheckOut(){
        Timer timer = Timer.getInstance();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 580, 380);
        contentPane = new JPanel();
        contentPane.setForeground(Color.DARK_GRAY);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        String str;

        if (!userName.equals("Null")){
            str = String.format("%s needs to pay : %.2f", userName,amount);
        }else {
            str = String.format("Amount need to pay : %.2f", amount);
        }

        totalAmountLabel = new JLabel(str);
        totalAmountLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OwnerSummary ownerSummary = OwnerSummary.getInstance();
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                ownerSummary.addTransaction(simpleDateFormat.format(date), "user cancelled", userName);
                try{
//                    timer.stop();
                    frame.dispose();
//                    UserLoggedVendingMachine.startTime = new Date();
//                    UserLoggedVendingMachine.timer.restart();

                    String[] args = new String[]{};
                    VendingMachine.main(args);
                    timer.setCurrentFrame("vend");
                }catch(NullPointerException exception){
                    exception.printStackTrace();
                }

            }
        });



//        timeLabel = new JLabel();
////
//        startTime = new Date();
//        timeFormat = new SimpleDateFormat("mm:ss");
//        timer = new Timer(1000, this::timerListener);
//        // to make sure it doesn't wait one second at the start
//        timer.setInitialDelay(0);
//        timer.start();


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

                    if (cashString == "100$"){
                        cash = 100;
                    }


                    amount -= amountInt*cash;
                    money_paid += amountInt*cash;
                    if (amount <= 0) {
                        HashMap<Double, Integer> change =  Coin.getChange(-amount);

                        if(change.isEmpty()){
                            JOptionPane.showMessageDialog(LoggedCheckOut.this,
                                    "Sorry, Not enough Coin!",
                                    "Coin Error!",
                                    JOptionPane.ERROR_MESSAGE);

                            OwnerSummary ownerSummary = OwnerSummary.getInstance();
                            Date date = new Date();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                            ownerSummary.addTransaction(simpleDateFormat.format(date), "change not available", userName);

                            ownerSummary.writeIntoHistory();

                            LoggedCheckOut.frame.dispose();
                            timer.setCurrentFrame("vend");
                            VendingMachine.main(new String[]{});
                        }else{
                            String args[] = new String[]{String.valueOf(change)};

                            OrderCompleted.main(args);
                            frame.dispose();
                            timer.setCurrentFrame("oc");

                            if(!userName.equals("Null")){
                                LoggedPurchaseSum.UserPurchase(UserLoggedVendingMachine.tableModel, userName);
                            }

                            double return_change = -amount;
                            TransactionsReport.write_to_file(
                                    TransactionsReport.get_current_date(),
                                    TransactionsReport.item_list(UserLoggedVendingMachine.tableModel),
                                    money_paid,
                                    return_change,
                                    "Cash");

                            AddToBag.reduce_stock(UserLoggedVendingMachine.tableModel);
                        }

                    }


                    String str = String.format("Remained to pay : %.2f", amount);

                    remainedLabel.setText(str);

                }catch (Exception exception){
                    JOptionPane.showMessageDialog(LoggedCheckOut.this,
                            "Sorry, Invalid Input!",
                            "Amount Error!",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });



        payByCard = new JLabel("Pay by Card");

        JLabel accountLabel = new JLabel("Account Name");

        if(!userName.equals("Null")){

            String[] cardDetails = User.getUserCard(userName);

            if(cardDetails != null){
                accountField = new JTextField(cardDetails[0]);
                cardField = new JTextField(cardDetails[1]);
            }else{
                accountField = new JTextField();
                cardField = new JTextField();
            }

        }else{
            accountField = new JTextField();
            cardField = new JTextField();
        }



        JLabel cardLabel = new JLabel("Card Number");





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

                    timer.setCurrentFrame("oc");

                    if(!userName.equals("Null")){
                        LoggedPurchaseSum.UserPurchase(UserLoggedVendingMachine.tableModel, userName);
                    }

                    TransactionsReport.write_to_file(
                            TransactionsReport.get_current_date(),
                            TransactionsReport.item_list(UserLoggedVendingMachine.tableModel),
                            AddToBag.get_totalprice(UserLoggedVendingMachine.tableModel),
                            0,
                            "Credit card");

                    AddToBag.reduce_stock(UserLoggedVendingMachine.tableModel);

                }else{
                    JOptionPane.showMessageDialog(LoggedCheckOut.this,
                            "Sorry, username or password error!",
                            "Proceed error!",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        saveBankAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean exist = false;
                try {
                    exist = CreditCard.cardExist(accountField.getText(), cardField.getText());
                } catch (IOException | ParseException ioException) {
                    ioException.printStackTrace();
                }

                if(exist == true && !userName.equals("Null")){
                    try {
                        User.saveBankDetail(userName, accountField.getText(), cardField.getText());

                        JOptionPane.showMessageDialog(LoggedCheckOut.this,
                                "Saved Success!",
                                "Save Card!",
                                JOptionPane.PLAIN_MESSAGE);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }else{
                    JOptionPane.showMessageDialog(LoggedCheckOut.this,
                            "Can not save unExisted card!",
                            "Card error!",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.CENTER)
//                .addComponent(timeLabel)
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
                                .addGroup(gl_contentPane.createSequentialGroup()
                                        .addGap(20)
                                        .addComponent(saveBankAccount)
                                        .addGap(300)
                                        .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                .addComponent(proceedButton, GroupLayout.Alignment.TRAILING)
                                        )
                                )

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
                                                .addComponent(saveBankAccount)
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

        ownerSummary.addTransaction(simpleDateFormat.format(date), "time out", userName);

        ownerSummary.writeIntoHistory();
    }
}

