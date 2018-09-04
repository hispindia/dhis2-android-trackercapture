package org.hisp.dhis.android.trackercapture.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.hisp.dhis.android.sdk.persistence.models.BaseValue;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.hisp.dhis.android.trackercapture.R;
import org.hisp.dhis.client.sdk.ui.activities.BaseActivity;

import java.util.ArrayList;

import static org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.AbsDatePickerRow.EMPTY_FIELD;
import static org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.QuestionCoordinatesRow.UNDEFINED;


public class MapActivity extends BaseActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    public static String ATTRIBUTE_COORDINATES = "coordinate_attributes_extra";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ArrayList<TrackedEntityAttributeValue> values =
                (ArrayList<TrackedEntityAttributeValue>) getIntent().getExtras()
                        .get(ATTRIBUTE_COORDINATES);
        if(values!=null){
            LatLng camera = null;
            for(TrackedEntityAttributeValue value :values){
                if(!getLatitudeFromValue(value).equals("")) {
                    LatLng temp = new LatLng(
                            Double.parseDouble(getLatitudeFromValue(value)),
                            Double.parseDouble(getLongitudeFromValue(value)));
                    mMap.addMarker(new MarkerOptions().position(temp));
                    camera =temp;
                }
            }
            if(camera!=null)mMap.moveCamera(CameraUpdateFactory.newLatLng(camera));
            
        }



//        LatLng sydney = new LatLng(-34,151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Synd"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
