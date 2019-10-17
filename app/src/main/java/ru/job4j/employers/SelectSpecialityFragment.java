package ru.job4j.employers;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.job4j.employers.models.Speciality;
import ru.job4j.employers.models.Worker;

public class SelectSpecialityFragment extends Fragment {
    private RecyclerView recycler;
    private SpecialitiesAdapter adapter;
    private OnSpecialitySelectClickListener callback;
    private List<Speciality> specialityList = new ArrayList<>();
    private NetworkService networkService = NetworkService.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_select_speciality, container, false);
        recycler = view.findViewById(R.id.recyclerViewSpecialities);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        getSpecialitites();
        return view;
    }


    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnSpecialitySelectClickListener) context; // назначаем активити при присоединении фрагмента к активити
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null; // обнуляем ссылку при отсоединении фрагмента от активити
    }

    public class SpecialitiesAdapter extends RecyclerView.Adapter<SpecialitiesAdapter.SpecialitiesHolder> {
        private List<Speciality> specialities;
        private LayoutInflater inflater;

        SpecialitiesAdapter(Context context, List<Speciality> specialityList) {
            this.inflater = LayoutInflater.from(context);
            specialities = specialityList;
        }

        @NonNull
        @Override
        public SpecialitiesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.speciality_item, viewGroup, false);
            return new SpecialitiesHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SpecialitiesHolder specialityHolder, int i) {
            Speciality speciality = specialities.get(i);
            specialityHolder.textViewName.setText(speciality.getName());
        }

        @Override
        public int getItemCount() {
            return specialities.size();
        }

        public class SpecialitiesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView textViewName;

            SpecialitiesHolder(View view) {
                super(view);
                textViewName = view.findViewById(R.id.textViewSpeciality);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                callback.onSpecialityClicked(specialities.get(getAdapterPosition()));

            }
        }
    }

    public interface OnSpecialitySelectClickListener {
        void onSpecialityClicked(Speciality speciality);
    }

    public void getSpecialitites() {
        if (specialityList.isEmpty()) {
            networkService.getJSONApi().getWorkers()
                    .enqueue(new Callback<List<Worker>>() {
                        @Override
                        public void onResponse(Call<List<Worker>> call, Response<List<Worker>> response) {
                            if (response.isSuccessful()) {
                                List<Worker> workers = response.body();
                                Set<Speciality> specialitySet = new TreeSet<>();
                                for (Worker worker : workers) {
                                    for (Speciality speciality : worker.getSpecialty()) {
                                        specialitySet.add(speciality);
                                    }
                                }
                                specialityList.addAll(specialitySet);
                                saveToRecycler(specialityList);

                            } else {
                                Toast.makeText(getContext(), String.format("Error code is: %s", response.code()), Toast.LENGTH_SHORT).show();
                                Log.i("MyError", "" + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Worker>> call, Throwable t) {
                            Toast.makeText(getContext(), String.format("Error code is: %s", t.getMessage()), Toast.LENGTH_SHORT).show();
                            Log.i("MyError", "" + t.getMessage());

                        }
                    });
        } else saveToRecycler(specialityList);
    }

    private void saveToRecycler(List<Speciality> specialityList) {
        adapter = new SpecialitiesAdapter(getContext(), specialityList);
        this.recycler.setAdapter(adapter);
    }

}
