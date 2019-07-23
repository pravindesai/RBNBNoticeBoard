package com.pack.batman.rbnbnoticeboard;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
public class ProgrammingAdapter extends RecyclerView.Adapter<ProgrammingAdapter.ProgrammingViewHolder> {
    ArrayList<String> date,name,title,text;
    public ProgrammingAdapter(ArrayList date,ArrayList name,ArrayList title,ArrayList text) {
        this.date=date;
        this.name=name;
        this.title=title;
        this.text=text;
    }

    @NonNull
    @Override
    public ProgrammingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
        View view=inflater.inflate(R.layout.list_item_layout,viewGroup,false);
        return new ProgrammingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgrammingViewHolder programmingViewHolder, int i) {
//        String name=data.get(i);
        String DATE=date.get(i);
        String NAME=name.get(i);
        String TITLE=title.get(i);
        String TEXT=text.get(i);

        programmingViewHolder.dateTv.setText(DATE);
        programmingViewHolder.nameTv.setText(NAME);
        programmingViewHolder.titleTv.setText(TITLE);
        programmingViewHolder.textTv.setText(TEXT);
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class ProgrammingViewHolder extends RecyclerView.ViewHolder
    {
        TextView nameTv,dateTv,titleTv,textTv;
        public ProgrammingViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTv=(TextView)itemView.findViewById(R.id.dateTv);
            nameTv=(TextView)itemView.findViewById(R.id.nameTv);
            titleTv=(TextView)itemView.findViewById(R.id.titleTv);
            textTv=(TextView)itemView.findViewById(R.id.textTv);
        }
    }

}
