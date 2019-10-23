package br.com.ricardouesb.banco.model;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.ricardouesb.banco.R;

public class MovimentAdapter extends ArrayAdapter<Moviment> {
    private Context context;

    public MovimentAdapter(Context context) {
        super(context, R.layout.row_list_extract, BancoDados.getMoviments());
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = li.inflate(R.layout.row_list_extract, parent, false);

        TextView title, data, value;

        title = rowView.findViewById(R.id.title_moviment);
        data = rowView.findViewById(R.id.data_moviment);
        value = rowView.findViewById(R.id.value_moviment);

        Moviment moviment = BancoDados.getMoviments().get(position);

        title.setText(moviment.getTitle());
        data.setText(moviment.getDateFormated());
        value.setText("R$ " + moviment.getValueFormated());

        if(moviment.getType()){
            value.setTextColor(Color.rgb(0, 100, 0));
        }else{
            value.setTextColor(Color.rgb(100, 0, 0));
        }

        return rowView;
    }
}
