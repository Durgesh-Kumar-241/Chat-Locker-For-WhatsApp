package com.dktechhub.chatlockerforwhatsapp.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;

public class AppAdmob {
    public static int mCount;
    public static int mCount2;
    //public static InterstitialAd mInterstitialAd;

    @SuppressLint({"MissingPermission"})
    public static void bannerCall(Activity activity, final LinearLayout linearLayout) {
        linearLayout.setVisibility(0);
        //AdView adView = new AdView(activity);
        //adView.setAdUnitId(AppSettings.banner);
        //adView.setAdSize(AdSize.SMART_BANNER);
        //adView.loadAd(AppConsent.getAdRequest(activity));
        //linearLayout.setVisibility(8);
        //linearLayout.addView(adView);
        /*adView.setAdListener(new AdListener() {


            @Override // com.google.android.gms.ads.AdListener
            public void onAdLoaded() {
                super.onAdLoaded();
                linearLayout.setVisibility(0);
            }
        });
        */
    }

    public static void initInterstitial(final Activity activity) {
        //mInterstitialAd = new InterstitialAd(activity);
        //mInterstitialAd.setAdUnitId(AppSettings.interstitial);
        /*mInterstitialAd.setAdListener(new AdListener() {


            @Override // com.google.android.gms.ads.AdListener
            public void onAdClosed() {
                AppAdmob.requestNewInterstitial(activity);
            }
        });
        requestNewInterstitial(activity);
        */
    }

    @SuppressLint({"MissingPermission"})
    public static void requestNewInterstitial(Context context) {
        //mInterstitialAd.loadAd(AppConsent.getAdRequest(context));
    }

    public static void showInterstitial(Context context, boolean z, boolean z2) {
        /*
        if (z) {
            if (z2) {
                mCount++;
                if (AppSettings.nbShowInterstitial != mCount) {
                    return;
                }
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mCount = 0;
                    return;
                }
                mCount--;
                return;
            }
            mCount2++;
            if (AppSettings.nbShowInterstitial2 != mCount2) {
                return;
            }
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
                mCount2 = 0;
                return;
            }
            mCount2--;
        } else if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

         */
    }
}