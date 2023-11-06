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
import ma.ensa.volley.beans.Role;

public class RoleAdapter extends ArrayAdapter<Role> {

    private static class ViewHolder {
        TextView roleName,id;
    }

    public RoleAdapter(@NonNull Context context, int resource, @NonNull List<Role> roles) {
        super(context, resource, roles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        View item = convertView;

        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.itemrole, parent, false);

            holder = new ViewHolder();
            holder.id=item.findViewById(R.id.id);
            holder.roleName = item.findViewById(R.id.nom);
            item.setTag(holder);
        } else {
            holder = (ViewHolder) item.getTag();
        }

        Role role = getItem(position);
        if (role != null) {
            holder.roleName.setText(role.getName());
        }

        return item;
    }
}
