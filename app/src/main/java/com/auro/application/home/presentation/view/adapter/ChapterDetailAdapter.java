package com.auro.application.home.presentation.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.LayoutChapterdetailBinding;
import com.auro.application.databinding.LayoutModulecourselitItemBinding;
import com.auro.application.home.data.model.CourseModule.ChapterData;
import com.auro.application.home.data.model.CourseModule.ModuleTaskData;
import com.auro.application.util.AppUtil;
import com.google.android.gms.common.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class ChapterDetailAdapter extends RecyclerView.Adapter<ChapterDetailAdapter.ViewHolder> {

    List<ChapterData> mValues;
    Context mContext;
    LayoutChapterdetailBinding binding;
    CommonCallBackListner listner;
    public ChapterDetailAdapter(Context context, List<ChapterData> values, CommonCallBackListner listner) {
        mValues = values;
        mContext = context;
        this.listner=listner;

    }

    public void updateList(ArrayList<ChapterData> values) {
        mValues = values;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutChapterdetailBinding binding;

        public ViewHolder(LayoutChapterdetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    @Override
    public ChapterDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.layout_chapterdetail, viewGroup, false);
        return new ViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(ViewHolder Vholder, @SuppressLint("RecyclerView") int position) {



        String videopath = mValues.get(position).getYoutube();
        String getUser=videopath.substring(videopath.lastIndexOf("/")+1,videopath.length());
        String path = "<iframe width= \"100%\"height=\"100%\" src=\"https://www.youtube.com/embed/"+getUser+"\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        binding.videoView.getSettings().setPluginState(WebSettings.PluginState.ON);
        binding.videoView.getSettings().setBuiltInZoomControls(false);
        binding.videoView.getSettings().setSupportZoom(false);
        binding.videoView.loadData(path,"text/html","utf-8");
        binding.videoView.getSettings().setJavaScriptEnabled(true);
        binding.videoView



                .setWebChromeClient(new WebChromeClient());
       binding.txtheading.setText(mValues.get(position).getPageTitle());

        if (mValues.get(position).getPageText().equals("")||mValues.get(position).getPageText() == null){
            binding.txtdetail.setVisibility(View.GONE);
        }
        else{
            String html = mValues.get(position).getPageText();
          //  html = html.replaceAll("\\r\\n","");
          //  Spanned spanned = Html.fromHtml(html);


            binding.txtdetail.setText(
                    Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT),
                    TextView.BufferType.SPANNABLE);




            binding.txtdetail.setVisibility(View.VISIBLE);
        }
        if (mValues.get(position).getYoutube().equals("")||mValues.get(position).getYoutube() == null){
            binding.layoutVideo.setVisibility(View.GONE);
        }

        else{
            binding.layoutVideo.setVisibility(View.VISIBLE);
        }



    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }


}
