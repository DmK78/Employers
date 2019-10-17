package ru.job4j.employers;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.job4j.employers.models.Speciality;
import ru.job4j.employers.models.Worker;

public interface JsonPlaceHolderApi {
    @GET("task.json")
    Call<List<Worker>> getWorkers();

    @GET("task.json")
    Call<List<Worker>> getWorkersBySpeciality(@Query("specialty") int speciality_id);

    @GET("task.json")
    Call<Worker> getWorker(
            @Query("f_name") String firstName,
            @Query("l_name") String lastName,
            @Query("birthday") String birthday
    );
}
