package com.amitupadhyay.getcountrycode;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.minimumstack.amit_upadhyay_it.datastructure.CountryCode;

import java.util.Locale;

public class NumberValidation extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NumberValidation";
    private Spinner mCountryCode;
    private EditText mPhone;
    private Button mValidateButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_validation);
        mCountryCode = (Spinner) findViewById(R.id.phone_cc);
        mPhone = (EditText) findViewById(R.id.phone_number);
        mValidateButton = (Button) findViewById(R.id.button_validate);
        mValidateButton.setOnClickListener(this);
        mCountryCode.setAdapter(new CountryCodesAdapter());
        mCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryCode cc = (CountryCode) mCountryCode.getAdapter().getItem(position);
                Log.d(TAG, " Show ee : " + cc.getRegionName());
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // FIXME this doesn't consider creation because of configuration change
        PhoneNumberUtil util = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber myNum = getMyNumber(this);
        if (myNum != null) {
            CountryCode cc = new CountryCode();
            cc.setRegionCode(util.getRegionCodeForNumber(myNum));
            if (cc.getRegionCode() == null)
                cc.setRegionCode(util.getRegionCodeForCountryCode(myNum.getCountryCode()));
//            mCountryCode.setSelection(ccList.getPositionForId(cc));
            //mPhone.setText(String.valueOf(myNum.getNationalNumber()));
        } else {
            final TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            final String regionCode = tm.getSimCountryIso().toUpperCase(Locale.US);
            CountryCode cc = new CountryCode();
            cc.setRegionCode(regionCode);
            cc.setCountryCode(util.getCountryCodeForRegion(regionCode));
//            mCountryCode.setSelection(ccList.getPositionForId(cc));
        }
    }

    /**
     * Returns the (parsed) number stored in this device SIM card.
     */
    @SuppressLint("HardwareIds")
    public Phonenumber.PhoneNumber getMyNumber(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String regionCode = tm.getSimCountryIso().toUpperCase(Locale.US);
            return PhoneNumberUtil.getInstance().parse(tm.getLine1Number(), regionCode);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onClick(View view) {
        String the_country_code = mCountryCode.getSelectedItem().toString();
        Toast.makeText(this, the_country_code, Toast.LENGTH_SHORT).show();
    }
}
