package org.hisp.dhis.android.trackercapture.activities;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;


import org.hisp.dhis.android.sdk.persistence.models.BaseValue;
import org.hisp.dhis.android.sdk.persistence.models.DataValue;
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
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            map.setHasTransientState(true);
        }
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);


        ArrayList<TrackedEntityAttributeValue> values =
        (ArrayList<TrackedEntityAttributeValue>) getIntent().getExtras()
         .get(ATTRIBUTE_COORDINATES);
         if(values!=null){

                 for(TrackedEntityAttributeValue value :values){
                        if(!getLatitudeFromValue(value).equals("")) {
                                Marker marker =  new Marker(map);
                                marker.setIcon( ContextCompat.getDrawable(getApplicationContext(),R.drawable.lastknown_location_icon));

                                GeoPoint tempGeo = new GeoPoint(
                                                Double.parseDouble(getLatitudeFromValue(value)),
                                                Double.parseDouble(getLongitudeFromValue(value)));
                                marker.setPosition(tempGeo);
                                map.getOverlays().add(marker);

                            }
                }

         }

        ArrayList<DataValue> dataValues =
                (ArrayList<DataValue>) getIntent().getExtras()
                        .get(DATAELEMENT_COORDINATES);
        if(dataValues!=null){

            for(DataValue value :dataValues){
                if(!getLatitudeFromValue(value).equals("")) {
                    Marker marker =  new Marker(map);
                    marker.setIcon( ContextCompat.getDrawable(getApplicationContext(),R.drawable.persons_location_icon));

                    GeoPoint tempGeo = new GeoPoint(
                            Double.parseDouble(getLatitudeFromValue(value)),
                            Double.parseDouble(getLongitudeFromValue(value)));
                    marker.setPosition(tempGeo);
                    map.getOverlays().add(marker);

                }
            }

        }




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
