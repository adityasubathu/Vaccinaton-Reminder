package com.adityasubathu.vaccinationreminder

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    private var v: View? = null
    private lateinit var childrenList: MutableList<String>
    private lateinit var dOBList: MutableList<String>
    private var helper: DatabaseHandler? = null
    private var alarm: AlarmManagerClass? = null
    private var listCreator = ListCreator()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = layoutInflater.inflate(R.layout.home_fragment, container, false)
        setHasOptionsMenu(true)
        (Objects.requireNonNull(activity) as MainFragmentHolder).setActionBarTitle("Home")
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lv = v!!.findViewById<ExpandableListView>(R.id.childListView)
        helper = DatabaseHandler(activity, LoginActivitySignUpFragment.activeUsername)
        childrenList = activity?.let { listCreator.getChildrenList(it) }!!
        dOBList = listCreator.getDOBList(activity)
        val adap= ChildListAdapter(activity!!, childrenList, dOBList, DataHolder().vaccineList)
        lv.setAdapter(adap)
        lv.isLongClickable = true
        alarm = AlarmManagerClass()
        lv.onItemLongClickListener = OnItemLongClickListener { _, view, position, _ ->
            val deletedName = adap.getGroup(position) as String
            val builder = AlertDialog.Builder(activity!!, R.style.VaccineReminderMaterialDialogTheme)
            builder.setTitle("Delete $deletedName")
            builder.setMessage("Are you sure you want to delete $deletedName?")
            builder.setPositiveButton("Yes") { _, _ ->
                helper!!.delete(deletedName)
                adap.childrenList.removeAt(position)
                adap.DOBList.removeAt(position)
                adap.nextDateList.removeAt(position)
                adap.notifyDataSetChanged()
                alarm!!.cancelAlarm(activity!!, position)
                val deleteMessageSnackBar = Snackbar.make(view, "$deletedName deleted successfully", Snackbar.LENGTH_SHORT)
                deleteMessageSnackBar.show()
            }
            builder.setNeutralButton("Cancel") { dialog, _ -> dialog.dismiss() }
            builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            builder.show()
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            val mySharedPrefs = activity!!.getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
            val e = mySharedPrefs.edit()
            e.putBoolean("loggedIn", false)
            e.apply()
            val i = Intent(activity, LoginActivity::class.java)
            startActivity(i)
            activity!!.finish()
        }
        return super.onOptionsItemSelected(item)
    }
}

internal class ChildListAdapter
(private val context: Context, var childrenList: MutableList<String>, var DOBList: MutableList<String>,
 private val scheduleVaccinesList: List<String>) : BaseExpandableListAdapter() {
    var nextDateList: MutableList<String> = ArrayList()
    private val alarm = AlarmManagerClass()
    override fun getGroupCount(): Int {
        return childrenList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return scheduleVaccinesList.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return childrenList[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return scheduleVaccinesList[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, v: View, parent: ViewGroup): View {
        val convertView: View
        val inflater = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        convertView = inflater.inflate(R.layout.home_fragment_expandable_listview_adapter_layout, parent, false)
        val childNameTextView = convertView.findViewById<TextView>(R.id.childName)
        val dobViewerTextView = convertView.findViewById<TextView>(R.id.dobViewer)
        val offsetViewerTextView = convertView.findViewById<TextView>(R.id.offsetViewer)
        val vaccineListTextView = convertView.findViewById<TextView>(R.id.nextVaccineList)
        val genderViewerTextView = convertView.findViewById<TextView>(R.id.genderViewer)
        val dataHolder = DataHolder()
        val s1 = DOBList[groupPosition]
        val nextDateMilliseconds = dataHolder.getNextVaccineDate(s1)
        val vaccineList = dataHolder.getVaccineList(context, groupPosition, ", ")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = nextDateMilliseconds
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val creator = ListCreator()
        childNameTextView.text = childrenList[groupPosition]
        dobViewerTextView.text = DOBList[groupPosition]
        genderViewerTextView.text = creator.getGenderList(context)[groupPosition]
        nextDateList.add(formatter.format(calendar.time))
        if (nextDateMilliseconds != 1L) {
            offsetViewerTextView.text = nextDateList[groupPosition]
            vaccineListTextView.text = vaccineList
            val alarmNotUp = PendingIntent.getBroadcast(context, groupPosition, Intent(context, AlarmReceiver::class.java), PendingIntent.FLAG_NO_CREATE) != null
            if (alarmNotUp) {
                Log.d("alarm", "setAlarm called")
                alarm.setAlarm(calendar.timeInMillis, context, groupPosition)
            } else {
                Log.d("alarm", "alarm exists")
            }
        } else {
            offsetViewerTextView.text = ""
            vaccineListTextView.setText(R.string.vacc_complete)
        }
        return convertView
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, v: View, parent: ViewGroup): View {
        val convertView: View
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        convertView = inflater.inflate(R.layout.home_fragment_expandable_listview_sublist_layout, parent, false)
        val indices: TextView
        val vaccineName: TextView
        val date: TextView
        indices = convertView.findViewById(R.id.indices)
        vaccineName = convertView.findViewById(R.id.vaccinesName)
        date = convertView.findViewById(R.id.VaccineDate)
        indices.text = String.format(Locale.getDefault(), "%s", (childPosition + 1).toString())
        vaccineName.text = scheduleVaccinesList[childPosition]
        val creator = ListCreator()
        val dOB = DOBList[groupPosition]
        val nextVaccinesDateList = creator.getFullVaccineDatesList(dOB)
        date.text = nextVaccinesDateList[childPosition]
        return convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }

}