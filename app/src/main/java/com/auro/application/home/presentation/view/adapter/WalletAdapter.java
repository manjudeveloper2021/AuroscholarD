package com.auro.application.home.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.CartWalletItemLayoutBinding;
import com.auro.application.databinding.FragmentCertificateItemBinding;
import com.auro.application.home.data.model.WalletResponseAmountResModel;
import com.auro.application.home.data.model.response.APIcertificate;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ImageUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class WalletAdapter  extends RecyclerView.Adapter<WalletAdapter.WalletHolder> {

    List<WalletResponseAmountResModel> mValues;
    Context mContext;
    CartWalletItemLayoutBinding binding;
    CommonCallBackListner listner;

    public WalletAdapter(Context context, List<WalletResponseAmountResModel> values, CommonCallBackListner listner) {
        mValues = values;
        mContext = context;
        this.listner = listner;
    }

    public void updateList(List<WalletResponseAmountResModel> values) {
        mValues = values;
        notifyDataSetChanged();
    }


    public class WalletHolder extends RecyclerView.ViewHolder {
        CartWalletItemLayoutBinding binding;

        public WalletHolder(CartWalletItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(WalletResponseAmountResModel resModel, int position) {
            binding.backgroundBox.setBackground(resModel.getDrawable());
            binding.amountcost.setText(resModel.getAmount());
            binding.textTitle.setText(resModel.getText());



        }

    }

    @Override
    public WalletAdapter.WalletHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.cart_wallet_item_layout, viewGroup, false);
        return new WalletAdapter.WalletHolder(binding);
    }


    @Override
    public void onBindViewHolder(WalletAdapter.WalletHolder Vholder, int position) {
        Vholder.setData(mValues.get(position), position);




    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void updateData() {

    }

}
