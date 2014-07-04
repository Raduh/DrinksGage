package com.raduh.drinksgage;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class GlobalData extends Application {
	
	public AlcoholicDrink[] beverages;
	
	public int bev_nr;
	
	public SharedPreferences otherData;
	public SharedPreferences bevNames;
	public SharedPreferences bevAlc;
	public SharedPreferences bevVol;
	public Context contxt;
	
	public boolean isMale, isFemale;
	public int weight;
	
	public double standardDrinks;
	
	public void initData(Context cont){
		
		this.contxt=cont;
		
		otherData = cont.getSharedPreferences("otherData", 0);
		bevNames = cont.getSharedPreferences("bevNames", 0);
		bevAlc = cont.getSharedPreferences("bevAlc", 0);
		bevVol = cont.getSharedPreferences("bevVol", 0);
		
		
		bev_nr = otherData.getInt("bev_nr", 9);
		beverages = new AlcoholicDrink[bev_nr];
		
		for(int i=0; i<beverages.length;i++) beverages[i] = new AlcoholicDrink();
		
		beverages[0].name = "Beer";
		beverages[0].volume = 500;
		beverages[0].alcohol_content=5;
		beverages[0].icon=R.drawable.beer;
		
		beverages[1].name="Whiskey";
		beverages[1].volume = 36;
		beverages[1].alcohol_content=40;
		beverages[1].icon=R.drawable.whiskey;
		
		beverages[2].name="Gin";
		beverages[2].volume = 35;
		beverages[2].alcohol_content=37.5;
		beverages[2].icon=R.drawable.gin;
		
		beverages[3].name="Wine";
		beverages[3].volume = 150;
		beverages[3].alcohol_content=13.5;
		beverages[3].icon=R.drawable.wine;
		
		beverages[4].name="Brandy";
		beverages[4].volume = 36;
		beverages[4].alcohol_content=40;
		beverages[4].icon=R.drawable.brandy;
		
		beverages[5].name="Vodka";
		beverages[5].volume = 35;
		beverages[5].alcohol_content=37.5;
		beverages[5].icon=R.drawable.vodka;
		
		beverages[6].name="Liquor";
		beverages[6].volume = 70;
		beverages[6].alcohol_content=17;
		beverages[6].icon=R.drawable.liqueur;
		
		beverages[7].name="Tequila";
		beverages[7].volume = 30;
		beverages[7].alcohol_content=40;
		beverages[7].icon=R.drawable.tequilla;
		
		beverages[8].name="Rum";
		beverages[8].volume = 35;
		beverages[8].alcohol_content=50;
		beverages[8].icon=R.drawable.rum;
		
		if(bev_nr > 9)
			for (int i=9; i<bev_nr;i++){
				beverages[i].name = bevNames.getString(("item"+i), "Not defined");
				beverages[i].volume = bevVol.getInt(("item"+i), 0);
				beverages[i].alcohol_content = bevAlc.getFloat(("item"+i), 0);
				beverages[i].icon = R.drawable.custom_drink;
			}
		
		for(int i=0; i<bev_nr; i++)
			beverages[i].count = otherData.getInt(("itemCount"+i), 0);
		
	} 
	
	public void setUserData(Context cont){
		otherData = cont.getSharedPreferences("otherData", 0);
		weight = otherData.getInt("userWeight", -1);
		isMale = otherData.getBoolean("isMale", false);
		isFemale = otherData.getBoolean("isFemale", false);
	}
	
	public void computeStandardDrinks(){
		
		standardDrinks = 0;	
		for(int i=0; i < bev_nr;i++){
			
			standardDrinks += (beverages[i].volume *( beverages[i].alcohol_content / 100) * beverages[i].count)/18;
		}
		
	}//end of computeStandardDrinks
	
	/** returns the effects at a particular BAC*/
	public String getBacEffectsString (double BAC){
		
		if(BAC < 0.02) return contxt.getString(R.string.BACeffects0);
		if(BAC < 0.04) return contxt.getString(R.string.BACeffects1);
		if(BAC < 0.07) return contxt.getString(R.string.BACeffects2);
		if(BAC < 0.10) return contxt.getString(R.string.BACeffects3);
		if(BAC < 0.13) return contxt.getString(R.string.BACeffects4);
		if(BAC < 0.16) return contxt.getString(R.string.BACeffects5);
		if(BAC < 0.20) return contxt.getString(R.string.BACeffects6);
		if(BAC < 0.25) return contxt.getString(R.string.BACeffects7);
		if(BAC < 0.30) return contxt.getString(R.string.BACeffects8);
		if(BAC < 0.35) return contxt.getString(R.string.BACeffects9);
		if(BAC < 0.40) return contxt.getString(R.string.BACeffects10);
		
		return contxt.getString(R.string.BACeffects11);		
	}
	
	public int getBacEffectsDrawable (double BAC){
		
		if(BAC < 0.02) return R.drawable.baceffects0;
		if(BAC < 0.04) return R.drawable.baceffects1;
		if(BAC < 0.07) return R.drawable.baceffects2;
		if(BAC < 0.10) return R.drawable.baceffects3;
		if(BAC < 0.20) return R.drawable.baceffects4;
		if(BAC < 0.30) return R.drawable.baceffects5;
		if(BAC < 0.40) return R.drawable.baceffects6;
		
		return R.drawable.baceffects7;		
	}
	
	
	
	

}
