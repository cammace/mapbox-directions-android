package com.mapbox.directions.service.models;

/**
 * Defines a <a href="http://geojson.org/geojson-spec.html#feature-objects">GeoJSON Feature Object</a>
 * with a Point geometry type.
 */
public class DirectionsFeature {

    private String type;
    private FeatureGeometry geometry;
    private FeatureProperties properties;

    /**
     * Commonly used identifier.
     * @return string type.
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * {@link FeatureGeometry} object contains type and the coordinate.
     * @return {@link FeatureGeometry} object.
     */
    public FeatureGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(FeatureGeometry geometry) {
        this.geometry = geometry;
    }

    /**
     * Properties describing the point. This includes, at a minimum, a name.
     * @return {@link FeatureProperties} object.
     */
    public FeatureProperties getProperties() {
        return properties;
    }

    public void setProperties(FeatureProperties properties) {
        this.properties = properties;
    }
}
