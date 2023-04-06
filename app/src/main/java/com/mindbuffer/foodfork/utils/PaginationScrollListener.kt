package com.mindbuffer.foodfork.utils

import android.widget.AbsListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

abstract class PaginationScrollListener(var layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val currentItems = layoutManager.childCount
        val totalItems = layoutManager.itemCount
        val scrollOutItems = layoutManager.findFirstVisibleItemPosition()

        Timber.d(
            "totalResults: $totalItems \n" +
                    "currentItems: $currentItems \n" +
                    "scrollOutItems: $scrollOutItems \n"
        )

        if(getTotalResults() > totalItems && isScrolling() && (currentItems + scrollOutItems == totalItems))
        {
            loadMoreItems()
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            setScrolling(true)
        }
    }

    protected abstract fun loadMoreItems()
    abstract fun setScrolling(flag: Boolean)
    abstract fun getTotalResults(): Int
    abstract fun isScrolling(): Boolean
}