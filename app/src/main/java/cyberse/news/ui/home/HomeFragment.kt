package cyberse.news.ui.home

import cyberse.news.bases.BaseFragment
import cyberse.news.databinding.FragmentHomeBinding
import cyberse.news.extensions.openSourceFragment
import cyberse.news.models.Category
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val homeAdapter by lazy { HomeAdapter() }

    override fun setUpViews() {
        with(homeAdapter) {
            binding?.categories?.adapter = this
            setItems(Category.entries)
            setOnClickItemListener { _, _, item ->
                when (item) {
                    Category.APPLE -> openSourceFragment(item.name)
                    Category.TESLA -> openSourceFragment(item.name)
                    Category.BUSINESS -> openSourceFragment(item.name)
                    Category.TECH_CRUNCH -> openSourceFragment(item.name)
                    else -> openSourceFragment(item.name)
                }
            }
        }
    }

}