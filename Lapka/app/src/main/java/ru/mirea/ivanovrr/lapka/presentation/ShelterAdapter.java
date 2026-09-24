package ru.mirea.ivanovrr.lapka.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.ivanovrr.lapka.R;
import ru.mirea.ivanovrr.lapka.domain.models.Shelter;

/**
 * Адаптер списка приютов и магазинов. Данные приходят из MainViewModel через LiveData:
 * Activity вызывает setItems, адаптер перерисовывает карточки.
 */
public class ShelterAdapter extends RecyclerView.Adapter<ShelterAdapter.ShelterViewHolder> {

    private List<Shelter> items = new ArrayList<>();

    public void setItems(List<Shelter> shelters) {
        this.items = shelters == null ? new ArrayList<>() : shelters;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShelterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shelter, parent, false);
        return new ShelterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShelterViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /** ViewHolder держит ссылки на view карточки, чтобы не искать их при каждой прокрутке. */
    static class ShelterViewHolder extends RecyclerView.ViewHolder {

        private final ImageView photo;
        private final TextView name;
        private final TextView type;
        private final TextView address;
        private final TextView hours;

        ShelterViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.imageShelter);
            name = itemView.findViewById(R.id.textShelterName);
            type = itemView.findViewById(R.id.textShelterType);
            address = itemView.findViewById(R.id.textShelterAddress);
            hours = itemView.findViewById(R.id.textShelterHours);
        }

        void bind(Shelter shelter) {
            name.setText(shelter.getName());
            type.setText(shelter.getType());
            address.setText(shelter.getAddress());
            hours.setText(shelter.getWorkingHours() + " · "
                    + String.join(", ", shelter.getAvailableBreeds()));

            // фото приюта по URL из ответа сервера; заглушка — лапка из макета
            Picasso.get()
                    .load(shelter.getImageUrl())
                    .placeholder(R.drawable.ic_paw_dark)
                    .error(R.drawable.ic_paw_dark)
                    .fit()
                    .centerCrop()
                    .into(photo);
            itemView.setOnClickListener(v -> Toast.makeText(v.getContext(),
                    shelter.getName() + "\n" + shelter.getPhone(), Toast.LENGTH_SHORT).show());
        }
    }
}
