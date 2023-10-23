package com.rutvik.locofit.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.rutvik.locofit.BaseActivity;
import com.rutvik.locofit.MainActivity;
import com.rutvik.locofit.R;
import com.rutvik.locofit.models.User;
import com.rutvik.locofit.util.DBHandler;

import java.util.Calendar;

public class RegisterFormActivity extends Activity {
    private EditText formFirstNameField, formLastNameField, formDOBFIield, formHeightField, formWeightField, formEmailField;
    private Spinner genderSpinner;
    private Button signupBtn;
    private TextView registerFormMessageField;
    private DBHandler dbHandler = new DBHandler(RegisterFormActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);
        formFirstNameField = findViewById(R.id.formFirstNameField);
        formLastNameField = findViewById(R.id.formLastNameField);
        formDOBFIield = findViewById(R.id.formDOBFIield);
        formHeightField = findViewById(R.id.formHeightField);
        formWeightField = findViewById(R.id.formWeightField);
        formEmailField = findViewById(R.id.formEmailField);
        genderSpinner = findViewById(R.id.genderSpinner);
        signupBtn = findViewById(R.id.signupBtn);
        registerFormMessageField = findViewById(R.id.registerFormMessageField);
        String[] genders = {"Male", "Female", "Other"};
        final String[] selectedGender = {"Other"};
        String dob[] = {"0000", "00", "00"};
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        formDOBFIield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        RegisterFormActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                formDOBFIield.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                                dob[0] = String.valueOf(year);
                                dob[1] = String.valueOf(monthOfYear + 1).length() == 1 ? "0" + String.valueOf(monthOfYear+1) : String.valueOf(monthOfYear + 1);
                                dob[2] = String.valueOf(dayOfMonth).length() == 1 ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);

                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(RegisterFormActivity.this, android.R.layout.simple_spinner_item, genders);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedGender[0] = genders[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = formFirstNameField.getText().toString().trim();
                String lastName = formLastNameField.getText().toString().trim();
                int height;
                int weight;
                try {
                    height = Integer.parseInt(formHeightField.getText().toString().trim());
                    weight = Integer.parseInt(formWeightField.getText().toString().trim());}
                catch (Exception e) {
                    height = 0;
                    weight = 0;
                }
                String email = formEmailField.getText().toString().trim();
                String DOB = dob[0] + "-" + dob[1] + "-" + dob[2];
                String gender = selectedGender[0];
                if(firstName.equals("") || lastName.equals("") || email.equals("") || dob.equals("") || gender.equals("") || height == 0 || weight == 0) {
                    registerFormMessageField.setText("All fields are required!");
                } else {
                    User user = new User(username, password, firstName, lastName, DOB, gender, email, height, weight);
                    dbHandler.addUser(user);
                    SharedPreferences sharedPreferences = getSharedPreferences("com.rutvik.locofit.SHAREDPREFERENCES", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.commit();
                    Intent intent1 = new Intent(RegisterFormActivity.this, BaseActivity.class);
                    startActivity(intent1);
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHandler.closeDatabase();
    }
}