package com.ani.e_canteen.ui.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.ani.e_canteen.R
import com.ani.e_canteen.databinding.FragmentMyOrderBinding


class MyOrderFragment : SuperBottomSheetFragment() {

    lateinit var binding: FragmentMyOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_order, container, false)
        binding.lifecycleOwner = this

        binding.btnOvo.setOnClickListener {
            binding.bulatputihOvo.setBackgroundResource(R.drawable.ellipse_hitam)
            binding.bulatputihDana.setBackgroundResource(R.drawable.ellipse)
            binding.bulatputihGopay.setBackgroundResource(R.drawable.ellipse)
            binding.bulatputihVirtual.setBackgroundResource(R.drawable.ellipse)
        }

        binding.btnDana.setOnClickListener {
            binding.bulatputihOvo.setBackgroundResource(R.drawable.ellipse)
            binding.bulatputihDana.setBackgroundResource(R.drawable.ellipse_hitam)
            binding.bulatputihGopay.setBackgroundResource(R.drawable.ellipse)
            binding.bulatputihVirtual.setBackgroundResource(R.drawable.ellipse)
        }

        binding.btnGopay.setOnClickListener {
            binding.bulatputihOvo.setBackgroundResource(R.drawable.ellipse)
            binding.bulatputihDana.setBackgroundResource(R.drawable.ellipse)
            binding.bulatputihGopay.setBackgroundResource(R.drawable.ellipse_hitam)
            binding.bulatputihVirtual.setBackgroundResource(R.drawable.ellipse)
        }

        binding.btnVirtual.setOnClickListener {
            binding.bulatputihOvo.setBackgroundResource(R.drawable.ellipse)
            binding.bulatputihDana.setBackgroundResource(R.drawable.ellipse)
            binding.bulatputihGopay.setBackgroundResource(R.drawable.ellipse)
            binding.bulatputihVirtual.setBackgroundResource(R.drawable.ellipse_hitam)
        }



        binding.imageButton.text = "Total order Rp. ${CartFragment.total_belanja.toString()}"
        return binding.root
    }


}