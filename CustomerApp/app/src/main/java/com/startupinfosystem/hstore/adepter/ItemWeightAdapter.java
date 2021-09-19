package com.startupinfosystem.hstore.adepter;

import static com.startupinfosystem.hstore.utils.SessionManager.currncy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.startupinfosystem.hstore.R;
import com.startupinfosystem.hstore.activity.ItemDetailsActivity;
import com.startupinfosystem.hstore.model.Pbonus;
import com.startupinfosystem.hstore.model.Price;
import com.startupinfosystem.hstore.model.ProductItem;
import com.startupinfosystem.hstore.utils.SessionManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ItemWeightAdapter extends RecyclerView.Adapter<ItemWeightAdapter.ViewHolder> {

    Context context;
    List<Price> list;
    List<ProductItem> mData;
    ProductItem dataaa;
    List<Pbonus> pbonuses;
    int lastCheckedPos = -1;
    private onRvWeightItemClickListner rvWeightItemClickListner;
    ItemAdp.ViewHolder pViewHolder;
    ItemDetailsActivity activity;
    SessionManager sessionManager;
    String TAG = this.getClass().getName();
    int productDetailsView = -1;


    public ItemWeightAdapter(int itemsView,Context context, List<Price> list, ItemAdp.ViewHolder pViewHolder, List<ProductItem> mData, List<Pbonus> pbonuses) {
        this.productDetailsView = itemsView;
        this.context = context;
        this.list = list;
        this.pViewHolder = pViewHolder;
        this.mData = mData;
        sessionManager = new SessionManager(context);
        this.pbonuses = pbonuses;
    }

    public ItemWeightAdapter(int productDetailsView, ItemDetailsActivity context, ArrayList<Price> price, ItemDetailsActivity itemDetailsActivity, ProductItem mData, ArrayList<Pbonus> pBonuslist,onRvWeightItemClickListner rvWeightItemClickListner) {
        this.productDetailsView = productDetailsView;
        this.context = context;
        this.list = price;
        this.activity = itemDetailsActivity;
        this.dataaa = mData;
        sessionManager = new SessionManager(context);
        this.pbonuses = pBonuslist;
        this.rvWeightItemClickListner = rvWeightItemClickListner;
    }

    public interface onRvWeightItemClickListner {
        void onItemWeightClickListner(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemweightboxes, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (productDetailsView == 1){
            Price p = list.get(position);
            ItemAdp iad = new ItemAdp();
            ProductItem datum = mData.get(position);
            Pbonus pbonus = pbonuses.get(position);

            holder.weights.setText(p.getProductType());

            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (lastCheckedPos != position) {
                        if (datum.getmDiscount() > 0) {
                            double res = (Double.parseDouble(p.getProductPrice()) / 100.0f) * datum.getmDiscount();
                            res = Double.parseDouble(p.getProductPrice()) - res;
                            pViewHolder.txtItemOffer.setText(sessionManager.getStringData(currncy) + p.getProductPrice());
                            pViewHolder.txtItemOffer.setPaintFlags(pViewHolder.txtItemOffer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            pViewHolder.txtPrice.setText(sessionManager.getStringData(currncy) + new DecimalFormat("##.##").format(res));
                            pViewHolder.txtItemOffer.setText(sessionManager.getStringData(currncy) + p.getProductPrice());
                        } else {
                            pViewHolder.txtItemOffer.setVisibility(View.GONE);
                            pViewHolder.txtPrice.setText(sessionManager.getStringData(currncy) + p.getProductPrice());
                        }
                        iad.setJoinPlayrList(context, pViewHolder.lvlSubitem, datum, p, pbonus);

                        lastCheckedPos = position;
                    } else {
                        lastCheckedPos = -1;
                    }
                    notifyDataSetChanged();
                }
            });

            if (lastCheckedPos == position) {
                holder.weights.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                holder.parent.setBackgroundResource(R.drawable.selected_address_type_review);
            } else {
                holder.weights.setTextColor(ContextCompat.getColor(context, R.color.colorBalck));
                holder.parent.setBackgroundResource(R.drawable.deselected_address_type_review);
            }
        }
        else if (productDetailsView == 2){
            Price p = list.get(position);
            holder.weights.setText(p.getProductType());
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (lastCheckedPos != position) {

                        rvWeightItemClickListner.onItemWeightClickListner(position);
                        lastCheckedPos = position;
                    } else {
                        lastCheckedPos = -1;
                    }
                    notifyDataSetChanged();
                }
            });

            if (lastCheckedPos == position) {
                holder.parent.setBackgroundResource(R.drawable.selected_address_type_review);
                holder.weights.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            } else {
                holder.weights.setTextColor(ContextCompat.getColor(context, R.color.colorBalck));
                holder.parent.setBackgroundResource(R.drawable.deselected_address_type_review);
            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout parent;
        TextView weights;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            weights = itemView.findViewById(R.id.weights_title);
            parent = itemView.findViewById(R.id.parent);
        }

    }
}
