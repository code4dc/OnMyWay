package com.code4dc.onmyway;

import java.io.IOException;

import com.code4dc.stopby.OnTheWayService;

public class APITester {

	public static void main(String[] args) {
		try {
		    OnTheWayService.findBestDestination( "2400 Research Blvd Rockville, MD",
		        "132 S Washington St Baltimore, MD", "Target");
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	}

}
