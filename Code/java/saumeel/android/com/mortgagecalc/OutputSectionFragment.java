package saumeel.android.com.mortgagecalc;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;


public class OutputSectionFragment extends Fragment{

    private static TextView putTotalTax;
    private static TextView putTotalInterest;
    private static TextView putMonthlyPayment;
    private static TextView putPayOffDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.output_fragment,container,false);

        putTotalTax = (TextView)view.findViewById(R.id.putTotalTax);
        putTotalInterest = (TextView)view.findViewById(R.id.putTotalInterest);
        putMonthlyPayment = (TextView)view.findViewById(R.id.putMontlyPayment);
        putPayOffDate = (TextView)view.findViewById(R.id.putPayOffDate);

        return view;
    }

    public void setData(double totalTax, double interest, double monthlyPayment, String date)
    {

        putTotalTax.setText(String.format("%.2f",totalTax));
        putTotalInterest.setText(String.format("%.2f", interest));
        putMonthlyPayment.setText(String.format("%.2f",monthlyPayment));
        putPayOffDate.setText(date);
    }
}
