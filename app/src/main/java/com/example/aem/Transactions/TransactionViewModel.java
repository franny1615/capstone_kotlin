package com.example.aem.Transactions;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.aem.AppDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TransactionViewModel extends AndroidViewModel {
    private final TransactionsDao transactionsDao;
    private final ExecutorService executorService;

    public TransactionViewModel(@NonNull @NotNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.Companion.getInstance(application);
        assert db != null;
        this.transactionsDao = db.transactionsDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void insertTransaction(TransactionEntity t) {
        executorService.execute(()->transactionsDao.insertTransaction(t));
    }

    public void deleteAllTransactionsByItemId(String item_id){
        executorService.execute(()->transactionsDao.deleteTransactionsByItemId(item_id));
    }

    public List<TransactionEntity> getAllTransactionsByItemId(String item_id){
        Future<List<TransactionEntity>> f = executorService.submit(()->transactionsDao.loadAllTransactionsByItemId(item_id));
        List<TransactionEntity> b = null;
        try {
            b = f.get(200, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    public TransactionEntity getbyTransactionId(String trans_id){
        Future<TransactionEntity> f = executorService.submit(()->transactionsDao.getbyTransactionId(trans_id));
        TransactionEntity b = null;
        try {
            b = f.get(200, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
}
