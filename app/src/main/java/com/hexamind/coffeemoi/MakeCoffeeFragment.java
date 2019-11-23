package com.hexamind.coffeemoi;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

public class MakeCoffeeFragment extends Fragment {
    View root;
    Spinner size, coffeeType;
    RadioGroup expressoShot;
    RadioButton yes, no;
    AppCompatButton makeCoffee;
    ProgressBar progressBar;
    RelativeLayout layout;
    String[] coffeeSizes = getResources().getStringArray(R.array.coffee_size);
    String[] coffeeTypes = getResources().getStringArray(R.array.coffee_types);
    DatabaseHelper helper;
    boolean agreed = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_make_coffee, container, false);

        size = root.findViewById(R.id.size);
        coffeeType = root.findViewById(R.id.coffeeType);
        expressoShot = root.findViewById(R.id.expressoShot);
        yes = root.findViewById(R.id.yes);
        no = root.findViewById(R.id.no);
        makeCoffee = root.findViewById(R.id.makeCoffee);
        progressBar = root.findViewById(R.id.progressBar);
        layout = root.findViewById(R.id.layout);
        makeCoffee.setEnabled(false);
        helper = new DatabaseHelper(root.getContext(), "", null, 0);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(root.getContext(), android.R.layout.simple_spinner_item, coffeeSizes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        size.setAdapter(adapter);
        coffeeType.setAdapter(adapter);
        size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    coffeeType.setEnabled(false);
                    yes.setEnabled(false);
                    no.setEnabled(false);
                } else {
                    coffeeType.setEnabled(true);
                    yes.setEnabled(true);
                    no.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        coffeeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    size.setEnabled(false);
                    yes.setEnabled(false);
                    no.setEnabled(false);
                } else {
                    size.setEnabled(true);
                    yes.setEnabled(true);
                    no.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        expressoShot.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (id == R.id.yes) {
                    if (coffeeType.getSelectedItemPosition() != 0 && size.getSelectedItemPosition() != 0)
                        makeCoffee.setEnabled(true);
                    else
                        makeCoffee.setEnabled(false);
                } else {
                    if (coffeeType.getSelectedItemPosition() != 0 && size.getSelectedItemPosition() != 0)
                        makeCoffee.setEnabled(true);
                    else
                        makeCoffee.setEnabled(false);
                }
            }
        });
        makeCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewAgreement()) {
                    int id = expressoShot.getCheckedRadioButtonId();
                    boolean expresso = id == R.id.yes;
                    String username = getActivity().getSharedPreferences(Constants.USER_SHARED_PREFS, Context.MODE_PRIVATE).getString(Constants.USERS_INTENT, "");
                    String selectedSize = coffeeSizes[size.getSelectedItemPosition()];
                    String selectedType = coffeeTypes[coffeeType.getSelectedItemPosition()];

                    Orders orders = new Orders(username, selectedSize, selectedType, expresso);
                    progressBar.setVisibility(View.VISIBLE);
                    boolean orderAdded = helper.makeCoffee(orders);

                    if (orderAdded) {
                        Snackbar.make(layout, getString(R.string.coffee_order_placed_message_string), Snackbar.LENGTH_SHORT).show();
                        ProfileFragment fragment = new ProfileFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout, fragment)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        Snackbar.make(layout, getString(R.string.order_creation_problem), Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(layout, getString(R.string.agree_to_terms_string), Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    private boolean viewAgreement() {
        AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.layout_coffee_dialog, null);
        builder.setView(dialogView);

        AppCompatButton agree = dialogView.findViewById(R.id.agree);
        AppCompatButton disagree = dialogView.findViewById(R.id.disagree);
        ImageView close = dialogView.findViewById(R.id.close);

        final AlertDialog dialog = builder.create();
        dialog.show();

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agreed = true;
                dialog.dismiss();
            }
        });
        disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agreed = false;
                dialog.dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agreed = false;
                dialog.dismiss();
            }
        });

        return agreed;
    }
}
