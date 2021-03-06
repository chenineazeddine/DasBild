package azeddine.project.summer.dasBild.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import azeddine.project.summer.dasBild.R;
import azeddine.project.summer.dasBild.adapters.CountryAlbumAdapter;
import azeddine.project.summer.dasBild.costumComponents.WrapContentGridLayoutManager;
import azeddine.project.summer.dasBild.loaders.SavedPhotosLoader;
import azeddine.project.summer.dasBild.loaders.OnlinePhotosLoader;
import azeddine.project.summer.dasBild.objectsUtils.KeysUtil;

/**
 * Created by azeddine on 10/23/17.
 */

public abstract class PhotosGalleryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Object>{
    private static final String TAG = "PhotosGalleryFragment";
    protected RecyclerView mAlbumRecyclerView;
    protected ImageView mErrorImageView ;
    protected TextView mErrorText;
    protected CountryAlbumAdapter mCountryAlbumAdapter;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected String mAlbumName;
    protected String mCategoryName;
    protected View mErrorDisplayLayout;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState,Boolean hasToolBar) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(hasToolBar ? R.layout.fragment_bookmarkes_album : R.layout.fragment_country_album, container, false);
        mAlbumRecyclerView = view.findViewById(R.id.country_album);
        mErrorImageView = view.findViewById(R.id.error_image);
        mErrorText = view.findViewById(R.id.error_text);
        mSwipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        mErrorDisplayLayout = view.findViewById(R.id.error_frame);
        return view;
    }

    /**
     * a method that sets the empty list view bitmap and error message
     * @param errorText the error text to display
     * @param drawableId the drawable image to display
     */

    protected void showEmptyListBitmap(@StringRes int errorText, @DrawableRes int drawableId){
            mAlbumRecyclerView.setVisibility(View.GONE);
            mErrorDisplayLayout.setVisibility(View.VISIBLE);
            mErrorImageView.setImageResource(drawableId);
            mErrorText.setText(errorText);
    }

    /**
     * a method used to hide the empty list view
     * and show the app pastern as a background
     */

    protected void hideEmptyListBitmap(){
            mAlbumRecyclerView.setVisibility(View.VISIBLE);
            mErrorDisplayLayout.setVisibility(View.GONE);
    }

    /**
     * get the visibility of the photos recycler view
     * @return true if visible else it returns false
     */
    protected boolean isEmptyListViewVisible(){
        return mAlbumRecyclerView.getVisibility() == View.GONE;
    }

    /**
     * set the scroll offset to zero smoothly
     */
    public void resetAlbumScroll(){
        mAlbumRecyclerView.smoothScrollToPosition(0);
    }

    public void setAlbumOnRefreshListener(SwipeRefreshLayout.OnRefreshListener refreshListener, @ColorRes int circleColor){
        mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
        mSwipeRefreshLayout.setColorSchemeResources(circleColor);

    }

    /**
     * init the album adapter fields
     * @param context of the host activity
     * @param columnNumber in one row
     * @return the album adapter adapter
     * @throws Exception if the specified number of columns is zero
     */
    public  CountryAlbumAdapter initAlbumAdapter(Context context,int columnNumber) throws Exception {
        if(columnNumber < 1) throw new Exception("the column number can't be less then one!");
        mAlbumRecyclerView.setLayoutManager(new WrapContentGridLayoutManager(context,columnNumber));
        return new CountryAlbumAdapter(context, mAlbumRecyclerView,columnNumber);
    }


    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        switch (id) {
            case KeysUtil.ONLINE_MORE_PHOTOS_LOADER_ID:
                return new OnlinePhotosLoader(getContext(),mAlbumName,mCategoryName);
            case  KeysUtil.ONLINE_RECENT_PHOTOS_LOADER_ID:
                return new OnlinePhotosLoader(getContext(),mAlbumName,mCategoryName);
            case KeysUtil.SAVED_PHOTOS_LOADER_ID:
                return new SavedPhotosLoader(getContext());
            default:
                return null;
        }
    }


}
