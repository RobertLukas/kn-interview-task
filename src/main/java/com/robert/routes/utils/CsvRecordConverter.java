package com.robert.routes.utils;

import com.robert.routes.converters.CsvToRouteConverter;
import org.apache.commons.csv.CSVRecord;

/**
 * {@link CsvToRouteConverter} is a class, that can perform {@link CSVRecord} to {@link T} conversion.
 *
 * @param <T> - the type of object
 */
public interface CsvRecordConverter<T> {

    /**
     * Convert the specified CSV record value into a T object
     *
     * @param record - {@link CSVRecord} value to be converted (may be null)
     * @return null if the input is not present, T if a present value was passed
     */
    T convertCsvRecord(CSVRecord record);
}
