package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import android.provider.SyncStateContract.Helpers.insert
import android.provider.SyncStateContract.Helpers.update
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.launch

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {
        //2.6)B4*****Hiển thị dữ liệu*****
        //Lấy tất cả các đêm từ cơ sở dữ liệu và gán chúng cho biến nights
        private val nights = database.getAllNights()//2.6)B4
        //thêm mã để chuyển đổi nightsthành tệp nightsString. Sử dụng formatNights()chức năng từ Util.kt
        //Truyền nights vào hàm map() từ lớp Transformations
        //Để có quyền truy cập vào tài nguyên chuỗi của bạn, hãy xác định chức năng ánh xạ là gọi formatNights()
        val nightsString = Transformations.map(nights) { nights ->//2.6)B4
                formatNights(nights, application.resources)
        }

        //Xác định một biến được gọi là tonight để giữ đêm hiện tại. Tạo biến MutableLiveData,
        // vì bạn cần có khả năng quan sát dữ liệu và thay đổi nó.
        private var tonight = MutableLiveData<SleepNight?>()//2.6)B2:Thiết lập coroutines cho hoạt động cơ sở dữ liệu
        //Để khởi tạo biến tonight càng sớm càng tốt, hãy tạo một initkhối bên dưới định nghĩa tonight
        // và gọi initializeTonight()
        init {//2.6)B2
                initializeTonight()
        }
        private fun initializeTonight() {2//2.6)B2
                //Sử dụng viewModelScope.launchđể bắt đầu một coroutine trong ViewModelScope
                viewModelScope.launch {
                        //lấy giá trị cho tonight từ cơ sở dữ liệu bằng cách gọi getTonightFromDatabase()
                        // và gán giá trị cho tonight.value
                        tonight.value = getTonightFromDatabase()
                }
        }
        //Định nghĩa nó là một hàm private suspend trả về một SleepNight không có giá trị
        private suspend fun getTonightFromDatabase(): SleepNight? { //2.6)B2
                //lấy tonight từ cơ sở dữ liệu. Nếu thời gian bắt đầu và thời
                // gian kết thúc không giống nhau, nghĩa là đêm đó đã được hoàn thành, hãy return null.
                // Nếu không, return night.
                var night = database.getTonight()
                if (night?.endTimeMilli != night?.startTimeMilli) {
                        night = null
                }
                return night
        }
        fun onStartTracking() {//2.6)B3Thêm trình xử lý nhấp chuột cho nút Bắt đầu
                // khởi chạy một coroutine trong viewModelScope, bởi vì bạn cần kết quả này để tiếp
                // tục và cập nhật giao diện người dùng
                viewModelScope.launch {
                        //tạo một tệp mới SleepNight, ghi lại thời gian hiện tại làm thời gian bắt đầu.
                        val newNight = SleepNight()

                        //Gọi insert()để chèn newNight vào cơ sở dữ liệu. Bạn sẽ thấy lỗi vì bạn chưa xác
                        // định insert()chức năng tạm dừng này. Lưu ý điều này không giống insert()với
                        // phương thức có cùng tên trong SleepDatabaseDAO.kt
                        insert(newNight)
                        //cập nhật tonight.
                        tonight.value = getTonightFromDatabase()
                }
        }
        private suspend fun insert(night: SleepNight) {//2.6)B3
                // sử dụng DAO để chèn night vào cơ sở dữ liệu.
                database.insert(night)
        }

        fun onStopTracking() {//2.6)B5Thêm trình xử lý nhấp chuột cho nút Dừng
                // Khởi chạy một coroutine trong viewModelScope
                viewModelScope.launch {
                        //cú pháp return@label chỉ định hàm mà từ đó câu lệnh này trả về, trong số một số hàm lồng nhau.
                        val oldNight = tonight.value ?: return@launch
                        //Nếu thời gian kết thúc chưa được đặt, hãy đặt thành endTimeMilli thời gian
                        // hệ thống hiện tại và gọi update()với the night data.
                        oldNight.endTimeMilli = System.currentTimeMillis()
                        update(oldNight)
                }
        }
        private suspend fun update(night: SleepNight) {
                database.update(night)
        }

        fun onClear() {//2.6)Bước 6: Thêm trình xử lý nhấp chuột cho nút Xóa
                viewModelScope.launch {
                        clear()
                        tonight.value = null
                }
        }
        suspend fun clear() {
                database.clear()
        }
}

