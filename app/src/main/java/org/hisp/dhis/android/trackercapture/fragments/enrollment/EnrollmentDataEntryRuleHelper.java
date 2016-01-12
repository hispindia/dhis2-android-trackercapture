package org.hisp.dhis.android.trackercapture.fragments.enrollment;

import android.support.v4.app.Fragment;

import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.persistence.models.DataElement;
import org.hisp.dhis.android.sdk.persistence.models.DataValue;
import org.hisp.dhis.android.sdk.persistence.models.Enrollment;
import org.hisp.dhis.android.sdk.persistence.models.Event;
import org.hisp.dhis.android.sdk.persistence.models.ProgramRule;
import org.hisp.dhis.android.sdk.persistence.models.ProgramRuleAction;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.hisp.dhis.android.sdk.ui.fragments.common.IProgramRuleFragmentHelper;
import org.hisp.dhis.android.sdk.ui.fragments.dataentry.ValidationErrorDialog;
import org.hisp.dhis.android.sdk.utils.services.ProgramRuleService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EnrollmentDataEntryRuleHelper implements IProgramRuleFragmentHelper {

    private final EnrollmentDataEntryFragment enrollmentDataEntryFragment;

    public EnrollmentDataEntryRuleHelper(EnrollmentDataEntryFragment enrollmentDataEntryFragment) {
        this.enrollmentDataEntryFragment = enrollmentDataEntryFragment;
    }

    @Override
    public void initiateEvaluateProgramRules() {
        enrollmentDataEntryFragment.initiateEvaluateProgramRules();
    }

    @Override
    public void mapFieldsToRulesAndIndicators() {
        enrollmentDataEntryFragment.setProgramRulesForTrackedEntityAttributes(new HashMap<String, List<ProgramRule>>());
        for(ProgramRule programRule: enrollmentDataEntryFragment.getForm().getProgram().getProgramRules()) {
            for(String trackedEntityAttribute: ProgramRuleService.getTrackedEntityAttributesInRule(programRule)) {
                List<ProgramRule> rulesForTrackedEntityAttribute = enrollmentDataEntryFragment.getProgramRulesForTrackedEntityAttributes().get(trackedEntityAttribute);
                if(rulesForTrackedEntityAttribute == null) {
                    rulesForTrackedEntityAttribute = new ArrayList<>();
                    rulesForTrackedEntityAttribute.add(programRule);
                    enrollmentDataEntryFragment.getProgramRulesForTrackedEntityAttributes().put(trackedEntityAttribute, rulesForTrackedEntityAttribute);
                } else {
                    rulesForTrackedEntityAttribute.add(programRule);
                }
            }
        }
    }

    @Override
    public Fragment getFragment() {
        return enrollmentDataEntryFragment;
    }

    @Override
    public List<ProgramRule> getProgramRules() {
        return enrollmentDataEntryFragment.getForm().getProgram().getProgramRules();
    }

    @Override
    public Event getEvent() {
        return null;
    }

    @Override
    public void applyCreateEventRuleAction(ProgramRuleAction programRuleAction) {

    }

    @Override
    public void applyDisplayKeyValuePairRuleAction(ProgramRuleAction programRuleAction) {

    }

    @Override
    public void applyDisplayTextRuleAction(ProgramRuleAction programRuleAction) {

    }

    @Override
    public Enrollment getEnrollment() {
        return enrollmentDataEntryFragment.getForm().getEnrollment();
    }

    @Override
    public TrackedEntityAttributeValue getTrackedEntityAttributeValue(String id) {
        return enrollmentDataEntryFragment.getForm().getTrackedEntityAttributeValueMap().get(id);
    }

    @Override
    public DataValue getDataElementValue(String uid) {
        return null;
    }

    @Override
    public void saveDataElement(String uid) {

    }

    @Override
    public void saveTrackedEntityAttribute(String id) {
        enrollmentDataEntryFragment.getSaveThread().schedule();
    }

    @Override
    public void updateUi() {

    }

    /**
     * Displays a warning dialog to the user, indicating the data entry rows with values in them
     * are being hidden due to program rules.
     * @param fragment
     * @param affectedValues
     */
    @Override
    public void showWarningHiddenValuesDialog(Fragment fragment, ArrayList<String> affectedValues) {
        ArrayList<String> dataElementNames = new ArrayList<>();
        for (String s : affectedValues) {
            DataElement de = MetaDataController.getDataElement(s);
            if (de != null) {
                dataElementNames.add(de.getDisplayName());
            }
        }
        if(dataElementNames.isEmpty()) {
            return;
        }
        if (enrollmentDataEntryFragment.getValidationErrorDialog() == null || !enrollmentDataEntryFragment.getValidationErrorDialog().isVisible()) {
            ValidationErrorDialog validationErrorDialog = ValidationErrorDialog
                    .newInstance(fragment.getString(org.hisp.dhis.android.sdk.R.string.warning_hidefieldwithvalue), dataElementNames
                    );
            enrollmentDataEntryFragment.setValidationErrorDialog(validationErrorDialog);
            if(fragment.isAdded()) {
                enrollmentDataEntryFragment.getValidationErrorDialog().show(fragment.getChildFragmentManager());
            }
        }
    }

    @Override
    public void flagDataChanged(boolean hasChanged) {
        enrollmentDataEntryFragment.flagDataChanged(hasChanged);
    }

    @Override
    public void applyShowWarningRuleAction(ProgramRuleAction programRuleAction) {
        String uid = programRuleAction.getDataElement();
        if(uid == null) {
            uid = programRuleAction.getTrackedEntityAttribute();
        }
        enrollmentDataEntryFragment.getListViewAdapter().showWarningOnIndex(uid, programRuleAction.getContent());
    }

    @Override
    public void applyShowErrorRuleAction(ProgramRuleAction programRuleAction) {
        //todo: implement
    }


    @Override
    public void applyHideFieldRuleAction(ProgramRuleAction programRuleAction, List<String> affectedFieldsWithValue) {
        enrollmentDataEntryFragment.getListViewAdapter().hideIndex(programRuleAction.getDataElement());
        if(enrollmentDataEntryFragment.containsValue(getDataElementValue(programRuleAction.getDataElement()))) {// form.getDataValues().get(programRuleAction.getDataElement()))) {
            affectedFieldsWithValue.add(programRuleAction.getDataElement());
        }
    }

    @Override
    public void applyHideSectionRuleAction(ProgramRuleAction programRuleAction) {}
}
