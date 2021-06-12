package com.example.flickrtestapp.ui.view.recyclerview

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.ViewTreeObserver
import androidx.annotation.IntRange
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.flickrtestapp.R

class EndlessRecyclerView(context: Context, attrSet: AttributeSet?, defStyle: Int) :
    RecyclerView(context, attrSet, defStyle) {
    val progressLayoutId: Int
    private val loadingAdapterDataObserver = LoadingAdapterDataObserver()
    private var mVisibleThreshold = DEFAULT_VISIBLE_THRESHOLD
    var isEndlessScrollEnable = true
        set(value) {
            if (field != value) {
                field = value
                setEndlessScrollEnableInner()
            }
        }
    var isLoading = true
    var endlessScrollListener: EndlessScrollListener? = null
        set(value) {
            if (field == null && value != null) {
                field = value
                attachEndlessScrollListener()
            } else if (value == null) {
                detachEndlessScrollListener()
            }
        }
    private var endlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener? = null
    var onLoadMoreRunnable: Runnable? = null
    private var adapter: ProgressAdapterWrapper? = null
    private var previousTotal = 0

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)

    override fun getAdapter(): Adapter<*>? {
        return if (adapter == null) null else adapter!!.innerAdapter
    }

    override fun setAdapter(adapter: Adapter<ViewHolder>?) {
        if (this.adapter != null) {
            this.adapter!!.unregisterAdapterDataObserver(loadingAdapterDataObserver)
        }
        if (adapter == null) {
            this.adapter = null
        } else {
            val progressEnable = isEndlessScrollEnable && progressLayoutId != 0
            this.adapter = ProgressAdapterWrapper(this, adapter, false)
        }
        super.setAdapter(this.adapter)
        isLoading = false
        if (this.adapter != null) {
            this.adapter!!.registerAdapterDataObserver(loadingAdapterDataObserver)
            attachEndlessScrollListener()
        } else {
            detachEndlessScrollListener()
        }
        viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    loadMore()
                }
            })
    }

    override fun setLayoutManager(layoutManager: LayoutManager?) {
        super.setLayoutManager(layoutManager)
        if (layoutManager == null) {
            detachEndlessScrollListener()
        } else {
            attachEndlessScrollListener()
        }
    }

    private fun detachEndlessScrollListener() {
//        if (adapter != null) {
//            adapter!!.setProgressEnable(false)
//        }
        if (onLoadMoreRunnable != null) {
            HANDLER.removeCallbacks(onLoadMoreRunnable!!)
            onLoadMoreRunnable = null
        }
        isLoading = false
        if (endlessRecyclerOnScrollListener != null) {
            removeOnScrollListener(endlessRecyclerOnScrollListener!!)
            endlessRecyclerOnScrollListener = null
        }
    }

    private fun attachEndlessScrollListener() {
        if (!isEndlessScrollEnable) {
            return
        }
        if (adapter != null && layoutManager != null && endlessScrollListener != null) {
            if (endlessRecyclerOnScrollListener == null) {
                endlessRecyclerOnScrollListener = EndlessRecyclerOnScrollListener()
            }
            addOnScrollListener(endlessRecyclerOnScrollListener!!)
//            if (adapter != null) {
//                adapter!!.setProgressEnable(true)
//            }
        }
        loadMore()
    }

    @get:IntRange(from = 1)
    var visibleThreshold: Int
        get() = mVisibleThreshold
        set(threshold) {
            require(mVisibleThreshold >= 1) { "Visible threshold must be positive value." }
            if (endlessRecyclerOnScrollListener == null) {
                if (mVisibleThreshold != threshold) {
                    mVisibleThreshold = threshold
                    detachEndlessScrollListener()
                    attachEndlessScrollListener()
                }
            } else {
                throw UnsupportedOperationException(
                    "Changing visible threshold is only " +
                            "possible when RecyclerView doesn't have adapter and layout manager."
                )
            }
        }

    private fun setEndlessScrollEnableInner() {
        if (isEndlessScrollEnable) {
            attachEndlessScrollListener()
        } else {
            detachEndlessScrollListener()
        }
    }

    fun loadMore() {
        if (!isEndlessScrollEnable || endlessScrollListener == null) {
            return
        }
        val visibleItemCount = childCount
        val totalItemCount = layoutManager!!.itemCount
        val firstVisibleItem = findFirstVisibleItemPosition(
            layoutManager!!
        )
        if (firstVisibleItem < 0) {
            return
        }
        if (isLoading && totalItemCount > previousTotal) {
            isLoading = false
            previousTotal = totalItemCount
        }
        if (!isLoading
            && totalItemCount - visibleItemCount <= firstVisibleItem + mVisibleThreshold
        ) {
            if (onLoadMoreRunnable != null) {
                HANDLER.removeCallbacks(onLoadMoreRunnable!!)
            }
            onLoadMoreRunnable = OnLoadMoreRunnable()
            HANDLER.post(onLoadMoreRunnable as OnLoadMoreRunnable)
            isLoading = true
        }
    }

    interface EndlessScrollListener {
        fun onLoadMore()
    }

    inner class EndlessRecyclerOnScrollListener : OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dx != 0 || isVertical(layoutManager!!)) {
                loadMore()
            }
        }
    }

    private inner class OnLoadMoreRunnable : Runnable {
        override fun run() {
            if (isEndlessScrollEnable && endlessScrollListener != null && context != null) {
                endlessScrollListener?.onLoadMore()
            }
            onLoadMoreRunnable = null
        }
    }

    private inner class LoadingAdapterDataObserver : AdapterDataObserver() {
        override fun onChanged() {
            resetLoadingAndLoadMore()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            resetLoadingAndLoadMore()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            resetLoadingAndLoadMore()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            resetLoadingAndLoadMore()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            resetLoadingAndLoadMore()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            resetLoadingAndLoadMore()
        }

        fun resetLoadingAndLoadMore() {
            isLoading = false
            loadMore()
        }
    }

    companion object {
        const val DEFAULT_VISIBLE_THRESHOLD = 10
        private val HANDLER = Handler(Looper.getMainLooper())
        private fun findFirstVisibleItemPosition(layoutManager: LayoutManager): Int {
            return if (layoutManager is LinearLayoutManager) {
                layoutManager.findFirstVisibleItemPosition()
            } else {
                if (layoutManager is StaggeredGridLayoutManager) {
                    val positions = layoutManager.findFirstVisibleItemPositions(null)
                    Log.e("positions", positions.contentToString())
                    positions.maxOf { it }
                } else {
                    throw UnsupportedOperationException()
                }
            }
        }

        private fun isVertical(layoutManager: LayoutManager): Boolean {
            return if (layoutManager is LinearLayoutManager) {
                layoutManager.orientation == LinearLayoutManager.VERTICAL
            } else {
                if (layoutManager is StaggeredGridLayoutManager) {
                    layoutManager.orientation == StaggeredGridLayoutManager.VERTICAL
                } else {
                    throw UnsupportedOperationException()
                }
            }
        }
    }

    init {
        val attrs = context.obtainStyledAttributes(
            attrSet, R.styleable.EndlessRecyclerView, defStyle, 0
        )
        isEndlessScrollEnable =
            attrs.getBoolean(R.styleable.EndlessRecyclerView_erv_endlessScrollEnabled, true)
        mVisibleThreshold = attrs.getInt(
            R.styleable.EndlessRecyclerView_erv_visibleThreshold,
            DEFAULT_VISIBLE_THRESHOLD
        )
        progressLayoutId = 0/*attrs.getResourceId(
            R.styleable.EndlessRecyclerView_erv_progressLayout,
            R.layout.erv_progress
        )*/
        attrs.recycle()
        setEndlessScrollEnableInner()
    }
}