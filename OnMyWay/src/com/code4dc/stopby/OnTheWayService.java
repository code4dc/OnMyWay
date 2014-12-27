package com.code4dc.stopby;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.sprockets.google.Place;
import net.sf.sprockets.google.Places;
import net.sf.sprockets.google.Places.Params;

import org.apache.commons.collections4.CollectionUtils;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Duration;
import com.google.maps.model.GeocodingResult;

public class OnTheWayService {

	public static DirectionsRoute[] findBestDestination(String origin,
			String dest, String placeName) throws IOException {
		GeoApiContext context = new GeoApiContext()
		.setApiKey("AIzaSyCzBKtXjbTZkkqBIFLVsFefCMw6z_y6Ack");

		GeocodingApiRequest originRequest = GeocodingApi.geocode(context, origin);
		GeocodingResult originAddress = null;
		
		try {
			originAddress = originRequest.await()[0];
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		List<Place> allPlaces = Places.nearbySearch(
				new Params()
						.name(placeName)
						.location(originAddress.geometry.location.lat,
								originAddress.geometry.location.lng)
						.radius(5000).maxResults(20))
				.getResult();
		
		
		DirectionsApiRequest directions = DirectionsApi.getDirections(context,
				origin, dest);
		
		Set<String> completedAddresses = new HashSet<String>();
		
		try {
			// Get Original Directions
			directions.alternatives(false);
			DirectionsRoute[] route = directions.await();
			for (int i = 0; i < route.length; i++) {
				Duration originalDuration = route[i].legs[0].duration;
				System.out.println("Direct duration: " + originalDuration);
			}
			
			for (Place currentPlace : allPlaces) {
				// Get Directions with waypoint 1
				if(currentPlace.getVicinity() == null) continue; 
				
				String addr = currentPlace.getVicinity();
				if(completedAddresses.contains(addr)) continue;
				
				directions = DirectionsApi.getDirections(context, origin, dest);
				directions.waypoints(addr);
				route = directions.await();
				long totalDuration = 0;
				for (int i = 0; i < route.length; i++) {
					for (int j = 0; j < route[i].legs.length; j++) {
						Duration stepDuration = route[i].legs[j].duration;
						totalDuration += stepDuration.inSeconds;
						System.out.println("Leg " + j + " duration: "
								+ stepDuration);
					}
				}
				completedAddresses.add(addr);
				System.out.println("Total duration for " + currentPlace.getName() + " stop: "
						+ totalDuration/60.0 + " at " + addr);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

}
