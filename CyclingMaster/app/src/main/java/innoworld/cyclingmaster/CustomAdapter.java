package innoworld.cyclingmaster;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sesar on 05/01/2016.
 */



public class CustomAdapter extends BaseAdapter {
    Context context;
    List<RowItem> rowItems;

    CustomAdapter(Context context, List<RowItem> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    /* private view holder class */
    private class ViewHolder {
        TextView rank;
        ImageView profile_pic;
        TextView member_name;
        TextView totalride;
        ImageView badge;
        TextView xp;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();

            holder.rank = (TextView) convertView
                    .findViewById(R.id.rank);
            holder.member_name = (TextView) convertView
                    .findViewById(R.id.member_name);
            holder.profile_pic = (ImageView) convertView
                    .findViewById(R.id.profile_pic);
            holder.totalride = (TextView) convertView
                    .findViewById(R.id.totalride);
            holder.badge = (ImageView) convertView
                    .findViewById(R.id.badge);
            holder.xp = (TextView) convertView
                    .findViewById(R.id.xp);

            RowItem row_pos = rowItems.get(position);

            holder.profile_pic.setImageResource(row_pos.getProfile_pic_id());
            holder.rank.setText(row_pos.getRank());
            holder.member_name.setText(row_pos.getMember_name());
            holder.totalride.setText(row_pos.gettotalride());
            holder.badge.setImageResource(row_pos.getBadge());
            holder.xp.setText(row_pos.getxp());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
}
