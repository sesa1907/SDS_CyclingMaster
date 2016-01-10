package innoworld.cyclingmaster;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sesar on 30/11/2015.
 */
public class Leaderboard extends ListFragment {

    String[] rank;
    String[] member_names;
    TypedArray profile_pics;
    String[] totalride;
    TypedArray badge;
    String[] xp;
    List<RowItem> rowItems;
    ListView mylistview;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_leaderboard, container, false);
        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rowItems = new ArrayList<RowItem>();

        rank = getResources().getStringArray(R.array.rank);
        member_names = getResources().getStringArray(R.array.Member_names);
        profile_pics = getResources().obtainTypedArray(R.array.profile_pics);
        totalride = getResources().getStringArray(R.array.totalride);
        badge = getResources().obtainTypedArray(R.array.badge);
        xp = getResources().getStringArray(R.array.xp);

        for (int i = 0; i < member_names.length; i++) {
            RowItem item = new RowItem(rank[i],member_names[i],
                    profile_pics.getResourceId(i, -1), totalride[i],
                    badge.getResourceId(i, -1),xp[i]);
            rowItems.add(item);
        }
        CustomAdapter adapter = new CustomAdapter(getActivity(), rowItems);
        setListAdapter(adapter);
        profile_pics.recycle();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
}
