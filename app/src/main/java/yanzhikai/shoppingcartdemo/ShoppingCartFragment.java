package yanzhikai.shoppingcartdemo;


import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.TextView;

import com.aitsuki.swipe.SwipeMenuRecyclerView;

import butterknife.BindView;
import yanzhikai.shoppingcartdemo.widget.SwitchView;


public class ShoppingCartFragment extends BaseFragment implements ShoppingCartContract.IShoppingCartView {
    public static final String TAG = "ShoppingCartFragment";
//    public static final String DATA_LIST = "dataList";

    @BindView(R.id.rv_shopping_cart)
    SwipeMenuRecyclerView rvShoppingCart;
    @BindView(R.id.sw_choose_all)
    SwitchView swChooseAll;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;


    private ShoppingCartPresenter mShoppingCartPresenter;
    private ShoppingCartAdapter mShoppingCartAdapter;

    public ShoppingCartFragment() {

    }

    //预加载参数可使用
//    public static ShoppingCartFragment newInstance(ArrayList<CommodityEntity> param1) {
//        ShoppingCartFragment fragment = new ShoppingCartFragment();
//        Bundle args = new Bundle();
//        args.putParcelableArrayList(DATA_LIST, (ArrayList<? extends Parcelable>) param1);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void loadData() {
        mShoppingCartPresenter = new ShoppingCartPresenter(this);
        mShoppingCartPresenter.bindData();
        Log.d(TAG, "loadData: ");
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_shopping_cart;
    }

    @Override
    protected void initViews() {
        rvShoppingCart.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.d(TAG, "initViews: ");
        //设置分界线
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvShoppingCart.addItemDecoration(itemDecoration);
    }

    @Override
    public void bindData(ShoppingCartEntity entity) {
        mShoppingCartAdapter = new ShoppingCartAdapter(entity);
        mShoppingCartAdapter.setOnItemClickListener(new ShoppingCartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onRightMenuClick(int position) {
                Log.d(TAG, "onRightMenuClick: " + position);
                mShoppingCartPresenter.deleteCommodity(position);
            }
        });

        mShoppingCartAdapter.setOnDataChangedListener(new ShoppingCartAdapter.OnDataChangedListener() {
            @Override
            public void onChosenChanged() {
                Log.d(TAG, "onChosenChanged: ");
                mShoppingCartPresenter.dataStateChanged();
            }

            @Override
            public void onDataIsNull(boolean isNull) {
                swChooseAll.setOpened(false);
                swChooseAll.setCanOpen(!isNull);
            }
        });
        rvShoppingCart.setAdapter(mShoppingCartAdapter);
        refreshBottomUI(entity.isIsChosenAll(),entity.getTotalPrice());
    }

    @Override
    public void refreshData(int index) {
        mShoppingCartAdapter.notifyDataSetChanged();
    }

    @Override
    public void chooseAll() {
//        mShoppingCartPresenter.
    }

    @Override
    public void refreshBottomUI(boolean isChosenAll, float totalPrice) {
        Log.d(TAG, "refreshBottomUI: ");
        swChooseAll.setOpened(isChosenAll);
        tvTotalPrice.setText(String.format(getString(R.string.shopping_cart_price_total), NumberUtil.floatToStringWith1Bit(totalPrice)));
    }

}
