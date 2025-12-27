package com.example.financemanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.financemanager.R;
import com.example.financemanager.utils.PrefManager;

public class LoginActivity extends AppCompatActivity {
    
    private EditText etUsername, etPassword;
    private CheckBox cbRememberMe;
    private PrefManager prefManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        prefManager = new PrefManager(this);
        
        // بررسی اگر کاربر قبلا لاگین کرده
        if (prefManager.isLoggedIn() && prefManager.getRememberMe()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        Button btnLogin = findViewById(R.id.btnLogin);
        
        btnLogin.setOnClickListener(v -> loginUser());
        
        findViewById(R.id.tvRegister).setOnClickListener(v -> {
            // رفتن به صفحه ثبت نام
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
    
    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "لطفا تمام فیلدها را پر کنید", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // احراز هویت ساده (در نسخه واقعی باید با سرور چک شود)
        if (username.equals("admin") && password.equals("1234")) {
            prefManager.setLogin(true, username, "user@example.com");
            prefManager.setRememberMe(cbRememberMe.isChecked());
            
            Toast.makeText(this, "ورود موفق", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "نام کاربری یا رمز عبور اشتباه است", Toast.LENGTH_SHORT).show();
        }
    }
}
