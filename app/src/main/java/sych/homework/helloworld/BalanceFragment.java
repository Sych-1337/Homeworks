package sych.homework.helloworld;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BalanceFragment extends Fragment {

    private Api mApi;
    private TextView myExpences;
    private TextView myIncome;
    private TextView totalFinances;
    private DiagramView mDiagramView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static BalanceFragment newInstance() {
        return new BalanceFragment();
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApi = ((LoftApp) getActivity().getApplication()).getApi();
        loadBalance();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_balance, null);
        myExpences = view.findViewById(R.id.my_expences);
        myIncome = view.findViewById(R.id.my_income);
        totalFinances = view.findViewById(R.id.total_finances);
        mDiagramView = view.findViewById(R.id.diagram_view);
        mSwipeRefreshLayout = view.findViewById(R.id.balance_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadBalance();
            }
        });
        return view;
    }

    public void loadBalance() {
        String token = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(MainActivity.TOKEN, "");
        final Call<BalanceResponce> responceCall = mApi.getBalance(token);
        responceCall.enqueue(new Callback<BalanceResponce>() {

            @Override
            public void onResponse(
                    final Call<BalanceResponce> call, final Response<BalanceResponce> response
            ) {

                final float totalExpences = response.body().getTotalExpences();
                final float totalIncome = response.body().getTotalIncome();

                myExpences.setText(String.valueOf(totalExpences));
                myIncome.setText(String.valueOf(totalIncome));
                totalFinances.setText(String.valueOf(totalIncome - totalExpences));
                mDiagramView.update(totalExpences, totalIncome);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(final Call<BalanceResponce> call, final Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
