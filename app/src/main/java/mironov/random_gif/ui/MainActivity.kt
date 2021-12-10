package mironov.random_gif.ui



import android.os.Bundle
import android.os.Debug
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import mironov.random_gif.*
import mironov.random_gif.contract.Navigator
import mironov.random_gif.model.GifObject


class MainActivity : AppCompatActivity(), Navigator {

    private val currentFragment: Fragment
        get() = supportFragmentManager.findFragmentById(R.id.fragmentContainer)!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragmentContainer, GifListFragment())
            .commit()

            //Debug.waitForDebugger()
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun showGifInfoScreen(gif: GifObject) {
        launchFragment(GifInfoFragment.newInstance(gif))
    }

    override fun <T : Parcelable> showGif(result: T) {
        supportFragmentManager.setFragmentResult(result.javaClass.name, bundleOf(KEY_RESULT to result))
    }

    companion object {
        @JvmStatic private val KEY_RESULT = "RESULT"
    }
}