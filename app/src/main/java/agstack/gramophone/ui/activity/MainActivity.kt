package agstack.gramophone.ui.activity

import agstack.gramophone.R
import agstack.gramophone.databinding.ActivityMainBinding
import agstack.gramophone.utils.SharedPref
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var sharedPref: SharedPref

   /* private val viewModel: MainViewModel by viewModel()*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*navController = findNavController(R.id.nav_host_fragment)*/

        sharedPref.isUserLoggedIn()

        val : Boolean = false;
    }
}
