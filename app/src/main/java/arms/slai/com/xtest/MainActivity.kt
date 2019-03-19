package arms.slai.com.xtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import arms.slai.com.xtest.interfaces.IMainPresenter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val presenter : IMainPresenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // Activity is on screen
    override fun onResume() {
        super.onResume()
        setupHello()
    }

    // Setup the clickable part of hello in hello world for task 1
    private fun setupHello() {
        val text = "Hello world!"
        val clickable = object : ClickableSpan() {
            override fun onClick(view: View) {
                // Snackbar is better
                Toast.makeText(applicationContext, "Task 1 done! Doing work", Toast.LENGTH_LONG).show()
                presenter.setupLocationWork()
            }
        }
        val builder = SpannableStringBuilder(text)
        builder.setSpan(clickable,0, 5, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)
        id_text_view.text = builder
        id_text_view.movementMethod = LinkMovementMethod.getInstance()
    }

    // Respond to permissions
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // Activity is destroyed, remove references and clean up
    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }
}
