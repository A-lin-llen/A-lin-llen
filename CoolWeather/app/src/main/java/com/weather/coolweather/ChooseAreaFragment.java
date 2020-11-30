package com.weather.coolweather;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.weather.db.City;
import com.weather.db.Country;
import com.weather.db.Province;
import com.weather.util.HttpUtil;
import com.weather.util.Utility;

import org.litepal.LitePal;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaFragment  extends Fragment{
    public static final int LEVEL_PROVINCE=0;//省级
    public static final int LEVEL_CITY=1;//市级
    public static final int LEVEL_COUNTY=2;//县级
    private ProgressDialog progressDialog;//请求服务器加载数据时的进度对话框
    private TextView titleText;
    private Button backButton;
    private ListView listview;
    private ArrayAdapter<String> adapter;
    private List<String> dataList=new ArrayList<>();//保存省市县的名字

    private List<Province> provinceList; /*省列表*/

    private List<City> cityList; /*市列表*/

    private List<Country> countryList;/*县级列表*/

    private Province selectedProvince;    /*选中省份*/

    private City selectedCity;/*选中的城市*/

    private int currentLevel;  /*当前选中的级别*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.choose_area,container,false);
        titleText=view.findViewById(R.id.title_text);
        backButton=view.findViewById(R.id.back_button);
        listview= view.findViewById(R.id.list_view);
        adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listview.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(currentLevel==LEVEL_PROVINCE){
                    selectedProvince=provinceList.get(position);
                    queryCities();
                }else if (currentLevel==LEVEL_CITY){
                    selectedCity=cityList.get(position);
                    queryCounties();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel==LEVEL_COUNTY){
                    queryCities();
                }else if (currentLevel==LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();//初始化先加载省份
    }
    /*查询全国所有的省，有限总数据库查询，如果没有查询到再去服务器上查询*/
    private void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList= LitePal.findAll(Province.class);
        if (provinceList.size()>0){
            Log.d("TAG","数据库有数据"+provinceList.toString());

            dataList.clear();
            for (Province province:provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listview.setSelection(0);
            currentLevel=LEVEL_PROVINCE;
        }else{
            String address="http://guolin.tech/api/china";
            Log.d("TAG","数据库无数据"+address);

            queryFromServer(address,"privince");
        }
    }

    /*查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询*/
    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList=LitePal.where("provinceid=?",String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size()>0){
            dataList.clear();
            for (City city:cityList) {
                dataList.add(city.getCityName());
            }
                adapter.notifyDataSetChanged();
                 listview.setSelection(0);
                currentLevel=LEVEL_CITY;
        }else{
            int provinceCode=selectedProvince.getProvinceCode();
            String address="http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }
    }
    /*查询选中市内所有的县，优先从数据库查询，如果没有查询到再到服务器上查询*/
    private void queryCounties(){
        Log.d("TAG","查询");

        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countryList=LitePal.where("cityid=?",String.valueOf(selectedCity.getId())).find(Country.class);
        if(countryList.size()>0){
            dataList.clear();
            Log.d("TAG",dataList.toString());
            for (Country country:countryList) {
                dataList.add(country.getCountryName());
            }
            adapter.notifyDataSetChanged();
            listview.setSelection(0);
            currentLevel=LEVEL_COUNTY;
        }else {
            int provinceCode=selectedProvince.getProvinceCode();
            int cityCode=selectedCity.getCityCode();
            String address="http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address,"country");
        }
    }
    /*根据传入的地址和类型从服务器上查询省市县数据*/
    private void queryFromServer(String address,final String type) {
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText=response.body().string();
                boolean result=false;
                Log.d("TAG","type"+type);

                if("privince".equals(type)){
                    Log.d("TAG","数据请求成功");

                    result=Utility.handleProvinceResponse(responseText);
                }else if("city".equals(type)){
                    result=Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if("country".equals(type)){
                    result=Utility.handleCountyReponse(responseText,selectedCity.getId());
                }if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("privince".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("country".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }

            }
        });
    }
    /*显示进度对话框*/
    private void showProgreDialog(){
        if (progressDialog==null){
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    /*关闭进度对话框*/
    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
