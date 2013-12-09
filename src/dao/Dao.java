package dao;

import java.sql.*;
import java.util.ArrayList;
import model.Address;
import model.Person;
import model.Product;
import model.shoppingCart;

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
			_conn = DriverManager.getConnection("jdbc:derby:store", uname, pword);
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
			ResultSet results = sql.executeQuery("SELECT * FROM store.inv_core");

			while (results.next()) {
				prods.add(new Product(results.getString("name"), results.getInt("id"), results.getInt("quantity"), results
						.getInt("reorder_level"), results.getBigDecimal("cost_price"), results.getBigDecimal("list_price")));
			}
			sql.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prods;
	}

	public String getProductsAsString() {
		Statement sql;
		String prods = "";

		try {
			sql = _conn.createStatement();
			ResultSet results = sql.executeQuery("SELECT * FROM store.inv_core");

			while (results.next()) {
				prods += String.format("%-15s%-15d%-15d%-8.2f%n", results.getString("name"), results.getInt("quantity"),
						results.getInt("reorder_level"), results.getDouble("cost_price"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return prods;
	}

	public Person getPerson(String incID) {
		Statement sql;
		Person emp = null;
		Address addy = null;
		
		System.out.println("dao.getPerson: _incID: " + incID);
		
		try {
			sql = _conn.createStatement();
			ResultSet results = sql.executeQuery("SELECT * FROM store.emp_core WHERE id = " + incID);
			while (results.next()) {
				emp = new Person(results.getInt("id"), results.getString("first_name"), results.getString("last_name"));
				emp.set_hourly_rate(results.getString("hourly_rate"));
				emp.set_social(results.getString("social_security"));
				emp.set_birth(results.getString("birth_date"));
				emp.set_hire_date(results.getString("hire_date"));
			}
			sql.close();
			
			System.out.println("dao.getPerson: newPerson: " + emp);
			
			sql = _conn.createStatement();
			results = sql.executeQuery("SELECT * FROM store.emp_address WHERE core_id = " + emp.get_id());
			while (results.next()) {
				addy = new Address(results.getString("street"), "", results.getString("town"), results.getString("state"),
						results.getInt("zip"));
				emp.set_addy(addy);
			}
			sql.close();

			// TODO: get phone numbers, add to emp

		} catch (Exception e) {
			e.printStackTrace();
		}

		return emp;
	}

	public Person findPerson(String[] inputFields) {
		String whereClause = "";
		// For all that is holy, there must be a better way
		if (!inputFields[0].equals(""))
			whereClause += " first_name = '" + inputFields[0] + "'";
		if (!whereClause.equals("") && !inputFields[1].equals("") && whereClause.substring(whereClause.length() - 5) != " AND ")
			whereClause += " AND ";
		if (!inputFields[1].equals(""))
			whereClause += " last_name = '" + inputFields[1] + "'";
		if (!whereClause.equals("") && !inputFields[2].equals("") && whereClause.substring(whereClause.length() - 5) != " AND ")
			whereClause += " AND ";
		if (!inputFields[2].equals(""))
			whereClause += " hourly_rate = '" + inputFields[2] + "'";
		if (!whereClause.equals("") && !inputFields[3].equals("") && whereClause.substring(whereClause.length() - 5) != " AND ")
			whereClause += " AND ";
		if (!inputFields[3].equals(""))
			whereClause += " social_security = '" + inputFields[3] + "'";
		if (!whereClause.equals("") && !inputFields[4].equals("") && whereClause.substring(whereClause.length() - 5) != " AND ")
			whereClause += " AND ";
		if (!inputFields[4].equals(""))
			whereClause += " birth_date = '" + inputFields[4] + "'";
		if (!whereClause.equals("") && !inputFields[5].equals("") && whereClause.substring(whereClause.length() - 5) != " AND ")
			whereClause += " AND ";
		if (!inputFields[5].equals(""))
			whereClause += " hire_date = '" + inputFields[5] + "'";

		if (whereClause != "") {
			Statement sql;
			try {
				sql = _conn.createStatement();
				String sqlStr = "SELECT id FROM store.emp_core WHERE " + whereClause;
				//NOTE: CASE SENSITIVE
				ResultSet results = sql.executeQuery(sqlStr);
				System.out.println("dao.findPerson sql: " + sqlStr);
				int foundID = -1;
				while (results.next()) {
					foundID = results.getInt("id");
				}

				return getPerson(foundID + "");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public String getAllEmployees() {
		Statement sql;
		String emps = "";
		ResultSet results = null;
		try {
			sql = _conn.createStatement();
			results = sql.executeQuery("SELECT a.first_name, a.last_name, a.hourly_rate, b.town, c.number "
					+ "FROM store.emp_core AS a INNER JOIN store.emp_address AS b "
					+ "ON a.id = b.core_id INNER JOIN store.emp_phone AS c ON a.id = c.core_id");
			while (results.next()) {
				emps += String.format("%-15s%-15s%-6.2f%-15s%-15s%n", results.getString("first_name"),
						results.getString("last_name"), results.getDouble("hourly_rate"), results.getString("town"),
						results.getString("number"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return emps;
	}

	public String createPO() {
		Statement sql;
		String items = "";
		ResultSet results = null;
		try {
			sql = _conn.createStatement();
			results = sql.executeQuery("SELECT * FROM store.inv_core WHERE quantity < reorder_level");
			while (results.next()) {
				items += String.format("%-6d%-10s%-6.2f%n", results.getInt("id"), results.getString("name"),
						results.getDouble("list_price"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return items;
	}

	public Product getProduct(String incID) {
		Statement sql;
		Product prod = null;
		try {
			sql = _conn.createStatement();
			ResultSet results = sql.executeQuery("SELECT * FROM store.inv_core WHERE id = " + incID);
			while (results.next()) {

				prod = new Product(results.getString("name"), Integer.parseInt(incID), results.getInt("quantity"),
						results.getInt("reorder_level"), results.getBigDecimal("cost_price"), results.getBigDecimal("list_price"));
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
				sql = "UPDATE store.emp_core SET first_name = ?, last_name = ?, social_security = ?,"
						+ "hourly_rate = ?, birth_date = ?, hire_date = ?, leave_date = ," + " leave_reason = ? WHERE id = ?";
				PreparedStatement stmt = _conn.prepareStatement(sql);
				stmt.setString(1, emp.get_first_name());
				stmt.setString(2, emp.get_last_name());
				stmt.setString(3, emp.get_social());
				stmt.setString(4, emp.get_hourly_rate());
				stmt.setString(5, emp.get_birth());
				stmt.setString(6, emp.get_hire_date());
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
				sql = "UPDATE store.emp_core SET name = ? " + ", cost_price = ?, list_price = ?"
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
			sql = "UPDATE store.inv_core SET name = ?, cost_price = ?, " + "list_price = ?, quantity = ?, reorder_level = ?";
			PreparedStatement stmt = _conn.prepareStatement(sql);
			stmt.setString(1, newProd.getName());
			stmt.setBigDecimal(2, newProd.getCost());
			stmt.setBigDecimal(3, newProd.getList());
			stmt.setInt(4, newProd.getQuantity());
			stmt.setInt(5, newProd.getReorder());
			stmt.executeUpdate();
			stmt.close();
			_conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				_conn.rollback();
			} catch (SQLException e1) {
			}
		}
	}

	public void newPerson(Person newPerson) {
		// TODO: save new Person to DB here
		try {
			String sql;
			sql = "UPDATE store.emp_core SET first_name = ?, last_name = ?, "
					+ "social_security = ?, Hourly_rate = ?, birth_date= ?," + "hire_date = ?";
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
		} catch (Exception e) {
			e.printStackTrace();
			try {
				_conn.rollback();
			} catch (SQLException e1) {
			}
		}
	}

	public void completeSale(Person cashier, shoppingCart cart) {
		try {
			for (Product p : cart.getProds()) {
				if (p != null){
				System.out.println("dao.completeSale: prod: " + p);
				String sql = "INSERT INTO store.sls_core(emp_id, inv_id, sale_date) VALUES (?,?,?)";
				PreparedStatement stmt = _conn.prepareStatement(sql);
				stmt.setInt(1, cashier.get_id());
				stmt.setInt(2, p.getID());
				stmt.setTimestamp(3, getCurrentTimeStamp());
				stmt.execute();
				stmt.close();
				_conn.commit();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String salesReport(String id) {
		Person p = this.getPerson(id);
		String sales = "";
		int total = 0;
		try {
			Statement sql = _conn.createStatement();
			ResultSet results = sql.executeQuery("SELECT * FROM store.sls_core WHERE emp_id = " + p.get_id());
			while (results.next()) {
				int inv_id = results.getInt("inv_id");
				Statement prodSQL = _conn.createStatement();
				ResultSet prodResults = prodSQL.executeQuery("SELECT * FROM store.inv_core WHERE id = " + inv_id);
				while (prodResults.next()) {
					total += prodResults.getDouble("list_price");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return String.format("%s %s has sold a total of %d.", p.get_first_name(), p.get_last_name(), total);
	}

	/**
	 * This method stolen from:
	 * http://www.mkyong.com/jdbc/how-to-insert-timestamp
	 * -value-in-preparedstatement/
	 * 
	 * @return
	 */
	private Timestamp getCurrentTimeStamp() {

		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());

	}

}
