package com.hexamind.coffeemoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText username, password;
    private ConstraintLayout constraintLayout;
    private AppCompatButton login;
    private FirebaseDatabase db;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        constraintLayout = findViewById(R.id.constraintLayout);
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("Users");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateCredentials(username.getText().toString(), password.getText().toString());
            }
        });
    }

    private void validateCredentials(String username, String password) {
        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches() && password.length() < 6) {
            if (Patterns.EMAIL_ADDRESS.matcher(username).matches())
                this.username.setError(getString(R.string.incorrect_email));
            if (password.length() < 6) {
                this.password.setError(getString(R.string.incorrect_password));
            }
        } else
            checkUserCredentials(username, password);
    }

    private void checkUserCredentials(final String username, final String password) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Users user = ds.getValue(Users.class);

                        if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                            Intent intent = new Intent(LoginActivity.this, PostLoginActivity.class);
                            intent.putExtra(Constants.USERS_INTENT, username);
                            startActivity(intent);
                        } else {
                            Snackbar.make(constraintLayout, "Incorrect username and password", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Snackbar.make(constraintLayout, getString(R.string.no_users), Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snackbar.make(constraintLayout, "There was some problem: " + databaseError.toException().getMessage(), Snackbar.LENGTH_SHORT)
                        .show();
                Log.e(LoginActivity.class.getName(), databaseError.toException().getMessage());
            }
        });
    }
}
