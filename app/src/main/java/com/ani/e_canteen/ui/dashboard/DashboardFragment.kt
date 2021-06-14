package com.ani.e_canteen.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ani.e_canteen.R
import com.ani.e_canteen.databinding.FragmentDashboardBinding
import com.ani.e_canteen.ui.dashboard.menu.DessertActivity
import com.ani.e_canteen.ui.dashboard.menu.DrinkActivity
import com.ani.e_canteen.ui.dashboard.menu.FoodActivity
import com.ani.e_canteen.ui.dashboard.menu.SnackActivity
import org.jetbrains.anko.support.v4.startActivity


class DashboardFragment : Fragment() {

    lateinit var binding : FragmentDashboardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_dashboard,container,false)
        binding.lifecycleOwner = this

        binding.imgFood.setOnClickListener {
            startActivity<FoodActivity>()
        }

        binding.imgDrink.setOnClickListener {
            startActivity<DrinkActivity>()
        }

        binding.imgSnack.setOnClickListener {
            startActivity<SnackActivity>()
        }

        binding.imgDessert.setOnClickListener {
            startActivity<DessertActivity>()
        }




        return  binding.root
    }


}