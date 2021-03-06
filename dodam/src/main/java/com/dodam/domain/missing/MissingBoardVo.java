package com.dodam.domain.missing;

import java.sql.Timestamp;

public class MissingBoardVo {
	private int no;
	private String category;
	private String title;
	private String animal;
	private String writer;
	private String contents;
	private Timestamp registerdate;
	private int readcount;
	private int likecount;
	private String img;
	private String thumbimg;
	private String location;
	private String dlocation;
	private Timestamp missingdate;
	private char gender;
	private String breed;
	private String age;
	private String name;
	private String contact;
	private String dpchknum;
	public MissingBoardVo() {
		super();
	}
	public MissingBoardVo(int no, String category, String title, String animal, String writer, String contents,
			Timestamp registerdate, int readcount, int likecount, String img, String thumbimg, String location,
			String dlocation, Timestamp missingdate, char gender, String breed, String age, String name, String contact,
			String dpchknum) {
		super();
		this.no = no;
		this.category = category;
		this.title = title;
		this.animal = animal;
		this.writer = writer;
		this.contents = contents;
		this.registerdate = registerdate;
		this.readcount = readcount;
		this.likecount = likecount;
		this.img = img;
		this.thumbimg = thumbimg;
		this.location = location;
		this.dlocation = dlocation;
		this.missingdate = missingdate;
		this.gender = gender;
		this.breed = breed;
		this.age = age;
		this.name = name;
		this.contact = contact;
		this.dpchknum = dpchknum;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAnimal() {
		return animal;
	}
	public void setAnimal(String animal) {
		this.animal = animal;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public Timestamp getRegisterdate() {
		return registerdate;
	}
	public void setRegisterdate(Timestamp registerdate) {
		this.registerdate = registerdate;
	}
	public int getReadcount() {
		return readcount;
	}
	public void setReadcount(int readcount) {
		this.readcount = readcount;
	}
	public int getLikecount() {
		return likecount;
	}
	public void setLikecount(int likecount) {
		this.likecount = likecount;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getThumbimg() {
		return thumbimg;
	}
	public void setThumbimg(String thumbimg) {
		this.thumbimg = thumbimg;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDlocation() {
		return dlocation;
	}
	public void setDlocation(String dlocation) {
		this.dlocation = dlocation;
	}
	public Timestamp getMissingdate() {
		return missingdate;
	}
	public void setMissingdate(Timestamp missingdate) {
		this.missingdate = missingdate;
	}
	public char getGender() {
		return gender;
	}
	public void setGender(char gender) {
		this.gender = gender;
	}
	public String getBreed() {
		return breed;
	}
	public void setBreed(String breed) {
		this.breed = breed;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getDpchknum() {
		return dpchknum;
	}
	public void setDpchknum(String dpchknum) {
		this.dpchknum = dpchknum;
	}
	@Override
	public String toString() {
		return "MissingBoardVo [no=" + no + ", category=" + category + ", title=" + title + ", animal=" + animal
				+ ", writer=" + writer + ", contents=" + contents + ", registerdate=" + registerdate + ", readcount="
				+ readcount + ", likecount=" + likecount + ", img=" + img + ", thumbimg=" + thumbimg + ", location="
				+ location + ", dlocation=" + dlocation + ", missingdate=" + missingdate + ", gender=" + gender
				+ ", breed=" + breed + ", age=" + age + ", name=" + name + ", contact=" + contact + ", dpchknum="
				+ dpchknum + "]";
	}

	
}