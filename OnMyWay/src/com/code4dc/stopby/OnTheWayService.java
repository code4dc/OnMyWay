package com.code4dc.stopby;

import java.io.IOException;

import net.sf.sprockets.google.Places;
import net.sf.sprockets.google.Places.Field;
import net.sf.sprockets.google.Places.Params;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Duration;

public class OnTheWayService {

    public static DirectionsRoute[] findBestDestination(String origin, String dest,
	    String placeName) throws IOException {
	
	Places.nearbySearch(new Params().name(placeName), Field.GEOMETRY);
	
	GeoApiContext context = new GeoApiContext()
		.setApiKey("AIzaSyCzBKtXjbTZkkqBIFLVsFefCMw6z_y6Ack");

	DirectionsApiRequest directions = DirectionsApi.getDirections(context,
		origin,
		dest);
	try {
	    // Get Original Directions
	    directions.alternatives(false);
	    DirectionsRoute[] route = directions.await();
	    for (int i = 0; i < route.length; i++) {
		Duration originalDuration = route[i].legs[0].duration;
		System.out.println("Direct duration: " + originalDuration);
	    }

	    // Get Directions with waypoint
	    directions = DirectionsApi.getDirections(context,
		    origin,
		    dest);
	    directions.waypoints("1340 Smith Ave Baltimore, MD 21209");
	    route = directions.await();
	    long totalDuration = 0;
	    for (int i = 0; i < route.length; i++) {
		for (int j = 0; j < route[i].legs.length; j++) {
		    Duration stepDuration = route[i].legs[j].duration;
		    totalDuration = +stepDuration.inSeconds;
		    System.out.println("Leg " + j + " duration: "
			    + stepDuration);
		}
	    }
	    System.out.println("Total Duration with 1st choice stop: "
		    + totalDuration / 60.0);

	    // Get Directions with waypoint
	    directions = DirectionsApi.getDirections(context,
		    origin,
		    dest);
	    directions.waypoints("4400 Evans Chapel Road Baltimore, MD 21211");
	    route = directions.await();
	    totalDuration = 0;
	    for (int i = 0; i < route.length; i++) {
		for (int j = 0; j < route[i].legs.length; j++) {
		    Duration stepDuration = route[i].legs[j].duration;
		    totalDuration = +stepDuration.inSeconds;
		    System.out.println("Leg " + j + " duration: "
			    + stepDuration);
		}
	    }
	    System.out.println("Total Duration with 2nd choice stop: "
		    + totalDuration / 60.0);

	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return null;

    }

}
