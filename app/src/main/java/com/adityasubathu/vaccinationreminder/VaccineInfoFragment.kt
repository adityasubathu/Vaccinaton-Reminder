package com.adityasubathu.vaccinationreminder

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.*

class VaccineInfoFragment : Fragment() {

    private lateinit var v: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.vaccine_info_fragment, container, false)
        (Objects.requireNonNull(activity) as MainFragmentHolder).setActionBarTitle("Vaccines Info")
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lv = v.findViewById<ListView>(R.id.vaccineListView)
        val weeks: MutableList<String> = ArrayList()
        val dataHolder = DataHolder()
        for (i in 1 until dataHolder.weekList.size) {
            weeks.add(dataHolder.weekList[i].toString())
        }
        val adap = VaccineListAdapter(activity, weeks)
        lv.adapter = adap
    }
}

internal class VaccineListAdapter(private val c: Context?, private val weeks: List<String>) : BaseAdapter() {
    override fun getCount(): Int {
        return weeks.size
    }

    override fun getItem(position: Int): Any {
        return weeks[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, v: View, parent: ViewGroup): View {
        val convertView: View
        val inflater = c!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        convertView = inflater.inflate(R.layout.vaccine_list_adapter, parent, false)
        val dataHolder = DataHolder()
        val vaccineNameTextView = convertView.findViewById<TextView>(R.id.VaccineName)
        val weeksNumberTextView = convertView.findViewById<TextView>(R.id.WeekNumber)
        val indexTextView = convertView.findViewById<TextView>(R.id.index)
        vaccineNameTextView.text = dataHolder.vaccineList[position]
        if (weeks[position] == "1") {
            weeksNumberTextView.text = "Birth"
        } else {
            weeksNumberTextView.text = weeks[position] + " Weeks"
        }
        indexTextView.text = (position + 1).toString()
        return convertView
    }

}