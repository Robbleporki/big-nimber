package com.example.financemanager.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.financemanager.R;
import com.example.financemanager.models.Transaction;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    
    private List<Transaction> transactionList;
    private SimpleDateFormat dateFormat;
    
    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
        this.dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        
        holder.tvTitle.setText(transaction.getTitle());
        holder.tvCategory.setText(transaction.getCategory());
        holder.tvDescription.setText(transaction.getDescription());
        holder.tvDate.setText(dateFormat.format(transaction.getDate()));
        
        // نمایش مبلغ با رنگ متفاوت برای درآمد و هزینه
        String amountText = String.format(Locale.getDefault(), "%,.0f تومان", transaction.getAmount());
        holder.tvAmount.setText(amountText);
        
        if (transaction.getType().equals("income")) {
            holder.tvAmount.setTextColor(Color.GREEN);
        } else {
            holder.tvAmount.setTextColor(Color.RED);
        }
    }
    
    @Override
    public int getItemCount() {
        return transactionList.size();
    }
    
    public void updateList(List<Transaction> newList) {
        transactionList = newList;
        notifyDataSetChanged();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvDescription, tvDate, tvAmount;
        
        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }
}