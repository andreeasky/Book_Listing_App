package com.example.android.booklisting;

import android.app.Fragment;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.booklisting.R.id.search;

public class BookActivity extends AppCompatActivity {

    // This is the tag for LOG message
    public static final String LOG_TAG=BookActivity.class.getName();

    // This is the Google API URL
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?&maxResults=10&q=";

    // Add a maximum results of 20 to search query
    private static final String MAX_RESULTS="&maxResults=20";

    // Adapter for the list of books
    private BookAdapter bookAdapter;

    // Edit text field used for searching for books
    private EditText searchBook;

    // TextView visible when there is a problem with the internet connection and the list is empty
    private TextView emptyStateTextView;

    // Progress bar visible when the internet connection is delayed or slow
    private View loadingIndicator;

    // Checker for the internet connection
    private boolean isInternetConnected;

    private static final String LIST_STATE = "listState";

    private Parcelable mListState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        // Find a reference to the ListView in the layout
        ListView bookListView=(ListView) findViewById(R.id.list_view);

        // Find a reference to the EditText in the layout
        searchBook=(EditText) findViewById(search);

        // Find a reference to the Search Button in the layout
        ImageButton buttonSearch=(ImageButton) findViewById(R.id.button_search);

        // Find a reference to the empty state TextView
        emptyStateTextView=(TextView) findViewById(R.id.empty_text_view);

        // Set empty state TextView on the ListView with books when no data can be found
        bookListView.setEmptyView(emptyStateTextView);

        // Find a reference to the progress bar
        loadingIndicator=findViewById(R.id.loading_indicator);

        // Check internet connection
        isInternetConnected=checkInternetConnection();

        // Create a new adapter that takes an empty list of books as input
              bookAdapter=new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the ListView so the list can be populated in the user interface
        bookListView.setAdapter(bookAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book currentBook=bookAdapter.getItem(position);
                Intent webIntent=new Intent(Intent.ACTION_VIEW);
                if (webIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(webIntent);
                }
            }
        });

        // Set a click listener to the ImageButton Search which sends query to the URL based on the user input
        buttonSearch.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                // If there is a network connection, fetch data
                if (isInternetConnected) {
                    String searchUrl=searchBook.getText().toString();
                    // This is called when there is an internet connection.
                    // Start the AsyncTask to fetch the books data
                    new BookAsyncTask().execute(BASE_URL + searchUrl + MAX_RESULTS);

                } else {
                    Log.e(LOG_TAG, "This is called when there is no internet connection.");
                    // Otherwise, display error
                    // First, hide loading indicator so error will be visible
                    loadingIndicator.setVisibility(View.GONE);
                    // Show the empty state with no connection error message
                    emptyStateTextView.setVisibility(View.VISIBLE);
                    // Update empty state with no connection error message
                    emptyStateTextView.setText(R.string.no_internet_connection);
                }
            }
        });
        
        // Write list state to bundle
        @Override
        protected void onSaveInstanceState (Bundle state){
            super.onSaveInstanceState(state);
            mListState=getbookListView().onSaveInstanceState();
            state.putParcelable(LIST_STATE, mListState);
        }

    private Fragment getbookListView() {
        return null;
    }

    // Create a new adapter that takes an empty list of books as input

    bookAdapter=new BookAdapter(this, new ArrayList<Book>());

    // Set the adapter on the ListView so the list can be populated in the user interface
        bookListView.setAdapter(bookAdapter);

    @Override
    public void onPause() {
        // Save ListView state @ onPause
        Log.d(TAG, "saving listview state @ onPause");
        state=bookListView.onSaveInstanceState();
        super.onPause();
    }

    @Override
    public void onViewCreated(final View view, Bundle onsaveInstanceState) {
        super.onViewCreated(view, onsaveInstanceState);
        // Set new items - Set the adapter on the ListView so the list can be populated in the user interface
        bookListView.setAdapter(bookAdapter);
        // Restore previous state (including selected item index and scroll position)
        if (state != null) {
            Log.d(TAG, "trying to restore listview state..");
            listView.onRestoreInstanceState(state);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(); // make sure data has been reloaded into adapter first
        // ONLY call this part once the data items have been loaded back into the adapter
        // for example, inside a success callback from the network
        if (mListState != null) {
            bookListView.onRestoreInstanceState(mListState);
            mListState=null;
        }
    }

    // Restore list state from bundle
    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        mListState=state.getParcelable(LIST_STATE);
        {

            // Check the internet connection
            private boolean checkInternetConnection (){
            // Get a reference to the ConnectivityManager to check the state of network connectivity
            ConnectivityManager connectivityManager=(ConnectivityManager)
                    getSystemService(CONNECTIVITY_SERVICE);

            // Get details on the currently active default network
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            } else {
                // Show the empty state with no connection error message
                emptyStateTextView.setVisibility(View.VISIBLE);
                // Update empty state with no connection error message
                emptyStateTextView.setText(R.string.no_internet_connection);
                return false;
            }
        }

            // {@link AsyncTask} to perform the network request on a background thread, and then
            // update the UI with the list of earthquakes in the response.
            //
            // We'll only override two of the methods of AsyncTask: doInBackground() and onPostExecute().
            // The doInBackground() method runs on a background thread, so it can run long-running code
            // (like network activity), without interfering with the responsiveness of the app.
            // Then onPostExecute() is passed the result of doInBackground() method, but runs on the
            // UI thread, so it can use the produced data to update the UI.
            //
            private class BookAsyncTask extends AsyncTask<String, Void, List<Book>> {

                // This method runs on the UI thread before foInBackground().
                // It shows the progress bar when the internet connection is delayed or slow.
                //
                @Override
                protected void onPreExecute() {
                    Log.e(LOG_TAG, "When is the onPreExecute called?");
                    // Show loading indicator if the internet connection is delayed or slow.
                    loadingIndicator.setVisibility(View.VISIBLE);
                }

                // This method runs on a background thread and performs the network request.
                // We should not update the UI from a background thread, so we return a list of
                // Books as the result.
                //
                @Override
                protected List<Book> doInBackground(String... urls) {
                    // Don't perform the request if there are no URLs, or the first URL is null.
                    if (urls.length < 1 || urls[0] == null) {
                        return null;
                    }
                    List<Book> result=Utils.fetchBooksData(urls[0]);
                    Log.i("search", urls[0]);

                    return result;
                }

                // This method runs on the main UI thread after the background work has been
                // completed. This method receives as input, the return value from the doInBackground()
                // method. First we clear out the adapter, to get rid of books data from a previous
                // query to Google Books API. Then we update the adapter with the new list of books,
                // which will trigger the ListView to re-populate its list items.
                //
                @Override
                protected void onPostExecute(List<Book> books) {
                    // First, hide loading indicator so error will be visible
                    loadingIndicator.setVisibility(View.GONE);
                    // Clear the adapter of previous book data
                    bookAdapter.clear();

                    // If there is a valid list of Books, then add them to the adapter's
                    // data set. This will trigger the ListView to update.
                    if (books != null && !books.isEmpty()) {
                        bookAdapter.addAll(books);
                    } else {
                        // Show the empty state with no connection error message
                        emptyStateTextView.setVisibility(View.VISIBLE);
                        // Update empty state with no connection error message
                        emptyStateTextView.setText(R.string.no_data);
                    }
                }
            }
        }
    }
}


