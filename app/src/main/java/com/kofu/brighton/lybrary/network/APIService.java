package com.kofu.brighton.lybrary.network;

import com.kofu.brighton.lybrary.models.resources.Book;
import com.kofu.brighton.lybrary.models.resources.BorrowBook;
import com.kofu.brighton.lybrary.models.resources.Login;
import com.kofu.brighton.lybrary.models.resources.Register;
import com.kofu.brighton.lybrary.models.resources.Token;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    /*
     * Authentication
     *
     * */
    @POST("Authentication/SignIn")
    Call<Token> login(@Body Login login);

    @POST("Authentication/SignUp/Librarian")
    Call<Void> register(@Body Register details);

    /*
     * Books
     *
     * */
    @GET("Library/Book/All")
    Call<List<Book>> getAllBooks();

    @POST("Library/Book/Add")
    Call<Book> saveNewBook(@Body Book book);

    @GET("Library/Book/{bookId}")
    Call<Book> getBookById(@Path("bookId") int bookId);

    @POST("Library/Book/Borrow")
    Call<BorrowBook> borrowBook(@Body BorrowBook borrowBook);
}
