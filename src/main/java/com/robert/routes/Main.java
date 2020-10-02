package com.robert.routes;

import com.robert.routes.converters.CsvToRouteConverter;
import com.robert.routes.models.Pair;
import com.robert.routes.models.Route;
import com.robert.routes.utils.CSVLoader;
import com.robert.routes.utils.GosJsonWriter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

public class Main {

    public static void main(String[] args) throws IOException {
        List<Route> routes = CSVLoader.readBooksFromCSV("input/DEBRV_DEHAM_historical_routes.csv", new CsvToRouteConverter());

        // group the routes by track id
        Map<Pair<Integer, Integer>, List<Route>> routesGroupBySeq = routes.stream()
                .collect(groupingBy(route -> new Pair<>(route.getFrom_seq(), route.getTo_seq())));

        // calc the average of all distances
        double distanceAverage = routes.stream().mapToDouble(Route::getDistance).average().orElse(0.0);

        // find the key where the route length is least different from the average
        Pair<Integer, Integer> bestRouteKeys = routesGroupBySeq.entrySet().stream()
                .collect(toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream().mapToDouble(Route::getDistance).average().orElse(0.0)
                ))
                .entrySet()
                .stream()
                .min((r1, r2) -> (int) (Math.abs(distanceAverage - r1.getValue()) - Math.abs(distanceAverage - r2.getValue())))
                .map(Map.Entry::getKey)
                .orElse(null);

        // find routes by best key
        List<Route> bestRoutes = routesGroupBySeq.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(bestRouteKeys))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(Collections.emptyList());

        // calc the average of all duration
        double averageDurationBestRoute = bestRoutes.stream().mapToDouble(Route::getLeg_duration).average().orElse(0.0);

        // find best Route where the duration is least different from the average
        bestRoutes.stream()
                .min((r1, r2) -> (int) (Math.abs(averageDurationBestRoute - r1.getLeg_duration()) - Math.abs(averageDurationBestRoute - r2.getLeg_duration())))
                .ifPresent(route -> GosJsonWriter.writeRouteToGeoJson(route, "output/DEBRV_DEHAM_best_route.geojson"));
    }
}
