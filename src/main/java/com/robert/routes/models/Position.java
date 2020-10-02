package com.robert.routes.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Position {
    double latitude;
    double longitude;
    double timestamp;
    double speed;
}
