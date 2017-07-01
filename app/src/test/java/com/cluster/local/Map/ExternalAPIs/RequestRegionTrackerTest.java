package com.cluster.local.Map.ExternalAPIs;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.junit.Test;

import static org.junit.Assert.*;


public class RequestRegionTrackerTest {

    @Test
    public void distanceBetweenLatLng() throws Exception {

       // float actual = RequestRegionTracker.distanceBetweenLatLng(new LatLng(49.034617, 12.104317), new LatLng(49.081748, 12.112345));
        float expected = 	5.27e3f;
    //    assertEquals(expected,actual,1.0);
    }

}