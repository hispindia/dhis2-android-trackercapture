package org.hisp.dhis.android.trackercapture.fragments.TrackerAssociate;

import android.content.Context;
import android.content.DialogInterface;
import android.view.MenuItem;

import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.TrackerAssociateRowActionListener;
import org.hisp.dhis.android.sdk.utils.UiUtils;
import org.hisp.dhis.android.trackercapture.activities.HolderActivity;
import org.hisp.dhis.android.trackercapture.fragments.enrollment.EnrollmentDataEntryFragment;

public class TrackerAssociateEnrollmentDataEntryFragment extends EnrollmentDataEntryFragment {
    TrackerAssociateRowActionListener actionListener;

    public TrackerAssociateRowActionListener getActionListener() {
        return actionListener;
    }

    public void setActionListener(TrackerAssociateRowActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            showConfirmDiscardDialog();
            return true;
        } else if (menuItem.getItemId() == org.hisp.dhis.android.sdk.R.id.action_new_event) {
            proceed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void proceed(){

        if(validate()){
            confirmSave();
            //TODO: set the inserted TEI id
            if(actionListener!=null)
                actionListener.getValue(TrackerAssociateRowActionListener.STATES.ADD).onValueChange(getTrackedEntityInstance().getTrackedEntityInstance());
//                actionListener.setValue(TrackerAssociateRowActionListener.STATES.ADD
//                    ,getTrackedEntityInstance().getTrackedEntityInstance());
            //this.getFragmentManager().beginTransaction().remove(this).commit();
            getFragmentManager().popBackStack();
        }
    }


    protected void showConfirmDiscardDialog() {
        UiUtils.showConfirmDialog(getActivity(),
                getString(org.hisp.dhis.android.sdk.R.string.discard), getString(org.hisp.dhis.android.sdk.R.string.discard_confirm_changes),
                getString(org.hisp.dhis.android.sdk.R.string.discard),
                getString(org.hisp.dhis.android.sdk.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //discard
                        discardChanges();
                        getFragmentManager().popBackStack();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cancel
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public boolean onBackPressed() {
        //super.onBackPressed();
        showConfirmDiscardDialog();
        return true;
    }

   @Override
    public boolean doBack(){
       showConfirmDiscardDialog();
       return false;
   }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((HolderActivity)getActivity()).setOnBackPressedListener(this);
    }



}
