/*
 * Copyright 2007 Kasper B. Graversen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.supercsv.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.supercsv.SuperCsvTestUtils.CSV_FILE;
import static org.supercsv.SuperCsvTestUtils.CUSTOMERS;
import static org.supercsv.SuperCsvTestUtils.HEADER;
import static org.supercsv.SuperCsvTestUtils.STRING_CUSTOMERS;
import static org.supercsv.SuperCsvTestUtils.WRITE_PROCESSORS;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.mock.CustomerBean;
import org.supercsv.mock.CustomerStringBean;
import org.supercsv.prefs.CsvPreference;

/**
 * Tests the CsvMapWriter class.
 * 
 * @author James Bassett
 * @author Pietro Aragona
 */
public class CsvMapWriterTest {
	
	private static final CsvPreference PREFS = CsvPreference.STANDARD_PREFERENCE;

	private static final String[] NAME_MAPPING = new String[] { "name", null, "city" };

	private static final Map<String, Object> MAP = new HashMap<String, Object>();
	static {
		MAP.put("name", "Ezio");
		MAP.put("age", 25);
		MAP.put("city", "Venice");
	}

	private Writer writer;
	
	private CsvMapWriter mapWriter;
	
	/**
	 * Sets up the writer for the tests.
	 */
	@Before
	public void setUp() {
		writer = new StringWriter();
		mapWriter = new CsvMapWriter(writer, PREFS);
	}
	
	/**
	 * Closes the map writer after the test.
	 */
	@After
	public void tearDown() throws IOException {
		mapWriter.close();
	}
	
	/**
	 * Tests the constructor with a null writer.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testConstructorWillNullWriter() {
		new CsvMapWriter(null, PREFS);
	}
	
	/**
	 * Tests the constructor with a null CsvPreference.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testConstructorWillNullPreference() {
		new CsvMapWriter(writer, null);
	}
	
	/**
	 * Tests the write() method.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testWrite() throws IOException {
		mapWriter.writeHeader(HEADER);
		for( CustomerStringBean customer : STRING_CUSTOMERS ) {
			Map<String, Object> customerMap = new HashMap<String, Object>();
			CsvMapReader.filterListToMap(
				customerMap,
				HEADER,
				Arrays.asList(new String[] { customer.getCustomerNo(), customer.getFirstName(), customer.getLastName(),
					customer.getBirthDate(), customer.getBirthTime(), customer.getMailingAddress(),
					customer.getMarried(), customer.getNumberOfKids(), customer.getFavouriteQuote(),
					customer.getEmail(), customer.getLoyaltyPoints() }));
			mapWriter.write(customerMap, HEADER);
		}
		mapWriter.flush();
		assertEquals(CSV_FILE, writer.toString());
	}
	
	/**
	 * Tests the write() method with processors.
	 */
	@Test
	public void testWriteProcessors() throws IOException {
		mapWriter.writeHeader(HEADER);
		for( CustomerBean customer : CUSTOMERS ) {
			Map<String, Object> customerMap = new HashMap<String, Object>();
			CsvMapReader.filterListToMap(
				customerMap,
				HEADER,
				Arrays.asList(new Object[] { customer.getCustomerNo(), customer.getFirstName(), customer.getLastName(),
					customer.getBirthDate(), customer.getBirthTime(), customer.getMailingAddress(),
					customer.getMarried(), customer.getNumberOfKids(), customer.getFavouriteQuote(),
					customer.getEmail(), customer.getLoyaltyPoints() }));
			mapWriter.write(customerMap, HEADER, WRITE_PROCESSORS);
		}
		mapWriter.flush();
		assertEquals(CSV_FILE, writer.toString());
	}
	
	/**
	 * Tests the write() method with a null map.
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteWithNullMap() throws IOException {
		mapWriter.write(null, HEADER);
	}
	
	/**
	 * Tests the write() method with a null name mapping array.
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteWithNullNameMapping() throws IOException {
		mapWriter.write(new HashMap<String, Object>(), (String[]) null);
	}
	
	/**
	 * Tests the write() method (with processors) with a null map.
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteProcessorsWithNullMap() throws IOException {
		mapWriter.write(null, HEADER, WRITE_PROCESSORS);
	}
	
	/**
	 * Tests the write() method (with processors) with a null name mapping array.
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteProcessorsWithNullNameMapping() throws IOException {
		mapWriter.write(new HashMap<String, Object>(), null, WRITE_PROCESSORS);
		
	}
	
	/**
	 * Tests the write() method (with processors) with a null cell processor array.
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteProcessorsWithNullProcessors() throws IOException {
		mapWriter.write(new HashMap<String, Object>(), HEADER, null);
		
	}

	/**
	 * Tests the filterMapToObjectArray() method.
	 */
	@Test
	public void testFilterMapToObjectArray() {
		final Object[] objectArray = mapWriter.filterMapToObjectArray(MAP, NAME_MAPPING);
		assertTrue(objectArray.length == 3);
		assertEquals("Ezio", objectArray[0]);
		assertNull(objectArray[1]);
		assertEquals("Venice", objectArray[2]);
	}

	/**
	 * Tests the filterMapToObjectArray() method with a null values Map (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testFilterMapToObjectArrayWithNullValues() {
		mapWriter.filterMapToObjectArray(null, NAME_MAPPING);
	}

	/**
	 * Tests the filterMapToObjectArray() method with a null nameMapping array (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testFilterMapToObjectArrayWithNullNameMapping() {
		mapWriter.filterMapToObjectArray(MAP, null);
	}

	/**
	 * Tests the filterMapToList() method (the age attribute is not used).
	 */
	@Test
	public void testFilterMapToList() {
		List<Object> list = mapWriter.filterMapToList(MAP, NAME_MAPPING);
		assertTrue(list.size() == 3);
		assertEquals("Ezio", list.get(0));
		assertNull(list.get(1));
		assertEquals("Venice", list.get(2));
	}

	/**
	 * Tests the filterMapToList() method with a null map (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testFilterMapToListWithNullMap() {
		mapWriter.filterMapToList(null, NAME_MAPPING);
	}

	/**
	 * Tests the filterMapToList() method with a null name mapping array (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testFilterMapToListWithNullNameMapping() {
		mapWriter.filterMapToList(MAP, null);
	}

}
