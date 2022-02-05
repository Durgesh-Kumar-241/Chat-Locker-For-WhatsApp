package com.dktechhub.chatlockerforwhatsapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;
import com.codemybrainsout.ratingdialog.RatingDialog;
import com.dktechhub.chatlockerforwhatsapp.AboutActivity;
import com.dktechhub.chatlockerforwhatsapp.R;
import com.dktechhub.chatlockerforwhatsapp.SettingsActivity;
import com.dktechhub.chatlockerforwhatsapp.ads.AppSettings;
import com.dktechhub.chatlockerforwhatsapp.fragment.HomeFragment;
import com.dktechhub.chatlockerforwhatsapp.utils.AccessibilityServiceHandler;
import com.dktechhub.chatlockerforwhatsapp.utils.AppAdmob;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    public static final String HAS_ACCESS_CONSENT = "ACCESS_CONSENT";
    private static final String TAG = "MyAccessibilityService";
    public static Activity activity = null;
    public static LinearLayout adunit = null;
    public static Context context = null;
    public static boolean isFromActivity = false;
    FrameLayout content_frame;
    SharedPreferences.Editor editor;
    FloatingActionButton floatingActionButton;
    SharedPreferences sharedPreferences;
    Button tvPermissions;

    public static boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        final String service = context.getPackageName () + "/" + AccessibilityServiceHandler.class.getCanonicalName ();
        try {
            accessibilityEnabled = Settings.Secure.getInt (context.getApplicationContext ().getContentResolver (), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {  }

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter (':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString (context.getApplicationContext ().getContentResolver (), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                colonSplitter.setString (settingValue);
                while (colonSplitter.hasNext ()) {
                    String accessibilityService = colonSplitter.next ();

                    if (accessibilityService.equalsIgnoreCase (service)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    /* access modifiers changed from: protected */
    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.editor = this.sharedPreferences.edit();
        if (!isAccessibilitySettingsOn(this)) {
            if (!this.sharedPreferences.getBoolean(HAS_ACCESS_CONSENT, false)) {
                showAccessConsent();
            } else {
                showAccessibilitySettings();
            }
        }
        this.tvPermissions = (Button) findViewById(R.id.tvPermissions);
        this.tvPermissions.setOnClickListener(new View.OnClickListener() {
            /* class com.vestaentertainment.whatsappchatloker.MainActivity.View$OnClickListenerC07361 */

            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    Intent intent = new Intent();
                    String packageName = MainActivity.this.getPackageName();
                    if (!((PowerManager) MainActivity.this.getSystemService(Context.POWER_SERVICE)).isIgnoringBatteryOptimizations(packageName)) {
                        intent.setAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
                        intent.setData(Uri.parse("package:" + packageName));
                        MainActivity.this.startActivity(intent);
                    }
                }
            }
        });
        batteryOptButton();
        context = this;
        activity = this;

        adunit = (LinearLayout) findViewById(R.id.unitAd);
        AppAdmob.bannerCall(this, adunit);
        AppAdmob.initInterstitial(this);
        this.content_frame = (FrameLayout) findViewById(R.id.content_frame);
        if (bundle == null) {
            HomeFragment homeFragment = new HomeFragment();
            FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
            beginTransaction.add(R.id.content_frame, homeFragment);
            beginTransaction.detach(homeFragment);
            beginTransaction.attach(homeFragment);
            beginTransaction.commit();
        }
        this.floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        this.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            /* class com.vestaentertainment.whatsappchatloker.MainActivity.View$OnClickListenerC07383 */

            @SuppressLint({"WrongConstant"})
            public void onClick(View view) {
                if (MainActivity.isAccessibilitySettingsOn(MainActivity.this)) {
                    AppAdmob.showInterstitial(MainActivity.context, true, false);
                    try {
                        MainActivity.isFromActivity = true;
                        Intent intent = new Intent();
                        intent.setClassName("com.whatsapp", "com.whatsapp.HomeActivity");
                        intent.addFlags(1342177280);
                        intent.addFlags(67108864);
                        MainActivity.this.startActivityForResult(intent, 9);
                    } catch (Exception unused) {
                        MainActivity mainActivity = MainActivity.this;
                        Toast.makeText(mainActivity, mainActivity.getString(R.string.not_installed), 0).show();
                    }
                } else if (!MainActivity.this.sharedPreferences.getBoolean(MainActivity.HAS_ACCESS_CONSENT, false)) {
                    MainActivity.this.showAccessConsent();
                } else {
                    MainActivity.this.showAccessibilitySettings();
                }
            }
        });
       // new ForceUpdateAsync(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
        new RatingDialog.Builder(this).threshold(4.0f).session(3).title(getString(R.string.rate_dialog_title)).positiveButtonText(getString(R.string.positive_button)).negativeButtonText(getString(R.string.negative_button)).formTitle(getString(R.string.form_title)).formHint(getString(R.string.form_hint)).formSubmitText(getString(R.string.form_submit)).formCancelText(getString(R.string.form_cancel)).onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
            /* class com.vestaentertainment.whatsappchatloker.MainActivity.C07394 */

            @Override // com.codemybrainsout.ratingdialog.RatingDialog.Builder.RatingDialogFormListener
            public void onFormSubmitted(String str) {
            }
        }).build().show();
    }

    /* access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity
    public void onResume() {
        batteryOptButton();
        super.onResume();
    }

    @SuppressLint("ResourceType")
    private void showAccessConsent() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.consent_layout_acs);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        dialog.getWindow().setLayout(-1, -2);
        dialog.setCancelable(false);
        final CheckBox checkBox = dialog.findViewById(R.id.checkBox);
        final TextView textView = dialog.findViewById(R.id.checkBoxWarning);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class com.vestaentertainment.whatsappchatloker.MainActivity.C07405 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    textView.setVisibility(8);
                }
            }
        });
        ((TextView) dialog.findViewById(R.id.privacyButton)).setOnClickListener(new View.OnClickListener() {
            /* class com.vestaentertainment.whatsappchatloker.MainActivity.View$OnClickListenerC07416 */

            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(AppSettings.privacyPolicy)));
            }
        });
        dialog.findViewById(R.id.confirmButton).setOnClickListener(new View.OnClickListener() {
            /* class com.vestaentertainment.whatsappchatloker.MainActivity.View$OnClickListenerC07427 */

            @SuppressLint("WrongConstant")
            public void onClick(View view) {
                if (!checkBox.isChecked()) {
                    textView.setVisibility(0);
                    return;
                }
                MainActivity.this.editor.putBoolean(MainActivity.HAS_ACCESS_CONSENT, true).apply();
                StringBuilder sb = new StringBuilder();
                sb.append("package:");
                sb.append(MainActivity.this.getPackageName());
                Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                intent.addFlags(1342177280);
                MainActivity.this.startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void batteryOptButton() {
        if (Build.VERSION.SDK_INT >= 23) {
            new Intent();
            if (!((PowerManager) getSystemService(Context.POWER_SERVICE)).isIgnoringBatteryOptimizations(getPackageName())) {
                this.tvPermissions.setVisibility(0);
            } else {
                this.tvPermissions.setVisibility(8);
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // androidx.activity.ComponentActivity
    public void onBackPressed() {
        AppAdmob.showInterstitial(context, true, false);
        new AlertDialog.Builder(this).setMessage("Are you sure you want to exit?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.finish();
            }
        }).setNegativeButton("No", null).show();
    }

    public void showAccessibilitySettings() {
        new AlertDialog.Builder(this).setTitle(getString(R.string.app_name)).setMessage(getString(R.string.accessibility_service_des)).setIcon(R.mipmap.ic_launcher).setCancelable(false).setPositiveButton(getResources().getString(R.string.enable), new DialogInterface.OnClickListener() {
            /* class com.vestaentertainment.whatsappchatloker.MainActivity.DialogInterface$OnClickListenerC07449 */

            @SuppressLint("WrongConstant")
            public void onClick(DialogInterface dialogInterface, int i) {
                StringBuilder sb = new StringBuilder();
                sb.append("package:");
                sb.append(MainActivity.this.getPackageName());
                Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                intent.addFlags(1342177280);
                MainActivity.this.startActivity(intent);
            }
        }).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint({"WrongConstant"})
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.main_about /*{ENCODED_INT: 2131230903}*/:
                startActivity(new Intent(this, AboutActivity.class).addFlags(65536));
                AppAdmob.showInterstitial(context, true, false);
                break;
            case R.id.main_setting /*{ENCODED_INT: 2131230904}*/:
                startActivity(new Intent(this, SettingsActivity.class).addFlags(65536));
                AppAdmob.showInterstitial(context, true, false);
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}