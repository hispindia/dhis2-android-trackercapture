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

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.controllers.tracker.TrackerController;
import org.hisp.dhis.android.sdk.persistence.loaders.Query;
import org.hisp.dhis.android.sdk.persistence.models.Enrollment;
import org.hisp.dhis.android.sdk.persistence.models.OptionSet;

import org.hisp.dhis.android.sdk.persistence.models.UserAccount;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.EditTextRow;
import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.hisp.dhis.android.sdk.persistence.models.Program;
import org.hisp.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeGeneratedValue;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.CheckBoxRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowTypes;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.DatePickerRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.EnrollmentDatePickerRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.IncidentDatePickerRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.RadioButtonsRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.Row;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow.AutoCompleteRow;
import org.hisp.dhis.android.sdk.utils.api.ValueType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

class EnrollmentDataEntryFragmentQuery implements Query<EnrollmentDataEntryFragmentForm> {
    public static final String CLASS_TAG = EnrollmentDataEntryFragmentQuery.class.getSimpleName();

    private final String mOrgUnitId;
    private final String mProgramId;
    private final long mTrackedEntityInstanceId;
    private final String enrollmentDate;
    private String incidentDate;
    private TrackedEntityInstance currentTrackedEntityInstance;
    private Enrollment currentEnrollment;
    private List<OrganisationUnit> assignedOrganisationUnits;
    private UserAccount userAccounts;
    EnrollmentDataEntryFragmentQuery(String mOrgUnitId, String mProgramId,
            long mTrackedEntityInstanceId,
            String enrollmentDate, String incidentDate) {
        this.mOrgUnitId = mOrgUnitId;
        this.mProgramId = mProgramId;
        this.mTrackedEntityInstanceId = mTrackedEntityInstanceId;
        this.enrollmentDate = enrollmentDate;
        this.incidentDate = incidentDate;
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
//            boolean editable = true;
//            boolean shouldNeverBeEdited = false;
//            if (programTrackedEntityAttributes.get(i).getTrackedEntityAttribute().isGenerated()) {
//                editable = false;
//                shouldNeverBeEdited = true;
//            }
//            if (ValueType.COORDINATE.equals(programTrackedEntityAttributes.get(
//                    i).getTrackedEntityAttribute().getValueType())) {
//                GpsController.activateGps(context);
//            }
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
//                    editable, shouldNeverBeEdited, mProgram.getDataEntryMethod());
//            dataEntryRows.add(row);
            Row row = createDataEntryView(programTrackedEntityAttributes.get(i), programTrackedEntityAttributes.get(i).getTrackedEntityAttribute(),
                    getTrackedEntityDataValue(programTrackedEntityAttributes.get(i).getTrackedEntityAttribute().getUid(), trackedEntityAttributeValues));
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

        TrackedEntityAttributeValue trackedEntityAttributeValue = new TrackedEntityAttributeValue();

        trackedEntityAttributeValue.setTrackedEntityAttributeId(trackedEntityAttribute);
        //@sou DOH ID Auto Sequential
        if(trackedEntityAttribute.equals("yeI6AOQjrqg"))
        {
            String code="";
            OrganisationUnit ou_user=new OrganisationUnit();
            OrganisationUnit ou_user_=null;
            List<OrganisationUnit> org=null;
            List<TrackedEntityInstance> tei_list= MetaDataController.getTrackedEntityInstancesFromLocal();
            int count=tei_list.size();
            String seq_count = String.format ("%05d", count+1);
            assignedOrganisationUnits= MetaDataController.getAssignedOrganisationUnits();
            userAccounts=MetaDataController.getUserAccount();
            org=MetaDataController.getOrganisationUnit_list();
            code=org.get(0).getCode();
            int year = Calendar.getInstance().get(Calendar.YEAR);
            String year_=String.valueOf(year);
            String nimhans_=code+"-"+year_.toString()+"-"+seq_count;
            trackedEntityAttributeValue.setTrackedEntityInstanceId(currentTrackedEntityInstance.getTrackedEntityInstance());
            trackedEntityAttributeValue.setValue(nimhans_);
            trackedEntityAttributeValues.add(trackedEntityAttributeValue);
            return trackedEntityAttributeValue;
        }


        //the datavalue didnt exist for some reason. Create a new one.
        trackedEntityAttributeValue.setTrackedEntityAttributeId(trackedEntityAttribute);
        trackedEntityAttributeValue.setTrackedEntityInstanceId(
                currentTrackedEntityInstance.getTrackedEntityInstance());
        trackedEntityAttributeValue.setValue("");
        trackedEntityAttributeValues.add(trackedEntityAttributeValue);
        return trackedEntityAttributeValue;
    }

    public Row createDataEntryView(ProgramTrackedEntityAttribute programTrackedEntityAttribute, TrackedEntityAttribute trackedEntityAttribute, TrackedEntityAttributeValue dataValue) {
        Row row;
        String trackedEntityAttributeName = trackedEntityAttribute.getName();
        TrackedEntityAttributeValue trackedEntityAttributeValue = new TrackedEntityAttributeValue();
        if (trackedEntityAttribute.getOptionSet() != null) {
            OptionSet optionSet = MetaDataController.getOptionSet(trackedEntityAttribute.getOptionSet());
            if (optionSet == null) {
                row = new EditTextRow(trackedEntityAttributeName, programTrackedEntityAttribute.getMandatory(), null, dataValue, DataEntryRowTypes.TEXT);
            } else {
                row = new AutoCompleteRow(trackedEntityAttributeName, programTrackedEntityAttribute.getMandatory(), null, dataValue, optionSet);
            }
        }
        else  if(trackedEntityAttribute.getShortName().equals("phone_number"))
        {
            row = new EditTextRow(trackedEntityAttributeName, programTrackedEntityAttribute.getMandatory(), null, dataValue, DataEntryRowTypes.PHONE_NUMBER);
        }

        //@sou Disable DOH Attribute for Edit
        else  if(trackedEntityAttribute.getShortName().equals("doh_id_no"))
        {
            row = new EditTextRow(trackedEntityAttributeName, programTrackedEntityAttribute.getMandatory(), null, dataValue, DataEntryRowTypes.DOH_ID_NO);
        }

        //@sou_ Todo DOB Age Relation


        else if (trackedEntityAttribute.getValueType().equals(ValueType.TEXT)) {

            row = new EditTextRow(trackedEntityAttributeName, programTrackedEntityAttribute.getMandatory(), null, dataValue, DataEntryRowTypes.TEXT);
        }

        else if (trackedEntityAttribute.getValueType().equals(ValueType.LONG_TEXT)) {
            row = new EditTextRow(trackedEntityAttributeName, programTrackedEntityAttribute.getMandatory(), null, dataValue, DataEntryRowTypes.LONG_TEXT);
        } else if (trackedEntityAttribute.getValueType().equals(ValueType.NUMBER)) {
            row = new EditTextRow(trackedEntityAttributeName, programTrackedEntityAttribute.getMandatory(), null, dataValue, DataEntryRowTypes.NUMBER);
        } else if (trackedEntityAttribute.getValueType().equals(ValueType.INTEGER)) {

            row = new EditTextRow(trackedEntityAttributeName, programTrackedEntityAttribute.getMandatory(), null, dataValue, DataEntryRowTypes.INTEGER);
        }
        else if (trackedEntityAttribute.getValueType().equals(ValueType.INTEGER_ZERO_OR_POSITIVE)) {
            row = new EditTextRow(trackedEntityAttributeName, programTrackedEntityAttribute.getMandatory(), null, dataValue, DataEntryRowTypes.INTEGER_ZERO_OR_POSITIVE);
        }
        else if (trackedEntityAttribute.getValueType().equals(ValueType.INTEGER_POSITIVE)) {
            row = new EditTextRow(trackedEntityAttributeName, programTrackedEntityAttribute.getMandatory(), null, dataValue, DataEntryRowTypes.INTEGER_POSITIVE);
        } else if (trackedEntityAttribute.getValueType().equals(ValueType.INTEGER_NEGATIVE)) {
            row = new EditTextRow(trackedEntityAttributeName, programTrackedEntityAttribute.getMandatory(), null, dataValue, DataEntryRowTypes.INTEGER_NEGATIVE);
        } else if (trackedEntityAttribute.getValueType().equals(ValueType.BOOLEAN)) {
            row = new RadioButtonsRow(trackedEntityAttributeName, programTrackedEntityAttribute.getMandatory(), null, dataValue, DataEntryRowTypes.BOOLEAN);
        } else if (trackedEntityAttribute.getValueType().equals(ValueType.TRUE_ONLY)) {
            row = new CheckBoxRow(trackedEntityAttributeName, programTrackedEntityAttribute.getMandatory(), null, dataValue);
        } else if (trackedEntityAttribute.getValueType().equals(ValueType.DATE)) {
            row = new DatePickerRow(trackedEntityAttributeName, programTrackedEntityAttribute.getMandatory(), null, dataValue, programTrackedEntityAttribute.getAllowFutureDate());
        }
        else {
            row = new EditTextRow(trackedEntityAttributeName, programTrackedEntityAttribute.getMandatory(), null, dataValue, DataEntryRowTypes.LONG_TEXT);
        }
        System.out.println("Short Names:"+trackedEntityAttribute.getShortName());
        return row;
    }

}
