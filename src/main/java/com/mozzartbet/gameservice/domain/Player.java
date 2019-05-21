package com.mozzartbet.gameservice.domain;

public class Player {
	private int number;
	private String name;
	private String position;
	private String height;
	private String weight;
	private String birthDate;
	private String nationality;
	private int expirience;
	private String college;
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getCollege() {
		return college;
	}
	public void setCollege(String college) {
		this.college = college;
	}
	public int getExpirience() {
		return expirience;
	}
	public void setExpirience(int expirience) {
		this.expirience = expirience;
	}
	
	@Override
	public String toString() {
		return "Player [number=" + number + ", name=" + name + ", position=" + position + ", height=" + height
				+ ", weight=" + weight + ", birthDate=" + birthDate + ", nationality=" + nationality + ", expirience="
				+ expirience + ", college=" + college + "]";
	}

}
