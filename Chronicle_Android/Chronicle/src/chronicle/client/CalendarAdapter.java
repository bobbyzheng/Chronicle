package chronicle.client;
import java.util.ArrayList;
import java.util.Calendar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> events;

    public CalendarAdapter(Context c) {
        mContext = c;
        this.events = new ArrayList<String>();
        populate();
    }
    
    public void setItems(ArrayList<String> events) {
    	this.events = events;
    }
    

    public int getCount() {
        return cells.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new TextView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
    	View v = convertView;
        TextView scheduleView;
        
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            scheduleView = new TextView(mContext);
        	LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.calendar_item, null);
        }
        
		v.setBackgroundResource(R.drawable.list_item_background);

        scheduleView = (TextView)v.findViewById(R.id.date);
        //scheduleView.setClickable(false);
    	//scheduleView.setFocusable(false);
        if(position%3==1){
    		v.setBackgroundResource(R.drawable.item_background_focused);
        }
        else{
     		v.setBackgroundResource(R.drawable.list_item_background);
     		v.setClickable(false);
        }
        scheduleView.setText(cells[position]);

        String event = cells[position];
        ImageView iw = (ImageView)v.findViewById(R.id.date_icon);
        /*
        if(event.length()>0 && events!=null && events.contains(event)) {        		
        	iw.setVisibility(View.VISIBLE);
        }
        else {
        	iw.setVisibility(View.INVISIBLE);
        }
        */
        iw.setVisibility(View.INVISIBLE);
        return v;
        //scheduleView.setText(cells[position]);
        //return scheduleView;
    }

    // references to our images
    public void populate() {
    	events.clear();
    	cells = new String[168];
    	for(int i=0; i<168; i++){
    		int tempInt = (i/7)+1;
			String temp = Integer.toString(tempInt);
    		if (tempInt < 12){
    			cells[i]= temp.concat(" AM");
    		}
    		else
    			cells[i]=temp.concat(" PM");
    		
    	}
    }
    private String[] cells; 
}