package com.lxc.birthday;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	RecyclerView recyclerView;
	Button btNew;
	BirthDB db = new BirthDB(this, "birth", null, 1);
	ArrayList<BirthdayBean> birthdayBeans = new ArrayList<>();
	BirthAdapter birthAdapter;
	TelInterface telInterface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btNew = findViewById(R.id.bt_new);
		btNew.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, AddActivity.class);
				startActivity(i);
			}
		});
		recyclerView = findViewById(R.id.rc_items);
		recyItemClickListener itemClickListener = new recyItemClickListener() {
			@Override
			public void onClick(int position) {
				showChangeDialog(position);
			}
			@Override
			public void onLongClick(int position) {
				showDeleteDialog(position);
			}
		};
		birthAdapter = new BirthAdapter(this, birthdayBeans , itemClickListener);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(birthAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshView();
	}

	private void showDeleteDialog(final int position){
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("是否删除？");

		builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				db.delete(birthdayBeans.get(position).name);	//数据库删除数据
				refreshView();		//刷新界面
			}
		});
		builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	private void showChangeDialog(final int position){
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater factor = LayoutInflater.from(MainActivity.this);
		View view_in = factor.inflate(R.layout.dialog_layout, null);
		builder.setView(view_in);

		// *********填入数据************
		final BirthdayBean bean = birthdayBeans.get(position);
		final TextView tv_name = view_in.findViewById(R.id.tv_name);
		tv_name.setText(bean.name);
		final TextView tv_tel = view_in.findViewById(R.id.tv_tel);
		telInterface = new TelInterface() {
			@Override
			public void setTel() {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						final String tel = db.queryTel(bean.name);
						tv_tel.post(new Runnable() {
							@Override
							public void run() {
								tv_tel.setText(tel);
							}
						});
					}
				});
				t.start();
			}
		};
		requestAndGetTel();
		final EditText tv_birthday = view_in.findViewById(R.id.tv_birthday);
		tv_birthday.setText(bean.birthDay);
		final EditText tv_gift = view_in.findViewById(R.id.tv_gift);
		tv_gift.setText(bean.gift);

		builder.setPositiveButton(getString(R.string.save_modify), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String birthday = tv_birthday.getText().toString();
				String gift = tv_gift.getText().toString();
				db.update(bean.name, birthday, gift);	//数据库更新数据
				refreshView();		//刷新界面
			}
		});
		builder.setNegativeButton(getString(R.string.give_up_modify), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	private void requestAndGetTel(){
		if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
				!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, 1);
		} else {//已授权
			telInterface.setTel();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == 1) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				//权限申请成功
				telInterface.setTel();
			} else {
				Toast.makeText(MainActivity.this, "获取联系人的权限申请失败", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	private void refreshView(){
		ArrayList<BirthdayBean> tmpList = db.queryAll();
		birthdayBeans.clear();
		birthdayBeans.addAll(tmpList);
		birthAdapter.notifyDataSetChanged();
	}

}
