package com.fmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author Carlos Bueno and Sowparnika Sandhya
 * Date: 2023-04-14
 *
 */
public class DatabaseLoader {
	
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * This method gets the Address using the given ID.
	 * 
	 * @param addressId
	 * @return
	 */
	public static Address getDetailedAddress(int addressId) {
		
		Address address = null;
		Connection conn = DatabaseInfo.getConnection();
		
		String query = "Select a.street, a.city, s.state, a.zip,  c.country" + "	   From Address a"
				+ "    Join State s" + "    Join Country c" + "    On a.countryId = c.countryId "
				+ "    and a.stateId = s.stateId" + "    Where addressId = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, addressId);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				String street = rs.getString("a.street");
				String city = rs.getString("a.city");
				String state = rs.getString("s.state");
				String zip = rs.getString("a.zip");
				String country = rs.getString("c.country");
				address = new Address(street, city, state, zip, country);
			} else {
				LOGGER.error("SQLException : Address not found. ");
				throw new IllegalStateException("Address not found. ");
			}
			rs.close();
			ps.close();
			DatabaseInfo.closeConnection(conn);
			
		} catch (SQLException e) {
			LOGGER.error("SQLException : ");
			throw new RuntimeException(e);
		}
		return address;
	}

	/**
	 * This method gets the Person using the given ID.
	 * @param personId
	 * @return
	 */
	public static Person getDetailedPerson(int personId) {
		
		Person person = null;
		Connection conn = DatabaseInfo.getConnection();

		String query = "Select p.personCode, p.lastName, p.firstName, p.addressId, p.personId" + "	   From Person p"
				+ "    Join Address a" + "    On p.addressId = a.addressId" + "    Where p.personId = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, personId);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				String personCode = rs.getString("p.personCode");
				String lastName = rs.getString("p.lastName");
				String firstName = rs.getString("p.firstName");
				int addressId = rs.getInt("p.addressId");
				Address a = DatabaseLoader.getDetailedAddress(addressId);
				List<String> emails = DatabaseLoader.getemailList(rs.getInt("p.personId"));
				person = new Person(personCode, lastName, firstName, a, emails);
			} else {
				LOGGER.error("SQLException : Person not found. ");
				throw new IllegalStateException("Person not found. ");
			}
			rs.close();
			ps.close();
			DatabaseInfo.closeConnection(conn);
			
		} catch (SQLException e) {
			LOGGER.error("SQLException : ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return person;
	}

	/**
	 * This method returns a hashmap of person IDs to Persons
	 * @return
	 */
	public static HashMap<String, Person> personMap() {
		
		HashMap<String, Person> personMap = new HashMap<String, Person>();
		Person person = null;
		Connection conn = DatabaseInfo.getConnection();

		String query = "Select p.personCode, p.lastName, p.firstName, p.addressId, p.personId" + "	   From Person p"
				+ "    Join Address a" + "    On p.addressId = a.addressId;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				String personCode = rs.getString("p.personCode");
				String lastName = rs.getString("p.lastName");
				String firstName = rs.getString("p.firstName");
				int addressId = rs.getInt("p.addressId");
				int personId = rs.getInt("p.personId");
				Address a = DatabaseLoader.getDetailedAddress(addressId);
				List<String> emails = DatabaseLoader.getemailList(personId);
				person = new Person(personCode, lastName, firstName, a, emails);
				personMap.put(personCode, person);
			} else {
				LOGGER.error("SQLException : Person not found. ");
				throw new IllegalStateException("Trouble loading Person table");
			}
			rs.close();
			ps.close();
			DatabaseInfo.closeConnection(conn);
			
		} catch (SQLException e) {
			LOGGER.error("SQLException : ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		LOGGER.info("Done loading all person data...");
		return personMap;
	}

	/**
	 * This method returns a list of Emails
	 * @param personId
	 * @return
	 */
	public static List<String> getemailList(int personId) {
		
		List<String> emailList = new ArrayList<String>();
		Connection conn = DatabaseInfo.getConnection();

		String emailQuery = "Select email" + "	   From Email " + "    Where personId = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(emailQuery);
			ps.setInt(1, personId);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				emailList.add(rs.getString("email"));
			}
			rs.close();
			ps.close();
			DatabaseInfo.closeConnection(conn);

		} catch (SQLException e) {
			LOGGER.error("SQLException : ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return emailList;
	}

	/**
	 * This method returns a hashmap of Store codes to stores.
	 * @return
	 */
	public static HashMap<Integer, Store> loadStore() {
		
		HashMap<Integer, Store> storeMap = new HashMap<Integer, Store>();
		Store store = null;
		Connection conn = DatabaseInfo.getConnection();

		String storeQuery = "Select storeId, storeCode, managerId, addressId" + "	From Store;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(storeQuery);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				int storeId = rs.getInt("storeId");
				String storeCode = rs.getString("storeCode");
				int managerId = rs.getInt("managerId");
				int addressId = rs.getInt("addressId");
				Address address = DatabaseLoader.getDetailedAddress(addressId);
				Person people = DatabaseLoader.getDetailedPerson(managerId);
				store = new Store(storeId, storeCode, people, address);
				storeMap.put(storeId, store);
			}
			rs.close();
			ps.close();
			DatabaseInfo.closeConnection(conn);
			
		} catch (SQLException e) {
			LOGGER.error("SQLException : ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		LOGGER.info("Done loading all stores...");
		return storeMap;
	}

	/**
	 * This method returns a hashmap of Invoice IDs to Invoices.
	 * @param stores
	 * @return
	 */
	public static HashMap<Integer, Invoice> getInvoices(HashMap<Integer, Store> stores) {
		
		List<Invoice> invList = new ArrayList<Invoice>();
		Connection conn = DatabaseInfo.getConnection();

		String invoiceQuery = "Select i.invoiceId, i.invoiceCode, i.customerId, i.salespersonId, date, s.storeId"
				+ "    From Invoice i" + "    Join Store s" + "    On i.storeId = s.storeId;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(invoiceQuery);
			rs = ps.executeQuery();

			while (rs.next()) {
				int invoiceId = rs.getInt("invoiceId");
				String invoiceCode = rs.getString("invoiceCode");
				int customerId = rs.getInt("customerId");
				int salespersonId = rs.getInt("salespersonId");
				Person customer = DatabaseLoader.getDetailedPerson(customerId);
				Person salesPerson = DatabaseLoader.getDetailedPerson(salespersonId);
				String date = rs.getString("date");
				Integer storeId = rs.getInt("s.storeId");
				Store storeCode = stores.get((storeId));
				Invoice i = new Invoice(invoiceId, invoiceCode, storeCode, customer, salesPerson, date);
				invList.add(i);
				storeCode.addInvoice(i);
			}
			rs.close();
			ps.close();
			DatabaseInfo.closeConnection(conn);
			
		} catch (SQLException e) {
			LOGGER.error("SQLException : ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		HashMap<Integer, Invoice> invoiceMap = new HashMap<Integer, Invoice>();
		for (Invoice invoice : invList) {
			invoiceMap.put(invoice.getInvoiceId(), invoice);
		}
		LOGGER.info("Done loading all invoices...");
		return invoiceMap;
	}
	
	/**
	 * This method adds the invoice item to the corresponding invoice.
	 * @param invoice
	 */
	public static void getItemList(HashMap<Integer, Invoice> invoice) {
		
		Connection conn = DatabaseInfo.getConnection();
		Item item = null;

		String itemQuery = "Select i.type, t.date, t.invoiceId, t.invoiceCode, i.itemId, i.name, i.model, i.itemCode,"
				+ "	   i.hourlyRate, i.unit, i.unitPrice, v.quantity,"
				+ "	   v.purchasePrice, v.leaseRate, v.startDate, v.endDate, v.hoursBilled" + "	   From Item i"
				+ "    Join InvoiceItem v" + "	   Join Invoice t"
				+ "    On i.itemId = v.itemId and v.invoiceId = t.invoiceId;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(itemQuery);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				String itemType = rs.getString("i.type");
				String itemCode = rs.getString("i.itemCode");
				String itemName = rs.getString("i.name");
				Integer invoiceId = rs.getInt("t.invoiceId");
				Invoice currentInvoice = invoice.get(invoiceId);
				
				if (itemType.equals("E")) {
					String model = rs.getString("i.model");

					if (rs.getString("v.leaseRate") == null) {
						double purchasePrice = rs.getDouble("v.purchasePrice");
						item = new Purchase(itemCode, itemName, model, purchasePrice);

					} else if (rs.getString("v.purchasePrice") == null) {
						double leaseRate = rs.getDouble("v.leaseRate");
						String startDate = rs.getString("v.startDate");
						String endDate = rs.getString("v.endDate");
						item = new Lease(itemCode, itemName, model, leaseRate, startDate, endDate);
					}
				} else if (itemType.equals("P")) {
					String unit = rs.getString("i.unit");
					double unitPrice = rs.getDouble("i.unitPrice");
					int quantity = rs.getInt("v.quantity");
					item = new Product(itemCode, itemName, unit, unitPrice, quantity);

				} else if (itemType.equals("S")) {
					double hourlyRate = rs.getDouble("i.hourlyRate");
					double hoursBilled = rs.getDouble("v.hoursBilled");
					item = new Service(itemCode, itemName, hourlyRate, hoursBilled);
					
				} else {
					LOGGER.error("SQLException : Address not found. ");
					throw new IllegalStateException("Address not found. ");
				}
				currentInvoice.addItem(item);
			}
			rs.close();
			ps.close();
			DatabaseInfo.closeConnection(conn);
			
		} catch (SQLException e) {
			LOGGER.error("SQLException : ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		LOGGER.info("Done loading all items...");
	}

}