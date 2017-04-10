package de.ktran.anno1404warenrechner.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sbgapps.simplenumberpicker.decimal.DecimalPickerHandler;
import com.sbgapps.simplenumberpicker.utils.ThemeUtil;

import de.ktran.anno1404warenrechner.R;

public class NumberDialog extends DialogFragment {

    private static final String ARG_REFERENCE = "ARG_REFERENCE";
    private static final String ARG_RELATIVE = "ARG_RELATIVE";
    private static final String ARG_NATURAL = "ARG_NATURAL";
    private static final String ARG_THEME = "ARG_THEME";
    private static final String ARG_SAVED_VALUE = "ARG_SAVED_VALUE";

    private static final int NB_KEYS = 10;
    private static final int DEFAULT_REFERENCE = 0;

    private AlertDialog dialog;
    private TextView numberTextView;
    private ImageButton backspaceButton;

    private int reference = DEFAULT_REFERENCE;
    private boolean sign = true;
    private boolean natural = false;
    private String decimalSeparator;
    private int theme = R.style.SimpleNumberPickerTheme;
    private boolean defaultNegative = false;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null != savedInstanceState) {
            this.assignArguments(savedInstanceState);
        } else if(null != this.getArguments()) {
            this.assignArguments(this.getArguments());
        }

        this.setStyle(1, this.theme);
        this.setCancelable(false);
    }
    public void onStart() {
        super.onStart();
        this.onNumberChanged();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ARG_REFERENCE", this.reference);
        outState.putBoolean("ARG_RELATIVE", this.sign);
        outState.putBoolean("ARG_NATURAL", this.natural);
        outState.putInt("ARG_THEME", this.theme);
        outState.putBoolean("ARG_DEFAULT_NEGATIVE", this.defaultNegative);
        outState.putString("ARG_SAVED_VALUE", this.numberTextView.getText().toString());
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TypedArray attributes = getContext().obtainStyledAttributes(theme, R.styleable.SimpleNumberPicker);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.number_picker, null);

        // Init number
        int color = attributes.getColor(R.styleable.SimpleNumberPicker_snpKeyColor,
                ContextCompat.getColor(getContext(), android.R.color.secondary_text_light));
        numberTextView = (TextView) view.findViewById(R.id.tv_hex_number);
        numberTextView.setTextColor(color);
        if (null != savedInstanceState && savedInstanceState.containsKey(ARG_SAVED_VALUE))
            numberTextView.setText(savedInstanceState.getString(ARG_SAVED_VALUE));
        else if (defaultNegative)
            numberTextView.setText("⌂");
        // Init backspace
        color = attributes.getColor(R.styleable.SimpleNumberPicker_snpBackspaceColor,
                ContextCompat.getColor(getContext(), android.R.color.secondary_text_light));
        backspaceButton = (ImageButton) view.findViewById(R.id.key_backspace);
        backspaceButton.setImageDrawable(
                ThemeUtil.makeSelector(getContext(), R.drawable.snp_ic_backspace_black_24dp, color));
        backspaceButton.setOnClickListener(v -> {
            if (numberTextView.getText().length() == 1 && defaultNegative) return;

            CharSequence number = numberTextView.getText()
                    .subSequence(0, numberTextView.getText().length() - 1);
            if (1 == number.length() && '-' == number.charAt(0)) number = "";
            numberTextView.setText(number);
            onNumberChanged();
        });
        backspaceButton.setOnLongClickListener(v -> {
            numberTextView.setText("");
            onNumberChanged();
            return true;
        });

        // Create dialog
        dialog = new AlertDialog.Builder(getContext(), theme)
                .setView(view)
                .setPositiveButton(android.R.string.ok, (dialog12, which) -> {
                    String result = numberTextView.getText().toString();
                    if (result.isEmpty()) result = "0";
                    result = result.replace(',', '.');
                    result = result.replace("⌂", "-");
                    if (result.equals("-")) result = "0";
                    final float number = Float.parseFloat(result);
                    final Activity activity = getActivity();
                    final Fragment fragment = getParentFragment();
                    if (activity instanceof DecimalPickerHandler) {
                        final DecimalPickerHandler handler = (DecimalPickerHandler) activity;
                        handler.onDecimalNumberPicked(reference, number);
                    } else if (fragment instanceof DecimalPickerHandler) {
                        final DecimalPickerHandler handler = (DecimalPickerHandler) fragment;
                        handler.onDecimalNumberPicked(reference, number);
                    }
                    dismiss();
                })
                .setNegativeButton(android.R.string.cancel, (dialog1, which) -> dismiss())
                .create();

        // Init dialog
        color = attributes.getColor(R.styleable.SimpleNumberPicker_snpDialogBackground,
                ContextCompat.getColor(getContext(), android.R.color.white));
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(color));
        }

        // Init keys
        View.OnClickListener listener = v -> {
            int key = (int) v.getTag();
            String id = numberTextView.getText() + Integer.toString(key);
            numberTextView.setText(id);
            onNumberChanged();
        };

        color = attributes.getColor(
                R.styleable.SimpleNumberPicker_snpKeyColor,
                ThemeUtil.getThemeAccentColor(getContext()));
        TypedArray ids = getResources().obtainTypedArray(R.array.snp_key_ids);
        for (int i = 0; i < NB_KEYS; i++) {
            TextView key = (TextView) view.findViewById(ids.getResourceId(i, -1));
            key.setTag(i);
            key.setOnClickListener(listener);
            key.setTextColor(color);
        }

        // Init sign
        TextView sign = (TextView) view.findViewById(R.id.key_house);
        if (this.sign) {
            sign.setTextColor(color);
            sign.setOnClickListener(v -> {
                String number = numberTextView.getText().toString();
                if (number.startsWith("⌂")) {
                    numberTextView.setText(number.substring(1));
                } else {
                    numberTextView.setText("⌂" + number);
                }
                onNumberChanged();
            });
        } else {
            sign.setVisibility(View.INVISIBLE);
        }

        // Init decimal separator
        initDecimalSeparator();
        TextView separator = (TextView) view.findViewById(R.id.key_point);
        if (natural) {
            separator.setVisibility(View.INVISIBLE);
        } else {
            separator.setText(decimalSeparator);
            separator.setTextColor(color);
            separator.setOnClickListener(v -> {
                if (numberTextView.getText().toString().contains(decimalSeparator)) return;
                String number = numberTextView.getText().toString();
                numberTextView.setText(number + decimalSeparator);
                onNumberChanged();
            });
        }

        ids.recycle();
        attributes.recycle();

        return dialog;
    }

    private void onNumberChanged() {
        backspaceButton.setEnabled(0 != numberTextView.length());
        if (0 == numberTextView.getText().length()) {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        } else if (numberTextView.getText().charAt(0) == '⌂' && numberTextView.getText().length() == 1) {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        } else {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        }
    }

    private void assignArguments(Bundle args) {
        if (args.containsKey(ARG_REFERENCE))
            reference = args.getInt(ARG_REFERENCE);
        if (args.containsKey(ARG_RELATIVE))
            sign = args.getBoolean(ARG_RELATIVE);
        if (args.containsKey(ARG_NATURAL))
            natural = args.getBoolean(ARG_NATURAL);
        if (args.containsKey(ARG_THEME))
            theme = args.getInt(ARG_THEME);
        if (args.containsKey("ARG_DEFAULT_NEGATIVE"))
            defaultNegative = args.getBoolean("ARG_DEFAULT_NEGATIVE");
    }

    private void initDecimalSeparator() {
        decimalSeparator = "" + ".";
    }

    private static NumberDialog newInstance(int reference, boolean sign, boolean natural, int theme, boolean defaultNegative) {
        Bundle args = new Bundle();
        args.putInt("ARG_REFERENCE", reference);
        args.putBoolean("ARG_RELATIVE", sign);
        args.putBoolean("ARG_NATURAL", natural);
        args.putInt("ARG_THEME", theme);
        args.putBoolean("ARG_DEFAULT_NEGATIVE", defaultNegative);
        NumberDialog fragment = new NumberDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static class Builder {
        private int reference = 0;
        private boolean sign = true;
        private boolean natural = false;
        private int theme;
        private boolean defaultNegative = false;

        public Builder() {
            this.theme = com.sbgapps.simplenumberpicker.R.style.SimpleNumberPickerTheme;
        }

        public NumberDialog.Builder setReference(int reference) {
            this.reference = reference;
            return this;
        }

        public NumberDialog.Builder setSign(boolean sign) {
            this.sign = sign;
            return this;
        }

        public NumberDialog.Builder setNatural(boolean natural) {
            this.natural = natural;
            return this;
        }

        public NumberDialog.Builder setTheme(int theme) {
            this.theme = theme;
            return this;
        }

        public NumberDialog.Builder setDefaultNegative(boolean isNegative) {
            this.defaultNegative = isNegative;
            return this;
        }

        public NumberDialog create() {
            return NumberDialog.newInstance(this.reference, this.sign, this.natural, this.theme, this.defaultNegative);
        }
    }
}
