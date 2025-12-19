package com.example.financemanager.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.financemanager.R;
import com.example.financemanager.database.DatabaseHelper;
import com.example.financemanager.models.Transaction;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTransactionActivity extends AppCompatActivity {
    
    private EditText etTitle, etAmount, etCategory, etDescription;
    private RadioGroup rgType;
    private Button btnDate, btnSave;
    private Calendar selectedDate;
    private DatabaseHelper dbHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        
        dbHelper = new DatabaseHelper(this);
        selectedDate = Calendar.getInstance();
        
        initViews();
        setupListeners();
    }
    
    private void initViews() {
        rgType = findViewById(R.id.rgType);
        etTitle = findViewById(R.id.etTitle);
        etAmount = findViewById(R.id.etAmount);
        etCategory = findViewById(R.id.etCategory);
        etDescription = findViewById(R.id.etDescription);
        btnDate = findViewById(R.id.btnDate);
        btnSave = findViewById(R.id.btnSave);
        
        // تنظیم تاریخ امروز به صورت پیش‌فرض
        updateDateButton();
    }
    
    private void setupListeners() {
        btnDate.setOnClickListener(v -> showDatePicker());
        
        btnSave.setOnClickListener(v -> saveTransaction());
    }
    
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(Calendar.YEAR, year);
                    selectedDate.set(Calendar.MONTH, month);
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateButton();
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    
    private void updateDateButton() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        btnDate.setText(sdf.format(selectedDate.getTime()));
    }
    
    private void saveTransaction() {
        String title = etTitle.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        
        if (title.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "لطفا عنوان و مبلغ را وارد کنید", Toast.LENGTH_SHORT).show();
            return;
        }
        
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "مبلغ نامعتبر است", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // تعیین نوع تراکنش
        String type = "income";
        int selectedId = rgType.getCheckedRadioButtonId();
        if (selectedId == R.id.rbExpense) {
            type = "expense";
        }
        
        // ایجاد تراکنش جدید
        Transaction transaction = new Transaction(
                title,
                description,
                amount,
                type,
                selectedDate.getTime(),
                category
        );
        
        // ذخیره در پایگاه داده
        long id = dbHelper.addTransaction(transaction);
        
        if (id > 0) {
            Toast.makeText(this, "تراکنش با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "خطا در ثبت تراکنش", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}