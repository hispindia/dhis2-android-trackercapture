package org.hisp.dhis.android.trackercapture.fragments.NewCase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.hisp.dhis.android.sdk.persistence.models.Program;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.hisp.dhis.android.sdk.ui.fragments.selectprogram.SelectProgramFragmentForm;
import org.hisp.dhis.android.sdk.ui.fragments.selectprogram.SelectProgramFragmentPreferences;
import org.hisp.dhis.android.trackercapture.MainActivity;
import org.hisp.dhis.android.trackercapture.R;
import org.hisp.dhis.android.trackercapture.activities.HolderActivity;
import org.hisp.dhis.android.trackercapture.fragments.selectprogram.EnrollmentDateSetterHelper;
import org.hisp.dhis.android.trackercapture.fragments.selectprogram.IEnroller;
import org.hisp.dhis.client.sdk.ui.fragments.BaseFragment;
import org.joda.time.DateTime;

import java.util.List;

public class NewCaseFragment extends BaseFragment implements View.OnClickListener,IEnroller {
    protected Button fullInvestigationsBtn;
    protected Button intakeFormBtn;
    protected String intakeFormId="xO7WLJ8DIDK";
    protected SelectProgramFragmentForm mForm;
    protected SelectProgramFragmentPreferences mPrefs;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_new_case,container,false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mForm = new SelectProgramFragmentForm();
        directToForms(intakeFormId);

//        fullInvestigationsBtn = (Button) view.findViewById(R.id.full_investigations_btn);
//        intakeFormBtn = (Button) view.findViewById(R.id.intake_form_btn);
//        fullInvestigationsBtn.setOnClickListener(this);
//        intakeFormBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.intake_form_btn:
//                Toast.makeText(getContext(),"Intake form",Toast.LENGTH_SHORT).show();
//                directToForms(getString(R.string.intake_form_program_id));
//                break;

//            case R.id.full_investigations_btn:
//                Toast.makeText(getContext(),"fULL INVESTIGATIONS form",Toast.LENGTH_SHORT).show();
//                directToForms(getString(R.string.full_investigation_program_id));
//                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        super.onBackPressed();
        ((MainActivity)getActivity()).navigateToHomeFragment();
        return false;
    }

    private void directToForms(String programId){
        mPrefs = new SelectProgramFragmentPreferences(getActivity().getApplicationContext());
        Program program = MetaDataController.getProgram(programId);
//        OrganisationUnit organisationUnit = MetaDataController.getOrganisationUnit(mPrefs.getOrgUnit().first);
//        OrganisationUnit organisationUnit = MetaDataController.getOrganisationUnit("C5pMQzJzCFw");
        List<OrganisationUnit> organisationUnits_=MetaDataController.getAssignedOrganisationUnits();
        OrganisationUnit organisationUnit=MetaDataController.getOrganisationUnit(organisationUnits_.get(0).getId());

//        OrganisationUnit organisationUnit = MetaDataController.getOrganisationUnit(mPrefs.getOrgUnit().first);
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
