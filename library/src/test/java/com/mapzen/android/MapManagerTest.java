package com.mapzen.android;

import com.mapzen.android.lost.api.LostApiClient;
import com.mapzen.tangram.MapController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import android.location.Location;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyFloat;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.mapzen.tangram.*")
@PrepareForTest(MapManager.class)
public class MapManagerTest {

    MapController mapController;
    MapManager mapManager;

    @Before
    public void setup() throws Exception {
        mapController = PowerMockito.mock(TestMapController.class);
        LostApiClient lostApiClient = PowerMockito.mock(LostApiClient.class);
        mapManager = PowerMockito.spy(new MapManager(mapController, lostApiClient));
        PowerMockito.doNothing().when(mapManager, "addCurrentLocationMapDataToMap");
        PowerMockito.doNothing().when(mapManager, "handleMyLocationEnabledChanged");
    }

    @Test
    public void setMyLocationEnabled_shouldCenterMapOnCurrentLocation() throws Exception {
        Mockito.doCallRealMethod().when(mapController).setMapPosition(anyDouble(), anyDouble(),
                anyFloat());
        Mockito.when(mapController.getMapPosition()).thenCallRealMethod();

        mapManager.setMyLocationEnabled(true);
        Location location = new Location("test");
        location.setLongitude(-40.0);
        location.setLatitude(-70.0);
        Whitebox.invokeMethod(mapManager.locationListener, "onLocationChanged", location);
        assert (mapController.getMapPosition().latitude == location.getLatitude());
        assert (mapController.getMapPosition().longitude == location.getLongitude());
    }

    @Test
    public void setMyLocationEnabled_shouldNotChangeZoomLevel() throws Exception {
        Mockito.doCallRealMethod().when(mapController).setMapPosition(anyDouble(), anyDouble(),
                anyFloat());
        Mockito.doCallRealMethod().when(mapController).setMapZoom(anyFloat());
        Mockito.when(mapController.getMapZoom()).thenCallRealMethod();

        mapController.setMapZoom(17);
        mapManager.setMyLocationEnabled(true);
        Location location = new Location("test");
        Whitebox.invokeMethod(mapManager.locationListener, "onLocationChanged", location);
        assert (mapController.getMapZoom() == 17);
    }

    @Test
    public void setMyLocationEnabled_shouldNotChangeTilt() throws Exception {
        Mockito.doCallRealMethod().when(mapController).setMapPosition(anyDouble(), anyDouble(),
                anyFloat());
        Mockito.doCallRealMethod().when(mapController).setMapTilt(anyFloat());
        Mockito.when(mapController.getMapTilt()).thenCallRealMethod();

        mapController.setMapTilt(8);
        mapManager.setMyLocationEnabled(true);
        Location location = new Location("test");
        Whitebox.invokeMethod(mapManager.locationListener, "onLocationChanged", location);
        assert (mapController.getMapTilt() == 8);
    }

    @Test
    public void setMyLocationEnabled_shouldNotChangeCameraType() throws Exception {
        Mockito.doCallRealMethod().when(mapController).setMapPosition(anyDouble(), anyDouble(),
                anyFloat());
        Mockito.doCallRealMethod().when(mapController).setMapCameraType(any(
                MapController.CameraType.class));
        Mockito.when(mapController.getMapCameraType()).thenCallRealMethod();

        mapController.setMapCameraType(MapController.CameraType.ISOMETRIC);
        mapManager.setMyLocationEnabled(true);
        Location location = new Location("test");
        Whitebox.invokeMethod(mapManager.locationListener, "onLocationChanged", location);
        assert (mapController.getMapCameraType() == MapController.CameraType.ISOMETRIC);
    }

    @Test
    public void setMyLocationEnabled_shouldNotChangeRotation() throws Exception {
        Mockito.doCallRealMethod().when(mapController).setMapPosition(anyDouble(), anyDouble(),
                anyFloat());
        Mockito.doCallRealMethod().when(mapController).setMapRotation(anyFloat());
        Mockito.when(mapController.getMapRotation()).thenCallRealMethod();

        mapController.setMapRotation(8);
        mapManager.setMyLocationEnabled(true);
        Location location = new Location("test");
        Whitebox.invokeMethod(mapManager.locationListener, "onLocationChanged", location);
        assert (mapController.getMapRotation() == 8);
    }
}
