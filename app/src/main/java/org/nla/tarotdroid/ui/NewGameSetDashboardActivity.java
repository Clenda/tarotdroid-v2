package org.nla.tarotdroid.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.ui.constants.ActivityParams;
import org.nla.tarotdroid.ui.controls.ThumbnailItem;

import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;

import static com.google.common.collect.Lists.newArrayList;

public class NewGameSetDashboardActivity extends BaseActivity {

    @BindView(R.id.listView) protected ListView listView;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
            // set excuse as background image
            listView.setCacheColorHint(0);
            listView.setBackgroundResource(R.drawable.img_excuse);
            initializeViews();
        }
        catch (Exception e) {
        	AuditHelper.auditError(AuditHelper.ErrorTypes.newGameSetDashboardActivityError, e, this);
        }
    }

	@Override
    protected void inject() {

	}

    @Override
    protected void auditEvent() {
        AuditHelper.auditEvent(AuditHelper.EventTypes.displayNewGameSetDashboardPage);
	}

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_new_gameset_dashboard;
    }

    @Override
    protected int getTitleResId() {
        return R.string.lblMainActivityTitle;
    }

    private void initializeViews() {
        DashboardOption newTarot3Option = new DashboardOption(
				R.drawable.icon_3players, 
				R.string.lblNewTarot3, 
				R.string.lblNewTarot3Description, 
				R.id.new_tarot3_gameset_item
		);
		DashboardOption newTarot4Option = new DashboardOption(
				R.drawable.icon_4players, 
				R.string.lblNewTarot4, 
				R.string.lblNewTarot4Description, 
				R.id.new_tarot4_gameset_item
		);
		DashboardOption newTarot5Option = new DashboardOption(
				R.drawable.icon_5players, 
				R.string.lblNewTarot5, 
				R.string.lblNewTarot5Description, 
				R.id.new_tarot5_gameset_item
		);
		
		List<DashboardOption> options = newArrayList();
		options.add(newTarot3Option);
		options.add(newTarot4Option);
		options.add(newTarot5Option);
		listView.setAdapter(new DashboardOptionAdapter(this, options));
	}

    @OnItemClick(R.id.listView)
    protected void onListItemClick(AdapterView<?> parent, int position) {
        DashboardOption option = (DashboardOption) listView.getAdapter().getItem(position);
        GameStyleType gameStyleType = null;
        int tagValue = ((Integer) option.getTag()).intValue();
        if (tagValue == R.id.new_tarot3_gameset_item) {
            gameStyleType = GameStyleType.Tarot3;
        } else if (tagValue == R.id.new_tarot4_gameset_item) {
            gameStyleType = GameStyleType.Tarot4;
        } else { // tagValue == R.id.new_tarot5_gameset_item
            gameStyleType = GameStyleType.Tarot5;
        }

        Intent intent = new Intent(this, PlayerSelectorActivity.class);
        intent.putExtra(ActivityParams.PARAM_TYPE_OF_GAMESET, gameStyleType.toString());
        startActivity(intent);
        finish();
    }

    private class DashboardOptionAdapter extends ArrayAdapter<DashboardOption> {

		public DashboardOptionAdapter(Context context, List<DashboardOption> objects) {
            super(context, R.layout.thumbnail_item, objects);
        }

        @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
			DashboardOption option = this.getItem(position);
			ThumbnailItem thumbnailItem = new ThumbnailItem(this.getContext(), option.getDrawableId(), option.getTitleResourceId(), option.getContentResourceId());
	        return thumbnailItem;
	    }
	}
}