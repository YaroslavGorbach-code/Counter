package com.yaroslavgorbachh.counter.di;

import com.yaroslavgorbachh.counter.data.Repo;

public interface RepoProvider {
    Repo provideRepo();
}
