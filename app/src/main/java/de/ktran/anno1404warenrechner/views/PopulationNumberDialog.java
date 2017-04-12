package de.ktran.anno1404warenrechner.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sbgapps.simplenumberpicker.utils.ThemeUtil;

import javax.inject.Inject;

import de.ktran.anno1404warenrechner.R;
import de.ktran.anno1404warenrechner.data.DataManager;
import de.ktran.anno1404warenrechner.data.Game;
import de.ktran.anno1404warenrechner.data.PopulationType;
import de.ktran.anno1404warenrechner.views.game.GameActivity;

/**
 * Modified class from SimpleNumberPicker by Stephane Baiget.
 *
 * (C) 2017, Stephane Baiget under the Apache License, Version 2.0
 */
public class PopulationNumberDialog extends DialogFragment {
    private static final String ARG_POPULATION_TYPE = "ARG_POPULATION_TYPE";
    private static final String ARG_FORCE_HOUSE = "ARG_FORCE_HOUSE";
    private static final String ARG_SAVED_VALUE = "ARG_SAVED_VALUE";

    private static final int NB_KEYS = 10;

    private AlertDialog dialog;
    private TextView numberTextView;
    private ImageButton backspaceButton;

    private final int theme = R.style.SimpleNumberPickerTheme;
    private boolean forceHouse = false;
    private Enum populationType;

    @Inject
    DataManager dataManager;

    @Inject
    Game game;

    @Nullable
    private SeekBar seekBar;

    @Nullable
    private TextView advancementText;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null != savedInstanceState) {
            this.assignArguments(savedInstanceState);
        } else if(null != this.getArguments()) {
            this.assignArguments(this.getArguments());
        }

        this.setStyle(1, this.theme);
        this.setCancelable(false);

        ((GameActivity) getActivity()).component().inject(this);
    }
    public void onStart() {
        super.onStart();
        this.onNumberChanged();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_FORCE_HOUSE, this.forceHouse);
        outState.putString(ARG_POPULATION_TYPE, this.populationType.name());
        outState.putString(ARG_SAVED_VALUE, this.numberTextView.getText().toString());
    }

    @NonNull
    private PopulationType getPopulationTypeByProgress(int number, PopulationType.Civilization civ) {
        if (civ == PopulationType.Civilization.OCCIDENTAL) switch (number) {
            case 0:
                return PopulationType.PEASANTS;
            case 1:
                return PopulationType.CITIZENS;
            case 2:
                return PopulationType.PATRICIANS;
            case 3:
                return PopulationType.NOBLEMEN;
        } else if (civ == PopulationType.Civilization.ORIENTAL) switch (number) {
            case 0:
                return PopulationType.NOMADS;
            case 1:
                return PopulationType.ENVOYS;
        }
        throw new IllegalArgumentException("Civilization with that number does not exist");
    }

    private void setAdvancementText(int value, PopulationType.Civilization civilization) {
        String advanceTextStart = getActivity().getString(R.string.advance_to);
        String advanceName = getPopulationTypeByProgress(value, civilization).getString(getContext());
        if (advancementText != null) {
            advancementText.setText(advanceTextStart + " " + advanceName.toLowerCase(), TextView.BufferType.SPANNABLE);

            Spannable sp = (Spannable) advancementText.getText();
            sp.setSpan(new StyleSpan(Typeface.BOLD), advanceTextStart.length(), 1 + advanceTextStart.length() + advanceName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TypedArray attributes = getContext().obtainStyledAttributes(theme, R.styleable.SimpleNumberPicker);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view;
        if (populationType instanceof PopulationType) {
            view = inflater.inflate(R.layout.number_picker, null);
        } else {
            view = inflater.inflate(R.layout.house_number_picker, null);
            seekBar = (SeekBar) view.findViewById(R.id.houseNumberPickerPopLimit);
            advancementText = (TextView) view.findViewById(R.id.houseNumberPickerPopLimitDesc);

            if (seekBar == null || advancementText == null) throw new IllegalStateException("Not all views could be found.");
            if (populationType == PopulationType.Civilization.OCCIDENTAL) {
                seekBar.setMax(3);
                seekBar.setProgress(3);

                setAdvancementText(3, PopulationType.Civilization.OCCIDENTAL);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        setAdvancementText(progress, PopulationType.Civilization.OCCIDENTAL);
                    }
                    @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override public void onStopTrackingTouch(SeekBar seekBar) {}
                });
            } else if (populationType == PopulationType.Civilization.ORIENTAL){
                seekBar.setMax(1);
                seekBar.setProgress(1);

                setAdvancementText(1, PopulationType.Civilization.ORIENTAL);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        setAdvancementText(progress, PopulationType.Civilization.ORIENTAL);
                    }
                    @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override public void onStopTrackingTouch(SeekBar seekBar) {}
                });
            }
        }

        // Init number
        int color = attributes.getColor(R.styleable.SimpleNumberPicker_snpKeyColor,
                ContextCompat.getColor(getContext(), android.R.color.secondary_text_light));
        numberTextView = (TextView) view.findViewById(R.id.tv_hex_number);
        numberTextView.setTextColor(color);
        if (null != savedInstanceState && savedInstanceState.containsKey(ARG_SAVED_VALUE))
            numberTextView.setText(savedInstanceState.getString(ARG_SAVED_VALUE));
        else if (forceHouse)
            numberTextView.setText("⌂");
        // Init backspace
        color = attributes.getColor(R.styleable.SimpleNumberPicker_snpBackspaceColor,
                ContextCompat.getColor(getContext(), android.R.color.secondary_text_light));
        backspaceButton = (ImageButton) view.findViewById(R.id.key_backspace);
        backspaceButton.setImageDrawable(
                ThemeUtil.makeSelector(getContext(), R.drawable.snp_ic_backspace_black_24dp, color));
        backspaceButton.setOnClickListener(v -> {
            if (numberTextView.getText().length() == 1 && forceHouse) return;

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
                    final int number = Integer.parseInt(result);

                    handleDialogFinish(number);
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
        if (!this.forceHouse) {
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
            sign.setVisibility(View.GONE);
        }

        ids.recycle();
        attributes.recycle();

        return dialog;
    }

    private void onNumberChanged() {
        backspaceButton.setEnabled(0 != numberTextView.length());
        if (0 == numberTextView.getText().length()) {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        } else if (numberTextView.getText().toString().startsWith("⌂") && numberTextView.getText().length() == 1) {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        } else {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        }
    }

    private void assignArguments(Bundle args) {
        if (args.containsKey(ARG_FORCE_HOUSE))
            forceHouse = args.getBoolean(ARG_FORCE_HOUSE);
        if (args.containsKey(ARG_POPULATION_TYPE)) {
            try {
                populationType = PopulationType.valueOf(args.getString(ARG_POPULATION_TYPE));
            } catch (IllegalArgumentException e) {
                populationType = PopulationType.Civilization.valueOf(args.getString(ARG_POPULATION_TYPE));
            }
        }
    }

    private static PopulationNumberDialog newInstance(boolean forceHouse, Enum populationType) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_FORCE_HOUSE, forceHouse);
        args.putString(ARG_POPULATION_TYPE, populationType.name());
        PopulationNumberDialog fragment = new PopulationNumberDialog();
        fragment.setArguments(args);
        return fragment;
    }

    private void handleDialogFinish(int number) {
        if (populationType == PopulationType.Civilization.OCCIDENTAL) {
            if (seekBar != null)
                dataManager.changeTotalCountOccidental(game, number, seekBar.getProgress());
        } else if (populationType == PopulationType.Civilization.ORIENTAL) {
            if (seekBar != null)
            dataManager.changeTotalCountOriental(game, number, seekBar.getProgress());
        } else if (populationType instanceof PopulationType) {
            dataManager.setPopulation(game, (PopulationType) populationType, number);
        }
    }

    public static class Builder {
        private boolean forceHouse = false;

        private Enum populationType;


        public PopulationNumberDialog.Builder setForceHouse(boolean forceHouse) {
            this.forceHouse = forceHouse;
            return this;
        }

        public PopulationNumberDialog.Builder setPopulationType(PopulationType populationType) {
            this.populationType = populationType;
            return this;
        }

        public PopulationNumberDialog.Builder setPopulationType(PopulationType.Civilization civilization) {
            this.populationType = civilization;
            return this;
        }

        public PopulationNumberDialog create() {
            return PopulationNumberDialog.newInstance(this.forceHouse, this.populationType);
        }
    }
}
