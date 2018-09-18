package org.hisp.dhis.android.trackercapture.activities;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import org.osmdroid.util.BoundingBox;
import android.view.View;
import java.util.List;
import org.hisp.dhis.android.sdk.controllers.GpsController;
import org.hisp.dhis.android.sdk.controllers.tracker.TrackerController;
import org.hisp.dhis.android.sdk.persistence.models.BaseValue;
import org.hisp.dhis.android.sdk.persistence.models.DataValue;
import org.hisp.dhis.android.sdk.persistence.models.Event;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.hisp.dhis.android.trackercapture.R;
import org.hisp.dhis.client.sdk.ui.activities.BaseActivity;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

import static org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.AbsDatePickerRow.EMPTY_FIELD;
import static org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.QuestionCoordinatesRow.UNDEFINED;


public class MapActivity extends BaseActivity{
    MapView map = null;
    public static String ATTRIBUTE_COORDINATES = "coordinate_attributes_extra";
    public static final String DATAELEMENT_COORDINATES = "data_element_coordinates:extra";
    public static final String MANUAL_ID_ATTRIBUTE = "I7OncVzPZKS";
    public static final String EXPOSURE_ID_ATTRIBUTE = "k4gkDsoA1Tp";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GpsController.activateGps(getBaseContext());
        setContentView(R.layout.activity_map);
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            map.setHasTransientState(true);
        }
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);


        List<GeoPoint> pointsforBoundingBox = new ArrayList<>();

        ArrayList<DataValue> values =
                (ArrayList<DataValue>) getIntent().getExtras()
                        .get(ATTRIBUTE_COORDINATES);
        if(values!=null){
            String manualId="";
            for(DataValue value :values){
                Event ev_=TrackerController.getEvent(value.getLocalEventId());
                if(ev_.getProgramStageId().equals("DbsGMk0zLxr")||ev_.getProgramStageId().equals("R8zfsjiFerK"))
                {
                    manualId =
                            TrackerController.getTrackedEntityAttributeValue(
                                    EXPOSURE_ID_ATTRIBUTE,TrackerController.getEvent(value.getLocalEventId()).getTrackedEntityInstance()
                            ).getValue();
                }
                else
                {
                    manualId =
                            TrackerController.getTrackedEntityAttributeValue(
                                    MANUAL_ID_ATTRIBUTE,TrackerController.getEvent(value.getLocalEventId()).getTrackedEntityInstance()
                            ).getValue();
                }


                if(!getLatitudeFromValue(value).equals("")) {
                    Marker marker =  new Marker(map);
                    marker.setIcon( ContextCompat.getDrawable(getApplicationContext(),R.drawable.lastknown_location_icon));

                    GeoPoint tempGeo = new GeoPoint(
                            Double.parseDouble(getLatitudeFromValue(value)),
                            Double.parseDouble(getLongitudeFromValue(value)));
                    marker.setPosition(tempGeo);
                    marker.setTitle(manualId);
                    map.getOverlays().add(marker);
                    pointsforBoundingBox.add(tempGeo);
//                    map.getController().animateTo(marker.getPosition());

                }
            }

        }

        ArrayList<DataValue> dataValues =
                (ArrayList<DataValue>) getIntent().getExtras()
                        .get(DATAELEMENT_COORDINATES);
        if(dataValues!=null){

            for(DataValue value :dataValues){
                if(!getLatitudeFromValue(value).equals("")) {
                    String manualId =
                            TrackerController.getTrackedEntityAttributeValue(
                                    MANUAL_ID_ATTRIBUTE,TrackerController.getEvent(value.getLocalEventId()).getTrackedEntityInstance()
                            ).getValue();
                    Marker marker =  new Marker(map);
                    marker.setIcon( ContextCompat.getDrawable(getApplicationContext(),R.drawable.persons_location_icon));

                    GeoPoint tempGeo = new GeoPoint(
                            Double.parseDouble(getLatitudeFromValue(value)),
                            Double.parseDouble(getLongitudeFromValue(value)));
                    marker.setPosition(tempGeo);
                    marker.setTitle(manualId);
                    map.getOverlays().add(marker);
                    pointsforBoundingBox.add(tempGeo);
//                    map.getController().animateTo(marker.getPosition());

                }
            }

        }

        Marker marker = new Marker(map);
        marker.setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.current_location_icon));
        GeoPoint tempGeo = new GeoPoint(GpsController.getLocation().getLatitude(),GpsController.getLocation().getLongitude());
        marker.setPosition(tempGeo);
        marker.setTitle("Current Location");
        map.getOverlays().add(marker);


        pointsforBoundingBox.add(tempGeo);
        BoundingBox bb = BoundingBox.fromGeoPoints(pointsforBoundingBox);
        map.zoomToBoundingBox(bb,false);
        map.getController().zoomToSpan(tempGeo.getLatitude(),tempGeo.getLongitude());
        map.setTilesScaledToDpi(true);
        map.getController().zoomTo(9);
        map.getController().animateTo(tempGeo);

    }


    @Override
    public void onResume(){
        super.onResume();

    }

    public static String getLatitudeFromValue(BaseValue baseValue) {
        if(baseValue == null || baseValue.getValue() == null)
            return "0";
        String value = baseValue.getValue();
        if (value.contains(",")) {
            String latitude = value.substring(value.indexOf(",") + 1, value.length()).replace("]", EMPTY_FIELD);
            if(!latitude.equals(UNDEFINED)){
                return latitude;
            }
        }
        return "";
    }

    public static String getLongitudeFromValue(BaseValue baseValue) {
        if(baseValue == null || baseValue.getValue() == null)
            return "0";
        String value = baseValue.getValue();
        if (value.contains(",")) {
            String longitude = value.substring(0, value.indexOf(",")).replace("[", EMPTY_FIELD);
            if(!longitude.equals(UNDEFINED)){
                return longitude;
            }
        }
        return "";
    }
}
