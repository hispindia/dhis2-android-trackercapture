package org.hisp.dhis.android.trackercapture.activities;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;


import org.hisp.dhis.android.sdk.persistence.models.BaseValue;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.hisp.dhis.android.trackercapture.R;
import org.hisp.dhis.client.sdk.ui.activities.BaseActivity;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

import static org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.AbsDatePickerRow.EMPTY_FIELD;
import static org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.QuestionCoordinatesRow.UNDEFINED;


public class MapActivity extends BaseActivity{
    MapView map = null;
    public static String ATTRIBUTE_COORDINATES = "coordinate_attributes_extra";
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

        ArrayList<OverlayItem> items = new ArrayList<>();

        ArrayList<TrackedEntityAttributeValue> values =
        (ArrayList<TrackedEntityAttributeValue>) getIntent().getExtras()
         .get(ATTRIBUTE_COORDINATES);
        GeoPoint camera = null;
         if(values!=null){

                 for(TrackedEntityAttributeValue value :values){
            if(!getLatitudeFromValue(value).equals("")) {
                    GeoPoint temp = new GeoPoint(
                                    Double.parseDouble(getLatitudeFromValue(value)),
                                    Double.parseDouble(getLongitudeFromValue(value)));
                    items.add(new OverlayItem("","",temp));
                    camera =temp;
                }
             }

         }
        ItemizedIconOverlay<OverlayItem> mOverlay = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(),items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(int index, OverlayItem item) {
                        return false;
                    }
                });



            mOverlay.setFocus(mOverlay.getItem(0));
         map.getOverlays().add(mOverlay);


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
