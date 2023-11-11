package com.muhsanapps.ecommerce.activities;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.muhsanapps.ecommerce.R;
//import com.google.firebase.firestore.DocumentReference;


//import java.util.HashMap;
//import java.util.Map;




public class SignInFragment extends Fragment {


    public SignInFragment() {
        // Required empty public constructor
    }
    private TextView dontHaveAnAccount;
    private FrameLayout parentFrameLayout;
    private EditText email, password;
    private Button signInBtn;
    private ProgressBar progressBar;
    private ImageButton closeBtn;

    private FirebaseAuth firebaseAuth;
    private String emailPattern="[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_sign_in, container, false);
        dontHaveAnAccount = view.findViewById(R.id.tv_dont_have_an_account);
        parentFrameLayout= getActivity().findViewById(R.id.register_framelayout);
        email = view.findViewById(R.id.sign_in_email);
        password = view.findViewById(R.id.sign_in_password);
        signInBtn = view.findViewById(R.id.sign_in_btn);
        closeBtn = view.findViewById(R.id.sign_in_closeBtn);
        progressBar = view.findViewById(R.id.sign_in_progressbar);
        firebaseAuth = FirebaseAuth.getInstance();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dontHaveAnAccount.setOnClickListener(v -> setFragment(new SignUpFragment()));
        email.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        signInBtn.setOnClickListener(v -> checkEmailAndPassword());
        closeBtn.setOnClickListener(v -> mainIntent());
    }
        private TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs() {
        boolean isEmailEmpty = TextUtils.isEmpty(email.getText());
        boolean isPasswordEmpty = TextUtils.isEmpty(password.getText());
        boolean isPasswordValid = password.length() >= 8;

        boolean isEnabled = !isEmailEmpty && !isPasswordEmpty && isPasswordValid;
        int textColor = isEnabled ? Color.rgb(255, 255, 255) : Color.argb(50, 255, 255, 255);

        signInBtn.setEnabled(isEnabled);
        signInBtn.setTextColor(textColor);
    }

    private void checkEmailAndPassword() {
        if (email.getText().toString().matches(emailPattern)) {
            if(password.getText().length()>=8)
            {
                progressBar.setVisibility(View.VISIBLE);
                signInBtn.setEnabled(false);
                signInBtn.setTextColor(Color.argb(50, 255, 255, 255));

                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    mainIntent();
                                }
                                else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signInBtn.setEnabled(true);
                                    signInBtn.setTextColor(Color.rgb(255, 255, 255));
                                    String error=task.getException().getMessage();
                                    Toast.makeText(getActivity(), error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(getActivity(), "Incorrect email or password!", Toast.LENGTH_SHORT).show(); // Removed extra parentheses
            }
        } else {
            Toast.makeText(getActivity(), "Incorrect email or password!", Toast.LENGTH_SHORT).show(); // Removed extra parentheses
        }
    }
    private  void mainIntent(){
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainIntent);
        getActivity().finish();
    }
}