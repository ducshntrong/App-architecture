package com.example.android.trackmysleepquality.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
//2.4****Thêm ViewModel****
class SleepTrackerFragment : Fragment() {

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)

        val application = requireNotNull(this.activity).application//2.4
        //cần tham chiếu đến nguồn dữ liệu của mình thông qua tham chiếu đến DAO.
        //xác định một dataSource. Để tham chiếu đến DAO của cơ sở dữ liệu
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao//2.4
        //tạo một phiên bản của viewModelFactory.
        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)//2.4
        val sleepTrackerViewModel =
            ViewModelProvider(this, viewModelFactory).get(SleepTrackerViewModel::class.java)
        //Đặt hoạt động hiện tại làm chủ sở hữu vòng đời của liên kết
        binding.setLifecycleOwner(this)//2.4

        binding.sleepTrackerViewModel = sleepTrackerViewModel
        return binding.root
    }
}
