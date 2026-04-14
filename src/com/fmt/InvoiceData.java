package com.fmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class InvoiceData {

	/**
	 * Removes all records from all tables in the database.
	 */
	public static void clearDatabase() {
		Connection conn = DatabaseInfo.getConnection();

		String query = null;
		PreparedStatement ps = null;

		try {
			query = "delete from InvoiceItem;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			ps.close();

			query = "delete from Item;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			ps.close();

			query = "delete from Invoice;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			ps.close();

			query = "delete from Store;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			ps.close();

			query = "delete from Email;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			ps.close();

			query = "delete from Person;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			ps.close();

			query = "delete from Address;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			ps.close();

			query = "delete from State;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			ps.close();

			query = "delete from Country;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			ps.close();
			DatabaseInfo.closeConnection(conn);

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personCode
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static void addPerson(String personCode, String firstName, String lastName, String street, String city,
			String state, String zip, String country) {

		Connection conn = DatabaseInfo.getConnection();

		String query = null;
		PreparedStatement ps = null;

		try {

			int addressId = addAddress(street, city, state, zip, country);

			query = "Insert into Person (personCode, firstName, lastName, addressId) values(?,?,?,?);";
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.setString(2, firstName);
			ps.setString(3, lastName);
			ps.setInt(4, addressId);

			ps.executeUpdate();
			ps.close();
			DatabaseInfo.closeConnection(conn);

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return;
	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personCode</code>
	 *
	 * @param personCode
	 * @param email
	 */
	public static void addEmail(String personCode, String email) {
		Connection conn = DatabaseInfo.getConnection();

		String query = "Select personId From Person Where personCode = ?;";
		PreparedStatement ps = null;
		ResultSet rs = null;
		int personId = 0;

		try {

			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				personId = rs.getInt("personId");
			}

			rs.close();
			ps.close();

			query = "Insert into Email (personId, email) values (?,?);";
			ps = conn.prepareStatement(query);
			ps.setInt(1, personId);
			ps.setString(2, email);
			ps.executeUpdate();

			ps.close();
			DatabaseInfo.closeConnection(conn);

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return;

	}

	/**
	 * Adds a store record to the database managed by the person identified by the
	 * given code.
	 *
	 * @param storeCode
	 * @param managerCode
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static void addStore(String storeCode, String managerCode, String street, String city, String state,
			String zip, String country) {

		Connection conn = DatabaseInfo.getConnection();

		String query = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int managerId = 0;
		int addressId = 0;

		try {

			addressId = addAddress(street, city, state, zip, country);

			query = "Select personId From Person Where personCode = ?;";
			ps = conn.prepareStatement(query);
			ps.setString(1, managerCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				managerId = rs.getInt("personId");
			}

			rs.close();
			ps.close();

			query = "Insert into Store (storeCode, managerId, addressId) values (?,?,?);";
			ps = conn.prepareStatement(query);
			ps.setString(1, storeCode);
			ps.setInt(2, managerId);
			ps.setInt(3, addressId);
			ps.executeUpdate();

			ps.close();
			DatabaseInfo.closeConnection(conn);

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return;

	}

	/**
	 * Adds a product record to the database with the given <code>code</code>,
	 * <code>name</code> and <code>unit</code> and <code>pricePerUnit</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param unit
	 * @param pricePerUnit
	 */
	public static void addProduct(String code, String name, String unit, double pricePerUnit) {
		Connection conn = DatabaseInfo.getConnection();

		String query = null;
		PreparedStatement ps = null;
		String type = "P";

		try {

			query = "Insert into Item (itemCode, type, name, unit, unitPrice) values(?,?,?,?,?);";
			ps = conn.prepareStatement(query);

			ps.setString(1, code);
			ps.setString(2, type);
			ps.setString(3, name);
			ps.setString(4, unit);
			ps.setDouble(5, pricePerUnit);
			ps.executeUpdate();

			ps.close();
			DatabaseInfo.closeConnection(conn);

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return;
	}

	/**
	 * Adds an equipment record to the database with the given <code>code</code>,
	 * <code>name</code> and <code>modelNumber</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param modelNumber
	 */
	public static void addEquipment(String code, String name, String modelNumber) {
		Connection conn = DatabaseInfo.getConnection();

		String query = null;
		PreparedStatement ps = null;
		String type = "E";

		try {

			query = "Insert into Item (itemCode, type, name, model) values(?,?,?,?);";
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			ps.setString(2, type);
			ps.setString(3, name);
			ps.setString(4, modelNumber);
			ps.executeUpdate();

			ps.close();
			DatabaseInfo.closeConnection(conn);
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return;
	}

	/**
	 * Adds a service record to the database with the given <code>code</code>,
	 * <code>name</code> and <code>costPerHour</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param modelNumber
	 */
	public static void addService(String code, String name, double costPerHour) {
		Connection conn = DatabaseInfo.getConnection();

		String query = null;
		PreparedStatement ps = null;
		String type = "S";

		try {

			query = "Insert into Item (itemCode, type, name, hourlyRate) values(?,?,?,?);";
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			ps.setString(2, type);
			ps.setString(3, name);
			ps.setDouble(4, costPerHour);
			
			ps.executeUpdate();
			ps.close();
			DatabaseInfo.closeConnection(conn);

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return;
	}

	/**
	 * Adds an invoice record to the database with the given data.
	 *
	 * @param invoiceCode
	 * @param storeCode
	 * @param customerCode
	 * @param salesPersonCode
	 * @param invoiceDate
	 */
	public static void addInvoice(String invoiceCode, String storeCode, String customerCode, String salesPersonCode,
			String invoiceDate) {

		Connection conn = DatabaseInfo.getConnection();

		String query = null;
		PreparedStatement ps = null;
		int storeId = 0;
		int customerId = 0;
		int salesPersonId = 0;

		try {

			query = "Select storeId From Store where storeCode = ?;";
			ps = conn.prepareStatement(query);
			ps.setString(1, storeCode);
			ResultSet storeKey = ps.executeQuery();

			storeKey.next();
			storeId = storeKey.getInt(1);

			storeKey.close();
			ps.close();

			query = "Select personId from Person where personCode = ?;";
			ps = conn.prepareStatement(query);
			ps.setString(1, customerCode);
			ResultSet customerKey = ps.executeQuery();

			customerKey.next();
			customerId = customerKey.getInt(1);

			customerKey.close();
			ps.close();

			query = "Select personId from Person where personCode = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, salesPersonCode);
			ResultSet managerKey = ps.executeQuery();

			managerKey.next();
			salesPersonId = managerKey.getInt(1);

			managerKey.close();
			ps.close();

			query = "Insert into Invoice (invoiceCode, storeId, customerId, salesPersonId, date) values(?,?,?,?,?);";
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			ps.setInt(2, storeId);
			ps.setInt(3, customerId);
			ps.setInt(4, salesPersonId);
			ps.setString(5, invoiceDate);

			ps.executeUpdate();

			ps.close();
			DatabaseInfo.closeConnection(conn);

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return;

	}

	/**
	 * Adds a particular product (identified by <code>itemCode</code>) to a
	 * particular invoice (identified by <code>invoiceCode</code>) with the
	 * specified quantity.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param quantity
	 */
	public static void addProductToInvoice(String invoiceCode, String itemCode, int quantity) {
		Connection conn = DatabaseInfo.getConnection();

		String query = null;
		PreparedStatement ps = null;
		int invoiceId = 0;
		int itemId = 0;

		try {

			query = "Select invoiceId From Invoice Where invoiceCode = ?;";
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			ResultSet invoiceKey = ps.executeQuery();
			invoiceKey.next();
			invoiceId = invoiceKey.getInt(1);
			invoiceKey.close();
			ps.close();

			query = "Select itemId From Item where itemCode = ?;";
			ps = conn.prepareStatement(query);
			ps.setString(1, itemCode);
			ResultSet itemKey = ps.executeQuery();
			itemKey.next();
			itemId = itemKey.getInt(1);
			itemKey.close();
			ps.close();

			query = "Insert into InvoiceItem (invoiceId, itemId, quantity) values(?,?,?);";
			ps = conn.prepareStatement(query);
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setInt(3, quantity);
			ps.executeUpdate();

			ps.close();
			DatabaseInfo.closeConnection(conn);

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return;

	}

	/**
	 * Adds a particular equipment <i>purchase</i> (identified by
	 * <code>itemCode</code>) to a particular invoice (identified by
	 * <code>invoiceCode</code>) at the given <code>purchasePrice</code>.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param purchasePrice
	 */
	public static void addEquipmentToInvoice(String invoiceCode, String itemCode, double purchasePrice) {
		Connection conn = DatabaseInfo.getConnection();

		String query = null;
		PreparedStatement ps = null;
		int invoiceId = 0;
		int itemId = 0;

		try {

			query = "Select invoiceId From Invoice Where invoiceCode = ?;";
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			ResultSet invoiceKey = ps.executeQuery();
			invoiceKey.next();
			invoiceId = invoiceKey.getInt(1);
			invoiceKey.close();
			ps.close();

			query = "Select itemId From Item where itemCode = ?;";
			ps = conn.prepareStatement(query);
			ps.setString(1, itemCode);
			ResultSet itemKey = ps.executeQuery();
			itemKey.next();
			itemId = itemKey.getInt(1);
			itemKey.close();
			ps.close();

			query = "Insert into InvoiceItem (invoiceId, itemId, purchasePrice) values(?,?,?);";
			ps = conn.prepareStatement(query);
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setDouble(3, purchasePrice);
			ps.executeUpdate();

			ps.close();
			DatabaseInfo.closeConnection(conn);

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return;

	}

	/**
	 * Adds a particular equipment <i>lease</i> (identified by
	 * <code>itemCode</code>) to a particular invoice (identified by
	 * <code>invoiceCode</code>) with the given 30-day <code>periodFee</code> and
	 * <code>beginDate/endDate</code>.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param amount
	 */
	public static void addEquipmentToInvoice(String invoiceCode, String itemCode, double periodFee, String beginDate,
			String endDate) {

		Connection conn = DatabaseInfo.getConnection();

		String query = null;
		PreparedStatement ps = null;
		int invoiceId = 0;
		int itemId = 0;

		try {

			query = "Select invoiceId From Invoice Where invoiceCode = ?;";
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			ResultSet invoiceKey = ps.executeQuery();

			invoiceKey.next();
			invoiceId = invoiceKey.getInt(1);

			invoiceKey.close();
			ps.close();

			query = "Select itemId From Item where itemCode = ?;";
			ps = conn.prepareStatement(query);
			ps.setString(1, itemCode);
			ResultSet itemKey = ps.executeQuery();
			itemKey.next();
			itemId = itemKey.getInt(1);
			itemKey.close();
			ps.close();

			query = "Insert into InvoiceItem (invoiceId, itemId, leaseRate, startDate, endDate) values(?,?,?,?,?);";
			ps = conn.prepareStatement(query);
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setDouble(3, periodFee);
			ps.setString(4, beginDate);
			ps.setString(5, endDate);
			ps.executeUpdate();

			ps.close();
			DatabaseInfo.closeConnection(conn);

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return;

	}

	/**
	 * Adds a particular service (identified by <code>itemCode</code>) to a
	 * particular invoice (identified by <code>invoiceCode</code>) with the
	 * specified number of hours.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param billedHours
	 */
	public static void addServiceToInvoice(String invoiceCode, String itemCode, double billedHours) {
		Connection conn = DatabaseInfo.getConnection();

		String query = null;
		PreparedStatement ps = null;
		int invoiceId = 0;
		int itemId = 0;

		try {

			query = "Select invoiceId From Invoice Where invoiceCode = ?;";
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			ResultSet invoiceKey = ps.executeQuery();

			invoiceKey.next();
			invoiceId = invoiceKey.getInt(1);
			invoiceKey.close();
			ps.close();

			query = "Select itemId From Item where itemCode = ?;";
			ps = conn.prepareStatement(query);
			ps.setString(1, itemCode);
			ResultSet itemKey = ps.executeQuery();

			itemKey.next();
			itemId = itemKey.getInt(1);
			itemKey.close();
			ps.close();

			query = "Insert into InvoiceItem (invoiceId, itemId, hoursBilled) values(?,?,?);";
			ps = conn.prepareStatement(query);
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setDouble(3, billedHours);
			ps.executeUpdate();

			ps.close();
			DatabaseInfo.closeConnection(conn);

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return;

	}

	/**
	 * Method to get the Address Id
	 * 
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 * @return
	 */
	public static int addAddress(String street, String city, String state, String zip, String country) {
		Connection conn = DatabaseInfo.getConnection();

		String query = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			query = "Select stateId from State where state =?;";
			ps = conn.prepareStatement(query);
			ps.setString(1, state);
			rs = ps.executeQuery();

			int stateId = 0;
			if (rs.next()) {
				stateId = rs.getInt("stateId");

				rs.close();
				ps.close();
			} else {

				rs.close();
				ps.close();

				query = "Insert into State (state) values (?);";
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, state);
				ps.executeUpdate();

				ResultSet stateKey = ps.getGeneratedKeys();
				stateKey.next();
				stateId = stateKey.getInt(1);

				stateKey.close();
				ps.close();

			}

			query = "Select countryId from Country where country =?;";
			ps = conn.prepareStatement(query);
			ps.setString(1, country);
			rs = ps.executeQuery();

			int countryId = 0;
			if (rs.next()) {
				countryId = rs.getInt("countryId");

				rs.close();
				ps.close();
			} else {

				rs.close();
				ps.close();

				query = "Insert into Country (country) values (?);";
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, country);
				ps.executeUpdate();

				ResultSet countryKey = ps.getGeneratedKeys();
				countryKey.next();
				countryId = countryKey.getInt(1);

				countryKey.close();
				ps.close();

			}

			query = "Insert into Address (street, city, stateId, zip, countryId) values (?,?,?,?,?);";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setInt(3, stateId);
			ps.setString(4, zip);
			ps.setInt(5, countryId);

			ps.executeUpdate();

			ResultSet addressKey = ps.getGeneratedKeys();
			addressKey.next();
			int addressId = addressKey.getInt(1);

			addressKey.close();
			ps.close();
			DatabaseInfo.closeConnection(conn);

			return addressId;

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);

		}

	}

}