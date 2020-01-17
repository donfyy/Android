package com.donfyy.crowds.dagger;

import com.donfyy.crowds.L1Fragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class L1FragmentModule {

    @ContributesAndroidInjector(modules = {L2FragmentModule.class})
    abstract L1Fragment L1Fragment();
}
