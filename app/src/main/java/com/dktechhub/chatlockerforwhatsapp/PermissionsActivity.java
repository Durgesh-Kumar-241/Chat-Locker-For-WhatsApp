package com.dktechhub.chatlockerforwhatsapp;
import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.PowerManager;
        import android.view.MenuItem;
        import android.widget.Button;
        import androidx.appcompat.app.AppCompatActivity;

public class PermissionsActivity extends AppCompatActivity {
    Button btnGrantPermissions;

    /* access modifiers changed from: protected */
    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Permissions");
        setContentView(R.layout.activity_permissions);
        this.btnGrantPermissions = findViewById(R.id.btnGrantPermissions);
        /* class com.vestaentertainment.whatsappchatloker.PermissionsActivity.View$OnClickListenerC07451 */
        this.btnGrantPermissions.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= 23) {
                Intent intent = new Intent();
                String packageName = PermissionsActivity.this.getPackageName();
                if (!((PowerManager) PermissionsActivity.this.getSystemService(Context.POWER_SERVICE)).isIgnoringBatteryOptimizations(packageName)) {
                    intent.setAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
                    intent.setData(Uri.parse("package:" + packageName));
                    PermissionsActivity.this.startActivity(intent);
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        onBackPressed();
        return true;
    }
}