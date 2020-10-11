package studio.kio.demo

import android.content.Intent
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import studio.kio.mentionlibrary.MentionEditTextHandler
import studio.kio.mentionlibrary.MentionHandlerBuilder
import studio.kio.mentionlibrary.MentionHandlerBuilder.OnMentionInsertedListener
import studio.kio.mentionlibrary.MentionUtil

class MainActivity : AppCompatActivity() {

    private lateinit var mentionHandler: MentionEditTextHandler<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        this.mentionHandler = MentionUtil.withType(User::class)
            .onMention(object : OnMentionInsertedListener<User> {
                override fun onMentionInserted(position: Int) {
                    val intent = Intent(this@MainActivity, MentionListActivity::class.java)
                    intent.putExtra("position", position)
                    startActivityForResult(intent, 0)
                }
            })
            .decorate(object : MentionHandlerBuilder.MentionDecorator {
                override fun getSpan(): Any {
                    return ForegroundColorSpan((0xff000000 or (Math.random() * Int.MAX_VALUE).toLong()).toInt())
                }
            })
            .tag('@')
            .attach(et_sample)
        bt_show.setOnClickListener {
            tv_mentions.text = mentionHandler.getMentionItems().toString()
        }

        bt_mention.setOnClickListener {
            mentionHandler.automaticallyAppend()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            val user = data.getSerializableExtra("user") as User
            mentionHandler.insert(user, user.name, data.getIntExtra("position", 0))
        }
    }

}
