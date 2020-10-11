package studio.kio.mentionlibrary

/**
 * created by KIO on 2020/10/10
 */

class MentionHandlerBuilder<T> {

    var onMentionInsertListener: OnMentionInsertedListener<T>? = null
        private set

    var mentionTag = '@'
        private set

    var editText: MentionEditText? = null
        private set

    var decorator: MentionDecorator? = null
        private set

    fun onMention(onMentionInsertListener: OnMentionInsertedListener<T>): MentionHandlerBuilder<T> {
        this.onMentionInsertListener = onMentionInsertListener
        return this
    }

    fun decorate(decorator: MentionDecorator): MentionHandlerBuilder<T> {
        this.decorator = decorator
        return this
    }

    fun tag(mentionTag: Char): MentionHandlerBuilder<T> {
        this.mentionTag = mentionTag
        return this
    }

    fun attach(editText: MentionEditText): MentionEditTextHandler<T> {
        this.editText = editText
        return MentionEditTextHandler(this)
    }

    interface OnMentionInsertedListener<T> {
        fun onMentionInserted(position: Int)
    }

    interface MentionDecorator {
        fun getSpan(): Any
    }

}