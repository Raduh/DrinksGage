package com.raduh.drinksgage;


public class AlcoholicDrink{
	
	public String name;
	
	public double alcohol_content;
	public double volume;
	
	public int count;
	
	public int icon;
	
	public AlcoholicDrink(String name){
		this.name=name;
	}

	public AlcoholicDrink() {
		alcohol_content = 0;
		volume = 0;
		count = 0;
	}
	
}
