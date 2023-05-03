package com.auro.application.home.presentation.view.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.FaqCategoryViewallItemLayoutBinding;
import com.auro.application.databinding.FaqQuesCategoryItemLayoutBinding;
import com.auro.application.home.data.FaqCatData;
import com.auro.application.util.AppUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FaqCategoryViewAllAdapter extends RecyclerView.Adapter<FaqCategoryViewAllAdapter.CategoryHolder> {
    boolean isSelectedAll = true;
    List<FaqCatData> list;
    List<FaqCatData> mlist = new ArrayList<>();
    CommonCallBackListner commonCallBackListner;
   Context mcontext;
    String faqcategoryid2;
    public FaqCategoryViewAllAdapter(Context mContext, List<FaqCatData> list, CommonCallBackListner commonCallBackListner) {
        this.list = list;
        this.mcontext = mContext;
        this.commonCallBackListner=commonCallBackListner;

    }

    @NonNull
    @Override
    public FaqCategoryViewAllAdapter.CategoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        FaqCategoryViewallItemLayoutBinding languageItemLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.faq_category_viewall_item_layout, viewGroup, false);
        return new FaqCategoryViewAllAdapter.CategoryHolder(languageItemLayoutBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull FaqCategoryViewAllAdapter.CategoryHolder holder, int position) {
        holder.bindUser(list.get(position), position);
    }

    @Override
    public int getItemCount() {

            return list.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        FaqCategoryViewallItemLayoutBinding binding;

        public CategoryHolder(@NonNull FaqCategoryViewallItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bindUser(FaqCatData model, int position) {
            binding.tvoption.setText(list.get(position).getTranslateCategoryName());

            if (!isSelectedAll){
                binding.checkIcon.setChecked(false);
            }


            binding.checkIcon.setChecked(model.isChecked());



            binding.checkIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (binding.checkIcon.isChecked()){
                         commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.ITEM_CLICK, list.get(position)));
                    }
                    else{
                        binding.checkIcon.setChecked(false);
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.ITEM_LONG_CLICK, list.get(position)));
                    }
                }
            });
        }
    }

    public void unselectall(){
        isSelectedAll=false;
        notifyDataSetChanged();
    }


}