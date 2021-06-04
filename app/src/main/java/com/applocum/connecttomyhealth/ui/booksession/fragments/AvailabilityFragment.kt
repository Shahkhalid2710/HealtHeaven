package com.applocum.connecttomyhealth.ui.booksession.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.booksession.presenters.BookSessionPresenter
import com.applocum.connecttomyhealth.ui.booksession.adapters.AvailableTimeAdapter
import com.applocum.connecttomyhealth.ui.booksession.adapters.SelectSlotAdapter
import com.applocum.connecttomyhealth.ui.booksession.adapters.SessionTypeAdapter
import com.applocum.connecttomyhealth.ui.booksession.models.Common
import com.applocum.connecttomyhealth.ui.booksession.models.SessionType
import com.applocum.connecttomyhealth.ui.booksession.models.Time
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import com.google.android.material.snackbar.Snackbar
import com.prolificinteractive.materialcalendarview.*
import kotlinx.android.synthetic.main.custom_small_progress.view.*
import kotlinx.android.synthetic.main.fragment_availability.*
import kotlinx.android.synthetic.main.fragment_availability.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import javax.inject.Inject
import kotlin.collections.ArrayList


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AvailabilityFragment : Fragment(), OnDateSelectedListener, BookSessionPresenter.View {
    private val mListSessionType: ArrayList<SessionType> = ArrayList()
    private val mListSelectSlot: ArrayList<SessionType> = ArrayList()
    private var sType = ""
    private var sSlot = ""
    private var seleteddate = ""
    lateinit var specialist: Specialist
    lateinit var v: View

    @Inject
    lateinit var presenter: BookSessionPresenter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_availability, container, false)

        MyApplication.getAppContext().component.inject(this)
        presenter.injectview(this)

        specialist = arguments?.getSerializable("specialist") as Specialist

        val sessionType1 = SessionType("Phone Call")
        val sessionType2 = SessionType("Video Call")
        val sessionType3 = SessionType("Face to Face")
        mListSessionType.add(sessionType1)
        mListSessionType.add(sessionType2)
        mListSessionType.add(sessionType3)

        v.rvSessionType.layoutManager = GridLayoutManager(requireActivity(), 3)
        v.rvSessionType.adapter = SessionTypeAdapter(requireActivity(), mListSessionType,
            object : SessionTypeAdapter.ItemClickListner {
                override fun onItemClick(sessionType: SessionType, position: Int) {
                    when (position) {
                        0 -> {
                            sType = "phone_call_appointment"
                            presenter.getTimeSlots(specialist.id, seleteddate, sType, sSlot)
                        }
                        1 -> {
                            sType = "online_appointment"
                            presenter.getTimeSlots(specialist.id, seleteddate, sType, sSlot)
                        }
                        2 -> {
                            sType = "offline_appointment"
                            presenter.getTimeSlots(specialist.id, seleteddate, sType, sSlot)
                        }
                    }
                }
            })

        val sessionType4 = SessionType("10 mins")
        val sessionType5 = SessionType("20 mins")
        val sessionType11 = SessionType("30 mins")

        mListSelectSlot.add(sessionType4)
        mListSelectSlot.add(sessionType5)
        mListSelectSlot.add(sessionType11)
        v.rvSelectSlot.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        v.rvSelectSlot.adapter = SelectSlotAdapter(requireActivity(),mListSelectSlot,
            object : SelectSlotAdapter.ItemClickListner {
                override fun onItemClick(sessionType: SessionType, position: Int) {
                    when (position) {
                        0 -> {
                            sSlot = "10"
                            presenter.getTimeSlots(specialist.id, seleteddate, sType, sSlot)
                        }
                        1 -> {
                            sSlot = "20"
                            presenter.getTimeSlots(specialist.id, seleteddate, sType, sSlot)
                        }
                        2 -> {
                            sSlot = "30"
                            presenter.getTimeSlots(specialist.id, seleteddate, sType, sSlot)
                        }
                    }
                }
            })

        v.calendarView.setOnDateChangedListener(this)
        v.calendarView.addDecorator(PrimeDayDisableDecorator())
        return v
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDateSelected(
        widget: MaterialCalendarView,
        date1: CalendarDay,
        selected: Boolean
    ) {
        var date = date1.date
        val formatter = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy")
        val temp: String = date.date.toString()
        try {
            date = formatter.parse(temp)
            Log.e("dateeeeeee", date.toString() + "")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val formateDate = SimpleDateFormat("yyyy-MM-dd").format(date)
        seleteddate = formateDate
        presenter.getTimeSlots(specialist.id, seleteddate, sType, sSlot)
    }

    override fun getTimeSlot(list: ArrayList<Time>) {
        if (list.isEmpty()) {
            v.rvAvailableTime.visibility = View.GONE
            v.NotAvailabeTime.visibility = View.VISIBLE
        } else {
            val availableTimeAdapter = AvailableTimeAdapter(requireActivity(), list)
            v.NotAvailabeTime.visibility = View.GONE
            v.rvAvailableTime.visibility = View.VISIBLE
            rvAvailableTime.layoutManager = GridLayoutManager(requireActivity(), 4)
            rvAvailableTime.adapter = availableTimeAdapter
            availableTimeAdapter.notifyDataSetChanged()
        }
    }

    override fun displaymessage(message: String) {
        val snackBar = Snackbar.make(llAvailability, message, Snackbar.LENGTH_LONG)
        snackBar.changeFont()
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
        snackBar.show()
    }

    override fun viewProgress(isShow: Boolean) {
        v.progressSmall.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun getPrice(common: Common) {}


    private class PrimeDayDisableDecorator : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            val date = CalendarDay.today()
            return day.isBefore(date)
        }
        override fun decorate(view: DayViewFacade) {
            view.setDaysDisabled(true)
        }
    }

    fun newInstance(specialist: Specialist): AvailabilityFragment {
        val args = Bundle()
        args.putSerializable("specialist", specialist)
        val fragment = AvailabilityFragment()
        fragment.arguments = args
        return fragment
    }
}

