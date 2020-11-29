package com.example.dzik;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface JsonPlaceHolderApi {

    @POST("boar/post.php")
    Call<PostRegister> createPostRegister(@Body PostRegister postRegister);

    @POST("boar/post.php")
    Call<PostLogin> createPostLogin(@Body PostLogin postLogin);

    @POST("boar/post.php")
    Call<PostGetSet> createPostGetSet(@Body PostGetSet postGetSet);

    @POST("boar/post.php")
    Call<PostUpDataSet> createPostUpDataSet(@Body PostUpDataSet postUpDataSet);

    @POST("boar/post.php")
    Call<PostAdd> createPostAdd(@Body PostAdd postAdd);

    @POST("boar/post.php")
    Call<List <PostHistory> > createPostHistory(@Body PostHistory postHistory);

    @POST("boar/post.php")
    Call<List <PostArea>> createPostArea(@Body PostArea postArea);

    @POST("boar/post.php")
    Call<PostArch> createPostArch(@Body PostArch postArch);




    /*@POST("boar/post.php")
    @FormUrlEncoded
    Call<PostImage> creatPostImage(
            @Field("task") String task,
            @Field("uniq") String uniq,
            @Field("image") String image
    );*/


}
