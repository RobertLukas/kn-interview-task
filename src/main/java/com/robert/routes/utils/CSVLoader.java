package com.robert.routes.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Csv files loader
 */
public class CSVLoader {

    /**
     * Load csv file content into a list
     *
     * @param inputPath           - input file path
     * @param csvToRouteConverter - the row converter
     * @param <T>                 - the type of object the Collection contains
     * @return list of object T
     * @throws IOException when the file is not present
     */
    public static <T> List<T> readBooksFromCSV(String inputPath, CsvRecordConverter<T> csvToRouteConverter) throws IOException {
        Optional<File> fileOpt = Optional.ofNullable(inputPath)
                .map(Paths::get)
                .map(Path::toFile);

        if (fileOpt.isPresent()) {
            CSVParser parser = CSVParser.parse(fileOpt.get(), StandardCharsets.UTF_8, CSVFormat.EXCEL.withHeader());
            return parser.getRecords()
                    .stream()
                    .map(csvToRouteConverter::convertCsvRecord)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}