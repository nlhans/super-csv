package org.supercsv.io;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.util.CsvContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hans on 26-6-17.
 */
public class AbstractCsvProcessor {
    /**
     * Processes each element in the source List (using the corresponding processor chain in the processors array) and
     * adds it to the destination List. A <tt>null</tt> CellProcessor in the array indicates that no processing is
     * required and the element should be added as-is.
     *
     * @param destination
     *            the List to add the processed elements to (which is cleared before it's populated)
     * @param source
     *            the List of source elements to be processed
     * @param processors
     *            the array of CellProcessors used to process each element. The number of elements in this array must
     *            match the size of the source List. A <tt>null</tt> CellProcessor in this array indicates that no
     *            processing is required and the element should be added as-is.
     * @param lineNo
     *            the current line number
     * @param rowNo
     *            the current row number
     * @throws NullPointerException
     *             if destination, source or processors is null
     * @throws SuperCsvConstraintViolationException
     *             if a CellProcessor constraint failed
     * @throws SuperCsvException
     *             if source.size() != processors.length, or CellProcessor execution failed
     */
    protected void executeCellProcessors(final List<Object> destination, final List<?> source,
                                             final CellProcessor[] processors, final int lineNo, final int rowNo) {

        if( destination == null ) {
            throw new NullPointerException("destination should not be null");
        } else if( source == null ) {
            throw new NullPointerException("source should not be null");
        } else if( processors == null ) {
            throw new NullPointerException("processors should not be null");
        }

        // the context used when cell processors report exceptions
        final CsvContext context = new CsvContext(lineNo, rowNo, 1);
        context.setRowSource(new ArrayList<Object>(source));

        if( source.size() != processors.length ) {
            throw new SuperCsvException(String.format(
                "The number of columns to be processed (%d) must match the number of CellProcessors (%d): check that the number"
                    + " of CellProcessors you have defined matches the expected number of columns being read/written",
                source.size(), processors.length), context);
        }

        destination.clear();

        for( int i = 0; i < source.size(); i++ ) {

            context.setColumnNumber(i + 1); // update context (columns start at 1)

            if( processors[i] == null ) {
                destination.add(source.get(i)); // no processing required
            } else {
                destination.add(processors[i].execute(source.get(i), context)); // execute the processor chain
            }
        }
    }
}
