package studio.kio.mentionlibrary

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.Toast

/**
 * created by KIO on 2020/10/10
 */

class MentionEditText(context: Context, attributeSet: AttributeSet?) :
    androidx.appcompat.widget.AppCompatEditText(context, attributeSet) {

    private var onSelectionChangedListener: OnSelectionChangedListener? = null

    constructor(context: Context) : this(context, null)

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        onSelectionChangedListener?.onSelectionChanged(selStart, selEnd)
    }

    fun setOnSelectionChangedListener(onSelectionChangedListener: OnSelectionChangedListener) {
        this.onSelectionChangedListener = onSelectionChangedListener
    }


    interface OnSelectionChangedListener {
        fun onSelectionChanged(selStart: Int, selEnd: Int)
    }

}