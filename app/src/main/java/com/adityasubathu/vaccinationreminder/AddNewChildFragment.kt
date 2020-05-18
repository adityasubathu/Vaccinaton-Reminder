package com.adityasubathu.vaccinationreminder

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.util.*

class AddNewChildFragment : Fragment() {
    private var v: View? = null
    private var selectedGenderId = 0
    private var year = 0
    private var month = 0
    private var day = 0
    private var hour = 0
    private var minute = 0
    private var childNameTextView: TextView? = null
    private var dateOfBirthTextView: TextView? = null
    private var helper: databaseHandler? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.add_new_child_fragment, container, false)
        (Objects.requireNonNull(activity) as MainFragmentHolder).setActionBarTitle("Add Child")
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        helper = databaseHandler(activity, LoginActivitySignUpFragment.activeUsername)
        val childGenderRadioGroup = v!!.findViewById<RadioGroup>(R.id.addChildRadioGroup)
        childGenderRadioGroup.setOnCheckedChangeListener { _, _ -> selectedGenderId = childGenderRadioGroup.checkedRadioButtonId }
        val addChildConfirmButton = v!!.findViewById<Button>(R.id.addChildConfirmButton)
        childNameTextView = v!!.findViewById(R.id.fullNameAddChildField)
        val calenderDialogOpener = v!!.findViewById<ImageView>(R.id.childDOBPicker)
        dateOfBirthTextView = v!!.findViewById(R.id.dateOFBirthAddChildField)
        calenderDialogOpener.setOnClickListener {
            val cal = Calendar.getInstance()
            year = cal[Calendar.YEAR]
            month = cal[Calendar.MONTH]
            day = cal[Calendar.DAY_OF_MONTH]
            hour = cal[Calendar.HOUR_OF_DAY]
            minute = cal[Calendar.MINUTE]
            val dp = activity?.let { it1 ->
                DatePickerDialog(it1, OnDateSetListener { _, y, m, d ->
                    var month = m
                    var mm: String
                    var dd: String
                    mm = (++month).toString()
                    dd = d.toString()
                    if (mm.length == 1) {
                        mm = "0$mm"
                    }
                    if (dd.length == 1) {
                        dd = "0$dd"
                    }
                    dateOfBirthTextView?.text = String.format("%s/%s/%s", dd, mm, y)
                }, year, month, day)
            }
            dp?.show()
        }
        addChildConfirmButton.setOnClickListener {
            val childFullName = childNameTextView?.text.toString()
            val dateOfBirth = dateOfBirthTextView?.text.toString()
            val male = "Male"
            val female = "Female"
            if (childFullName.isEmpty() || dateOfBirth.isEmpty() || selectedGenderId == 0) {
                Toast.makeText(activity, R.string.empty_child_name_toast, Toast.LENGTH_SHORT).show()
            } else {
                var id: Long = 0
                if (selectedGenderId == R.id.male) {
                    id = helper!!.insertData(childFullName, dateOfBirth, male)
                } else if (selectedGenderId == R.id.female) {
                    id = helper!!.insertData(childFullName, dateOfBirth, female)
                }
                if (id <= 0) {
                    Log.e("database", "insertion unsuccessful")
                } else {
                    Log.e("database", "insertion successful")
                    val fm = activity?.supportFragmentManager
                    val ft = fm?.beginTransaction()
                    ft?.replace(R.id.fragment_holder, HomeFragment())
                    ft?.commit()
                }
            }
        }
    }
}