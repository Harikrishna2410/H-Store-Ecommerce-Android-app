package com.startupinfosystem.hstore.adepter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.startupinfosystem.hstore.R;
import com.startupinfosystem.hstore.activity.ItemDetailsActivity;
import com.startupinfosystem.hstore.database.DatabaseHelper;
import com.startupinfosystem.hstore.database.MyCart;
import com.startupinfosystem.hstore.model.Pbonus;
import com.startupinfosystem.hstore.model.Price;
import com.startupinfosystem.hstore.model.ProductItem;
import com.startupinfosystem.hstore.retrofit.APIClient;
import com.startupinfosystem.hstore.utils.SessionManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.startupinfosystem.hstore.fragment.ItemListFragment.itemListFragment;
import static com.startupinfosystem.hstore.utils.SessionManager.currncy;

public class ItemAdp extends RecyclerView.Adapter<ItemAdp.ViewHolder> {
    private List<ProductItem> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context mContext;
    SessionManager sessionManager;
    ProductItem datum;
    ViewHolder v;
    private int newitem;
    String TAG = this.getClass().getName();

    public ItemAdp() {
    }

    public ItemAdp(Context context, List<ProductItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        sessionManager = new SessionManager(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_custome, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        v = holder;
        datum = mData.get(position);
        if (datum.getStock() == 0) {
            holder.lvlOutofstock.setVisibility(View.VISIBLE);
        } else {
            holder.lvlOutofstock.setVisibility(View.GONE);

        }
        Glide.with(mContext).load(APIClient.baseUrl + "/" + datum.getProductImage()).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).into(holder.imgIcon);
        holder.txtTitle.setText("" + datum.getProductName());
        holder.imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ItemDetailsActivity.class).putExtra("MyClass", datum).putParcelableArrayListExtra("MyList", datum.getPrice()).putParcelableArrayListExtra("MyList1", datum.getPbonus()));
            }
        });
        if (datum.getmDiscount() > 0) {
            holder.lvlOffer.setVisibility(View.VISIBLE);
            holder.txtOffer.setText(datum.getmDiscount() + "% Off");
        } else {
            holder.lvlOffer.setVisibility(View.GONE);
        }
/*
        List<String> arrayList = new ArrayList<>();
        for (int i = 0; i < datum.getPrice().size(); i++) {

            arrayList.add(datum.getPrice().get(i).getProductType());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mContext, R.layout.spinner_layout, arrayList);
*/

        ItemWeightAdapter adapter = new ItemWeightAdapter(1,mContext, datum.getPrice(),holder,mData,mData.get(position).getPbonus());
        holder.rv_weight_items.setAdapter(adapter);
        holder.rv_weight_items.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.lvlSubitem.setVisibility(View.VISIBLE);
       /* dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        holder.spinner.setAdapter(dataAdapter);
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (datum.getmDiscount() > 0) {
                    double res = (Double.parseDouble(datum.getPrice().get(position).getProductPrice()) / 100.0f) * datum.getmDiscount();
                    res = Double.parseDouble(datum.getPrice().get(position).getProductPrice()) - res;
                    holder.txtItemOffer.setText(sessionManager.getStringData(currncy) + datum.getPrice().get(position).getProductPrice());
                    holder.txtItemOffer.setPaintFlags(holder.txtItemOffer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.txtPrice.setText(sessionManager.getStringData(currncy) + new DecimalFormat("##.##").format(res));
                    holder.txtItemOffer.setText(sessionManager.getStringData(currncy) + datum.getPrice().get(position).getProductPrice());
                } else {
                    holder.txtItemOffer.setVisibility(View.GONE);
                    holder.txtPrice.setText(sessionManager.getStringData(currncy) + datum.getPrice().get(position).getProductPrice());
                }
                setJoinPlayrList(mContext,holder.lvlSubitem, datum, datum.getPrice().get(position), datum.getPbonus().get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

  /*  @Override
    public void onItemWeightClickListner(int position) {
        position = newitem;
        Log.i(TAG,String.valueOf(position));
        if (datum.getmDiscount() > 0) {
            double res = (Double.parseDouble(datum.getPrice().get(position).getProductPrice()) / 100.0f) * datum.getmDiscount();
            res = Double.parseDouble(datum.getPrice().get(position).getProductPrice()) - res;
            v.txtItemOffer.setText(sessionManager.getStringData(currncy) + datum.getPrice().get(position).getProductPrice());
            v.txtItemOffer.setPaintFlags(v.txtItemOffer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            v.txtPrice.setText(sessionManager.getStringData(currncy) + new DecimalFormat("##.##").format(res));
            v.txtItemOffer.setText(sessionManager.getStringData(currncy) + datum.getPrice().get(position).getProductPrice());
        } else {
            v.txtItemOffer.setVisibility(View.GONE);
            v.txtPrice.setText(sessionManager.getStringData(currncy) + datum.getPrice().get(position).getProductPrice());
        }
        setJoinPlayrList(v.lvlSubitem, datum, datum.getPrice().get(position), datum.getPbonus().get(position));
//        notifyDataSetChanged();
//        notifyDataSetChanged();
    }
*/

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txt_offer)
        TextView txtOffer;
        @BindView(R.id.txt_item_offer)
        TextView txtItemOffer;
        @BindView(R.id.txt_price)
        TextView txtPrice;
        @BindView(R.id.lvl_subitem)
        LinearLayout lvlSubitem;
        @BindView(R.id.lvl_offer)
        LinearLayout lvlOffer;
        @BindView(R.id.lvl_outofstock)
        LinearLayout lvlOutofstock;
        @BindView(R.id.spinner)
        Spinner spinner;
        @BindView(R.id.img_icon)
        ImageView imgIcon;
        @BindView(R.id.rv_weight_items)
        RecyclerView rv_weight_items;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setJoinPlayrList(Context context,LinearLayout lnrView, ProductItem datum, Price price, Pbonus pbonus) {
        lnrView.removeAllViews();
        final int[] count = {0};
        DatabaseHelper helper = new DatabaseHelper(lnrView.getContext());
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custome_prize, null);
        TextView txtcount = view.findViewById(R.id.txtcount);
        LinearLayout lvlAddremove = view.findViewById(R.id.lvl_addremove);
        LinearLayout lvlAddcart = view.findViewById(R.id.lvl_addcart);
        lvlAddcart.setVisibility(View.VISIBLE);
        LinearLayout imgMins = view.findViewById(R.id.img_mins);
        LinearLayout imgPlus = view.findViewById(R.id.img_plus);
        MyCart myCart = new MyCart();
        myCart.setPid(datum.getId());
        myCart.setImage(datum.getProductImage());
        myCart.setTitle(datum.getProductName());
        myCart.setWeight(price.getProductType());
        myCart.setCost(price.getProductPrice());
        myCart.setBonus(pbonus.getProductBonus());
        myCart.setDiscount(datum.getmDiscount());
        int qrt = helper.getCard(myCart.getPid(), myCart.getCost());
        if (qrt != -1) {
            count[0] = qrt;
            txtcount.setText("" + count[0]);
            lvlAddremove.setVisibility(View.VISIBLE);
            lvlAddcart.setVisibility(View.GONE);
        } else {
            lvlAddremove.setVisibility(View.GONE);
            lvlAddcart.setVisibility(View.VISIBLE);

        }
        imgMins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count[0] = Integer.parseInt(txtcount.getText().toString());

                count[0] = count[0] - 1;
                if (count[0] <= 0) {
                    txtcount.setText("" + count[0]);
                    lvlAddremove.setVisibility(View.GONE);
                    lvlAddcart.setVisibility(View.VISIBLE);
                    helper.deleteRData(myCart.getPid(), myCart.getCost());
                } else {
                    txtcount.setVisibility(View.VISIBLE);
                    txtcount.setText("" + count[0]);
                    myCart.setQty(String.valueOf(count[0]));
                    helper.insertData(myCart);
                }
                itemListFragment.updateItem();
            }
        });

        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count[0] = Integer.parseInt(txtcount.getText().toString());
                count[0] = count[0] + 1;
                txtcount.setText("" + count[0]);
                myCart.setQty(String.valueOf(count[0]));
                Log.e("INsert", "--> " + helper.insertData(myCart));
                itemListFragment.updateItem();
            }
        });
        lvlAddcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvlAddcart.setVisibility(View.GONE);
                lvlAddremove.setVisibility(View.VISIBLE);
                count[0] = Integer.parseInt(txtcount.getText().toString());
                count[0] = count[0] + 1;
                txtcount.setText("" + count[0]);
                myCart.setQty(String.valueOf(count[0]));
                Log.e("INsert", "--> " + helper.insertData(myCart));
                itemListFragment.updateItem();
            }
        });
        lnrView.addView(view);

    }

}