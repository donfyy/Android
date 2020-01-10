package com.donfyy.crowds;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;

public class MainActivity extends FragmentActivity implements HasFragmentInjector {
    private static final String MY_APP_TAG = "MY_APP_TAG";
    //    private static final String MY_APP_TAG = MainActivity.class.getSimpleName();
    @Inject
    AppComponentA mAppComponentA;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(this.getClass().getSimpleName(), "injected!" + mAppComponentA);

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.e(MY_APP_TAG, "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e(MY_APP_TAG, "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e(MY_APP_TAG, "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e(MY_APP_TAG, "The user hasn't associated " +
                        "any biometric credentials with their account.");
                break;
        }
/*
        从系统29开始支持脸部识别
        应用如果要使用脸部识别，第一要升级目标版本29或以上，第二使用androidx中的BiometricPrompt（androidx.biometric.BiometricPrompt）中的Api
        应用无法选择使用的是脸部识别或指纹识别，由系统决定使用哪一种，如果都有的话
        android.hardware.biometrics.BiometricPrompt从28开始引入，由系统提供指纹识别的对话框，但是有兼容问题
                这也是为啥velo自定义该对话框
        但是从29开始该对话框无兼容问题，因为这个对话框是有androidx提供打包到app里的
        https://source.android.com/security/biometric/face-authentication
        */
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
//                .setNegativeButtonText("Use account password")
                .setDeviceCredentialAllowed(true)
                .build();

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        TextView biometricLoginButton = findViewById(R.id.hello_world);
        biometricLoginButton.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });
    }


    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    @Override
    public AndroidInjector<Fragment> fragmentInjector() {
        return fragmentInjector;
    }
}
