package com.mapbox.directions.service.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes the geometry of a {@link DirectionsFeature}.
 */
public class FeatureGeometry {

    private String type;
    private List<Double> coordinates;

    public FeatureGeometry() {
        coordinates = new ArrayList<>();
    }

    /**
     * Gives GeoJSON geometry type which can be Point, LineString, Polygon, or Multipolygon. Should
     * always be "Point".
     * @return string naming GeoJSON geometry type.
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gives the coordinate of the Point. Longitude will always be first (index 0)in list and
     * latitude will be second (index 1).
     * @return List of Double objects containing Point Longitude and Latitude.
     */
    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
