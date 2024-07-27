package VendorMachine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;

public class UserLoggedVendingMachine extends JFrame{

    static String userName = "Null";

    static UserLoggedVendingMachine frame;

    private JPanel contentPane;


    static Date startTime;
    static Timer timer;
    static double checkoutPrice = 0;


    private DateFormat timeFormat;


    JLabel userTitle;

    JLabel drinks;
    JLabel chocolates;
    JLabel chips;
    JLabel candies;
    JLabel timeLabel;

    //  ##### Mineral Water, Sprite, Coca cola, Pepsi, Juice. ##### //

    JComboBox cb1;
    //  ##### Mars, M&M, Bounty, Snickers ##### //
    JComboBox cb2;
    //  #####  Smiths, Pringles, Kettle, Thins ##### //
    JComboBox cb3;
    //  #####  Mentos, Sour Patch, Skittles ##### //
    JComboBox cb4;

    JTextField textField1;
    JTextField textField2;
    JTextField textField3;
    JTextField textField4;

    JLabel quantity1 = new JLabel("Quantity");
    JLabel quantity2 = new JLabel("Quantity");
    JLabel quantity3 = new JLabel("Quantity");
    JLabel quantity4 = new JLabel("Quantity");

    JLabel last_items_Bought = new JLabel("LAST BOUGHT 5 ITEMS");
    JTextArea last_items = new JTextArea();
    JButton addToBag = new JButton("Add To Bag");
    JLabel shoppingBag = new JLabel("Shopping Bag");

    JButton log_out = new JButton("Log Out");

//    JButton sign_in = new JButton("Sign In");

    JButton checkout = new JButton("Check Out");

    static DefaultTableModel tableModel;
    JTable shoppingItems;
    JScrollPane scrollPane;

    GroupLayout gl_contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    if(args.length != 0){
                        userName = args[0];
                        System.out.println(userName);
                    }
                    frame = new UserLoggedVendingMachine();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
    public UserLoggedVendingMachine() {
//        Font font = new Font("宋体",Font.PLAIN,12);
//        InitGlobalFont(font);
        Timer timer = Timer.getInstance();


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 600);
        contentPane = new JPanel();
        contentPane.setForeground(Color.DARK_GRAY);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);


        if(!userName.equals("Null")){
            userTitle = new JLabel(String.format("Hi, %s! Welcome Back!", userName));
        }else{
            userTitle = new JLabel("Vending Machine");
        }

        userTitle.setFont(new Font("Tahoma", Font.PLAIN, 20));

        drinks = new JLabel("Drinks");
        chocolates = new JLabel("Chocolates");
        chips = new JLabel("Chips");
        candies = new JLabel("Candies");

        //      Timer init
//        timeLabel = new JLabel();
////        add(timeLabel);
//        startTime = new Date();
//        timeFormat = new SimpleDateFormat("mm:ss");
//        timer = new Timer(1000, this::timerListener);
//        // to make sure it doesn't wait one second at the start
//        timer.setInitialDelay(0);
//        timer.start();

        //  ##### Mineral Water, Sprite, Coca cola, Pepsi, Juice. ##### //
        String kinds[]={"Mineral Water","Sprite","Coca cola","Pepsi","Juice"};
        cb1 = new JComboBox(kinds);
        //  ##### Mars, M&M, Bounty, Snickers ##### //
        String kinds2[]={"Mars","M&M","Bounty","Snickers"};
        cb2 = new JComboBox(kinds2);
        //  #####  Smiths, Pringles, Kettle, Thins ##### //
        String kinds3[]={"Smiths","Pringles","Kettle","Thins"};
        cb3 = new JComboBox(kinds3);
        //  #####  Mentos, Sour Patch, Skittles ##### //
        String kinds4[]={"Mentos","Sour Patch","Skittles"};
        cb4 = new JComboBox(kinds4);


        textField1 = new JTextField();
        textField2 = new JTextField();
        textField3 = new JTextField();
        textField4 = new JTextField();

        if(!userName.equals("Null")){
            LoggedDisplay.display(last_items, userName);
        }


        String[][] data = {};
        String[] columnNames = {"Item(s)", "Quantity", "Price"};
        tableModel = new DefaultTableModel(data, columnNames);
        shoppingItems = new JTable(tableModel);
        scrollPane = new JScrollPane(shoppingItems);
        shoppingItems.setFillsViewportHeight(true);
        Dimension d = shoppingItems.getPreferredSize();
        scrollPane.setPreferredSize(
                new Dimension(d.width,shoppingItems.getRowHeight()*tableModel.getRowCount()+1));
        addToBag.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                AddToBag atb = new AddToBag();
                atb.addText(textField1, cb1, tableModel);
                atb.addText(textField2, cb2, tableModel);
                atb.addText(textField3, cb3, tableModel);
                atb.addText(textField4, cb4, tableModel);
                checkoutPrice = atb.set_totalprice(tableModel);
            }
        });


        log_out.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
//                timer.stop();
//                startTime = new Date();
                if(frame != null){
                    frame.dispose();
                }

//                VendingMachine.timer.stop();
//                VendingMachine.startTime = new Date();
                String[] args = new String[]{};
                VendingMachine.main(args);
//                VendingMachine.timer.restart();
                timer.setCurrentFrame("vend");

            }
        });



        checkout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(!userName.equals("Null")){
                    LoggedCheckOut.main(new String[]{userName, String.valueOf(checkoutPrice)});
                }else{
                    LoggedCheckOut.main(new String[]{});
                }


                if(frame != null){
                    frame.dispose();

                }

                timer.setCurrentFrame("lc");
//                VendingMachine.startTime = new Date();
//                startTime = new Date();
//                timer.restart();

            }
        });


        gl_contentPane = addGroupLayout();

        contentPane.setLayout(gl_contentPane);

    }

    public GroupLayout addGroupLayout(){
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(gl_contentPane.createSequentialGroup()
//                                .addGap(20)
//                                .addGroup(gl_contentPane.createParallelGroup()
//                                        .addComponent(timeLabel)
//                                )
                        )
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(20)
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(userTitle)
                                )
                        )
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(20)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(drinks)
                                        .addComponent(cb1)
                                        .addComponent(chocolates)
                                        .addComponent(cb2)
                                        .addComponent(chips)
                                        .addComponent(cb3)
                                        .addComponent(candies)
                                        .addComponent(cb4)
                                )
                                .addGap(25)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(quantity1)
                                        .addComponent(textField1)
                                        .addComponent(quantity2)
                                        .addComponent(textField2)
                                        .addComponent(quantity3)
                                        .addComponent(textField3)
                                        .addComponent(quantity4)
                                        .addComponent(textField4)
                                )
                                .addGap(70)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(last_items_Bought)
                                        .addComponent(last_items)
                                        .addComponent(addToBag)
                                        .addComponent(shoppingBag)
                                        .addComponent(scrollPane)
                                )
                                .addGap(50)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(log_out)
                                        .addComponent(checkout)
                                )
                                .addGap(20)
                        )

        );
        gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup()
                        .addGroup(gl_contentPane.createSequentialGroup()
                                        .addGroup(gl_contentPane.createParallelGroup()
//                                        .addGroup(gl_contentPane.createSequentialGroup()
//                                                .addComponent(timeLabel)
//                                        )
                                        )
                                        .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                                                .addGroup(gl_contentPane.createSequentialGroup()
                                                        .addComponent(userTitle)
                                                )
                                        )
                                        .addGroup(gl_contentPane.createParallelGroup()
                                                .addGroup(gl_contentPane.createSequentialGroup()
                                                        .addGap(50)
                                                        .addComponent(drinks)
                                                        .addGap(10)
                                                        .addComponent(cb1)
                                                        .addGap(30)
                                                        .addComponent(chocolates)
                                                        .addGap(10)
                                                        .addComponent(cb2)
                                                        .addGap(30)
                                                        .addComponent(chips)
                                                        .addGap(10)
                                                        .addComponent(cb3)
                                                        .addGap(30)
                                                        .addComponent(candies)
                                                        .addGap(10)
                                                        .addComponent(cb4)
                                                        .addGap(20)
                                                )
                                                .addGroup(gl_contentPane.createSequentialGroup()
                                                        .addGap(50)
                                                        .addComponent(quantity1)
                                                        .addGap(10)
                                                        .addComponent(textField1)
                                                        .addGap(30)
                                                        .addComponent(quantity2)
                                                        .addGap(10)
                                                        .addComponent(textField2)
                                                        .addGap(30)
                                                        .addComponent(quantity3)
                                                        .addGap(10)
                                                        .addComponent(textField3)
                                                        .addGap(30)
                                                        .addComponent(quantity4)
                                                        .addGap(10)
                                                        .addComponent(textField4)
                                                        .addGap(20)
                                                )
                                                .addGroup(gl_contentPane.createSequentialGroup()
                                                        .addGap(50)
                                                        .addComponent(last_items_Bought)
                                                        .addGap(10)
                                                        .addComponent(last_items)
                                                        .addGap(20)
                                                        .addComponent(addToBag)
                                                        .addGap(20)
                                                        .addComponent(shoppingBag)
                                                        .addGap(10)
                                                        .addComponent(scrollPane)
                                                        .addGap(20)

                                                )
                                                .addGroup(gl_contentPane.createSequentialGroup()
                                                        .addGap(150)
                                                        .addComponent(log_out)
                                                        .addGap(50)
                                                        .addGap(50)
                                                        .addComponent(checkout)
                                                        .addGap(100)
                                                )
                                        )
                        )
        );



        return gl_contentPane;
    }

}

