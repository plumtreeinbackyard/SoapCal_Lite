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
import android.view.Gravity;
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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class CreateByWeight extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout selectedOilListLayout;

    private boolean[] checkSelected;
    private ArrayList<String> selectedItems = new ArrayList<>();
    private ArrayList<String> selectedItemsWeight = new ArrayList<>();

    private ArrayList<String> popUpOilItems = new ArrayList<>();
    private ArrayList<String> oilSap = new ArrayList<>();
    private TextView chooseOil;

    private ArrayList<String> array_oil_name = new ArrayList<>();
    private ArrayList<String> array_oil_weight = new ArrayList<>();

    private EditText NaOHDiscount, NaOHPurity, water, superfatting, sfName, eo, eoName, ad, adName;
    private ImageButton NaOHPurityHelp;

    private String a, b, c, d, e, f, g, h, m;

    //private ListView selectedOilListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_by_weight);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_by_weight);
        setSupportActionBar(toolbar);

        ImageButton btnBackByWeight = (ImageButton)findViewById(R.id.btn_back_by_weight);
        btnBackByWeight.setOnClickListener(this);

        //selectedOilListView = (ListView)findViewById(R.id.selected_oil_list_view);
        chooseOil = (TextView)findViewById(R.id.choose_oil);
        selectedOilListLayout = (LinearLayout)findViewById(R.id.ll_selected_oil_list);
        this.initializePopUpWindow();
        loadSelectedOilData();
        createList();

        NaOHDiscount = (EditText)findViewById(R.id.editText_NaOH_discount);
        NaOHPurity = (EditText)findViewById(R.id.editText_NaOH_purity);
        water = (EditText)findViewById(R.id.editText_water);
        superfatting = (EditText)findViewById(R.id.editText_sf);
        sfName = (EditText)findViewById(R.id.editText_sf_name);
        eo = (EditText)findViewById(R.id.editText_eo);
        eoName = (EditText)findViewById(R.id.editText_eo_name);
        ad = (EditText)findViewById(R.id.editText_additive);
        adName = (EditText)findViewById(R.id.editText_additive_name);

        NaOHPurityHelp = (ImageButton)findViewById(R.id.ib_NaOH_purity_help);
        NaOHPurityHelp.setOnClickListener(this);

        loadData();
        display();

        Button btnCal = (Button)findViewById(R.id.btn_cal);
        btnCal.setOnClickListener(this);

        //ad.
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9005389628695120~2202441693");
        AdView mAdView = (AdView) findViewById(R.id.adView_by_weight);
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("2B71BD2A2993412FB98017B30DB2AD64").build();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    void initializePopUpWindow() {
        if (popUpOilItems.size() == 0) {
            popUpOilItems.addAll(Arrays.asList(getResources().getStringArray(R.array.oil_name)));
        }
        if (oilSap.size() == 0) {
            oilSap.addAll(Arrays.asList(getResources().getStringArray(R.array.oil_sap_value)));
        }

        checkSelected = new boolean[popUpOilItems.size()];
        for(int i = 0; i < checkSelected.length; i++) {
            checkSelected[i] = false;
        }

        chooseOil.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //save chosen oil name to array_oil_name, and save chosen oil weight to array_oil_weight.
                array_oil_name.clear();
                array_oil_weight.clear();
                if (selectedItems.size() > 0){
                    for (int i = 0; i < selectedItems.size(); i++) {
                        array_oil_name.add(selectedItems.get(i));
                        array_oil_weight.add(selectedItemsWeight.get(i));
                    }
                }

                View view = LayoutInflater.from(CreateByWeight.this).inflate(R.layout.pop_up_window, null, false);

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

                ListView oilList = (ListView)view.findViewById(R.id.list_oil);
                ChooseOilListAdapterByWeight adapter = new ChooseOilListAdapterByWeight(CreateByWeight.this, popUpOilItems);
                oilList.setAdapter(adapter);

                //设置popupWindow里的按钮的事件
                Button btnOK = (Button)view.findViewById(R.id.btn_ok);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vvv) {
                        popWindow.dismiss();

                        //clear selectedItemsWeight first, then read oil weight from array_oil_weight, and generate selectedOilListView.
                        selectedItemsWeight.clear();
                        if (selectedItems.size() > 0) {
                            for (int i = 0; i < selectedItems.size(); i++) {
                                if (array_oil_name.contains(selectedItems.get(i))) {
                                    selectedItemsWeight.add(array_oil_weight.get(array_oil_name.indexOf(selectedItems.get(i))));
                                } else {
                                    selectedItemsWeight.add("");
                                }
                            }
                        }
                        createList();
                    }
                });
            }
        });
    }

    class ChooseOilListAdapterByWeight extends BaseAdapter {
        private ArrayList<String> mListItems = new ArrayList<>();
        private LayoutInflater mInflater;
        private ViewHolder popUpHolder;

        public ChooseOilListAdapterByWeight(Context context, ArrayList<String> items) {
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
                convertView = this.mInflater.inflate(R.layout.pop_up_oil_list_row, null);
                popUpHolder = new ViewHolder();
                popUpHolder.popUpOilName = (TextView)convertView.findViewById(R.id.pop_up_oil_name);
                popUpHolder.chkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                convertView.setTag(popUpHolder);
            }else{
                popUpHolder = (ViewHolder) convertView.getTag();
            }
            popUpHolder.popUpOilName.setText(mListItems.get(position));

            if(checkSelected[position]) {
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
            if(!checkSelected[position1]) {
                checkSelected[position1] = true;
                selectedItems.add(mListItems.get(position1));
            } else {
                checkSelected[position1] = false;
                selectedItems.remove(mListItems.get(position1));
            }
        }
    }

    class ListViewAdapterByWeight extends BaseAdapter {
        private ArrayList<String> mListItems = new ArrayList<>();
        private LayoutInflater mInflater;

        public ListViewAdapterByWeight(Context context, ArrayList<String> items) {
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
                convertView = this.mInflater.inflate(R.layout.oil_list_row, null);
                holder = new ViewHolder();
                holder.oilName = (TextView)convertView.findViewById(R.id.oil_name);
                holder.oilWeight = (EditText) convertView.findViewById(R.id.oil_weight);
                holder.oilWeightUnit = (TextView)convertView.findViewById(R.id.oil_weight_unit);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.ref = position;
            holder.oilName.setText(mListItems.get(position));
            holder.oilWeight.setText(selectedItemsWeight.get(position));
            holder.oilWeightUnit.setText(MainActivity.unitSettings);

            holder.oilWeight.addTextChangedListener(new TextWatcher() {

                // the user's changes are saved here
                public void onTextChanged(CharSequence c, int start, int before, int count) {

                }

                public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                    // this space intentionally left blank
                }

                public void afterTextChanged(Editable c) {
                    selectedItemsWeight.set(holder.ref, c.toString());
                }
            });

            return convertView;
        }

        private class ViewHolder{
            private int ref;
            private TextView oilName;
            private EditText oilWeight;
            private TextView oilWeightUnit;
        }
    }

    public void loadSelectedOilData() {
        //clear selectedItems and selectedItemsWeight first, then check if table 2 has records,
        //if it does, then read data and generate selectedOilListView.
        DBHelper dbHelper = new DBHelper(this);
        selectedItems.clear();
        selectedItemsWeight.clear();
        if (dbHelper.getTableTwoListLength() > 0) {
            selectedItems.addAll(dbHelper.getAllOilName());
            selectedItemsWeight.addAll(dbHelper.getAllOilWeight());
        }

        //check if table 3 has records, if it does, then read data to checkSelected.
        if (dbHelper.getTableThreeListLength() > 0){
            for (int i = 0; i < dbHelper.getTableThreeListLength(); i++) {
                checkSelected[i] = dbHelper.getTableThreeContent(i + 1);
            }
        }
    }

    public void loadData(){
        //check if table 1 has records, if it does, then read data to screen.
        DBHelper dbHelper = new DBHelper(this);
        if (dbHelper.getTableOneIdArray().contains(1)){
            a = dbHelper.getTableOneContent(1, DBHelper.KEY_NAOH_DISCOUNT);
            b = dbHelper.getTableOneContent(1, DBHelper.KEY_WATER_TIMES);
            c = dbHelper.getTableOneContent(1, DBHelper.KEY_SF);
            d = dbHelper.getTableOneContent(1, DBHelper.KEY_SF_NAME);
            e = dbHelper.getTableOneContent(1, DBHelper.KEY_EO);
            f = dbHelper.getTableOneContent(1, DBHelper.KEY_EO_NAME);
            g = dbHelper.getTableOneContent(1, DBHelper.KEY_AD);
            h = dbHelper.getTableOneContent(1, DBHelper.KEY_AD_NAME);
            m = dbHelper.getTableOneContent(1, DBHelper.KEY_NAOH_PURITY);
        }

        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //unitSettings = MainActivity.preferences.getString("Unit", "g");
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
        NaOHPurity.setText(m);
    }

    public void createList(){

        selectedOilListLayout.removeAllViews();

        ListViewAdapterByWeight adapter = new ListViewAdapterByWeight(CreateByWeight.this, selectedItems); // Your adapter.

        final int adapterCount = adapter.getCount();

        for (int i = 0; i < adapterCount; i++) {
            View item = adapter.getView(i, null, null);
            selectedOilListLayout.addView(item);
        }
    }

    @Override
    public void onClick(View v){
        double totalOilWeight;
        switch (v.getId()) {
            case R.id.btn_back_by_weight:
                onBackPressed();
                break;
            case R.id.btn_cal:
                if (selectedItems.size() > 0) {
                    double NaOHWeight = 0;
                    totalOilWeight = 0;
                    //ArrayList<String> singleOilName = new ArrayList<>();
                    ArrayList<Double> singleOilWeight = new ArrayList<>();
                    ArrayList<Double> percentage = new ArrayList<>();
                    ArrayList<Double> sapValueList = new ArrayList<>();
                    double singleOilWeightValue, singlePercentage, NaOHDiscountValue, NaOHPurityValue, waterValue, sfValue, eoValue, adValue;

                    //get totalOilWeight, NaOHWeight, singleOilWeight.
                    for (int i = 0; i < selectedItems.size(); i++) {
                        //singleOilName.add(selectedItems.get(i));
                        int id = popUpOilItems.indexOf(selectedItems.get(i));
                        String oilSapValue = oilSap.get(id);
                        double sapValue = Double.parseDouble(oilSapValue);
                        sapValueList.add(sapValue);

                        if (selectedItemsWeight.get(i).equals("")) {
                            singleOilWeightValue = 0;
                        } else {
                            singleOilWeightValue = Double.parseDouble(selectedItemsWeight.get(i));
                            totalOilWeight = totalOilWeight + singleOilWeightValue;
                            NaOHWeight = NaOHWeight + singleOilWeightValue * sapValue;

                            singleOilWeightValue = Math.round(singleOilWeightValue * 10);
                            singleOilWeightValue = singleOilWeightValue / 10;
                        }
                        singleOilWeight.add(singleOilWeightValue);
                    }
                    //get percentage.
                    for (int i = 0; i < selectedItems.size(); i++) {
                        singlePercentage = singleOilWeight.get(i) / totalOilWeight * 100;
                        singlePercentage = Math.round(singlePercentage * 100);
                        singlePercentage = singlePercentage / 100;
                        percentage.add(singlePercentage);
                    }

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

                        totalOilWeight = Math.round(totalOilWeight);
                        NaOHWeight = Math.round(NaOHWeight);
                        double waterWeight = NaOHWeight * waterValue;
                        waterWeight = Math.round(waterWeight);
                        double SFWeight = totalOilWeight * sfValue * 0.01;
                        SFWeight = Math.round(SFWeight);
                        double totalSoapWeight = totalOilWeight + NaOHWeight + waterWeight + SFWeight;
                        double EOAmount = totalSoapWeight * eoValue * 0.01;
                        EOAmount = Math.round(EOAmount);
                        double ADAmount = totalSoapWeight * adValue * 0.01;
                        ADAmount = Math.round(ADAmount);
                        totalSoapWeight = totalOilWeight + NaOHWeight + waterWeight + SFWeight + EOAmount + ADAmount;
                        totalSoapWeight = Math.round(totalSoapWeight);

                        recipe.append(" " + (int) totalOilWeight + "g\n\n" + "- Oils:\n");
                        for (int i = 0; i < selectedItems.size(); i++) {
                            recipe.append("  " + (i+1) + ".  " + selectedItems.get(i) + " (Sap: " + sapValueList.get(i) + "):  " + removeTrailingZero(percentage.get(i)) + "%, " + (int) Math.round(singleOilWeight.get(i)) + "g\n");
                        }

                        recipe.append("\n" + "- "+ getResources().getString(R.string.NaOH) + "  " + (int) NaOHWeight + "g (" + getResources().getString(R.string.discount) + " " + removeTrailingZero(NaOHDiscountValue) + "%, purity " + removeTrailingZero(NaOHPurityValue) + "% )\n");
                        recipe.append("- "+ getResources().getString(R.string.water) + "  " + (int) waterWeight + "g (" + removeTrailingZero(waterValue) + " " + getResources().getString(R.string.times_of_NaOH_weight) + ")\n");
                        recipe.append("- "+ getResources().getString(R.string.superfatting) + "  " + (int) SFWeight + "g (" + removeTrailingZero(sfValue) + "% " + sfName.getText().toString() + ")\n");
                        recipe.append("- "+ getResources().getString(R.string.essential_oil) + "  " + (int) EOAmount + "g (" + removeTrailingZero(eoValue) + "% " + eoName.getText().toString() + ")\n");
                        recipe.append("- "+ getResources().getString(R.string.additive) + "  " + (int) ADAmount + "g (" + removeTrailingZero(adValue) + "% " + adName.getText().toString() + ")\n\n");
                        recipe.append(getResources().getString(R.string.total_soap_weight) + " " + (int) totalSoapWeight + "g\n\n");
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

                        totalOilWeight = Math.round(totalOilWeight * 10);
                        totalOilWeight = totalOilWeight / 10;
                        NaOHWeight = Math.round(NaOHWeight * 10);
                        NaOHWeight = NaOHWeight / 10;
                        double waterWeight = NaOHWeight * waterValue;
                        waterWeight = Math.round(waterWeight * 10);
                        waterWeight = waterWeight / 10;
                        double SFWeight = totalOilWeight * sfValue * 0.01;
                        SFWeight = Math.round(SFWeight * 10);
                        SFWeight = SFWeight / 10;
                        double totalSoapWeight = totalOilWeight + NaOHWeight + waterWeight + SFWeight;
                        double EOAmount = totalSoapWeight * eoValue * 0.01;
                        EOAmount = Math.round(EOAmount * 10);
                        EOAmount = EOAmount / 10;
                        double ADAmount = totalSoapWeight * adValue * 0.01;
                        ADAmount = Math.round(ADAmount * 10);
                        ADAmount = ADAmount / 10;
                        totalSoapWeight = totalOilWeight + NaOHWeight + waterWeight + SFWeight + EOAmount + ADAmount;
                        totalSoapWeight = Math.round(totalSoapWeight * 10);
                        totalSoapWeight = totalSoapWeight / 10;

                        recipe.append(" " + removeTrailingZero(totalOilWeight) + "oz\n\n"+ "- Oils:\n");
                        for (int i = 0; i < selectedItems.size(); i++) {
                            recipe.append("  " + (i+1) + ".  " + selectedItems.get(i) + " (Sap: " + sapValueList.get(i) + "):  " + removeTrailingZero(percentage.get(i)) + "%, " + removeTrailingZero(singleOilWeight.get(i)) + "oz\n");
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

                    Intent intent = new Intent(CreateByWeight.this, DisplaySingleRecipe.class);
                    intent.putExtra("recipe", recipe.toString());
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(this, getResources().getString(R.string.please_select_oils), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;
            case R.id.ib_NaOH_purity_help:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.NaOH_purity_help));
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        new Thread(){
            @Override
            public void run(){
                saveData();
            }
        }.start();

        finish();
    }

    static String removeTrailingZero(double input) {
        BigDecimal number = new BigDecimal(Double.toString(input));
        return number.stripTrailingZeros().toPlainString();
    }

    public void saveData() {
        //save data to table 1.
        DBHelper dbHelper = new DBHelper(this);

        if (dbHelper.getTableOneIdArray().contains(1)) {
                dbHelper.updateTableOne(1, NaOHDiscount.getText().toString(), NaOHPurity.getText().toString(), water.getText().toString(),
                        superfatting.getText().toString(), sfName.getText().toString(), eo.getText().toString(),
                        eoName.getText().toString(), ad.getText().toString(), adName.getText().toString(), "");
        }
        else {   //save data on to line 1
                dbHelper.insertTableOneById(1, NaOHDiscount.getText().toString(), NaOHPurity.getText().toString(), water.getText().toString(),
                        superfatting.getText().toString(), sfName.getText().toString(), eo.getText().toString(),
                        eoName.getText().toString(), ad.getText().toString(), adName.getText().toString(), "");
        }

        //save current selected oil name and weight to table 2.
        if (dbHelper.getTableTwoListLength() > 0) {
                dbHelper.deleteAllTableTwo();
        }
        if (selectedItems.size() != 0) {
            for (int i = 0; i < selectedItems.size(); i++) {
                dbHelper.insertTableTwoById(i + 1, selectedItems.get(i), selectedItemsWeight.get(i));
            }
        }

        //save checkSelected data to table 3.
        if (dbHelper.getTableThreeListLength() > 0){

                for (int i = 0; i < checkSelected.length; i++) {
                    dbHelper.updateTableThree(i + 1, String.valueOf(checkSelected[i]));
                }
        }
        else {
                for (int i = 0; i < checkSelected.length; i++) {
                    dbHelper.insertTableThreeById(i + 1, String.valueOf(checkSelected[i]));
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_by_weight_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.reset_by_weight) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(CreateByWeight.this);
            builder1.setMessage(getResources().getString(R.string.reset_all_the_data));
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            NaOHDiscount.setText("");
                            NaOHPurity.setText("");
                            water.setText("");
                            superfatting.setText("");
                            sfName.setText("");
                            eo.setText("");
                            eoName.setText("");
                            ad.setText("");
                            adName.setText("");

                            for(int i = 0; i < checkSelected.length; i++) {
                                checkSelected[i] = false;
                            }
                            selectedItems.clear();
                            selectedItemsWeight.clear();
                            createList();

                            DBHelper dbHelper = new DBHelper(CreateByWeight.this);
                            if (dbHelper.getTableOneIdArray().contains(1)){
                                dbHelper.deleteTableOneById(1);
                            }
                            dbHelper.deleteAllTableTwo();
                            dbHelper.deleteAllTableThree();
                        }
                    });

            builder1.setNegativeButton(
                    getResources().getString(R.string.no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
            return true;
        }
        else if (id == R.id.unit_setting_by_weight) {
            final String[] unit = new String[]{"g", "oz"};
            AlertDialog alert_unit;
            AlertDialog.Builder unit_setting_builder = new AlertDialog.Builder(this);
            if (MainActivity.unitSettings.equals("g")) {
                alert_unit = unit_setting_builder.setTitle(getResources().getString(R.string.choose_unit))
                        .setSingleChoiceItems(unit, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.unitSettings = unit[which];
                                //saveUnit();
                                dialog.cancel();

                                createList();

                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.you_chose) + " '" + unit[which] + "' as unit.", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
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
                                //saveUnit();
                                dialog.cancel();

                                createList();

                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.you_chose) + " '" + unit[which] + "' as unit.", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }).create();
                alert_unit.show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
