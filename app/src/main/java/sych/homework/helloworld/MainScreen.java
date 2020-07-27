package sych.homework.helloworld;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import sych.homework.helloworld.MoneyAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class MainScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        RecyclerView recyclerView = findViewById(R.id.costRecyclerView);
        MoneyAdapter moneyAdapter = new MoneyAdapter();

        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new BudgetPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(R.string.Expenses);
        tabLayout.getTabAt(1).setText(R.string.Income);
        tabLayout.getTabAt(2).setText(R.string.Balance);




        moneyAdapter.setMoneyAdapterClick(new MoneyAdapterClick() {
            @Override
            public void onMoneyClick(MoneyCellModel moneyCellModel) {
                Intent intent = new Intent(getApplicationContext(), WelcomeScreen.class);
                startActivity(intent);
            }
        });
        moneyAdapter.setData(generateIncomes());
        recyclerView.setAdapter(moneyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false));

    }

    static class BudgetPagerAdapter extends FragmentPagerAdapter {

        public BudgetPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(final int position){
            return new BudgetFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }
    }



    private List<MoneyCellModel> generateExpenses() {
        List<MoneyCellModel> moneyCellModels = new ArrayList<>();
        moneyCellModels.add(new MoneyCellModel("Молоко", "70 P", R.color.expenceColor));
        moneyCellModels.add(new MoneyCellModel("Зубная щетка", "70 P", R.color.expenceColor));
        moneyCellModels.add(new MoneyCellModel("Сковородка с антипригарным покрытием",
                "1670 P", R.color.expenceColor));

        return moneyCellModels;

    }
    private List<MoneyCellModel> generateIncomes(){
        List<MoneyCellModel> moneyCellModels = new ArrayList<>();
        moneyCellModels.add(new MoneyCellModel("Зарплата.Июнь", "70000 ₽ P", R.color.expenceColor));
        moneyCellModels.add(new MoneyCellModel("Премия", "7000 ₽ P", R.color.expenceColor));
        moneyCellModels.add(new MoneyCellModel("Олег наконец-то\n" +
                "вернул долг",
                "300000 ₽", R.color.expenceColor));

        return moneyCellModels;
    }
}