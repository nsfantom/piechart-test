package tm.nsfantom.piechart.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.jakewharton.rxbinding2.widget.RxAutoCompleteTextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import tm.nsfantom.piechart.BuildConfig;
import tm.nsfantom.piechart.PieChartApp;
import tm.nsfantom.piechart.R;
import tm.nsfantom.piechart.databinding.ActivityCountryBinding;
import tm.nsfantom.piechart.model.CountryModel;
import tm.nsfantom.piechart.util.PrefStorage;

public final class CountryActivity extends AppCompatActivity {

    ActivityCountryBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_country);
    }

    @Override
    protected void onStart() {
        super.onStart();
        PrefStorage prefStorage = ((PieChartApp) getApplication()).getPrefStorage();
        updateText(prefStorage.getCountryCode());

        List<CountryModel> list = new ArrayList<>();
        try (InputStream inputStream = this.getAssets().open("countries.json")) {
            list = readJsonStream(inputStream);
        } catch (IOException e) {
            Timber.e(e, "Error: %s", e.getMessage());
        }

        HashMap<String, String> countryMap = new HashMap<>();
        Observable.fromIterable(list)
                .forEach(countryModel ->
                        countryMap.put(countryModel.getName(), countryModel.getCountryCode()));
        if (BuildConfig.DEBUG)
            Observable.fromIterable(countryMap.entrySet())
                    .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                    .forEach(cm -> Timber.d("Country entry: %s, %s", cm.getKey(), cm.getValue()));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                countryMap.keySet().toArray(new String[countryMap.size()]));
        binding.etCountry.setThreshold(0);
        binding.etCountry.setAdapter(adapter);
        RxAutoCompleteTextView.itemClickEvents(binding.etCountry)
                .subscribe(event -> {
                    prefStorage.saveCountryCode(countryMap.get(adapter.getItem(event.position())));
                    updateText(prefStorage.getCountryCode());
                }, e -> Timber.e(e, "error: %s", e.getMessage()));
    }

    private List<CountryModel> readJsonStream(InputStream in) throws IOException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"))) {
            return readCountriesArray(reader);
        }
    }

    private List<CountryModel> readCountriesArray(JsonReader reader) throws IOException {
        List<CountryModel> country = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            country.add(new GsonBuilder().create().fromJson(reader, CountryModel.class));
        }
        reader.endArray();
        return country;
    }

    private void updateText(String text) {
        binding.tvCountry.setText(getString(R.string.country_code).concat(text.isEmpty() ? "unset" : text));
    }
}
