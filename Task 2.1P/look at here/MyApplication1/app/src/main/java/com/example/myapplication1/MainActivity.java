package com.example.myapplication1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        //initial the text in the app
        TextView Task = findViewById(R.id.Task);
        Task.setText(R.string.Task);
        TextView Select = findViewById(R.id.Select);
        Select.setText(R.string.select_source_unit_and_value);
        TextView destination_text = findViewById(R.id.destination_text);
        destination_text.setText(R.string.destination_text);
        TextView Empty = findViewById(R.id.Empty);
        Empty.setText(R.string.conversion_type);

        //find the module
        Spinner conversion_type = findViewById(R.id.Conversion_Type);
        EditText input_value = findViewById(R.id.inputValue);
        Spinner source_unit = findViewById(R.id.Source_Unit);
        Spinner destination_unit = findViewById(R.id.destination_unit);
        TextView answer = findViewById(R.id.Answer);
        Button convertButton = findViewById(R.id.convertButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Conversion_Type, R.layout.custom_spinner_item);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        conversion_type.setAdapter(adapter);

        // Set up item selected listener for sourceUnitSpinner
        conversion_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDestinationUnits(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set up conversion button click listener
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performConversion();
            }
        });
    }

    private void updateDestinationUnits(String selectedSourceUnit) {
        Spinner source_unit = findViewById(R.id.Source_Unit);
        Spinner destination_unit = findViewById(R.id.destination_unit);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                getUnitOptions(selectedSourceUnit), R.layout.custom_spinner_item);
        adapter1.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        destination_unit.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                getUnitOptions(selectedSourceUnit), R.layout.custom_spinner_item);
        adapter2.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        source_unit.setAdapter(adapter2);
    }

    private int getUnitOptions(String selectedSourceUnit) {
        if (Objects.equals(selectedSourceUnit, "Length")) {
            return R.array.Unit_Of_Length;
        } else if (Objects.equals(selectedSourceUnit, "Weight")) {
            return R.array.Unit_Of_Weight;
        } else if (Objects.equals(selectedSourceUnit, "Temperature")) {
            return R.array.Unit_Of_Temperature;
        } else return R.array.Conversion_Type;
    }

    private void performConversion() {
        TextView answer = findViewById(R.id.Answer);
        EditText input_value = findViewById(R.id.inputValue);
        Spinner source_unit = findViewById(R.id.Source_Unit);
        Spinner destination_unit = findViewById(R.id.destination_unit);

        String sourceUnit = source_unit.getSelectedItem().toString();
        String destinationUnit = destination_unit.getSelectedItem().toString();
        double value = Double.parseDouble(input_value.getText().toString());

        // Perform conversion based on selected units
        double result;
        if (sourceUnit.equals(destinationUnit)) {
            result = value; // No conversion needed
        } else {
            result = convertUnits(sourceUnit, destinationUnit, value);
        }

        // Display result
        answer.setText("Answer: " + String.valueOf(result) + " " + destinationUnit);
    }

    private double convertUnits(String sourceUnit, String destinationUnit, double value) {
        // Add conversion logic here based on the provided conversion factors
        // Length Conversions
        if (sourceUnit.equals("Inch") && destinationUnit.equals("Foot")) {
            return value * 2.54 / 30.48;
        } else if (sourceUnit.equals("Inch") && destinationUnit.equals("Yard")) {
            return value * 2.54 / 91.44;
        } else if (sourceUnit.equals("Inch") && destinationUnit.equals("Mile")) {
            return value * 2.54 / 160934;
        } else if (sourceUnit.equals("Foot") && destinationUnit.equals("Inch")) {
            return value * 30.48 / 2.54;
        } else if (sourceUnit.equals("Foot") && destinationUnit.equals("Yard")) {
            return value * 30.48 / 91.44;
        } else if (sourceUnit.equals("Foot") && destinationUnit.equals("Mile")) {
            return value * 30.48 / 160934;
        } else if (sourceUnit.equals("Yard") && destinationUnit.equals("Inch")) {
            return value * 91.44 / 2.54;
        } else if (sourceUnit.equals("Yard") && destinationUnit.equals("Foot")) {
            return value * 91.44 / 30.48;
        } else if (sourceUnit.equals("Yard") && destinationUnit.equals("Mile")) {
            return value * 91.44 / 160934;
        } else if (sourceUnit.equals("Mile") && destinationUnit.equals("Inch")) {
            return value * 160934 / 2.54;
        } else if (sourceUnit.equals("Mile") && destinationUnit.equals("Yard")) {
            return value * 160934 / 91.44;
        } else if (sourceUnit.equals("Mile") && destinationUnit.equals("Foot")) {
            return value * 160934 / 30.48;
        }
        //Weight conversions
        if (sourceUnit.equals("Pound") && destinationUnit.equals("Ounce")) {
            return value * 453.592 / 28.3495;
        } else if (sourceUnit.equals("Pound") && destinationUnit.equals("Ton")) {
            return value * 453.592 / 907185;
        } else if (sourceUnit.equals("Ounce") && destinationUnit.equals("Pound")) {
            return value * 28.3495 / 453.592;
        } else if (sourceUnit.equals("Ounce") && destinationUnit.equals("Ton")) {
            return value * 28.3495 / 907185;
        } else if (sourceUnit.equals("Ton") && destinationUnit.equals("Pound")) {
            return value * 907185 / 453.592;
        } else if (sourceUnit.equals("Ton") && destinationUnit.equals("Ounce")) {
            return value * 907185 / 28.3495;
        }

        //Temperature
        if (sourceUnit.equals("Celsius") && destinationUnit.equals("Fahrenheit")) {
            return value * 1.8 + 32;
        } else if (sourceUnit.equals("Celsius") && destinationUnit.equals("Kelvin")) {
            return value + 273.15;
        } else if (sourceUnit.equals("Fahrenheit") && destinationUnit.equals("Celsius")) {
            return (value - 32) / 1.8;
        } else if (sourceUnit.equals("Fahrenheit") && destinationUnit.equals("Kelvin")) {
            return (value - 32) / 1.8 + 273.15;
        } else if (sourceUnit.equals("Kelvin") && destinationUnit.equals("Celsius")) {
            return value - 273.15;
        } else if (sourceUnit.equals("Kelvin") && destinationUnit.equals("Fahrenheit")) {
            return (value - 273.15) * 1.8 + 32;
        }
        return value; // If no conversion found, return the original value
    }
}