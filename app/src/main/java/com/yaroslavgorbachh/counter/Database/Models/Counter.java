package com.yaroslavgorbachh.counter.Database.Models;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.yaroslavgorbachh.counter.Database.Repo;
import com.yaroslavgorbachh.counter.R;

import java.security.PublicKey;
import java.util.Date;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableEmitter;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.CompletableOnSubscribe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Entity(tableName = "counter_table")
public class Counter {
    public final static long MAX_VALUE = 999999999999999999L;
    public final static long MIN_VALUE = -999999999999999999L;

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String title;
    public long value;
    public long maxValue;
    public long minValue;
    public long step;
    public String grope;
    @ColumnInfo(name = "createData")
    public Date createDate;
    @ColumnInfo(name = "createDataSort")
    public Date createDateSort;
    @ColumnInfo(name = "lastResetData")
    public Date lastResetDate;
    @ColumnInfo(defaultValue = "0")
    public long lastResetValue;
    @ColumnInfo(defaultValue = "0")
    public long counterMaxValue;
    @ColumnInfo(defaultValue = "0")
    public long counterMinValue;

    public Counter(String title,
                   long value,
                   long maxValue,
                   long minValue,
                   long step,
                   String grope,
                   Date createDate,
                   Date createDateSort,
                   Date lastResetDate,
                   long lastResetValue,
                   long counterMaxValue,
                   long counterMinValue) {
        this.title = title;
        this.value = value;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.step = step;
        this.grope = grope;
        this.createDate = createDate;
        this.createDateSort = createDateSort;
        this.lastResetDate = lastResetDate;
        this.lastResetValue = lastResetValue;
        this.counterMaxValue = counterMaxValue;
        this.counterMinValue = counterMinValue;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void inc(Context context, Resources resources, Repo repo){
            long maxValue;
            long incOn;
            long value = this.value;
            maxValue = this.maxValue;
            incOn = this.step;
            value += incOn;

            if (value > maxValue) {
                Toast.makeText(context, resources.getString(R.string.thisIsMaximum), Toast.LENGTH_SHORT).show();
                this.value = maxValue;
            } else {
                this.value = Math.max(this.minValue, value);
            }

            if (this.value == this.minValue){
                Toast.makeText(context, resources.getString(R.string.thisIsMinimum), Toast.LENGTH_SHORT).show();
            }

            if (this.value > this.counterMaxValue) this.counterMaxValue = this.value;

            if (this.value < this.counterMinValue) this.counterMaxValue = this.value;
         repo.updateCounter(this);
    }

    public void dec(Context context, Resources resources, Repo repo){
        long minValue;
        long decOn;
        minValue = this.minValue;
        long value = this.value;
        decOn = this.step;
        value -= decOn;

        if (value < minValue){
            Toast.makeText(context, resources.getString(R.string.thisIsMinimum), Toast.LENGTH_SHORT).show();
            this.value = minValue;
        }else {
            this.value = Math.min(this.maxValue, value);
        }
        if (this.value == this.maxValue){
            Toast.makeText(context, resources.getString(R.string.thisIsMaximum), Toast.LENGTH_SHORT).show();
        }

        if (this.value > this.counterMaxValue) this.counterMaxValue = this.value;

        if (this.value < this.counterMinValue) this.counterMinValue = this.value;
        repo.updateCounter(this);
    }

    public void reset(Repo repo){
        lastResetValue = value;
        lastResetDate = new Date();
        if (minValue > 0){
            value = minValue;
        }else {
            value = 0;
        }
        repo.updateCounter(this);
    }
}
