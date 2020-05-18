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
import java.text.SimpleDateFormat
import java.util.*

class AlarmManagerFragment : Fragment() {
    private var v: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.alarm_manager_fragment, container, false)
        (Objects.requireNonNull(activity) as MainFragmentHolder).setActionBarTitle("Alarms")
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lv = v!!.findViewById<ListView>(R.id.alarmManagerFragmentListView)
        val alarmTitleList: MutableList<String> = ArrayList()
        val listCreator = ListCreator()
        val dataHolder = DataHolder()
        for (i in listCreator.getChildrenList(activity!!).indices) {
            alarmTitleList.add(dataHolder.getVaccineList(activity, i, ", ")!!)
        }
        val adapter = AlarmManagerFragmentAdapter(activity, alarmTitleList)
        lv.adapter = adapter
    }
}

internal class AlarmManagerFragmentAdapter(private val context: Context?, private val alarmTitleList: List<String>) : BaseAdapter() {
    private val listCreator = ListCreator()
    override fun getCount(): Int {
        return alarmTitleList.size
    }

    override fun getItem(position: Int): Any {
        return alarmTitleList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        val convertView1: View
        val dataHolder = DataHolder()
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        convertView1 = Objects.requireNonNull(inflater).inflate(R.layout.alarm_manager_fragment_listview_adapter, null)
        val alarmTitleTextView = convertView1.findViewById<TextView>(R.id.alarmTitle)
        val alarmDateTextView = convertView1.findViewById<TextView>(R.id.alarmDate)
        val alarmTriggerTimeTextView = convertView1.findViewById<TextView>(R.id.triggerTimeDisplay)
        val alarmRemainingTimeTextView = convertView1.findViewById<TextView>(R.id.remainingTime)
        val nameOfChildTextView = convertView1.findViewById<TextView>(R.id.alarmChildName)
        val offsetMilliSeconds = dataHolder.getNextVaccineDate(listCreator.getDOBList(context)[position])
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = offsetMilliSeconds
        calendar.add(Calendar.HOUR, 8)
        val dateFormatter = SimpleDateFormat("EEEE MMMM dd, yyyy", Locale.getDefault())
        val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val remainingTime = getRemainingTime(position)
        val date = dateFormatter.format(calendar.time)
        val nextVaccineDate = DataHolder().getNextVaccineDate(ListCreator().getDOBList(context)[position])
        if (nextVaccineDate < System.currentTimeMillis()) {
            alarmTitleTextView.text = "No Alarm needed"
        } else {
            alarmTitleTextView.text = alarmTitleList[position]
            alarmRemainingTimeTextView.text = remainingTime
            alarmDateTextView.text = date
            alarmTriggerTimeTextView.text = timeFormatter.format(calendar.time).toUpperCase(Locale.getDefault())
        }
        nameOfChildTextView.text = listCreator.getChildrenList(context)[position]
        return convertView1
    }

    private fun getRemainingTime(position: Int): String {
        val dataHolder = DataHolder()
        val offsetMilliSeconds = dataHolder.getNextVaccineDate(listCreator.getDOBList(context)[position])
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = offsetMilliSeconds
        calendar.add(Calendar.HOUR, 8)
        val currentTime = Calendar.getInstance()
        currentTime.timeInMillis = System.currentTimeMillis()
        var millis = calendar.timeInMillis - currentTime.timeInMillis
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        val elapsedDays = millis / daysInMilli
        millis %= daysInMilli
        val elapsedHours = millis / hoursInMilli
        millis %= hoursInMilli
        val elapsedMinutes = millis / minutesInMilli
        var days = String.format(Locale.getDefault(), "%s Days:", elapsedDays.toString())
        var hours = String.format(Locale.getDefault(), "%s Hours:", elapsedHours.toString())
        var minutes = String.format(Locale.getDefault(), "%s Minutes", elapsedMinutes.toString())
        if (elapsedDays == 0L) {
            days = ""
        }
        if (elapsedHours == 0L) {
            hours = ""
        }
        if (elapsedMinutes == 0L) {
            minutes = ""
        }
        return String.format(Locale.getDefault(), "%s%s%s", days, hours, minutes)
    }

}