package dao;

import java.sql.*;
import java.util.ArrayList;

import model.Product;

/**
 * CRUDs the database, provides info to all comers.
 * 
 * @author bill
 * 
 */

public class Dao {
	private Connection _conn;

	public Dao(String uname, String pword) {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			_conn = DriverManager.getConnection("jdbc:derby:store", uname,
					pword);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Product> getProds() {
		Statement sql;
		ArrayList<Product> prods = new ArrayList<Product>();

		try {
			sql = _conn.createStatement();
			ResultSet results = sql.executeQuery("SELECT * FROM store.inv_core");

			while (results.next()) {
				prods.add(new Product(
						results.getString("name"),
						results.getInt("id"),
						results.getInt("quantity"),
						results.getInt("reorder_level"),
						results.getBigDecimal("cost_price"),
						results.getBigDecimal("list_price")
				));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prods;
	}
}
