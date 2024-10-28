package com.example.eventapps.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.eventapps.database.FavoriteEvent
import com.example.eventapps.database.FavoriteEventDao
import com.example.eventapps.database.FinishedEvent
import com.example.eventapps.database.FinishedEventDao
import com.example.eventapps.database.UpcomingEvent
import com.example.eventapps.database.UpcomingEventDao
import com.example.eventapps.remote.response.DetailEventResponse
import com.example.eventapps.remote.response.Event
import com.example.eventapps.remote.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EventRepository(
    private val apiService: ApiService,
    private val favoriteEventDao: FavoriteEventDao,
    private val upcomingEventDao: UpcomingEventDao,
    private val finishedEventDao: FinishedEventDao
) {

    fun getUpcomingEvent(limitFiveData: Boolean? = null, keyword: String? = null): LiveData<Result<List<UpcomingEvent>>> = liveData {
        emit(Result.Loading)
        try {
            val client = when {
                limitFiveData == true -> apiService.getFiveUpcomingEvents()
                keyword != null -> apiService.getSearchedEvents(1,keyword)
                else -> apiService.getUpcomingEvents()
            }
            val response = client.listEvents
            val items = ArrayList<UpcomingEvent>()
            response.map { listEvents ->
                val event = UpcomingEvent(
                    id = listEvents.id,
                    mediaCover =  listEvents.mediaCover,
                    imageLogo = listEvents.imageLogo,
                    name = listEvents.name,
                    ownerName = listEvents.ownerName,
                    beginTime = listEvents.beginTime,
                    quota = listEvents.quota,
                    registrants = listEvents.registrants,
                    description = listEvents.description,
                    link = listEvents.link
                )
                items.add(event)
            }
            upcomingEventDao.deleteAllUpcomingEvent()
            upcomingEventDao.insertUpcomingEvent(items)
        } catch (e: Exception) {
            Log.d("EventRepository", "getUpcomingEvent: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<UpcomingEvent>>> = upcomingEventDao.getUpcomingEvent().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getFinishedEvent(limitFiveData: Boolean? = null, keyword: String? = null): LiveData<Result<List<FinishedEvent>>> = liveData {
        emit(Result.Loading)
        try {
            val client = when {
                limitFiveData == true -> apiService.getFiveFinishedEvents()
                keyword != null -> apiService.getSearchedEvents(0,keyword)
                else -> apiService.getFinishedEvents()
            }
            val response = client.listEvents
            val items = ArrayList<FinishedEvent>()
            response.map { listEvents ->
                val event = FinishedEvent(
                    id = listEvents.id,
                    mediaCover =  listEvents.mediaCover,
                    imageLogo = listEvents.imageLogo,
                    name = listEvents.name,
                    ownerName = listEvents.ownerName,
                    beginTime = listEvents.beginTime,
                    quota = listEvents.quota,
                    registrants = listEvents.registrants,
                    description = listEvents.description,
                    link = listEvents.link
                )
                items.add(event)
            }
            finishedEventDao.deleteAllFinishedEvent()
            finishedEventDao.insertFinishedEvent(items)
        } catch (e: Exception) {
            Log.d("EventRepository", "getFinishedEvent: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<FinishedEvent>>> = finishedEventDao.getFinishedEvent().map { Result.Success(it) }
        emitSource(localData)
    }


    fun getDetailEvent(id: String): LiveData<Result<DetailEventResponse>> {
        val detailEvent = MediatorLiveData<Result<DetailEventResponse>>()
        detailEvent.value = Result.Loading
        val client = apiService.getDetailEvent(id)
        client.enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(call: Call<DetailEventResponse>, response: Response<DetailEventResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        detailEvent.value = Result.Success(it)
                    } ?: run {
                        detailEvent.value = Result.Error("Response body is null")
                    }
                }
            }
            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                detailEvent.value = Result.Error("Response body is null")
                Log.e("EventRepository", "onFailure: ${t.message.toString()}")
            }
        })

        val localDataFinished = finishedEventDao.getFinishedEventById(id.toInt())
        val localDataUpcoming = upcomingEventDao.getUpcomingEventById(id.toInt())

        detailEvent.addSource(localDataFinished) { finishedEvent ->
            if (finishedEvent != null) {
                detailEvent.value = Result.Success(DetailEventResponse(event = Event(
                    summary = finishedEvent.summary,
                    mediaCover = finishedEvent.mediaCover,
                    registrants = finishedEvent.registrants,
                    imageLogo = finishedEvent.imageLogo,
                    link = finishedEvent.link,
                    description = finishedEvent.description,
                    ownerName = finishedEvent.ownerName,
                    cityName = finishedEvent.cityName,
                    quota = finishedEvent.quota,
                    name = finishedEvent.name,
                    id = finishedEvent.id,
                    beginTime = finishedEvent.beginTime,
                    endTime = finishedEvent.endTime,
                    category = finishedEvent.category
                )))
                detailEvent.removeSource(localDataFinished)
            } else {
                detailEvent.addSource(localDataUpcoming) { upcomingEvent ->
                    upcomingEvent?.let {
                        detailEvent.value = Result.Success(DetailEventResponse(event = Event(
                            summary = it.summary,
                            mediaCover = it.mediaCover,
                            registrants = it.registrants,
                            imageLogo = it.imageLogo,
                            link = it.link,
                            description = it.description,
                            ownerName = it.ownerName,
                            cityName = it.cityName,
                            quota = it.quota,
                            name = it.name,
                            id = it.id,
                            beginTime = it.beginTime,
                            endTime = it.endTime,
                            category = it.category
                        )))
                        detailEvent.removeSource(localDataUpcoming)
                    } ?: run {
                        detailEvent.value = Result.Error("Event not found")
                    }
                }
            }
        }

        return detailEvent
    }


    fun getFavoriteEventById(id: String): LiveData<FavoriteEvent?> = favoriteEventDao.getFavoriteEventById(id)

    suspend fun insertFavoriteEvent(favoriteEvent: FavoriteEvent) {
        favoriteEventDao.insertFavoriteEvent(favoriteEvent)
    }

    suspend fun deleteFavoriteEventById(id: String) {
        favoriteEventDao.deleteFavoriteEventById(id)
    }

    fun getAllFavoriteEvents(): LiveData<List<FavoriteEvent>> = favoriteEventDao.getAllFavoriteEvents()


    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteEventDao: FavoriteEventDao,
            upcomingEventDao: UpcomingEventDao,
            finishedEventDao: FinishedEventDao
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(
                    apiService,
                    favoriteEventDao,
                    upcomingEventDao,
                    finishedEventDao
                )
            }.also { instance = it }
    }
}