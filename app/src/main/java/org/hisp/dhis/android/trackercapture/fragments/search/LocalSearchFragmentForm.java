package org.hisp.dhis.android.trackercapture.fragments.search;

import org.hisp.dhis.android.sdk.persistence.models.BaseValue;
import org.hisp.dhis.android.sdk.persistence.models.DataValue;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.Row;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalSearchFragmentForm {
    private String organisationUnitId;
    private String program;
    private String queryString;
    private HashMap<String, String> attributeValues;
    private List<TrackedEntityAttribute> trackedEntityAttributes;
    private List<TrackedEntityAttributeValue> trackedEntityAttributeValues;
    private List<Row> dataEntryRows;

    private BaseValue startDate ;
    private BaseValue endDate;
    private BaseValue stageFilter;

    private BaseValue atr_coord;
    private BaseValue de_coord;
    private BaseValue en_fl;

    public BaseValue getEn_fl() {
        return en_fl;
    }

    public void setEn_fl(BaseValue en_fl) {
        this.en_fl = en_fl;
    }

    public BaseValue getDe_coord() {
        return de_coord;
    }

    public void setDe_coord(BaseValue de_coord) {
        this.de_coord = de_coord;
    }

    public BaseValue getAtr_coord() {
        return atr_coord;
    }

    public void setAtr_coord(BaseValue atr_coord) {
        this.atr_coord = atr_coord;
    }

    public BaseValue getStageFilter() {
        return stageFilter;
    }

    public void setStageFilter(BaseValue stageFilter) {
        this.stageFilter = stageFilter;
    }

    public BaseValue getStartDate() {
        return startDate;
    }

    public void setStartDate(BaseValue startDate) {
        this.startDate = startDate;
    }

    public BaseValue getEndDate() {
        return endDate;
    }

    public void setEndDate(BaseValue endDate) {
        this.endDate = endDate;
    }

    public String getOrganisationUnitId() {
        return organisationUnitId;
    }

    public void setOrganisationUnitId(String organisationUnitId) {
        this.organisationUnitId = organisationUnitId;
    }

    public HashMap<String, String> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(HashMap<String, String> attributeValues) {
        this.attributeValues = attributeValues;
    }

    public List<TrackedEntityAttribute> getTrackedEntityAttributes() {
        return trackedEntityAttributes;
    }

    public void setTrackedEntityAttributes(List<TrackedEntityAttribute> trackedEntityAttributes) {
        this.trackedEntityAttributes = trackedEntityAttributes;
    }

    public List<Row> getDataEntryRows() {
        return dataEntryRows;
    }

    public void setDataEntryRows(List<Row> dataEntryRows) {
        this.dataEntryRows = dataEntryRows;
    }

    public List<TrackedEntityAttributeValue> getTrackedEntityAttributeValues() {
        return trackedEntityAttributeValues;
    }

    public void setTrackedEntityAttributeValues(List<TrackedEntityAttributeValue> trackedEntityAttributeValues) {
        this.trackedEntityAttributeValues = trackedEntityAttributeValues;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
}
