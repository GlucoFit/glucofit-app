package com.fitcoders.glucofitapp.view.fragment.favorite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.FragmentFavoriteBinding
import com.fitcoders.glucofitapp.utils.adapter.FavoriteAdapter
import com.fitcoders.glucofitapp.utils.adapter.FoodAdapter
import com.fitcoders.glucofitapp.view.ViewModelFactory
import com.fitcoders.glucofitapp.view.activity.fooddetail.FoodDetailActivity
import com.fitcoders.glucofitapp.view.activity.main.MainActivity
import com.fitcoders.glucofitapp.view.fragment.history.HistoryFragment

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var modelFactory: ViewModelFactory
    private val favoriteViewModel: FavoriteViewModel by viewModels { modelFactory }
    private var isListLayout = false

    private val favoriteAdapter: FavoriteAdapter by lazy {
        FavoriteAdapter(
            onItemClicked = { item ->
              val intent = Intent(requireContext(), FoodDetailActivity::class.java)
              intent.putExtra("food", item.food)
               startActivity(intent)
            },
            onFavoriteClicked = { item, isFavorite ->
                item.id?.let {
                   favoriteViewModel.markAsFavorite(it, if (isFavorite) 1 else 0 )
                }
            },
            isListView = isListLayout
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        modelFactory = ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set judul dan tombol kembali
        val titleText: TextView = binding.root.findViewById(R.id.titleText)
        val backButton: ImageButton = binding.root.findViewById(R.id.backButton)

        titleText.text = "Favorite Foods"

        backButton.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
        }

        setupRecyclerView()
        observeViewModel()
        // Muat data favorit saat tampilan dibuat
        favoriteViewModel.fetchFavoriteFoods()


        binding.toggleButton.setOnClickListener {
            toggleLayout()
        }
    }

    private fun setupRecyclerView() {
        binding.rvFavorite.apply {
            adapter = favoriteAdapter
            layoutManager = if (isListLayout) {
                LinearLayoutManager(context)
            } else {
                GridLayoutManager(context, 2)
            }
        }
    }


    private fun observeViewModel() {
        // Mengamati hasil respons favorit
        favoriteViewModel.favoriteFoods.observe(viewLifecycleOwner) { result ->
            result.onSuccess { favoriteFoods ->
                Log.d("FavoriteFragment", "Favorite data loaded successfully: ${favoriteFoods.size} items")
                favoriteAdapter.submitList(favoriteFoods)
            }.onFailure { exception ->
                Log.e("FavoriteFragment", "Failed to load favorite foods: ${exception.message}", exception)
                Toast.makeText(requireContext(), "Failed to load favorite foods: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun toggleLayout() {
        isListLayout = !isListLayout
        favoriteAdapter.setViewType(isListLayout)

        binding.rvFavorite.layoutManager = if (isListLayout) {
            LinearLayoutManager(context)
        } else {
            GridLayoutManager(context, 2)
        }

        binding.toggleButton.setImageResource(
            if (isListLayout) R.drawable.ic_window else R.drawable.ic_table
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoriteFragment()
    }
}



