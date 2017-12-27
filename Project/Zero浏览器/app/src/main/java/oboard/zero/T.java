package oboard.zero;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceScreen;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.EditTextPreference;

public class T extends PreferenceActivity {
    
    static ListPreference lp;
    static EditTextPreference etp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.setting);
        etp = (EditTextPreference)findPreference("home");
        etp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference p,Object o) {
                    S.put("h", lp.getValue()).ok();
                    return true;
                }
            });
        lp = (ListPreference)findPreference("search");
        lp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference p,Object o) {
                S.put("s", lp.getValue()).ok();
                return true;
            }
        });
    }
}
