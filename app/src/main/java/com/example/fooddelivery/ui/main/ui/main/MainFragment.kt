package com.example.fooddelivery.ui.main.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.database.entity.Product
import com.example.domain.models.Response
import com.example.fooddelivery.R
import com.example.fooddelivery.databinding.MainFragmentBinding
import com.example.fooddelivery.ui.main.base.*
import com.example.fooddelivery.ui.main.ui.cart.CartFragment
import com.example.fooddelivery.ui.main.ui.viewPager.ScreenSlidePagerAdapter
import com.example.fooddelivery.ui.main.utils.*
import com.gadgeon.webcardio.operations.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent


class MainFragment : BaseFragment(), KoinComponent, ProjectEventListeners.ItemEvents {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var mutableList: MutableList<Product>
    private lateinit var dataAdapter: CollapsingRecyclerViewDataAdapter
    private lateinit var mBinder: MainFragmentBinding
    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinder = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)

        return mBinder.root
    }

    override fun viewReady() {
        subscribeToData()
        viewModel.productListLiveData.observe(this, {
            fab.count = it.size
        })
        main_content.setOnTouchListener(object : OnSwipeTouchListener(main_content.context) {

            override fun onSwipeLeft() {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, CartFragment())
                    ?.commitNow()
            }

            override fun onSwipeRight() {
                snackbar("Swipe Right", mBinder.root)
            }
        })
        fab.setOnClickListener {
            if (viewModel.allRecords?.isNotEmpty() == true) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, CartFragment())
                    ?.addToBackStack(CartFragment.TAG)
                    ?.commitAllowingStateLoss()
            } else {
                snackbar("No items added to cart", mBinder.root)
            }
        }
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)

    }

    private fun handleViewState(viewState: ViewState<Response>) {
        when (viewState) {
            is Loading -> showProgressBar()
            is Success -> onSuccess(viewState.data)
            is Error -> handleError(viewState.error)
            is NoInternetState -> showNoInternetError()
        }
    }

    private fun onSuccess(data: Response) {
        val listOfProducts = data.products
        if (!listOfProducts.isNullOrEmpty() && listOfProducts.first() is Product) {
            mutableList = listOfProducts as MutableList<Product>
            viewModel.updateCartWithList(mutableList)
            with(collapsing_toolbar_recycler_view) {
                val linearLayoutManager = LinearLayoutManager(context)
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                this.layoutManager = linearLayoutManager
                dataAdapter = CollapsingRecyclerViewDataAdapter(mutableList, this@MainFragment)
                this.adapter = dataAdapter
            }

        }

        dismissProgressBar()
    }

    private fun handleError(error: String?) {
        dismissProgressBar()
        if (error != null) {
            snackbar(error, mBinder.root)
        }
        mBinder.root.hideKeyboard()
    }

    private fun showNoInternetError() {
        dismissProgressBar()
        mBinder.root.hideKeyboard()
        snackbar(getString(R.string.no_internet_error_message), mBinder.root)
    }

    override fun getLayout(): Int {
        return R.layout.main_fragment
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getAllData("")
        activity?.let {
            viewPager.adapter = ScreenSlidePagerAdapter(it)
            TabLayoutMediator(tab_layout, viewPager) { tab, position ->

            }.attach()
        }


    }

    override fun onAddedToCart(product: Product) {
        viewModel.updateCart(product)
        //product.productName?.let { snackbar(it, mBinder.root) }
    }

}