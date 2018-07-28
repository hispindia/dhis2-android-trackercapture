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

package org.hisp.dhis.android.trackercapture.fragments.programoverview;

import android.content.Context;
import android.util.Log;

import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.controllers.tracker.TrackerController;
import org.hisp.dhis.android.sdk.persistence.loaders.Query;
import org.hisp.dhis.android.sdk.persistence.models.DataValue;
import org.hisp.dhis.android.sdk.persistence.models.Enrollment;
import org.hisp.dhis.android.sdk.persistence.models.Event;
import org.hisp.dhis.android.sdk.persistence.models.Program;
import org.hisp.dhis.android.sdk.persistence.models.ProgramIndicator;
import org.hisp.dhis.android.sdk.persistence.models.ProgramStage;
import org.hisp.dhis.android.sdk.persistence.models.ProgramStageDataElement;
import org.hisp.dhis.android.sdk.persistence.models.ProgramStageSection;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.IndicatorRow;
import org.hisp.dhis.android.sdk.utils.Utils;
import org.hisp.dhis.android.sdk.utils.comparators.EventDateComparator;
import org.hisp.dhis.android.sdk.utils.services.ProgramIndicatorService;
import org.hisp.dhis.android.sdk.utils.support.DateUtils;
import org.hisp.dhis.android.trackercapture.ui.rows.programoverview.ProgramStageEventRow;
import org.hisp.dhis.android.trackercapture.ui.rows.programoverview.ProgramStageLabelRow;
import org.hisp.dhis.android.trackercapture.ui.rows.programoverview.ProgramStageRow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static org.hisp.dhis.android.trackercapture.fragments.programoverview.ProgramOverviewFragment.QUARANTINE;

class ProgramOverviewFragmentQuery implements Query<ProgramOverviewFragmentForm> {

    public static final String CLASS_TAG = ProgramOverviewFragmentQuery.class.getSimpleName();

    private static final String ANIMAL_STATUS_DE = "v7sgdI8CtvP";

    private final String mProgramId;
    private final long mTrackedEntityInstanceId;


    private   String NOTIFICATION_VALUE = "";
    private   String ETHUNIZEDANIMAL = "";
    private   String CHECKIN_1 = "";
    private   String CHECKIN_2 = "";
    private   String TESTRESULTS = "";
    private   String DEADANIMAL = "";
    private   String ASSESSMENT_DECISION = "";
    private   String FOLLOWUP = "";
    private   String FOLLOWUP_OTHER = "";
    private static final String ANIMAL_EXPOSED_INDICATOR = "hE8L9tjVdSX";
    private static final String EVENT_NOTIFICATION = "PwGD626AbHf";
    private static final String RABIES_FOLLOWUP = "MkiHGIm385w";
    private static final String LAB_INVESTIGATION = "eSOtGji0yna";
    private static final String REPORT_DETAILS = "bXZaSp2arEk";
    private static  String ANIMALID_BARCODE = null;
    private static final String HUMAN_EXPOSURE = "Hs1zoGOwY8B";
    private static final String ANIMAL_EXPOSURE = "oLY6uR5jJh9";

    private static final String HUMAN_EXPOSURE_STAGE = "DbsGMk0zLxr";
    private static final String ANIMAL_EXPOSURE_STAGE = "R8zfsjiFerK";


    private static final String ANIMAL_DETAILS_ATTR_ID="S8DQwjTgtSV";
    private static final String RABIESASSESMENT="ww8DSCToHag";
    public static final String QUARANTINE="IXdxLjRSFT8";
    public static final String QUARANTINE_SCHEDULER = "SH5ad8iQpQB";


    public ProgramOverviewFragmentQuery(String programId, long trackedEntityInstanceId) {
        mProgramId = programId;
        mTrackedEntityInstanceId = trackedEntityInstanceId;
    }

    @Override
    public ProgramOverviewFragmentForm query(Context context) {
        ProgramOverviewFragmentForm programOverviewFragmentForm = new ProgramOverviewFragmentForm();
        programOverviewFragmentForm.setProgramIndicatorRows(new LinkedHashMap<ProgramIndicator, IndicatorRow>());

        Program program = MetaDataController.getProgram(mProgramId);
        TrackedEntityInstance trackedEntityInstance = TrackerController.getTrackedEntityInstance(mTrackedEntityInstanceId);

        programOverviewFragmentForm.setProgram(program);
        programOverviewFragmentForm.setTrackedEntityInstance(trackedEntityInstance);
        programOverviewFragmentForm.setDateOfEnrollmentLabel(program.getEnrollmentDateLabel());
        programOverviewFragmentForm.setIncidentDateLabel(program.getIncidentDateLabel());

        if(trackedEntityInstance == null) {
            return programOverviewFragmentForm;
        }
        List<Enrollment> enrollments = TrackerController.getEnrollments(mProgramId, trackedEntityInstance);
        Enrollment activeEnrollment = null;
        if(enrollments!=null) {
            for(Enrollment enrollment: enrollments) {
                if(enrollment.getStatus().equals(Enrollment.ACTIVE)) {
                    activeEnrollment = enrollment;
                }
            }
        }
        if (activeEnrollment==null) {
            return programOverviewFragmentForm;
        }
        programOverviewFragmentForm.setEnrollment(activeEnrollment);
        programOverviewFragmentForm.setDateOfEnrollmentValue(Utils.removeTimeFromDateString(activeEnrollment.getEnrollmentDate()));
        programOverviewFragmentForm.setIncidentDateValue(Utils.removeTimeFromDateString(activeEnrollment.getIncidentDate()));
        List<TrackedEntityAttributeValue> trackedEntityAttributeValues =
                TrackerController.getVisibleTrackedEntityAttributeValues(trackedEntityInstance.getLocalId());
        if(trackedEntityAttributeValues!=null) {
            if(trackedEntityAttributeValues.size() > 0) {
                programOverviewFragmentForm.setAttribute1Label(MetaDataController.
                        getTrackedEntityAttribute(trackedEntityAttributeValues.get(0).getTrackedEntityAttributeId()).
                        getName());
                programOverviewFragmentForm.setAttribute1Value(trackedEntityAttributeValues.get(0).getValue());
            }
            if(trackedEntityAttributeValues.size() > 1) {
                programOverviewFragmentForm.setAttribute2Label(MetaDataController.
                        getTrackedEntityAttribute(trackedEntityAttributeValues.get(1).getTrackedEntityAttributeId()).
                        getName());
                programOverviewFragmentForm.setAttribute2Value(trackedEntityAttributeValues.get(1).getValue());
            }
        }

        List<ProgramStageRow> programStageRows = getProgramStageRows(activeEnrollment);
        programOverviewFragmentForm.setProgramStageRows(programStageRows);

        List<ProgramIndicator> programIndicators = programOverviewFragmentForm.getProgram().getProgramIndicators();
        if(programIndicators != null ) {
            for(ProgramIndicator programIndicator : programIndicators) {
                if(!programIndicator.isDisplayInForm()){
                    continue;
                }
                String value = ProgramIndicatorService.getProgramIndicatorValue(programOverviewFragmentForm.getEnrollment(), programIndicator);
                if(value==null) {
                    continue;
                }
                IndicatorRow indicatorRow = new IndicatorRow(programIndicator, value,
                        programIndicator.getDisplayDescription());
                programOverviewFragmentForm.getProgramIndicatorRows().put(programIndicator,
                        indicatorRow);
            }

//ToDO Description Added for now
            for(ProgramIndicator programIndicator : programIndicators) {

                String value = ProgramIndicatorService.getProgramIndicatorValue(programOverviewFragmentForm.getEnrollment(), programIndicator);
                IndicatorRow indicatorRow = new IndicatorRow(programIndicator, value,"Test");
                programOverviewFragmentForm.getProgramIndicatorRows().put(programIndicator, indicatorRow);
            }
        }
        else{
            programOverviewFragmentForm.getProgramIndicatorRows().clear();
        }
        return programOverviewFragmentForm;
    }

    private List<ProgramStageRow> getProgramStageRows(Enrollment enrollment) {
        List<ProgramStageRow> rows = new ArrayList<>();
        List<Event> events = enrollment.getEvents(true);
        HashMap<String, List<Event>> eventsByStage = new HashMap<>();

        for(Event event: events) {
//            if(event.getProgramStageId().equals("PwGD626AbHf"))
//            {
//                Long event_=event.getLocalId();
//                DataValue dv_=TrackerController.getDataValue(event.getLocalId(),"YfxxJJKM5vK");
//                DataValue dv_1=TrackerController.getDataValue(event.getLocalId(),"YfxxJJKM5vK");
//
//
//            }



            List<Event> eventsForStage = eventsByStage.get(event.getProgramStageId());
            if(eventsForStage==null) {
                eventsForStage = new ArrayList<>();
                eventsByStage.put(event.getProgramStageId(), eventsForStage);
            }
            eventsForStage.add(event);
        }
        Program program = MetaDataController.getProgram(mProgramId);



        for(ProgramStage programStage: program.getProgramStages()) {


//          List<ProgramStageDataElement> pg_=MetaDataController.getProgramStageDataElements(programStage);

            List<Event> eventsForStage = eventsByStage.get(programStage.getUid());
            ProgramStageLabelRow labelRow = new ProgramStageLabelRow(programStage);
            rows.add(labelRow);
            if(eventsForStage==null) {
                continue;
            }
            else {
                EventDateComparator comparator = new EventDateComparator();
                Collections.sort(eventsForStage, comparator);
            }

            if(programStage.getUid().equals(QUARANTINE)){

                boolean animalDead = false;
                for(Event event: eventsForStage) {

                    if(!animalDead){
                        for(DataValue dv :event.getDataValues()){
                            if(dv.getDataElement().equals(ANIMAL_STATUS_DE)){
                                if(dv.getValue().equalsIgnoreCase("dead")){
                                    animalDead = true;
                                    break;
                                }
                            }
                        }
                        ProgramStageEventRow row = new ProgramStageEventRow(event);
                        row.setLabelRow(labelRow);
                        labelRow.getEventRows().add(row);
                        rows.add(row);
                    }else{
                        event.delete();
                    }

                }
            }else{
                for(Event event: eventsForStage) {
                    ProgramStageEventRow row = new ProgramStageEventRow(event);
                    row.setLabelRow(labelRow);
                    labelRow.getEventRows().add(row);
                    rows.add(row);
                }
            }


        }

        return rows;
    }

    private static <T> boolean isListEmpty(List<T> items) {
        return items == null || items.isEmpty();
    }
}
