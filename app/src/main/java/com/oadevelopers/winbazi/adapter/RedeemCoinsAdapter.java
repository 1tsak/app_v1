package com.oadevelopers.winbazi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import com.oadevelopers.winbazi.R;
import com.oadevelopers.winbazi.activity.MyWalletActivity;
import com.oadevelopers.winbazi.common.Config;
import com.oadevelopers.winbazi.model.PayoutPojo;
import com.oadevelopers.winbazi.views.TextView_Lato;

public class RedeemCoinsAdapter extends RecyclerView.Adapter<RedeemCoinsAdapter.ViewHolder> {

    private Context context;
    private List<PayoutPojo> payoutList;

    public RedeemCoinsAdapter(List<PayoutPojo> payoutList, Context context) {
        super();
        this.payoutList = payoutList;
        this.context = context;
    }

    @Override
    public RedeemCoinsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_redeem, parent, false);
        RedeemCoinsAdapter.ViewHolder viewHolder = new RedeemCoinsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RedeemCoinsAdapter.ViewHolder holder, int position) {
        final PayoutPojo payoutPojo = payoutList.get(position);

        holder.tnName.setText(payoutPojo.getTitle());
        holder.tncat.setText(payoutPojo.getSubtitle());
        holder.amount.setText(payoutPojo.getAmount() + " " + payoutPojo.getCurrency());

        Glide.with(context).load(Config.FILE_PATH_URL + payoutPojo.getImage())
                .apply(new RequestOptions().override(120, 120))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .into(holder.image);

        holder.SingleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyWalletActivity) context).Redeem(payoutPojo.getTitle(), payoutPojo.getSubtitle(), payoutPojo.getMessage(), payoutPojo.getAmount(), payoutPojo.getCoins(), payoutPojo.getId(), payoutPojo.getStatus(), payoutPojo.getImage(), payoutPojo.getType(), payoutPojo.getCurrency());
            }
        });

    }


    @Override
    public int getItemCount() {
        return payoutList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView_Lato date, tnName, tncat, amount;
        CircleImageView image;
        LinearLayout SingleItem;

        public ViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            tnName = itemView.findViewById(R.id.tnName);
            tncat = itemView.findViewById(R.id.tnType);
            amount = itemView.findViewById(R.id.amount);
            image = itemView.findViewById(R.id.image);
            SingleItem = itemView.findViewById(R.id.SingleItem);
        }

    }
}
