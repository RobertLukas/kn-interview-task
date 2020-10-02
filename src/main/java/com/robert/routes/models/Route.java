package com.robert.routes.models;

import lombok.Data;

import java.util.List;

@Data
public class Route {
    String id;
    int from_seq;
    int to_seq;
    String from_port;
    String to_port;
    int leg_duration;
    int count;
    List<Position> points;
    double distance;
}
