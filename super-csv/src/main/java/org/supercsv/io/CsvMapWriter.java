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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.prefs.CsvPreference;

/**
 * CsvMapWriter writes Maps of Objects to a CSV file.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class CsvMapWriter extends AbstractCsvWriter implements ICsvMapWriter {
	
	// temporary storage of processed columns to be written
	private final List<Object> processedColumns = new ArrayList<Object>();
	
	/**
	 * Constructs a new <tt>CsvMapWriter</tt> with the supplied Writer and CSV preferences. Note that the
	 * <tt>writer</tt> will be wrapped in a <tt>BufferedWriter</tt> before accessed.
	 * 
	 * @param writer
	 *            the writer
	 * @param preference
	 *            the CSV preferences
	 * @throws NullPointerException
	 *             if writer or preference are null
	 * @since 1.0
	 */
	public CsvMapWriter(final Writer writer, final CsvPreference preference) {
		super(writer, preference);
	}

	/**
	 * Constructs a new <tt>CsvMapWriter</tt> with the supplied Writer, CSV preferences and option
	 * to bufferize the writer.
	 *
	 * @param writer
	 *            the writer
	 * @param preference
	 *            the CSV preferences
	 * @param bufferizeWriter
	 *            if {@code true}, the <tt>writer</tt> will be wrapped in
	 *            a <tt>BufferedWriter</tt> before accessed.
	 * @throws NullPointerException
	 *             if writer or preference are null
	 * @since 1.0
	 */
	public CsvMapWriter(final Writer writer, final CsvPreference preference, boolean bufferizeWriter) {
		super(writer, preference, bufferizeWriter);
	}

	/**
	 * Returns a List of all of the values in the Map whose key matches an entry in the nameMapping array.
	 *
	 * @param map
	 *            the map
	 * @param nameMapping
	 *            the keys of the Map values to add to the List
	 * @return a List of all of the values in the Map whose key matches an entry in the nameMapping array
	 * @throws NullPointerException
	 *             if map or nameMapping is null
	 */
	public List<Object> filterMapToList(final Map<String, ?> map, final String[] nameMapping) {
		if( map == null ) {
			throw new NullPointerException("map should not be null");
		} else if( nameMapping == null ) {
			throw new NullPointerException("nameMapping should not be null");
		}

		final List<Object> result = new ArrayList<Object>(nameMapping.length);
		for( final String key : nameMapping ) {
			result.add(map.get(key));
		}
		return result;
	}

	/**
	 * Converts a Map to an array of objects, adding only those entries whose key is in the nameMapping array.
	 *
	 * @param values
	 *            the Map of values to convert
	 * @param nameMapping
	 *            the keys to extract from the Map (elements in the target array will be added in this order)
	 * @return the array of Objects
	 * @throws NullPointerException
	 *             if values or nameMapping is null
	 */
	public Object[] filterMapToObjectArray(final Map<String, ?> values, final String[] nameMapping) {

		if( values == null ) {
			throw new NullPointerException("values should not be null");
		} else if( nameMapping == null ) {
			throw new NullPointerException("nameMapping should not be null");
		}

		final Object[] targetArray = new Object[nameMapping.length];
		int i = 0;
		for( final String name : nameMapping ) {
			targetArray[i++] = values.get(name);
		}
		return targetArray;
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(final Map<String, ?> values, final String... nameMapping) throws IOException {
		super.incrementRowAndLineNo();
		super.writeRow(filterMapToObjectArray(values, nameMapping));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void write(final Map<String, ?> values, final String[] nameMapping, final CellProcessor[] processors)
		throws IOException {
		
		super.incrementRowAndLineNo();
		
		// execute the processors for each column
		executeProcessors(processedColumns, filterMapToList(values, nameMapping), processors);

		super.writeRow(processedColumns);
	}
}
