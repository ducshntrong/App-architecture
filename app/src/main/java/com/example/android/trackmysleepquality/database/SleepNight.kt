package com.example.android.trackmysleepquality.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//4.****Tạo thực thể SleepNight****
@Entity(tableName = "daily_sleep_quality_table")//4
data class SleepNight(//4
    //nightId là khóa chính
    @PrimaryKey(autoGenerate = true)
    var nightId: Long = 0L,
    //Chú thích các thuộc tính còn lại với @ColumnInfo
    @ColumnInfo(name = "start_time_milli")
    val startTimeMilli: Long = System.currentTimeMillis(),

    //phải khởi tạo thời gian kết thúc. Đặt nó thành thời gian bắt đầu để cho biết chưa có thời gian kết thúc nào được ghi lại.
    @ColumnInfo(name = "end_time_milli")
    var endTimeMilli: Long = startTimeMilli,

    //khởi tạo sleepQuality, vì vậy hãy đặt thành -1, cho biết rằng không có dữ liệu chất lượng nào được thu thập.
    @ColumnInfo(name = "quality_rating")
    var sleepQuality: Int = -1
)