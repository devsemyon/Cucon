package com.semyon.cucon.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.semyon.cucon.InstantAutoComplete;
import com.semyon.cucon.R;
import com.semyon.cucon.SimpleTokenizer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import static com.semyon.cucon.HttpRequestsKt.requestCryptoRates;
import static com.semyon.cucon.HttpRequestsKt.requestCurrencies;

public class CryptoFragment extends Fragment {

    private Context context;
    private InstantAutoComplete currency1, currency2;
    private EditText rate1, rate2;
    private Float rate; // переменная для хранения курса
    private TextView mode;
    private JSONObject cryptoRates;
    private ArrayList<String> cryptoCurrencies = new ArrayList<>();
    private ArrayList<String> cryptoCurrencies2 = new ArrayList<>();

    public CryptoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crypto, container, false);

        currency1 = view.findViewById(R.id.currency1);
        currency2 = view.findViewById(R.id.currency2);

        mode = view.findViewById(R.id.mode);

        rate1 = view.findViewById(R.id.rate1);
        rate2 = view.findViewById(R.id.rate2);

        cryptoCurrencies.add("BTC");
        cryptoCurrencies.add("ETH");
        cryptoCurrencies.add("LTC");
        cryptoCurrencies.add("DASH");
        cryptoCurrencies.add("EOS");
        cryptoCurrencies.add("WAVES");
        cryptoCurrencies.add("ZEC");

        cryptoCurrencies2.add("USD");
        cryptoCurrencies2.add("RUB");
        cryptoCurrencies2.add("EUR");

        cryptoRates = requestCryptoRates();
        //addAddapters(cryptoCurrencies);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, cryptoCurrencies);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, cryptoCurrencies2);

        currency1.setAdapter(adapter1);
        currency1.setTokenizer(new SimpleTokenizer());
        currency2.setAdapter(adapter2);
        currency2.setTokenizer(new SimpleTokenizer());

        currency1.addTextChangedListener(new GenericTextWatcher(currency1));
        currency2.addTextChangedListener(new GenericTextWatcher(currency2));

        currency1.setOnTouchListener(showCurrencies);
        currency2.setOnTouchListener(showCurrencies);

        rate1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                count(rate1.getId());
                return false;
            }
        });
        rate2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                count(rate2.getId());
                return false;
            }
        });

        ImageButton switchCurrencies = view.findViewById(R.id.switchCurrencies);
        switchCurrencies.setOnClickListener(switchCurrenciesClick);

        return view;
    }

    // показываем валюты при нажатие на поля
    View.OnTouchListener showCurrencies = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (v.getId()) {
                case (R.id.currency1):
                    currency1.showDropDown();
                    break;
                case (R.id.currency2):
                    currency2.showDropDown();
                    break;
            }
            return false;
        }
    };

    private void showDialogNoOffline() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context, R.style.Theme_MaterialComponents_Bridge);
        builder.setTitle("Нет интернета!")
                .setMessage("Чтобы использовать приложение в оффлайне вам необходимо зайти в него хотя бы один раз с интернетом.")
                .setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .setIcon(android.R.drawable.stat_notify_error)
                .setCancelable(false)
                .show();
    }

    // меняем валюты местами
    View.OnClickListener switchCurrenciesClick = new View.OnClickListener() {
        public void onClick(View v) {
            if (currency1.getText().length() >= 3 && currency2.getText().length() >= 3) {
                try {
                    String c1 = currency1.getText().toString();
                    String c2 = currency2.getText().toString();
                    String r1 = rate1.getText().toString();
                    String r2 = rate2.getText().toString();

                    currency1.setText(c2);
                    currency2.setText(c1);
                    rate1.setText(r2);
                    rate2.setText(r1);

                } catch (Exception ignored) {
                }
            }
        }
    };

    // метод пересчитавания курса/значения
    private void count(int id) {
        if (id == rate2.getId()) {
            if (rate2.getText().length() != 0 && currency1.getText().length() != 0 && currency2.getText().length() != 0) {
                try {
                    Float.valueOf(rate2.getText().toString());
                    rate1.setText(String.valueOf(Float.valueOf(rate2.getText().toString()) / rate));
                } catch (Exception ignored) {
                }
            }
        } else if (id == currency1.getId() || id == currency2.getId()) {

            if (currency1.getText().length() >= 3 && currency2.getText().length() >= 3) {

                String crypto1 = currency1.getText().toString().toUpperCase();
                String crypto2 = currency2.getText().toString().toUpperCase();

                if (isInternet()) {

                    try {
                        rate = (float) cryptoRates.getJSONObject(crypto1).getDouble(crypto2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //editor.putFloat(pair, rate);
                    //editor.apply();
                    mode.setText(null);
                } else {
                    mode.setText("Оффлайн режим недоступен у криптовалют");
                    //rate = sharedPref.getFloat(pair, 0);
                    //if (rate == 0) {
                    //    Toast.makeText(getActivity().getApplicationContext(), "Эта валютная пара не загружена в оффлайн!", Toast.LENGTH_LONG).show();
                    //}
                }

                if (rate != null) {
                    rate2.setText(String.valueOf(rate * Float.valueOf(rate1.getText().toString())));
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Произошла ошибка, возможно превышен лимит!", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            if (rate1.getText().length() != 0 && currency1.getText().length() != 0 && currency2.getText().length() != 0) {
                try {
                    Float.valueOf(rate1.getText().toString());
                    rate2.setText(String.valueOf(rate * Float.valueOf(rate1.getText().toString())));
                } catch (Exception ignored) {
                }
            }
        }
    }

    // проверка интернета на устройстве
    private boolean isInternet() {
        Iterator<String> t = requestCurrencies();
        return t != null;
    }

    class GenericTextWatcher implements TextWatcher {

        private View view;

        GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            switch (view.getId()) {
                case R.id.currency1:
                    if (currency1.getText().length() >= 3) {
                        if (cryptoCurrencies.contains(currency1.getText().toString().toUpperCase())) {
                            count(view.getId());
                        } else {
                            Toast.makeText(context, "Такой валюты не существует ;(", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case R.id.currency2:
                    if (currency2.getText().length() >= 3) {
                        if (cryptoCurrencies2.contains(currency2.getText().toString().toUpperCase())) {
                            count(view.getId());
                        } else {
                            Toast.makeText(context, "Такой валюты не существует ;(", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }

        public void afterTextChanged(Editable editable) {
        }
    }
}
