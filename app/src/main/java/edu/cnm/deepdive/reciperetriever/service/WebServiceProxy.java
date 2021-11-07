package edu.cnm.deepdive.reciperetriever.service;

import androidx.viewbinding.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.reciperetriever.model.entity.Ingredient;
import edu.cnm.deepdive.reciperetriever.model.entity.Recipe;
import edu.cnm.deepdive.reciperetriever.model.entity.Recipe.CuisineType;
import io.reactivex.Single;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceProxy {

  @POST("https://api.spoonacular.com/recipes/cuisine")
  Single<CuisineType> searchCuisine(@Body CuisineType cuisineType);

  @GET("https://api.spoonacular.com/recipes/complexSearch")
  Single<Recipe> findRecipe(@Body Recipe recipe);

  @POST("https://api.spoonacular.com/recipes/{id}/information")
  List<Ingredient> findIngredients(@Body Ingredient ingredient, @Path("{recipeId}") long id );

  class InstanceHolder {

    private static final WebServiceProxy INSTANCE;

    static {
      Gson gson = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
          .create();
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(Level.BODY);
      OkHttpClient client = new Builder()
          .addInterceptor(interceptor)
          .build();
      Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(BuildConfig.BASE_URL)
          .addConverterFactory(GsonConverterFactory.create(gson))
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .client(client)
          .build();
      INSTANCE = retrofit.create(WebServiceProxy.class);
    }

  }

}