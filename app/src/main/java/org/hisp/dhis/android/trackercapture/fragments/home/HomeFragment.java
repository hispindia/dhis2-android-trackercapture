package org.hisp.dhis.android.trackercapture.fragments.home;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.hisp.dhis.android.sdk.persistence.models.Program;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.hisp.dhis.android.sdk.ui.activities.OnBackPressedListener;
import org.hisp.dhis.android.sdk.ui.dialogs.AutoCompleteDialogFragment;
import org.hisp.dhis.android.sdk.ui.dialogs.OrgUnitDialogFragment;
import org.hisp.dhis.android.sdk.ui.dialogs.ProgramDialogFragment;
import org.hisp.dhis.android.sdk.ui.fragments.selectprogram.SelectProgramFragmentForm;
import org.hisp.dhis.android.sdk.ui.fragments.selectprogram.SelectProgramFragmentPreferences;
import org.hisp.dhis.android.sdk.utils.api.ProgramType;
import org.hisp.dhis.android.trackercapture.MainActivity;
import org.hisp.dhis.android.trackercapture.R;
import org.hisp.dhis.android.trackercapture.activities.HolderActivity;
import org.hisp.dhis.android.trackercapture.fragments.selectprogram.EnrollmentDateSetterHelper;
import org.hisp.dhis.android.trackercapture.fragments.selectprogram.IEnroller;
import org.hisp.dhis.android.trackercapture.fragments.selectprogram.SelectProgramFragment;
import org.hisp.dhis.client.sdk.ui.activities.BaseActivity;
import org.hisp.dhis.client.sdk.ui.activities.OnBackPressedFromFragmentCallback;
import org.hisp.dhis.client.sdk.ui.fragments.BaseFragment;
import org.joda.time.DateTime;

import java.util.List;

public class HomeFragment extends BaseFragment implements View.OnClickListener, IEnroller {
    protected SelectProgramFragmentPreferences mPrefs;
    private OnBackPressedFromFragmentCallback onBackPressedFromFragmentCallback;
    private SelectProgramFragmentForm mForm;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
        mForm = new SelectProgramFragmentForm();
        mPrefs = new SelectProgramFragmentPreferences(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ibmc_home,container,false);
        if(getActivity() instanceof AppCompatActivity){
            Toolbar toolbar  = getParentToolbar();
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleNavigationDrawer();
                }
            });
        }
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.ibmc_jobs).setOnClickListener(this);
        view.findViewById(R.id.ibmc_new_case).setOnClickListener(this);
        view.findViewById(R.id.ibmc_review_cases).setOnClickListener(this);
        view.findViewById(R.id.ibmc_upload).setOnClickListener(this);
        view.findViewById(R.id.ibmc_statistics).setOnClickListener(this);
        view.findViewById(R.id.ibmc_notifications).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
//        if(mPrefs.getOrgUnit()==null){
//            Toast.makeText(getContext(),"Please Select and Organization unit",Toast.LENGTH_LONG).show();
//        }else {

            switch (view.getId()) {
                case R.id.ibmc_jobs:
//                    OrganisationUnit organisationUnit = MetaDataController.getOrganisationUnit("C5pMQzJzCFw");
                    List<OrganisationUnit> organisationUnits_=MetaDataController.getAssignedOrganisationUnits();
                    OrganisationUnit organisationUnit=MetaDataController.getOrganisationUnit(organisationUnits_.get(0).getId());
//                    HolderActivity.navigateToLocalSearchFragment(getActivity(),
//                            mPrefs.getOrgUnit().first,getString(R.string.intake_form_program_id));

                    HolderActivity.navigateToLocalSearchFragment(getActivity(),
                            organisationUnit.getId(),getString(R.string.intake_form_program_id));
                    break;

                case R.id.ibmc_new_case:
//                    ((MainActivity) getActivity()).navigateToSelectProgramFragment();
//                    ((MainActivity) getActivity()).navigateToNewcaseFragment();
                    directToForms(getString(R.string.intake_form_program_id));
                    break;

                case R.id.ibmc_review_cases:
                    Toast.makeText(getContext(), "Review Cases", Toast.LENGTH_LONG).show();
                    break;

                case R.id.ibmc_upload:
                    Toast.makeText(getContext(), "Upload", Toast.LENGTH_LONG).show();
                    break;

                case R.id.ibmc_statistics:
                    Toast.makeText(getContext(), "Statistics", Toast.LENGTH_LONG).show();
                    break;

                case R.id.ibmc_notifications:
                    Toast.makeText(getContext(), "Notifications", Toast.LENGTH_LONG).show();
                    break;
            }
//        }


    }

    @Override
    public boolean onBackPressed() {
        if (onBackPressedFromFragmentCallback != null) {
            onBackPressedFromFragmentCallback.onBackPressedFromFragment();
            return false;
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).setOnBackPressedCallback(this);
        }
        if (context instanceof OnBackPressedFromFragmentCallback) {
            onBackPressedFromFragmentCallback = (OnBackPressedFromFragmentCallback) context;
        }
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        // nullifying callback references
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setOnBackPressedCallback(null);
        }
        onBackPressedFromFragmentCallback = null;
        super.onDetach();
    }

    private void directToForms(String programId){
        mPrefs = new SelectProgramFragmentPreferences(getActivity().getApplicationContext());
        Program program = MetaDataController.getProgram(programId);
        //OrganisationUnit organisationUnit = MetaDataController.getOrganisationUnit(mPrefs.getOrgUnit().first);
        List<OrganisationUnit> organisationUnits_=MetaDataController.getAssignedOrganisationUnits();
        OrganisationUnit organisationUnit=MetaDataController.getOrganisationUnit(organisationUnits_.get(0).getId());

        mForm.setProgram(program);
        mForm.setOrgUnit(organisationUnit);
        createEnrollment();
    }

   private void createEnrollment() {
        if (mForm != null && mForm.getProgram() != null) {
        EnrollmentDateSetterHelper.createEnrollment(this, getActivity(), mForm.getProgram().
               getDisplayIncidentDate(), mForm.getProgram().getSelectEnrollmentDatesInFuture(),
                mForm.getProgram().getSelectIncidentDatesInFuture(), mForm.getProgram().getEnrollmentDateLabel(),
                mForm.getProgram().getIncidentDateLabel());
        }
    }

    public void showEnrollmentFragment(TrackedEntityInstance trackedEntityInstance, DateTime enrollmentDate, DateTime incidentDate) {
        String enrollmentDateString = enrollmentDate.toString();
        String incidentDateString = null;
        if (incidentDate != null) {
                incidentDateString = incidentDate.toString();
            }
        if (trackedEntityInstance == null) {
                HolderActivity.navigateToEnrollmentDataEntryFragment(getActivity(), mForm.getOrgUnit().getId(), mForm.getProgram().getUid(), enrollmentDateString, incidentDateString);

        } else {
                HolderActivity.navigateToEnrollmentDataEntryFragment(getActivity(), mForm.getOrgUnit().getId(), mForm.getProgram().getUid(), trackedEntityInstance.getLocalId(), enrollmentDateString, incidentDateString);

        }
    }
}
