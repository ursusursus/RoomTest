package eu.activstar.ursus.myapplication;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Vlastimil Brečka (www.vlastimilbrecka.sk)
 * on 02-Jan-18.
 */
public abstract class ListEndScrollListener extends RecyclerView.OnScrollListener {

    private static final String KEY_LAST_VIS_POS = "last_vis_pos";

    private final int mTriggerIndexFromEnd;
    private int mLastLastVisiblePosition;

    public ListEndScrollListener() {
        this(2);
    }

    public ListEndScrollListener(int triggerIndexFromEnd) {
        mTriggerIndexFromEnd = triggerIndexFromEnd;
    }

    public void attachToRecyclerView(RecyclerView recyclerView, Bundle savedInstanceState) {
        recyclerView.addOnScrollListener(this);
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        final int currentFirstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        if (currentFirstVisiblePosition == -1) return;

        // Load more
        final int currentLastVisiblePosition = layoutManager.findLastVisibleItemPosition();
        if (currentLastVisiblePosition > mLastLastVisiblePosition &&
                currentLastVisiblePosition >= recyclerView.getAdapter().getItemCount() - mTriggerIndexFromEnd) { // Ak je predposledny index zobrazeny
            // Cannot call notifyDatasetChanged in onScroll callback
            // as per recyclerview warning
            recyclerView.post(new Runnable() {
                @Override public void run() {
                    onScrolledToEnd();
                }
            });
        }
        mLastLastVisiblePosition = currentLastVisiblePosition;
    }

    public abstract void onScrolledToEnd();

    private void onRestoreInstanceState(Bundle savedState) {
        mLastLastVisiblePosition = savedState.getInt(KEY_LAST_VIS_POS);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_LAST_VIS_POS, mLastLastVisiblePosition);
    }
}
