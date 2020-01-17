package com.donfyy.crowds.dagger;

import com.donfyy.crowds.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector(modules = L1FragmentModule.class)
    abstract MainActivity mainActivityInjector();
}
