/*
 * Copyright (c) 2015, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisp.dhis.android.trackercapture.fragments.programoverview.registerRelationshipDialogFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.structure.Model;

import org.hisp.dhis.android.sdk.R;
import org.hisp.dhis.android.sdk.controllers.Dhis2;
import org.hisp.dhis.android.sdk.controllers.datavalues.DataValueController;
import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.persistence.loaders.DbLoader;
import org.hisp.dhis.android.sdk.persistence.models.Enrollment;
import org.hisp.dhis.android.sdk.persistence.models.Program;
import org.hisp.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.hisp.dhis.android.sdk.persistence.models.Relationship;
import org.hisp.dhis.android.sdk.persistence.models.RelationshipType;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.hisp.dhis.android.sdk.utils.ui.adapters.rows.AbsTextWatcher;
import org.hisp.dhis.android.sdk.utils.ui.dialogs.AutoCompleteDialogFragment;
import org.hisp.dhis.android.sdk.utils.ui.views.CardTextViewButton;
import org.hisp.dhis.android.sdk.utils.ui.views.FontTextView;
import org.hisp.dhis.android.trackercapture.ui.adapters.RelationshipTypeAdapter;
import org.hisp.dhis.android.trackercapture.ui.adapters.TrackedEntityInstanceAdapter;

import java.util.ArrayList;
import java.util.List;

public class RegisterRelationshipDialogFragment extends DialogFragment
        implements View.OnClickListener, LoaderManager.LoaderCallbacks<RegisterRelationshipDialogFragmentForm>,AutoCompleteDialogFragment.OnOptionSelectedListener, AdapterView.OnItemClickListener {
    private static final String TAG = RegisterRelationshipDialogFragment.class.getSimpleName();

    private static final int LOADER_ID = 956401663;

    private RegisterRelationshipDialogFragmentForm mForm;
    private EditText mFilter;
    private CardTextViewButton mRelationshipTypeButton;
    private TextView mDialogLabel;
    private TrackedEntityInstanceAdapter mAdapter;
    private ListView mListView;
    private FontTextView mTrackedEntityInstanceLabel;
    private Spinner mSpinner;
    private RelationshipTypeAdapter mSpinnerAdapter;
    private int mDialogId;

    private static final String EXTRA_TRACKEDENTITYINSTANCEID = "extra:trackedEntityInstanceId";
    private static final String EXTRA_ARGUMENTS = "extra:Arguments";
    private static final String EXTRA_SAVED_INSTANCE_STATE = "extra:savedInstanceState";

    public static RegisterRelationshipDialogFragment newInstance(long trackedEntityInstanceId) {
        RegisterRelationshipDialogFragment dialogFragment = new RegisterRelationshipDialogFragment();
        Bundle args = new Bundle();

        args.putLong(EXTRA_TRACKEDENTITYINSTANCEID, trackedEntityInstanceId);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE,
                R.style.Theme_AppCompat_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        return inflater.inflate(org.hisp.dhis.android.trackercapture.R.layout.dialog_fragment_registerrelationship, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mListView = (ListView) view
                .findViewById(R.id.simple_listview);
        mRelationshipTypeButton = (CardTextViewButton) view.findViewById(org.hisp.dhis.android.trackercapture.R.id.relationshiptypebutton);
        mRelationshipTypeButton.setOnClickListener(this);

        ImageView registerDialogButton = (ImageView) view
                .findViewById(R.id.load_dialog_button);
        ImageView closeDialogButton = (ImageView) view
                .findViewById(R.id.close_dialog_button);
        mTrackedEntityInstanceLabel = (FontTextView) view.findViewById(org.hisp.dhis.android.trackercapture.R.id.tei_label);
        mFilter = (EditText) view
                .findViewById(R.id.filter_options);
        mDialogLabel = (TextView) view
                .findViewById(R.id.dialog_label);
        InputMethodManager imm = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mFilter.getWindowToken(), 0);

        mAdapter = new TrackedEntityInstanceAdapter(getActivity().getLayoutInflater());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        mFilter.addTextChangedListener(new AbsTextWatcher() {
            @Override public void afterTextChanged(Editable s) {
                mAdapter.getFilter().filter(TrackedEntityInstanceAdapter.FILTER_SEARCH + s.toString());
            }
        });

        mSpinner = (Spinner) view.findViewById(org.hisp.dhis.android.trackercapture.R.id.spinner);
        mSpinnerAdapter = new RelationshipTypeAdapter(getLayoutInflater(savedInstanceState));
        mSpinner.setAdapter(mSpinnerAdapter);

        closeDialogButton.setOnClickListener(this);

        //registerDialogButton.setOnClickListener(this);
        registerDialogButton.setVisibility(View.GONE);

        setDialogLabel(org.hisp.dhis.android.trackercapture.R.string.register_relationship);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putBundle(EXTRA_ARGUMENTS, getArguments());
        argumentsBundle.putBundle(EXTRA_SAVED_INSTANCE_STATE, savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, argumentsBundle, this);
    }

    @Override
    public Loader<RegisterRelationshipDialogFragmentForm> onCreateLoader(int id, Bundle args) {
        if (LOADER_ID == id && isAdded()) {
            // Adding Tables for tracking here is dangerous (since MetaData updates in background
            // can trigger reload of values from db which will reset all fields).
            // Hence, it would be more safe not to track any changes in any tables
            List<Class<? extends Model>> modelsToTrack = new ArrayList<>();
            Bundle fragmentArguments = args.getBundle(EXTRA_ARGUMENTS);
            long teiId = fragmentArguments.getLong(EXTRA_TRACKEDENTITYINSTANCEID);

            return new DbLoader<>(
                    getActivity().getBaseContext(), modelsToTrack, new RegisterRelationshipDialogFragmentQuery(
                    teiId)
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<RegisterRelationshipDialogFragmentForm> loader, RegisterRelationshipDialogFragmentForm data) {

        Log.d(TAG, "load finished");
        if (loader.getId() == LOADER_ID && isAdded())
        {
            mListView.setVisibility(View.VISIBLE);

            mForm = data;
            if(mForm.getTrackedEntityInstance()!=null) {
                List<Enrollment> enrollments = DataValueController.getEnrollments(mForm.getTrackedEntityInstance());
                List<TrackedEntityAttribute> attributesToShow = new ArrayList<>();
                String value = "";
                if(enrollments!=null && !enrollments.isEmpty()) {
                    Program program = null;
                    for(Enrollment e: enrollments) {
                        if(e!=null && e.getProgram()!=null && e.getProgram().getProgramTrackedEntityAttributes()!=null) {
                            program = e.getProgram();
                        }
                    }
                    List<ProgramTrackedEntityAttribute> programTrackedEntityAttributes = program.getProgramTrackedEntityAttributes();
                    for(int i = 0; i<programTrackedEntityAttributes.size() && i<2; i++) {
                        attributesToShow.add(programTrackedEntityAttributes.get(i).getTrackedEntityAttribute());
                    }
                    for(int i=0; i<attributesToShow.size() && i<2; i++) {
                        TrackedEntityAttributeValue av = DataValueController.getTrackedEntityAttributeValue(attributesToShow.get(i).getId(), mForm.getTrackedEntityInstance().getLocalId());
                        if (av != null && av.getValue() != null) {
                            value +=av.getValue() + " ";
                        }
                    }
                } else {
                    for(int i = 0; i<mForm.getTrackedEntityInstance().getAttributes().size() && i<2; i++) {
                        if(mForm.getTrackedEntityInstance().getAttributes().get(i) != null && mForm.getTrackedEntityInstance().getAttributes().get(i).getValue() != null) {
                            value += mForm.getTrackedEntityInstance().getAttributes().get(i).getValue() + " ";
                        }
                    }
                }
                mTrackedEntityInstanceLabel.setText(value);
            }
            mAdapter.setData(mForm.getRows());
            mAdapter.swapData(mForm.getRows());
        }
    }

    @Override
    public void onLoaderReset(Loader<RegisterRelationshipDialogFragmentForm> loader) {

    }

    /* This method must be called only after onViewCreated() */
    public void setDialogLabel(int resourceId) {
        if (mDialogLabel != null) {
            mDialogLabel.setText(resourceId);
        }
    }

    /* This method must be called only after onViewCreated() */
    public void setDialogLabel(CharSequence sequence) {
        if (mDialogLabel != null) {
            mDialogLabel.setText(sequence);
        }
    }

    public void setDialogId(int dialogId) {
        mDialogId = dialogId;
    }

    public int getDialogId() {
        return mDialogId;
    }

    /* This method must be called only after onViewCreated() */
    public CharSequence getDialogLabel() {
        if (mDialogLabel != null) {
            return mDialogLabel.getText();
        } else {
            return null;
        }
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, TAG);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == org.hisp.dhis.android.trackercapture.R.id.relationshiptypebutton ) {
            RelationshipTypesDialogFragment fragment = RelationshipTypesDialogFragment
                    .newInstance(this);
            fragment.show(getChildFragmentManager());
        } else if(v.getId() == R.id.close_dialog_button) {
            dismiss();
        }
    }

    @Override
    public void onOptionSelected(int dialogId, int position, String id, String name) {
        RelationshipType relationshipType = MetaDataController.getRelationshipType(id);
        if(relationshipType!=null) {
            List<String> values = new ArrayList<>();
            mRelationshipTypeButton.setText(relationshipType.getDisplayName());
            values.add(new String(relationshipType.getaIsToB()));
            values.add(new String(relationshipType.getbIsToA()));
            mSpinnerAdapter.swapData(values, id);
        } else {
            mSpinnerAdapter.swapData(null, null);
        }
    }

    public boolean registerRelationship(TrackedEntityInstance relative) {
        Relationship relationship = new Relationship();
        if(mSpinnerAdapter != null && relative != null) {
            if(mSpinnerAdapter.getRelationshipType()!=null) {
                relationship.setRelationship(mSpinnerAdapter.getRelationshipType());
                if(mSpinner.getSelectedItemPosition() == 0) {
                    relationship.setTrackedEntityInstanceA(mForm.getTrackedEntityInstance().getTrackedEntityInstance());
                    relationship.setTrackedEntityInstanceB(relative.getTrackedEntityInstance());
                } else {
                    relationship.setTrackedEntityInstanceB(mForm.getTrackedEntityInstance().getTrackedEntityInstance());
                    relationship.setTrackedEntityInstanceA(relative.getTrackedEntityInstance());
                }
                relationship.save();
                mForm.getTrackedEntityInstance().setFromServer(false);
                mForm.getTrackedEntityInstance().update();
                return true;
            }
        }
        return false;
    }

    public void showConfirmRelationshipDialog(final int position) {
        Dhis2.showConfirmDialog(getActivity(), getString(R.string.confirm),
                getString(R.string.confirm_relationship), getString(R.string.yes),
                getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TrackedEntityInstance relative = DataValueController.getTrackedEntityInstance(mAdapter.getItemId(position));
                        if(registerRelationship(relative)){
                            RegisterRelationshipDialogFragment.this.dismiss();
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.please_select_relationshiptype), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showConfirmRelationshipDialog(position);
    }
}
