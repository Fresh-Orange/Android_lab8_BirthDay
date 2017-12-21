package com.lxc.birthday;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by LaiXiancheng on 2017/12/18.
 * Email: lxc.sysu@qq.com
 */

public class BirthAdapter extends RecyclerView.Adapter<BirthAdapter.BirthVH> {

	private List<BirthdayBean> BirthList;//列表对应的数据
	private Context context;
	private recyItemClickListener itemClickListener;//自定义的接口

	public BirthAdapter(Context context, List<BirthdayBean> BirthList, recyItemClickListener itemClickListener) {
		this.BirthList = BirthList;
		this.context = context;
		this.itemClickListener = itemClickListener;
	}

	@Override
	public BirthVH onCreateViewHolder(ViewGroup parent, int viewType) {
		//直接在此处将布局文件inflate成View
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
		return new BirthVH(view);
	}

	@Override
	public void onBindViewHolder(final BirthVH holder, int position) {
		//直接在这里设置数据
		String name = BirthList.get(position).name;
		String birthday = BirthList.get(position).birthDay;
		String gift = BirthList.get(position).gift;
		holder.tvName.setText(name);
		holder.tvBirthday.setText(birthday);
		holder.tvGift.setText(gift);
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				itemClickListener.onClick(holder.getAdapterPosition());
			}
		});
		holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				itemClickListener.onLongClick(holder.getAdapterPosition());
				return true;//消费长按事件
			}
		});
	}

	@Override
	public int getItemCount() {
		return BirthList.size();
	}

	class BirthVH extends RecyclerView.ViewHolder{
		TextView tvName;
		TextView tvBirthday;
		TextView tvGift;
		BirthVH(View view) {
			super(view);
			tvBirthday = view.findViewById(R.id.tv_birthday);
			tvName = view.findViewById(R.id.tv_name);
			tvGift = view.findViewById(R.id.tv_gift);
		}
	}

}
