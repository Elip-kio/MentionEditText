package studio.kio.mentionlibrary

import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.util.Log
import java.lang.IllegalStateException

/**
 * created by KIO on 2020/10/10
 */
class MentionEditTextHandler<T>(private val handlerBuilder: MentionHandlerBuilder<T>) {

    private var mentions = mutableListOf<MentionNode<T>>()

    init {
        handlerBuilder.editText?.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.e(
                    "onTextChanged:",
                    "s:${s.toString()}  start:${start} before:${before} count:${count}"
                )
                if (s == null)
                    return
                if (count == 1 && s[start] == handlerBuilder.mentionTag) {//用户输入了@
                    handlerBuilder.onMentionInsertListener?.onMentionInserted(start)
                }

                //长度变小了或者有被替换的部分
                if (count < before || before != 0) {
                    var i = 0
                    while (i < mentions.size) {
                        val it = mentions[i]
                        val mStart = handlerBuilder.editText?.editableText?.getSpanStart(it.span)!!
                        val mEnd = handlerBuilder.editText?.editableText?.getSpanEnd(it.span)!!

                        val needRemove: Boolean
                        Log.e("onTextChanged", "mStart:$mStart , mEnd:$mEnd")
                        needRemove = if (mStart == -1 || mEnd == -1) {
                            true //已没有span
                        } else (start == mEnd && mEnd - mStart == it.label.length + 1) //删除span中最右边一个字符

                        if (needRemove) {
                            handlerBuilder.editText?.editableText?.removeSpan(it.span)
                            mentions.remove(it)
                        } else {
                            i++
                        }

                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        handlerBuilder.editText?.setOnSelectionChangedListener(object :
            MentionEditText.OnSelectionChangedListener {
            override fun onSelectionChanged(selStart: Int, selEnd: Int) {
                Log.e("selectionChange", "${selStart}::${selEnd}")
                //碰撞检测，不允许光标在span中
                mentions.forEach {
                    val start = handlerBuilder.editText?.editableText?.getSpanStart(it.span)
                    val end = handlerBuilder.editText?.editableText?.getSpanEnd(it.span)
                    if (start != null && start != -1 && end != null && end != -1) {
                        var adjustSelStart = selStart
                        var adjustSelEnd = selEnd
                        var needAdjust = true

                        if (selStart > start && selEnd < end) {//span包含了光标
                            adjustSelStart = start
                            adjustSelEnd = end
                        } else if (selEnd > start && selEnd < end) {//光标与span的左边相交
                            adjustSelEnd = end
                        } else if (selStart > start && selStart < end) {//光标与span的右边相交
                            adjustSelStart = start
                        } else {
                            needAdjust = false
                        }
                        if (needAdjust) {
                            handlerBuilder.editText?.requestFocus()
                            handlerBuilder.editText?.setSelection(adjustSelStart, adjustSelEnd)
                        }
                    }

                }
            }
        })

    }

    fun insert(obj: T, label: String, labelStart: Int) {

        val e = handlerBuilder.editText?.editableText
        val span = handlerBuilder.decorator?.getSpan()

        val node = MentionNode(
            obj,
            label,
            span
        )

        mentions.add(node)
        e?.insert(labelStart + 1, "${node.label} ")

        e?.setSpan(
            span,
            labelStart, node.label.length + labelStart + 2,//这里的1是加上了前面的@与后面的空格
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        handlerBuilder.editText?.requestFocus()
    }

    fun automaticallyAppend() {
        val et = handlerBuilder.editText
        val e = et?.editableText
        val length = e?.length
        val tag = handlerBuilder.mentionTag
        if (length != null) {
            e.insert(et.selectionEnd, "$tag")
        }
    }

    fun getMentionItems(): List<MentionItem<T>> {
        val list = mutableListOf<MentionItem<T>>()
        mentions.forEach {
            val start = handlerBuilder.editText?.editableText?.getSpanStart(it.span)
            val end = handlerBuilder.editText?.editableText?.getSpanEnd(it.span)
            if (start == null || start == -1 || end == null || end == -1) {//找不到记录
                throw IllegalStateException("Could Not Find Out MentionItem for Input Text!")
            }
            list.add(MentionItem(it.obj, it.label, start, end))
        }
        return list
    }

    private data class MentionNode<T>(
        val obj: T,
        var label: String,
        val span: Any?
    )

    data class MentionItem<T>(
        val obj: T,
        val label: String,
        var start: Int,
        val end: Int
    )

}