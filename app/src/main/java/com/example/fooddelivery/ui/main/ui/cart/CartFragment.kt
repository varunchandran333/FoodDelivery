package com.example.fooddelivery.ui.main.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.database.entity.Product
import com.example.fooddelivery.R
import com.example.fooddelivery.ui.main.ui.main.MainViewModel
import com.example.fooddelivery.ui.main.utils.ProjectEventListeners
import com.example.fooddelivery.ui.main.utils.snackbar
import com.gadgeon.webcardio.operations.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_cart_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A fragment representing a list of Cart Items.
 */
class CartFragment : BaseFragment(), ProjectEventListeners.CartEvents {
    private var listOfItems = ArrayList<Product>()
    private val viewModel: MainViewModel by viewModel()
    private lateinit var mAdapter: MyProductRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return inflater.inflate(getLayout(), container, false)
    }

    override fun viewReady() {
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.title = getString(R.string.cart)
        toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        listOfItems = viewModel.allRecords as ArrayList<Product>
        if (!listOfItems.isNullOrEmpty()) {
            textViewPrice.text = "${viewModel.getTotalPrice(listOfItems)} INR"
            with(recyclerCartList) {
                layoutManager = LinearLayoutManager(context)
                mAdapter =
                    MyProductRecyclerViewAdapter(listOfItems, this@CartFragment)
                adapter = mAdapter
            }
        }

    }

    override fun getLayout(): Int {
        return R.layout.fragment_cart_list
    }

    override fun onRemoveItem(position: Int) {
        viewModel.deleteCartItem(listOfItems[position])
        listOfItems.removeAt(position)
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, mAdapter.itemCount - position);
        if (listOfItems.isEmpty()) {
            view?.let { snackbar(getString(R.string.cart_empty), it) }
            activity?.supportFragmentManager?.popBackStack()
        } else {
            textViewPrice.text = "${viewModel.getTotalPrice(listOfItems)} INR"
        }
        Log.d("", "")
    }

    companion object {

        val TAG = CartFragment::class.java.simpleName

//        // TODO: Customize parameter argument names
//        const val ARG_COLUMN_COUNT = "column-count"
//
//        // TODO: Customize parameter initialization
//        @JvmStatic
//        fun newInstance(columnCount: Int) =
//            CartFragment().apply {
//                arguments = Bundle().apply {
//                    putInt(ARG_COLUMN_COUNT, columnCount)
//                }
//            }
    }
}