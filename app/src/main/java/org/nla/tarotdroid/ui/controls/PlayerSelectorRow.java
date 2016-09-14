package org.nla.tarotdroid.ui.controls;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.nla.tarotdroid.AppContext;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.helpers.UIHelper;
import org.nla.tarotdroid.ui.constants.UIConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PlayerSelectorRow extends LinearLayout {

	private int playerIndex;
	private TextView txtPlayerIndex;
	private LinearLayout textLayout;
	private PlayerSelectorAutoCompleteTextView txtPlayerName;
	private String tempClickedName;
	
	public PlayerSelectorRow(Activity context, int playerIndex) {
		super(context);
		this.playerIndex = playerIndex;
		this.tempClickedName = "";

		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		this.setOrientation(HORIZONTAL);
		
		// player id
		this.txtPlayerIndex = new TextView(context);
		this.txtPlayerIndex.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		this.txtPlayerIndex.setText(Integer.toString(playerIndex));
		
		// player name
		this.txtPlayerName = new PlayerSelectorAutoCompleteTextView(context);
		this.txtPlayerName.setWidth(10);
		this.txtPlayerName.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		this.txtPlayerName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(9) });
		this.txtPlayerName.setThreshold(1);

		// player id / name container
		this.textLayout = new LinearLayout(context);
		this.textLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		this.textLayout.setOrientation(VERTICAL);
		this.textLayout.setPadding(2, 2, 2, 2);
		this.textLayout.setGravity(Gravity.CENTER_VERTICAL);
		this.textLayout.addView(this.txtPlayerIndex);
		this.textLayout.addView(this.txtPlayerName);

		this.addView(this.textLayout);
		this.initializeViews();
	}
	
	public PlayerSelectorRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.playerIndex = 0;
		this.tempClickedName = "";
		
		this.setOrientation(HORIZONTAL);
		
		// player id
		this.txtPlayerIndex = new TextView(context);
		this.txtPlayerIndex.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		this.txtPlayerIndex.setText(Integer.toString(playerIndex));
		
		// player name
		this.txtPlayerName = new PlayerSelectorAutoCompleteTextView(context);
		this.txtPlayerName.setWidth(10);
		this.txtPlayerName.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		this.txtPlayerName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(9) });
		this.txtPlayerName.setThreshold(1);

		// player id / name container
		this.textLayout = new LinearLayout(context);
		this.textLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 15));
		this.textLayout.setOrientation(VERTICAL);
		this.textLayout.setPadding(2, 2, 2, 2);
		this.textLayout.setGravity(Gravity.CENTER_VERTICAL);
		this.textLayout.addView(this.txtPlayerIndex);
		this.textLayout.addView(this.txtPlayerName);

		this.addView(this.textLayout);
		this.initializeViews();
	}

	private void initializeViews() {
		this.txtPlayerIndex.setText(this.getContext().getString(R.string.lblPlayerNumber, String.valueOf(this.playerIndex + 1)));
		this.txtPlayerName.setAdapter(this.buildAdapter());
				
		this.txtPlayerName.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View clickedView, int arg2, long arg3) {

				LinearLayout layout = (LinearLayout)clickedView;
				TextView txtName = (TextView)layout.getChildAt(1);
				tempClickedName = txtName.getText().toString();
				txtPlayerName.setText(tempClickedName);
			}
		});
	}
	
	private PlayerAdapter buildAdapter() {
		List<HashMap<String,String>> friendList = new ArrayList<HashMap<String,String>>();
		for (Player player : AppContext.getApplication().getDalService().getAllPlayers()) {
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put(UIConstants.PLAYER_PICTURE_URI, player.getPictureUri());
            hm.put(UIConstants.PLAYER_NAME, player.getName());
            friendList.add(hm);
		}
		
		return new PlayerAdapter(this.getContext(), friendList);
	}

	public int getPlayerIndex() {
		return this.playerIndex;
	}

	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	public String getPlayerName() {
		return this.txtPlayerName.getText().toString();
	}

	public void setPlayerName(String playerName) {
		this.txtPlayerName.setText(playerName);
	}
	
    private class PlayerAdapter extends BaseAdapter implements Filterable {
    	
    	private AlphabeticalPlayerNameFilter alphabeticalPlayerNameFilter;
		private List<HashMap<String, String>> filteredFriendList;
		private List<HashMap<String,String>> originalFriendList;
    	private Context context;
    	
    	protected PlayerAdapter(Context context, List<HashMap<String,String>> friendList) {
    		this.context = context;
    		this.originalFriendList = newArrayList(friendList);
    		this.filteredFriendList = friendList;
    		this.alphabeticalPlayerNameFilter = new AlphabeticalPlayerNameFilter();
    	}
    	
		@Override
		public int getCount() {
			return this.filteredFriendList.size();
		}

		@Override
		public Object getItem(int position) {
			return this.filteredFriendList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressWarnings("unchecked")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

		   // TODO Improve for perf issues

//		   View view = convertView;
//		   if (view == null) {
//			   LayoutInflater layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			   view = layoutInflater.inflate(R.layout.autocomplete_layout_pic, parent, false);
//		   }

			HashMap<String,String> playerMap = (HashMap<String,String>)this.getItem(position);
		   
		   View view = null;
		   LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   
		   String playerPictureUri = playerMap.get(UIConstants.PLAYER_PICTURE_URI);
		   if (playerPictureUri != null && playerPictureUri.length() != 0) {
			   
			   Bitmap contactBitmap = null;
			   try {
				   String contactId = Uri.parse(playerMap.get(UIConstants.PLAYER_PICTURE_URI)).getLastPathSegment();
				   contactBitmap = UIHelper.getContactPicture(this.context, contactId);
			   }
			   catch(Exception e) {
				   contactBitmap = null;
			   }
			   
			   // no problem when retrieving image => set it as player image 
			   if (contactBitmap != null) {
				   view = layoutInflater.inflate(R.layout.autocomplete_layout_pic, parent, false);
				   ImageView playerPicture = (ImageView)view.findViewById(R.id.playerPicture);
				   playerPicture.setImageBitmap(contactBitmap);
			   }
		   }

			TextView textView = (TextView)view.findViewById(R.id.username);
		   textView.setText(playerMap.get(UIConstants.PLAYER_NAME));

		   
		   return view;
			
		}

		@Override
		public Filter getFilter() {
			return this.alphabeticalPlayerNameFilter;
		}
		
		private class AlphabeticalPlayerNameFilter extends Filter {
			
		    @Override
		    protected FilterResults performFiltering(CharSequence filterString) {
		        FilterResults results = new FilterResults();
		        if (filterString == null || filterString.length() == 0) {
		            results.values = originalFriendList;
		            results.count = originalFriendList.size();
		        }
		        else {
		        	String name = filterString.toString().toLowerCase();
			        List<HashMap<String,String>> friendsMatching = new ArrayList<HashMap<String,String>>();
			        for (HashMap<String,String> friend : originalFriendList) {
			        	if (friend.get(UIConstants.PLAYER_NAME).toLowerCase().startsWith(name)) {
			        		friendsMatching.add(friend);
			        	}
			        }
			        results.values = friendsMatching;
			        results.count = friendsMatching.size();
		        }

		        return results;
		    }

		    @SuppressWarnings("unchecked")
			@Override
		    protected void publishResults(CharSequence constraint, FilterResults results) {
		        if (results.count == 0) {
		            notifyDataSetInvalidated();
		        }
		        else {
		        	filteredFriendList = (List<HashMap<String,String>>) results.values;
		            notifyDataSetChanged();
		        }
		    }
		}
    }
}