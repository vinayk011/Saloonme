/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ezeetech.salonme.BuildConfig
import com.ezeetech.salonme.R
import com.ezeetech.salonme.adapter.AdapterBestDeal
import com.ezeetech.salonme.adapter.AdapterCategories
import com.ezeetech.salonme.databinding.FragmentUserHomeBinding
import com.ezeetech.salonme.listener.ItemClickListener
import com.ezeetech.salonme.model.BestDeal
import com.ezeetech.salonme.model.Slider
import com.ezeetech.salonme.model.StoreCategory
import com.ezeetech.salonme.network.model.NetworkResponse
import com.ezeetech.salonme.network_call.GetSlidersNetworkCall
import com.ezeetech.salonme.stores
import com.salonme.base.*
import io.paperdb.Paper
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import org.imaginativeworld.whynotimagecarousel.CarouselOnScrollListener
import org.imaginativeworld.whynotimagecarousel.OnItemClickListener
import java.util.*

class UserHomeFragment : BaseFragment<FragmentUserHomeBinding>() {
    private val categories = ArrayList<StoreCategory>()
    private val bestDeals = ArrayList<BestDeal>()
    private val adapterCategories by lazy {
        context?.let { AdapterCategories(it, categories, storeCategoryClickListener) }
    }

    private val adapterBestDeal by lazy {
        context?.let { AdapterBestDeal(it, bestDeals, bestDealClickListener) }
    }

    private val slidersNetworkCall by lazy {
        ViewModelProvider(this).get(GetSlidersNetworkCall::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as ActivityUserHome).appBar(show = true, back = false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    private val slidersNetworkResult = Observer<NetworkResponse<Boolean>> { res ->
        updateSliders()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflateFragment(
            inflater,
            container,
            R.layout.fragment_user_home
        ) as FragmentUserHomeBinding
        observeClick(root)
        return root
    }

    override fun init() {
        setDefaultData()
        slidersNetworkCall.getResult().observe(this, slidersNetworkResult)
        slidersNetworkCall.execute(null)
        context?.let {
            binding.recyclerViewCategory.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewBestDeals.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.recyclerViewCategory.adapter = adapterCategories
        binding.recyclerViewBestDeals.adapter = adapterBestDeal
        // Scroll listener
        binding.homeCarousel.onScrollListener = object : CarouselOnScrollListener {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int,
                position: Int,
                carouselItem: CarouselItem?
            ) {
                // ...
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // ...
            }
        }

        // Item click listener
        binding.homeCarousel.onItemClickListener = object : OnItemClickListener {
            override fun onClick(position: Int, carouselItem: CarouselItem) {
                // ...
            }

            override fun onLongClick(position: Int, dataObject: CarouselItem) {
                // ...
            }

        }
    }

    override fun onBackPressed() {
            (activity as ActivityUserHome).finish()
    }
    private fun updateSliders() {
        val sliders = Paper.book().read<List<Slider>>(DB_SLIDERS)
        sliders?.let { binding.homeCarousel.addData(getCarousalData(it)) }
    }

    private val storeCategoryClickListener = object : ItemClickListener<StoreCategory> {
        override fun onClicked(category: StoreCategory) {
            Trace.i("Selected Store:" + category.type)
            context?.let {
                stores(
                    it, bundle = bundleOf(
                        Pair(CATEGORY, category.type)
                    )
                )
            }
        }
    }


    private val bestDealClickListener = object : ItemClickListener<BestDeal>{
        override fun onClicked(bestDeal: BestDeal) {
           Trace.i("Selected item:"+bestDeal.title)
        }
    }

    /**
     * Get static list of images for temporary
     */
    private fun getCarousalData(sliders: List<Slider>): List<CarouselItem> {
        val list = mutableListOf<CarouselItem>()
        for(slider in sliders){
            list.add(CarouselItem(BuildConfig.WEB_SLIDER_URL + slider.image))
        }
        return list
    }

    // organise categories from cloud remove temp objects
    private fun setDefaultData() {
        context?.let {
            if (categories.isEmpty()) {
                categories.add(
                    StoreCategory(
                        ContextCompat.getDrawable(it, R.drawable.ic_category_men),
                        getString(R.string.category_men)
                    )
                )
                categories.add(
                    StoreCategory(
                        ContextCompat.getDrawable(it, R.drawable.ic_category_women),
                        getString(R.string.category_women)
                    )
                )
                categories.add(
                    StoreCategory(
                        ContextCompat.getDrawable(it, R.drawable.ic_category_kids),
                        getString(R.string.category_kids)
                    )
                )
            }

            if (bestDeals.isEmpty()) {
                bestDeals.add(
                    BestDeal(
                        ContextCompat.getDrawable(it, R.drawable.saloon_image),
                        getString(R.string.best_deals),
                        getString(R.string.deals_decs)
                    )
                )
                bestDeals.add(
                    BestDeal(
                        ContextCompat.getDrawable(it, R.drawable.saloon_image),
                        getString(R.string.best_deals),
                        getString(R.string.deals_decs)
                    )
                )
                bestDeals.add(
                    BestDeal(
                        ContextCompat.getDrawable(it, R.drawable.saloon_image),
                        getString(R.string.best_deals),
                        getString(R.string.deals_decs)
                    )
                )
                bestDeals.add(
                    BestDeal(
                        ContextCompat.getDrawable(it, R.drawable.saloon_image),
                        getString(R.string.best_deals),
                        getString(R.string.deals_decs)
                    )
                )
                bestDeals.add(
                    BestDeal(
                        ContextCompat.getDrawable(it, R.drawable.saloon_image),
                        getString(R.string.best_deals),
                        getString(R.string.deals_decs)
                    )
                )
            }
        }
    }

}
