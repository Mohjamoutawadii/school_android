package ma.ensa.volley.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ma.ensa.volley.R;
import ma.ensa.volley.beans.Filiere;

public class FiliereAdapter extends ArrayAdapter<Filiere> {

        private static class ViewHolder {
            TextView id;
            TextView nom;
            TextView code;

        }

        public FiliereAdapter(@NonNull Context context, int resource, @NonNull List<Filiere> Filieres) {
            super(context, resource, Filieres);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            View item = convertView;

            if (item == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                item = inflater.inflate(R.layout.item, parent, false);

                holder = new ViewHolder();
                holder.id = item.findViewById(R.id.id);
                holder.nom = item.findViewById(R.id.nom);
                holder.code = item.findViewById(R.id.code);


                item.setTag(holder);
            } else {
                holder = (ViewHolder) item.getTag();
            }

            Filiere st = getItem(position);
            if (st != null) {
                holder.id.setText(String.valueOf(st.getId()));
                holder.nom.setText(st.getname());
                holder.code.setText(st.getcode());

            }

            return item;
        }

    }

