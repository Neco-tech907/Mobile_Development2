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
import java.util.Locale;

import ru.mirea.ivanovrr.lapka.R;
import ru.mirea.ivanovrr.lapka.domain.models.Pet;

/** Адаптер списка питомцев приюта. Фото грузит Picasso по URL из ответа сервера. */
public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    private List<Pet> items = new ArrayList<>();

    public void setItems(List<Pet> pets) {
        this.items = pets == null ? new ArrayList<>() : pets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pet, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class PetViewHolder extends RecyclerView.ViewHolder {

        private final ImageView photo;
        private final TextView name;
        private final TextView info;
        private final TextView description;

        PetViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.imagePet);
            name = itemView.findViewById(R.id.textPetName);
            info = itemView.findViewById(R.id.textPetInfo);
            description = itemView.findViewById(R.id.textPetDescription);
        }

        void bind(Pet pet) {
            name.setText(pet.getName());
            info.setText(String.format(Locale.getDefault(), "%s · %s · %s",
                    pet.getBreedName(), formatAge(pet.getAgeMonths()),
                    pet.getGender().toLowerCase(Locale.getDefault())));
            description.setText(pet.getDescription());

            // пока грузится и если не загрузилось — лапка-заглушка из макета
            Picasso.get()
                    .load(pet.getImageUrl())
                    .placeholder(R.drawable.ic_paw_dark)
                    .error(R.drawable.ic_paw_dark)
                    .fit()
                    .centerCrop()
                    .into(photo);

            itemView.setOnClickListener(v -> Toast.makeText(v.getContext(),
                    pet.getName() + " — " + pet.getDescription(), Toast.LENGTH_SHORT).show());
        }

        private static String formatAge(int months) {
            if (months < 12) {
                return months + " мес.";
            }
            int years = months / 12;
            String word = years == 1 ? "год" : (years < 5 ? "года" : "лет");
            return years + " " + word;
        }
    }
}
