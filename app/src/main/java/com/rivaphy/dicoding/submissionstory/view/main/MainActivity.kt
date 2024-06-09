package com.rivaphy.dicoding.submissionstory.view.main


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rivaphy.dicoding.submissionstory.R
import com.rivaphy.dicoding.submissionstory.data.api.ApiConfig
import com.rivaphy.dicoding.submissionstory.databinding.ActivityMainBinding
import com.rivaphy.dicoding.submissionstory.view.UserViewModelFactory
import com.rivaphy.dicoding.submissionstory.view.ViewModelFactory
import com.rivaphy.dicoding.submissionstory.view.maps.MapsActivity
import com.rivaphy.dicoding.submissionstory.view.story.StoryActivity
import com.rivaphy.dicoding.submissionstory.view.story.TellYoursViewModel
import com.rivaphy.dicoding.submissionstory.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val tellYoursViewModel by viewModels<TellYoursViewModel> {
        UserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()

        val layoutManager = LinearLayoutManager(this)
        binding.rvMain.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvMain.addItemDecoration(itemDecoration)

        binding.fbMainUpload.setOnClickListener {
            val intent = Intent(this, StoryActivity::class.java)
            startActivity(intent)
        }

        setupView()
        setupAction()
    }

    private fun setupAction() {
        val mainAdapter = MainAdapter()
        binding.rvMain.adapter = mainAdapter

        showLoading(true)

        viewModel.getSession().observe(this) {
            if (!it.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                ApiConfig.setToken(it.token)
            }
        }

        tellYoursViewModel.pagingStory.observe(this) {
            mainAdapter.submitData(lifecycle, it)
            showLoading(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pgMain.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_menu_logout -> {
                viewModel.logout()
            }

            R.id.item_menu_maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}