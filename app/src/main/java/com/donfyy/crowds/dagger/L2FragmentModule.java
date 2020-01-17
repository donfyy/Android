package com.donfyy.crowds.dagger;

import com.donfyy.crowds.L2Fragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class L2FragmentModule {

    @ContributesAndroidInjector
    abstract L2Fragment L2Fragment();
}
