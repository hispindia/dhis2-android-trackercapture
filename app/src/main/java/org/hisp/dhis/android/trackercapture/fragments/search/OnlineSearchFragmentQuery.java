package org.hisp.dhis.android.trackercapture.fragments.search;

import android.content.Context;
import android.util.Log;

import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.persistence.loaders.Query;
import org.hisp.dhis.android.sdk.persistence.models.BaseValue;
import org.hisp.dhis.android.sdk.persistence.models.Program;
import org.hisp.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowFactory;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.DatePickerRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.Row;

import java.util.ArrayList;
import java.util.List;


public class OnlineSearchFragmentQuery implements Query<OnlineSearchFragmentForm> {
    public static final String TAG = OnlineSearchFragmentQuery.class.getSimpleName();
    private String orgUnit;
    private String programId;

    public OnlineSearchFragmentQuery(String orgUnit, String programId) {
        this.programId = programId;
        this.orgUnit = orgUnit;
    }

    @Override
    public OnlineSearchFragmentForm query(Context context) {
        OnlineSearchFragmentForm form = new OnlineSearchFragmentForm();
        form.setOrganisationUnit(orgUnit);
        form.setProgram(programId);

        Log.d(TAG, orgUnit + programId);

        Program program = MetaDataController.getProgram(programId);
        if (program == null || orgUnit == null) {
            return form;
        }
        List<ProgramTrackedEntityAttribute> programAttrs =
                program.getProgramTrackedEntityAttributes();
        List<TrackedEntityAttributeValue> values = new ArrayList<>();
        List<Row> dataEntryRows = new ArrayList<>();
        BaseValue startDate = new BaseValue(){};
        BaseValue endDate = new BaseValue(){};
        dataEntryRows.add(new DatePickerRow("StartDate",false,null,startDate,true));
        dataEntryRows.add(new DatePickerRow("End Date ", false, null, endDate,true));
        form.setStartDate(startDate);
        form.setEndDate(endDate);
        for (ProgramTrackedEntityAttribute ptea : programAttrs) {
            TrackedEntityAttribute trackedEntityAttribute = ptea.getTrackedEntityAttribute();
            TrackedEntityAttributeValue value = new TrackedEntityAttributeValue();
            value.setTrackedEntityAttributeId(trackedEntityAttribute.getUid());
            values.add(value);

            if (ptea.getMandatory()) {
                ptea.setMandatory(
                        !ptea.getMandatory()); // HACK to skip mandatory fields in search form
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
        form.setTrackedEntityAttributeValues(values);
        form.setDataEntryRows(dataEntryRows);
        return form;
    }
}
