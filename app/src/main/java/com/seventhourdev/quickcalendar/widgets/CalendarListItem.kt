package com.seventhourdev.quickcalendar.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.seventhourdev.quickcalendar.R
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.TypedValue.applyDimension
import android.util.TypedValue




/**
 * Created by david on 12/1/2016.
 */
class CalendarListItem : RelativeLayout{
    constructor(context: Context) : this(context, null) {

    }

    constructor(context: Context, attrs: AttributeSet?): this(context,attrs, 0) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context,attrs,defStyleAttr) {
        inflate(context, R.layout.list_item_calendar_entry, this)

        val regularDrawable:GradientDrawable = GradientDrawable()
        regularDrawable.setColor(ContextCompat.getColor(context,R.color.colorPrimary))
        regularDrawable.cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                resources.getDimension(R.dimen.corner_radius), resources.displayMetrics)

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            val BackgroundDrawable: RippleDrawable = RippleDrawable(ColorStateList(
                    arrayOf(intArrayOf()),
                    intArrayOf(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            ), regularDrawable, null)
            background = BackgroundDrawable
        }else{
            val darkerDrawable:GradientDrawable = GradientDrawable();
            regularDrawable.setColor(ContextCompat.getColor(context,R.color.colorPrimaryDark))
            regularDrawable.cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    resources.getDimension(R.dimen.corner_radius), resources.displayMetrics)
            val stateListDrawable: StateListDrawable = StateListDrawable()
            stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed),darkerDrawable)
            stateListDrawable.addState(intArrayOf(android.R.attr.state_focused),darkerDrawable)
            stateListDrawable.addState(intArrayOf(),regularDrawable)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                background = stateListDrawable
            }else{
                setBackgroundDrawable(stateListDrawable)
            }
        }
    }

}