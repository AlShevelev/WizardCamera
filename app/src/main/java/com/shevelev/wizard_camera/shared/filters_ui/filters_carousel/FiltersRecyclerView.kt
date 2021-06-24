package com.shevelev.wizard_camera.shared.filters_ui.filters_carousel

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.shared.filters_ui.display_data.FilterDisplayId
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

    private var onItemSelectedListener: ((FilterDisplayId) -> Unit)? = null
    private var lastPostId: FilterDisplayId? = null

    private var startId: FilterDisplayId? = null

    private var lastSelectedPosition: Int? = null

    private lateinit var filtersAdapter: FiltersAdapter

    fun setStartData(data: FiltersListData, eventsProcessor: FilterEventsProcessor) {
        startId = data.items[data.startPosition].displayData.id

        if(!::filtersAdapter.isInitialized) {
            filtersAdapter = FiltersAdapter(R.layout.view_filters_carousel_item, eventsProcessor)
            addAdapter(filtersAdapter)
        }

        filtersAdapter.setItems(data.items, data.startPosition)
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

    fun setOnItemSelectedListener(listener: ((FilterDisplayId) -> Unit)?) {
        onItemSelectedListener = listener
    }

    private fun addAdapter(newAdapter: FiltersAdapter) {
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        newAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                post {
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
                            onScrollChanged()
                        }

                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            super.onScrollStateChanged(recyclerView, newState)

                            if(newState == SCROLL_END) {
                                lastItemTag?.let {
                                    scrollToAbsolutePosition(it.position)

                                    if(currentScrollState == SCROLLING_FAST || currentScrollState == SCROLL_START) {
                                        postOnItemSelectedEvent(it.id, it.position)
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
            val startPosition = (adapter as FiltersAdapter).recalculatePosition(position)
            scrollToAbsolutePosition(startPosition)
        }
    }

    private fun onScrollChanged() {
        post {
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
                    maxScaleChildTag?.let {
                        postOnItemSelectedEvent(it.id, it.position)
                    }
                }
            }
        }
    }

    private fun getGaussianScale(childCenterX: Int, parentCenterX: Int): Float {
        val minScaleOffset = 1f
        val scaleFactor = 1f
        //val spreadFactor = 150.0
        val spreadFactor = 250.0

        return (Math.E.pow(
            -(childCenterX - parentCenterX.toDouble()).pow(2.0) / (2 * spreadFactor.pow(2.0))
        ) * scaleFactor + minScaleOffset).toFloat()
    }

    private fun postOnItemSelectedEvent(id: FilterDisplayId, position: Int) {
        if(lastPostId == id) {
            return
        }
        lastPostId = id

        // To filter the very first event
        if(startId == id) {
            startId = null
        } else {
            lastSelectedPosition?.let {
                filtersAdapter.setItemSelectionState(it, false)
            }

            lastSelectedPosition = position
            filtersAdapter.setItemSelectionState(position, true)

            onItemSelectedListener?.invoke(id)
        }
    }

    private fun scrollToAbsolutePosition(position: Int) {
        (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, offsetToCenterScroll)
    }
}