package com.example.xmtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText registerAccount;
    private EditText registerPassword;
    private EditText confirmPassword;
    private TextView registerTitle;
    private LinearLayout banks;
    private LinearLayout registerLayout;
    private DBOpenHelper dbHelper;
    private String firstCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        dbHelper = new DBOpenHelper(RegisterActivity.this, "Bank", null, 1);
    }

    private void initView() {
        Button gsBank = findViewById(R.id.bank_gs);
        gsBank.setOnClickListener(this);
        Button zgBank = findViewById(R.id.bank_zg);
        zgBank.setOnClickListener(this);
        Button jsBank = findViewById(R.id.bank_js);
        jsBank.setOnClickListener(this);
        Button registerBack = findViewById(R.id.register_back);
        registerBack.setOnClickListener(this);
        registerTitle = findViewById(R.id.register_title);
        banks = findViewById(R.id.bank_layout);
        registerLayout = findViewById(R.id.register_layout);
        registerAccount = findViewById(R.id.register_account);
        registerPassword = findViewById(R.id.register_password);
        confirmPassword = findViewById(R.id.confirm_password);
        Button register = findViewById(R.id.register);
        register.setOnClickListener(this);
    }

    /**
     * 隐藏键盘
     * 弹窗弹出的时候把键盘隐藏掉
     */
    protected void hideInputKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bank_gs:
                banks.setVisibility(View.GONE);
                registerTitle.setText("您当前选择的银行为工商银行");
                registerLayout.setVisibility(View.VISIBLE);
                firstCode = "61";
                break;
            case R.id.bank_zg:
                banks.setVisibility(View.GONE);
                registerTitle.setText("您当前选择的银行为中国银行");
                registerLayout.setVisibility(View.VISIBLE);
                firstCode = "62";
                break;
            case R.id.bank_js:
                banks.setVisibility(View.GONE);
                registerTitle.setText("您当前选择的银行为建设银行");
                registerLayout.setVisibility(View.VISIBLE);
                firstCode = "63";
                break;
            case R.id.register_back:
                banks.setVisibility(View.VISIBLE);
                registerTitle.setText("请选择您要注册的银行: ");
                registerLayout.setVisibility(View.GONE);
                hideInputKeyboard(v);
                registerAccount.setText("");
                registerPassword.setText("");
                confirmPassword.setText("");
                break;
            case R.id.register:
                String account = registerAccount.getText().toString();
                String password = registerPassword.getText().toString();
                final String confirm = confirmPassword.getText().toString();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                /**
                 * 工商银行卡号以61开头
                 * 中国银行卡号以62开头
                 * 建设银行卡号以63开头
                 */

                if ("".equals(account) || "".equals(password)) {
                    Toast.makeText(RegisterActivity.this, "卡号和密码不能为空", Toast.LENGTH_LONG).show();
                }else if ("".equals(confirm)) {
                    Toast.makeText(RegisterActivity.this, "请确认您的登录密码", Toast.LENGTH_LONG).show();
                }else if (account.length() != 4) {
                    Toast.makeText(RegisterActivity.this, "卡号格式输入有误", Toast.LENGTH_LONG).show();
                    registerAccount.setText("");
                    registerPassword.setText("");
                    confirmPassword.setText("");
                }else if (!password.equals(confirm)) {
                    Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_LONG).show();
                    confirmPassword.setText("");
                }else {
                    account = firstCode + account;
                    int bankCode = Integer.parseInt(firstCode);
                    switch (bankCode) {
                        case 61:
                            Cursor gsCursor = db.query("gsBank", null, "account=?", new String[]{account}, null, null, null);
                            if (gsCursor.moveToFirst()) {
                                Toast.makeText(RegisterActivity.this, "账户已存在", Toast.LENGTH_LONG).show();
                                registerAccount.setText("");
                                registerPassword.setText("");
                                confirmPassword.setText("");
                            }else {
                                ContentValues values = new ContentValues();
                                values.put("account", account);
                                values.put("password", password);
                                values.put("money", 0);
                                db.insert("gsBank", null, values);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                                dialog.setTitle("注册成功");
                                dialog.setMessage("账户创建成功，是否返回登录");
                                dialog.setCancelable(false);
                                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        RegisterActivity.this.finish();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        registerAccount.setText("");
                                        registerPassword.setText("");
                                        confirmPassword.setText("");
                                    }
                                });
                                dialog.show();
                            }
                            db.close();
                            break;
                        case 62:
                            Cursor zgCursor = db.query("zgBank", null, "account=?", new String[]{account}, null, null, null);
                            if (zgCursor.moveToFirst()) {
                                Toast.makeText(RegisterActivity.this, "账户已存在", Toast.LENGTH_LONG).show();
                                registerAccount.setText("");
                                registerPassword.setText("");
                                confirmPassword.setText("");
                            }else {
                                ContentValues values = new ContentValues();
                                values.put("account", account);
                                values.put("password", password);
                                values.put("money", 0);
                                db.insert("zgBank", null, values);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                                dialog.setTitle("注册成功");
                                dialog.setMessage("账户创建成功，是否返回登录");
                                dialog.setCancelable(false);
                                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        RegisterActivity.this.finish();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        registerAccount.setText("");
                                        registerPassword.setText("");
                                        confirmPassword.setText("");
                                    }
                                });
                                dialog.show();
                            }
                            db.close();
                            break;
                        case 63:
                            Cursor jsCursor = db.query("jsBank", null, "account=?", new String[]{account}, null, null, null);
                            if (jsCursor.moveToFirst()) {
                                Toast.makeText(RegisterActivity.this, "账户已存在", Toast.LENGTH_LONG).show();
                                registerAccount.setText("");
                                registerPassword.setText("");
                                confirmPassword.setText("");
                            }else {
                                ContentValues values = new ContentValues();
                                values.put("account", account);
                                values.put("password", password);
                                values.put("money", 0);
                                db.insert("jsBank", null, values);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                                dialog.setTitle("注册成功");
                                dialog.setMessage("账户创建成功，是否返回登录");
                                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        RegisterActivity.this.finish();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        registerAccount.setText("");
                                        registerPassword.setText("");
                                        confirmPassword.setText("");
                                    }
                                });
                                dialog.show();
                            }
                            db.close();
                            break;
                    }
                }
        }
    }
}
