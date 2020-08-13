package sych.homework.helloworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static sych.homework.helloworld.R.*;

public class BudgetFragment extends Fragment implements ItemsAdapterListener, ActionMode.Callback {

    public static final int REQUEST_CODE = 100;
    private static final String COLOR_ID = "colorId";
    private static final String TYPE = "fragmentType";

    private ItemsAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ActionMode mActionMode;

    private Api mApi;

    public static BudgetFragment newInstance(final int colorId, final String type) {
        BudgetFragment budgetFragment = new BudgetFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(COLOR_ID, colorId);
        bundle.putString(TYPE, type);
        budgetFragment.setArguments(bundle);
        return budgetFragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = ((LoftApp)getActivity().getApplication()).getApi();
        loadItems();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState
    ) {
        View view = inflater.inflate(layout.fragment_budget, null);

        RecyclerView recyclerView = view.findViewById(id.budget_item_list);
        mSwipeRefreshLayout = view.findViewById(id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadItems();
            }
        });

        mAdapter = new ItemsAdapter(getArguments().getInt(COLOR_ID));
        mAdapter.setListener(this);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onActivityResult(
            final int requestCode, final int resultCode, @Nullable final Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            int price;
            try {
                price = Integer.parseInt(data.getStringExtra("price"));
            } catch (NumberFormatException e) {
                price = 0;
            }
            final int realPrice = price;
            final String name = data.getStringExtra("name");

            final String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(MainActivity.TOKEN, "");

            Call<Status> call = mApi.addItem(new AddItemRequest(name, getArguments().getString(TYPE), price), token);
            call.enqueue(new Callback<Status>() {

                @Override
                public void onResponse(
                        final Call<Status> call, final Response<Status> response
                ) {
                    if (response.body().getStatus().equals("success")) {
                        loadItems();
                    }
                }

                @Override
                public void onFailure(final Call<Status> call, final Throwable t) {
                    t.printStackTrace();
                }
            });

        }
    }



    public void loadItems() {
        final String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(MainActivity.TOKEN, "");

        Call<List<Item>> items = mApi.getItems(getArguments().getString(TYPE), token);
        items.enqueue(new Callback<List<Item>>() {

            @Override
            public void onResponse(
                    final Call<List<Item>> call, final Response<List<Item>> response
            ) {
                mAdapter.clearItems();
                mSwipeRefreshLayout.setRefreshing(false);
                List<Item> items = response.body();
                for (Item item : items) {
                    mAdapter.addItem(item);
                }
                ((MainActivity)getActivity()).loadBalance();
            }

            @Override
            public void onFailure(final Call<List<Item>> call, final Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void onItemClick(final Item item, final int position) {
        mAdapter.clearItem(position);
        if (mActionMode != null) {
            mActionMode.setTitle(getString(string.selected, String.valueOf(mAdapter.getSelectedSize())));
        }
    }

    @Override
    public void onItemLongClick(final Item item, final int position) {
        if (mActionMode == null) {
            getActivity().startActionMode(this);
        }
        mAdapter.toggleItem(position);
        if (mActionMode != null) {
            mActionMode.setTitle(getString(string.selected, String.valueOf(mAdapter.getSelectedSize())));
        }
    }

    @Override
    public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
        mActionMode = actionMode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
        MenuInflater menuInflater = new MenuInflater(getActivity());
        menuInflater.inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.remove) {
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.confirmation)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialogInterface, final int i) {
                            removeItems();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialogInterface, final int i) {

                        }
                    }).show();
        }
        return true;
    }

    private void removeItems() {
        String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(MainActivity.TOKEN, "");
        List<Integer> selectedItems = mAdapter.getSelectedItemIds();
        for (Integer itemId : selectedItems) {
            Call<Status> call = mApi.removeItem(String.valueOf(itemId.intValue()), token);
            call.enqueue(new Callback<Status>() {

                @Override
                public void onResponse(
                        final Call<Status> call, final Response<Status> response
                ) {
                    loadItems();
                    mAdapter.clearSelections();
                }

                @Override
                public void onFailure(final Call<Status> call, final Throwable t) {

                }
            });
        }
    }

    @Override
    public void onDestroyActionMode(final ActionMode actionMode) {
        mActionMode = null;
        mAdapter.clearSelections();
    }
}
