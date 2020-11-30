package com.example.widgetapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_register;
    Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        btn_login=findViewById(R.id.btn_login);
        btn_register=findViewById(R.id.btn_regster);

        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_regster:
//                Toast.makeText(LoginActivity.this,"注册成功！欢迎登陆",Toast.LENGTH_SHORT).show();
                Toast mytoast=Toast.makeText(LoginActivity.this,"注册成功！",Toast.LENGTH_SHORT);
                mytoast.show();
                break;
            case R.id.btn_login:
                AlertDialog.Builder alterbuilder=new AlertDialog.Builder(LoginActivity.this);
                alterbuilder.setIcon(R.mipmap.ic_launcher);
                alterbuilder.setTitle("用户确认登陆弹框！");

                alterbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(LoginActivity.this,"登陆成功！",Toast.LENGTH_SHORT).show();
                    }
                });

                alterbuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(LoginActivity.this,"登陆失败！",Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog myAlter=alterbuilder.create();
                myAlter.show();
                break;

            default:
                break;
        }
    }
}