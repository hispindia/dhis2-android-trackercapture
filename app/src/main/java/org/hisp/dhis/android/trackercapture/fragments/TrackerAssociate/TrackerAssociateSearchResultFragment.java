package org.hisp.dhis.android.trackercapture.fragments.TrackerAssociate;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;

import org.hisp.dhis.android.sdk.events.OnTrackerItemClick;
import org.hisp.dhis.android.sdk.persistence.Dhis2Application;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.TrackerAssociateRowActionListener;
import org.hisp.dhis.android.sdk.utils.UiUtils;
import org.hisp.dhis.android.trackercapture.R;
import org.hisp.dhis.android.trackercapture.activities.HolderActivity;
import org.hisp.dhis.android.trackercapture.fragments.search.LocalSearchResultFragment;

public class TrackerAssociateSearchResultFragment extends LocalSearchResultFragment {
    protected TrackerAssociateRowActionListener actionListener;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Dhis2Application.getEventBus().register(this);



    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        //Dhis2Application.getEventBus().unregister(this);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            getFragmentManager().popBackStack();
            return true;
        }
        return false;
        //return super.onOptionsItemSelected(item);
    }



    @Subscribe
    public void onItemClick(final OnTrackerItemClick eventClick) {
        UiUtils.showConfirmDialog(getActivity(), getActivity().getString(R.string.confirm),
            getActivity().getString(R.string.tracker_associate_selection_confirmation),
            getActivity().getString(R.string.yes), getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    actionListener.getValue(TrackerAssociateRowActionListener.STATES.SEARCH)
                            .onValueChange(((TrackedEntityInstance)eventClick.getItem()).getTrackedEntityInstance());
                    dialogInterface.dismiss();
                    getFragmentManager().popBackStack("EN_DATA_ENTRY", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            });


    }
    public void setActionListener(TrackerAssociateRowActionListener actionListener) {
        this.actionListener = actionListener;
    }


}
