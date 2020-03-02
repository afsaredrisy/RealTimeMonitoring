package com.nitrr.model;

public class Patient {
	
	private String _id;
	private String name;
	private int age;
	private float height;
	private String gender;
	private int weight;
	private String history;
	private String region;
	
	
	public Patient(String _id,String name, int age, float height, String gender, int weight, String history, String region) {
		this._id = _id;
		this.name = name;
		this.age = age;
		this.height = height;
		this.gender = gender;
		this.weight = weight;
		this.history = history;
		this.region = region;
	}
	
	public String get_id() {
		return _id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}

	@Override
	public String toString() {
		return "{_id=" + _id + ", name=" + name + ", age=" + age + ", height=" + height + ", gender=" + gender
				+ ", weight=" + weight + ", history=" + history + ", region=" + region + "}";
	}
	
}
