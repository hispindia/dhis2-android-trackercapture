package org.hisp.dhis.android.trackercapture.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.hisp.dhis.android.sdk.persistence.models.DataValue;
import org.hisp.dhis.android.sdk.persistence.models.Event;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.hisp.dhis.android.sdk.ui.activities.OnBackPressedListener;
import org.hisp.dhis.android.sdk.ui.fragments.eventdataentry.EventDataEntryFragment;
import org.hisp.dhis.android.trackercapture.R;
import org.hisp.dhis.android.trackercapture.fragments.home.HomeFragment;
import org.hisp.dhis.android.trackercapture.fragments.trackedentityinstance
        .TrackedEntityInstanceDataEntryFragment;
import org.hisp.dhis.android.trackercapture.fragments.enrollment.EnrollmentDataEntryFragment;
import org.hisp.dhis.android.trackercapture.fragments.enrollmentdate.EnrollmentDateFragment;
import org.hisp.dhis.android.trackercapture.fragments.programoverview.ProgramOverviewFragment;
import org.hisp.dhis.android.trackercapture.fragments.search.LocalSearchFragment;
import org.hisp.dhis.android.trackercapture.fragments.search.LocalSearchResultFragment;
import org.hisp.dhis.android.trackercapture.fragments.search.OnlineSearchFragment;
import org.hisp.dhis.android.trackercapture.fragments.search.OnlineSearchResultFragment;
import org.hisp.dhis.android.trackercapture.fragments.selectprogram.SelectProgramFragment;
import org.hisp.dhis.android.trackercapture.fragments.settings.SettingsFragment;
import org.hisp.dhis.android.trackercapture.fragments.trackedentityinstanceprofile.TrackedEntityInstanceProfileFragment;
import org.hisp.dhis.android.trackercapture.fragments.upcomingevents.UpcomingEventsFragment;
import org.hisp.dhis.client.sdk.ui.activities.AbsHomeActivity;
import org.hisp.dhis.client.sdk.ui.fragments.WrapperFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hisp.dhis.android.trackercapture.activities.MapActivity.ATTRIBUTE_COORDINATES;
import static org.hisp.dhis.android.trackercapture.activities.MapActivity.DATAELEMENT_COORDINATES;

public class HolderActivity extends AbsHomeActivity{

    public static final String ARG_TYPE = "arg:FragmentType";
    public static final String ARG_TYPE_ENROLLMENTFRAGMENT = "arg:EnrollmentTypeFragment";
    public static final String ARG_TYPE_PROGRAMOVERVIEWFRAGMENT = "arg:ProgramOverviewFragment";
    public static final String ARG_TYPE_SETTINGSFRAGMENT = "arg:SettingsFragment";
    public static final String ARG_TYPE_DATAENTRYFRAGMENT = "arg:DataEntryFragment";
    public static final String ARG_TYPE_TRACKEDENTITYINSTANCEPROFILE = "arg:TrackedEntityInstanceProfile";
    public static final String ARG_TYPE_ENROLLMENTDATEFRAGMENT = "arg:EnrollmentDateFragment";
    public static final String ARG_TYPE_LOCALSEARCHFRAGMENT = "arg:LocalSearchFragment";
    public static final String ARG_TYPE_TRACKEDENTITYINSTANCEDATAENTRYFRAGMENT = "arg:TrackedEntityInstanceDataEntryFragment";
    public static final String ARG_TYPE_LOCALSEARCHRESULTFRAGMENT = "arg:LocalSearchResultFragment";
    public static final String ARG_TYPE_ONLINESEARCHFRAGMENT = "arg:OnlineSearchFragment";
    public static final String ARG_TYPE_ONLINESEARCHRESULTFRAGMENT = "arg:OnlineSearchResultFragment";
    public static final String ARG_TYPE_SELECT_PROGRAME_FRAGMENT = "arg:SelectProgramFragment";
    private static final String ARG_TYPE_UPCOMINGEVENTSFRAGMENT = "arg:UpcomingEventsFragment";



    OnBackPressedListener onBackPressedListener;
    public static OnlineSearchResultFragment.CallBack mCallBack;


    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null) {
            if (onBackPressedListener.doBack()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //addMenuItem(11, R.drawable.ic_add, R.string.enroll);
        //change by ifhaam for IBMC on 31-3-2018
        addMenuItem(12, R.drawable.ic_home, R.string.home);

        if (savedInstanceState == null) {
            onNavigationItemSelected(getNavigationView().getMenu()
                    .findItem(12));
        }

        String arg = getIntent().getExtras().getString(ARG_TYPE);
        System.out.println(arg);
        String argType = getIntent().getExtras().getString(ARG_TYPE);
        switch (argType) {
            case ARG_TYPE_ENROLLMENTFRAGMENT: {
                EnrollmentDataEntryFragment fragment = new EnrollmentDataEntryFragment();
                onBackPressedListener = fragment;
                fragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            }
            case ARG_TYPE_PROGRAMOVERVIEWFRAGMENT: {
                ProgramOverviewFragment fragment = new ProgramOverviewFragment();
                onBackPressedListener = fragment;
                fragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            }
            case ARG_TYPE_SETTINGSFRAGMENT: {
                onBackPressedListener = null;
                SettingsFragment settingsFragment = new SettingsFragment();
                settingsFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, settingsFragment).commit();
                break;
            }
            case ARG_TYPE_SELECT_PROGRAME_FRAGMENT:{
                onBackPressedListener = null;
                attachFragment(WrapperFragment.newInstance(SelectProgramFragment.class,getString(R.string.app_name)));
                break;
            }
            case ARG_TYPE_DATAENTRYFRAGMENT: {
                EventDataEntryFragment eventDataEntryFragment = new EventDataEntryFragment();
                onBackPressedListener = eventDataEntryFragment;
                eventDataEntryFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, eventDataEntryFragment).commit();
                break;
            }
            case ARG_TYPE_TRACKEDENTITYINSTANCEPROFILE: {
                TrackedEntityInstanceProfileFragment trackedEntityInstanceProfileFragment = new TrackedEntityInstanceProfileFragment();
                onBackPressedListener = trackedEntityInstanceProfileFragment;
                trackedEntityInstanceProfileFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, trackedEntityInstanceProfileFragment).commit();
                break;
            }
            case ARG_TYPE_TRACKEDENTITYINSTANCEDATAENTRYFRAGMENT: {
                TrackedEntityInstanceDataEntryFragment trackedEntityInstanceDataEntryFragment = new TrackedEntityInstanceDataEntryFragment();
                onBackPressedListener = trackedEntityInstanceDataEntryFragment;
                trackedEntityInstanceDataEntryFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, trackedEntityInstanceDataEntryFragment).commit();
                break;
            }
            case ARG_TYPE_ENROLLMENTDATEFRAGMENT: {
                EnrollmentDateFragment enrollmentDateFragment = new EnrollmentDateFragment();
                onBackPressedListener = enrollmentDateFragment;
                enrollmentDateFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, enrollmentDateFragment).commit();
                break;
            }
            case ARG_TYPE_ONLINESEARCHFRAGMENT: {
                onBackPressedListener = null;
                OnlineSearchFragment onlineSearchFragment = new OnlineSearchFragment();
                onlineSearchFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, onlineSearchFragment).commit();
                break;
            }
            case ARG_TYPE_ONLINESEARCHRESULTFRAGMENT: {
                onBackPressedListener = null;
                OnlineSearchResultFragment onlineSearchResultFragment = new OnlineSearchResultFragment();
                onlineSearchResultFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, onlineSearchResultFragment).commit();
                break;
            }
            case ARG_TYPE_LOCALSEARCHFRAGMENT: {
                onBackPressedListener = null;
                LocalSearchFragment localSearchFragment = new LocalSearchFragment();
                localSearchFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, localSearchFragment).commit();
                break;
            }
            case ARG_TYPE_LOCALSEARCHRESULTFRAGMENT: {
                onBackPressedListener = null;
                LocalSearchResultFragment localSearchResultFragment = new LocalSearchResultFragment();
                localSearchResultFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, localSearchResultFragment).commit();
                break;
            }
            case ARG_TYPE_UPCOMINGEVENTSFRAGMENT: {
                onBackPressedListener = null;
                UpcomingEventsFragment upcomingEventsFragment = new UpcomingEventsFragment();
                upcomingEventsFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, upcomingEventsFragment).commit();
                break;
            }
        }

    }

    @NonNull
    @Override
    protected Fragment getProfileFragment() {
        return new Fragment();
    }

    @NonNull
    @Override
    protected Fragment getSettingsFragment() {
        return WrapperFragment.newInstance(SettingsFragment.class,
                getString(R.string.drawer_item_settings));
    }

    @Override
    protected boolean onItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 12) {
            //Change by ifhaam for IBMC on 31-3-2018
            //attachFragment(WrapperFragment.newInstance(SelectProgramFragment.class, getString(R.string.app_name)));
            attachFragment(WrapperFragment.newInstance(HomeFragment.class, getString(R.string.app_name)));

            return true;
        }
        return false;
    }


    public static void navigateToEnrollmentDataEntryFragment(Activity activity, String orgUnitId,
                                                             String programId,
                                                             long trackedEntityInstanceId,
                                                             String dateOfEnrollment,
                                                             String dateOfIncident) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra(EnrollmentDataEntryFragment.ORG_UNIT_ID, orgUnitId);
        intent.putExtra(EnrollmentDataEntryFragment.PROGRAM_ID, programId);
        intent.putExtra(EnrollmentDataEntryFragment.TRACKEDENTITYINSTANCE_ID, trackedEntityInstanceId);
        intent.putExtra(EnrollmentDataEntryFragment.ENROLLMENT_DATE, dateOfEnrollment);
        intent.putExtra(EnrollmentDataEntryFragment.INCIDENT_DATE, dateOfIncident);
        intent.putExtra(ARG_TYPE, ARG_TYPE_ENROLLMENTFRAGMENT);

//        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // we don't want to keep it to backstack
        activity.startActivity(intent);

    }

    public static void navigateToEnrollmentDataEntryFragment(Activity activity, String orgUnitId,
                                                             String programId,
                                                             String dateOfEnrollment,
                                                             String dateOfIncident) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra(EnrollmentDataEntryFragment.ORG_UNIT_ID, orgUnitId);
        intent.putExtra(EnrollmentDataEntryFragment.PROGRAM_ID, programId);
        intent.putExtra(EnrollmentDataEntryFragment.ENROLLMENT_DATE, dateOfEnrollment);
        intent.putExtra(EnrollmentDataEntryFragment.INCIDENT_DATE, dateOfIncident);
        intent.putExtra(ARG_TYPE, ARG_TYPE_ENROLLMENTFRAGMENT);

//        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // we don't want to keep it to backstack
        activity.startActivity(intent);

    }

    public static void navigateToProgramOverviewFragment(Activity activity,
                                                         String orgUnitId,
                                                         String programId,
                                                         long trackedEntityInstanceId) {

        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra(EnrollmentDataEntryFragment.ORG_UNIT_ID, orgUnitId);
        intent.putExtra(EnrollmentDataEntryFragment.PROGRAM_ID, programId);
        intent.putExtra(EnrollmentDataEntryFragment.TRACKEDENTITYINSTANCE_ID, trackedEntityInstanceId);
        intent.putExtra(ARG_TYPE, ARG_TYPE_PROGRAMOVERVIEWFRAGMENT);

        activity.startActivity(intent);

    }


    public static void navigateToDataEntryFragment(Activity activity, String orgUnitId,
                                                   String programId, String programStageId,
                                                   long localEnrollmentId, long eventId,String lastCompletedEventDate,String lastUnCompletedDate) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra(EventDataEntryFragment.ORG_UNIT_ID, orgUnitId);
        intent.putExtra(EventDataEntryFragment.PROGRAM_ID, programId);
        intent.putExtra(EventDataEntryFragment.PROGRAM_STAGE_ID, programStageId);
        intent.putExtra(EventDataEntryFragment.ENROLLMENT_ID, localEnrollmentId);
        intent.putExtra(EventDataEntryFragment.EVENT_ID, eventId);
        intent.putExtra(ARG_TYPE, ARG_TYPE_DATAENTRYFRAGMENT);
        intent.putExtra(EventDataEntryFragment.LAST_COMPLETED_EVENT_DATE,lastCompletedEventDate);
        intent.putExtra(EventDataEntryFragment.LAST_UNCOMPLETED_EVENT_DATE,lastUnCompletedDate);
        activity.startActivity(intent);
    }
    
    //To pass UID to new program
    
    public static void navigateToDataEntryFragment_(Activity activity, String orgUnitId,
                                                             String programId,
                                                             String dateOfEnrollment,
                                                             String dateOfIncident,String trackedEntityInstance,
                                                             String ANIMALID_BARCODE){
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra(EnrollmentDataEntryFragment.ORG_UNIT_ID, orgUnitId);
        intent.putExtra(EnrollmentDataEntryFragment.PROGRAM_ID, programId);
        intent.putExtra(EnrollmentDataEntryFragment.ENROLLMENT_DATE, dateOfEnrollment);
        intent.putExtra(EnrollmentDataEntryFragment.INCIDENT_DATE, dateOfIncident);
        intent.putExtra(ARG_TYPE, ARG_TYPE_ENROLLMENTFRAGMENT);
        intent.putExtra(EnrollmentDataEntryFragment.ANIMALID_BARCODE,ANIMALID_BARCODE);
        intent.putExtra(EnrollmentDataEntryFragment.TRACKEDENTITYINSTANCE_SID,trackedEntityInstance);
        activity.startActivity(intent);
    }



    public static void navigateToEnrollmentDateFragment(Activity activity, long enrollmentId) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra(EnrollmentDateFragment.ENROLLMENT_ID, enrollmentId);
        intent.putExtra(ARG_TYPE, ARG_TYPE_ENROLLMENTDATEFRAGMENT);
        activity.startActivity(intent);
    }

    public static void navigateToTrackedEntityInstanceProfileFragment(Activity activity, long trackedEntityInstanceId, String programId) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra(TrackedEntityInstanceProfileFragment.TRACKEDENTITYINSTANCE_ID, trackedEntityInstanceId);
        intent.putExtra(TrackedEntityInstanceProfileFragment.PROGRAM_ID, programId);
        intent.putExtra(ARG_TYPE, ARG_TYPE_TRACKEDENTITYINSTANCEPROFILE);
        activity.startActivity(intent);
    }

    public static void navigateToDataEntryFragment(Activity activity, String orgUnitId, String programId, String programStageId, long localEnrollmentId) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra(EventDataEntryFragment.ORG_UNIT_ID, orgUnitId);
        intent.putExtra(EventDataEntryFragment.PROGRAM_ID, programId);
        intent.putExtra(EventDataEntryFragment.PROGRAM_STAGE_ID, programStageId);
        intent.putExtra(EventDataEntryFragment.ENROLLMENT_ID, localEnrollmentId);
        intent.putExtra(ARG_TYPE, ARG_TYPE_DATAENTRYFRAGMENT);
        activity.startActivity(intent);
    }

    public static void navigateToSettingsFragment(Activity activity) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra(ARG_TYPE, ARG_TYPE_SETTINGSFRAGMENT);
        activity.startActivity(intent);
    }

    public static void navigateToOnlineSearchFragment(Activity activity, String programId,
            String orgUnitId, boolean backNavigation,
            OnlineSearchResultFragment.CallBack callBack) {
        mCallBack = callBack;
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra(OnlineSearchFragment.EXTRA_PROGRAM, programId);
        intent.putExtra(OnlineSearchFragment.EXTRA_ORGUNIT, orgUnitId);
        intent.putExtra(OnlineSearchFragment.EXTRA_NAVIGATION, backNavigation);
        intent.putExtra(ARG_TYPE, ARG_TYPE_ONLINESEARCHFRAGMENT);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // we don't want to keep it to backstack
        activity.startActivity(intent);
    }

    public static void startMaps(Activity activity){
        Intent intent = new Intent(activity,MapActivity.class);
        activity.startActivity(intent);
    }
    public static void startMaps(Activity activity, ArrayList<DataValue> dataElementOneValues, ArrayList<DataValue> dataElementTwoValues){
        Intent intent = new Intent(activity,MapActivity.class);
        intent.putExtra(ATTRIBUTE_COORDINATES,dataElementOneValues);
        intent.putExtra(DATAELEMENT_COORDINATES,dataElementTwoValues);
        activity.startActivity(intent);
    }

    public static void navigateToOnlineSearchResultFragment(Activity activity, List<TrackedEntityInstance> trackedEntityInstances, String orgUnit, String program, boolean backNavigation) {
        try {
            Intent intent = new Intent(activity, HolderActivity.class);

            OnlineSearchResultFragment.ParameterSerializible parameterSerializible1 =
                    new OnlineSearchResultFragment.ParameterSerializible(trackedEntityInstances);
            OnlineSearchResultFragment.ParameterSerializible parameterSerializible2 =
                    new OnlineSearchResultFragment.ParameterSerializible(new ArrayList<TrackedEntityInstance>());


            intent.putExtra(OnlineSearchResultFragment.EXTRA_ORGUNIT, orgUnit);
            intent.putExtra(OnlineSearchResultFragment.EXTRA_SELECTALL, false);
            intent.putExtra(OnlineSearchResultFragment.EXTRA_PROGRAM, program);
            intent.putExtra(OnlineSearchResultFragment.EXTRA_TRACKEDENTITYINSTANCESSELECTED, parameterSerializible1);
            intent.putExtra(OnlineSearchResultFragment.EXTRA_TRACKEDENTITYINSTANCESLIST, parameterSerializible2);
            intent.putExtra(OnlineSearchResultFragment.EXTRA_NAVIGATION, backNavigation);
            intent.putExtra(ARG_TYPE, ARG_TYPE_ONLINESEARCHRESULTFRAGMENT);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // we don't want to keep it to backstack

            activity.startActivity(intent);
        } catch (Exception e) {
            if (activity != null) {
                Toast.makeText(activity, R.string.generic_error, Toast.LENGTH_LONG).show();
            }
        }

    }

    public static void navigateToLocalSearchFragment(Activity activity, String orgUnitId, String programId) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra(LocalSearchFragment.EXTRA_PROGRAM, programId);
        intent.putExtra(LocalSearchFragment.EXTRA_ORGUNIT, orgUnitId);
        intent.putExtra(ARG_TYPE, ARG_TYPE_LOCALSEARCHFRAGMENT);
        activity.startActivity(intent);
    }

    public static void navigateToLocalSearchResultFragment(Activity activity, String organisationUnitId, String program, HashMap<String, String> attributeValues,String startDate,String endDate,String stageId,String atrCord,String decord,String enrollmentfl) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra(LocalSearchResultFragment.EXTRA_ORGUNIT, organisationUnitId);
        intent.putExtra(LocalSearchResultFragment.EXTRA_PROGRAM, program);
        intent.putExtra(LocalSearchResultFragment.EXTRA_ATTRIBUTEVALUEMAP, attributeValues);
        intent.putExtra(LocalSearchResultFragment.START_DATE,startDate);
        intent.putExtra(LocalSearchResultFragment.END_DATE,endDate);
        intent.putExtra(LocalSearchResultFragment.EXTRA_STAGE_ID,stageId);
        intent.putExtra(LocalSearchResultFragment.EXTRA_COORD_ATR,atrCord);
        intent.putExtra(LocalSearchResultFragment.EXTRA_COORD_DE,decord);
        intent.putExtra(LocalSearchResultFragment.EXTRA_ENROLLMENT_FL,enrollmentfl);
        intent.putExtra(ARG_TYPE, ARG_TYPE_LOCALSEARCHRESULTFRAGMENT);
        activity.startActivity(intent);
    }

    public static void navigateToUpcomingEventsFragment(Activity activity) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra(ARG_TYPE, ARG_TYPE_UPCOMINGEVENTSFRAGMENT);
        activity.startActivity(intent);
    }

    public static void navigateToTrackedEntityInstanceDataEntryFragment(Activity activity,
            String programId, String orgUnit, boolean navigationBack, OnlineSearchResultFragment.CallBack callBack) {
        mCallBack = callBack;
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra(TrackedEntityInstanceDataEntryFragment.PROGRAM_ID, programId);
        intent.putExtra(TrackedEntityInstanceDataEntryFragment.ORG_UNIT_ID, orgUnit);
        intent.putExtra(TrackedEntityInstanceDataEntryFragment.EXTRA_NAVIGATION, navigationBack);
        intent.putExtra(ARG_TYPE, ARG_TYPE_TRACKEDENTITYINSTANCEDATAENTRYFRAGMENT);
        activity.startActivity(intent);
    }

    public static void navigateToSelectProgrameFragment(Activity activity){
        Intent intent = new Intent(activity,HolderActivity.class);
        intent.putExtra(ARG_TYPE,ARG_TYPE_SELECT_PROGRAME_FRAGMENT);
        activity.startActivity(intent);
    }





}