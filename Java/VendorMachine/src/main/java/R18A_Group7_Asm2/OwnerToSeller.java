package R18A_Group7_Asm2;

import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class OwnerToSeller extends JFrame {
    static OwnerToSeller frame;
    private JPanel contentPane;
    GroupLayout gl_contentPane;
    public JComboBox comboBox1;
    public JComboBox comboBox2;
    public ComboBoxModel[] items = new ComboBoxModel[4];

    JLabel modifyItems = new JLabel("Seller Admin Page");
    JLabel categories = new JLabel("Categories");
    JLabel itemLabel = new JLabel("Items");
    JLabel stockLabel = new JLabel();
    JLabel priceLabel = new JLabel();
    JLabel categoryLabel = new JLabel();
    JLabel nameLabel = new JLabel();
    JLabel itemCodeLabel = new JLabel();
    JLabel functionLabel = new JLabel("Please Choose an Aspect to modify");

    JTextField textField = new JTextField();


    public JComboBox modification;

    JButton confirmButton = new JButton("Confirm");

    JButton generateItemDetailsButton = new JButton("Items Details Report");
    JButton generateItemSalesButton = new JButton("Items Sales Report");

    JButton back = new JButton("Back");

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frame = new OwnerToSeller();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public OwnerToSeller(){
        Timer timer = Timer.getInstance();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 485);
        contentPane = new JPanel();
        contentPane.setForeground(Color.DARK_GRAY);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        modifyItems.setFont(new Font("Tahoma", Font.PLAIN, 20));

        String drinkStr[] = new String[]{"Mineral Water", "Sprite", "Coca cola", "Pepsi", "Juice"};
        items[0] = new DefaultComboBoxModel(drinkStr);

        String chocStr[] = new String[]{"Mars","M&M","Bounty","Snickers"};
        items[1] = new DefaultComboBoxModel(chocStr);

        String chipStr[] = new String[]{"Smiths","Pringles","Kettle","Thins"};
        items[2] = new DefaultComboBoxModel(chipStr);

        String candiesStr[] = new String[]{"Mentos","Sour Patch","Skittles"};
        items[3] = new DefaultComboBoxModel(candiesStr);


        String totalKindsStr[] = new String[]{"Drinks","Chocolates","Chips", "Candies"};
        comboBox1 = new JComboBox(totalKindsStr);

        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = comboBox1.getSelectedIndex();
                comboBox2.setModel(items[i]);

                try {
                    categoryLabel.setText(String.format("Current Category is %s", comboBox1.getSelectedItem()));
                    nameLabel.setText(String.format("Current Name is: %s", comboBox2.getSelectedItem()));
                    priceLabel.setText(String.format("Current Price is %s", Product.getProductPrice((String) comboBox2.getSelectedItem())));
                    itemCodeLabel.setText(String.format("Current Item Code is %s", Product.getProductCode((String) comboBox2.getSelectedItem())));
                    stockLabel.setText(String.format("Current Stock is %d", Product.getProductStock((String) comboBox2.getSelectedItem())));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }

            }
        });

        comboBox2 = new JComboBox(items[0]);


        comboBox2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    categoryLabel.setText(String.format("Current Category is %s", comboBox1.getSelectedItem()));
                    nameLabel.setText(String.format("Current Name is: %s", comboBox2.getSelectedItem()));
                    priceLabel.setText(String.format("Current Price is %s", Product.getProductPrice((String) comboBox2.getSelectedItem())));
                    itemCodeLabel.setText(String.format("Current Item Code is %s", Product.getProductCode((String) comboBox2.getSelectedItem())));
                    stockLabel.setText(String.format("Current Stock is %d", Product.getProductStock((String) comboBox2.getSelectedItem())));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
            }
        });


        try {
            categoryLabel.setText(String.format("Current Category is %s", comboBox1.getSelectedItem()));
            nameLabel.setText(String.format("Current Name is: %s", comboBox2.getSelectedItem()));
            priceLabel.setText(String.format("Current Price is %s", Product.getProductPrice((String) comboBox2.getSelectedItem())));
            itemCodeLabel.setText(String.format("Current Item Code is %s", Product.getProductCode((String) comboBox2.getSelectedItem())));
            stockLabel.setText(String.format("Current Stock is %d", Product.getProductStock((String) comboBox2.getSelectedItem())));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.dispose();

                String[] args = new String[]{};
                OwnerAdmin.main(args);
                timer.setCurrentFrame("oa");
            }
        });


        String behaviour[] = new String[] { "Price", "Item Code", "Stock"};
        modification = new JComboBox(behaviour);

        generateItemDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ItemReport.generate_report();
                    Desktop.getDesktop().open(new File("item_report.csv"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        });

        generateItemSalesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SalesReport.generate_report();
                    Desktop.getDesktop().open(new File("sales_report.csv"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        }
        );
        confirmButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try{
                            if(modification.getItemAt(modification.getSelectedIndex()).equals("Stock")){
                                int input = Integer.parseInt(textField.getText());



                                if(input + Product.getProductStock((String) comboBox2.getSelectedItem()) > 15){
                                    JOptionPane.showMessageDialog(OwnerToSeller.this,
                                            "Sorry, More than 15!",
                                            "Stock Error!",
                                            JOptionPane.ERROR_MESSAGE);
                                }else {
                                    Product.updateProductStock((String) comboBox2.getSelectedItem(), input);
                                }


                            }else if(modification.getItemAt(modification.getSelectedIndex()).equals("Price")){
                                double input = Double.parseDouble(textField.getText());

                                if(input < 0){
                                    JOptionPane.showMessageDialog(OwnerToSeller.this,
                                            "Sorry, Invalid Input!",
                                            "Amount Error!",
                                            JOptionPane.ERROR_MESSAGE);
                                }

                                Product.updateProductPrice((String) comboBox2.getSelectedItem(), textField.getText());
                            }else if(modification.getItemAt(modification.getSelectedIndex()).equals("Item Code")){

                                if(textField.getText().equals("")){
                                    JOptionPane.showMessageDialog(OwnerToSeller.this,
                                            "Sorry, Invalid Input!",
                                            "Amount Error!",
                                            JOptionPane.ERROR_MESSAGE);
                                }else if(Product.checkItemCode(textField.getText())){
                                    JOptionPane.showMessageDialog(OwnerToSeller.this,
                                            "Sorry, Already exist ItemCode!",
                                            "Code Error!",
                                            JOptionPane.ERROR_MESSAGE);
                                }else{
                                    Product.updateProductCode((String) comboBox2.getSelectedItem(), textField.getText());
                                }

                            }

                            categoryLabel.setText(String.format("Current Category is %s", comboBox1.getSelectedItem()));
                            nameLabel.setText(String.format("Current Name is: %s", comboBox2.getSelectedItem()));
                            priceLabel.setText(String.format("Current Price is %s", Product.getProductPrice((String) comboBox2.getSelectedItem())));
                            itemCodeLabel.setText(String.format("Current Item Code is %s", Product.getProductCode((String) comboBox2.getSelectedItem())));
                            stockLabel.setText(String.format("Current Stock is %d", Product.getProductStock((String) comboBox2.getSelectedItem())));
                        }catch (Exception exception){
                            JOptionPane.showMessageDialog(OwnerToSeller.this,
                                    "Sorry, Invalid Input!",
                                    "Amount Error!",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
        );

        gl_contentPane = addGroupLayout();

        contentPane.setLayout(gl_contentPane);

//        startTime = new Date();
//        timeFormat = new SimpleDateFormat("mm:ss");
//        timer = new Timer(1000, this::timerListener);
//        // to make sure it doesn't wait one second at the start
//        timer.setInitialDelay(0);
//        timer.start();


    }


    public GroupLayout addGroupLayout(){

        GroupLayout gl_contentPane = new GroupLayout(contentPane);

        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup()
                        .addComponent(modifyItems, GroupLayout.Alignment.CENTER)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(20)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(categories)
                                        .addComponent(comboBox1)
                                )
                                .addGap(80)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(itemLabel)
                                        .addComponent(comboBox2)
                                )
                                .addGap(160)
                        )
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(20)
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(categoryLabel)
                                        .addComponent(nameLabel)
                                        .addComponent(itemCodeLabel)
                                        .addComponent(priceLabel)
                                        .addComponent(stockLabel)
                                )
                        )
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(20)
                                .addComponent(functionLabel)
                        )

                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(20)
                                .addComponent(modification)
                                .addGap(80)
                                .addComponent(textField)
                                .addGap(40)
                                .addComponent(confirmButton)
                                .addGap(120)
                        )
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(20)
                                .addComponent(generateItemDetailsButton)
                                .addGap(60)
                                .addComponent(generateItemSalesButton)
                                .addGap(20)
                        ).addComponent(back, GroupLayout.Alignment.TRAILING)



        );
        gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup()
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(20)
                                .addComponent(modifyItems)
                                .addGap(40)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addComponent(categories)
                                                .addComponent(comboBox1)
                                        )
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addComponent(itemLabel)
                                                .addComponent(comboBox2)
                                        )
                                )
                                .addGap(20)
                                .addGroup(gl_contentPane.createSequentialGroup()
                                        .addComponent(categoryLabel)
                                        .addComponent(nameLabel)
                                        .addComponent(itemCodeLabel)
                                        .addComponent(priceLabel)
                                        .addComponent(stockLabel)
                                )
                                .addGap(40)
                                .addComponent(functionLabel)
                                .addGap(10)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(modification)
                                        .addComponent(textField)
                                        .addComponent(confirmButton)
                                )
                                .addGap(30)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(generateItemDetailsButton)
                                        .addComponent(generateItemSalesButton)
                                )
                                .addGap(20)
                                .addGroup(gl_contentPane.createParallelGroup()
                                        .addComponent(back, GroupLayout.Alignment.TRAILING)
                                )
                                .addGap(20)
                        )



        );

        return  gl_contentPane;
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
//            String[] args = new String[]{};
//            VendingMachine.main(args);
//
//        }
//
//    }

}
