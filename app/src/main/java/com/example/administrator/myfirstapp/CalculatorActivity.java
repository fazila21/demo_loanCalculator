package com.example.administrator.myfirstapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static android.provider.Settings.NameValueTable.VALUE;

public class CalculatorActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        etLoanAmount = (EditText) findViewById(R.id.loan_amount);
        etDownPayment = (EditText) findViewById(R.id.down_payment);
        etTerm = (EditText) findViewById(R.id.term);
        etAnnualInterestRate = (EditText) findViewById(R.id.annual_interest_rate);

        tvMonthlyPayment = (TextView) findViewById(R.id.monthly_repayment);
        tvTotalRePayment = (TextView) findViewById(R.id.total_repayment);
        tvTotalInterest = (TextView) findViewById(R.id.total_interest);
        tvAverageMonthlyInterest = (TextView) findViewById(R.id.average_monthly_interest);

        // control if shared preference is empty - default value
        sharedPreferences = getSharedPreferences("LocalHistory", Context.MODE_PRIVATE);
        etLoanAmount.setText(sharedPreferences.getString(LoanAmount, ""));
        etDownPayment.setText(sharedPreferences.getString(DownPayment, ""));
        etAnnualInterestRate.setText(sharedPreferences.getString(InterestRate, ""));
        etTerm.setText(sharedPreferences.getString(Term, ""));
    }

    private EditText etLoanAmount, etDownPayment, etTerm, etAnnualInterestRate;
    private TextView tvMonthlyPayment, tvTotalRePayment, tvTotalInterest, tvAverageMonthlyInterest;
    public SharedPreferences sharedPreferences;

    // constant due to can be used multiple times with different keys
    private static final String LoanAmount = "LA";
    private static final String DownPayment = "DP";
    private static final String InterestRate = "IR";
    private static final String Term = "T";

    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_calculate:
                Log.d("Check","Button calculate clicked!");
                calculate();
                break;
            case R.id.button_reset:
                Log.d("Check","Button reset clicked!");
                reset();
                break;
        }
    }

    private void calculate(){
        String amount = etLoanAmount.getText().toString();
        String downPayment = etDownPayment.getText().toString();
        String interestRate = etAnnualInterestRate.getText().toString();
        String term = etTerm.getText().toString();

        double loanAmount = Double.parseDouble(amount) - Double.parseDouble(downPayment);
        double interest = Double.parseDouble(interestRate) / 12 / 100;
        double noOfMonth = (Integer.parseInt(term) * 12);

        if(noOfMonth > 0){
            double monthlyRepayment = Double.parseDouble(String.format("%.2f", loanAmount * ( interest + ( interest / (Math.pow( (1+interest), noOfMonth)-1) ))));
            double totalRepayment = Double.parseDouble(String.format("%.2f", monthlyRepayment * noOfMonth));
            double totalInterest = Double.parseDouble(String.format("%.2f", totalRepayment - loanAmount));
            double monthlyInterest = Double.parseDouble(String.format("%.2f", totalInterest / noOfMonth));

            tvMonthlyPayment.setText(String.valueOf(monthlyRepayment));
            tvTotalRePayment.setText(String.valueOf(totalRepayment));
            tvTotalInterest.setText(String.valueOf(totalInterest));
            tvAverageMonthlyInterest.setText(String.valueOf(monthlyInterest));

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(LoanAmount, amount);
            editor.putString(DownPayment, downPayment);
            editor.putString(InterestRate, interestRate);
            editor.putString(Term, term);
            editor.commit();
        }
    }

    private void reset(){
        etLoanAmount.setText("");
        etDownPayment.setText("");
        etTerm.setText("");
        etAnnualInterestRate.setText("");

        tvMonthlyPayment.setText(R.string.default_result);
        tvTotalRePayment.setText(R.string.default_result);
        tvTotalInterest.setText(R.string.default_result);
        tvAverageMonthlyInterest.setText(R.string.default_result);
    }
}
