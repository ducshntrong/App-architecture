package com.example.android.trackmysleepquality.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
//6.****Tạo và kiểm tra cơ sở dữ liệu Room****

//Cung cấp SleepNight dưới dạng mục duy nhất có danh sách entities.
//Đặt versionlà 1. Bất cứ khi nào bạn thay đổi lược đồ, bạn sẽ phải tăng số phiên bản.
//Đặt exportSchema thành false, để không giữ các bản sao lưu lịch sử phiên bản lược đồ.
@Database(entities = [SleepNight::class], version = 1, exportSchema = false)//6
abstract class SleepDatabase : RoomDatabase() {//6
    //Cơ sở dữ liệu cần biết về DAO. khai báo một giá trị trừu tượng trả
    // về phần mở rộng SleepDatabaseDao. Bạn có thể có nhiều DAO
    abstract val sleepDatabaseDao: SleepDatabaseDao

    //Đối tượng companion đồng hành cho phép các máy khách truy cập các phương thức để tạo hoặc lấy cơ
    //sở dữ liệu mà không cần khởi tạo lớp. Vì mục đích duy nhất của lớp này là cung cấp cơ sở dữ liệu,
    // nên không có lý do gì để khởi tạo nó.
    companion object {
        //Biến INSTANCEsẽ giữ một tham chiếu đến cơ sở dữ liệu, khi một biến đã được tạo. Điều này giúp
        //bạn tránh phải mở liên tục các kết nối tới cơ sở dữ liệu, điều này rất tốn kém về mặt tính toán.
        @Volatile
        private var INSTANCE: SleepDatabase? = null
        //xác định phương thức getInstance() có Context tham số mà trình tạo cơ sở dữ liệu sẽ cần.
        // Trả lại một loại SleepDatabase
        fun getInstance(context: Context): SleepDatabase {//Tạo một lớp trừu tượng có getInstance()chức năng trả về cơ sở dữ liệu.
            //thêm một synchronized{}khối. Chuyển vào this để có thể truy cập ngữ cảnh
            //Gói mã để đưa cơ sở dữ liệu vào synchronizedcó nghĩa là mỗi lần chỉ có một luồng thực
            // thi có thể nhập khối mã này, điều này đảm bảo cơ sở dữ liệu chỉ được khởi tạo một lần.
            synchronized(this) {
                var instance = INSTANCE
                //kiểm tra xem instancecó null hay không tức là chưa có cơ sở dữ liệu.
                if (instance == null) {
                    //gọi Room.databaseBuildervà cung cấp ngữ cảnh mà bạn đã chuyển vào,
                    // lớp cơ sở dữ liệu và tên cho cơ sở dữ liệu, sleep_history_database
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SleepDatabase::class.java,
                        "sleep_history_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}