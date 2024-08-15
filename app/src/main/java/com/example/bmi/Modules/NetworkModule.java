package com.example.bmi.Modules;



import androidx.annotation.NonNull;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    @Provides
    public static OkHttpClient provideOkHttpClient() {
        Interceptor authInterceptor = new Interceptor() {
            @NonNull
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder builder = original.newBuilder()
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImlzcyI6IkFHVy1iVlh0Rk85UzNwZUtycy14emVnME1xRVFDZ082In0.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.yPXvYwk2MllM8GOAJ9ISfC86st0pjq-uShpVYh1cBjs");
                Request request = builder.build();
                return chain.proceed(request);
            }
        };

        return new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build();
    }

    @Provides
    public static Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl("https://fbqiykghaf.c1.hn.apigw.bfcplatform.vn/chat/generate/")
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }
}
