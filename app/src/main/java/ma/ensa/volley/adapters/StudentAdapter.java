package ma.ensa.volley.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import ma.ensa.volley.R;
import ma.ensa.volley.beans.Student;

public class StudentAdapter extends ArrayAdapter<Student> {

    private static class ViewHolder {
        TextView id;
        TextView firstName;
        TextView lastName;
        TextView telephone;
    }

    public StudentAdapter(@NonNull Context context, int resource, @NonNull List<Student> students) {
        super(context, resource, students);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View item = convertView;

        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.itemstudent, parent, false);

            holder = new ViewHolder();
            holder.id = item.findViewById(R.id.id);
            holder.firstName = item.findViewById(R.id.firstName);
            holder.lastName = item.findViewById(R.id.lastName);
            holder.telephone = item.findViewById(R.id.telephone);

            item.setTag(holder);
        } else {
            holder = (ViewHolder) item.getTag();
        }

        Student student = getItem(position);
        if (student != null) {
            holder.id.setText(String.valueOf(student.getId()));
            holder.firstName.setText(student.getFirstName());
            holder.lastName.setText(student.getLastName());
            holder.telephone.setText(student.getTelephone());
        }

        return item;
    }

}

