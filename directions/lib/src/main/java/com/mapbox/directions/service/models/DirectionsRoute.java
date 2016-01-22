package com.mapbox.directions.service.models;

import com.mapbox.directions.MapboxDirections;

import java.util.ArrayList;
import java.util.List;

/**
 * Gives detailed information about an individual route such as the duration, distance and geometry.
 */
public class DirectionsRoute {

    private int distance;
    private int duration;
    private String summary;
    private RouteGeometry geometry;
    private List<RouteStep> steps;

    public DirectionsRoute() {
        steps = new ArrayList<>();
    }

    /**
     * The distance traveled from origin to destination.
     * @return an integer number with unit meters.
     */
    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * The estimated travel time from origin to destination.
     * @return an integer number with unit seconds.
     */
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * A short, human-readable summary of the route (e.g., major roads traversed).
     * @return a string briefly describing route.
     */
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Gives the geometry of the route. Commonly used to draw the route on the mapView.
     * @return {@link RouteGeometry} object.
     */
    public RouteGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(RouteGeometry geometry) {
        this.geometry = geometry;
    }

    /**
     * Steps gives you the route instructions.
     * @return List of {@link RouteStep} objects.
     */
    public List<RouteStep> getSteps() {
        return steps;
    }

    public void setSteps(List<RouteStep> steps) {
        this.steps = steps;
    }

    /**
     * Computes whether the given point lies on or near a polyline, given a tolerance value in
     * miles. This method is commonly used to determine whether a users current geolocation is
     * still following the route.
     * @param point the {@link Waypoint} in which we are trying to determine whether near polyline.
     *              Often the devices current location.
     * @param polyline List of {@link Waypoint} making up the polyline. It's advised to pass a List
     *                 of size greater then 2 {@link Waypoint} to get accurate results.
     * @param tolerance double with unit miles.
     * @return true if distance between point and polyline is greater then tolerance.
     */
    public static Boolean isOffRoute(Waypoint point, List<Waypoint> polyline, double tolerance){

        double distance;
        double shortestDistance = 0.0;
        int closest = 0;

        if(polyline.size() == 0){
            return false;
        }

        // Find closest polyline Waypoint to point
        for(int i=0; i<polyline.size(); i++){
            distance = computeDistance(point, polyline.get(i));
            if(i == 0) shortestDistance = distance;
            if(distance < shortestDistance){
                shortestDistance = distance;
                closest = i;
            }
        }

        // If closest Waypoints an endpoint on the polyline just measure the distance as it's always
        // going to be the closest
        if(closest == 0 || closest == polyline.size()-1){
            shortestDistance = computeDistance(point, polyline.get(closest));
        }else {
            // Inorder to increase accuracy in determining closest polyline waypoint to point we must
            // increase the waypoint samples avaliable along the polyline section. We do this by
            // adding a waypoint every x amount of length along the polyline section. This makes our
            // sample evenly distributed across the polyline section even when a paticular polyline
            // segment is long.

            // After so much length along polyline create another waypoint. The larger the accuracy,
            // the shorter the distance between waypoints
            // TODO formula needs tweaking as it doesn't include user defined tolerance in anyway
            double dis = computeDistance(polyline.get(closest - 1), polyline.get(closest + 1));
            double accuracy = (dis * 5280) / 20;

            List<Waypoint> Sample = pointsBetween(polyline.get(closest - 1), polyline.get(closest + 1), accuracy);

            // Using the sample Waypoints, we now find the shortest distance between point and polyline
            for (int i = 0; i < Sample.size(); i++) {
                distance = computeDistance(point, Sample.get(i));
                if (i == 0) shortestDistance = distance;
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                }
            }
        }

        // Once the shortestDistance between point and polyline is found, we compare value to
        // tolerance and return true if shortestDistance is larger
        return shortestDistance > tolerance;
    }

    /**
     * Same as {@link #isOffRoute(Waypoint, List, double)} with a default tolerance of 0.1 miles.
     */
    public static Boolean isOffRoute(Waypoint point, List<Waypoint> polyline){
        return isOffRoute(point, polyline, MapboxDirections.OFF_ROUTE_THRESHOLD);
    }

    /**
     * gives a list of intermediate points between two given Waypoints
     * More info at: http://williams.best.vwh.net/avform.htm#Intermediate
     */
    private static List<Waypoint> pointsBetween(Waypoint start, Waypoint finish, double smoothness){

        List<Waypoint> pointsBetween = new ArrayList<>();

        // Check if start and finish Waypoints are the same
        if(start == finish) {
            pointsBetween.add(start);
        } else {
            for(int i = 0; i<smoothness; i++) {

                double lat1 = start.getLatitude();
                double lon1 = start.getLongitude();
                double lat2 = finish.getLatitude();
                double lon2 = finish.getLongitude();
                double f = i * (1 / smoothness);

                lat1 = Math.toRadians(lat1);
                lat2 = Math.toRadians(lat2);
                lon1 = Math.toRadians(lon1);
                lon2 = Math.toRadians(lon2);

                double deltaLat = (lat2 - lat1);
                double deltaLon = (lon2 - lon1);

                double m = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) + Math.cos(lat1) * Math.cos(lat2)
                        * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
                double distance = 2 * Math.atan2(Math.sqrt(m), Math.sqrt(1 - m));

                double a = Math.sin((1 - f) * distance) / Math.sin(distance);
                double b = Math.sin(f * distance) / Math.sin(distance);
                double x = a * Math.cos(lat1) * Math.cos(lon1) + b * Math.cos(lat2) * Math.cos(lon2);
                double y = a * Math.cos(lat1) * Math.sin(lon1) + b * Math.cos(lat2) * Math.sin(lon2);
                double z = a * Math.sin(lat1) + b * Math.sin(lat2);
                double lat3 = Math.atan2(z, Math.sqrt((x * x) + (y * y)));
                double lon3 = Math.atan2(y, x);

                pointsBetween.add(new Waypoint(Math.toDegrees(lon3), Math.toDegrees(lat3)));
            }
        }
        return pointsBetween;
    }

    /**
     * Compute distance between two different {@link Waypoint}
     * @param from first {@link Waypoint}
     * @param to second {@link Waypoint}
     * @return distance between two {@link Waypoint}s in miles
     */
    private static double computeDistance(Waypoint from, Waypoint to) {
        double dLat = Math.toRadians(to.getLatitude() - from.getLatitude());
        double dLon = Math.toRadians(to.getLongitude() - from.getLongitude());
        double lat1 = Math.toRadians(from.getLatitude());
        double lat2 = Math.toRadians(to.getLatitude());

        double a = Math.pow(Math.sin(dLat/2), 2) + Math.pow(Math.sin(dLon/2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double R = 3960;

        double distance = R * c;
        return distance;
    }
}
