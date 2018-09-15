package oboard.zero;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceScreen;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.EditTextPreference;
import android.preference.SwitchPreference;

public class T extends PreferenceActivity {
    
    static ListPreference lp;
    static EditTextPreference etp;
    static SwitchPreference sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.setting);
        etp = (EditTextPreference)findPreference("home");
        etp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference p, Object o) {
                    S.put("h", etp.getText()).ok();
                    M.d_h = etp.getText();
                    return true;
                }
            });
        lp = (ListPreference)findPreference("search");
        lp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference p, Object o) {
                S.put("s", lp.getValue()).ok();
                M.d_s = lp.getValue();
                return true;
            }
            });
        sp = (SwitchPreference)findPreference("color");
        sp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference p, Object o) {
                    S.put("c", !sp.isChecked()).ok();
                    M.d_c = !sp.isChecked();
                    return true;
                }
            });
    }
}
