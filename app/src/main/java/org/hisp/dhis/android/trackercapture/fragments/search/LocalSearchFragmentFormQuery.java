package org.hisp.dhis.android.trackercapture.fragments.search;

import android.content.Context;
import android.util.Log;

import org.hisp.dhis.android.sdk.controllers.GpsController;
import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.persistence.loaders.Query;
import org.hisp.dhis.android.sdk.persistence.models.BaseValue;
import org.hisp.dhis.android.sdk.persistence.models.Option;
import org.hisp.dhis.android.sdk.persistence.models.OptionSet;
import org.hisp.dhis.android.sdk.persistence.models.Program;
import org.hisp.dhis.android.sdk.persistence.models.ProgramStage;
import org.hisp.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.CheckBoxRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowFactory;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.DatePickerRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.RadioButtonsOptionSetRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.Row;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow.AutoCompleteRow;
import org.hisp.dhis.android.sdk.utils.api.ValueType;

import java.util.ArrayList;
import java.util.List;

import static org.hisp.dhis.android.trackercapture.fragments.search.LocalSearchResultFragmentFormQuery.ATR_COORD_ID;

public class LocalSearchFragmentFormQuery implements Query<LocalSearchFragmentForm> {
    private String TAG = this.getClass().getSimpleName();
    private String orgUnitId;
    private String programId;

    public LocalSearchFragmentFormQuery(String orgUnitId, String programId) {
        this.orgUnitId = orgUnitId;
        this.programId = programId;
    }

    @Override
    public LocalSearchFragmentForm query(Context context) {
        LocalSearchFragmentForm form = new LocalSearchFragmentForm();
        form.setOrganisationUnitId(orgUnitId);
        form.setProgram(programId);

        Log.d(TAG, orgUnitId + programId);

        Program program = MetaDataController.getProgram(programId);
        if (program == null || orgUnitId == null) {
            return form;
        }
        List<ProgramTrackedEntityAttribute> programAttrs =
                program.getProgramTrackedEntityAttributes();
        List<TrackedEntityAttributeValue> values = new ArrayList<>();
        List<Row> dataEntryRows = new ArrayList<>();

        BaseValue startDate = new BaseValue(){};
        BaseValue endDate = new BaseValue(){};
        BaseValue stageFl = new BaseValue(){};
        BaseValue atrFl = new BaseValue() {};
        BaseValue deFl = new BaseValue() {};
        BaseValue enrollmentFl = new BaseValue() {};

        dataEntryRows.add(new DatePickerRow("StartDate",false,null,startDate,true));
        dataEntryRows.add(new DatePickerRow("End Date ", false, null, endDate,true));
        //TODO://options can be dynamically generated
        List<Option> options = new ArrayList<>();
        for(ProgramStage stage:MetaDataController.getProgramStages(programId)){
            Option option = new Option();
            option.setCode(stage.getUid());
            option.setDisplayName(stage.getDisplayName());
            options.add(
              option
            );
        }
        dataEntryRows.add(new CheckBoxRow("Persons Location ",false,null,deFl));
        dataEntryRows.add(new CheckBoxRow("Last Known Location", false, null, atrFl));
        dataEntryRows.add(new RadioButtonsOptionSetRow("Contain Stage",false,"",stageFl,options));
        dataEntryRows.add(new CheckBoxRow("Active Enrollment",false,null,enrollmentFl));
        form.setStageFilter(stageFl);
        form.setStartDate(startDate);
        form.setEndDate(endDate);
        form.setAtr_coord(atrFl);
        form.setDe_coord(deFl);
        form.setEn_fl(enrollmentFl);
        for (ProgramTrackedEntityAttribute ptea : programAttrs) {
            TrackedEntityAttribute trackedEntityAttribute = ptea.getTrackedEntityAttribute();
            if(!ptea.getTrackedEntityAttributeId().equals(ATR_COORD_ID)){
                TrackedEntityAttributeValue value = new TrackedEntityAttributeValue();
                value.setTrackedEntityAttributeId(trackedEntityAttribute.getUid());
                values.add(value);

                if (ptea.getMandatory()) {
                    ptea.setMandatory(
                            !ptea.getMandatory()); // HACK to skip mandatory fields in search form
                }
                if (ValueType.COORDINATE.equals(ptea.getTrackedEntityAttribute().getValueType())) {
                    GpsController.activateGps(context);
                }
                boolean isRadioButton = program.getDataEntryMethod();
                if(!isRadioButton){
                    isRadioButton = ptea.isRenderOptionsAsRadio();
                }
                Row row = DataEntryRowFactory.createDataEntryView(ptea.getMandatory(),
                        ptea.getAllowFutureDate(), trackedEntityAttribute.getOptionSet(),
                        trackedEntityAttribute.getName(), value, trackedEntityAttribute.getValueType(),
                        true, false, isRadioButton);
                dataEntryRows.add(row);
            }

        }
        form.setTrackedEntityAttributeValues(values);
        form.setDataEntryRows(dataEntryRows);
        return form;


    }
}
