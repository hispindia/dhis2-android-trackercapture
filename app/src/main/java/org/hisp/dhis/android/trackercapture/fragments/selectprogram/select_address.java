package org.hisp.dhis.android.trackercapture.fragments.selectprogram;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.JSONObject;
import org.json.JSONArray;
import org.hisp.dhis.android.sdk.controllers.realm.ROrganisationHelper;
import org.hisp.dhis.android.sdk.controllers.realm.ROrganisationUnit;
import org.hisp.dhis.android.sdk.persistence.models.Cascading;
import org.hisp.dhis.android.trackercapture.R;
import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnitforcascading;
import org.hisp.dhis.android.sdk.ui.fragments.selectprogram.CustomOnItemSelectedListener;
import org.hisp.dhis.android.sdk.ui.fragments.selectprogram.SelectAddress;
import org.hisp.dhis.android.trackercapture.fragments.enrollmentdate.EnrollmentDateFragmentQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

import java.util.LinkedHashMap;

import static org.hisp.dhis.android.trackercapture.R.layout.select_address;

/**
 * Created by Sourabh Bhardwaj on 09-03-2017.
 */

public class select_address extends Activity{


    private Spinner spinner1,spinner2,spinner3,spinner4;
    private Button btnSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(select_address);



        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner4 = (Spinner) findViewById(R.id.spinner3_);
        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        btnSubmit.setEnabled(false);
        addItemsOnSpinner2();
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner2() {

        List<String> list = new ArrayList<String>();
        List<String> list11 = new ArrayList<String>();
//        List<OrganisationUnitforcascading> orgUnits = MetaDataController
//                .getorganisationUnitsLevelWise1(3);
        List<ROrganisationUnit> organisationUnitListlevel3= ROrganisationHelper.getOrgFromLocalByLevel(3);
        for (ROrganisationUnit orgunit: organisationUnitListlevel3)
        {
            list.add(orgunit.getId());
            list11.add(orgunit.getDisplayName());

        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list11);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                String orgname=String.valueOf(spinner1.getSelectedItem());
//                OrganisationUnitforcascading mOrgUnit = MetaDataController.getOrganisationUnitID1(String.valueOf(spinner1.getSelectedItem()));
                List<ROrganisationUnit> mOrgUnit1 = ROrganisationHelper.getOrganisationUnitID(String.valueOf(String.valueOf(spinner1.getSelectedItem())));
                Iterator<ROrganisationUnit> iter = mOrgUnit1.iterator();
                String ouuid = iter.next().getId();
                List<ROrganisationUnit> organisationUnitList_=ROrganisationHelper.getOrgFromLocalByLevel(ouuid,5);


//                List<OrganisationUnitforcascading> orgUnits1 = MetaDataController
//                        .getLevel5OrgUnitWithParentLevel31(String.valueOf(mOrgUnit.getId()));

                List<String> list1 = new ArrayList<String>();
                List<String> list22 = new ArrayList<String>();

                for (ROrganisationUnit orgUnit1 : organisationUnitList_) {

                    list1.add(orgUnit1.getId());
                    list22.add(orgUnit1.getDisplayName());
                }


                ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(select_address.this,
                        android.R.layout.simple_spinner_item, list22);
                dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(dataAdapter1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                OrganisationUnitforcascading mOrgUnit2 = MetaDataController.getOrganisationUnitID1(String.valueOf(spinner2.getSelectedItem()));
//                Log.e("Spinner2_district", String.valueOf(mOrgUnit2.getId()));
//
//                List<ROrganisationUnit> organisationUnitList_=ROrganisationHelper.getOrgFromLocalByLevel(String.valueOf(mOrgUnit2.getId()),6);
//
////                List<OrganisationUnitforcascading> orgUnits2 = MetaDataController
////                        .getLevel6OrgUnitWithParentLevel51(String.valueOf(mOrgUnit2.getId()));

                List<ROrganisationUnit> mOrgUnit1 = ROrganisationHelper.getOrganisationUnitID(String.valueOf(String.valueOf(spinner2.getSelectedItem())));
                Iterator<ROrganisationUnit> iter = mOrgUnit1.iterator();
                String ouuid = iter.next().getId();
                List<ROrganisationUnit> organisationUnitList_=ROrganisationHelper.getOrgFromLocalByLevel(ouuid,6);

                List<String> list2 = new ArrayList<String>();
                List<String> list33 = new ArrayList<String>();
                for (ROrganisationUnit orgUnit2 : organisationUnitList_) {

                    list2.add(orgUnit2.getId());
                    list33.add(orgUnit2.getDisplayName());
                }
                ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(select_address.this,
                        android.R.layout.simple_spinner_item, list33);
                dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner4.setAdapter(dataAdapter2);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                OrganisationUnitforcascading mOrgUnit44 = MetaDataController.getOrganisationUnitID1(String.valueOf(spinner4.getSelectedItem()));
//                Log.e("Spinner_block", String.valueOf(mOrgUnit44.getId()));
//                List<ROrganisationUnit> organisationUnitList_=ROrganisationHelper.getOrgFromLocalByLevel(String.valueOf(mOrgUnit44.getId()),6);

//                List<OrganisationUnitforcascading> orgUnits2 = MetaDataController
//                        .getLevel7OrgUnitWithParentLevel61(String.valueOf(mOrgUnit44.getId()));

                List<ROrganisationUnit> mOrgUnit1 = ROrganisationHelper.getOrganisationUnitID(String.valueOf(String.valueOf(spinner2.getSelectedItem())));
                Iterator<ROrganisationUnit> iter = mOrgUnit1.iterator();
                String ouuid = iter.next().getId();
                List<ROrganisationUnit> organisationUnitList_=ROrganisationHelper.getOrgFromLocalByLevel(ouuid,7);

                List<String> list22 = new ArrayList<String>();
                List<String> list333 = new ArrayList<String>();
                for (ROrganisationUnit orgUnit22 : organisationUnitList_) {

                    list22.add(orgUnit22.getId());
                    list333.add(orgUnit22.getDisplayName());
                }
                ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(select_address.this,
                        android.R.layout.simple_spinner_item, list333);
                dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner3.setAdapter(dataAdapter2);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                btnSubmit.setEnabled(true);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(select_address.this.getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("NameOfShared", "Value");
                editor.commit();
                int int_condition=1;

                Intent intent = new Intent(select_address.this, EnrollmentDateSetterHelper.class);
                Intent intent_enroll = new Intent(select_address.this, EnrollmentDateFragmentQuery.class);
                LocalBroadcastManager.getInstance(select_address.this).sendBroadcast(intent);
                Cascading cascade=new Cascading();


                cascade.setDistrict(String.valueOf(spinner1.getSelectedItem()));
                cascade.setTaluk(String.valueOf(spinner2.getSelectedItem()));
                cascade.setHabitat(String.valueOf(spinner3.getSelectedItem()));
                cascade.save();


                Log.e("cascade", cascade.getDistrict());
                Log.e("cascade", cascade.getHabitat());
                Log.e("cascade", cascade.getTaluk());


                intent_enroll.putExtra("district", String.valueOf(spinner1.getSelectedItem()));
                intent_enroll.putExtra("taluk", String.valueOf(spinner2.getSelectedItem()));
                intent_enroll.putExtra("village", String.valueOf(spinner3.getSelectedItem()));

                intent.putExtra("district",String.valueOf(spinner1.getSelectedItem()));
                intent.putExtra("taluk",String.valueOf(spinner2.getSelectedItem()));
                intent.putExtra("village",String.valueOf(spinner3.getSelectedItem()));
                intent.putExtra("enroll", int_condition);

                select_address.this.startActivity(intent);
            }

        });

    }
}
