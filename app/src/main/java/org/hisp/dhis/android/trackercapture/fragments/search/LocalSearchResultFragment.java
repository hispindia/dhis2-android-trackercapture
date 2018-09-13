package org.hisp.dhis.android.trackercapture.fragments.search;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.raizlabs.android.dbflow.structure.Model;
import com.squareup.otto.Subscribe;

import org.hisp.dhis.android.sdk.controllers.tracker.TrackerController;
import org.hisp.dhis.android.sdk.events.OnRowClick;
import org.hisp.dhis.android.sdk.events.OnTrackerItemClick;
import org.hisp.dhis.android.sdk.events.UiEvent;
import org.hisp.dhis.android.sdk.persistence.Dhis2Application;
import org.hisp.dhis.android.sdk.persistence.loaders.DbLoader;
import org.hisp.dhis.android.sdk.persistence.models.BaseSerializableModel;
import org.hisp.dhis.android.sdk.persistence.models.DataValue;
import org.hisp.dhis.android.sdk.persistence.models.Enrollment;
import org.hisp.dhis.android.sdk.persistence.models.Event;
import org.hisp.dhis.android.sdk.persistence.models.FailedItem;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.hisp.dhis.android.sdk.ui.activities.INavigationHandler;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.TrackerAssociateRowActionListener;
import org.hisp.dhis.android.sdk.ui.adapters.rows.events.EventRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.events.TrackedEntityInstanceItemRow;
import org.hisp.dhis.android.sdk.utils.UiUtils;
import org.hisp.dhis.android.trackercapture.R;
import org.hisp.dhis.android.trackercapture.activities.HolderActivity;
import org.hisp.dhis.android.trackercapture.fragments.TrackerAssociate.TrackerAssociateSearchResultFragment;
import org.hisp.dhis.android.trackercapture.fragments.programoverview.ProgramOverviewFragment;
import org.hisp.dhis.android.trackercapture.fragments.selectprogram.dialogs.ItemStatusDialogFragment;
import org.hisp.dhis.android.trackercapture.ui.adapters.TrackedEntityInstanceAdapter;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.text.TextUtils.isEmpty;

public class LocalSearchResultFragment extends Fragment implements LoaderManager.LoaderCallbacks<LocalSearchResultFragmentForm>,
                                                                    View.OnClickListener{
    public static final String EXTRA_PROGRAM = "extra:ProgramId";
    public static final String EXTRA_ORGUNIT = "extra:OrgUnitId";
    public static final String EXTRA_ATTRIBUTEVALUEMAP = "extra:AttributeValueMap";
    public static final String START_DATE = "extra:startDate";
    public static final String END_DATE = "extra:endDate";
    public static final String EXTRA_STAGE_ID = "extra:stageId";
    public static final String EXTRA_COORD_ATR = "extra:cord_atr_fl";
    public static final String EXTRA_COORD_DE = "extra:cord_de_fl";
    private static final String PERSONS_LOCATION_DATAELEMENT = "QMGWGK6wkET";

    private String orgUnitId;
    private String programId;
    private HashMap<String,String> attributeValueMap;
    private ListView searchResultsListView;
    private TrackedEntityInstanceAdapter mAdapter;
    private final int LOADER_ID = 1112222111;
    private LocalSearchResultFragmentForm mForm;
    private ProgressBar progressBar;
    private CardView cardView;
    private Button allinmap;

    String startDate;
    String endDate;
    String stageId;
    String cordAtrFl;
    String cordDeFl;

    private List<EventRow> completeEventRows;
    private int startIndex=1;
    private static final int ROWS_PER_PAGE = 10;
    private Button nextButton;
    private Button previousButton;
    private TextView infoText;

    public static LocalSearchResultFragment newInstance(String orgUnitId, String programId, HashMap<String,String> attributeValueMap,
                                                        String startDate,String endDate,String stageId, String cordAtrFlP,String cordDeFlP) {
        LocalSearchResultFragment fragment = new LocalSearchResultFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_ORGUNIT, orgUnitId);
        args.putString(EXTRA_PROGRAM, programId);
        args.putSerializable(EXTRA_ATTRIBUTEVALUEMAP, attributeValueMap);
        args.putString(START_DATE,startDate);
        args.putString(END_DATE,endDate);
        args.putString(EXTRA_STAGE_ID,stageId);
        args.putString(EXTRA_COORD_ATR,cordAtrFlP);
        args.putString(EXTRA_COORD_DE,cordDeFlP);
        fragment.setArguments(args);

        Log.d("HashMap size", attributeValueMap.size() + "");
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        orgUnitId = args.getString(EXTRA_ORGUNIT);
        programId = args.getString(EXTRA_PROGRAM);
        attributeValueMap = (HashMap) args.getSerializable(EXTRA_ATTRIBUTEVALUEMAP);
        startDate = args.getString(START_DATE);
        endDate = args.getString(END_DATE);
        stageId = args.getString(EXTRA_STAGE_ID);
        cordAtrFl = args.getString(EXTRA_COORD_ATR);
        cordDeFl = args.getString(EXTRA_COORD_DE);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_local_search_results,container,false);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            getActivity().finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getActivity() instanceof AppCompatActivity) {
            getActionBar().setDisplayShowTitleEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        searchResultsListView = (ListView) view.findViewById(R.id.listview_search_results);
        progressBar = (ProgressBar) view.findViewById(R.id.local_search_progressbar);
        cardView = (CardView) view.findViewById(R.id.search_online_cardview);
        allinmap = (Button) view.findViewById(R.id.showallinmap);
        nextButton = (Button) view.findViewById(R.id.next_set);
        previousButton = (Button) view.findViewById(R.id.prev_set);
        infoText = (TextView) view.findViewById(R.id.pag_info);
        mAdapter = new TrackedEntityInstanceAdapter(getLayoutInflater(savedInstanceState));
        searchResultsListView.setAdapter(mAdapter);
        progressBar.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.GONE);
        cardView.setOnClickListener(this);
        allinmap.setOnClickListener(this);
        ((Button)view.findViewById(R.id.filter)).setOnClickListener(this);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
    }


    private ActionBar getActionBar() {
        if (getActivity() != null &&
                getActivity() instanceof AppCompatActivity) {
            return ((AppCompatActivity) getActivity()).getSupportActionBar();
        } else {
            throw new IllegalArgumentException("Fragment should be attached to ActionBarActivity");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        final TrackedEntityInstanceItemRow itemRow = (TrackedEntityInstanceItemRow) searchResultsListView.getItemAtPosition(info.position);

        if(item.getTitle().toString().equals(getResources().getString(org.hisp.dhis.android.sdk.R.string.go_to_programoverview_fragment))) {
            HolderActivity.navigateToProgramOverviewFragment(getActivity(),
                            orgUnitId, programId, itemRow.getTrackedEntityInstance().getLocalId());
        } else if(item.getTitle().toString().equals(getResources().getString(org.hisp.dhis.android.sdk.R.string.delete))) {
            // if not sent to server, present dialog to user
            if( !(itemRow.getStatus().equals(OnRowClick.ITEM_STATUS.SENT))) {
                UiUtils.showConfirmDialog(getActivity(), getActivity().getString(R.string.confirm),
                        getActivity().getString(R.string.warning_delete_unsent_tei),
                        getActivity().getString(R.string.delete), getActivity().getString(R.string.cancel),
                        (R.drawable.ic_event_error),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                performSoftDeleteOfTrackedEntityInstance(itemRow.getTrackedEntityInstance());
                                dialog.dismiss();
                            }
                        });
            } else {
                //if sent to server, be able to soft delete without annoying the user
                performSoftDeleteOfTrackedEntityInstance(itemRow.getTrackedEntityInstance());
            }
        }
        return true;
    }

        public void performSoftDeleteOfTrackedEntityInstance(TrackedEntityInstance trackedEntityInstance) {
        List<Enrollment> enrollments = TrackerController.getEnrollments(programId, trackedEntityInstance);
        Enrollment activeEnrollment = null;
        for(Enrollment enrollment : enrollments) {
            if(Enrollment.ACTIVE.equals(enrollment.getStatus())) {
                activeEnrollment = enrollment;
            }
        }

        if(activeEnrollment != null) {
            List<Event> eventsForActiveEnrollment = TrackerController.getEventsByEnrollment(activeEnrollment.getLocalId());

            if(eventsForActiveEnrollment != null) {
                for(Event event : eventsForActiveEnrollment) {
                    event.delete();
                }
            }

            activeEnrollment.delete();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        Dhis2Application.getEventBus().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Dhis2Application.getEventBus().register(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getLoaderManager().restartLoader(LOADER_ID, getArguments(), this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ORGUNIT, orgUnitId);
        bundle.putString(EXTRA_PROGRAM, programId);
        bundle.putSerializable(EXTRA_ATTRIBUTEVALUEMAP, attributeValueMap);
        bundle.putString(START_DATE,startDate);
        bundle.putString(END_DATE,endDate);
        bundle.putString(EXTRA_STAGE_ID,stageId);
        bundle.putString(EXTRA_COORD_ATR,cordAtrFl);
        bundle.putString(EXTRA_COORD_DE,cordDeFl);
        getLoaderManager().initLoader(LOADER_ID, bundle, this);
    }


    @Override
    public Loader<LocalSearchResultFragmentForm> onCreateLoader(int id, Bundle args) {
        if (LOADER_ID == id && isAdded()) {
            String orgUnitId = args.getString(EXTRA_ORGUNIT);
            String programId = args.getString(EXTRA_PROGRAM);
            HashMap<String,String> attributeValueMap = (HashMap) args.getSerializable(EXTRA_ATTRIBUTEVALUEMAP);
            String startDate = args.getString(START_DATE);
            String endDate = args.getString(END_DATE);
            String stagefl = args.getString(EXTRA_STAGE_ID);
            String coordatrfl = args.getString(EXTRA_COORD_ATR);
            String coorddefl = args.getString(EXTRA_COORD_DE);
            List<Class<? extends Model>> modelsToTrack = new ArrayList<>();
            modelsToTrack.add(TrackedEntityInstance.class);
            modelsToTrack.add(Enrollment.class);
            modelsToTrack.add(Event.class);
            modelsToTrack.add(FailedItem.class);
            return new DbLoader<>(
                    getActivity().getBaseContext(), modelsToTrack,
                    new LocalSearchResultFragmentFormQuery(orgUnitId, programId,attributeValueMap,startDate,endDate,stagefl,coordatrfl,coorddefl));
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<LocalSearchResultFragmentForm> loader, LocalSearchResultFragmentForm data) {
        if (LOADER_ID == loader.getId()) {
            progressBar.setVisibility(View.GONE);
            mForm = data;
            Collections.sort(data.getEventRowList(), new Comparator<EventRow>() {
                        @Override
                        public int compare(EventRow lhs, EventRow rhs) {
                            if(lhs == null && rhs == null) {
                                return 0;
                            } else if (lhs == null) {
                                return -1;
                            } else if (rhs == null) {
                                return 1;
                            }

                            if(!(lhs instanceof TrackedEntityInstanceItemRow)
                                    && (!(rhs instanceof TrackedEntityInstanceItemRow))) {
                                return 0;
                            } else if (!(lhs instanceof TrackedEntityInstanceItemRow) ){
                                return -1;
                            } else if (!(rhs instanceof TrackedEntityInstanceItemRow) ) {
                                return 1;
                            }
                            DateTime lhsDate = ((TrackedEntityInstanceItemRow)lhs).getLatestEvent();
                            DateTime rhsDate = ((TrackedEntityInstanceItemRow)rhs).getLatestEvent();
                            if(isEmpty(lhsDate.toString()) && isEmpty(rhsDate.toString())) {
                                return 0;
                            } else if (isEmpty(lhsDate.toString())) {
                                return -1;
                            } else if (isEmpty(rhsDate.toString())) {
                                return 1;
                            }
                            if(lhsDate == null && rhsDate == null) {
                                return 0;
                            } else if (lhsDate == null) {
                                return -1;
                            } else if (rhsDate == null) {
                                return 1;
                            } else {
                                if(lhsDate.isBefore(rhsDate)) {
                                    return 1;
                                } else if(lhsDate.isAfter(rhsDate)) {
                                    return -1;
                                } else {
                                    return 0;
                                }
                            }
                        }
                    });

//                    mAdapter.swapData(data.getEventRowList());
            completeEventRows = data.getEventRowList();
            startIndex=1;
            refreshData();
        }
    }

    private void refreshData(){

        if(completeEventRows.size()>0){
            int ending = startIndex;
            if((startIndex+ROWS_PER_PAGE)>completeEventRows.size()){
                ending = completeEventRows.size();
            }else{
                ending = startIndex + ROWS_PER_PAGE;
            }
            List<EventRow> dataToShow = new ArrayList<>();
            dataToShow.add(completeEventRows.get(0));
            dataToShow.addAll(completeEventRows.subList(startIndex,ending));
            mAdapter.swapData(dataToShow);
            String text = "Showing "+(startIndex)+" - "+(startIndex+mAdapter.getCount()-2)+" of "+(completeEventRows.size()-1);
            infoText.setText(text);
        }

    }

    @Subscribe
    public void onItemClick(final OnTrackerItemClick eventClick) {
       if(!eventClick.isLongClick()){
            if (eventClick.isOnDescriptionClick()) {


                HolderActivity.navigateToProgramOverviewFragment(getActivity(),orgUnitId, programId,
                        eventClick.getItem().getLocalId());
            } else {
                showStatusDialog(eventClick.getItem());
            }
        }else{
            UiUtils.showConfirmDialog(getActivity(),getActivity().getString(R.string.confirm),
                    getActivity().getString(R.string.tracked_entity_instance_delete_dialog),
                    getActivity().getString(R.string.delete), getActivity().getString(R.string.cancel),
                    (R.drawable.ic_event_error),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //performSoftDeleteOfTrackedEntityInstance((TrackedEntityInstance) eventClick.getItem());
                            deleteSelectedRow(eventClick);
                            dialog.dismiss();
                        }
                    }
                    );
        }

    }



    private void deleteSelectedRow(final OnTrackerItemClick eventClick){
        if( !eventClick.getStatus().equals(OnRowClick.ITEM_STATUS.SENT)) {
            UiUtils.showConfirmDialog(getActivity(), getActivity().getString(R.string.confirm),
                    getActivity().getString(R.string.warning_delete_unsent_tei),
                    getActivity().getString(R.string.delete), getActivity().getString(R.string.cancel),
                    (R.drawable.ic_event_error),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            performSoftDeleteOfTrackedEntityInstance((TrackedEntityInstance) eventClick.getItem());
                            dialog.dismiss();
                        }
                    });
        } else {
            //if sent to server, be able to soft delete without annoying the user
            performSoftDeleteOfTrackedEntityInstance((TrackedEntityInstance) eventClick.getItem());
        }
    }



    public void showStatusDialog(BaseSerializableModel model) {
        ItemStatusDialogFragment fragment = ItemStatusDialogFragment.newInstance(model);
        fragment.show(getChildFragmentManager());
    }

    @Override
    public void onLoaderReset(Loader<LocalSearchResultFragmentForm> loader) {

    }

    public void searchOnline() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_online_cardview: {
                searchOnline();
                break;
            }
            case R.id.showallinmap:
                showMap();
                break;

            case R.id.filter:
                showFilter();
                break;

            case R.id.next_set:
                if(startIndex+ROWS_PER_PAGE<completeEventRows.size()){
                    startIndex+=ROWS_PER_PAGE;
                }
                refreshData();
                break;

            case R.id.prev_set:
                if(startIndex-ROWS_PER_PAGE>=0){
                    startIndex-=ROWS_PER_PAGE;
                }else{
                    startIndex=1;
                }
                refreshData();
                break;
        }
    }

    private void showFilter(){
        HolderActivity.navigateToLocalSearchFragment(getActivity(),orgUnitId,programId);
    }

    private void showMap(){
        List<TrackedEntityAttributeValue> data = new ArrayList<>();
        List<DataValue> persons_locations = new ArrayList<>();
        for(EventRow eventRow :mForm.getEventRowList()){
            if(eventRow instanceof TrackedEntityInstanceItemRow){
                Map<String, TrackedEntityAttributeValue> attributes = ((TrackedEntityInstanceItemRow) eventRow).getAttributes();
                if(attributes != null){
                    data.add(attributes.get("x8iA6APPjTm"));//last known attribute values
                }
                TrackedEntityInstance trackedEntityInstance = ((TrackedEntityInstanceItemRow) eventRow).getTrackedEntityInstance();
                for(Enrollment enrollment:TrackerController.getEnrollments(programId,trackedEntityInstance)){
                    List<Event> events = TrackerController.getEventsByEnrollment(enrollment.getLocalId());
                    for(Event event : events){
                        if(event.getProgramStageId().equals(LocalSearchResultFragmentFormQuery.EVENT_NOTIFICATION_STAGE)){
                            for(DataValue dataValue:event.getDataValues()){
                                if(dataValue.getDataElement().equals(PERSONS_LOCATION_DATAELEMENT)){
                                    persons_locations.add(dataValue);
                                }
                            }
                        }
                    }
                }
                
            }
        }
        
        //persons location on event notification stage 
        

        HolderActivity.startMaps(getActivity(),
                (ArrayList<TrackedEntityAttributeValue>) data, (ArrayList<DataValue>) persons_locations);
    }
}
