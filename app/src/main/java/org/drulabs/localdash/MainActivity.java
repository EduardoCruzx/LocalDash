package org.drulabs.localdash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TextView;

import org.drulabs.localdash.db.DBAdapter;
import org.drulabs.localdash.model.CardModel;
import org.drulabs.localdash.model.DealerModel;
import org.drulabs.localdash.model.PlayerModel;
import org.drulabs.localdash.transfer.TransferConstants;
import org.drulabs.localdash.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private ArrayList<CardModel> myHand;
    private RecyclerView recyclerView;
    private Button hand, play;
    private DBAdapter dbAdapter = null;
    private DealerModel dealer = null;
    public static final String KEY_CHATTING_WITH = "chattingwith";
    public static final String KEY_CHAT_IP = "chatterip";
    public static final String KEY_CHAT_PORT = "chatterport";
    private PlayerModel me;
    private TabLayout tabLayout;
    private SectionListCardAdapter handAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if(bar != null) {
            bar.hide();
        }

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        Bundle extras = getIntent().getExtras();
        String myName = Utility.getString(getApplication(), TransferConstants.KEY_USER_NAME);
        String myIP = Utility.getString(getApplication(), TransferConstants.KEY_MY_IP);
        int myPort = Utility.getInt(getApplication(), TransferConstants.KEY_PORT_NUMBER);
        if(myIP == null)
            myIP = "127.0.0.1";

        me = new PlayerModel(myName, myIP, myPort);

        dbAdapter = DBAdapter.getInstance(getApplicationContext());
        dealer = new DealerModel(dbAdapter);
        dealer.startGame(extras, me);
        tabLayout.addTab(tabLayout.newTab().setText(me.getName()));
        for(PlayerModel p : dealer.getTurnOrder()){
            if (!p.equals(me))
                tabLayout.addTab(tabLayout.newTab().setText(p.getName()));
        }

        play = (Button) findViewById(R.id.btn_play);
//        play.setVisibility(View.GONE);
        hand = (Button) findViewById(R.id.btn_hand);
        hand.setVisibility(View.VISIBLE);
        hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView.getVisibility() == View.GONE){
                    recyclerView.setVisibility(View.VISIBLE);
                }else {
                    recyclerView.setVisibility(View.GONE);
                }

            }
        });

//        if (dealer.getTurn().getIp().equals(me.getIp()))
//            play.setVisibility(View.VISIBLE);
//        else
//            play.setVisibility(View.GONE);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardModel enemy = dealer.kickTheDoor();
                Intent battleIntent = new Intent(MainActivity.this, BattleActivity.class);
                battleIntent.putExtra("enemy", enemy);
                battleIntent.putExtra("player", me);
                startActivityForResult(battleIntent,3);
            }
        });
        recyclerView = findViewById(R.id.recycler_view_list);
        handAdapter = new SectionListCardAdapter(me.getHand(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(handAdapter);
        recyclerView.setVisibility(View.GONE);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.setCount(tabLayout.getTabCount());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }


    // SectionListCardAdapter adiciona carta ao segurar
    public void equipItem(CardModel item){
        int pos = dealer.equipItem(item, me);
        handAdapter.notifyItemRemoved(pos);
        mSectionsPagerAdapter.notifyFragmentAdapter(me);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(data != null)
            if(resultCode == RESULT_OK && requestCode == 3){
                me = (PlayerModel) data.getSerializableExtra("player");
                CardModel enemy = (CardModel) data.getSerializableExtra("enemy");
                if(dealer.kill(enemy, me)) {
                    handAdapter = new SectionListCardAdapter(me.getHand(), this);
                    recyclerView.setAdapter(handAdapter);
                    handAdapter.notifyDataSetChanged();
                    mSectionsPagerAdapter.notifyFragmentAdapter(me);
                    if(dealer.checkWinner(me)){
                        Intent intent = new Intent(MainActivity.this, WinnerActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            if (Build.VERSION.SDK_INT >= 19) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            } else {
                if (Build.VERSION.SDK_INT > 10) {
                    findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                }
            }

    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private int count;
        ArrayList<PlaceholderFragment> fragments = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            PlaceholderFragment fragment = PlaceholderFragment.newInstance(position + 1, dealer.getTurnOrder().get(position));
            fragments.add(fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.

            return this.count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void notifyFragmentAdapter(PlayerModel player){
            for(PlaceholderFragment f : fragments){
                if (f.getPlayer().getIp().equals(player.getIp()))
                    f.notifyAdapter(player);
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        private SectionListCardAdapter itemsAdapter;
        private RecyclerView recyclerView;
        private PlayerModel player;
        private TextView level, power;

        public PlayerModel getPlayer() {
            return player;
        }

        public static PlaceholderFragment newInstance(int sectionNumber, PlayerModel player) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putSerializable("player", player);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        public void notifyAdapter(PlayerModel player){
            if (this.player != null)
                this.player = player;
            itemsAdapter = new SectionListCardAdapter(player.getItensInPlay(), this.getContext());
            recyclerView.setAdapter(itemsAdapter);
            itemsAdapter.notifyDataSetChanged();
            level.setText(String.valueOf(player.getLevel()));
            power.setText(String.valueOf(player.getPower()));
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            player = (PlayerModel) this.getArguments().getSerializable("player");

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_list);
            level = rootView.findViewById(R.id.level);
            power = rootView.findViewById(R.id.power);
            level.setText(String.valueOf(player.getLevel()));
            power.setText(String.valueOf(player.getPower()));

            itemsAdapter = new SectionListCardAdapter(player.getItensInPlay(), this.getContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(itemsAdapter);

            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            itemsAdapter = new SectionListCardAdapter(player.getItensInPlay(), this.getContext());
            recyclerView.setAdapter(itemsAdapter);
            itemsAdapter.notifyDataSetChanged();
        }
    }
}
