package R18A_Group7_Asm2;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogIn extends JFrame {
	static LogIn frame;
	JPanel contentPane;
	JTextField textField;
	JPasswordField passwordField;

//	private Date startTime;
//	private Timer timer;
//	private DateFormat timeFormat;



	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new LogIn();
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
	public LogIn() {

		Timer timer = Timer.getInstance();
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);


		JLabel loginLabel = new JLabel("Login");
		loginLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		loginLabel.setForeground(Color.DARK_GRAY);
		
		JLabel name = new JLabel("Name:");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JLabel password = new JLabel("Password:");
		
		passwordField = new JPasswordField();
		
		JButton loginButton = new JButton("login");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name=textField.getText();
				String password=String.valueOf(passwordField.getPassword());

				boolean exist = User.userExist(name,password);

				if(exist){

					if(User.isCustomer(name)){
						String args[] = new String[]{name};
						UserLoggedVendingMachine.main(args);
						frame.dispose();
						VendingMachine.frame.dispose();

						timer.setCurrentFrame("lvend");
					}else if(User.isSeller(name)){
						String args[] = new String[]{};
						SellerAdmin.main(args);
						frame.dispose();
						VendingMachine.frame.dispose();

						timer.setCurrentFrame("se");
					}else if(User.isCashier(name)){
						String args[] = new String[]{};
						CashierAdmin.main(args);
						frame.dispose();
						VendingMachine.frame.dispose();

						timer.setCurrentFrame("ca");
					}else if(User.isOwner(name)){
						String args[] = new String[]{};
						OwnerAdmin.main(args);
						frame.dispose();
						VendingMachine.frame.dispose();

						timer.setCurrentFrame("oa");

					}

				}else{
					JOptionPane.showMessageDialog(LogIn.this,
							"Sorry, Wrong password or account!",
							"Login Error!",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		
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
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(28)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(password)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(name)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(76)
											.addComponent(loginLabel))
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(54)
											.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
												.addComponent(passwordField)
												.addComponent(textField, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)))))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(158)
							.addComponent(loginButton, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
							.addGap(52)
							))
					.addContainerGap(78, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(loginLabel)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(name)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(password)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(loginButton, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
						)
					.addContainerGap(96, Short.MAX_VALUE)
					)
		);
		contentPane.setLayout(gl_contentPane);
	}

//	public void timerListener(ActionEvent e) {
//		Date actualTime = new Date();
//		String time = timeFormat.format(new Date(actualTime.getTime() - startTime.getTime()));
//
//		if(time.equals("02:00")) {
//			System.out.println("time over, order reset!");
//			timer.stop();
//			frame.dispose();
//			VendingMachine.startTime = new Date();
//			VendingMachine.timer.restart();
//
//		}
//	}

}
