package com.example.financemanager.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.financemanager.R;
import com.example.financemanager.adapters.TransactionAdapter;
import com.example.financemanager.database.DatabaseHelper;
import com.example.financemanager.models.Transaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class TransactionsActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private List<Transaction> transactionList;
    private DatabaseHelper dbHelper;
    private EditText etSearch;
    private Spinner spinnerFilter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        
        dbHelper = new DatabaseHelper(this);
        
        initViews();
        setupRecyclerView();
        setupFilterSpinner();
        setupSearch();
        loadTransactions("all");
        
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(TransactionsActivity.this, AddTransactionActivity.class));
        });
    }
    
    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewTransactions);
        etSearch = findViewById(R.id.etSearch);
        spinnerFilter = findViewById(R.id.spinnerFilter);
    }
    
    private void setupRecyclerView() {
        transactionList = new ArrayList<>();
        adapter = new TransactionAdapter(transactionList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    
    private void setupFilterSpinner() {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.filter_options,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(spinnerAdapter);
        
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                String filterType = "all";
                
                switch (selected) {
                    case "همه":
                        filterType = "all";
                        break;
                    case "درآمدها":
                        filterType = "income";
                        break;
                    case "هزینه‌ها":
                        filterType = "expense";
                        break;
                }
                loadTransactions(filterType);
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    
    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchTransactions(s.toString());
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    
    private void loadTransactions(String type) {
        List<Transaction> transactions;
        
        if (type.equals("all")) {
            transactions = dbHelper.getAllTransactions();
        } else {
            transactions = dbHelper.getTransactionsByType(type);
        }
        
        transactionList.clear();
        transactionList.addAll(transactions);
        adapter.notifyDataSetChanged();
    }
    
    private void searchTransactions(String query) {
        if (query.isEmpty()) {
            String selected = spinnerFilter.getSelectedItem().toString();
            String filterType = "all";
            
            switch (selected) {
                case "همه":
                    filterType = "
