package com.lxc.birthday;

/**
 * Created by LaiXiancheng on 2017/12/18.
 * Email: lxc.sysu@qq.com
 */

public class BirthdayBean {
	String name;
	String birthDay;
	String gift;
	String tel;

	public BirthdayBean(String name, String birthDay, String gift, String tel) {
		this.name = name;
		this.birthDay = birthDay;
		this.gift = gift;
		this.tel = tel;
	}

	public String getName() {
		return name;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public String getGift() {
		return gift;
	}

	public String getTel() {
		return tel;
	}
}
