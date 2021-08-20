package com.example.energy_trading.ui.personal.order;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;

import com.example.energy_trading.R;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

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

public class Sale_Order_Detail_Fragment extends FragmentActivity {
    @BindView(R.id.img_name_back)
    ImageView imgNameBack;
    @BindView(R.id.order_detail_name)
    AppCompatTextView orderDetailName;
    @BindView(R.id.tv_sale_order_detail_name_1)
    TextView tvSaleOrderDetailName1;
    @BindView(R.id.view_sale_detail_1)
    View viewSaleDetail1;
    @BindView(R.id.sale_detail_number)
    TextView saleDetailNumber;
    @BindView(R.id.sale_detail_tablelayout_2)
    TableLayout saleDetailTablelayout2;
    @BindView(R.id.sale_detail_total_price)
    TextView saleDetailTotalPrice;
    @BindView(R.id.sale_detail_tablelayout_3)
    TableLayout saleDetailTablelayout3;
    @BindView(R.id.tv_sale_order_detail_name_2)
    TextView tvSaleOrderDetailName2;
    @BindView(R.id.view_sale_detail_3)
    View viewSaleDetail3;
    @BindView(R.id.style_of_order_name_username_1)
    TextView styleOfOrderNameUsername1;
    @BindView(R.id.style_of_order_username)
    TextView styleOfOrderUsername;
    @BindView(R.id.sale_detail_tablelayout_4)
    TableLayout saleDetailTablelayout4;
    @BindView(R.id.order_number_1)
    TextView orderNumber1;
    @BindView(R.id.sale_detail_tablelayout_5)
    TableLayout saleDetailTablelayout5;
    @BindView(R.id.sale_detail_transaction_time)
    TextView saleDetailTransactionTime;
    @BindView(R.id.sale_detail_tablelayout_6)
    TableLayout saleDetailTablelayout6;
    @BindView(R.id.sale_order_detail_view_3)
    View saleOrderDetailView3;
    @BindView(R.id.btn_transaction_completed)
    Button btnTransactionCompleted;
    @BindView(R.id.btn_order_delete)
    Button btnOrderDelete;
    private String order_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_order_item_detail);
        ButterKnife.bind(this);

        //EventBus.getDefault().register(this);
        imgNameBack = this.findViewById(R.id.img_name_back);
        imgNameBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish ();
            }
        });
        initView();
        btnOrderDelete=this.findViewById(R.id.btn_order_delete);
        btnOrderDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog_Delete();
            }
        });
    }
    public void AlertDialog_Delete(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Confirm to delete the order?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Confirm ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(Sale_Order_Detail_Fragment.this,"Orders have been deleted",Toast.LENGTH_SHORT).show();
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
    public void delete_order(){
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", order_id);
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
                .url("http://192.168.138.48/energy_trade/delete_order.php")
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
                            Toast.makeText(Sale_Order_Detail_Fragment.this,"Order deleted successfully!",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Sale_Order_Detail_Fragment.this,"Order deletion failure!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private void initView(){
        Bundle bundle=getIntent().getExtras();
        String order_number=bundle.getString("order_number");
        String order_status=bundle.getString("order_status");
        order_id=bundle.getString("order_id");
        String order_total_price=bundle.getString("order_total_price");
        String order_seller_name=bundle.getString("order_buyer_name");
        String Order_pay_time=bundle.getString("Order_pay_time");
//        styleOfOrderNameUsername1=this.findViewById(R.id.style_of_order_name_username_1);
//        styleOfOrderNameUsername1.setText("Seller's nickname: ");
        saleDetailNumber=this.findViewById(R.id.sale_detail_number);
        saleDetailNumber.setText(order_number);
        saleDetailTotalPrice=this.findViewById(R.id.sale_detail_total_price);
        saleDetailTotalPrice.setText(order_total_price);
        styleOfOrderUsername=this.findViewById(R.id.style_of_order_username);
        styleOfOrderUsername.setText(order_seller_name);
        orderNumber1=this.findViewById(R.id.order_number_1);
        orderNumber1.setText(order_id);
        saleDetailTransactionTime=this.findViewById(R.id.sale_detail_transaction_time);
        saleDetailTransactionTime.setText(Order_pay_time);
        orderDetailName=this.findViewById(R.id.order_detail_name);
        orderDetailName.setText(order_status);
        btnTransactionCompleted=this.findViewById(R.id.btn_transaction_completed);
        btnTransactionCompleted.setVisibility(View.GONE);
    }

}
