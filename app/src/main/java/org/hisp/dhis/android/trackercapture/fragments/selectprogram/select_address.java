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

import org.hisp.dhis.android.sdk.persistence.models.Cascading;
import org.hisp.dhis.android.trackercapture.R;
import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.hisp.dhis.android.sdk.ui.fragments.selectprogram.CustomOnItemSelectedListener;
import org.hisp.dhis.android.sdk.ui.fragments.selectprogram.SelectAddress;
import org.hisp.dhis.android.trackercapture.fragments.enrollmentdate.EnrollmentDateFragmentQuery;

import java.util.ArrayList;
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
        spinner4 = (Spinner) findViewById(R.id.spinner4);
        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        btnSubmit.setEnabled(false);
        addItemsOnSpinner2();
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner2() {

        List<String> list = new ArrayList<String>();

        List<String> list11 = new ArrayList<String>();

//        OrganisationUnit mOrgUnit = MetaDataController.getOrganisationUnitID(String.valueOf("Tiruvallur"));


//        HashMap<String, String> spinnerMap = new HashMap<String, String>();

        List<OrganisationUnit> orgUnits = MetaDataController
                .getorganisationUnitsLevelWise(3);
        for (OrganisationUnit orgunit: orgUnits)
        {

//            spinnerMap.put(orgunit.getId(), orgunit.getLabel());

            list.add(orgunit.getId());
            list11.add(orgunit.getLabel());

        }
//        ArrayAdapter<HashMap<String, String>> adapter = new ArrayAdapter<HashMap<String,   String>>(this, android.R.layout.simple_spinner_item);
//
//        adapter.add(spinnerMap);
//        spinner1.setAdapter(adapter);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list11);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                OrganisationUnit mOrgUnit = MetaDataController.getOrganisationUnitID(String.valueOf(spinner1.getSelectedItem()));

                Log.e("Spinner1m", String.valueOf(mOrgUnit.getId()));

                List<OrganisationUnit> orgUnits1 = MetaDataController
                        .getLevel5OrgUnitWithParentLevel3(String.valueOf(mOrgUnit.getId()));

                List<String> list1 = new ArrayList<String>();
                List<String> list22 = new ArrayList<String>();

                for (OrganisationUnit orgUnit1 : orgUnits1) {

                    list1.add(orgUnit1.getId());
                    list22.add(orgUnit1.getLabel());
                }


                ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(select_address.this,
                        android.R.layout.simple_spinner_item, list22);
                dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(dataAdapter1);
                Log.e("Spinner1",String.valueOf(spinner1.getSelectedItem()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                OrganisationUnit mOrgUnit2 = MetaDataController.getOrganisationUnitID(String.valueOf(spinner2.getSelectedItem()));
                Log.e("Spinner2m", String.valueOf(mOrgUnit2.getId()));
                List<OrganisationUnit> orgUnits2 = MetaDataController
                        .getLevel7OrgUnitWithParentLevel5(String.valueOf(mOrgUnit2.getId()));
                List<String> list2 = new ArrayList<String>();
                List<String> list33 = new ArrayList<String>();
                for (OrganisationUnit orgUnit2 : orgUnits2) {

                    list2.add(orgUnit2.getId());
                    list33.add(orgUnit2.getLabel());
                }
                ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(select_address.this,
                        android.R.layout.simple_spinner_item, list33);
                dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner3.setAdapter(dataAdapter2);
                Log.e("Spinner2",String.valueOf(spinner2.getSelectedItem()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                OrganisationUnit mOrgUnit3 = MetaDataController.getOrganisationUnitIDParentLevel8(String.valueOf(spinner3.getSelectedItem()));
                Log.e("Spinner3m", String.valueOf(mOrgUnit3.getId()));
                List<OrganisationUnit> orgUnits3 = MetaDataController
                        .getLevel9OrgUnitWithParentLevel8(String.valueOf(mOrgUnit3.getId()));
                List<String> list3 = new ArrayList<String>();
                List<String> list44 = new ArrayList<String>();
                for (OrganisationUnit orgUnit3 : orgUnits3) {

                    list3.add(orgUnit3.getId());
                    list44.add(orgUnit3.getLabel());
                }
                ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(select_address.this,
                        android.R.layout.simple_spinner_item, list44);
                dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner4.setAdapter(dataAdapter3);
                btnSubmit.setEnabled(true);
                Log.e("Spinner3",String.valueOf(spinner3.getSelectedItem()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                Toast.makeText(select_address.this,
//                        "OnClickListener : " +
//                                "\nSpinner 1 : "+ String.valueOf(spinner1.getSelectedItem()) +
//                                "\nSpinner 2 : "+ String.valueOf(spinner2.getSelectedItem()),
//                        Toast.LENGTH_SHORT).show();

                SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(select_address.this.getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("NameOfShared", "Value");
                editor.commit();


//        OrgUnitDialogFragment3 ldf = new OrgUnitDialogFragment3();
//        Bundle args = new Bundle();
//        args.putString("orgid", orgUnitId);
//        ldf.setArguments(args);
//        getFragmentManager().beginTransaction().add(R.id.select_village, ldf).commit();

                int int_condition=1;
//                Intent intent11 = new Intent(org.hisp.dhis.android.trackercapture.fragments.selectprogram.select_address.this, SelectProgramFragment.class);
//                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent1);
                Intent intent = new Intent(select_address.this, EnrollmentDateSetterHelper.class);
                Intent intent_enroll = new Intent(select_address.this, EnrollmentDateFragmentQuery.class);

                Intent intent1 = new Intent("INTENT_NAME").putExtra("district", String.valueOf(spinner1.getSelectedItem()));
                LocalBroadcastManager.getInstance(select_address.this).sendBroadcast(intent);

                Cascading cascade=new Cascading();


                cascade.setDistrict(String.valueOf(spinner1.getSelectedItem()));
                cascade.setTaluk(String.valueOf(spinner2.getSelectedItem()));
                cascade.setHabitat(String.valueOf(spinner3.getSelectedItem()));
                cascade.setVillage(String.valueOf(spinner4.getSelectedItem()));
                cascade.save();


                Log.e("cascade", cascade.getDistrict());
                Log.e("cascade", cascade.getHabitat());
                Log.e("cascade", cascade.getTaluk());
                Log.e("cascade", cascade.getVillage());


                intent_enroll.putExtra("district", String.valueOf(spinner1.getSelectedItem()));
                intent_enroll.putExtra("taluk", String.valueOf(spinner2.getSelectedItem()));
                intent_enroll.putExtra("village", String.valueOf(spinner3.getSelectedItem()));
                intent_enroll.putExtra("habitation", String.valueOf(spinner4.getSelectedItem()));

                intent.putExtra("district",String.valueOf(spinner1.getSelectedItem()));
                intent.putExtra("taluk",String.valueOf(spinner2.getSelectedItem()));
                intent.putExtra("village",String.valueOf(spinner3.getSelectedItem()));
                intent.putExtra("habitation",String.valueOf(spinner4.getSelectedItem()));
                intent.putExtra("enroll", int_condition);

                select_address.this.startActivity(intent);
            }

        });

    }
}
