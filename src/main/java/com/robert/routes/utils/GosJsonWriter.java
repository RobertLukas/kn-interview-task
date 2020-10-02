package com.robert.routes.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.robert.routes.models.Position;
import com.robert.routes.models.Route;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GosJsonWriter {

    public static void writeRouteToGeoJson(Route route, String outputPath) {

        JsonObject geometry = new JsonObject();
        geometry.addProperty("type", "LineString");
        geometry.add("coordinates", toGeoArray(route.getPoints()));

        JsonObject properties = new JsonObject();
        properties.addProperty("Description", "Best Route");
        properties.addProperty("from_seq", route.getFrom_seq());
        properties.addProperty("to_seq", route.getTo_seq());
        properties.addProperty("stroke", "red");

        JsonObject feature = new JsonObject();
        feature.addProperty("type", "Feature");
        feature.add("properties", properties);
        feature.add("geometry", geometry);

        JsonArray features = new JsonArray();
        features.add(feature);

        JsonObject featureCollection = new JsonObject();
        featureCollection.addProperty("type", "FeatureCollection");
        featureCollection.add("features", features);

        try (FileWriter fileWriter = new FileWriter(outputPath)) {
            fileWriter.write(featureCollection.toString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static JsonArray toGeoArray(List<Position> points) {
        JsonArray geometry = new JsonArray();
        points.stream().map(position -> {
            JsonArray element = new JsonArray(2);
            element.add(position.getLatitude());
            element.add(position.getLongitude());
            return element;
        }).forEach(geometry::add);
        return geometry;
    }
}
