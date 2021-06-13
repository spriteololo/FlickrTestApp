package com.example.flickrtestapp.ui.main

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.Keep
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.flickrtestapp.Constants
import com.example.flickrtestapp.R
import com.example.flickrtestapp.data.exceptions.FlickrApiException
import com.example.flickrtestapp.data.exceptions.NoInternetException
import com.example.flickrtestapp.data.vo.MainScreenVo
import com.example.flickrtestapp.data.vo.PhotoVo
import com.example.flickrtestapp.ui.AppActivity
import com.example.flickrtestapp.ui.common.BaseInstanceSaverFragment
import com.example.flickrtestapp.ui.common.ItemClickListener
import com.example.flickrtestapp.ui.details.DetailsFragment
import com.example.flickrtestapp.ui.view.recyclerview.EndlessRecyclerView
import com.example.flickrtestapp.util.ActivityUtils
import com.example.flickrtestapp.util.extensions.addOnTextChangedListener
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.ext.android.get
import java.util.*

class MainFragment : BaseInstanceSaverFragment(), MainView, ItemClickListener<PhotoVo>,
    EndlessRecyclerView.EndlessScrollListener, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var binding: MainFragmentBinding

    private var nextPage = 1
    private val listAll = mutableListOf<PhotoVo>()

    private var isSearchActive = false
        set(value) {
            if (field != value) {
                field = value
                nextPage = 1
                if (!field) {
                    sendRequest()
                }
            }
        }

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter(): MainPresenter = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (::binding.isInitialized && binding.mainScreenVo?.query?.isNotBlank() == true) {
                        clearSearch()
                    } else {
                        isEnabled = false
                        activity?.onBackPressed()
                    }
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppActivity)?.showAppBarAndBackBtn(appBarVisibility = true, visible = false)
        if (savedInstanceState != null) {
            nextPage = savedInstanceState.getInt(SAVE_STATE_CURRENT_PAGE)
            isSearchActive = savedInstanceState.getBoolean(SAVE_STATE_SEARCH_ACTIVE, false)
            savedInstanceState.getParcelableArrayList<PhotoVo>(SAVE_STATE_LIST)?.let { list ->
                listAll.addAll(list)
            }

            binding.mainScreenVo = savedInstanceState.getParcelable(SAVE_STATE_CURRENT_VO)
        }

        if (binding.mainScreenVo == null) binding.mainScreenVo = MainScreenVo()
        if (!::photoAdapter.isInitialized) {
            initAdapter()
            if (savedInstanceState == null) {
                sendRequest()
            }

            binding.etSearch.addOnTextChangedListener { query ->
                if (binding.mainScreenVo!!.query != query) {
                    binding.mainScreenVo!!.query = query
                    if (query.isNotBlank()) {
                        isSearchActive = true
                        onRefresh()
                    } else {
                        isSearchActive = false
                    }
                }
            }
        }
        binding.closeSearch.setOnClickListener {
            clearSearch()
        }
        binding.srlPhotos.setOnRefreshListener(this)
    }

    override fun onNetworkStateChanged(networkAvailable: Boolean) {
        rv_photos.internetConnected = networkAvailable
        if(networkAvailable && listAll.isEmpty()) onRefresh()
    }

    private fun clearSearch() {
        binding.etSearch.setText(Constants.EMPTY_STRING)
        ActivityUtils.closeKeyboard(activity)
        binding.etSearch.clearFocus()
    }

    private fun sendRequest() {
        showProgress(true)
        binding.mainScreenVo?.query?.let { query ->
            if (query.isNotBlank()) {
                presenter.searchPhotos(query, nextPage)
            } else null
        } ?: presenter.getRecentPictures(nextPage)
    }

    private fun initAdapter() {
        val rvPhotos = binding.rvPhotos
        photoAdapter = PhotoAdapter(
            listAll,
            this
        )
        rvPhotos.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            photoAdapter.itemsGoUp = scrollY > oldScrollY
        }
        rvPhotos.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        rvPhotos.setAdapter(photoAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
        rvPhotos.endlessScrollListener = this
    }

    override fun updateRecent(
        items: List<PhotoVo>,
        clearItems: Boolean,
        hasNextPage: Boolean,
        resultCount: Int
    ) {
        binding.mainScreenVo!!.resultCount = if (isSearchActive) resultCount else null
        binding.srlPhotos.isRefreshing = false
        showProgress(false)
        if (clearItems) {
            binding.rvPhotos.apply { post { scrollToPosition(0) } }
        }
        photoAdapter.setItems(items, clearItems)
        if (hasNextPage) {
            nextPage++
        } else {
            nextPage = REJECT_PAGE
        }
    }

    override fun showError(exception: Throwable) {
        when (exception) {
            is FlickrApiException -> Toast.makeText(context, exception.message, Toast.LENGTH_SHORT)
                .show()
            is NoInternetException -> {}
            else -> Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT)
                .show()
        }
        rv_photos.isLoading = false
        showProgress(false)
    }

    override fun onItemClick(view: View, item: PhotoVo) {
        val imageView = view.findViewById<View>(R.id.photo)
        val textView = view.findViewById<View>(R.id.title)
        val extras = FragmentNavigatorExtras(
            imageView to imageView.transitionName,
            textView to textView.transitionName
        )
        findNavController().navigate(
            R.id.action_MainFragment_to_DetailsFragment,
            DetailsFragment.createBundle(item),
            null,
            extras
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SAVE_STATE_CURRENT_PAGE, nextPage)
        outState.putBoolean(SAVE_STATE_SEARCH_ACTIVE, isSearchActive)
        if (listAll.isNotEmpty()) {
            (listAll as? ArrayList<out Parcelable>)?.let { list ->
                outState.putParcelableArrayList(SAVE_STATE_LIST, list)
            }
        }
        if (::binding.isInitialized && binding.mainScreenVo != null) {
            outState.putParcelable(SAVE_STATE_CURRENT_VO, binding.mainScreenVo)

        }
        super.onSaveInstanceState(outState)
    }

    override fun onLoadMore() {
        if (nextPage == REJECT_PAGE) {
            return
        }
        sendRequest()
    }

    private fun showProgress(visible: Boolean) {
        binding.pbPhotos.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun onRefresh() {
        nextPage = 1
        sendRequest()
    }

    @Keep
    companion object {
        private const val SAVE_STATE_LIST = "SAVE_STATE_LIST"
        private const val SAVE_STATE_CURRENT_PAGE = "SAVE_STATE_CURRENT_PAGE"
        private const val SAVE_STATE_SEARCH_ACTIVE = "SAVE_STATE_SEARCH_ACTIVE"
        private const val SAVE_STATE_CURRENT_VO = "SAVE_STATE_CURRENT_VO"
        const val REJECT_PAGE = 0
    }
}