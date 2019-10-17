package ru.job4j.employers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.job4j.employers.models.Speciality;
import ru.job4j.employers.models.Worker;

public class WorkerInfoFragment extends Fragment {
    private TextView textViewFirstName, textViewLastName, textViewBirthDate, textViewSpeciality;
    private ImageView imageViewPhoto;
    private NetworkService networkService = NetworkService.getInstance();
    String firstName;
    String lastName;
    String birthDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_worker_info, container, false);
        textViewFirstName = view.findViewById(R.id.textViewInfoFirstName);
        textViewLastName = view.findViewById(R.id.textViewInfoLastName);
        textViewBirthDate = view.findViewById(R.id.textViewInfoBirthDate);
        textViewSpeciality = view.findViewById(R.id.textViewInfoSpeciality);
        imageViewPhoto = view.findViewById(R.id.imageViewInfoPhoto);
        firstName = getArguments().getString(MainActivity.WORKER_F_NAME);
        lastName = getArguments().getString(MainActivity.WORKER_L_NAME);
        birthDate = getArguments().getString(MainActivity.WORKER_BIRTH_DATE);
        networkService.getJSONApi().getWorkers()
                .enqueue(new Callback<List<Worker>>() {
                    @Override
                    public void onResponse(Call<List<Worker>> call, Response<List<Worker>> response) {
                        if (response.isSuccessful()) {
                            Worker worker = findWorker(response.body(), firstName, lastName, birthDate);
                            if (worker != null) {
                                textViewBirthDate.setText(worker.getBirthday());
                                textViewFirstName.setText(worker.getFName());
                                textViewLastName.setText(worker.getLName());
                                String avatar = worker.getAvatrUrl();
                                if (avatar != null && !avatar.equals("null") && !avatar.equals("")) {
                                    Context context = imageViewPhoto.getContext();
                                    Picasso.with(context).load(worker.getAvatrUrl())
                                            .into(imageViewPhoto);
                                }
                                StringBuilder stringBuilder = new StringBuilder();
                                for (Speciality speciality : worker.getSpecialty()) {
                                    stringBuilder.append(speciality.getName() + "\n");
                                }
                                textViewSpeciality.append(stringBuilder);
                            }


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
        return view;
    }

    private Worker findWorker(List<Worker> workers, String firstName, String lastName, String birthDate) {
        Worker result = null;
        for (Worker worker : workers) {
            if (firstName.equals(worker.getFName()) &&
                    lastName.equals(worker.getLName())) {
                if (birthDate != null) {
                    if (birthDate.equals(worker.getBirthday())) {
                        result = worker;
                        break;
                    }
                } else result = worker;
            }
        }
        return result;
    }

}
