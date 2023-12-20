package com.example.play_companion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    FirebaseUser currentUser;
    private User user;
    TextView email;
    TextView firstName;
    TextView lastName;
    TextView password;
    TextView nPassword;
    TextView cPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        email = findViewById(R.id.editTextTextEmail);
        firstName = findViewById(R.id.editTextTextFName);
        lastName = findViewById(R.id.editTextTextLName);
        password = findViewById(R.id.editTextTextPassword);
        nPassword = findViewById(R.id.editTextTextNPassword);
        cPassword = findViewById(R.id.editTextTextCPassword);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();

        db.collection("users")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document: task.getResult()) {
                                user = document.toObject(User.class);
                                user.setDocId(document.getId());

                                email.setText(user.getEmail());
                                firstName.setText(user.getFirst_name());
                                lastName.setText(user.getLast_name());
                            }
                        }
                    }
                });
    }

    public void submitLogout(View v) {
        mAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void submitEditForm(View v) {
        String newFirstName = firstName.getText().toString();
        String newLastName = lastName.getText().toString();
        String oldPassword = password.getText().toString();
        String newPassword = nPassword.getText().toString();
        String confirmPassord = cPassword.getText().toString();

        Map<String,Object> userUpdate = new HashMap<>();
        userUpdate.put("first_name", newFirstName);
        userUpdate.put("last_name", newLastName);

        db.collection("users")
                .document(user.getDocId())
                .update(userUpdate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (!oldPassword.isEmpty() || !newPassword.isEmpty() || !confirmPassord.isEmpty()) {
                            if (!oldPassword.isEmpty() && !newPassword.isEmpty() && !confirmPassord.isEmpty()) {
                                if (newPassword.equals(confirmPassord)) {
                                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);

                                    currentUser.reauthenticate(credential)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        currentUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(AccountActivity.this, "Fields updated successfully",
                                                                            Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(AccountActivity.this, "Error password not updated",
                                                                            Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        Toast.makeText(AccountActivity.this, "Incorrect password",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(AccountActivity.this, "New password must match confirmation",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AccountActivity.this, "All password fields must be complete",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AccountActivity.this, "Fields updated successfully",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AccountActivity.this, "Update failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void submitDeleteForm(View v) {
        db.collection("users")
                .document(user.getDocId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AccountActivity.this, "User deleted successfully",
                                Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AccountActivity.this, "Delete failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}