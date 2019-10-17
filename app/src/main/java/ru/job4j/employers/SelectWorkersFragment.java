package ru.job4j.employers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.job4j.employers.models.Speciality;
import ru.job4j.employers.models.Worker;

public class SelectWorkersFragment extends Fragment {
    private List<Worker> workers = new ArrayList<>();
    private RecyclerView recycler;
    private OnWorkerSelectClickListener callback;
    private NetworkService networkService = NetworkService.getInstance();
    private int specialityId;
    private boolean isBlue;
    private WorkersAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_select_workers, container, false);
        recycler = view.findViewById(R.id.recyclerViewWorkers);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        if (getArguments() != null) {
            specialityId = getArguments().getInt(MainActivity.SPECIALITY);
        }
        getWorkers();
        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnWorkerSelectClickListener) context; // назначаем активити при присоединении фрагмента к активити
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null; // обнуляем ссылку при отсоединении фрагмента от активити
    }

    public class WorkersAdapter extends RecyclerView.Adapter<WorkersAdapter.WorkersHolder> {
        private List<Worker> workers;
        private LayoutInflater inflater;

        WorkersAdapter(Context context, List<Worker> workers) {
            this.inflater = LayoutInflater.from(context);
            this.workers = workers;
        }

        @NonNull
        @Override
        public WorkersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.worker_item, viewGroup, false);
            return new WorkersHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WorkersHolder workersHolder, int i) {

            Worker worker = workers.get(i);
            workersHolder.textViewFirstName.setText(worker.getFName());
            workersHolder.textViewLastName.setText(worker.getLName());
            workersHolder.textViewAge.setText(worker.getBirthday());
            workersHolder.textViewSpeciality.setText("");
            for (Speciality speciality : worker.getSpecialty()) {
                workersHolder.textViewSpeciality.append(speciality.getName() + "\n");
            }
            String avatar = worker.getAvatrUrl();
            if (avatar != null && !avatar.equals("null") && !avatar.equals("")) {
                Context context = workersHolder.imageViewPhoto.getContext();
                Picasso.with(context).load(worker.getAvatrUrl())
                        .into(workersHolder.imageViewPhoto);
            }
        }

        @Override
        public int getItemCount() {
            return workers.size();
        }

        public class WorkersHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView textViewFirstName, textViewLastName, textViewAge, textViewSpeciality;
            private ImageView imageViewPhoto;

            WorkersHolder(View view) {
                super(view);
                textViewFirstName = view.findViewById(R.id.textViewFirstName);
                textViewLastName = view.findViewById(R.id.textViewLastName);
                textViewAge = view.findViewById(R.id.textViewBirth);
                textViewSpeciality = view.findViewById(R.id.textViewSpeciality);
                imageViewPhoto = view.findViewById(R.id.imageViewPhoto);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Worker worker = workers.get(getAdapterPosition());
                callback.onWorkerClicked(worker);
            }
        }
    }

    public interface OnWorkerSelectClickListener {
        void onWorkerClicked(Worker worker);
    }

    public void getWorkers() {
        if (workers.isEmpty()) {
            networkService.getJSONApi().getWorkers()
                    .enqueue(new Callback<List<Worker>>() {
                        @Override
                        public void onResponse(Call<List<Worker>> call, Response<List<Worker>> response) {
                            if (response.isSuccessful()) {
                                List<Worker> result = new ArrayList<>();
                                List<Worker> workers = response.body();
                                for (Worker worker : workers) {
                                    for (Speciality speciality : worker.getSpecialty()) {
                                        if (speciality.getSpecialtyId() == specialityId) {
                                            result.add(worker);
                                        }
                                    }
                                }
                                saveToRecycler(result);
                            } else {
                                // Toast.makeText(getApplicationContext(), String.format("Error code is: %s", response.code()), Toast.LENGTH_SHORT).show();
                                Log.i("MyError", "" + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Worker>> call, Throwable t) {
                            //   Toast.makeText(getApplicationContext(), String.format("Error code is: %s", t.getMessage()), Toast.LENGTH_SHORT).show();
                            Log.i("MyError", "" + t.getMessage());
                        }
                    });
        } else {
            saveToRecycler(workers);
        }
    }

    private void saveToRecycler(List<Worker> workers) {
        adapter = new WorkersAdapter(getContext(), workers);
        this.recycler.setAdapter(adapter);
    }
}
