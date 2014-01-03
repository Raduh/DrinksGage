package com.raduh.drinksgage;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter{
	
	private AlcoholicDrink[] data;
	private static LayoutInflater inflater=null;
	
	public CustomAdapter(Context context, AlcoholicDrink[] data){
		this.data=data;
		inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getCount(){
		return data.length;
	}
	
	public Object getItem(int position){
		return data[position];
	}
	
	public long getItemId(int position){
		return position;
	}
	
	public void updateContent(AlcoholicDrink[] new_array){
		this.data= new_array;
		this.notifyDataSetChanged();
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		
		View current= convertView;
		if(convertView==null)
			current = inflater.inflate(R.layout.list_item,null);
		
		TextView name = (TextView) current.findViewById(R.id.beverage_name);
		ImageView imag = (ImageView) current.findViewById(R.id.list_image);
		
		TextView count = (TextView) current.findViewById(R.id.drink_count);
		TextView alcohol_content = (TextView) current.findViewById(R.id.alcohol_content);
		TextView volume = (TextView) current.findViewById(R.id.volume);
		//Setting all values in ListView
		
		name.setText(data[position].name);
		imag.setImageResource(data[position].icon);
		
		count.setText(data[position].count + "");
		alcohol_content.setText(data[position].alcohol_content+"% ");
		volume.setText(data[position].volume+"ml ");
		
		
		Button plus_b = (Button) current.findViewById(R.id.plus_b);
		Button minus_b = (Button) current.findViewById(R.id.minus_b);
		
		plus_b.setTag(current);
		minus_b.setTag(current);

		
		return current;	
		
	}//end of method getView :)

}
