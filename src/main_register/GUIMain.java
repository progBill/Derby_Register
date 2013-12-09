package main_register;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import model.Person;
import model.Product;
import model.shoppingCart;
import dao.Dao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GUIMain extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final GridLayout _horz = new GridLayout(1, 0);
	private final GridLayout _vert = new GridLayout(0, 1);
	private final Color _bgColor = Color.GRAY;
	private JMenuItem _inv, _login, _po, _emps, _person, _sales;
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

		JMenu rep = new JMenu("Reports");
		_inv = new JMenuItem("Inventory");
		_inv.addActionListener(this);
		_po = new JMenuItem("Create PO");
		_po.addActionListener(this);
		_emps = new JMenuItem("View Employees");
		_emps.addActionListener(this);
		_sales = new JMenuItem("Sales Report");
		_sales.addActionListener(this);

		rep.add(_emps);
		rep.add(_inv);
		rep.add(_po);
		rep.add(_sales);


		JMenu hr = new JMenu("Human Resources");
		_person = new JMenuItem("Inspect Individual");
		_person.addActionListener(this);
		hr.add(_person);
		
		menuBar.add(log);
		menuBar.add(rep);
		menuBar.add(hr);
		
		return menuBar;
	}

	/**
	 * the login Screen
	 */
	private void loginScreen() {
		this.removeAll();
		
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
		this.repaint();
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
		this.repaint();
	}

	/**
	 * The ringing screen
	 */
	private void ringingScreen() {
		this.removeAll();
		this.repaint();

		final JTextArea rungThrough = new JTextArea(25, 30);
		rungThrough.setBackground(Color.WHITE);
		rungThrough.setFocusable(false);
		final shoppingCart cart = new shoppingCart();

		final JTextField skuIn = new JTextField(10);
		final JTextField totalList = new JTextField(10);
		totalList.setText("0.00");
		totalList.setFocusable(false);

		skuIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Product newProd = _dao.getProduct(skuIn.getText());
				if (newProd != null) {
					rungThrough.setText(rungThrough.getText()
							+ String.format("%-15s%-6.2f%n", newProd.getName(), newProd.getList().floatValue()));
					cart.addItem(newProd);
					skuIn.setText("");
					totalList.setText(BGAdd(new BigDecimal(totalList.getText()), newProd.getList()));
				}
			}
		});

		JButton CompleteTransaction = new JButton("Complete");
		CompleteTransaction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				totalList.setText("0.00");
				skuIn.setText("");
				rungThrough.setText("");
				_dao.completeSale(_loggedIn, cart);
			}
		});

		this.add(skuIn);
		this.add(rungThrough);
		this.add(totalList);
		this.add(CompleteTransaction);
		this.validate();
		this.repaint();
	}

	/**
	 * employee report
	 */
	private void getEmployees() {
		this.removeAll();
		_messageBox.removeAll();
		_messageBox.setText(_dao.getAllEmployees());
		this.add(_messageBox);
		this.validate();
		this.repaint();
	}

	private void salesReport(){
		this.removeAll();
		_messageBox.removeAll();
		final JPanel contentBox = new JPanel();

		JLabel name= new JLabel("Enter Employee ID:");
		final JTextField id = new JTextField(15);
		id.setBackground(Color.WHITE);
		id.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentBox.removeAll();
				_messageBox.setText(_dao.salesReport(id.getText()));
				contentBox.add(_messageBox);
				validate();
				repaint();
			}
		});

		contentBox.add(name);
		contentBox.add(id);

		this.add(contentBox);
		this.validate();
		this.repaint();
	}
	/**
	 * inventory report
	 */
	private void getInventory() {
		this.removeAll();
		_messageBox.setText("");
		_messageBox.removeAll();

		String header = String.format("%-15s%-15s%-15s%-15s%n", "Name", "Quantity", "Reorder Level", "Cost");
		
		_messageBox.setText(header + _dao.getProductsAsString());
		this.add(_messageBox);
		this.validate();
		this.repaint();
	}

	/**
	 * PO producer
	 */
	private void createPO() {
		this.removeAll();
		_messageBox.removeAll();
		String po = _dao.createPO();
		if (po.equals("")) {
			_messageBox.setText("Nothing to Order!");
		} else {
			_messageBox.setText(po);
		}
		this.add(_messageBox);
		this.validate();
		this.repaint();
	}

	private void updateEmployee() {
		this.removeAll();
		_messageBox.removeAll();

		JTextArea rightCol = new JTextArea(10,15);
		rightCol.setLayout(_vert);
		rightCol.setBackground(_bgColor);
		rightCol.setEditable(false);

		JTextArea leftCol = new JTextArea(10, 15);
		leftCol.setLayout(_vert);
		leftCol.setBackground(_bgColor);
		leftCol.setEditable(false);

		final JTextField firstTxt = new JTextField(20);
		final JTextField lastTxt = new JTextField(20);
		final JTextField hourlyTxt= new JTextField(20);
		final JTextField socialTxt= new JTextField(20);
		final JTextField birthTxt = new JTextField(20);
		final JTextField hireDateTxt = new JTextField(20);
		final JTextField streetTxt = new JTextField(20);
		final JTextField townTxt = new JTextField(20);
		final JTextField stateTxt = new JTextField(20);
		final JTextField zipTxt = new JTextField(20);

		JLabel firstLbl = new JLabel("First Name:");
		JLabel lastLbl = new JLabel("Last Name:");
		JLabel hourlyLbl = new JLabel("Hourly Rate:");
		JLabel socialLbl= new JLabel("Social Security:");
		JLabel birthLbl = new JLabel("Birth Date:");
		JLabel hireDateLbl = new JLabel("Hire Date:");
		JLabel streetLbl = new JLabel("Street:");
		JLabel townLbl = new JLabel("Town:");
		JLabel stateLbl = new JLabel("State:");
		JLabel zipLbl = new JLabel("zip Code:");
		leftCol.add(firstLbl);
		leftCol.add(lastLbl);
		leftCol.add(hourlyLbl);
		leftCol.add(socialLbl);
		leftCol.add(birthLbl);
		leftCol.add(hireDateLbl);
		leftCol.add(streetLbl);
		leftCol.add(townLbl);
		leftCol.add(stateLbl);
		leftCol.add(zipLbl);

		rightCol.add(firstTxt);
		rightCol.add(lastTxt);
		rightCol.add(hourlyTxt);
		rightCol.add(socialTxt);
		rightCol.add(birthTxt);
		rightCol.add(hireDateTxt);
		rightCol.add(streetTxt);
		rightCol.add(townTxt);
		rightCol.add(stateTxt);
		rightCol.add(zipTxt);

		JButton search = new JButton("Find Employee");
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] inputFields = {firstTxt.getText(), lastTxt.getText(), hourlyTxt.getText(), socialTxt.getText(),
						birthTxt.getText(), hireDateTxt.getText(), streetTxt.getText(), townTxt.getText(), stateTxt.getText(), zipTxt.getText()};

				Person p = _dao.findPerson(inputFields);

				firstTxt.setText(p.get_first_name());
				lastTxt.setText(p.get_last_name());
				hourlyTxt.setText(p.get_hourly_rate());
				socialTxt.setText(p.get_social());
				birthTxt.setText(p.get_birth());
				hireDateTxt.setText(p.get_hire_date());
				streetTxt.setText("");
				townTxt.setText("");
				stateTxt.setText("");
				zipTxt.setText("");
				}
		});


		JButton commit = new JButton("Commit Changes");
		commit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO: if person's data has changed, update
			}
		});


		this.add(leftCol);
		this.add(rightCol);
		this.add(commit);
		this.add(search);
		this.validate();
		this.repaint();
		
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

	private String BGAdd(BigDecimal bg1, BigDecimal bg2) {

		return bg1.add(bg2).toPlainString();
	}

	
	private JTextField txtFactory(){
		return new JTextField(20);
	}
	
	/**
	 * Action Listener Stuff
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _inv) {
			this.getInventory();
		} else if (e.getSource() == _login) {
			this.loginScreen();
		} else if (e.getSource() == _po) {
			this.createPO();
		} else if (e.getSource() == _emps) {
			this.getEmployees();
		} else if (e.getSource() == _person) {
			this.updateEmployee();
		} else if (e.getSource() == _sales) {
			this.salesReport();
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
