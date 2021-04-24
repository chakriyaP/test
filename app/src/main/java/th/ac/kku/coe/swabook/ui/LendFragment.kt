package th.ac.kku.coe.swabook.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import th.ac.kku.coe.swabook.R
import timber.log.Timber
import java.util.*

class LendFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var mView: View

    var day = 0
    var month = 0
    var year = 0
//    var hour = 0
//    var minute = 0

    var savedDay = 0
    var saveMonth = 0
    var saveYear = 0
//    var saveHour = 0
//    var saveMinute = 0


    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    private fun pickDate() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        mView.findViewById<Button>(R.id.select_date).setOnClickListener {
            getDateTimeCalendar()

            this@LendFragment.context?.let { context ->
                DatePickerDialog(context, this, year, month, day).show()
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        saveMonth = (month + 1)
        saveYear = year

        getDateTimeCalendar()
        mView.findViewById<TextView>(R.id.date_view).text = "$savedDay-$saveMonth-$saveYear"
//        TimePickerDialog(context,this,hour,minute,true).show()
    }

    //    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
//        saveHour = hourOfDay
//        saveMinute = minute
//    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_lend, container, false)
        mView.findViewById<Button>(R.id.button_lend).setOnClickListener {
            mView.findNavController().navigate(R.id.action_lendFragment_to_feedFragment)
        }
        setHasOptionsMenu(true)
        Timber.i("onCreateView called")
        pickDate()
        return mView

    }
}