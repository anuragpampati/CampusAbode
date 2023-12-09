package com.example.campusabode


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText


class FilterFragment : Fragment() {


    var myParent: FilterFragment.Parent? = null
    private var listener: OnFilterAppliedListener? = null
    interface Parent {

    }
    fun setParent(parent : Parent) {
        this.myParent = parent
    }
    interface OnFilterAppliedListener {
        fun onFilterApplied(selectedBedrooms: String, selectedBathrooms: String, minPrice: String, maxPrice : String)
    }

    private var filterListener: OnFilterAppliedListener? = null

    fun setFilterListener(listener: OnFilterAppliedListener) {
        this.filterListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filter, container, false)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnApply: Button = view.findViewById<Button>(R.id.btnApply)
        val closeBtn: Button = view.findViewById<Button>(R.id.closeBtn)

        btnApply.setOnClickListener {
            val bedroomRadioGroup: RadioGroup = view.findViewById(R.id.bedroomRadioGroup)
            val selectedBedRooms = getSelectedRadioButtonText(bedroomRadioGroup)

            val bathroomRadioGroup: RadioGroup = view.findViewById(R.id.bathroomRadioGroup)
            val selectedBathRooms = getSelectedRadioButtonText(bathroomRadioGroup)

            val minPrice :TextInputEditText = view.findViewById(R.id.minPrice)
            val maxPrice :TextInputEditText = view.findViewById(R.id.maxPrice)

            filterListener?.onFilterApplied(selectedBedRooms, selectedBathRooms, minPrice.text.toString(), maxPrice.text.toString())
            closeFragment()
        }
        closeBtn.setOnClickListener{
            closeFragment();
        }
    }
    private fun getSelectedRadioButtonText(radioGroup: RadioGroup): String {
        val selectedRadioButtonId = radioGroup.checkedRadioButtonId
        return if (selectedRadioButtonId != -1) {
            val selectedRadioButton: RadioButton = view?.findViewById(selectedRadioButtonId) ?: return ""
            selectedRadioButton.text.toString()
        } else {
           return ""
        }
    }
    private fun closeFragment() {
        val fragmentManager = activity?.supportFragmentManager
        if (fragmentManager != null) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(this)


            if (isFragmentInBackStack()) {
                fragmentManager.popBackStack()
            }

            fragmentTransaction.commit()
        }
    }
    private fun isFragmentInBackStack(): Boolean {
        val backStackCount = activity?.supportFragmentManager?.backStackEntryCount ?: 0
        return backStackCount > 0
    }

}
