package com.lxc.birthday;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
	private EditText Birthday;
	private EditText Gift;
	private EditText Name;
	private Button btAdd;
	BirthDB db = new BirthDB(this, "birth", null, 1);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		Name = findViewById(R.id.tv_name);
		Birthday = findViewById(R.id.tv_birthday);
		Gift = findViewById(R.id.tv_gift);
		btAdd = findViewById(R.id.bt_new);
		btAdd.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (TextUtils.isEmpty(Name.getText().toString()))
				{
					Toast.makeText(AddActivity.this, "名字不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				boolean isExits = db.isExits(Name.getText().toString());

				if (isExits)
				{
					Toast.makeText(AddActivity.this, "名字不能重复", Toast.LENGTH_SHORT).show();
					return;
				}
				db.insert(Name.getText().toString(), Birthday.getText().toString(), Gift.getText().toString());
				finish();
			}
		});
	}
}
