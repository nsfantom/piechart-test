package tm.nsfantom.piechart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 1/25/18.
 */

public class CountryModel {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("alpha-2")
    @Expose
    private String alpha2;
    @SerializedName("country-code")
    @Expose
    private String countryCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlpha2() {
        return alpha2;
    }

    public void setAlpha2(String alpha2) {
        this.alpha2 = alpha2;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
