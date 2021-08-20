package com.example.energy_trading.ui.trading;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.energy_trading.DBHelper.OrderDBHelper;
import com.example.energy_trading.Dao.OrderDao;
import com.example.energy_trading.R;
import com.example.energy_trading.bean.Trade_Item;
import com.example.energy_trading.bean.userevent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class TradingFragment extends Fragment {
    private static final int DATE_PICKER_ID = 1;
    private Calendar calendar = null;
    private boolean isAdd = true;
    private OrderDao dao;
    private EditText et_date, et_number, et_price;
    private Button bt_con,bt_reset;
    private Spinner sp = null;
    private String time = null;
    private SQLiteDatabase db;
    double order_number, order_unit_price, order_total_price, int_total_price;
    String uemail, upassword;
    OrderDBHelper orderdbhelper;

    private void clearUIData() {
        et_number.setText(" ");
        et_price.setText(" ");
        et_date.setText(" ");
        sp.setSelection(0);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trading, container, false);
        view.setSaveEnabled(false);
        et_date = (EditText) view.findViewById(R.id.et_order_date);
        et_date.setInputType(InputType.TYPE_NULL);
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(v);
            }
        });
        sp = (Spinner) view.findViewById(R.id.sp_trading_time_chelduled);
        //设置item的被选择的监听
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //当item被选择后调用此方法
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取我们所选中的内容
                time = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        EventBus.getDefault().register(this);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        et_number = (EditText) getActivity().findViewById(R.id.et_ord_number);
        et_price = (EditText) getActivity().findViewById(R.id.et_order_price);
        et_date = (EditText) getActivity().findViewById(R.id.et_order_date);
        bt_con = (Button) getActivity().findViewById(R.id.btn_confirm);
        bt_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog_Delete();
            }
        });
        bt_reset = (Button) getActivity().findViewById(R.id.btn_reset);
        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearUIData();
            }
        });
    }
    public void AlertDialog_Delete(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Confirm to insert the order?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Confirm ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(Buy_Order_Detail_Fragment.this,"Orders have been deleted",Toast.LENGTH_SHORT).show();
                if (checkForm()) {
                    new Thread() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            Insert();
                            Looper.loop();
                            et_number.setText(" ");
                            et_price.setText(" ");
                            et_date.setText(" ");
                            sp.setSelection(0);
                        }
                    }.start();

                }
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

    private boolean checkForm() {
        final String number = et_number.getText().toString();
        final String price = et_price.getText().toString();
        boolean isPass = true;
        if (number.isEmpty()) {
            et_number.setError("Please enter the desired number of transactions");
            isPass = false;
        } else {
            et_number.setError(null);
        }

        if (price.isEmpty()) {
            et_price.setError("Please enter the desired transaction price");
            isPass = false;
        } else {
            et_price.setError(null);
        }
        return isPass;
    }
    @Subscribe(sticky = true)  //必须使用EventBus的订阅注解
    public void onEventMainThread(userevent userevent) {
        uemail = userevent.getUseremail();
        upassword = userevent.getUserpassword();
        Log.i(TAG, "email3333333333333333333333333333333333333333333333333333333: " + uemail);
    }

    private void Insert() {
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        //申明给服务端传递一个json串
        JSONObject jsonObject = new JSONObject();
        //JSONArray jsonArray=new JSONArray();
        try {
            order_number = Double.parseDouble(et_number.getText().toString());
            order_unit_price = Double.parseDouble(et_price.getText().toString());
            int_total_price = order_number * order_unit_price;
            String.format("%.3f", int_total_price);
            jsonObject.put("number", et_number.getText().toString());
            jsonObject.put("unit_price", et_price.getText().toString());
            jsonObject.put("total_price", int_total_price);
            jsonObject.put("trading_date", et_date.getText().toString());
            jsonObject.put("trading_time", time);
            jsonObject.put("email", uemail);
            jsonObject.put("password", upassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jsonObject.toString();
        Log.i(TAG, json);
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON, json);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url("http://192.168.138.48/energy_trade/item_insert.php")
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int retCode = 0;
                        try {
                            String jsondate = response.body().string();
                            JSONObject json = new JSONObject(String.valueOf(jsondate));
                            retCode = json.getInt("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //客户端自己判断是否成功。
                        if (retCode == 1) {
                            //Intent intent= new Intent(getActivity(), MainActivity.class);
                            //startActivity(intent);
                            //AccountManager.setSignState(true);
                            Toast.makeText(getActivity(),"Successful Insert!",Toast.LENGTH_SHORT).show();
                            clearUIData();
                        } else {
                            Toast.makeText(getActivity(),"Error Insert!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
    /*

    public void Insert(){
        ContentValues values = new ContentValues();
        order_number = Double.parseDouble(et_number.getText().toString());
        order_unit_price = Double.parseDouble(et_price.getText().toString());
        int_total_price=order_number* order_unit_price;
        String.format("%.3f", int_total_price);
        values.put("number", order_number);
        values.put("unit_price", order_unit_price);
        values.put("total_price", int_total_price);
        values.put("trading_date", et_date.getText().toString());
        values.put("trading_time", time);
        db.insert("orders", null, values);
    }
     */

    public void openDatePickerDialog(final View v) {
        calendar = Calendar.getInstance(Locale.UK);
        // Get Current Date
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    String selectedDate = getDate(year, monthOfYear, dayOfMonth);
                    Log.i(TAG, "onDateSetdad0: " + selectedDate);
                    switch (v.getId()) {
                        case R.id.et_order_date:
                            ((EditText) v).setText(selectedDate);
                            break;
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        //datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }


/*
    @OnClick({R.id.btn_confirm, R.id.btn_reset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                if(checkForm()){
                    Trade_Item order = getOrderFromUI();
                    if (isAdd) {
                        long id = dao.addOrder(order);
                        dao.closeDB();
                        if (id > 0) {
                            Toast.makeText(getActivity(), "保存成功， ID=" + id,Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), "保存失败，请重新输入！", Toast.LENGTH_SHORT).show();
                        }
                    } else if (!isAdd) {
                        long id = dao.addOrder(order);
                        dao.closeDB();
                        if (id > 0) {
                            Toast.makeText(getActivity(), "更新成功",Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), "更新失败，请重新输入！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case R.id.btn_reset:
                clearUIData();
                break;
        }
    }

 */

    private void showEditUI(Trade_Item tradeitem) {
        // 先将order携带的数据还原到order的每一个属性中去
        String number = tradeitem.getNumber();
        String unit_price = tradeitem.getUnit_price();
        String trading_data = tradeitem.getTrading_date();
        String trading_time = tradeitem.getTrading_time();

        // 还原数据
        et_number.setText(number + "");
        et_price.setText(unit_price + "");
        et_date.setText(trading_data + "");
        sp.setSelection(0);
    }
    //      * 得到当前的日期
    private String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }
    //转化为XXXX-XX-XX格式
    private String getDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return DateFormat.format("yyyy-MM-dd", calendar).toString();
    }
}