package eu.activstar.ursus.myapplication;

import com.jakewharton.rxrelay2.BehaviorRelay;

import io.reactivex.Observable;

/**
 * Created by Vlastimil Brečka (www.vlastimilbrecka.sk)
 * on 02-Jan-18.
 */
public class PaginationManager {

    public static final int PAGE_SIZE = 20;
    private final BehaviorRelay<Integer> mRelay = BehaviorRelay.createDefault(0);
    private int mCurrentPageIndex;

    public void init(int initialPageIndex) {
        mCurrentPageIndex = initialPageIndex;
    }

    public void reset() {
        init(0);
    }

    public boolean nextPage(int currentItemsCount) {
        int pageIndex = currentItemsCount / PAGE_SIZE;
        if (pageIndex > mCurrentPageIndex) {
            mCurrentPageIndex = pageIndex;
            mRelay.accept(pageIndex);
            return true;
        }
        return false;
    }

    public Observable<Integer> currentPageObservable() {
        return mRelay;
    }

}
