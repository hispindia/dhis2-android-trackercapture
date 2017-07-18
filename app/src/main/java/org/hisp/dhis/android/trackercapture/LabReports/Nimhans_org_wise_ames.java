package org.hisp.dhis.android.trackercapture.LabReports;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.hisp.dhis.android.trackercapture.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hisp.dhis.android.trackercapture.R.id.spinner;

/**
 * Created by Sourabh Bhardwaj on 21-05-2017.
 */

public class Nimhans_org_wise_ames extends Activity {
    private List<OrganisationUnit> assignedOrganisationUnits;
    private Spinner spinner1, spinner2;
    private Button btnSubmit;
    private String org_id,name;
    private HashMap<Integer,String> spinnerMap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apex_org_select);

        addItemsOnSpinner2();
        addListenerOnButton();

    }




    // add items into spinner dynamically
    public void addItemsOnSpinner2() {

        spinner2 = (Spinner) findViewById(spinner);
        List<String> list = new ArrayList<String>();
        List<String> list_id = new ArrayList<String>();
        assignedOrganisationUnits= MetaDataController.getAssignedOrganisationUnits();

        String[] spinnerArray = new String[assignedOrganisationUnits.size()];
        spinnerMap = new HashMap<Integer, String>();

        for (int i = 0; i < assignedOrganisationUnits.size(); i++)
        {
            spinnerMap.put(i,assignedOrganisationUnits.get(i).getId());
            spinnerArray[i] = assignedOrganisationUnits.get(i).getLabel();
        }
        for (int i=0;i<assignedOrganisationUnits.size();i++)
        {
            list.add(assignedOrganisationUnits.get(i).getLabel());
            list_id.add(assignedOrganisationUnits.get(i).getId());

        }
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);


//        spinner2.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                name = spinner2.getSelectedItem().toString();
                org_id = spinnerMap.get(spinner2.getSelectedItemPosition());

                Log.d("Spinner_name_id",name+org_id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }


    // get the selected dropdown list value
    public void addListenerOnButton() {

        spinner2 = (Spinner) findViewById(spinner);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Nimhans_org_wise_ames.this, NimhansLabAmes.class);
                intent.putExtra("orgid", org_id);
                intent.putExtra("orgname", name);
                Nimhans_org_wise_ames.this.startActivity(intent);
            }

        });
    }
}
