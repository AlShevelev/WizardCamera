package com.shevelev.wizard_camera.main_activity.view.widgets.filters_carousel

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.main_activity.dto.FiltersListData
import com.shevelev.wizard_camera.main_activity.view_model.FilterEventsProcessor
import timber.log.Timber
import kotlin.math.pow

class FiltersRecyclerView(
    context: Context,
    attrs: AttributeSet
) : RecyclerView(context, attrs) {

    private companion object {
        const val SCROLL_START = 1
        const val SCROLL_END = 0
        const val SCROLLING_FAST = 2
    }

    private var offsetToCenterScroll = -1
    private var lastItemTag: FiltersItemTag? = null

    private var currentScrollState = -1

    private var onItemSelectedListener: ((FilterCode) -> Unit)? = null
    private var lastPostId: FilterCode? = null

    private var startCode: FilterCode? = null

    private lateinit var filtersAdapter: FiltersAdapter

    fun setStartData(data: FiltersListData, eventsProcessor: FilterEventsProcessor) {
        startCode = data.items[data.startPosition].displayData.code

        if(!::filtersAdapter.isInitialized) {
            filtersAdapter = FiltersAdapter(R.layout.view_filters_carousel_item, eventsProcessor)
            addAdapter(filtersAdapter)
        }

        filtersAdapter.setItems(data.items)
        setUp(data.startPosition)
    }

    override fun scrollToPosition(position: Int) =
        scrollToAbsolutePosition((adapter as FiltersAdapter).recalculatePosition(position))

    override fun smoothScrollToPosition(position: Int) {
        val recalculatedPosition = (adapter as FiltersAdapter).recalculatePosition(position)

        val scroller = object : LinearSmoothScroller(context) {
            override fun getHorizontalSnapPreference(): Int {
                return SNAP_TO_START
            }

            override fun calculateDtToFit(viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int): Int {
                return 0
            }
        }
        scroller.targetPosition = recalculatedPosition
        (layoutManager as LinearLayoutManager).startSmoothScroll(scroller)
    }

    fun setOnItemSelectedListener(listener: ((FilterCode) -> Unit)?) {
        onItemSelectedListener = listener
    }

    private fun addAdapter(newAdapter: FiltersAdapter) {
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        newAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                post {
                    Timber.tag("FAVORITES").d("addAdapter() -> onChanged()")
                    if(offsetToCenterScroll == -1) {
                        val child = getChildAt(0)
                        offsetToCenterScroll = -child.width/2
                    }

                    val sidePadding = (width / 2) - (getChildAt(0).width / 2)
                    setPadding(sidePadding, 0, sidePadding, 0)
                    scrollToAbsolutePosition(0)

                    addOnScrollListener(object : OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            Timber.tag("FAVORITES").d("addAdapter() -> onScrolled()")
                            onScrollChanged()
                        }

                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            super.onScrollStateChanged(recyclerView, newState)

                            if(newState == SCROLL_END) {
                                lastItemTag?.let {
                                    scrollToAbsolutePosition(it.position)

                                    if(currentScrollState == SCROLLING_FAST || currentScrollState == SCROLL_START) {
                                        postOnItemSelectedEvent(it.id)
                                    }
                                }
                            }
                            currentScrollState = newState
                        }
                    })
                }
            }
        })
        adapter = newAdapter
    }

    private fun setUp(position: Int) {
        post{
            Timber.tag("FAVORITES").d("setUp()")
            val startPosition = (adapter as FiltersAdapter).recalculatePosition(position)
            scrollToAbsolutePosition(startPosition)
        }
    }

    private fun onScrollChanged() {
        post {
            Timber.tag("FAVORITES").d("onScrollChanged()")
            val parentCenterX = width / 2

            var maxScale = Float.MIN_VALUE
            var maxScaleChildTag: FiltersItemTag? = null

            (0 until childCount).forEach { position ->
                val child = getChildAt(position)

                val childCenterX = child.left + child.width/2
                val scaleValue = getGaussianScale(childCenterX, parentCenterX)

                val alpha = scaleValue - 1f

                child.scaleX = scaleValue
                child.scaleY = scaleValue
                child.alpha = alpha

                if(scaleValue > maxScale) {
                    maxScale = scaleValue
                    maxScaleChildTag = child.tag as FiltersItemTag
                }
            }

            if(maxScaleChildTag != lastItemTag) {
                lastItemTag = maxScaleChildTag

                if(currentScrollState != SCROLLING_FAST) {
                    postOnItemSelectedEvent(maxScaleChildTag!!.id)
                }
            }
        }
    }

    private fun getGaussianScale(childCenterX: Int, parentCenterX: Int): Float {
        Timber.tag("FAVORITES").d("getGaussianScale()")
        val minScaleOffset = 1f
        val scaleFactor = 1f
        //val spreadFactor = 150.0
        val spreadFactor = 250.0

        return (Math.E.pow(
            -(childCenterX - parentCenterX.toDouble()).pow(2.0) / (2 * spreadFactor.pow(2.0))
        ) * scaleFactor + minScaleOffset).toFloat()
    }

    private fun postOnItemSelectedEvent(id: FilterCode) {
        if(lastPostId == id) {
            return
        }
        lastPostId = id

        // To filter the very first event
        if(startCode == id) {
            startCode = null
        } else {
            onItemSelectedListener?.invoke(id)
        }
    }

    private fun scrollToAbsolutePosition(position: Int) {
        Timber.tag("FAVORITES").d("scrollToAbsolutePosition($position)")
        (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, offsetToCenterScroll)
    }
}