package com.example.xmtest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private EditText mloginAccount;
    private EditText mloginPassword;
    private DBOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        dbHelper = new DBOpenHelper(MainActivity.this, "Bank", null, 1);
    }

    private void initView() {
        Button register = findViewById(R.id.register);
        register.setOnClickListener(this);
        Button login = findViewById(R.id.login);
        login.setOnClickListener(this);
        mloginAccount = findViewById(R.id.login_account);
        mloginPassword = findViewById(R.id.login_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(register);
                break;
            case R.id.login:
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String loginAccount = mloginAccount.getText().toString();
                String loginPassword = mloginPassword.getText().toString();

                if ("".equals(loginAccount) || "".equals(loginPassword)) {
                    Toast.makeText(MainActivity.this, "卡号或密码为空", Toast.LENGTH_LONG).show();
                }else {
                    /**
                     * 工商银行卡号以61开头
                     * 中国银行卡号以62开头
                     * 建设银行卡号以63开头
                     */
                    int bankCode = Integer.parseInt(loginAccount.substring(0, 2));
                    switch (bankCode) {
                       case 61:
                           Cursor gsCursor = db.query("gsBank", null, "account=?", new String[]{loginAccount}, null, null, null);
                           if (gsCursor.moveToFirst()) {
                               String account = gsCursor.getString(gsCursor.getColumnIndex("account"));
                               String password = gsCursor.getString(gsCursor.getColumnIndex("password"));
                               if (password.equals(loginPassword)) {
                                   MainActivity.this.finish();
                                   BankActivity.actionStart(MainActivity.this, "gsBank", account);
                               }else {
                                   Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_LONG).show();
                                   mloginPassword.setText("");
                               }
                           }else {
                               Toast.makeText(MainActivity.this, "账户不存在", Toast.LENGTH_LONG).show();
                               mloginAccount.setText("");
                               mloginPassword.setText("");
                           }
                           db.close();
                           break;
                       case 62:
                           Cursor zgCursor = db.query("zgBank", null, "account=?", new String[]{loginAccount}, null, null, null);
                           if (zgCursor.moveToFirst()) {
                               String account = zgCursor.getString(zgCursor.getColumnIndex("account"));
                               String password = zgCursor.getString(zgCursor.getColumnIndex("password"));
                               if (loginPassword.equals(password)) {
                                   BankActivity.actionStart(MainActivity.this, "zgBank", account);
                               }else {
                                   Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_LONG).show();
                                   mloginPassword.setText("");
                               }
                           }else {
                               Toast.makeText(MainActivity.this, "账户不存在", Toast.LENGTH_LONG).show();
                               mloginAccount.setText("");
                               mloginPassword.setText("");
                           }
                           db.close();
                           break;
                       case 63:
                           Cursor jsCursor = db.query("jsBank", null, "account=?", new String[]{loginAccount}, null, null, null);
                           if (jsCursor.moveToFirst()) {
                               String account = jsCursor.getString(jsCursor.getColumnIndex("account"));
                               String password = jsCursor.getString(jsCursor.getColumnIndex("password"));
                               if (loginPassword.equals(password)) {
                                   BankActivity.actionStart(MainActivity.this, "jsBank", account);
                               }
                               else {
                                   Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_LONG).show();
                                   mloginPassword.setText("");
                               }
                           }else {
                               Toast.makeText(MainActivity.this, "账户不存在", Toast.LENGTH_LONG).show();
                               mloginAccount.setText("");
                               mloginPassword.setText("");
                           }
                           db.close();
                           break;
                       default:
                           Toast.makeText(MainActivity.this, "无效卡号", Toast.LENGTH_LONG).show();
                           mloginAccount.setText("");
                           mloginPassword.setText("");
                    }
                }
        }
    }
}
