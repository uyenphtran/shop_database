package db;
import java.sql.*;

public class SHOPDB {

	public static void main(String[] args) throws SQLException{	
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Statement s = initializeConnection();
		displayProfitDB(s);
		soldItem("Reversible puffer jacket", s);
		
	}
	
	public static Statement initializeConnection() {
		String url = "jdbc:mysql://localhost:3306/shop_db";
		String username = "root";
		String pw = "uyenstfu";
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			Connection connection = DriverManager.getConnection(url, username, pw);
			Statement stmt = connection.createStatement();	
			return stmt;
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void addItem(String name, int price, int condition, String brand, Statement s) throws SQLException {
		if (name.length() > 25) {
			name = name.substring(0, 24);
		}
		if (brand.length() > 25) {
			brand = brand.substring(0, 24);
		}
		
		s.executeUpdate("INSERT INTO Items VALUES ('"+ name + "', " + price + ", " + condition+ ", '" + brand + "')");
		
	}
	
	public static void deleteItem(String name, Statement s) throws SQLException {
		if (name.length() > 25) {
			name = name.substring(0, 24);
		}
		
		s.executeUpdate("DELETE FROM Items WHERE Item_name='" + name + "'");
	}
	
	public static void updateItem(String col, String value, String name, Statement s) throws SQLException {
		col = col.substring(0, 1).toUpperCase() + col.substring(1); 
		if (col.compareTo("Item_name") == 0 || col.compareTo("Brand") == 0) {
			s.executeUpdate("UPDATE Items SET " + col + "='" + value + "' WHERE Item_name='" + name + "'");
		}
		else {
			int val = Integer.parseInt(value);
			//String q = "UPDATE Items SET " + col + "=" + val + " WHERE Item_name='" + name + "'";
			s.executeUpdate("UPDATE Items SET " + col + "=" + val + " WHERE Item_name='" + name + "'");
		}
		
	}
	
	public static void findItem(String col, String value, Statement s) throws SQLException {
		col = col.substring(0, 1).toUpperCase() + col.substring(1);  
		if (col.compareTo("Item_name") == 0 || col.compareTo("Brand") == 0) {
			value = "'" + value + "'";
		}
		ResultSet result = s.executeQuery("SELECT * FROM Items WHERE " + col + "=" + value);
		System.out.printf("%-30.30s  %-10.30s  %-10.30s  %-30.30s%n", "ITEM NAME", "PRICE", "CONDITION", "BRAND");
		while (result.next()) {
			System.out.printf("%-30.30s  %-10.30s  %-10.30s  %-30.30s%n", 
					result.getString(1), result.getString(2), result.getString(3), result.getString(4));
		}
		
	}
	
	public static void displayItemsDB(Statement s) {
		System.out.printf("%-30.30s  %-10.30s  %-10.30s  %-30.30s%n", "ITEM NAME", "PRICE", "CONDITION", "BRAND");
		try {
			ResultSet result = s.executeQuery("SELECT * FROM Items");
			while(result.next()) {
				System.out.printf("%-30.30s  %-10.30s  %-10.30s  %-30.30s%n", 
					result.getString(1), result.getString(2), result.getString(3), result.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void soldItem(String name, Statement s) throws SQLException {
		if (name.length() > 25) {
			name = name.substring(0, 24);
		}
		
		ResultSet result = s.executeQuery("SELECT Price FROM Items WHERE Item_name='" + name + "'");
		if (result.next()) {
			int price = result.getInt("Price");
			deleteItem(name, s);
			ResultSet result2 = s.executeQuery("SELECT SUM(Price) FROM Profit");
			int newSum = price;
			if (result2.next())	
				newSum += result2.getInt("SUM(Price)");
			s.executeUpdate("INSERT INTO Profit VALUES ('"+ name + "', " + Integer.toString(price) + ", " + Integer.toString(newSum)+")");
		}
	}
	
	public static void displayProfitDB(Statement s) {
		System.out.printf("%-30.30s  %-10.30s  %-10.30s%n", "ITEM_NAME", "PRICE", "PROFIT");
		try {
			ResultSet result = s.executeQuery("SELECT * FROM Profit");
			while(result.next()) {
				System.out.printf("%-30.30s  %-10.30s  %-10.30s%n", 
					result.getString(1), result.getString(2), result.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	


}
