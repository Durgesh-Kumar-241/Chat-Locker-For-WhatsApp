package com.dktechhub.applock.utils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import com.beautycoder.pflockscreen.PFFLockScreenConfiguration;
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;
import com.beautycoder.pflockscreen.security.PFResult;
import com.beautycoder.pflockscreen.viewmodels.PFPinCodeViewModel;
import com.dktechhub.applock.MainActivity;
import com.dktechhub.applock.R;

public class LockActivity extends AppCompatActivity {
    boolean closeChat = false;
    String fromService = "";
    private final PFLockScreenFragment.OnPFLockScreenCodeCreateListener mCodeCreateListener = new PFLockScreenFragment.OnPFLockScreenCodeCreateListener() {

        @Override
        public void onCodeCreated(String str) {
            Toast.makeText(LockActivity.this, "Code created", Toast.LENGTH_LONG).show();
            SharedPrefUtils.saveData(LockActivity.this, SharedPrefUtils.keylockPasscode, str);
            LockActivity.this.showMainFragment();
        }

        @Override // com.beautycoder.pflockscreen.fragments.PFLockScreenFragment.OnPFLockScreenCodeCreateListener
        public void onNewCodeValidationFailed() {
            Toast.makeText(LockActivity.this, "Code validation error", Toast.LENGTH_LONG).show();
        }
    };
    private final PFLockScreenFragment.OnPFLockScreenLoginListener mLoginListener = new PFLockScreenFragment.OnPFLockScreenLoginListener() {
        /* class com.vestaentertainment.whatsappchatloker.LockActivity.C07332 */

        @Override // com.beautycoder.pflockscreen.fragments.PFLockScreenFragment.OnPFLockScreenLoginListener
        public void onCodeInputSuccessful() {
            LockActivity lockActivity = LockActivity.this;
            lockActivity.closeChat = true;
            Toast.makeText(lockActivity, "Unlock successful", Toast.LENGTH_SHORT).show();
            LockActivity.this.showMainFragment();
        }

        @Override // com.beautycoder.pflockscreen.fragments.PFLockScreenFragment.OnPFLockScreenLoginListener
        public void onFingerprintSuccessful() {
            LockActivity lockActivity = LockActivity.this;
            lockActivity.closeChat = true;
            Toast.makeText(lockActivity, "Fingerprint Login successful", Toast.LENGTH_SHORT).show();
            LockActivity.this.showMainFragment();
        }

        @Override // com.beautycoder.pflockscreen.fragments.PFLockScreenFragment.OnPFLockScreenLoginListener
        public void onPinLoginFailed() {
            LockActivity lockActivity = LockActivity.this;
            lockActivity.closeChat = false;
            Toast.makeText(lockActivity, "Pin failed", Toast.LENGTH_SHORT).show();
        }

        @Override // com.beautycoder.pflockscreen.fragments.PFLockScreenFragment.OnPFLockScreenLoginListener
        public void onFingerprintLoginFailed() {
            LockActivity lockActivity = LockActivity.this;
            lockActivity.closeChat = false;
            Toast.makeText(lockActivity, "Fingerprint failed", Toast.LENGTH_SHORT).show();
        }
    };

    @Override // androidx.activity.ComponentActivity
    public void onBackPressed() {
    }

    /* access modifiers changed from: protected */
    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_lock);
        if (getIntent() != null) {
            this.fromService = getIntent().getStringExtra("fromService");
        }
        showLockScreenFragment();
    }

    /* access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onStop() {
        try {
            if (this.fromService != null && this.fromService.equalsIgnoreCase("Chat") && !this.closeChat) {
                Intent intent = new Intent();
                intent.setClassName("com.whatsapp", "com.whatsapp.HomeActivity");
                intent.addFlags(1342177280);
                intent.addFlags(67108864);
                startActivityForResult(intent, 9);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    private void showLockScreenFragment() {
        new PFPinCodeViewModel().isPinCodeEncryptionKeyExist().observe(this, new Observer<PFResult<Boolean>>() {
            /* class com.vestaentertainment.whatsappchatloker.LockActivity.C07343 */

            public void onChanged(@Nullable PFResult<Boolean> pFResult) {
                if (pFResult != null) {
                    if (pFResult.getError() != null) {
                        Toast.makeText(LockActivity.this, "Can not get pin code info", Toast.LENGTH_LONG).show();
                    } else {
                        LockActivity.this.showLockScreenFragment(pFResult.getResult().booleanValue());
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void showLockScreenFragment(boolean z) {
        PFFLockScreenConfiguration.Builder useFingerprint = new PFFLockScreenConfiguration.Builder(this).setTitle(z ? "Unlock with Pin or Fingerprint" : "Create Passcode").setCodeLength(4).setUseFingerprint(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("fingure_auth", true));
        PFLockScreenFragment pFLockScreenFragment = new PFLockScreenFragment();
        pFLockScreenFragment.setOnLeftButtonClickListener(new View.OnClickListener() {
            /* class com.vestaentertainment.whatsappchatloker.LockActivity.View$OnClickListenerC07354 */

            public void onClick(View view) {
                Toast.makeText(LockActivity.this, "Left button pressed", Toast.LENGTH_LONG).show();
            }
        });
        useFingerprint.setMode(z ? PFFLockScreenConfiguration.MODE_AUTH : PFFLockScreenConfiguration.MODE_CREATE);
        if (z) {
            pFLockScreenFragment.setEncodedPinCode(SharedPrefUtils.getStringData(this, SharedPrefUtils.keylockPasscode));
            pFLockScreenFragment.setLoginListener(this.mLoginListener);
        }
        pFLockScreenFragment.setConfiguration(useFingerprint.build());
        pFLockScreenFragment.setCodeCreateListener(this.mCodeCreateListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_view, pFLockScreenFragment).commit();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void showMainFragment() {
        String str = this.fromService;
        if (str == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else  {
            //Intent intent = new Intent();
            //intent.setClassName("com.whatsapp", "com.whatsapp.HomeActivity");
            //intent.addFlags(1342177280);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //startActivityForResult(intent, 9);
            finish();
        }
        /*else {
            moveTaskToBack(true);
        }

         */
    }
}