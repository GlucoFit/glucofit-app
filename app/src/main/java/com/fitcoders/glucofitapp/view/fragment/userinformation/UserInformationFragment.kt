package com.fitcoders.glucofitapp.view.fragment.userinformation

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.data.UserData
import com.fitcoders.glucofitapp.view.activity.assessment.AssessmentActivity
import com.fitcoders.glucofitapp.view.activity.assessment.AssessmentViewModel
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar


class UserInformationFragment : Fragment() {
    private lateinit var etName: TextInputEditText
    private lateinit var etDob: TextInputEditText
    private lateinit var rgGender: RadioGroup
    private lateinit var etWeight: TextInputEditText
    private lateinit var etHeight: TextInputEditText
    private val viewModel: AssessmentViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_information, container, false)

        etName = view.findViewById(R.id.et_name)
        etDob = view.findViewById(R.id.et_dob)
        rgGender = view.findViewById(R.id.rg_gender)
        etWeight = view.findViewById(R.id.et_weight)
        etHeight = view.findViewById(R.id.et_height)

        etDob.setOnClickListener {
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
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                etDob.setText(selectedDate)
            }, year, month, dayOfMonth)
        datePickerDialog.show()
    }

    fun validateInputs(): Boolean {
        return etName.text.toString().isNotEmpty() &&
                etDob.text.toString().isNotEmpty() &&
                rgGender.checkedRadioButtonId != -1 &&
                etWeight.text.toString().isNotEmpty() &&
                etHeight.text.toString().isNotEmpty()
    }

    fun collectData(): Boolean {
        // Validasi input sebelum mengumpulkan data
        return if (validateInputs()) {
            // Ambil userId dari ViewModel atau sumber lain

            // Membuat objek UserData dari input yang diberikan
            val newUser = UserData(
                name = etName.text.toString(),
                dob = etDob.text.toString(),
                gender = when (rgGender.checkedRadioButtonId) {
                    R.id.rb_male -> "Male"
                    R.id.rb_female -> "Female"
                    else -> "" // Mengatasi kasus ketika tidak ada pilihan gender yang dipilih
                },
                weight = etWeight.text.toString(),
                height = etHeight.text.toString(),
            )

            // Mengirim data yang dikumpulkan ke ViewModel
            viewModel.updateUserInformation(newUser)

            true
        } else {
            // Menampilkan pesan kesalahan jika validasi gagal
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            false
        }
    }
}
