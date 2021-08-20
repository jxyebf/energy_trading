package com.example.energy_trading.ui.personal.order;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.example.energy_trading.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class On_list_Trade_Item_Detail_Fragment extends FragmentActivity {
    @BindView(R.id.img_on_list_back)
    ImageView imgOnListBack;
    @BindView(R.id.on_list_order_detail_id)
    TextView onListOrderDetailId;
    @BindView(R.id.on_list_order_detail_tablelayout)
    TableLayout onListOrderDetailTablelayout;
    @BindView(R.id.on_list_order_detail_view_1)
    View onListOrderDetailView1;
    @BindView(R.id.on_list_order_detail_number)
    EditText onListOrderDetailNumber;
    @BindView(R.id.on_list_order_detail_tablelayout_1)
    TableLayout onListOrderDetailTablelayout1;
    @BindView(R.id.on_list_order_detail_view_2)
    View onListOrderDetailView2;
    @BindView(R.id.on_list_order_detail_unit_price)
    EditText onListOrderDetailUnitPrice;
    @BindView(R.id.on_list_order_detail_tablelayout_2)
    TableLayout onListOrderDetailTablelayout2;
    @BindView(R.id.on_list_order_detail_view_3)
    View onListOrderDetailView3;
    @BindView(R.id.on_list_order_detail_trade_date)
    EditText onListOrderDetailTradeDate;
    @BindView(R.id.on_list_order_detail_tablelayout_4)
    TableLayout onListOrderDetailTablelayout4;

    @BindView(R.id.on_list_order_detail_tablelayout_5)
    TableLayout onListOrderDetailTablelayout5;
    @BindView(R.id.on_list_order_detail_view_6)
    View onListOrderDetailView6;
    @BindView(R.id.btn_update_on_list_order)
    Button btnUpdateOnListOrder;

    @BindView(R.id.on_list_order_detail_trade_time)
    TextView onListOrderDetailTradeTime;
    @BindView(R.id.btn_on_list_order_delete)
    Button btnOnListOrderDelete;
    private String order_id;
    double order_number, order_unit_price, order_total_price, int_total_price;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_list_order_item_detail);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        @SuppressLint("ResourceType") ViewGroup vg = findViewById(R.layout.sale_order_item_detail);
        imgOnListBack = this.findViewById(R.id.img_on_list_back);
        imgOnListBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initView();
        btnUpdateOnListOrder = this.findViewById(R.id.btn_update_on_list_order);
        btnUpdateOnListOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //modify_status();
                AlertDialog_Modify();
            }
        });
        btnOnListOrderDelete = this.findViewById(R.id.btn_on_list_order_delete);
        btnOnListOrderDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog_Delete();
            }
        });
    }
    private void AlertDialog_Modify(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Make sure to change the order information?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Confirm ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(Buy_Order_Detail_Fragment.this,"Order status has been modified",Toast.LENGTH_SHORT).show();
                modify_order_detail();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }
    public void AlertDialog_Delete(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Confirm to delete the order?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Confirm ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(Buy_Order_Detail_Fragment.this,"Orders have been deleted",Toast.LENGTH_SHORT).show();
                delete_order();
                finish ();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }
    private void initView() {
        Bundle bundle = getIntent().getExtras();
        String order_number = bundle.getString("number");
        order_id = bundle.getString("item_id");
        String order_unit_price = bundle.getString("unit_price");
        String order_trade_date = bundle.getString("trading_date");
        String Order_trade_time = bundle.getString("trading_time");
        onListOrderDetailId = this.findViewById(R.id.on_list_order_detail_id);
        onListOrderDetailId.setText(order_id);
        onListOrderDetailNumber = this.findViewById(R.id.on_list_order_detail_number);
        onListOrderDetailNumber.setText(order_number);
        onListOrderDetailUnitPrice = this.findViewById(R.id.on_list_order_detail_unit_price);
        onListOrderDetailUnitPrice.setText(order_unit_price);
        onListOrderDetailTradeDate = this.findViewById(R.id.on_list_order_detail_trade_date);
        onListOrderDetailTradeDate.setText(order_trade_date);
        onListOrderDetailTradeTime = this.findViewById(R.id.on_list_order_detail_trade_time);
        onListOrderDetailTradeTime.setText(Order_trade_time);
    }
    private void modify_order_detail(){
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");

        JSONObject jsonObject = new JSONObject();
        try {
            order_number = Double.parseDouble(onListOrderDetailNumber.getText().toString());
            order_unit_price = Double.parseDouble(onListOrderDetailUnitPrice.getText().toString());
            int_total_price = order_number * order_unit_price;
            String.format("%.3f", int_total_price);
            jsonObject.put("item_id", order_id);
            jsonObject.put("number", order_number);
            jsonObject.put("unit_price", order_unit_price);
            jsonObject.put("total_price", int_total_price);
            jsonObject.put("trading_date", onListOrderDetailTradeDate.getText().toString());
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
                .url("http://192.168.138.48/energy_trade/update_order_detail.php")
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
                runOnUiThread(new Runnable() {
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
                            Toast.makeText(On_list_Trade_Item_Detail_Fragment.this,"Order information modified successfully!",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(On_list_Trade_Item_Detail_Fragment.this,"Order information modification failure!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    public void delete_order(){
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("item_id", order_id);
            Log.i(TAG, "123456789012345678901234567890: " + order_id);
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
                .url("http://192.168.138.48/energy_trade/delete_on_list_order.php")
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
                runOnUiThread(new Runnable() {
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
                            Toast.makeText(On_list_Trade_Item_Detail_Fragment.this,"Order deleted successfully!",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(On_list_Trade_Item_Detail_Fragment.this,"Order deletion failure!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
