package com.kofu.brighton.lybrary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kofu.brighton.lybrary.MainActivityCallBacks;
import com.kofu.brighton.lybrary.R;
import com.kofu.brighton.lybrary.models.resources.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private final List<Book> mBookList;

    public BookAdapter(Context context, List<Book> bookList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mBookList = bookList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View bookItem = mInflater.inflate(R.layout.book_list_item, parent, false);
        return new BookViewHolder(bookItem);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = mBookList.get(position);
        holder.mTitle.setText(book.name);
        holder.mAuthor.setText(book.author);
        holder.setBook(book);
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTitle;
        public final TextView mAuthor;
        private final View mItemView;
        private Book mBook;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemView = itemView;
            mTitle = mItemView.findViewById(R.id.tv_title);
            mAuthor = mItemView.findViewById(R.id.tv_author);
        }

        public void setBook(Book book) {
            mBook = book;

            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivityCallBacks activity = (MainActivityCallBacks) mContext;
                    activity.bookSelected(book.id);
                }
            });
        }
    }
}
