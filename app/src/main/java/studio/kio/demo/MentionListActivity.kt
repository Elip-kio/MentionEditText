package studio.kio.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_mention_list.*

class MentionListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mention_list)

        val users = arrayOf(
            User(1, "Daniel"),
            User(2, "Lee"),
            User(3, "Ronald"),
            User(4, "Vaughan"),
            User(5, "Gerald"),
            User(6, "Erik"),
            User(7, "Strawberry"),
            User(8, "Gerald"),
            User(9, "Ives"),
            User(10, "Royal"),
            User(11, "Crown"),
            User(12, "Warren"),
            User(13, "Philip"),
            User(14, "Gilbert"),
            User(15, "Rosemary"),
            User(16, "Timothea")
        )

        val userLabels = mutableListOf<String>()

        users.forEach {
            userLabels.add(it.name)
        }

        lv_users.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userLabels)
        title = "Select User"

        lv_users.setOnItemClickListener { _, _, position, _ ->
            intent.putExtra("user", users[position])
            setResult(0, intent)
            finish()
        }

    }
}