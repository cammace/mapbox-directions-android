package com.mapbox.directions.service.models;

import java.util.ArrayList;
import java.util.List;

/**
 * A Route will contain geometry which is described in the form of coordinates. Typically used to
 * draw the route on a mapView.
 */
public class RouteGeometry {

    private String type;
    private List<List<Double>> coordinates;

    public RouteGeometry() {
        coordinates = new ArrayList<>();
    }

    /**
     * Gives GeoJSON geometry type which can be Point, LineString, Polygon, or Multipolygon. Should
     * always be "LineString" however, as the geometry objects always going to be a route which has
     * an origin and destination that are different coordinates.
     * @return string naming GeoJSON geometry type.
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Here is where the coordinates making up the route are given.
     * The inner List is just a coordinate. It will always be a size of 2 with the first object
     * (index 0) being Longitude and second (index 1) being Latitude. Since we are creating a route
     * we'll always have more then one coordinate which is were the outer List comes to play. Size
     * of this List typically depends on the length of the route, the longer it is the bigger the
     * List size becomes. The origin or starting point of the route will be the first object (index 0)
     * while the destination or finishing point of the route will be the last object in List
     * (index {@code List.Size()-1}). When given to opportunity, always use {@link #getWaypoints()}
     * instead as it puts Latitude and Longitude into a {@link Waypoint}.
     * @return List of coordinates making up the route.
     */
    public List<List<Double>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<Double>> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Create {@link Waypoint}s from the Longitude Latitude values within {@link #coordinates} inner list.
     * This makes it magnitudes easier to navigate as now there is only one List still inorder. The
     * origin or starting point of the route will be the first object (index 0) while the
     * destination or finishing point of the route will be the last object in List (index {@code List.Size()-1}).
     * @return List of waypoints making up a route.
     */
    public List<Waypoint> getWaypoints() {
        List<Waypoint> waypoints = new ArrayList<>();

        // Parse raw pairs
        List<List<Double>> coordinates = this.getCoordinates();
        for (List<Double> coordinate: coordinates) {
            waypoints.add(new Waypoint(coordinate.get(0), coordinate.get(1)));
        }

        return waypoints;
    }

}
