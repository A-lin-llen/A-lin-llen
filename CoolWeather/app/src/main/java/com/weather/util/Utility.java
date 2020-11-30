package com.weather.util;

import android.text.TextUtils;
import android.util.Log;

import com.weather.db.City;
import com.weather.db.Country;
import com.weather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*解析json数据类：Utility*/

public class Utility {
    /*解析和处理服务器返回的省级数据*/
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvince=new JSONArray(response);
                for (int i=0;i<allProvince.length();i++){
                    JSONObject provinceObject=allProvince.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    Log.d("TAG","写入数据库"+province.toString());

                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /*解析和处理服务器返回的市级数据*/
    public static boolean handleCityResponse(String reponse,int provinceId){
        if (!TextUtils.isEmpty(reponse)){
            try {
                JSONArray allCities=new JSONArray(reponse);
                for (int i=0;i<allCities.length(); i++){
                    JSONObject cityObject=allCities.getJSONObject(i);
                    City city=new City();
                   // city.getCityName(cityObject.getString("name"));
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /*解析和处理服务器返回的县级数据*/
    public static boolean handleCountyReponse(String reponse,int cityId){
        if(!TextUtils.isEmpty(reponse)){
            try {
                JSONArray allCounties=new JSONArray(reponse);
                for (int i=0;i<allCounties.length(); i++) {
                    JSONObject countryObject = allCounties.getJSONObject(i);
                    Country country=new Country();
                    country.setCountryName(countryObject.getString("name"));
                    country.setWeatherId(countryObject.getInt("weather_id"));
                    country.setCityId(cityId);
                    country.save();
                    }
                return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                     }

         }return false;
        }
}
