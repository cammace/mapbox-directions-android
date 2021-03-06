package com.mapbox.directions.service.models;

/**
 * Properties describing a {@link DirectionsFeature}.
 */
public class FeatureProperties {

    private String name;

    /**
     * Gives the name of the closest street to DirectionFeature point.
     * @return String name.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
