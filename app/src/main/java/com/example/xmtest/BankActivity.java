package com.example.xmtest;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

public class BankActivity extends BaseActivity implements View.OnClickListener {

    private static String account;
    private static int firstCode;
    private TextView bankTitle;
    private TextView bankBalance;
    private RelativeLayout operation;
    private LinearLayout bankOperation;
    private EditText bankAccount;
    private EditText inputMoney;
    private Button save_withdraw;
    private DBOpenHelper dbHelper;
    private BigDecimal b;
    private String bank;
    private LinearLayout blanceLayout;

    public static void actionStart(Context context, String bank, String account){
        Intent intent = new Intent(context, BankActivity.class);
        intent.putExtra("Bank", bank);
        intent.putExtra("Account", account);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        initView();
        changeTitle();
        dbHelper = new DBOpenHelper(BankActivity.this, "Bank", null, 1);
    }

    //判断用户登录的银行以及更改银行界面标题
    private void changeTitle() {
        Intent intent = getIntent();
        bank = intent.getStringExtra("Bank");
        account = intent.getStringExtra("Account");
        String title;
        switch (bank) {
            case "gsBank":
                bankTitle.setText("工商银行");
                break;
            case "zgBank":
                bankTitle.setText("中国银行");
                break;
            case "jsBank":
                bankTitle.setText("建设银行");
                break;
        }
    }

    private void initView() {
        bankTitle = findViewById(R.id.bank_title);  //标题
        bankBalance = findViewById(R.id.bank_balance);  //余额
        bankAccount = findViewById(R.id.bank_account);  //转账银行卡号
        inputMoney = findViewById(R.id.money);   //存入的金额
        Button saveMoney = findViewById(R.id.save_money);   //存钱按钮
        saveMoney.setOnClickListener(this);
        Button withdrawMoney = findViewById(R.id.withdraw_money);   //取钱按钮
        withdrawMoney.setOnClickListener(this);
        Button transferAccounts = findViewById(R.id.transfer_accounts); //转账按钮
        transferAccounts.setOnClickListener(this);
        Button checkMoney = findViewById(R.id.check_money); //查询余额按钮
        checkMoney.setOnClickListener(this);
        Button exit = findViewById(R.id.exit);  //退出按钮
        exit.setOnClickListener(this);
        save_withdraw = findViewById(R.id.save_withdraw);   //存入及取出金额按钮
        save_withdraw.setOnClickListener(this);
        //返回按钮
        Button back = findViewById(R.id.back);
        back.setOnClickListener(this);
        operation = findViewById(R.id.operation);   //操作界面
        bankOperation = findViewById(R.id.bank_operation);  //跳转界面
        blanceLayout = findViewById(R.id.blance_layout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_money:
                operation.setVisibility(View.GONE);
                bankOperation.setVisibility(View.VISIBLE);
                blanceLayout.setVisibility(View.GONE);
                bankAccount.setVisibility(View.GONE);
                inputMoney.setVisibility(View.VISIBLE);
                save_withdraw.setVisibility(View.VISIBLE);
                break;
            case R.id.withdraw_money:
                operation.setVisibility(View.GONE);
                bankOperation.setVisibility(View.VISIBLE);
                bankAccount.setVisibility(View.GONE);
                blanceLayout.setVisibility(View.GONE);
                inputMoney.setVisibility(View.VISIBLE);
                inputMoney.setHint("请输入您要取出的金额");
                save_withdraw.setVisibility(View.VISIBLE);
                save_withdraw.setText("取出金额");
                break;
            case R.id.transfer_accounts:
                operation.setVisibility(View.GONE);
                bankOperation.setVisibility(View.VISIBLE);
                blanceLayout.setVisibility(View.GONE);
                bankAccount.setVisibility(View.VISIBLE);
                inputMoney.setVisibility(View.VISIBLE);
                inputMoney.setHint("请输入您要转出的金额");
                save_withdraw.setVisibility(View.VISIBLE);
                save_withdraw.setText("转账");
                break;
            case R.id.check_money:
                operation.setVisibility(View.GONE);
                bankOperation.setVisibility(View.VISIBLE);
                blanceLayout.setVisibility(View.VISIBLE);
                bankAccount.setVisibility(View.GONE);
                inputMoney.setVisibility(View.GONE);
                save_withdraw.setVisibility(View.GONE);
                checkBlance();
                break;
            case R.id.exit:
                BankActivity.this.finish();
                Intent intent = new Intent(BankActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.save_withdraw:
                String bt_text = save_withdraw.getText().toString();
                String inputText = inputMoney.getText().toString();
                String anotherAccount = bankAccount.getText().toString();
                switch (bt_text) {
                    case "存入金额":
                        if (TextUtils.isEmpty(inputText)) {
                            Toast.makeText(BankActivity.this, "请输入您要存入的金额", Toast.LENGTH_SHORT).show();
                        }else {
                            double money = Double.parseDouble(inputText);
                            saveMoney(money);
                            inputMoney.setText("");
                        }
                        break;
                    case "取出金额":
                        if (TextUtils.isEmpty(inputText)) {
                            Toast.makeText(BankActivity.this, "请输入您要取出的金额", Toast.LENGTH_SHORT).show();
                        }else {
                            double money = Double.parseDouble(inputText);
                            withdrawMoney(money);
                            inputMoney.setText("");
                        }
                        break;
                    case "转账":
                        if (TextUtils.isEmpty(anotherAccount)) {
                            Toast.makeText(BankActivity.this, "请输入对方的银行卡卡号", Toast.LENGTH_SHORT).show();
                        }else if (TextUtils.isEmpty(inputText)) {
                            Toast.makeText(BankActivity.this, "请输入您要转出的金额", Toast.LENGTH_SHORT).show();
                        }else {
                            double money = Double.parseDouble(inputText);
                            transferAccounts(anotherAccount, money);
                            inputMoney.setText("");
                            bankAccount.setText("");
                        }
                        break;
                }
                break;
            case R.id.back:
                bankOperation.setVisibility(View.GONE);
                operation.setVisibility(View.VISIBLE);
                hideInputKeyboard(v);
                inputMoney.setText("");
                bankAccount.setText("");
                break;
        }
    }

    //保留小数点后3位
    protected double math3(double blance) {
        b = new BigDecimal(blance);
        blance = b.setScale(3, BigDecimal.ROUND_DOWN).doubleValue();
        return blance;
    }

    //用户存钱操作
    protected void saveMoney(double money) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(bank, null, "account=?", new String[]{account}, null, null, null);
        cursor.moveToFirst();
        double balance = cursor.getDouble(cursor.getColumnIndex("money"));
        balance = balance + money + (money * 0.003);
        balance = math3(balance);
        ContentValues values = new ContentValues();
        values.put("money", balance);
        db.update(bank, values, "account=?", new String[]{account});
        Toast.makeText(BankActivity.this, "存入成功", Toast.LENGTH_SHORT).show();
        db.close();
    }

    //用户取钱操作
    protected void withdrawMoney(double money) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(bank, null, "account=?", new String[]{account}, null, null, null);
        cursor.moveToFirst();
        double blance = cursor.getDouble(cursor.getColumnIndex("money"));
        if (money > blance) {
            Toast.makeText(BankActivity.this, "账户余额不足", Toast.LENGTH_LONG).show();
            inputMoney.setText("");
        }else {
            blance -= money;
            blance = math3(blance);
            ContentValues values = new ContentValues();
            values.put("money", blance);
            db.update(bank, values, "account=?", new String[]{account});
            Toast.makeText(BankActivity.this, "金额已成功取出", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    //用户转账操作
    protected void transferAccounts(String anotherAccount, double money) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int anotherFirstCode = Integer.parseInt(anotherAccount.substring(0, 2));
        Cursor cursor = db.query(bank, null, "account=?", new String[]{account}, null, null, null);
        cursor.moveToFirst();
        double blance = cursor.getDouble(cursor.getColumnIndex("money")); //当前账户余额
        if (money > blance) {
            Toast.makeText(BankActivity.this, "账户余额不足", Toast.LENGTH_SHORT).show();
            inputMoney.setText("");
        }else {
            switch (anotherFirstCode) {
                case 61:
                    Cursor anoGsCursor = db.query("gsBank", null, "account=?", new String[]{anotherAccount}, null, null, null);
                    if (anoGsCursor.moveToFirst()) {
                        double anoGsBlance = anoGsCursor.getDouble(anoGsCursor.getColumnIndex("money"));    //对方账户余额
                        blance -= money;
                        anoGsBlance += money;
                        blance = math3(blance);
                        anoGsBlance = math3(anoGsBlance);
                        ContentValues anoGsValues = new ContentValues();
                        anoGsValues.put("money", anoGsBlance);
                        ContentValues values = new ContentValues();
                        values.put("money", blance);
                        db.update("gsBank", anoGsValues, "account=?", new String[]{anotherAccount});
                        db.update(bank, values, "account=?", new String[]{account});
                        Toast.makeText(BankActivity.this, "转账成功", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(BankActivity.this, "转账对方的银行卡号不存在",Toast.LENGTH_SHORT).show();
                        bankAccount.setText("");
                    }
                    db.close();
                    break;
                case 62:
                    Cursor anoZgCursor = db.query("zgBank", null, "account=?", new String[]{anotherAccount}, null, null, null);
                    if (anoZgCursor.moveToFirst()) {
                        double anoZgBlance = anoZgCursor.getDouble(anoZgCursor.getColumnIndex("money"));
                        blance -= money;
                        anoZgBlance += money;
                        blance = math3(blance);
                        anoZgBlance = math3(anoZgBlance);
                        ContentValues anoZgValues = new ContentValues();
                        anoZgValues.put("money", anoZgBlance);
                        ContentValues values = new ContentValues();
                        values.put("money", blance);
                        db.update("zgBank", anoZgValues, "account=?", new String[]{anotherAccount});
                        db.update(bank, values, "account=?", new String[]{account});
                        Toast.makeText(BankActivity.this, "转账成功", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(BankActivity.this, "转账对方的银行卡号不存在", Toast.LENGTH_SHORT).show();
                        bankAccount.setText("");
                    }
                    db.close();
                    break;
                case 63:
                    Cursor anoJsCursor = db.query("jsBank", null, "account=?", new String[]{anotherAccount}, null, null, null);
                    if (anoJsCursor.moveToFirst()) {
                        double anoJsBlance = anoJsCursor.getDouble(anoJsCursor.getColumnIndex("money"));
                        blance -= money;
                        anoJsBlance += money;
                        blance = math3(blance);
                        anoJsBlance = math3(anoJsBlance);
                        ContentValues anoJsValues = new ContentValues();
                        anoJsValues.put("money", anoJsBlance);
                        ContentValues values = new ContentValues();
                        values.put("money", blance);
                        db.update("jsBank", anoJsValues, "account=?", new String[]{anotherAccount});
                        db.update(bank, values, "account=?", new String[]{account});
                        Toast.makeText(BankActivity.this, "转账成功", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(BankActivity.this, "转账对方的银行卡号不存在", Toast.LENGTH_SHORT).show();
                        bankAccount.setText("");
                    }
                    db.close();
                    break;
                default:
                    Toast.makeText(BankActivity.this, "无效卡号", Toast.LENGTH_SHORT).show();
                    bankAccount.setText("");
            }
        }
    }

    //用户查询账户余额
    protected void checkBlance() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(bank, null, "account=?", new String[]{account}, null, null, null);
        cursor.moveToFirst();
        double blance = cursor.getDouble(cursor.getColumnIndex("money"));
        String firstText = String.valueOf(blance);
        bankBalance.setText(firstText);
    }

    /**
     * 隐藏键盘
     * 弹窗弹出的时候把键盘隐藏掉
     */
    protected void hideInputKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
