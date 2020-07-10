package com.proathome.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;


public class DatePickerFragment extends DialogFragment {

    public static final String TAG = DatePickerFragment.class.getSimpleName();

    private DatePickerDialog.OnDateSetListener listener;
    private static Long mCurrentDate;

    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener,
                                                 Long currentDate) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setListener(listener);
        mCurrentDate = currentDate;
        return fragment;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance(Locale.getDefault());
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                listener, year, month, day);
        if (mCurrentDate != null)
            datePickerDialog.getDatePicker().setMinDate(mCurrentDate);

        return datePickerDialog;
    }
}