package VendorMachine;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VendingMachine extends JFrame{

    static VendingMachine frame;
    private JPanel contentPane;

//    static Date startTime;
//    static Timer timer;

    static double checkoutPrice = 0;

    JLabel vendingMachine;

    JLabel drinks;
    JLabel chocolates;
    JLabel chips;
    JLabel candies;
//    JLabel timeLabel;

    //  ##### Mineral Water, Sprite, Coca cola, Pepsi, Juice. ##### //

    JComboBox cb1;
    //  ##### Mars, M&M, Bounty, Snickers ##### //
    JComboBox cb2;
    //  #####  Smiths, Pringles, Kettle, Thins ##### //
    JComboBox cb3;
    //  #####  Mentos, Sour Patch, Skittles ##### //
    JComboBox cb4;

    static JTextField textField1;
    static JTextField textField2;
    static JTextField textField3;
    static JTextField textField4;

    JLabel quantity1 = new JLabel("Quantity");
    JLabel quantity2 = new JLabel("Quantity");
    JLabel quantity3 = new JLabel("Quantity");
    JLabel quantity4 = new JLabel("Quantity");

    JLabel last_items_Bought = new JLabel("LAST BOUGHT 5 ITEMS");
    JTextArea last_items = new JTextArea();
    JButton addToBag = new JButton("Add To Bag");
    JLabel shoppingBag = new JLabel("Shopping Bag");

    JButton log_in = new JButton("Log In");

    JButton sign_in = new JButton("Sign Up");

    JButton checkout = new JButton("Check Out");


    static DefaultTableModel tableModel;
    static JTable shoppingItems;
    JScrollPane scrollPane;

    GroupLayout gl_contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    frame = new VendingMachine();
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
    public VendingMachine() {
//        Font font = new Font("宋体",Font.PLAIN,12);
//        InitGlobalFont(font);
        Timer timer = Timer.getInstance();


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 600);
        contentPane = new JPanel();
        contentPane.setForeground(Color.DARK_GRAY);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);


        vendingMachine = new JLabel("Vending Machine");
        vendingMachine.setFont(new Font("Tahoma", Font.PLAIN, 20));

        drinks = new JLabel("Drinks");
        chocolates = new JLabel("Chocolates");
        chips = new JLabel("Chips");
        candies = new JLabel("Candies");




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

        DisplayLastItems.display(last_items);

        String[][] data = {};
        String[] columnNames = {"Item(s)", "Quantity", "Price"};
        tableModel = new DefaultTableModel(data, columnNames);
        shoppingItems = new JTable(tableModel);
        // set the text center
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        shoppingItems.setDefaultRenderer(Object.class, tcr);
        //set the pane
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

                System.out.println(checkoutPrice);
            }
        });


        log_in.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String[] args = new String[]{};
                LogIn.main(args);
                timer.setCurrentFrame("log");

//                startTime = new Date();
//                timer.restart();

            }

        });

        sign_in.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String args[] = new String[]{};
                SignUp.main(args);

                timer.setCurrentFrame("sign");
//                startTime = new Date();
//                timer.restart();
            }
        });


        checkout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                CheckOut.main(new String[]{});
                if(frame != null){
                    frame.dispose();
                }
                timer.setCurrentFrame("ch");
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
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.CENTER)
                                        .addComponent(vendingMachine)
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
                                        .addComponent(log_in)
                                        .addComponent(sign_in)
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
//                                            .addComponent(timeLab
                                        )
                                        .addGroup(gl_contentPane.createParallelGroup(Alignment.CENTER)
                                                .addGroup(gl_contentPane.createSequentialGroup()
                                                        .addComponent(vendingMachine)
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
                                                        .addComponent(log_in)
                                                        .addGap(50)
                                                        .addComponent(sign_in)
                                                        .addGap(50)
                                                        .addComponent(checkout)
                                                        .addGap(100)
                                                )
                                        )
                        )
        );



        return gl_contentPane;
    }



    public static void reset() {
        textField1.setText(null);
        textField2.setText(null);
        textField3.setText(null);
        textField4.setText(null);

        while (tableModel.getRowCount() != 0) {
            tableModel.removeRow(0);
        }
    }

}

