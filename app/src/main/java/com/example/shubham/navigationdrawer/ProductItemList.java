package com.example.shubham.navigationdrawer;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.HashMap;


public class ProductItemList extends ArrayAdapter<String> {
    private String[] itemname;
    private String[] description;
    private String[] price;
    public static String[] quanx;
    private Activity context;

    AllSharedPref ses;



    public ProductItemList(Activity context, String[] itemname, String[] description, String[] price,String[] quanx) {
        super(context, R.layout.productitemlist, itemname);
        this.context = context;
        this.itemname = itemname;
        this.description = description;
        this.price = price;
        this.quanx = quanx;

        ses = new AllSharedPref(context);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.productitemlist, null, true);

        TextView textViewItemName = (TextView) listViewItem.findViewById(R.id.item_name);
        TextView textViewItemDesc = (TextView) listViewItem.findViewById(R.id.item_description);
        TextView textViewItemPrice = (TextView) listViewItem.findViewById(R.id.item_price);
        TextView textViewQuanX = (TextView)listViewItem.findViewById(R.id.quanx);


        textViewItemName.setText(itemname[position]);

        textViewItemDesc.setText(description[position]);
        textViewItemPrice.setText(price[position]);

        HashMap<String, String> loc = ses.getPosQuan();
        String pos = loc.get(AllSharedPref.KEY_POSITION);

        if(pos==null){
            pos = "0";

        }
        int p = Integer.parseInt(pos);
       // String name = String.valueOf(textViewItemName.getText().toString().charAt(p));
        String X= loc.get(AllSharedPref.KEY_QUANTITY);
        if(X!=null && position==p){
            textViewQuanX.setVisibility(View.VISIBLE);
            textViewQuanX.setText(" - "+X+"x");
        }
        else{
            textViewQuanX.setVisibility(View.INVISIBLE);

        }

        return listViewItem;
    }
}