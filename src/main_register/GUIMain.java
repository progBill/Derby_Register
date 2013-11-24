package main_register;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dao.Dao;

import javax.swing.*;

public class GUIMain extends JPanel implements ActionListener{

	public GUIMain() {
		Dao dao = new Dao(null, null);
		JFrame frame = new JFrame("Storinate!!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(this);
		this.init();
		frame.setJMenuBar(topMenu());
		frame.pack();
		frame.setVisible(true);
	}

	private void init() {
		// make the main JPanel
		this.setPreferredSize(new Dimension(1080, 640));
		this.setBackground(Color.GRAY);
		this.setLayout(null);
	}

	private JMenuBar topMenu(){
		JMenuBar menuBar = new JMenuBar();
		JMenu log = new JMenu("Log");
		JMenuItem login = new JMenuItem("Log In");
		login.addActionListener(this);
		JMenuItem logout = new JMenuItem("Log Out");
		logout.addActionListener(this);
		log.add(login);
		log.add(logout);
		menuBar.add(log);
		
		JMenu rep = new JMenu("Reports");
		JMenuItem inv = new JMenuItem("Inventory");
		inv.addActionListener(this);
		JMenuItem hrs = new JMenuItem("Time Cards");
		hrs.addActionListener(this);
		rep.add(inv);
		rep.add(hrs);
		menuBar.add(rep);

		return menuBar;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
