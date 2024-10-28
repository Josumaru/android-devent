package id.overlogic.devent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import id.overlogic.devent.R
import id.overlogic.devent.data.Result
import id.overlogic.devent.data.local.entity.EventEntity
import id.overlogic.devent.databinding.ActivityDetailBinding
import id.overlogic.devent.ui.favourite.FavouriteViewModel
import id.overlogic.devent.ui.favourite.ViewModelFactory as FavouriteFactory


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var favViewModel: FavouriteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnRegister.visibility = View.INVISIBLE


        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        val factoryFav: FavouriteFactory = FavouriteFactory.getInstance(this)
        favViewModel = ViewModelProvider(this, factoryFav)[FavouriteViewModel::class.java]


        val intent = intent
        val id = intent.getStringExtra("id")

        detailViewModel.getDetailEvent(id?.toInt() ?: 8722).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.pbLoading.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        setDetail(result.data)
                    }

                    is Result.Error -> {
                        binding.pbLoading.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }


    fun setDetail(event: EventEntity) {
        var isBookmark = false
        binding.tvName.text = event.name
        Glide.with(this@DetailActivity)
            .load(event.mediaCover)
            .into(binding.ivCover)
        binding.tvDescription.text = HtmlCompat.fromHtml(
            event.description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        detailViewModel.getBookmarkEvent().observe(this) { bookmarks ->
            if(bookmarks is Result.Success) {
                bookmarks.data.forEach { bookmark ->
                    if(bookmark.id == event.id) {
                        binding.btnFav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favourite_1, 0, 0, 0)
                        isBookmark = true
                    }
                }
            }

        }


        fun onClick() {
            if(isBookmark) {
                binding.btnFav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favourite, 0, 0, 0)
                detailViewModel.setBookmark(0, event.id)
            } else {
                binding.btnFav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favourite_1, 0, 0, 0)
                detailViewModel.setBookmark(1, event.id)
            }
            favViewModel.getBookmark()
        }

        binding.btnFav.setOnClickListener {
            onClick()
        }
        binding.tvTime.text = getString(R.string.dilaksanakan_pada, event.beginTime.split(" ")[0])
        binding.tvOwner.text = event.ownerName
        binding.tvQuota.text =
            getString(R.string.kuota, (event.quota - event.registrants).toString())
        binding.btnRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(event.link)
            startActivity(intent)
        }
        binding.btnRegister.visibility = View.VISIBLE
        binding.pbLoading.visibility = View.INVISIBLE
    }


}