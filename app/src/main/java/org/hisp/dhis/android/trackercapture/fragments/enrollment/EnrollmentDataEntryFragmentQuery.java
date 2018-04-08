/*
 *  Copyright (c) 2016, University of Oslo
 *  * All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, this
 *  * list of conditions and the following disclaimer.
 *  *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *  * this list of conditions and the following disclaimer in the documentation
 *  * and/or other materials provided with the distribution.
 *  * Neither the name of the HISP project nor the names of its contributors may
 *  * be used to endorse or promote products derived from this software without
 *  * specific prior written permission.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.hisp.dhis.android.trackercapture.fragments.enrollment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;


import org.hisp.dhis.android.sdk.controllers.GpsController;
import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.controllers.tracker.TrackerController;
import org.hisp.dhis.android.sdk.persistence.loaders.Query;
import org.hisp.dhis.android.sdk.persistence.models.Enrollment;
import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.hisp.dhis.android.sdk.persistence.models.Program;
import org.hisp.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeGeneratedValue;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowFactory;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.EnrollmentDatePickerRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.IncidentDatePickerRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.Row;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.TrackerAssociateRowActionListener;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.ValueChangeListener;
import org.hisp.dhis.android.sdk.ui.fragments.selectprogram.SelectProgramFragmentPreferences;
import org.hisp.dhis.android.sdk.utils.api.ValueType;
import org.hisp.dhis.android.trackercapture.R;
import org.hisp.dhis.android.trackercapture.fragments.HolderFragment;
import org.hisp.dhis.android.trackercapture.fragments.TrackerAssociate.TrackerAssociateEnrollmentDataEntryFragment;
import org.hisp.dhis.android.trackercapture.fragments.selectprogram.EnrollmentDateSetterHelper;
import org.hisp.dhis.android.trackercapture.fragments.selectprogram.IEnroller;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EnrollmentDataEntryFragmentQuery implements Query<EnrollmentDataEntryFragmentForm>,
        IEnroller{
    public static final String CLASS_TAG = EnrollmentDataEntryFragmentQuery.class.getSimpleName();

    private final String mOrgUnitId;
    private final String mProgramId;
    private final long mTrackedEntityInstanceId;
    private final String enrollmentDate;
    private String incidentDate;
    private TrackedEntityInstance currentTrackedEntityInstance;
    private Enrollment currentEnrollment;
    private Program trackerAssociateProgram;
    SelectProgramFragmentPreferences mPrefs;
    Activity activity;
    TrackerAssociateRowActionListener actionListener;
    EnrollmentDataEntryFragment fFragment;

    protected EnrollmentDataEntryFragmentQuery(String mOrgUnitId, String mProgramId,
                                               long mTrackedEntityInstanceId,
                                               String date, String enrollmentDate, String incidentDate) {
        this.mOrgUnitId = mOrgUnitId;
        this.mProgramId = mProgramId;
        this.mTrackedEntityInstanceId = mTrackedEntityInstanceId;
        this.enrollmentDate = enrollmentDate;
        this.incidentDate = incidentDate;

    }

    protected EnrollmentDataEntryFragmentQuery(String mOrgUnitId, String mProgramId,
                                               long mTrackedEntityInstanceId,
                                               String enrollmentDate, String incidentDate, EnrollmentDataEntryFragment fragment,String tapID) {
        this.mOrgUnitId = mOrgUnitId;
        this.mProgramId = mProgramId;
        this.mTrackedEntityInstanceId = mTrackedEntityInstanceId;
        this.enrollmentDate = enrollmentDate;
        this.incidentDate = incidentDate;
        this.activity = fragment.getActivity();
        this.fFragment = fragment;
        trackerAssociateProgram = MetaDataController.getProgram(tapID);
        mPrefs = new SelectProgramFragmentPreferences(activity.getApplicationContext());
    }

    @Override
    public EnrollmentDataEntryFragmentForm query(Context context) {
        EnrollmentDataEntryFragmentForm mForm = new EnrollmentDataEntryFragmentForm();
        final Program mProgram = MetaDataController.getProgram(mProgramId);
        final OrganisationUnit mOrgUnit = MetaDataController.getOrganisationUnit(mOrgUnitId);

        if (mProgram == null || mOrgUnit == null) {
            return mForm;
        }

        if (mTrackedEntityInstanceId < 0) {
            currentTrackedEntityInstance = new TrackedEntityInstance(mProgram, mOrgUnitId);
        } else {
            currentTrackedEntityInstance = TrackerController.getTrackedEntityInstance(
                    mTrackedEntityInstanceId);
        }
        if ("".equals(incidentDate)) {
            incidentDate = null;
        }
        currentEnrollment = new Enrollment(mOrgUnitId,
                currentTrackedEntityInstance.getTrackedEntityInstance(), mProgram, enrollmentDate,
                incidentDate);

        mForm.setProgram(mProgram);
        mForm.setOrganisationUnit(mOrgUnit);
        mForm.setDataElementNames(new HashMap<String, String>());
        mForm.setDataEntryRows(new ArrayList<Row>());
        mForm.setTrackedEntityInstance(currentTrackedEntityInstance);
        mForm.setTrackedEntityAttributeValueMap(new HashMap<String, TrackedEntityAttributeValue>());

        List<TrackedEntityAttributeValue> trackedEntityAttributeValues = new ArrayList<>();
        List<ProgramTrackedEntityAttribute> programTrackedEntityAttributes =
                mProgram.getProgramTrackedEntityAttributes();
        List<Row> dataEntryRows = new ArrayList<>();

        dataEntryRows.add(
                new EnrollmentDatePickerRow(currentEnrollment.getProgram().getEnrollmentDateLabel(),
                        currentEnrollment));

        if (currentEnrollment.getProgram().getDisplayIncidentDate()) {
            dataEntryRows.add(
                    new IncidentDatePickerRow(currentEnrollment.getProgram().getIncidentDateLabel(),
                            currentEnrollment));
        }

        for (ProgramTrackedEntityAttribute ptea : programTrackedEntityAttributes) {
            TrackedEntityAttributeValue value = TrackerController.getTrackedEntityAttributeValue(
                    ptea.getTrackedEntityAttributeId(), currentTrackedEntityInstance.getLocalId());
            if (value != null) {
                trackedEntityAttributeValues.add(value);
            } else {
                TrackedEntityAttribute trackedEntityAttribute =
                        MetaDataController.getTrackedEntityAttribute(
                                ptea.getTrackedEntityAttributeId());
                if (trackedEntityAttribute.isGenerated()) {
                    TrackedEntityAttributeGeneratedValue trackedEntityAttributeGeneratedValue =
                            MetaDataController.getTrackedEntityAttributeGeneratedValue(
                                    ptea.getTrackedEntityAttribute());

                    if (trackedEntityAttributeGeneratedValue != null) {
                        TrackedEntityAttributeValue trackedEntityAttributeValue =
                                new TrackedEntityAttributeValue();
                        trackedEntityAttributeValue.setTrackedEntityAttributeId(
                                ptea.getTrackedEntityAttribute().getUid());
                        trackedEntityAttributeValue.setTrackedEntityInstanceId(
                                currentTrackedEntityInstance.getUid());
                        trackedEntityAttributeValue.setValue(
                                trackedEntityAttributeGeneratedValue.getValue());
                        trackedEntityAttributeValues.add(trackedEntityAttributeValue);
                    } else {
                        mForm.setOutOfTrackedEntityAttributeGeneratedValues(true);
                    }
                }
            }
        }
        currentEnrollment.setAttributes(trackedEntityAttributeValues);
        for (int i = 0; i < programTrackedEntityAttributes.size(); i++) {
            boolean editable = true;
            boolean shouldNeverBeEdited = false;
            if (programTrackedEntityAttributes.get(i).getTrackedEntityAttribute().isGenerated()) {
                editable = false;
                shouldNeverBeEdited = true;
            }
            if (ValueType.COORDINATE.equals(programTrackedEntityAttributes.get(
                    i).getTrackedEntityAttribute().getValueType())) {
                GpsController.activateGps(context);
            }
            boolean isRadioButton = mProgram.getDataEntryMethod();
            if(!isRadioButton){
                isRadioButton = programTrackedEntityAttributes.get(
                        i).isRenderOptionsAsRadio();
            }
//            Row row = DataEntryRowFactory.createDataEntryView(
//                    programTrackedEntityAttributes.get(i).getMandatory(),
//                    programTrackedEntityAttributes.get(i).getAllowFutureDate(),
//                    programTrackedEntityAttributes.get(
//                            i).getTrackedEntityAttribute().getOptionSet(),
//                    programTrackedEntityAttributes.get(i).getTrackedEntityAttribute().getName(),
//                    getTrackedEntityDataValue(programTrackedEntityAttributes.get(i).
//                            getTrackedEntityAttribute().getUid(), trackedEntityAttributeValues),
//                    programTrackedEntityAttributes.get(
//                            i).getTrackedEntityAttribute().getValueType(),
//                    editable, shouldNeverBeEdited, isRadioButton);
            Row row = DataEntryRowFactory.createDataEntryView(
                    programTrackedEntityAttributes.get(i).getMandatory(),
                    programTrackedEntityAttributes.get(i).getAllowFutureDate(),
                    programTrackedEntityAttributes.get(
                            i).getTrackedEntityAttribute().getOptionSet(),
                    programTrackedEntityAttributes.get(i).getTrackedEntityAttribute().getName(),
                    getTrackedEntityDataValue(programTrackedEntityAttributes.get(i).
                            getTrackedEntityAttribute().getUid(), trackedEntityAttributeValues),
                    programTrackedEntityAttributes.get(
                            i).getTrackedEntityAttribute().getValueType(),
                    editable, shouldNeverBeEdited, isRadioButton, new TrackerAssociateRowActionListener() {
                        private ValueChangeListener clearState = null;
                        private ValueChangeListener searchState = null;
                        private ValueChangeListener addState = null;

                        @Override
                        public void addButtonClicked() {
                            createEnrollmentForTrackerAssociate(this);
                        }

                        @Override
                        public void searchButtonClicked() {
                            //TODO:implement action for search button
                        }

                        @Override
                        public void clearButtonClicked() {
                            //implement Action for clear
                        }

                        @Override
                        public ValueChangeListener getValue(STATES state) {
                            switch (state){
                                case ADD:
                                    return  this.addState;


                                case CLEAR:
                                    return this.clearState;


                                case SEARCH:
                                    return this.searchState;


                                    default:
                                        return null;
                            }
                        }


                        @Override
                        public void setValueListeners(STATES state, ValueChangeListener value) {
                            switch (state){
                                case ADD:
                                    this.addState = value;
                                    break;

                                case CLEAR:
                                    this.clearState = value;
                                    break;

                                case SEARCH:
                                    this.searchState = value;
                                    break;
                            }
                        }
                    });
            dataEntryRows.add(row);
        }
        for (TrackedEntityAttributeValue trackedEntityAttributeValue :
                trackedEntityAttributeValues) {
            mForm.getTrackedEntityAttributeValueMap().put(
                    trackedEntityAttributeValue.getTrackedEntityAttributeId(),
                    trackedEntityAttributeValue);
        }
        mForm.setDataEntryRows(dataEntryRows);
        mForm.setEnrollment(currentEnrollment);
        return mForm;
    }

    public TrackedEntityAttributeValue getTrackedEntityDataValue(String trackedEntityAttribute,
            List<TrackedEntityAttributeValue> trackedEntityAttributeValues) {
        for (TrackedEntityAttributeValue trackedEntityAttributeValue :
                trackedEntityAttributeValues) {
            if (trackedEntityAttributeValue.getTrackedEntityAttributeId().equals(
                    trackedEntityAttribute)) {
                return trackedEntityAttributeValue;
            }
        }

        //the datavalue didnt exist for some reason. Create a new one.
        TrackedEntityAttributeValue trackedEntityAttributeValue = new TrackedEntityAttributeValue();
        trackedEntityAttributeValue.setTrackedEntityAttributeId(trackedEntityAttribute);
        trackedEntityAttributeValue.setTrackedEntityInstanceId(
                currentTrackedEntityInstance.getTrackedEntityInstance());
        trackedEntityAttributeValue.setValue("");
        trackedEntityAttributeValues.add(trackedEntityAttributeValue);

        return trackedEntityAttributeValue;
    }


    //adding for tracker Associate
    private void createEnrollmentForTrackerAssociate(TrackerAssociateRowActionListener actionListener) {
        this.actionListener = actionListener;
        if(trackerAssociateProgram!=null){
            EnrollmentDateSetterHelper.createEnrollment(this, activity, trackerAssociateProgram.
                            getDisplayIncidentDate(), trackerAssociateProgram.getSelectEnrollmentDatesInFuture(),
                    trackerAssociateProgram.getSelectIncidentDatesInFuture(), trackerAssociateProgram.getEnrollmentDateLabel(),
                    trackerAssociateProgram.getIncidentDateLabel());
        }


    }

    //for tracker Associate
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showEnrollmentFragment(TrackedEntityInstance trackedEntityInstance, DateTime enrollmentDate, DateTime incidentDate) {
        String enrollmentDateString = enrollmentDate.toString();
        String incidentDateString = null;
        if (incidentDate != null) {
            incidentDateString = incidentDate.toString();
        }
        if (trackedEntityInstance == null) {
            TrackerAssociateEnrollmentDataEntryFragment fragment = new TrackerAssociateEnrollmentDataEntryFragment();
            fragment.setActionListener(actionListener);
            Bundle bundle = new Bundle();
            bundle.putString(EnrollmentDataEntryFragment.ORG_UNIT_ID,  mPrefs.getOrgUnit().first);
            bundle.putString(EnrollmentDataEntryFragment.PROGRAM_ID, trackerAssociateProgram.getUid());
            bundle.putString(EnrollmentDataEntryFragment.ENROLLMENT_DATE, enrollmentDateString);
            bundle.putString(EnrollmentDataEntryFragment.INCIDENT_DATE, incidentDateString);
            fragment.setArguments(bundle);
            FragmentTransaction ft = fFragment.getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame,fragment);
            ft.addToBackStack(null);
            ft.commit();
            //fragment1.attach(fragment);
            //            HolderActivity.navigateToEnrollmentDataEntryFragment(activity, mPrefs.getOrgUnit().first, trackerAssociateProgram.getUid(), enrollmentDateString, incidentDateString);

        } else {
//            HolderActivity.navigateToEnrollmentDataEntryFragment(activity, mPrefs.getOrgUnit().first, trackerAssociateProgram.getUid(), trackedEntityInstance.getLocalId(), enrollmentDateString, incidentDateString);
            TrackerAssociateEnrollmentDataEntryFragment fragment = new TrackerAssociateEnrollmentDataEntryFragment();
            fragment.setActionListener(actionListener);
            Bundle bundle = new Bundle();
            bundle.putString(EnrollmentDataEntryFragment.ORG_UNIT_ID,  mPrefs.getOrgUnit().first);
            bundle.putString(EnrollmentDataEntryFragment.PROGRAM_ID, trackerAssociateProgram.getUid());
            bundle.putLong(EnrollmentDataEntryFragment.TRACKEDENTITYINSTANCE_ID, trackedEntityInstance.getLocalId());
            bundle.putString(EnrollmentDataEntryFragment.ENROLLMENT_DATE, enrollmentDateString);
            bundle.putString(EnrollmentDataEntryFragment.INCIDENT_DATE, incidentDateString);
            fragment.setArguments(bundle);
            HolderFragment fragment1 = new HolderFragment();
//            fragment1.attach(fragment);
//            fragment1.show(activity.getFragmentManager(),"Holder");

        }



    }


}
