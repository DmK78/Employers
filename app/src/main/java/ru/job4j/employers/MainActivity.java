package ru.job4j.employers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Toast;

import ru.job4j.employers.models.Speciality;
import ru.job4j.employers.models.Worker;

public class MainActivity extends AppCompatActivity implements SelectSpecialityFragment.OnSpecialitySelectClickListener, SelectWorkersFragment.OnWorkerSelectClickListener {
    public static final String SPECIALITY = "speciality";
    public static final String WORKER_F_NAME = "workerFirstName";
    public static final String WORKER_L_NAME = "workerLastName";
    public static final String WORKER_BIRTH_DATE = "workerBirthDate";
    private FragmentManager fm;
    private Fragment selectSpecialityFragment;
    private Fragment selectWorkerFragment;
    private Fragment workerInfoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();
        selectSpecialityFragment = fm.findFragmentById(R.id.fragment_container);
        if (selectSpecialityFragment == null) {
            selectSpecialityFragment = new SelectSpecialityFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, selectSpecialityFragment)
                    .commit();
        }
    }

    @Override
    public void onSpecialityClicked(Speciality speciality) {
        Bundle bundle = new Bundle();
        bundle.putInt(SPECIALITY, speciality.getSpecialtyId());//передаем айди специальности
        selectWorkerFragment = new SelectWorkersFragment();
        selectWorkerFragment.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.fragment_container, selectWorkerFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onWorkerClicked(Worker worker) {
        Bundle bundle = new Bundle();
        bundle.putString(WORKER_F_NAME, worker.getFName());
        bundle.putString(WORKER_L_NAME, worker.getLName());
        bundle.putString(WORKER_BIRTH_DATE, worker.getBirthday());
        workerInfoFragment = new WorkerInfoFragment();
        workerInfoFragment.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.fragment_container, workerInfoFragment)
                .addToBackStack(null)
                .commit();

    }
}
