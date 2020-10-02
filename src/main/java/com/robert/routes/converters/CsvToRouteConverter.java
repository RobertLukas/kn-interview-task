package com.robert.routes.converters;

import com.robert.routes.models.Pair;
import com.robert.routes.models.Position;
import com.robert.routes.models.Route;
import com.robert.routes.utils.CsvRecordConverter;
import com.robert.routes.utils.DistanceCalculator;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * Implementation of {@link CsvRecordConverter}
 */
public class CsvToRouteConverter implements CsvRecordConverter<Route> {

    @Override
    public Route convertCsvRecord(CSVRecord record) {
        if (record != null) {
            Route route = new Route();
            route.setId(record.get("id"));
            route.setFrom_seq(Integer.parseInt(record.get("from_seq")));
            route.setTo_seq(Integer.parseInt(record.get("to_seq")));
            route.setFrom_port(record.get("from_port"));
            route.setTo_port(record.get("to_port"));
            route.setLeg_duration(Integer.parseInt(record.get("leg_duration")));
            route.setCount(Integer.parseInt(record.get("count")));
            route.setPoints(convertStringToArray(record.get("points")));
            route.setDistance(calculateDistance(route.getPoints()));

            return route;
        }
        return null;
    }

    private double calculateDistance(List<Position> points) {
        return twoGrams(points)
                .stream()
                .map(this::calculateDistance)
                .reduce(0.0, Double::sum);
    }

    private double calculateDistance(Pair<Position, Position> points) {
        Position left = points.getLeft();
        Position right = points.getRight();

        return DistanceCalculator.distance(left.getLatitude(), right.getLatitude(), left.getLongitude(), right.getLongitude(), 0, 0);
    }

    private <T> List<Pair<T, T>> twoGrams(List<T> list) {
        return IntStream.range(0, list.size() - 3)
                .mapToObj(i -> new ArrayList<>(list.subList(i, i + 2)))
                .map(arr -> new Pair<>(arr.get(0), arr.get(1)))
                .collect(Collectors.toList());
    }

    private List<Position> convertStringToArray(String stringArr) {
        return Arrays.stream(stringArr.substring(2, stringArr.length() - 2).replace("], [", "x").split("x"))
                .map(point -> {
                    List<Double> doubles = Arrays.stream(point.trim().split(","))
                            .map(val -> val.replace("null", "0.0"))
                            .map(Double::parseDouble)
                            .collect(toList());
                    double latitude = doubles.get(0);
                    double longitude = doubles.get(1);
                    double timestamp = doubles.get(2);
                    double speed = doubles.get(3);
                    return new Position(latitude, longitude, timestamp, speed);
                })
                .collect(Collectors.toList());
    }
}
