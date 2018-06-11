package com.example.frank.jinding.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.frank.jinding.R;

import java.util.List;


/**
 * Created by DELL on 2017/10/28.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private RecyclerViewOnItemListener listener;
    public List<String> list;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<String> list) {
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(View.inflate(context,R.layout.item_wait_check, null),listener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


    }
    public void setListener(RecyclerViewOnItemListener listener){
        this.listener=listener;
    }



    @Override
    public int getItemCount() {


        return list.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView author;
        private TextView title;
        private ImageView imageView;
        private RecyclerViewOnItemListener listener;
        public ViewHolder(View itemView,RecyclerViewOnItemListener listener) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            this.listener=listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (listener!=null)
                listener.onItemClick(v,getPosition());
        }
    }

 public interface RecyclerViewOnItemListener{
        public void onItemClick(View view, int position);
 }
}
