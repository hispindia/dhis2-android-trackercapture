package org.hisp.dhis.android.trackercapture.fragments;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.hisp.dhis.android.trackercapture.R;
import org.hisp.dhis.android.trackercapture.fragments.TrackerAssociate.TrackerAssociateEnrollmentDataEntryFragment;

public class HolderFragment extends DialogFragment {
    Fragment fragmentToAtt;

    public void setFragmentToAtt(Fragment fragmentToAtt) {
        this.fragmentToAtt = fragmentToAtt;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_holder,container,false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getFragmentManager()
                .beginTransaction()
                .show(fragmentToAtt)
                .commit();
    }
}
