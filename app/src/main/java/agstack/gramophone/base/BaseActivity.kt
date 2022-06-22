package agstack.gramophone.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


   fun <T> openActivity(cls :Class<T>,extras:Bundle?){
        Intent(this,cls).apply {

            if(extras!=null){
                putExtras(extras)
                startActivity(this)
            }
        }
    }



}