package org.hisp.dhis.android.trackercapture.fragments.TrackerAssociate;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import org.hisp.dhis.android.sdk.persistence.Dhis2Application;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.TrackerAssociateRowActionListener;
import org.hisp.dhis.android.sdk.utils.UiUtils;
import org.hisp.dhis.android.trackercapture.R;
import org.hisp.dhis.android.trackercapture.activities.HolderActivity;
import org.hisp.dhis.android.trackercapture.fragments.search.LocalSearchFragment;
import org.hisp.dhis.android.trackercapture.fragments.search.LocalSearchResultFragment;

import static org.hisp.dhis.android.trackercapture.activities.HolderActivity.ARG_TYPE;
import static org.hisp.dhis.android.trackercapture.activities.HolderActivity.ARG_TYPE_LOCALSEARCHRESULTFRAGMENT;

public class TrackerAssociateSearchFragment extends LocalSearchFragment{
    TrackerAssociateRowActionListener actionListener;

    public void setActionListener(TrackerAssociateRowActionListener actionListener) {
        this.actionListener = actionListener;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {

            buildQuery();
            navigateToSearchResult();
            return true;
        } else if (id == android.R.id.home) {
            showConfirmDiscardDialog();
            return true;
        }

        return false;
        //return super.onOptionsItemSelected(item);
    }

    protected void showConfirmDiscardDialog() {
        UiUtils.showConfirmDialog(getActivity(),
                getString(org.hisp.dhis.android.sdk.R.string.discard), getString(org.hisp.dhis.android.sdk.R.string.discard_confirm_changes),
                getString(org.hisp.dhis.android.sdk.R.string.discard),
                getString(org.hisp.dhis.android.sdk.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //discard

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
    private void navigateToSearchResult(){
        TrackerAssociateSearchResultFragment fragment = new TrackerAssociateSearchResultFragment();
        fragment.setActionListener(actionListener);
        Bundle bundle = new Bundle();

        bundle.putString(LocalSearchResultFragment.EXTRA_ORGUNIT, mForm.getOrganisationUnitId());
        bundle.putString(LocalSearchResultFragment.EXTRA_PROGRAM, mForm.getProgram());
        bundle.putSerializable(LocalSearchResultFragment.EXTRA_ATTRIBUTEVALUEMAP, mForm.getAttributeValues());
        bundle.putString(ARG_TYPE, ARG_TYPE_LOCALSEARCHRESULTFRAGMENT);

        fragment.setArguments(bundle);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame,fragment);
        ft.addToBackStack("TA_SEARCH");
        //Dhis2Application.getEventBus().unregister(this);
        ft.commit();
    }

}
