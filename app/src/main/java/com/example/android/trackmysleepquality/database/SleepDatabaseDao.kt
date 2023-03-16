package com.example.android.trackmysleepquality.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
//2.6*****Thu thập và hiển thị dữ liệu*****
// B1:thêm từ khóa suspend vào tất cả các phương pháp ngoại trừ getAllNights()vì Room đã sử dụng
// chuỗi nền cho @Query cụ thể trả về LiveData.

//5.****Tạo DAO***
//interface SleepDatabaseDao được chú thích với @Dao. Tất cả các DAO cần được chú thích bằng từ khóa @Dao
@Dao
interface SleepDatabaseDao{//5
    //thêm chú thích @Insert. Bên dưới @Insert, hãy thêm một hàm insert()
    // lấy một thể hiện của lớp Thực thể SleepNight làm đối số của nó.
    //Roomsẽ tạo tất cả các mã cần thiết để chèn SleepNightvào cơ sở dữ liệu. Khi bạn gọi insert()từ
    // mã Kotlin của mình, Roomhãy thực thi truy vấn SQL để chèn thực thể vào cơ sở dữ liệu.
    @Insert
    suspend fun insert(night: SleepNight)
    @Update
    suspend fun update(night: SleepNight)
    //Không có chú thích thuận tiện cho chức năng còn lại, vì vậy bạn phải sử dụng
    // @Query chú thích và cung cấp các truy vấn SQLite.
    //Lưu ý tệp :key. Bạn sử dụng ký hiệu dấu hai chấm trong truy vấn để tham chiếu các đối số trong hàm.
    @Query("SELECT * from daily_sleep_quality_table WHERE nightId = :key")
    suspend fun get(key: Long): SleepNight?

    @Query("DELETE FROM daily_sleep_quality_table")
    suspend fun clear()

    //Thêm @Query với hàm getTonight(). Đặt SleepNight được trả về bởi getTonight() thành null, để hàm
    //có thể xử lý trường hợp bảng trống. (Bảng trống lúc đầu và sau khi dữ liệu bị xóa.)
    //Để lấy "tonight" từ cơ sở dữ liệu, hãy viết một truy vấn SQLite trả về phần tử đầu tiên của danh sách
    // các kết quả được sắp xếp theo nightId thứ tự giảm dần. Sử dụng LIMIT 1 để chỉ trả về một phần tử.
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
    suspend fun getTonight(): SleepNight?

    //truy vấn SQLite trả về tất cả các cột từ daily_sleep_quality_table, được sắp xếp theo thứ tự giảm dần.
    //getAllNights()Đã trả về một danh sách các thực thể SleepNight dưới dạng LiveData. Room cập nhật thông tin
    // LiveData này cho bạn, nghĩa là bạn chỉ cần lấy dữ liệu một cách rõ ràng một lần.
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC")
    fun getAllNights(): LiveData<List<SleepNight>>
}
