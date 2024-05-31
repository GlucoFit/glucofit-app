package com.fitcoders.glucofitapp.view.fragment.userinformation

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.fitcoders.glucofitapp.R
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar


class UserInformationFragment : Fragment() {

    private lateinit var etDOB: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_information, container, false)

        etDOB = view.findViewById(R.id.et_dob)
        etDOB.setOnClickListener {
            showDatePicker()
        }

        return view
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(),
            DatePickerDialog.OnDateSetListener { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                // Set the selected date to the EditText
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                etDOB.setText(selectedDate)
            }, year, month, dayOfMonth)
        datePickerDialog.show()
    }


}