package com.auro.application.home.presentation.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.SubjectsPrefItemLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.response.CategorySubjectResModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ViewUtil;

import java.util.List;

public class SubjectPrefAdapter extends RecyclerView.Adapter<SubjectPrefAdapter.SubjectPrefHolder> {

    List<CategorySubjectResModel> list;
    CommonCallBackListner commonCallBackListner;
    Details details;

    public SubjectPrefAdapter(List<CategorySubjectResModel> list, CommonCallBackListner commonCallBackListner) {
        this.list = list;
        this.commonCallBackListner = commonCallBackListner;
    }

    @NonNull
    @Override
    public SubjectPrefHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        SubjectsPrefItemLayoutBinding languageItemLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.subjects_pref_item_layout, viewGroup, false);
        return new SubjectPrefHolder(languageItemLayoutBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull SubjectPrefHolder holder, @SuppressLint("RecyclerView") int index) {
        CategorySubjectResModel resModel = list.get(index);
        AppLogger.e("onBindViewHolder --", "step 0--" + resModel.isLock());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!resModel.isLock()) {
                    AppLogger.e("onBindViewHolder --", "step 1--" + resModel.isLock());
                    updateList(index);
                }
            }
        });

        holder.bindUser(list.get(index), index, commonCallBackListner);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SubjectPrefHolder extends RecyclerView.ViewHolder {
        SubjectsPrefItemLayoutBinding binding;

        public SubjectPrefHolder(@NonNull SubjectsPrefItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bindUser(CategorySubjectResModel model, int position, CommonCallBackListner commonCallBackListner) {
            binding.RPTextViewTitle.setText(model.getSubjectname());
            binding.mainParentLayout.setBackground(model.getBackgroundImage());
            if (model.isLock()) {
                binding.lockLayout.setBackground(AuroApp.getAppContext().getDrawable(R.drawable.disable_background));
                binding.lockLayout.setVisibility(View.VISIBLE);
                binding.checkIcon.setImageDrawable(AuroApp.getAppContext().getDrawable(R.drawable.ic_auro_check_disable));
            } else {
                binding.checkIcon.setImageDrawable(AuroApp.getAppContext().getDrawable(R.drawable.ic_auro_check));
                binding.lockLayout.setVisibility(View.GONE);
            }
            if (model.isSelected()) {
                binding.checkIcon.setVisibility(View.VISIBLE);
            } else {
                binding.checkIcon.setVisibility(View.GONE);
            }

        }
    }

    public void setData(List<CategorySubjectResModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    void updateList(int index) {
        int count = 0;
        for (CategorySubjectResModel resModel : list) {
            if (resModel.isSelected()) {
                count++;
            }
        }

        for (int i = 0; i < list.size(); i++) {
            boolean val = list.get(index).isSelected();
            if (i == index) {
                if (val) {
                    list.get(index).setSelected(false);
                    if (commonCallBackListner != null) {
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(index, Status.REMOVE_ITEM, list.get(index)));
                    }
                } else {
                    if (count < 5) {
                        list.get(index).setSelected(true);
                        if (commonCallBackListner != null) {
                            commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(index, Status.ITEM_CLICK, list.get(index)));
                        }

                    } else {
                        details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
                        ViewUtil.showToast(details.getPlease_select_the_five_subject());
                    }
                }
            }

            AppLogger.e("updateList values -0 ", list.get(i).getSubjectname() + "--selecetd--" + list.get(i).isSelected() + "-is lock-" + list.get(i).isLock());
        }


        notifyDataSetChanged();
    }
}