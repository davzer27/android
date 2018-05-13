package fr.ec.producthunt.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ViewAnimator;

import java.util.List;

import fr.ec.producthunt.R;
import fr.ec.producthunt.data.DataProvider;
import fr.ec.producthunt.data.SyncService;
import fr.ec.producthunt.data.model.Collection;
import fr.ec.producthunt.data.model.Comment;
import fr.ec.producthunt.data.model.Post;

public class CommentsFragments extends Fragment {

    private static final int PROGRESS_CHILD = 1;
    private static final int LIST_CHILD = 0;
    private static final String ARG_POSTID = "postId";
    private long postId;

    private DataProvider dataProvider;
    private CommentAdapter commentAdapter;
    private ViewAnimator viewAnimator;

    private SyncCommentReceiver syncCommentReceiver;

    public static CommentsFragments newInstance(long postId) {
        CommentsFragments fragment = new CommentsFragments();
        Bundle args = new Bundle();
        args.putLong(ARG_POSTID, postId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home_shit_list_fragment, container, false);
        /*if (getArguments() != null) {
            postId = getArguments().getLong(ARG_POSTID);
            System.out.println("-----");
            System.out.println(postId);
            System.out.println("-------");
        }*/
        syncCommentReceiver = new SyncCommentReceiver();

        commentAdapter = new CommentAdapter();

        ListView listView = rootView.findViewById(R.id.list_item);
        listView.setEmptyView(rootView.findViewById(R.id.empty_element));
        viewAnimator = rootView.findViewById(R.id.main_view_animator);
        listView.setAdapter(commentAdapter);
        refreshComments();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataProvider = DataProvider.getInstance(getActivity().getApplication());
        loadComments();
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SyncCommentReceiver.ACTION_LOAD_COMMENTS);
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(syncCommentReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(syncCommentReceiver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.refresh:
                refreshComments();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class SyncCommentReceiver extends BroadcastReceiver {
        public static final String ACTION_LOAD_COMMENTS = "fr.ec.producthunt.data.action.LOAD_COMMENTS";

        public SyncCommentReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals(ACTION_LOAD_COMMENTS)) {
                loadComments();
            }
        }
    }

    private void refreshComments() {
        Log.e("CommentFragment", "refreshing comments");
        Log.e("args", "" +  getArguments().getLong(ARG_POSTID, 0));
        SyncService.startSyncCommentsFromPostId(getContext(), getArguments().getLong(ARG_POSTID)); //TODO
    }

    private void loadComments() {
        FetchCommentAsyncTask fetchCommentAsyncTask = new FetchCommentAsyncTask();
        fetchCommentAsyncTask.execute();
    }

    private class FetchCommentAsyncTask extends AsyncTask<Void, Void, List<Comment>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            viewAnimator.setDisplayedChild(PROGRESS_CHILD);
        }

        @Override
        protected List<Comment> doInBackground(Void... params) {

            return dataProvider.getCommentsFromDatabase();
        }

        @Override
        protected void onPostExecute(List<Comment> comments) {
            if (comments != null && !comments.isEmpty()) {
                commentAdapter.showComments(comments);
            }
            viewAnimator.setDisplayedChild(LIST_CHILD);
        }
    }

}
