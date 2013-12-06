package dao;

import java.sql.*;
import java.util.ArrayList;

import model.Person;
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
			_conn.setAutoCommit(false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Product> getProducts() {
		Statement sql;
		ArrayList<Product> prods = new ArrayList<Product>();

		try {
			sql = _conn.createStatement();
			ResultSet results = sql
					.executeQuery("SELECT * FROM store.inv_core");

			while (results.next()) {
				prods.add(new Product(results.getString("name"), results
						.getInt("id"), results.getInt("quantity"), results
						.getInt("reorder_level"), results
						.getBigDecimal("cost_price"), results
						.getBigDecimal("list_price")));
			}
			sql.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prods;
	}

	public Person getPerson(String incID) {
		Statement sql;
		Person emp = null;
		try {
			sql = _conn.createStatement();
			ResultSet results = sql
					.executeQuery("SELECT * FROM store.emp_core WHERE id = " + incID);
			while (results.next()) {
				emp = new Person(results.getInt("id"),
						results.getString("first_name"),
						results.getString("last_name"));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return emp;
	}

	public Product getProduct(String incID) {
		Statement sql;
		Product prod = null;
		try {
			sql = _conn.createStatement();
			ResultSet results = sql
					.executeQuery("SELECT * FROM store.inv_core WHERE id = "
							+ incID);
			while (results.next()) {

				prod = new Product(results.getString("name"),
						Integer.parseInt(incID), results.getInt("quantity"),
						results.getInt("reorder_level"),
						results.getBigDecimal("cost_price"),
						results.getBigDecimal("list_price"));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return prod;
	}

	public void setPerson(Person emp) {
		if (emp.changed()) {
			try {
				String sql;
				sql = "UPDATE store.emp_core SET first_name = ?, last_name = ?, social_security = ?," +
						"hourly_rate = ?, birth_date = ?, hire_date = ?, leave_date = ," +
						" leave_reason = ? WHERE id = ?";				
				PreparedStatement stmt = _conn.prepareStatement(sql);				
				stmt.setString( 1, emp.get_first_name());
				stmt.setString( 2, emp.get_last_name());
				stmt.setString( 3,  emp.get_social());
				stmt.setString(4, emp.get_hourly_rate());
				stmt.setString( 5, emp.get_birth());
				stmt.setString(6,emp.get_hire_date());
				stmt.setString(7, emp.get_leave_date());
				stmt.setString(8, emp.get_leave_reason());
				stmt.setInt(9, emp.get_id());
				stmt.executeUpdate(sql);
				stmt.close();
				_conn.commit();
			} catch (SQLException e) {
				try {
					_conn.rollback();
				} catch (SQLException e1) {
				}
				e.printStackTrace();
			}
		}
	}

	public void setProduct(Product deltaProd) {
		if (deltaProd.hasChanged()) {
			try {
				String sql;
				sql = "UPDATE store.emp_core SET name = ? "
						+ ", cost_price = ?, list_price = ?"
						+ "	reorder_level = ?, quantity = ?" + "WHERE id = ?";
				PreparedStatement stmt = _conn.prepareStatement(sql);
				stmt.setString(1, deltaProd.getName());
				stmt.setBigDecimal(2, deltaProd.getCost());
				stmt.setBigDecimal(3, deltaProd.getList());
				stmt.setInt(4, deltaProd.getReorder());
				stmt.setInt(5, deltaProd.getQuantity());
				stmt.setInt(6, deltaProd.getID());
				stmt.executeUpdate();
				stmt.close();
				_conn.commit();
			} catch (Exception e) {
				try {
					_conn.rollback();
				} catch (SQLException e1) {
				}
				e.printStackTrace();
			}
		}
	}

	public void newProduct(Product newProd) {
		try {
			String sql;
			sql = "UPDATE store.inv_core SET name = ?, cost_price = ?, "
					+ "list_price = ?, quantity = ?, reorder_level = ?";
			PreparedStatement stmt = _conn.prepareStatement(sql);
			stmt.setString(1, newProd.getName());
			stmt.setBigDecimal(2, newProd.getCost());
			stmt.setBigDecimal(3, newProd.getList());
			stmt.setInt(4, newProd.getQuantity());
			stmt.setInt(5, newProd.getReorder());
			stmt.executeUpdate();
			stmt.close();
			_conn.commit();
		}catch(Exception e){
			e.printStackTrace();
			try {_conn.rollback();} catch (SQLException e1) {}
		}
	}

	public void newPerson(Person newPerson) {
		// TODO: save new Person to DB here
		try {
			String sql;
			// FIRST_NAME LAST_NAME SOCIAL_SECURITY HOURLY_RATE BIRTH_DATE HIRE_DATE
			sql = "UPDATE store.emp_core SET first_name = ?, last_name = ?, "
					+ "social_security = ?, Hourly_rate = ?, birth_date= ?,"
					+ "hire_date = ?";
			PreparedStatement stmt = _conn.prepareStatement(sql);
			stmt.setString(1, newPerson.get_first_name());
			stmt.setString(2, newPerson.get_last_name());
			stmt.setString(3, newPerson.get_social());
			stmt.setString(4, newPerson.get_hourly_rate());
			stmt.setString(5, newPerson.get_birth());
			stmt.setString(6, newPerson.get_hire_date());
			stmt.executeUpdate();
			stmt.close();
			_conn.commit();
		}catch(Exception e){
			e.printStackTrace();
			try {_conn.rollback();} catch (SQLException e1) {}
		}
	}
}
