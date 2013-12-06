package main_register;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.MathContext;

import model.Person;
import model.Product;
import dao.Dao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GUIMain extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final GridLayout _horz = new GridLayout(1, 0);
	private final GridLayout _vert = new GridLayout(0, 1);
	private final Color _bgColor = Color.GRAY;
	private JMenuItem _inv, _login;
	private JTextArea _messageBox;
	private Dao _dao;
	private Person _loggedIn;

	public GUIMain() {
		_dao = new Dao(null, null);
		_messageBox = new JTextArea("Welcome, Please log in.", 5, 50);
		_messageBox.setBackground(_bgColor);
		_messageBox.setWrapStyleWord(true);
		_messageBox.setLineWrap(true);
		_messageBox.setEditable(false);

		this.add(_messageBox);
		this.init();

		JFrame frame = new JFrame("Storinate!!");
		frame.setLayout(_horz);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(topMenu());
		frame.setContentPane(this);
		frame.pack();
		frame.setVisible(true);
	}

	private void init() {
		// make the main JPanel
		this.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.setPreferredSize(new Dimension(1080, 640));
		this.setBackground(_bgColor);
	}

	/**
	 * Creates the top menu.
	 * 
	 * @return
	 */
	private JMenuBar topMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu log = new JMenu("Log");
		_login = new JMenuItem("Log In");
		_login.addActionListener(this);
		JMenuItem logout = new JMenuItem("Log Out");
		logout.addActionListener(this);
		log.add(_login);
		log.add(logout);
		menuBar.add(log);

		JMenu rep = new JMenu("Reports");
		_inv = new JMenuItem("Inventory");
		_inv.addActionListener(this);
		JMenuItem hrs = new JMenuItem("Time Cards");
		hrs.addActionListener(this);
		rep.add(_inv);
		rep.add(hrs);
		menuBar.add(rep);

		return menuBar;
	}

	/**
	 * the login Screen
	 */
	private void loginScreen() {
		final JTextField id = new JTextField(15);
		id.setBackground(Color.WHITE);
		JButton loginBtn = new JButton("Login");
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_loggedIn = _dao.getPerson(id.getText());
				if (_loggedIn != null) {
					welcomeScreen();
				} else {
					loginScreen();
				}
			}
		});

		this.removeAll();
		this.add(new JLabel("Please enter your employee id:"));
		this.add(id);
		this.add(loginBtn);
		this.validate();
	}

	/**
	 * The screen after logging in
	 */
	private void welcomeScreen() {
		this.removeAll();
		_messageBox.removeAll();
		this.repaint();
		_messageBox.setText("Welcome, " + _loggedIn.get_first_name());

		JButton ringer = new JButton("Start Ringing");
		ringer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ringingScreen();
			}
		});

		this.removeAll();
		this.add(_messageBox);
		this.add(ringer);
		this.validate();
	}

	/**
	 * The ringing screen
	 */
	private void ringingScreen() {
		this.removeAll();
		this.repaint();

		final JTextArea rungThrough = new JTextArea(25, 30);
		rungThrough.setBackground(Color.WHITE);

		final JTextField skuIn = new JTextField(10);
		final JTextField totalList = new JTextField(10);
		totalList.setText("0.00");
		
		skuIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Product newProd = _dao.getProduct(skuIn.getText());
				if (newProd != null) {
					rungThrough.setText(rungThrough.getText() + String.format("%-15s%-6.2f%n",newProd.getName(),newProd.getList().floatValue()));
					skuIn.setText("");					
					totalList.setText(BGAdd(new BigDecimal(totalList.getText()), newProd.getList()));
				}
			}
		});

		JButton CompleteTransaction = new JButton("Complete");
		CompleteTransaction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				totalList.setText("0.00");
				skuIn.setText("");
				rungThrough.setText("");
				
			}
		});
		
		
		this.add(skuIn);
		this.add(rungThrough);
		this.add(totalList);
		this.add(CompleteTransaction);
		this.validate();
	}

	/**
	 * employee report
	 */
	private void getEmployees() {
		// TODO: print out employee stuff
	}

	/**
	 * inventory report
	 */
	private void getInventory() {
		// TODO: print out inventory
	}

	/**
	 * PO producer
	 */
	private void getPO() {
		// TODO: generate PO
	}

	private void updateEmployee(Person emp) {
		// TODO: update person in DB
	}

	private void updateProduct(Product p) {
		// TODO: update prod in DB
	}

	private void addEmployee() {
		// TODO: create person from scratch, add to DB
	}

	private void addProduct() {
		// TODO: create product from scratch, add to DB
	}

	private String BGAdd(BigDecimal bg1, BigDecimal bg2){
		
		return bg1.add(bg2).toPlainString();
	}
	
	/**
	 * Action Listener Stuff
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _inv) {
			String inventory = "";
			String header = String.format("%-15s%-15s%-10s%n", "Name",
					"Quantity", "Cost");
			String inv_template = "%-15s%-15d%-8.2f%n";
			for (Product p : _dao.getProducts()) {
				inventory += String.format(inv_template, p.getName(),
						p.getQuantity(), p.getCost());
			}
			_messageBox.setText(header + inventory);
		} else if (e.getSource() == _login) {
			this.loginScreen();
		}
	}

	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		new GUIMain();
	}
}
