package com.example.energy_trading.ui.home;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;

import com.example.energy_trading.R;
import com.example.energy_trading.bean.Trade_Item;
import com.example.energy_trading.bean.userevent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
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
import qiu.niorgai.StatusBarCompat;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Buy_on_list_order_Fragment extends FragmentActivity {
    @BindView(R.id.img_pay_order)
    ImageView imgPayOrder;
    @BindView(R.id.pay_detail_number)
    TextView payDetailNumber;
    @BindView(R.id.sale_detail_unit_price)
    TextView saleDetailUnitPrice;
    @BindView(R.id.pay_detail_total_price)
    TextView payDetailTotalPrice;
    @BindView(R.id.pay_detail_trade_date)
    TextView payDetailTradeDate;
    @BindView(R.id.pay_detail_trade_time)
    TextView payDetailTradeTime;
    @BindView(R.id.btn_order_pay)
    Button btnOrderPay;
    @BindView(R.id.pay_detail_seller_name)
    TextView payDetailSellerName;

    private Handler handler;
    private String number, unit_price, total_price, trading_date, trading_time,username,uemail,upassword,item_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pay_order_);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        StatusBarCompat.translucentStatusBar(this, false);//隐藏toolbar上面的状态栏：true：隐藏

        imgPayOrder = this.findViewById(R.id.img_pay_order);
        imgPayOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnOrderPay = this.findViewById(R.id.btn_order_pay);
        btnOrderPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog_Pay();
            }
        });
        initView();
    }
    @Subscribe(sticky = true)  //必须使用EventBus的订阅注解
    public void onEventMainThread(userevent userevent) {
        uemail = userevent.getUseremail();
        upassword = userevent.getUserpassword();

    }
    private void initView() {
        Bundle bundle = getIntent().getExtras();
        Trade_Item tradeitem = new Trade_Item();
        tradeitem = (Trade_Item) bundle.getSerializable("tradeitem");
        username = tradeitem.getUsername();
        item_id = tradeitem.getId();
        number = tradeitem.getNumber();
        unit_price = tradeitem.getUnit_price();
        total_price = tradeitem.getTotal_price();
        trading_date = tradeitem.getTrading_date();
        trading_time = tradeitem.getTrading_time();
        Log.i(TAG, "1234567890098765432123456789009876543212345678900987654321: " + username+"123132"+item_id+"123132"+number+"123132" +unit_price+"123132" +total_price+"123132" + trading_date+"123132"+ trading_time);
        payDetailSellerName = this.findViewById(R.id.pay_detail_seller_name);
        payDetailSellerName.setText(username);
        payDetailNumber = this.findViewById(R.id.pay_detail_number);
        payDetailNumber.setText(number);
        saleDetailUnitPrice = this.findViewById(R.id.sale_detail_unit_price);
        saleDetailUnitPrice.setText(unit_price);
        payDetailTotalPrice = this.findViewById(R.id.pay_detail_total_price);
        payDetailTotalPrice.setText(total_price);
        payDetailTradeDate = this.findViewById(R.id.pay_detail_trade_date);
        payDetailTradeDate.setText(trading_date);
        payDetailTradeTime = this.findViewById(R.id.pay_detail_trade_time);
        payDetailTradeTime.setText(trading_time);
    }
    private void AlertDialog_Pay(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Sure to buy this order??");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Confirm ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(Buy_Order_Detail_Fragment.this,"Order status has been modified",Toast.LENGTH_SHORT).show();
                pay();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }
    private void pay(){
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("item_id", item_id);
            jsonObject.put("number", number);
            jsonObject.put("unit_price", unit_price);
            jsonObject.put("total_price", total_price);
            jsonObject.put("trading_date", trading_date);
            jsonObject.put("trading_time", trading_time);
            jsonObject.put("email", uemail);
            jsonObject.put("password", upassword);
            Log.i(TAG, "qweretrtyioasdsagdgfjkyertwrwefd: " + username+item_id+number +unit_price +total_price + trading_date+ trading_time);
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
                .url("http://192.168.138.48/energy_trade/buy_a_order.php")
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
                            //Intent intent= new Intent(getActivity(), MainActivity.class);
                            //startActivity(intent);
                            //AccountManager.setSignState(true);
                            Toast.makeText(Buy_on_list_order_Fragment.this,"Payment successful!",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Buy_on_list_order_Fragment.this,"Payment Failure!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
