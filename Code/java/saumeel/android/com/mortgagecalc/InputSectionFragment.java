package saumeel.android.com.mortgagecalc;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class InputSectionFragment extends Fragment{

    private static EditText homeValue;
    private static EditText downPayment;
    private static EditText interestRate;
    private static EditText propertyTax;
    private static Spinner spinner;
    private static Button calculateButton;

    InputSectionListener activityCommander;

    public interface InputSectionListener{
        public void sendData(double totalTax, double interest, double monthlyPayment, String date);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try
        {
            activityCommander = (InputSectionListener) context;
        }
        catch(ClassCastException e){
            Log.d("Class Class Error", "Error in onAttach !!");
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.input_fragment,container, false);

        spinner = (Spinner)view.findViewById(R.id.spinner);
        homeValue = (EditText)view.findViewById(R.id.inputHomeValue);
        downPayment = (EditText)view.findViewById(R.id.inputDownPayment);
        interestRate = (EditText)view.findViewById(R.id.inputInterestRate);
        propertyTax = (EditText)view.findViewById(R.id.inputPropertyTaxRate);
        calculateButton = (Button)view.findViewById(R.id.calculateButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_array, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        String term = String.valueOf(spinner.getSelectedItem());


        calculateButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        onCalculateButtonClicked(view);
                    }
                }

        );


        return view;

    }

    public void onCalculateButtonClicked(View view){
        boolean flag = true;
        double doubleHomeValue=0;
        double doubleDownPayment=0;
        double doubleInterestRate=0;
        double doublePropertyTaxRate=0;
        double terms=0;

        // each edittext, the input should be a number & the input should not be a negative number.
        try {
            doubleHomeValue = Double.parseDouble(homeValue.getText().toString());
            if (doubleHomeValue<0){
                flag=false;
                homeValue.setError("Input has to be a positive decimal");
            }
        }
        catch(NumberFormatException e){
            flag=false;
            homeValue.setError("Invalid Input !");
        }
        try {
            doubleDownPayment = Double.parseDouble(downPayment.getText().toString());
            if (doubleDownPayment<0){
                flag=false;
                downPayment.setError("Input has to be a positive decimal");
            }
        }
        catch(NumberFormatException e){
            flag=false;
            downPayment.setError("Invalid Input !");
        }
        try {
            doubleInterestRate = Double.parseDouble(interestRate.getText().toString());
            if (doubleInterestRate<0){
                flag=false;
                interestRate.setError("Input has to be a positive decimal");
            }
        }
        catch(NumberFormatException e){
            flag=false;
            interestRate.setError("Invalid Input !");
        }
        try {
            doublePropertyTaxRate = Double.parseDouble(propertyTax.getText().toString());
            if (doublePropertyTaxRate<0){
                flag=false;
                propertyTax.setError("Input has to be a positive decimal");
            }
        }
        catch(NumberFormatException e){
            flag=false;
            propertyTax.setError("Invalid Input !");
        }

        // validation : Downpayment has to be less that the home value
        if (doubleDownPayment>=doubleHomeValue)
        {
            downPayment.setError("Down Payment should be less than the Home Value");
            flag=false;
        }
        // no validation required for dropdown list.
        terms = Double.parseDouble(spinner.getSelectedItem().toString());

        if(flag) {
            // finding monthly mortgage Payment
            double monthlyPayment = 0.0;
            double P = doubleHomeValue - doubleDownPayment;
            double i = doubleInterestRate / (1200); // 12 for months & 100 for percentage.
            double n = terms * 12;

            double Numerator = i * Math.pow((1 + i), n);
            double Denominator = Math.pow((1 + i), n) - 1;

            DecimalFormat df = new DecimalFormat("#.00");

            monthlyPayment = P * Numerator / Denominator;
            // end of finding monthly mortgage Payment

            // finding Total Interest Paid
            double totalInterestPaid = monthlyPayment * n - P;
            //


            // finding property tax
            double propertyTax = doublePropertyTaxRate / 1200 * doubleHomeValue;            //monthly
            double totalPropertyTax = propertyTax * n;        //In all property Tax
            // end of finding property tax

            // find payoffDate
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM");
            String month = sdf.format(cal.getTime());
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");

            cal.add(Calendar.YEAR,(int)terms);
            String year = sdf1.format(cal.getTime());

            String date = month + " " + year;
            // end of payOffDate calculation

            monthlyPayment += propertyTax;
            propertyTax*=n;
//            df.format(monthlyPayment);
//            df.format(propertyTax);
//            df.format(totalInterestPaid);

            activityCommander.sendData(propertyTax, totalInterestPaid, monthlyPayment, date);
            Log.d("Final Result :  ", String.valueOf(monthlyPayment));
            Log.d("Final propertyTax ", String.valueOf(propertyTax));
            Log.d("Final total Interest Paid  ", String.valueOf(totalInterestPaid));
            Log.d("Final total property tax ", String.valueOf(totalPropertyTax));

        }// end of if(flag)
    }

}
