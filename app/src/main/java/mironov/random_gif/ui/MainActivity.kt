package mironov.random_gif.ui




import android.os.Bundle
import android.os.Debug
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import mironov.random_gif.*
import mironov.random_gif.contract.Navigator
import mironov.random_gif.model.GifObject


class MainActivity : AppCompatActivity(), Navigator {


    private lateinit var navController: NavController

    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHost.navController

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)

        //Debug.waitForDebugger()
    }

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is NavHostFragment) return
            currentFragment = f
            updateUi()
        }
    }

    private fun updateUi() {
        val fragment = currentFragment

        if (navController.currentDestination?.id == navController.graph.startDestination) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun showGifInfoScreen(gif: GifObject) {
        launchDestination(R.id.gifInfoFragment,GifInfoFragment.createArgs(gif))
    }

    private fun launchDestination(destinationId: Int, args: Bundle? = null) {
        navController.navigate(
            destinationId,
            args,
            navOptions {
                anim {
                    enter = R.anim.slide_in
                    exit = R.anim.fade_out
                    popEnter = R.anim.fade_in
                    popExit = R.anim.slide_out
                }
            }
        )
    }

    override fun <T : Parcelable> showGif(result: T) {
        supportFragmentManager.setFragmentResult(result.javaClass.name, bundleOf(KEY_RESULT to result))
    }

    companion object {
        @JvmStatic private val KEY_RESULT = "RESULT"
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }
}