package com.example.airplaneticket.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.airplaneticket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegister;
    EditText edEmail,edPassword,edRePassword;
    private FirebaseAuth auth;
    String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        btnRegister = (Button) findViewById(R.id.btnRegister) ;
        edEmail = (EditText)findViewById(R.id.edEmail) ;
        edPassword = (EditText)findViewById(R.id.edPassword) ;
        edRePassword = (EditText)findViewById(R.id.edPasswordRetype) ;
        auth = FirebaseAuth.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = edEmail.getText().toString();
                password = edPassword.getText().toString();
                String rePassword = edRePassword.getText().toString();

                if(email.isEmpty()){
                    edEmail.setError("Email không được để trống");
                    return;
                }
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    edEmail.setError("Email không hợp lệ");
                    return;
                }
                if(password.isEmpty()){
                    edPassword.setError("Mật khẩu không được để trống");
                    return;
                }
                if(rePassword.isEmpty()){
                    edRePassword.setError("Phải nhập lại mật khẩu");
                    return;
                }
                if(password.length() < 8){
                    edPassword.setError("Mật khẩu phải có ít nhất 8 ký tự");
                    return;
                }
                if(!password.equals(rePassword)){
                    edRePassword.setError("Mật khẩu không khớp");
                    return;
                }

                // Check if email exists before creating account
                auth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                boolean emailExists = !task.getResult().getSignInMethods().isEmpty();
                                if (emailExists) {
                                    Toast.makeText(getApplicationContext(), "Email này đã tồn tại", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Email doesn't exist, proceed with registration
                                    createAccount(email, password);
                                }
                            } else {
                                // Error occurred, proceed with registration attempt anyway
                                createAccount(email, password);
                            }
                        });
            }
        });
    }
    private void createAccount(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Register thanh cong", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Đăng ký thất bại: " +
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}