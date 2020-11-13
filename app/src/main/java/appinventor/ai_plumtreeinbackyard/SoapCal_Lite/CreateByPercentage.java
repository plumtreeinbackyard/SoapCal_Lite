package appinventor.ai_plumtreeinbackyard.SoapCal_Lite;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static appinventor.ai_plumtreeinbackyard.SoapCal_Lite.CreateByWeight.removeTrailingZero;

public class CreateByPercentage extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout selectedOilListLayout;

    private boolean[] checkSelectedByPercentage;
    private ArrayList<String> selectedItemsByPercentage = new ArrayList<>();
    private ArrayList<String> selectedItemsPercentage = new ArrayList<>();

    private ArrayList<String> popUpOilItems = new ArrayList<>();
    private ArrayList<String> oilSap = new ArrayList<>();

    private TextView chooseOil;
    private ArrayList<String> array_oil_name = new ArrayList<>();
    private ArrayList<String> array_oil_percentage = new ArrayList<>();

    private EditText totalOilWeight, NaOHDiscount, NaOHPurity, water, superfatting, sfName, eo, eoName, ad, adName;
    private ImageButton NaOHPurityHelp;

    private String a, b, c, d, e, f, g, h, l, m;

    private TextView totalOilWeightUnit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_by_percentage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_by_percentage);
        setSupportActionBar(toolbar);

        ImageButton btnBackByPercentage = (ImageButton)findViewById(R.id.btn_back_by_percentage);
        btnBackByPercentage.setOnClickListener(this);

        //selectedOilListView = (ListView)findViewById(R.id.selected_oil_list_view_by_percentage);
        chooseOil = (TextView)findViewById(R.id.choose_oil_by_percentage);
        selectedOilListLayout = (LinearLayout)findViewById(R.id.ll_selected_oil_list_by_percentage);
        this.initializePopUpWindow();
        loadSelectedOilData();
        createList();

        totalOilWeight = (EditText)findViewById(R.id.total_oil_weight);
        totalOilWeightUnit = (TextView)findViewById(R.id.total_oil_weight_unit);
        NaOHDiscount = (EditText)findViewById(R.id.editText_NaOH_discount_by_percentage);
        NaOHPurity = (EditText)findViewById(R.id.editText_NaOH_purity_by_percentage);
        water = (EditText)findViewById(R.id.editText_water_by_percentage);
        superfatting = (EditText)findViewById(R.id.editText_sf_by_percentage);
        sfName = (EditText)findViewById(R.id.editText_sf_name_by_percentage);
        eo = (EditText)findViewById(R.id.editText_eo_by_percentage);
        eoName = (EditText)findViewById(R.id.editText_eo_name_by_percentage);
        ad = (EditText)findViewById(R.id.editText_additive_by_percentage);
        adName = (EditText)findViewById(R.id.editText_additive_name_by_percentage);

        NaOHPurityHelp = (ImageButton)findViewById(R.id.ib_NaOH_purity_help_by_percentage);
        NaOHPurityHelp.setOnClickListener(this);

        loadData();
        display();

        Button btnCal = (Button)findViewById(R.id.btn_cal_by_percentage);
        btnCal.setOnClickListener(this);

        if (MainActivity.unitSettings.equals("oz")) {
            totalOilWeightUnit.setText("oz");
        }

        //ad.
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9005389628695120~2202441693");
        AdView mAdView = (AdView) findViewById(R.id.adView_by_percentage);
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("2B71BD2A2993412FB98017B30DB2AD64").build();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void initializePopUpWindow() {
        if (popUpOilItems.size() == 0) {
            popUpOilItems.addAll(Arrays.asList(getResources().getStringArray(R.array.oil_name)));
        }
        if (oilSap.size() == 0) {
            oilSap.addAll(Arrays.asList(getResources().getStringArray(R.array.oil_sap_value)));
        }

        checkSelectedByPercentage = new boolean[popUpOilItems.size()];
        for(int i = 0; i < checkSelectedByPercentage.length; i++) {
            checkSelectedByPercentage[i] = false;
        }

        chooseOil.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //save chosen oil name to array_oil_name, and save chosen oil weight to array_oil_weight.
                array_oil_name.clear();
                array_oil_percentage.clear();
                if (selectedItemsByPercentage.size() > 0){
                    for (int i = 0; i < selectedItemsByPercentage.size(); i++) {
                        array_oil_name.add(selectedItemsByPercentage.get(i));
                        array_oil_percentage.add(selectedItemsPercentage.get(i));
                    }
                }

                View view = LayoutInflater.from(CreateByPercentage.this).inflate(R.layout.pop_up_window_by_percentage, null, false);

                //1.构造一个PopupWindow，参数依次是加载的View，宽高
                final PopupWindow popWindow = new PopupWindow(view,
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

                //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
                //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
                //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
                popWindow.setTouchable(true);
                popWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View vv, MotionEvent event) {
                        return false;
                        // 这里如果返回true的话，touch事件将被拦截
                        // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                    }
                });
                popWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));    //要为popWindow设置一个背景才有效

                //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
                int yOffset = -(v.getMeasuredHeight() - popWindow.getHeight());
                popWindow.showAsDropDown(v, 0, yOffset);

                ListView oilList = (ListView)view.findViewById(R.id.list_oil_by_percentage);
                ChooseOilListAdapterByPercentage adapter = new ChooseOilListAdapterByPercentage(CreateByPercentage.this, popUpOilItems);
                oilList.setAdapter(adapter);

                //设置popupWindow里的按钮的事件
                Button btnOK = (Button)view.findViewById(R.id.btn_ok_by_percentage);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vvv) {
                        popWindow.dismiss();

                        //clear selectedItemsWeight first, then read oil weight from array_oil_weight, and generate selectedOilListView.
                        selectedItemsPercentage.clear();
                        if (selectedItemsByPercentage.size() > 0) {
                            for (int i = 0; i < selectedItemsByPercentage.size(); i++) {
                                if (array_oil_name.contains(selectedItemsByPercentage.get(i))) {
                                    selectedItemsPercentage.add(array_oil_percentage.get(array_oil_name.indexOf(selectedItemsByPercentage.get(i))));
                                } else {
                                    selectedItemsPercentage.add("");
                                }
                            }
                        }

                        createList();
                    }
                });
            }
        });
    }

    class ChooseOilListAdapterByPercentage extends BaseAdapter {
        private ArrayList<String> mListItems = new ArrayList<>();
        private LayoutInflater mInflater;
        private ChooseOilListAdapterByPercentage.ViewHolder popUpHolder;

        public ChooseOilListAdapterByPercentage(Context context, ArrayList<String> items) {
            this.mListItems.addAll(items);
            this.mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return this.mListItems.size();
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0L;
        }

        private class ViewHolder{
            private TextView popUpOilName;
            private CheckBox chkBox;

            private ViewHolder() {
            }
        }

        public View getView(final int position , View convertView , ViewGroup parent){
            if(convertView == null){
                convertView = this.mInflater.inflate(R.layout.pop_up_oil_list_row_by_percentage, null);
                popUpHolder = new ChooseOilListAdapterByPercentage.ViewHolder();
                popUpHolder.popUpOilName = (TextView)convertView.findViewById(R.id.pop_up_oil_name_by_percentage);
                popUpHolder.chkBox = (CheckBox) convertView.findViewById(R.id.checkbox_by_percentage);
                convertView.setTag(popUpHolder);
            }else{
                popUpHolder = (ChooseOilListAdapterByPercentage.ViewHolder) convertView.getTag();
            }
            popUpHolder.popUpOilName.setText(mListItems.get(position));

            if(checkSelectedByPercentage[position]) {
                popUpHolder.chkBox.setChecked(true);
            } else {
                popUpHolder.chkBox.setChecked(false);
            }
            popUpHolder.chkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    selectItem(position);
                }
            });

            return convertView;
        }

        private void selectItem(int position1) {
            if(!checkSelectedByPercentage[position1]) {
                checkSelectedByPercentage[position1] = true;
                selectedItemsByPercentage.add(mListItems.get(position1));
            } else {
                checkSelectedByPercentage[position1] = false;
                selectedItemsByPercentage.remove(mListItems.get(position1));
            }
        }
    }

    class ListViewAdapterByPercentage extends BaseAdapter {
        private ArrayList<String> mListItems = new ArrayList<>();
        private LayoutInflater mInflater;

        public ListViewAdapterByPercentage(Context context, ArrayList<String> items) {
            this.mInflater = LayoutInflater.from(context);
            this.mListItems.addAll(items);
        }

        public int getCount() {
            return this.mListItems.size();
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0L;
        }

        public View getView(final int position , View convertView , ViewGroup parent){
            final ViewHolder holder;
            if(convertView == null){
                convertView = this.mInflater.inflate(R.layout.oil_list_row_by_percentage, null);
                holder = new ListViewAdapterByPercentage.ViewHolder();
                holder.oilName = (TextView)convertView.findViewById(R.id.oil_name_by_percentage);
                holder.oilPercentage = (EditText) convertView.findViewById(R.id.oil_percentage);
                convertView.setTag(holder);
            }else{
                holder = (ListViewAdapterByPercentage.ViewHolder) convertView.getTag();
            }
            holder.ref = position;
            holder.oilName.setText(mListItems.get(position));
            holder.oilPercentage.setText(selectedItemsPercentage.get(position));

            holder.oilPercentage.addTextChangedListener(new TextWatcher() {

                // the user's changes are saved here
                public void onTextChanged(CharSequence c, int start, int before, int count) {

                }

                public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                    // this space intentionally left blank
                }

                public void afterTextChanged(Editable c) {
                    selectedItemsPercentage.set(holder.ref, c.toString());
                }
            });

            return convertView;
        }

        private class ViewHolder{
            private int ref;
            private TextView oilName;
            private EditText oilPercentage;
        }
    }

    public void loadSelectedOilData() {
        //clear selectedItemsByPercentage and selectedItemsPercentage first, then check if table 4 has records,
        //if it does, then read data and generate selectedOilListView.
        DBHelper dbHelper = new DBHelper(this);
        selectedItemsByPercentage.clear();
        selectedItemsPercentage.clear();
        if (dbHelper.getTableFourListLength() > 0) {
            selectedItemsByPercentage.addAll(dbHelper.getAllOilNameByPercentage());
            selectedItemsPercentage.addAll(dbHelper.getAllOilPercentage());
        }

        //check if table 5 has records, if it does, then read data to checkSelected.
        if (dbHelper.getTableFiveListLength() > 0){
            for (int i = 0; i < dbHelper.getTableFiveListLength(); i++) {
                checkSelectedByPercentage[i] = dbHelper.getTableFiveContent(i + 1);
            }
        }
    }

    public void loadData(){
        //check if table 1 has records, if it does, then read data to screen.
        DBHelper dbHelper = new DBHelper(this);
        if (dbHelper.getTableOneIdArray().contains(2)){
            a = dbHelper.getTableOneContent(2, DBHelper.KEY_NAOH_DISCOUNT);
            b = dbHelper.getTableOneContent(2, DBHelper.KEY_WATER_TIMES);
            c = dbHelper.getTableOneContent(2, DBHelper.KEY_SF);
            d = dbHelper.getTableOneContent(2, DBHelper.KEY_SF_NAME);
            e = dbHelper.getTableOneContent(2, DBHelper.KEY_EO);
            f = dbHelper.getTableOneContent(2, DBHelper.KEY_EO_NAME);
            g = dbHelper.getTableOneContent(2, DBHelper.KEY_AD);
            h = dbHelper.getTableOneContent(2, DBHelper.KEY_AD_NAME);
            l = dbHelper.getTableOneContent(2, DBHelper.KEY_TOTAL_OIL_WEIGHT);
            m = dbHelper.getTableOneContent(2, DBHelper.KEY_NAOH_PURITY);
        }

        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //MainActivity.unitSettings = MainActivity.preferences.getString("Unit", "g");
    }

    public void display(){
        NaOHDiscount.setText(a);
        water.setText(b);
        superfatting.setText(c);
        sfName.setText(d);
        eo.setText(e);
        eoName.setText(f);
        ad.setText(g);
        adName.setText(h);
        totalOilWeight.setText(l);
        NaOHPurity.setText(m);
    }

    public void createList(){

        selectedOilListLayout.removeAllViews();

        ListViewAdapterByPercentage adapter = new ListViewAdapterByPercentage(this, selectedItemsByPercentage); // Your adapter.

        final int adapterCount = adapter.getCount();

        for (int i = 0; i < adapterCount; i++) {
            View item = adapter.getView(i, null, null);
            selectedOilListLayout.addView(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back_by_percentage:
                onBackPressed();
                break;
            case R.id.btn_cal_by_percentage:
                if (selectedItemsByPercentage.size() > 0) {
                    double totalOilWeightValue, oilPercentageValue, totalOilPercentage = 0, NaOHWeight = 0;
                    double oilWeightValue, NaOHDiscountValue, NaOHPurityValue, waterValue, sfValue, eoValue, adValue;
                    //ArrayList<String> singleOilName = new ArrayList<>();
                    ArrayList<Double> singleOilWeight = new ArrayList<>();
                    ArrayList<Double> singleOilPercentage = new ArrayList<>();
                    ArrayList<Double> sapValueList = new ArrayList<>();

                    //get totalOilWeightValue.
                    if (totalOilWeight.getText().toString().equals("")) {
                        totalOilWeightValue = 0;
                    } else {
                        totalOilWeightValue = Double.parseDouble(totalOilWeight.getText().toString());
                    }
                    //get totalOilPercentage, oilWeightValue, NaOHWeight.
                    for (int i = 0; i < selectedItemsByPercentage.size(); i++) {
                        //singleOilName.add(selectedItemsByPercentage.get(i));
                        int id = popUpOilItems.indexOf(selectedItemsByPercentage.get(i));
                        String oilSapValue = oilSap.get(id);
                        double sapValue = Double.parseDouble(oilSapValue);
                        sapValueList.add(sapValue);

                        if (selectedItemsPercentage.get(i).equals("")) {
                            oilPercentageValue = 0;
                            oilWeightValue = 0;
                        } else {
                            oilPercentageValue = Double.parseDouble(selectedItemsPercentage.get(i));
                            totalOilPercentage = totalOilPercentage + oilPercentageValue;
                            oilWeightValue = totalOilWeightValue * oilPercentageValue * 0.01;
                            NaOHWeight = NaOHWeight + oilWeightValue * sapValue;

                            oilPercentageValue = Math.round(oilPercentageValue * 10);
                            oilPercentageValue = oilPercentageValue / 10;
                            oilWeightValue = Math.round(oilWeightValue * 10);
                            oilWeightValue = oilWeightValue / 10;
                        }
                        singleOilPercentage.add(oilPercentageValue);
                        singleOilWeight.add(oilWeightValue);
                    }

                    if (totalOilPercentage != 100) {
                        Toast.makeText(this, getResources().getString(R.string.does_not_equal_100), Toast.LENGTH_LONG).show();
                    } else {
                        if (NaOHDiscount.getText().toString().equals("")) {
                            //NaOHDiscount.setText("0");
                            NaOHDiscountValue = 0;
                        } else {
                            NaOHDiscountValue = Double.parseDouble(NaOHDiscount.getText().toString());
                        }
                        if (NaOHPurity.getText().toString().equals("")) {
                            NaOHPurityValue = 100;
                        } else {
                            NaOHPurityValue = Double.parseDouble(NaOHPurity.getText().toString());
                        }
                        if (water.getText().toString().equals("")) {
                            //water.setText("0");
                            waterValue = 0;
                        } else {
                            waterValue = Double.parseDouble(water.getText().toString());
                        }
                        if (superfatting.getText().toString().equals("")) {
                            //superfatting.setText("0");
                            sfValue = 0;
                        } else {
                            sfValue = Double.parseDouble(superfatting.getText().toString());
                        }
                        if (eo.getText().toString().equals("")) {
                            //eo.setText("0");
                            eoValue = 0;
                        } else {
                            eoValue = Double.parseDouble(eo.getText().toString());
                        }
                        if (ad.getText().toString().equals("")) {
                            //eo.setText("0");
                            adValue = 0;
                        } else {
                            adValue = Double.parseDouble(ad.getText().toString());
                        }
                        //get discounted NaOHWeight.
                        NaOHWeight = NaOHWeight * (100 - NaOHDiscountValue) * 0.01;
                        NaOHWeight = NaOHWeight / NaOHPurityValue * 100;

                        Date myDate = new Date();

                        StringBuilder recipe = new StringBuilder(getResources().getString(R.string.total_oil_weight));
                        if (MainActivity.unitSettings == null || MainActivity.unitSettings.equals("g")) {
                            NaOHDiscountValue = Math.round(NaOHDiscountValue * 10);
                            NaOHDiscountValue = NaOHDiscountValue / 10;
                            waterValue = Math.round(waterValue * 10);
                            waterValue = waterValue / 10;
                            sfValue = Math.round(sfValue * 10);
                            sfValue = sfValue / 10;
                            eoValue = Math.round(eoValue * 10);
                            eoValue = eoValue / 10;
                            adValue = Math.round(adValue * 10);
                            adValue = adValue / 10;

                            totalOilWeightValue = Math.round(totalOilWeightValue);
                            NaOHWeight = Math.round(NaOHWeight);
                            double waterWeight = NaOHWeight * waterValue;
                            waterWeight = Math.round(waterWeight);
                            double SFWeight = totalOilWeightValue * sfValue * 0.01;
                            SFWeight = Math.round(SFWeight);
                            double totalSoapWeight = totalOilWeightValue + NaOHWeight + waterWeight + SFWeight;
                            double EOAmount = totalSoapWeight * eoValue * 0.01;
                            EOAmount = Math.round(EOAmount);
                            double ADAmount = totalSoapWeight * adValue * 0.01;
                            ADAmount = Math.round(ADAmount);
                            totalSoapWeight = totalOilWeightValue + NaOHWeight + waterWeight + SFWeight + EOAmount + ADAmount;
                            totalSoapWeight = Math.round(totalSoapWeight);

                            recipe.append(" " + (int) totalOilWeightValue + "g\n\n"+ "- Oils:\n");
                            for (int i = 0; i < selectedItemsByPercentage.size(); i++) {
                                recipe.append("  " + (i+1) + ".  " + selectedItemsByPercentage.get(i) + " (Sap: " + sapValueList.get(i) + "):  " + removeTrailingZero(singleOilPercentage.get(i)) + "%, " + (int) Math.round(singleOilWeight.get(i)) + "g\n");
                            }
                            recipe.append("\n" +  "- "+ getResources().getString(R.string.NaOH) + "  " + (int) NaOHWeight + "g (" + getResources().getString(R.string.discount) + " " + removeTrailingZero(NaOHDiscountValue) + "%, purity " + removeTrailingZero(NaOHPurityValue) + "% )\n");
                            recipe.append("- "+ getResources().getString(R.string.water) + "  " + (int) waterWeight + "g (" + removeTrailingZero(waterValue) + " " + getResources().getString(R.string.times_of_NaOH_weight) + ")\n");
                            recipe.append("- "+ getResources().getString(R.string.superfatting) + "  " + (int) SFWeight + "g (" + removeTrailingZero(sfValue) + "% " + sfName.getText().toString() + ")\n");
                            recipe.append("- "+ getResources().getString(R.string.essential_oil) + "  " + (int) EOAmount + "g (" + removeTrailingZero(eoValue) + "% " + eoName.getText().toString() + ")\n");
                            recipe.append("- "+ getResources().getString(R.string.additive) + "  " + (int) ADAmount + "g (" + removeTrailingZero(adValue) + "% " + adName.getText().toString() + ")\n\n");
                            recipe.append(getResources().getString(R.string.total_soap_weight) + "  " + (int) totalSoapWeight + "g\n\n");
                            recipe.append(getResources().getString(R.string.date) + " " + new SimpleDateFormat("yyyy-MM-dd").format(myDate));
                        } else {
                            NaOHDiscountValue = Math.round(NaOHDiscountValue * 10);
                            NaOHDiscountValue = NaOHDiscountValue / 10;
                            waterValue = Math.round(waterValue * 10);
                            waterValue = waterValue / 10;
                            sfValue = Math.round(sfValue * 10);
                            sfValue = sfValue / 10;
                            eoValue = Math.round(eoValue * 10);
                            eoValue = eoValue / 10;
                            adValue = Math.round(adValue * 10);
                            adValue = adValue / 10;

                            totalOilWeightValue = Math.round(totalOilWeightValue * 10);
                            totalOilWeightValue = totalOilWeightValue / 10;
                            NaOHWeight = Math.round(NaOHWeight * 10);
                            NaOHWeight = NaOHWeight / 10;
                            double waterWeight = NaOHWeight * waterValue;
                            waterWeight = Math.round(waterWeight * 10);
                            waterWeight = waterWeight / 10;
                            double SFWeight = totalOilWeightValue * sfValue * 0.01;
                            SFWeight = Math.round(SFWeight * 10);
                            SFWeight = SFWeight / 10;
                            double totalSoapWeight = totalOilWeightValue + NaOHWeight + waterWeight + SFWeight;
                            double EOAmount = totalSoapWeight * eoValue * 0.01;
                            EOAmount = Math.round(EOAmount * 10);
                            EOAmount = EOAmount / 10;
                            double ADAmount = totalSoapWeight * adValue * 0.01;
                            ADAmount = Math.round(ADAmount * 10);
                            ADAmount = ADAmount / 10;
                            totalSoapWeight = totalOilWeightValue + NaOHWeight + waterWeight + SFWeight + EOAmount + ADAmount;
                            totalSoapWeight = Math.round(totalSoapWeight * 10);
                            totalSoapWeight = totalSoapWeight / 10;

                            recipe.append(" " + removeTrailingZero(totalOilWeightValue) + "oz\n\n" + "- Oils:\n");
                            for (int i = 0; i < selectedItemsByPercentage.size(); i++) {
                                recipe.append("  " + (i+1) + ".  " + selectedItemsByPercentage.get(i) + " (Sap: " + sapValueList.get(i) + "):  " + removeTrailingZero(singleOilPercentage.get(i)) + "%, " + removeTrailingZero(singleOilWeight.get(i)) + "oz\n");
                            }
                            recipe.append("\n" + "- "+ getResources().getString(R.string.NaOH) + "  " + removeTrailingZero(NaOHWeight) + "oz (" + getResources().getString(R.string.discount) + " " + removeTrailingZero(NaOHDiscountValue) + "%, purity " + removeTrailingZero(NaOHPurityValue) + "% )\n");
                            recipe.append("- "+ getResources().getString(R.string.water) + "  " + removeTrailingZero(waterWeight) + "oz (" + removeTrailingZero(waterValue) + " " + getResources().getString(R.string.times_of_NaOH_weight) + ")\n");
                            recipe.append("- "+ getResources().getString(R.string.superfatting) + "  " + removeTrailingZero(SFWeight) + "oz (" + removeTrailingZero(sfValue) + "% " + sfName.getText().toString() + ")\n");
                            recipe.append("- "+ getResources().getString(R.string.essential_oil) + "  " + removeTrailingZero(EOAmount) + "oz (" + removeTrailingZero(eoValue) + "% " + eoName.getText().toString() + ")\n");
                            recipe.append("- "+ getResources().getString(R.string.additive) + "  " + removeTrailingZero(ADAmount) + "oz (" + removeTrailingZero(adValue) + "% " + adName.getText().toString() + ")\n\n");
                            recipe.append(getResources().getString(R.string.total_soap_weight) + " " + removeTrailingZero(totalSoapWeight) + "oz\n\n");
                            recipe.append(getResources().getString(R.string.date) + " " + new SimpleDateFormat("yyyy-MM-dd").format(myDate));
                        }
                        new Thread() {
                            @Override
                            public void run() {
                                saveData();
                            }
                        }.start();

                        Intent intent = new Intent(CreateByPercentage.this, DisplaySingleRecipe.class);
                        intent.putExtra("recipe", recipe.toString());
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, getResources().getString(R.string.please_select_oils), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ib_NaOH_purity_help_by_percentage:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.NaOH_purity_help));
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        new Thread() {
            @Override
            public void run() {
                saveData();
            }
        }.start();

        finish();
    }

    public void saveData() {
        //save data to table 1.
        DBHelper dbHelper = new DBHelper(this);
        if (dbHelper.getTableOneIdArray().contains(2)) {
                dbHelper.updateTableOne(2, NaOHDiscount.getText().toString(), NaOHPurity.getText().toString(), water.getText().toString(),
                        superfatting.getText().toString(), sfName.getText().toString(), eo.getText().toString(),
                        eoName.getText().toString(), ad.getText().toString(), adName.getText().toString(),
                        totalOilWeight.getText().toString());
        }
        else {  //save data on to line 2
                dbHelper.insertTableOneById(2, NaOHDiscount.getText().toString(), NaOHPurity.getText().toString(), water.getText().toString(),
                        superfatting.getText().toString(), sfName.getText().toString(), eo.getText().toString(),
                        eoName.getText().toString(), ad.getText().toString(), adName.getText().toString(),
                        totalOilWeight.getText().toString());
        }


        //save current selected oil name and weight to table 4.
        if (dbHelper.getTableFourListLength() > 0) {
                dbHelper.deleteAllTableFour();
        }
        if (selectedItemsByPercentage.size() != 0) {
            for (int i = 0; i < selectedItemsByPercentage.size(); i++) {
                dbHelper.insertTableFourById(i + 1, selectedItemsByPercentage.get(i), selectedItemsPercentage.get(i));
            }
        }

        //save checkSelected data to table 5.
        if (dbHelper.getTableFiveListLength() > 0) {
                for (int i = 0; i < checkSelectedByPercentage.length; i++) {
                    dbHelper.updateTableFive(i + 1, String.valueOf(checkSelectedByPercentage[i]));
                }
        }
        else {
                for (int i = 0; i < checkSelectedByPercentage.length; i++) {
                    dbHelper.insertTableFiveById(i + 1, String.valueOf(checkSelectedByPercentage[i]));
                }
        }
    }

    /*public void saveUnit() {
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = MainActivity.preferences.edit();
        editor.putString("Unit", CreateByWeight.unitSettings);
        editor.apply();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_by_percentage_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.reset_by_percentage) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateByPercentage.this);
            builder.setMessage(getResources().getString(R.string.reset_all_the_data));
            builder.setCancelable(true);

            builder.setPositiveButton(
                    getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            totalOilWeight.setText("");
                            NaOHPurity.setText("");
                            NaOHDiscount.setText("");
                            water.setText("");
                            superfatting.setText("");
                            sfName.setText("");
                            eo.setText("");
                            eoName.setText("");
                            ad.setText("");
                            adName.setText("");

                            for(int i = 0; i < checkSelectedByPercentage.length; i++) {
                                checkSelectedByPercentage[i] = false;
                            }
                            selectedItemsByPercentage.clear();
                            selectedItemsPercentage.clear();
                            createList();

                            DBHelper dbHelper = new DBHelper(CreateByPercentage.this);

                            if (dbHelper.getTableOneIdArray().contains(2)){
                                dbHelper.deleteTableOneById(2);
                            }

                            dbHelper.deleteAllTableFour();
                            dbHelper.deleteAllTableFive();
                        }
                    });

            builder.setNegativeButton(
                    getResources().getString(R.string.no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
        else if (id == R.id.unit_setting_by_percentage) {
            final String[] unit = new String[]{"g", "oz"};
            AlertDialog alert_unit;
            AlertDialog.Builder unit_setting_builder = new AlertDialog.Builder(this);
            if (MainActivity.unitSettings.equals("g")) {
                alert_unit = unit_setting_builder.setTitle(getResources().getString(R.string.choose_unit))
                        .setSingleChoiceItems(unit, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.unitSettings = unit[which];
                                totalOilWeightUnit.setText(MainActivity.unitSettings);
                                //saveUnit();
                                dialog.cancel();

                                createList();

                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.you_chose) + " '" + unit[which] + "' as unit.", Toast.LENGTH_LONG).show();
                            }
                        }).create();
                alert_unit.show();
            }
            else if (MainActivity.unitSettings.equals("oz")) {
                alert_unit = unit_setting_builder.setTitle(getResources().getString(R.string.choose_unit))
                        .setSingleChoiceItems(unit, 1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.unitSettings = unit[which];
                                totalOilWeightUnit.setText(MainActivity.unitSettings);
                                //saveUnit();
                                dialog.cancel();

                                createList();

                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.you_chose) + " '" + unit[which] + "' as unit.", Toast.LENGTH_LONG).show();
                            }
                        }).create();
                alert_unit.show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
