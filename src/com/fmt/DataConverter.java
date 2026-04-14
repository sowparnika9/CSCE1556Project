package com.fmt;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;

/**
 * Author: Carlos Bueno & Sowparnika sandhya
 * Date: 2023-02-24
 *
 *This program formats the loaded CSV files to JSON and XML 
 *and writes to an output file
 */
public class DataConverter {
	/**
	 * Converts Store.csv to Store.json
	 * @param stores
	 * @param writePath
	 * @return
	 */
	public static String storesToJson(HashMap<String, Store> stores, String writePath){
		
		GsonBuilder builder = new GsonBuilder();
		String storesString = builder.setPrettyPrinting().create().toJson(stores);
		 try {
			 FileWriter writer = new FileWriter(writePath);
			 writer.write(storesString);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return storesString;
	}
	/**
	 * Converts Person.csv to Person.json
	 * @param people
	 * @param writePath
	 * @return
	 */
	public static String peopleToJson(HashMap<String, Person> people, String writePath) {
		
		Collection<Person> values = people.values();
		GsonBuilder builder = new GsonBuilder();
		String peopleString = builder.setPrettyPrinting().create().toJson(values);
		try {
			FileWriter writer = new FileWriter(writePath);
			writer.write(peopleString);
			writer.close();
		}catch (IOException e ) {
			e.printStackTrace();
		}
		return peopleString;
	}
	/**
	 * Converts Item.csv to Item.json
	 * @param items
	 * @param writePath
	 * @return
	 */
	public static String itemsToJson(Map<String, Item>items, String writePath) {
		
		GsonBuilder builder = new GsonBuilder();
		String itemsString = builder.setPrettyPrinting().create().toJson(items);
		
		try {
			FileWriter writer = new FileWriter(writePath);
			writer.write(itemsString);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return itemsString;
	}
	/**
	 * Converts Store.csv to Store.xml
	 * @param stores
	 * @param writePath
	 * @return
	 */
	public static String storesToXml(HashMap<String, Store> stores, String writePath){
		
		XStream xstream = new XStream();
		String xmlString = xstream.toXML(stores);
		 try {
			 FileWriter writer = new FileWriter(writePath);
			 writer.write(xmlString);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xmlString;
	}
	/**
	 * Converts Person.csv to Person.xml
	 * @param people
	 * @param writePath
	 * @return
	 */
	public static String peopleToXml(HashMap<String, Person> people, String writePath) {
		
		XStream xstream = new XStream();
		String xmlString = xstream.toXML(people);
		try {
			FileWriter writer = new FileWriter(writePath);
			writer.write(xmlString);
			writer.close();
		}catch (IOException e ) {
			e.printStackTrace();
		}
		return xmlString;
	}
	/**
	 * Converts Item.csv to Item.xml
	 * @param items
	 * @param writePath
	 * @return
	 */
	public static String itemsToXml(Map<String, Item>items, String writePath) {
		XStream xstream = new XStream();
		String xmlString = xstream.toXML(items);
		try {
			FileWriter writer = new FileWriter(writePath);
			writer.write(xmlString);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xmlString;
	}
	
	
	public static void main(String[] args) throws IOException {
		
		HashMap<String, Person> people = LoadData.mapPersonFile();
		DataConverter.peopleToJson(people,"data/Persons.json");
		DataConverter.peopleToXml(people,"data/Persons.xml");
		
		HashMap<String, Store> store = LoadData.parseStoreFile(people);
		DataConverter.storesToJson(store,"data/Stores.json");
		DataConverter.storesToXml(store,"data/Stores.xml");
		
		Map<String, Item> item = LoadData.parseItemFile();
		DataConverter.itemsToJson(item,"data/Items.json");
		DataConverter.itemsToXml(item,"data/Items.xml");
	}
}

